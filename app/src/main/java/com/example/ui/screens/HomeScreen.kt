package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun HomeScreen(
    onNavigateToCurrentLocation: () -> Unit,
    onNavigateToPinSearch: () -> Unit,
    onNavigateToVoiceAi: () -> Unit,
    onNavigateToRouteMap: () -> Unit,
    onNavigateToMyArea: () -> Unit,
    onNavigateToReportIssue: () -> Unit,
    onNavigateToGovDashboard: () -> Unit,
    onNavigateToAbout: () -> Unit,
    onNavigateToLiveVoice: () -> Unit = {},
    onNavigateToMapsGrounding: () -> Unit = {}
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .testTag("home_screen_content"),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            // Header with prominent Logo, subtitle, slogan & prototype banner
            item {
                AppHeaderWithLogo(compact = false)
            }

            // Quick State Metrics Banner [DEMO DATA]
            item {
                Spacer(modifier = Modifier.height(16.dp))
                StateMetricsSummaryCard(modifier = Modifier.padding(horizontal = 16.dp))
            }

            // Main Menu Buttons (Exactly matching the prompt)
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "முக்கிய சேவைகள் | Key Services",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }

            // 1. 📍 என் தற்போதைய இடம்
            item {
                HomeMenuCard(
                    icon = Icons.Default.MyLocation,
                    titleTamil = "என் தற்போதைய இடம்",
                    titleEnglish = "My Current Location",
                    description = "GPS மூலம் உங்கள் பகுதிக்கான தூய்மை பணி விவரம்",
                    badgeText = "Area GPS",
                    containerColor = Color(0xFFEFF6FF),
                    iconColor = SafetyNavy,
                    testTag = "btn_current_location",
                    onClick = onNavigateToCurrentLocation
                )
            }

            // 2. 📮 PIN Code மூலம் தேடுங்கள்
            item {
                HomeMenuCard(
                    icon = Icons.Default.MarkunreadMailbox,
                    titleTamil = "PIN Code மூலம் தேடுங்கள்",
                    titleEnglish = "Search by PIN Code",
                    description = "உதாரணம்: 624306 (ஒட்டன்சத்திரம், திண்டுக்கல், சென்னை...)",
                    badgeText = "Quick PIN",
                    containerColor = Color(0xFFF0FDF4),
                    iconColor = SafetyGreenDark,
                    testTag = "btn_pin_code_search",
                    onClick = onNavigateToPinSearch
                )
            }

            // 3. 🎙️ Voice AI
            item {
                HomeMenuCard(
                    icon = Icons.Default.Mic,
                    titleTamil = "Voice AI உதவியாளர்",
                    titleEnglish = "AI Voice Assistant (Local DB)",
                    description = "தமிழ் / English குரல் கேள்வி - '624306 PIN Code-ல எவ்வளவு சுத்தம் பண்ணியிருக்காங்க?'",
                    badgeText = "Local DB",
                    containerColor = Color(0xFFFAF5FF),
                    iconColor = Color(0xFF7E22CE),
                    testTag = "btn_voice_ai",
                    onClick = onNavigateToVoiceAi
                )
            }

            // 🎙️⚡ Live Voice Conversations (gemini-3.8-live)
            item {
                HomeMenuCard(
                    icon = Icons.Default.GraphicEq,
                    titleTamil = "Live குரல் உரையாடல்",
                    titleEnglish = "Live Voice Conversation (gemini-3.8-live)",
                    description = "Gemini Live API மூலம் தொடர்ச்சியான இருவழி நேரடி குரல் உரையாடல் | Real-time voice interaction",
                    badgeText = "Live API",
                    containerColor = Color(0xFFF3E8FF),
                    iconColor = Color(0xFF6B21A8),
                    testTag = "btn_live_voice_conversation",
                    onClick = onNavigateToLiveVoice
                )
            }

            // 4. 🗺️ Cleaning Route Map
            item {
                HomeMenuCard(
                    icon = Icons.Default.Map,
                    titleTamil = "Cleaning Route Map",
                    titleEnglish = "Interactive Sweeping Routes",
                    description = "🟢 நிறைவடைந்தது (Completed) | 🔵 இயங்குகிறது (Active) | 🟡 திட்டமிடப்பட்டது",
                    badgeText = "Live Map",
                    containerColor = Color(0xFFF0F9FF),
                    iconColor = SafetyCyan,
                    testTag = "btn_cleaning_route_map",
                    onClick = onNavigateToRouteMap
                )
            }

            // 🌐 Google Maps Grounding AI (gemini-3.5-flash with googleMaps tool)
            item {
                HomeMenuCard(
                    icon = Icons.Default.PinDrop,
                    titleTamil = "Google Maps AI வழித்தட ஆய்வு",
                    titleEnglish = "Google Maps Grounding (gemini-3.5-flash)",
                    description = "Google Maps நேரடி தரவு மூலம் தூய்மை மையங்கள் மற்றும் சாலைகள் ஆய்வு (googleMaps tool)",
                    badgeText = "Maps AI",
                    containerColor = Color(0xFFE0F2FE),
                    iconColor = Color(0xFF0284C7),
                    testTag = "btn_maps_grounding_ai",
                    onClick = onNavigateToMapsGrounding
                )
            }

            // 5. 📊 என் பகுதி
            item {
                HomeMenuCard(
                    icon = Icons.Default.BarChart,
                    titleTamil = "என் பகுதி",
                    titleEnglish = "My Area Overview",
                    description = "சுத்தம் செய்த தூரம், சேகரித்த கழிவு, அடுத்த சுத்தம், வாகனங்கள்",
                    badgeText = "Area Stats",
                    containerColor = Color(0xFFFFFBEB),
                    iconColor = Color(0xFFB45309),
                    testTag = "btn_my_area",
                    onClick = onNavigateToMyArea
                )
            }

            // 6. 🚨 Report an Issue
            item {
                HomeMenuCard(
                    icon = Icons.Default.ReportProblem,
                    titleTamil = "Report an Issue",
                    titleEnglish = "Citizen Grievance Submission",
                    description = "புகார் பதிவு: சாலை சுத்தம் செய்யப்படவில்லை, குப்பை தேக்கம், விடுபட்ட பாதை",
                    badgeText = "Citizen Action",
                    containerColor = Color(0xFFFEF2F2),
                    iconColor = SafetyRed,
                    testTag = "btn_report_issue",
                    onClick = onNavigateToReportIssue
                )
            }

            // Government Monitoring Dashboard
            item {
                Spacer(modifier = Modifier.height(8.dp))
                HomeMenuCard(
                    icon = Icons.Default.Dashboard,
                    titleTamil = "அரசு கண்காணிப்பு முகப்பு",
                    titleEnglish = "Government Monitoring Dashboard",
                    description = "Fleet Telemetry, Live IoT Sweepers, Alerts, District Statistics [PROTOTYPE]",
                    badgeText = "Govt Portal",
                    containerColor = Color(0xFFE0E7FF),
                    iconColor = SafetyNavy,
                    testTag = "btn_gov_dashboard",
                    onClick = onNavigateToGovDashboard
                )
            }

            // 7. ℹ️ About
            item {
                HomeMenuCard(
                    icon = Icons.Default.Info,
                    titleTamil = "திட்டம் பற்றி",
                    titleEnglish = "About SAFETY MISSION TN",
                    description = "திட்டத்தின் குறிக்கோள், IoT தொழில்நுட்பம், அதிகாரப்பூர்வ முத்திரை விளக்கம்",
                    badgeText = "Prototype Info",
                    containerColor = Color(0xFFF1F5F9),
                    iconColor = Color(0xFF334155),
                    testTag = "btn_about",
                    onClick = onNavigateToAbout
                )
            }
        }
    }
}

