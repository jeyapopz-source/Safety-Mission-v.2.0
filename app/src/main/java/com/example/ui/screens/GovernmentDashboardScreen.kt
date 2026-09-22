package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
fun GovernmentDashboardScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    val allMachines by viewModel.allMachines.collectAsState()
    val allRecords by viewModel.allCleaningRecords.collectAsState()
    val allLocations by viewModel.allLocations.collectAsState()
    val allReports by viewModel.allReports.collectAsState()

    var selectedDistrict by remember { mutableStateOf("All Districts") }

    val filteredMachines = remember(allMachines, selectedDistrict) {
        if (selectedDistrict == "All Districts") allMachines
        else {
            val pinsInDistrict = allLocations.filter { it.district.equals(selectedDistrict, ignoreCase = true) }.map { it.pinCode }
            allMachines.filter { it.assignedPinCode in pinsInDistrict }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Government Monitoring Dashboard", fontSize = 16.sp, fontWeight = FontWeight.Bold) },
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
                .testTag("government_dashboard_screen"),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Mandatory Government Prototype Demonstration Branding
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF3C7)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFDE68A))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Verified, contentDescription = null, tint = Color(0xFFB45309), modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Prototype / Government Demonstration Version",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF78350F)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "This application is a demonstration system developed for smart road cleaning monitoring. Important notice: Developed as a high-tech field demonstration prototype. Does not represent official government approval or departmental endorsement.",
                            fontSize = 11.sp,
                            color = Color(0xFF92400E),
                            lineHeight = 15.sp
                        )
                    }
                }
            }

            // State Fleet Overview KPIs
            item {
                Text(
                    text = "மாநில அளவிலான நிகழ்நேர புள்ளிவிவரங்கள் | State Fleet Telemetry",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = SafetyNavy
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    MetricPill(
                        modifier = Modifier.weight(1f),
                        value = "${allMachines.size}",
                        label = "Total Fleet",
                        tamilLabel = "மொத்த இயந்திரங்கள்",
                        color = SafetyNavy
                    )
                    MetricPill(
                        modifier = Modifier.weight(1f),
                        value = "${allMachines.count { it.cleaningModeOn }}",
                        label = "Active Sweepers",
                        tamilLabel = "இயங்கும் வாகனங்கள்",
                        color = SafetyGreenDark
                    )
                    MetricPill(
                        modifier = Modifier.weight(1f),
                        value = "${allReports.size}",
                        label = "Citizen Reports",
                        tamilLabel = "பொதுமக்கள் புகார்",
                        color = SafetyRed
                    )
                }
            }

            // Machines Telemetry Section (Mandated all fields)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "ஸ்மார்ட் ஸ்வீப்பர் டெலிமெட்ரி தரவு (${filteredMachines.size})",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = SafetyNavy
                    )
                    Text(
                        text = "IoT Live Feeds",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }
            }

            items(filteredMachines) { machine ->
                FullMachineTelemetryCard(machine = machine)
            }

            // IoT Telemetry Event Log
            item {
                Text(
                    text = "நிகழ்நேர IoT விழிப்பூட்டல்கள் | Real-Time IoT Events",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = SafetyNavy
                )
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFCBD5E1))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        EventLogItem(
                            time = "10:14 AM",
                            tag = "NORMAL",
                            text = "TN-SWP-04 (Oddanchatram): Telemetry sync OK. Speed: 12.4 km/h. Brushes active."
                        )
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                        EventLogItem(
                            time = "09:45 AM",
                            tag = "STANDBY",
                            text = "TN-SWP-05: Scheduled for night shift 09:30 PM. Battery charge at 94%."
                        )
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                        EventLogItem(
                            time = "09:20 AM",
                            tag = "REPORT",
                            text = "TN-REP-2026-081 (Oddanchatram): Cleaning dispatch alert pushed to machine TN-SWP-04."
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EventLogItem(time: String, tag: String, text: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Text(text = time, fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.width(8.dp))
        Surface(
            color = if (tag == "NORMAL") Color(0xFFDCFCE7) else Color(0xFFFEF3C7),
            shape = RoundedCornerShape(4.dp)
        ) {
            Text(
                text = tag,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color = if (tag == "NORMAL") SafetyGreenDark else Color(0xFF92400E),
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, fontSize = 11.sp, color = Color(0xFF0F172A), modifier = Modifier.weight(1f))
    }
}

/**
 * Displays all Machine Data mandated by prompt:
 * - Machine ID
 * - GPS Location
 * - Speed (km/h)
 * - Total travel distance
 * - Active cleaning distance
 * - Operating hours
 * - Cleaning ON/OFF status
 * - Machine status
 * - Network status
 * - Battery %
 * - Assigned route
 */
@Composable
fun FullMachineTelemetryCard(machine: MachineEntity) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header Row: Machine ID & Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(if (machine.cleaningModeOn) SafetyGreenDark else SafetyAmber)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = machine.machineId,
                        fontSize = 15.sp,
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
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(text = machine.modelName, fontSize = 11.sp, color = Color.Gray)

            Spacer(modifier = Modifier.height(10.dp))

            // Cleaning Mode ON / OFF Switch status indicator
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        if (machine.cleaningModeOn) Color(0xFFF0FDF4) else Color(0xFFFFFBEB),
                        RoundedCornerShape(8.dp)
                    )
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        if (machine.cleaningModeOn) Icons.Default.CleaningServices else Icons.Default.PowerSettingsNew,
                        contentDescription = null,
                        tint = if (machine.cleaningModeOn) SafetyGreenDark else SafetyAmber,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (machine.cleaningModeOn) "Cleaning Mode: ON (Brushes Active)" else "Cleaning Mode: OFF (Standby / Transit)",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (machine.cleaningModeOn) SafetyGreenDark else Color(0xFFB45309)
                    )
                }
                Text(
                    text = "${machine.batteryPercent}% Battery",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (machine.batteryPercent > 20) SafetyGreenDark else SafetyRed
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Grid of mandatory telemetry parameters
            Row(modifier = Modifier.fillMaxWidth()) {
                InfoColItem(modifier = Modifier.weight(1f), label = "Speed (km/h)", value = "${machine.speedKmh} km/h")
                InfoColItem(modifier = Modifier.weight(1.2f), label = "Operating Hours", value = "${machine.operatingHours} hrs")
                InfoColItem(modifier = Modifier.weight(1.2f), label = "Assigned Route", value = machine.assignedRouteId)
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                InfoColItem(modifier = Modifier.weight(1f), label = "Total Travel", value = "${machine.totalTravelDistanceKm} km")
                InfoColItem(modifier = Modifier.weight(1.2f), label = "Active Cleaned", value = "${machine.activeCleaningDistanceKm} km")
                InfoColItem(modifier = Modifier.weight(1.2f), label = "Assigned Area", value = machine.assignedArea)
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                InfoColItem(
                    modifier = Modifier.weight(1.5f),
                    label = "GPS Coordinates",
                    value = "${machine.gpsLat}, ${machine.gpsLng}"
                )
                InfoColItem(
                    modifier = Modifier.weight(1.5f),
                    label = "Network Status",
                    value = machine.networkStatus
                )
            }
        }
    }
}
