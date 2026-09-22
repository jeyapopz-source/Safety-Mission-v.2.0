package com.example.ui

import android.app.Application
import android.content.Context
import android.location.Location
import android.speech.tts.TextToSpeech
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Locale

class MainViewModel(application: Application) : AndroidViewModel(application), TextToSpeech.OnInitListener {

    private val repository: SafetyMissionRepository
    private var tts: TextToSpeech? = null
    private var ttsReady = false

    init {
        val db = AppDatabase.getDatabase(application)
        val dao = db.appDao()
        repository = SafetyMissionRepository(dao)

        viewModelScope.launch(Dispatchers.IO) {
            DemoDataInitializer.seedDemoDataIfEmpty(dao)
        }

        try {
            tts = TextToSpeech(application, this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            ttsReady = true
            // Try setting Tamil or Indian English
            val tamilLocale = Locale("ta", "IN")
            val result = tts?.setLanguage(tamilLocale)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                tts?.setLanguage(Locale("en", "IN"))
            }
        }
    }

    fun speak(text: String) {
        if (ttsReady && tts != null) {
            val isTamil = text.any { it.code in 0x0B80..0x0BFF }
            if (isTamil) {
                tts?.setLanguage(Locale("ta", "IN"))
            } else {
                tts?.setLanguage(Locale("en", "IN"))
            }
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "SafetyMissionTts")
        }
    }

    fun stopSpeaking() {
        tts?.stop()
    }

    // --- State Flows ---
    val allLocations: StateFlow<List<LocationEntity>> = repository.allLocations
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allRoutes: StateFlow<List<RouteEntity>> = repository.allRoutes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allMachines: StateFlow<List<MachineEntity>> = repository.allMachines
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allCleaningRecords: StateFlow<List<CleaningRecordEntity>> = repository.allCleaningRecords
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allSchedules: StateFlow<List<ScheduleEntity>> = repository.allSchedules
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allReports: StateFlow<List<ReportEntity>> = repository.allReports
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- Selected Area / PIN ---
    private val _selectedPin = MutableStateFlow("624306")
    val selectedPin: StateFlow<String> = _selectedPin.asStateFlow()

    fun setSelectedPin(pin: String) {
        _selectedPin.value = pin
    }

    // --- Voice Assistant State ---
    private val _voiceQueryState = MutableStateFlow<VoiceQueryResult?>(null)
    val voiceQueryState: StateFlow<VoiceQueryResult?> = _voiceQueryState.asStateFlow()

    private val _isVoiceProcessing = MutableStateFlow(false)
    val isVoiceProcessing: StateFlow<Boolean> = _isVoiceProcessing.asStateFlow()

    fun askVoiceAssistant(query: String) {
        viewModelScope.launch {
            _isVoiceProcessing.value = true
            val result = repository.processAiVoiceQuery(query)
            _voiceQueryState.value = result
            _isVoiceProcessing.value = false
            speak(result.speechAnswer)
        }
    }

    // --- Citizen Report Submission ---
    private val _reportSubmissionStatus = MutableStateFlow<String?>(null)
    val reportSubmissionStatus: StateFlow<String?> = _reportSubmissionStatus.asStateFlow()

    fun submitReport(
        pinCode: String,
        address: String,
        category: String,
        description: String,
        photoUri: String?
    ) {
        viewModelScope.launch {
            val ref = repository.submitReport(pinCode, address, category, description, photoUri)
            _reportSubmissionStatus.value = ref
        }
    }

    fun clearReportStatus() {
        _reportSubmissionStatus.value = null
    }

    // --- Current Location Simulation / Fetch ---
    private val _currentLocationArea = MutableStateFlow<LocationEntity?>(null)
    val currentLocationArea: StateFlow<LocationEntity?> = _currentLocationArea.asStateFlow()

    fun updateCurrentLocationFromGps(lat: Double, lng: Double) {
        viewModelScope.launch {
            // Find closest location in demo database
            val locations = allLocations.value
            val closest = locations.minByOrNull { loc ->
                val dLat = loc.latitude - lat
                val dLng = loc.longitude - lng
                (dLat * dLat) + (dLng * dLng)
            } ?: locations.firstOrNull()
            _currentLocationArea.value = closest
            closest?.let { _selectedPin.value = it.pinCode }
        }
    }

    // --- Feature 1: Live Voice Conversations (gemini-3.8-live) ---
    private val _liveConversationTurns = MutableStateFlow<List<Pair<String, String>>>(
        listOf(
            "model" to "வணக்கம்! நான் SAFETY MISSION TN-ன் நேரடி குரல் உதவியாளர் (Live Voice AI). ஒட்டன்சத்திரம், திண்டுக்கல், சென்னை உள்ளிட்ட பகுதிகளில் ஸ்மார்ட் ஸ்வீப்பர் தூய்மை பணிகள், சாலை பாதுகாப்பு மற்றும் கழிவு மேலாண்மை குறித்து எதையும் கேட்கலாம்."
        )
    )
    val liveConversationTurns: StateFlow<List<Pair<String, String>>> = _liveConversationTurns.asStateFlow()

    private val _isLiveConnecting = MutableStateFlow(false)
    val isLiveConnecting: StateFlow<Boolean> = _isLiveConnecting.asStateFlow()

    private val _liveModelStatus = MutableStateFlow("gemini-3.8-live (Live API)")
    val liveModelStatus: StateFlow<String> = _liveModelStatus.asStateFlow()

    fun sendLiveMessage(message: String) {
        if (message.isBlank()) return
        val currentHistory = _liveConversationTurns.value
        _liveConversationTurns.value = currentHistory + ("user" to message)
        _isLiveConnecting.value = true

        viewModelScope.launch {
            val response = com.example.ai.GeminiApiClient.converseWithLiveModel(message, currentHistory)
            _liveConversationTurns.value = _liveConversationTurns.value + ("model" to response.text)
            _liveModelStatus.value = response.modelUsed
            _isLiveConnecting.value = false
            speak(response.text)
        }
    }

    fun clearLiveConversation() {
        _liveConversationTurns.value = listOf(
            "model" to "நேரடி உரையாடல் தயார். பேசலாம் | Live API ready. Start speaking."
        )
    }

    // --- Feature 2: Google Maps Grounding (gemini-3.5-flash with googleMaps tool) ---
    private val _mapsGroundingResult = MutableStateFlow<com.example.ai.MapsGroundingResult?>(null)
    val mapsGroundingResult: StateFlow<com.example.ai.MapsGroundingResult?> = _mapsGroundingResult.asStateFlow()

    private val _isMapsGroundingLoading = MutableStateFlow(false)
    val isMapsGroundingLoading: StateFlow<Boolean> = _isMapsGroundingLoading.asStateFlow()

    fun queryMapsGrounding(query: String) {
        viewModelScope.launch {
            _isMapsGroundingLoading.value = true
            val result = com.example.ai.GeminiApiClient.queryWithMapsGrounding(query)
            _mapsGroundingResult.value = result
            _isMapsGroundingLoading.value = false
        }
    }


    override fun onCleared() {
        super.onCleared()
        tts?.stop()
        tts?.shutdown()
    }
}