@Composable
fun StateMetricsSummaryCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "தமிழ்நாடு நேரடி நிலவரம் | TN Live Overview",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = SafetyNavy
                )
                Surface(
                    color = Color(0xFFDCFCE7),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = "IoT Active",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = SafetyGreenDark,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MetricPill(
                    modifier = Modifier.weight(1f),
                    value = "14",
                    label = "Active Sweepers",
                    tamilLabel = "இயங்கும் வாகனங்கள்",
                    color = SafetyNavy
                )
                MetricPill(
                    modifier = Modifier.weight(1f),
                    value = "145.8 km",
                    label = "Cleaned Today",
                    tamilLabel = "சுத்தம் செய்த தூரம்",
                    color = SafetyGreenDark
                )
                MetricPill(
                    modifier = Modifier.weight(1f),
                    value = "2,463 kg",
                    label = "Waste Collected",
                    tamilLabel = "சேகரித்த கழிவு",
                    color = Color(0xFFD97706)
                )
            }
        }
    }
}

@Composable
fun MetricPill(
    modifier: Modifier = Modifier,
    value: String,
    label: String,
    tamilLabel: String,
    color: Color
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        color = color.copy(alpha = 0.08f),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.2f))
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                fontSize = 14.sp,
                fontWeight = FontWeight.Black,
                color = color
            )
            Text(
                text = label,
                fontSize = 9.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.DarkGray
            )
            Text(
                text = tamilLabel,
                fontSize = 8.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun HomeMenuCard(
    icon: ImageVector,
    titleTamil: String,
    titleEnglish: String,
    description: String,
    badgeText: String,
    containerColor: Color,
    iconColor: Color,
    testTag: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 5.dp)
            .testTag(testTag)
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(iconColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = titleEnglish,
                    tint = iconColor,
                    modifier = Modifier.size(26.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = titleTamil,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                    Surface(
                        color = iconColor.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = badgeText,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = iconColor,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Text(
                    text = titleEnglish,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = iconColor
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = description,
                    fontSize = 11.sp,
                    color = Color(0xFF475569),
                    lineHeight = 15.sp
                )
            }

            Spacer(modifier = Modifier.width(6.dp))

            Icon(
                Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Open",
                tint = iconColor.copy(alpha = 0.6f),
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
