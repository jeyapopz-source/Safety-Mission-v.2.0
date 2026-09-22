package com.example.ui.screens

import androidx.compose.foundation.background
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
import com.example.data.MachineEntity
import com.example.ui.MainViewModel
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyAreaScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit,
    onNavigateToRouteMap: () -> Unit
) {
    val selectedPin by viewModel.selectedPin.collectAsState()
    val allLocations by viewModel.allLocations.collectAsState()
    val allRecords by viewModel.allCleaningRecords.collectAsState()
    val allMachines by viewModel.allMachines.collectAsState()

    val location = allLocations.find { it.pinCode == selectedPin } ?: allLocations.firstOrNull()
    val record = allRecords.find { it.pinCode == (location?.pinCode ?: "624306") }
    val areaMachines = allMachines.filter { it.assignedPinCode == (location?.pinCode ?: "624306") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("என் பகுதி | My Area", fontSize = 16.sp, fontWeight = FontWeight.Bold) },
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
                .testTag("my_area_content"),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                DemoDataBadge()
            }

            // Quick Area Switcher
            item {
                Text(
                    text = "பகுதியை தேர்ந்தெடுக்கவும் (Select Area):",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF475569)
                )
                Spacer(modifier = Modifier.height(6.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(allLocations) { loc ->
                        FilterChip(
                            selected = (selectedPin == loc.pinCode),
                            onClick = { viewModel.setSelectedPin(loc.pinCode) },
                            label = { Text("${loc.pinCode} - ${loc.villageOrTown}", fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = SafetyNavy,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }

            // Header Hero Card: Area & PIN
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SafetyNavy),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                color = SafetyGreenDark,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "PIN: ${location?.pinCode ?: "624306"}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                            Text(
                                text = "Tamil Nadu State",
                                fontSize = 11.sp,
                                color = Color(0xFF94A3B8)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Area Name
                        Text(
                            text = location?.area ?: "Oddanchatram Market & Bus Corridor",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )

                        Text(
                            text = "${location?.localBody ?: "Oddanchatram Municipality"}, ${location?.district ?: "Dindigul"}",
                            fontSize = 12.sp,
                            color = Color(0xFFE2E8F0)
                        )
                    }
                }
            }

            // Key Metrics mandated by prompt:
            // Distance Cleaned, Waste Collected, Operating Hours, Routes Completed
            item {
                Text(
                    text = "தூய்மை பணி புள்ளிவிவரங்கள் | Cleaning Metrics",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = SafetyNavy
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    MetricBox(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.LinearScale,
                        title = "Distance Cleaned",
                        tamilTitle = "சுத்தம் செய்த தூரம்",
                        value = "${record?.distanceCleanedKm ?: 23.4} km",
                        color = SafetyGreenDark
                    )
                    MetricBox(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.DeleteSweep,
                        title = "Waste Collected",
                        tamilTitle = "சேகரித்த கழிவு",
                        value = "${record?.wasteCollectedKg ?: 412.5} kg",
                        color = Color(0xFFD97706)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    MetricBox(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.Timer,
                        title = "Operating Hours",
                        tamilTitle = "இயக்க நேரம்",
                        value = "${record?.operatingHours ?: 5.2} hrs",
                        color = SafetyNavy
                    )
                    MetricBox(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.Route,
                        title = "Routes Completed",
                        tamilTitle = "முடிக்கப்பட்ட பாதைகள்",
                        value = "${record?.routesCompleted ?: 2} Routes",
                        color = Color(0xFF0284C7)
                    )
                }
            }

            // Last Cleaning & Next Cleaning Schedules (mandated by prompt)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "தூய்மை அட்டவணை | Cleaning Timeline",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = SafetyNavy
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = SafetyGreenDark, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text("கடைசி சுத்தம் | Last Cleaning", fontSize = 11.sp, color = Color.Gray)
                                Text(record?.lastCleaningTime ?: "Today, 08:15 AM", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                            }
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.AccessTimeFilled, contentDescription = null, tint = SafetyAmber, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text("அடுத்த சுத்தம் | Next Cleaning Schedule", fontSize = 11.sp, color = Color.Gray)
                                Text(record?.nextCleaningTime ?: "Today, 09:00 PM (Night Shift)", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = SafetyGreenDark)
                            }
                        }
                    }
                }
            }

            // Active Machines in this Area (mandated by prompt)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "பகுதியில் இயங்கும் இயந்திரங்கள் (${areaMachines.size})",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = SafetyNavy
                    )
                    Text(
                        text = "Active Machines",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }
            }

            if (areaMachines.isEmpty()) {
                item {
                    Text(
                        text = "இப்பகுதியில் தற்போது ஒதுக்கப்பட்ட இயந்திரங்கள் எதுவும் இல்லை.",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            } else {
                items(areaMachines) { machine ->
                    MachineStatusCard(machine = machine)
                }
            }

            // Navigation to Route Map
            item {
                Spacer(modifier = Modifier.height(6.dp))
                Button(
                    onClick = onNavigateToRouteMap,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("view_area_routes_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = SafetyNavy)
                ) {
                    Icon(Icons.Default.Map, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("இப்பகுதிக்கான பாதை வரைபடம் | View Cleaning Route Map")
                }
            }
        }
    }
}

@Composable
fun MetricBox(
    modifier: Modifier = Modifier,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    tamilTitle: String,
    value: String,
    color: Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.08f)),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.2f))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.height(6.dp))
            Text(value, fontSize = 16.sp, fontWeight = FontWeight.Black, color = color)
            Text(title, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
            Text(tamilTitle, fontSize = 9.sp, color = Color.Gray)
        }
    }
}

@Composable
fun MachineStatusCard(machine: MachineEntity) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(RoundedCornerShape(5.dp))
                            .background(if (machine.cleaningModeOn) SafetyGreenDark else SafetyAmber)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = machine.machineId,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black,
                        color = SafetyNavy
                    )
                }
                Surface(
                    color = if (machine.cleaningModeOn) Color(0xFFDCFCE7) else Color(0xFFFEF3C7),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = machine.machineStatus,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (machine.cleaningModeOn) SafetyGreenDark else Color(0xFF92400E),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(text = machine.modelName, fontSize = 11.sp, color = Color(0xFF475569))

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Speed: ${machine.speedKmh} km/h", fontSize = 11.sp, color = Color.DarkGray)
                Text("Cleaned: ${machine.activeCleaningDistanceKm} km", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = SafetyGreenDark)
                Text("Battery: ${machine.batteryPercent}%", fontSize = 11.sp, color = if (machine.batteryPercent > 20) SafetyGreenDark else SafetyRed)
            }
        }
    }
}
