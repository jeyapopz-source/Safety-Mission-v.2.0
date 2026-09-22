package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("திட்டம் பற்றி | About Mission", fontSize = 16.sp, fontWeight = FontWeight.Bold) },
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
                .testTag("about_screen"),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Prominent Logo Display
            item {
                Box(
                    modifier = Modifier
                        .size(140.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .border(3.dp, SafetyNavy, CircleShape)
                        .padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_safety_mission_logo),
                        contentDescription = "Official SAFETY MISSION TN Logo",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "SAFETY MISSION TN",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    color = SafetyNavy,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "AI + GPS + IoT Based Smart Cleaning & Public Monitoring Platform",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF334155),
                    textAlign = TextAlign.Center,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Surface(
                    color = Color(0xFF0F5A36),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "சுத்தமான சாலை | பாதுகாப்பான மக்கள் | வெளிப்படையான கண்காணிப்பு",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFD1FAE5),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Mandatory Government Prototype Disclaimer Notice
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF3C7)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFDE68A))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.WarningAmber, contentDescription = null, tint = Color(0xFFB45309))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Prototype / Government Demonstration Version",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF78350F)
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "This application is a demonstration system developed for smart road cleaning monitoring. Developed as a high-tech demonstration prototype. Does not represent official government approval or departmental endorsement.",
                            fontSize = 11.sp,
                            color = Color(0xFF92400E),
                            lineHeight = 16.sp
                        )
                    }
                }
            }

            // Official Logo Elements breakdown (mandated in prompt)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "சின்னத்தின் கூறுகள் | Official Logo Architecture",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = SafetyNavy
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        LogoElementItem(
                            icon = Icons.Default.Public,
                            title = "Tamil Nadu Map (தமிழ்நாடு வரைபடம்)",
                            description = "State-wide municipal coverage across districts, corporations, and town panchayats."
                        )
                        LogoElementItem(
                            icon = Icons.Default.CleaningServices,
                            title = "Smart Road Sweeper (ஸ்மார்ட் சாலை ஸ்வீப்பர்)",
                            description = "Heavy duty electric sweeping machines equipped with automated dust & waste suction."
                        )
                        LogoElementItem(
                            icon = Icons.Default.GpsFixed,
                            title = "GPS / Location Symbol (GPS செயற்கைக்கோள் வழித்தடம்)",
                            description = "Real-time breadcrumb route verification and geofenced street cleaning compliance."
                        )
                        LogoElementItem(
                            icon = Icons.Default.Psychology,
                            title = "AI Neural Engine (செயற்கை நுண்ணறிவு)",
                            description = "Strict verified bilingual voice assistant delivering precise status updates without hallucination."
                        )
                    }
                }
            }

            // Technology Stack
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFCBD5E1))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "தொழில்நுட்ப கட்டமைப்பு | Technology Pillars",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = SafetyNavy
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "• Jetpack Compose & Material 3\n" +
                                    "• Room SQLite Local Verified Database\n" +
                                    "• Android SpeechRecognizer & Text-To-Speech (Tamil & Indian English)\n" +
                                    "• FusedLocationProviderClient Geolocation\n" +
                                    "• 4G/5G IoT Sweeper Telemetry Simulation Protocol",
                            fontSize = 11.sp,
                            color = Color(0xFF334155),
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LogoElementItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(SafetyNavy.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = SafetyNavy, modifier = Modifier.size(18.dp))
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(text = title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
            Text(text = description, fontSize = 11.sp, color = Color(0xFF64748B), lineHeight = 15.sp)
        }
    }
}
