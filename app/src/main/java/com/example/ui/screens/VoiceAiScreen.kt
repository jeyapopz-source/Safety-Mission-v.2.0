package com.example.ui.screens

import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.MainViewModel
import com.example.ui.theme.*
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoiceAiScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit,
    onNavigateToLiveVoice: () -> Unit = {}
) {
    var queryText by remember { mutableStateOf("") }
    val voiceResult by viewModel.voiceQueryState.collectAsState()
    val isProcessing by viewModel.isVoiceProcessing.collectAsState()

    // Speech Recognizer launcher
    val speechLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val spokenSpoken = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val firstResult = spokenSpoken?.firstOrNull()
            if (!firstResult.isNullOrBlank()) {
                queryText = firstResult
                viewModel.askVoiceAssistant(firstResult)
            }
        }
    }

    fun startVoiceRecognition() {
        try {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ta-IN")
                putExtra(RecognizerIntent.EXTRA_SUPPORTED_LANGUAGES, arrayListOf("ta-IN", "en-IN"))
                putExtra(RecognizerIntent.EXTRA_PROMPT, "பேசுங்கள் | Speak your question (e.g. 624306 PIN Code சுத்தம்?)")
            }
            speechLauncher.launch(intent)
        } catch (e: Exception) {
            // Speech recognizer not installed or unavailable
        }
    }

    // Auto submit first query on launch if empty
    LaunchedEffect(Unit) {
        if (voiceResult == null) {
            viewModel.askVoiceAssistant("624306 PIN Code-ல எவ்வளவு சுத்தம் பண்ணியிருக்காங்க?")
        }
    }

    val sampleVoiceQueries = listOf(
        "624306 PIN Code-ல எவ்வளவு சுத்தம் பண்ணியிருக்காங்க?",
        "624306 பகுதியில் சேகரிக்கப்பட்ட கழிவு எவ்வளவு?",
        "624306 அடுத்த தூய்மை பணி எப்போது?",
        "ஒட்டன்சத்திரம் பகுதியில் இயங்கும் இயந்திரங்கள் நிலை என்ன?",
        "தமிழ்நாடு முழுவதும் மொத்த தூய்மை நிலவரம் என்ன?",
        "999999 PIN Code தகவல் என்ன?"
    )

    // Pulse animation for mic
    val infiniteTransition = rememberInfiniteTransition(label = "voicePulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "micPulse"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Voice AI உதவியாளர் | Voice AI", fontSize = 16.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("back_button")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = SafetyNavy,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .testTag("voice_ai_screen"),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                DemoDataBadge()
            }

            // Switch to Real-Time Continuous Live Voice Mode
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigateToLiveVoice() }
                        .testTag("btn_switch_to_live_api"),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF3E8FF)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFD8B4FE))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF7E22CE)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.GraphicEq, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Live குரல் உரையாடல் (gemini-3.8-live)",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF581C87)
                                )
                                Text(
                                    text = "இருவழி நேரடி பேச்சு | Continuous Live Voice Conversation",
                                    fontSize = 10.sp,
                                    color = Color(0xFF7E22CE)
                                )
                            }
                        }
                        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color(0xFF7E22CE))
                    }
                }
            }

            // Voice Engine Protocol Card
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFAF5FF)),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE9D5FF))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = Color(0xFF7E22CE), modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Strict Verified Demo DB Protocol (கட்டுப்பாடு)",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF7E22CE)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "AI கற்பனையாக தரவை உருவாக்காது. தரவுத்தளத்தில் உள்ள சரிபார்க்கப்பட்ட தகவல்களை மட்டுமே குரல் மூலம் தெரிவிக்கும். தரவு இல்லையெனில் 'இந்த தகவல் தற்போது demo database-ல் கிடைக்கவில்லை' என்று மட்டுமே கூறும்.",
                            fontSize = 11.sp,
                            color = Color(0xFF581C87),
                            lineHeight = 15.sp
                        )
                    }
                }
            }

            // Mic Interactive Hub
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "குரல் மூலம் கேளுங்கள் | Speak to Voice Assistant",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = SafetyNavy
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        // Big Mic Action Button
                        Box(
                            modifier = Modifier
                                .size(88.dp)
                                .scale(if (isProcessing) pulseScale else 1.0f)
                                .clip(CircleShape)
                                .background(if (isProcessing) SafetyAmber else SafetyNavy)
                                .clickable { startVoiceRecognition() }
                                .testTag("mic_voice_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Mic,
                                contentDescription = "Start Voice Input",
                                tint = Color.White,
                                modifier = Modifier.size(44.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = if (isProcessing) "ஆராய்ச்சி செய்கிறது... | Processing..." else "மைக்-ஐ அழுத்தி கேள்வி கேளுங்கள் (Tamil/English)",
                            fontSize = 11.sp,
                            color = Color.Gray
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Text query fallback input
                        OutlinedTextField(
                            value = queryText,
                            onValueChange = { queryText = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("voice_text_input_field"),
                            placeholder = { Text("அல்லது தட்டச்சு செய்து கேளுங்கள் (Type question)", fontSize = 12.sp) },
                            trailingIcon = {
                                IconButton(onClick = {
                                    if (queryText.isNotBlank()) {
                                        viewModel.askVoiceAssistant(queryText)
                                    }
                                }) {
                                    Icon(Icons.Default.Send, contentDescription = "Send", tint = SafetyNavy)
                                }
                            },
                            shape = RoundedCornerShape(10.dp)
                        )
                    }
                }
            }

            // AI Answer Response Card
            item {
                val res = voiceResult
                if (res != null) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("voice_answer_card"),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (res.isVerifiedData) Color(0xFFF0FDF4) else Color(0xFFFEF2F2)
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (res.isVerifiedData) Color(0xFF86EFAC) else Color(0xFFFECACA)
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = if (res.isVerifiedData) Icons.Default.CheckCircle else Icons.Default.Info,
                                        contentDescription = null,
                                        tint = if (res.isVerifiedData) SafetyGreenDark else SafetyRed,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = res.displayCardTitle,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (res.isVerifiedData) SafetyGreenDark else SafetyRed
                                    )
                                }

                                IconButton(
                                    onClick = { viewModel.speak(res.speechAnswer) },
                                    modifier = Modifier.testTag("replay_voice_button")
                                ) {
                                    Icon(
                                        Icons.AutoMirrored.Filled.VolumeUp,
                                        contentDescription = "Replay Speech",
                                        tint = SafetyNavy
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = res.speechAnswer,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A),
                                lineHeight = 22.sp
                            )

                            HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))

                            Text(
                                text = res.displayDetails,
                                fontSize = 11.sp,
                                color = Color(0xFF334155),
                                lineHeight = 16.sp
                            )
                        }
                    }
                }
            }

            // Sample Questions (Mandated example: “624306 PIN Code-ல எவ்வளவு சுத்தம் பண்ணியிருக்காங்க?”)
            item {
                Text(
                    text = "மாதிரி குரல் கேள்விகள் (Sample Questions to Try):",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = SafetyNavy
                )
            }

            items(sampleVoiceQueries) { sample ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            queryText = sample
                            viewModel.askVoiceAssistant(sample)
                        },
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.RecordVoiceOver, contentDescription = null, tint = SafetyNavy, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = sample,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF0F172A),
                            modifier = Modifier.weight(1f)
                        )
                        Icon(Icons.Default.ArrowForwardIos, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(12.dp))
                    }
                }
            }
        }
    }
}
