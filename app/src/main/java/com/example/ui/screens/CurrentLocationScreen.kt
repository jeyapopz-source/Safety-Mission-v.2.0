package com.example.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.ui.MainViewModel
import com.example.ui.theme.*
import com.google.android.gms.location.LocationServices

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrentLocationScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit,
    onNavigateToRouteMap: () -> Unit,
    onNavigateToMyArea: () -> Unit
) {
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    var isLocating by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
        val coarseGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        hasPermission = fineGranted || coarseGranted
        if (hasPermission) {
            isLocating = true
            try {
                fusedLocationClient.lastLocation.addOnSuccessListener { loc ->
                    isLocating = false
                    if (loc != null) {
                        viewModel.updateCurrentLocationFromGps(loc.latitude, loc.longitude)
                    } else {
                        // Fallback to Oddanchatram 624306 demo coordinates
                        viewModel.updateCurrentLocationFromGps(10.4852, 77.7472)
                    }
                }.addOnFailureListener {
                    isLocating = false
                    viewModel.updateCurrentLocationFromGps(10.4852, 77.7472)
                }
            } catch (e: SecurityException) {
                isLocating = false
            }
        }
    }

    LaunchedEffect(Unit) {
        if (hasPermission) {
            isLocating = true
            try {
                fusedLocationClient.lastLocation.addOnSuccessListener { loc ->
                    isLocating = false
                    if (loc != null) {
                        viewModel.updateCurrentLocationFromGps(loc.latitude, loc.longitude)
                    } else {
                        viewModel.updateCurrentLocationFromGps(10.4852, 77.7472)
                    }
                }.addOnFailureListener {
                    isLocating = false
                    viewModel.updateCurrentLocationFromGps(10.4852, 77.7472)
                }
            } catch (e: SecurityException) {
                isLocating = false
            }
        }
    }

    val currentLocationArea by viewModel.currentLocationArea.collectAsState()
    val allRecords by viewModel.allCleaningRecords.collectAsState()
    val areaRecord = allRecords.find { it.pinCode == (currentLocationArea?.pinCode ?: "624306") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("என் தற்போதைய இடம் | My Location", fontSize = 16.sp, fontWeight = FontWeight.Bold) },
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
                .testTag("current_location_content"),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                DemoDataBadge()
            }

            // Privacy Notice (Mandated by user prompt)
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Security,
                            contentDescription = "Privacy",
                            tint = SafetyGreenDark,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "தனியுரிமை பாதுகாப்பு: உங்கள் சரியான தனிப்பட்ட இருப்பிடம் பகிரங்கமாக காட்டப்படாது. உங்கள் பகுதி அளவிலான (Area-level) தூய்மை விவரங்கள் மட்டுமே கண்காணிக்கப்படும்.",
                            fontSize = 11.sp,
                            color = Color(0xFF166534),
                            lineHeight = 16.sp
                        )
                    }
                }
            }

            // GPS Permission & Status card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.GpsFixed,
                                    contentDescription = null,
                                    tint = if (hasPermission) SafetyGreenDark else SafetyAmber
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (hasPermission) "GPS நிலை: இயக்கப்பட்டது (Active)" else "GPS அனுமதி தேவை",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (hasPermission) SafetyGreenDark else SafetyAmber
                                )
                            }
                            if (isLocating) {
                                CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        if (!hasPermission) {
                            Text(
                                text = "உங்கள் பகுதியை கண்டறிந்து அருகிலுள்ள சாலை ஸ்வீப்பர் இயந்திரங்களின் தூய்மை தகவல்களை பார்க்க GPS அணுகலை அனுமதிக்கவும்.",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Button(
                                onClick = {
                                    permissionLauncher.launch(
                                        arrayOf(
                                            Manifest.permission.ACCESS_FINE_LOCATION,
                                            Manifest.permission.ACCESS_COARSE_LOCATION
                                        )
                                    )
                                },
                                modifier = Modifier.testTag("request_gps_permission_button"),
                                colors = ButtonDefaults.buttonColors(containerColor = SafetyNavy)
                            ) {
                                Icon(Icons.Default.LocationSearching, contentDescription = null)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("GPS அணுகலை இயக்கு | Grant GPS")
                            }
                        } else {
                            Text(
                                text = "உங்கள் சாதனத்தின் GPS தகவல்கள் மூலம் கீழ்வரும் உள்ளாட்சி நிர்வாக பகுதி கண்டறியப்பட்டுள்ளது.",
                                fontSize = 12.sp,
                                color = Color.DarkGray
                            )
                        }
                    }
                }
            }

            // Detected Area Cleaning Info
            item {
                val loc = currentLocationArea
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "கண்டறியப்பட்ட பகுதி | Identified Zone",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = SafetyNavy
                            )
                            Surface(
                                color = SafetyNavy.copy(alpha = 0.12f),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = "PIN: ${loc?.pinCode ?: "624306"}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SafetyNavy,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = loc?.area ?: "Oddanchatram Central Market Corridor",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF0F172A)
                        )

                        Text(
                            text = "${loc?.localBody ?: "Oddanchatram Municipality"}, ${loc?.district ?: "Dindigul"} District",
                            fontSize = 13.sp,
                            color = Color(0xFF334155)
                        )

                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                        // Area Metrics
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            MetricPill(
                                modifier = Modifier.weight(1f),
                                value = "${areaRecord?.distanceCleanedKm ?: 23.4} km",
                                label = "Cleaned Today",
                                tamilLabel = "சுத்தம் செய்த தூரம்",
                                color = SafetyGreenDark
                            )
                            MetricPill(
                                modifier = Modifier.weight(1f),
                                value = "${areaRecord?.wasteCollectedKg ?: 412.5} kg",
                                label = "Waste Collected",
                                tamilLabel = "சேகரித்த கழிவு",
                                color = Color(0xFFD97706)
                            )
                            MetricPill(
                                modifier = Modifier.weight(1f),
                                value = "${areaRecord?.activeMachinesCount ?: 2}",
                                label = "Active Sweepers",
                                tamilLabel = "இயங்கும் வாகனங்கள்",
                                color = SafetyNavy
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Schedule, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "கடைசி சுத்தம்: ${areaRecord?.lastCleaningTime ?: "Today, 08:15 AM"}",
                                fontSize = 12.sp,
                                color = Color(0xFF475569)
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Update, contentDescription = null, tint = SafetyGreenDark, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "அடுத்த சுத்தம்: ${areaRecord?.nextCleaningTime ?: "Today, 09:00 PM (Night Shift)"}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = SafetyGreenDark
                            )
                        }
                    }
                }
            }

            // Quick Actions
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = onNavigateToRouteMap,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("view_route_map_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = SafetyNavy)
                    ) {
                        Icon(Icons.Default.Map, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("பாதை வரைபடம் | Map", fontSize = 12.sp)
                    }

                    OutlinedButton(
                        onClick = onNavigateToMyArea,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("view_my_area_details_button")
                    ) {
                        Icon(Icons.Default.BarChart, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("முழு விவரம் | Details", fontSize = 12.sp)
                    }
                }
            }
        }
    }
}
