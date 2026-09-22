package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.MainViewModel
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapsGroundingScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit,
    onNavigateToRouteMap: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("Oddanchatram Kamarajar Market Road & Sanitation Hub") }
    val result by viewModel.mapsGroundingResult.collectAsState()
    val isLoading by viewModel.isMapsGroundingLoading.collectAsState()

    // Auto-trigger default query on open if empty
    LaunchedEffect(Unit) {
        if (result == null) {
            viewModel.queryMapsGrounding("Oddanchatram Kamarajar Market Road & Waste Processing Facilities")
        }
    }

    val sampleMapQueries = listOf(
        "Oddanchatram Kamarajar Market Road (PIN 624306)",
        "Dindigul Palani Road Corridor & Rock Fort",
        "Madurai Meenakshi Amman Temple 4 Chithirai Streets",
        "Chennai George Town & Rajaji Salai Port Gate",
        "Coimbatore Gandhipuram Commercial Sweeping Corridor"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Google Maps Grounding AI", fontSize = 16.sp, fontWeight = FontWeight.Bold) },
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
                .testTag("maps_grounding_screen"),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                DemoDataBadge()
            }

            // Google Maps Tool Protocol Badge
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBFDBFE))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.PinDrop, contentDescription = null, tint = SafetyNavy, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Tool: googleMaps Grounding (gemini-3.5-flash)",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black,
                                color = SafetyNavy
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Google Maps தரவுத்தளத்தின் நேரடி புவியியல் தரவு (Geospatial grounding) மூலம் தமிழ்நாட்டின் சாலைகள், கழிவு பரிமாற்ற மையங்கள் மற்றும் நகராட்சி தூய்மை மண்டலங்கள் சரிபார்க்கப்படுகின்றன.",
                            fontSize = 11.sp,
                            color = Color(0xFF1E3A8A),
                            lineHeight = 15.sp
                        )
                    }
                }
            }

            // Search input field
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "சாலை அல்லது பகுதியை தேடுங்கள் | Search Location",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = SafetyNavy
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("maps_grounding_search_input"),
                            placeholder = { Text("Enter area or road (e.g. Oddanchatram 624306)") },
                            leadingIcon = {
                                Icon(Icons.Default.Place, contentDescription = null, tint = SafetyNavy)
                            },
                            trailingIcon = {
                                if (searchQuery.isNotEmpty()) {
                                    IconButton(onClick = { searchQuery = "" }) {
                                        Icon(Icons.Default.Clear, contentDescription = "Clear")
                                    }
                                }
                            },
                            shape = RoundedCornerShape(10.dp)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = { viewModel.queryMapsGrounding(searchQuery) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("maps_grounding_search_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = SafetyNavy)
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(modifier = Modifier.size(18.dp), color = Color.White, strokeWidth = 2.dp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Maps தரவு பெறப்படுகிறது...")
                            } else {
                                Icon(Icons.Default.Search, contentDescription = null)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Google Maps மூலம் தேடுங்கள் | Query Maps Grounding")
                            }
                        }
                    }
                }
            }

            // Quick preset chips
            item {
                Text(
                    text = "மாதிரி பகுதிகள் (Quick Locations):",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF475569)
                )
                Spacer(modifier = Modifier.height(6.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(sampleMapQueries) { q ->
                        FilterChip(
                            selected = (searchQuery == q),
                            onClick = {
                                searchQuery = q
                                viewModel.queryMapsGrounding(q)
                            },
                            label = { Text(q, fontSize = 11.sp) }
                        )
                    }
                }
            }

            // Grounding Results Card
            item {
                val res = result
                if (res != null) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("maps_grounding_result_card"),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFCBD5E1)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Map, contentDescription = null, tint = SafetyGreenDark, modifier = Modifier.size(20.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Maps Grounded Intelligence",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = SafetyGreenDark
                                    )
                                }

                                Surface(
                                    color = Color(0xFFDCFCE7),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = "Google Maps Verified",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = SafetyGreenDark,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = res.content,
                                fontSize = 13.sp,
                                color = Color(0xFF0F172A),
                                lineHeight = 20.sp
                            )

                            if (res.sources.isNotEmpty()) {
                                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                                Text(
                                    text = "சரிபார்க்கப்பட்ட ஆதாரங்கள் | Grounding Citations:",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SafetyNavy
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                res.sources.forEach { src ->
                                    Text(
                                        text = "• $src",
                                        fontSize = 10.sp,
                                        color = Color(0xFF475569)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Button(
                                onClick = onNavigateToRouteMap,
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = SafetyNavy)
                            ) {
                                Icon(Icons.Default.Route, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("இந்த பகுதியை ரூட் மேப்பில் பார்க்க | Open in Route Map")
                            }
                        }
                    }
                }
            }
        }
    }
}
