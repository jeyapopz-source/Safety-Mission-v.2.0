package com.example.ui.screens

import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiveVoiceConversationScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    val conversationTurns by viewModel.liveConversationTurns.collectAsState()
    val isConnecting by viewModel.isLiveConnecting.collectAsState()
    val modelStatus by viewModel.liveModelStatus.collectAsState()
    val listState = rememberLazyListState()

    var textInput by remember { mutableStateOf("") }

    // Scroll to bottom when conversation advances
    LaunchedEffect(conversationTurns.size) {
        if (conversationTurns.isNotEmpty()) {
            listState.animateScrollToItem(conversationTurns.size - 1)
        }
    }

    // Voice Recognizer for Live Conversation
    val speechLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val spoken = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val first = spoken?.firstOrNull()
            if (!first.isNullOrBlank()) {
                viewModel.sendLiveMessage(first)
            }
        }
    }

    fun startListening() {
        try {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ta-IN")
                putExtra(RecognizerIntent.EXTRA_SUPPORTED_LANGUAGES, arrayListOf("ta-IN", "en-IN"))
                putExtra(RecognizerIntent.EXTRA_PROMPT, "Live API உரையாடல்: பேசுங்கள் | Speak into Live API")
            }
            speechLauncher.launch(intent)
        } catch (e: Exception) {
            // Speech recognizer fallback
        }
    }

    // Infinite animation for audio waveform
    val infiniteTransition = rememberInfiniteTransition(label = "audioWaveform")
    val waveScale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "waveScale"
    )

    val quickTopics = listOf(
        "ஒட்டன்சத்திரம் சாலை தூய்மை நிலை என்ன?",
        "Explain Smart Sweeper telemetry & AI suction",
        "How is municipal waste tracked in 624306?",
        "தமிழ்நாடு முழுவதும் சாலை பாதுகாப்பு திட்டம் பற்றி கூறுங்கள்"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Live Voice Conversation", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(if (isConnecting) SafetyAmber else SafetyGreenDark)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = modelStatus,
                                fontSize = 11.sp,
                                color = Color(0xFFD1FAE5),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("back_button")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.clearLiveConversation() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Clear Chat", tint = Color.White)
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF8FAFC))
                .testTag("live_voice_conversation_screen")
        ) {
            // Top Live Banner
            Surface(
                color = Color(0xFF0F2642),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.GraphicEq, contentDescription = null, tint = SafetyGreenLight, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Live Model: gemini-3.8-live (Live API)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    DemoDataBadge()
                }
            }

            // Quick Topic Suggestion Chips
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(quickTopics) { topic ->
                    SuggestionChip(
                        onClick = { viewModel.sendLiveMessage(topic) },
                        label = { Text(topic, fontSize = 11.sp) }
                    )
                }
            }

            // Conversation Messages Feed
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 12.dp)
            ) {
                items(conversationTurns) { (role, msg) ->
                    val isUser = role == "user"
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
                    ) {
                        Card(
                            shape = RoundedCornerShape(
                                topStart = 16.dp,
                                topEnd = 16.dp,
                                bottomStart = if (isUser) 16.dp else 4.dp,
                                bottomEnd = if (isUser) 4.dp else 16.dp
                            ),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isUser) SafetyNavy else Color.White
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                            modifier = Modifier.widthIn(max = 300.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = if (isUser) "You (குடிமகன் / அதிகாரி)" else "SAFETY MISSION TN (Live API)",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isUser) Color(0xFF93C5FD) else SafetyNavy
                                    )
                                    if (!isUser) {
                                        IconButton(
                                            onClick = { viewModel.speak(msg) },
                                            modifier = Modifier.size(24.dp)
                                        ) {
                                            Icon(
                                                Icons.AutoMirrored.Filled.VolumeUp,
                                                contentDescription = "Speak",
                                                tint = SafetyGreenDark,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = msg,
                                    fontSize = 13.sp,
                                    color = if (isUser) Color.White else Color(0xFF0F172A),
                                    lineHeight = 18.sp
                                )
                            }
                        }
                    }
                }

                if (isConnecting) {
                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(8.dp)
                        ) {
                            CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Live API பதில் உருவாக்குகிறது...", fontSize = 11.sp, color = Color.Gray)
                        }
                    }
                }
            }

            // Bottom Voice & Text Controls
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Animated Live Voice Waveform Bar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(28.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        for (i in 0..12) {
                            val barHeight = if (isConnecting) (12 + (i % 5) * 4 * waveScale).dp else 8.dp
                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 2.dp)
                                    .width(3.dp)
                                    .height(barHeight)
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(if (isConnecting) SafetyGreenLight else SafetyNavy.copy(alpha = 0.3f))
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = textInput,
                            onValueChange = { textInput = it },
                            placeholder = { Text("நேரடி உரை / கேள்வி உள்ளிடவும்...", fontSize = 12.sp) },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("live_text_input"),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        if (textInput.isNotBlank()) {
                            IconButton(
                                onClick = {
                                    viewModel.sendLiveMessage(textInput)
                                    textInput = ""
                                },
                                modifier = Modifier
                                    .size(46.dp)
                                    .background(SafetyNavy, CircleShape)
                            ) {
                                Icon(Icons.Default.Send, contentDescription = "Send", tint = Color.White)
                            }
                        } else {
                            // Big Voice Action Button for Continuous Conversation
                            IconButton(
                                onClick = { startListening() },
                                modifier = Modifier
                                    .size(48.dp)
                                    .scale(if (isConnecting) waveScale else 1f)
                                    .background(if (isConnecting) SafetyAmber else SafetyGreenDark, CircleShape)
                                    .testTag("live_mic_action_button")
                            ) {
                                Icon(Icons.Default.Mic, contentDescription = "Live Voice Mic", tint = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }
}
