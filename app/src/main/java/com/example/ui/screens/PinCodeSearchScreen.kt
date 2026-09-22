package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.LocationEntity
import com.example.ui.MainViewModel
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PinCodeSearchScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit,
    onNavigateToRouteMap: () -> Unit,
    onNavigateToMyArea: () -> Unit
) {
    val allLocations by viewModel.allLocations.collectAsState()
    val allRecords by viewModel.allCleaningRecords.collectAsState()
    val allMachines by viewModel.allMachines.collectAsState()

    var searchQuery by remember { mutableStateOf("624306") }
    var selectedLocation by remember { mutableStateOf<LocationEntity?>(null) }
    val focusManager = LocalFocusManager.current

    // Initialize with 624306 as requested
    LaunchedEffect(allLocations) {
        if (selectedLocation == null && allLocations.isNotEmpty()) {
            selectedLocation = allLocations.find { it.pinCode == "624306" } ?: allLocations.firstOrNull()
        }
    }

    fun executeSearch(pin: String) {
        val trimmed = pin.trim()
        val found = allLocations.find { it.pinCode == trimmed }
        selectedLocation = found
        focusManager.clearFocus()
        if (found != null) {
            viewModel.setSelectedPin(found.pinCode)
        }
    }

    val demoPins = listOf(
        "624306" to "ஒட்டன்சத்திரம் (Oddanchatram)",
        "624001" to "திண்டுக்கல் (Dindigul)",
        "625001" to "மதுரை (Madurai)",
        "600001" to "சென்னை (Chennai)",
        "641001" to "கோவை (Coimbatore)",
        "620001" to "திருச்சி (Trichy)"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("PIN Code மூலம் தேடுங்கள்", fontSize = 16.sp, fontWeight = FontWeight.Bold) },
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
                .testTag("pin_search_content"),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                DemoDataBadge()
            }

            // Input field
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "அஞ்சல் குறியீட்டு எண் (PIN Code)",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = SafetyNavy
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { if (it.length <= 6) searchQuery = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("pin_code_input_field"),
                            placeholder = { Text("Enter 6-digit PIN (e.g. 624306)") },
                            leadingIcon = {
                                Icon(Icons.Default.Search, contentDescription = null, tint = SafetyNavy)
                            },
                            trailingIcon = {
                                if (searchQuery.isNotEmpty()) {
                                    IconButton(onClick = { searchQuery = "" }) {
                                        Icon(Icons.Default.Clear, contentDescription = "Clear")
                                    }
                                }
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Search
                            ),
                            keyboardActions = KeyboardActions(onSearch = { executeSearch(searchQuery) }),
                            singleLine = true,
                            shape = RoundedCornerShape(10.dp)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = { executeSearch(searchQuery) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("search_pin_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = SafetyNavy)
                        ) {
                            Icon(Icons.Default.Search, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("தேடுங்கள் | Search Demo Database")
                        }
                    }
                }
            }

            // Quick Demo PIN chips
            item {
                Text(
                    text = "விரைவு டெமோ PIN எண்கள் (Quick Demo PINs):",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF475569)
                )
                Spacer(modifier = Modifier.height(6.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(demoPins) { (pin, label) ->
                        FilterChip(
                            selected = (searchQuery == pin),
                            onClick = {
                                searchQuery = pin
                                executeSearch(pin)
                            },
                            label = { Text("$pin - $label", fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = SafetyNavy,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }

            // Results Card
            item {
                val loc = selectedLocation
                if (loc != null) {
                    val record = allRecords.find { it.pinCode == loc.pinCode }
                    val machines = allMachines.filter { it.assignedPinCode == loc.pinCode }

                    Card(
                        modifier = Modifier.fillMaxWidth(),
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
                                Text(
                                    text = "அரசு நிர்வாக விவரம் [DEMO DATA]",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SafetyNavy
                                )
                                Surface(
                                    color = SafetyGreenDark,
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = "PIN: ${loc.pinCode}",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Black,
                                        color = Color.White,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Mandatory Fields requested by user:
                            // State, District, Local Body, Village/Town, Area, PIN Code
                            InfoRowItem("மாநிலம் | State", loc.state)
                            InfoRowItem("மாவட்டம் | District", "${loc.district} District")
                            InfoRowItem("உள்ளாட்சி அமைப்பு | Local Body", loc.localBody)
                            InfoRowItem("கிராமம் / நகரம் | Village/Town", loc.villageOrTown)
                            InfoRowItem("பகுதி | Area", loc.area)
                            InfoRowItem("அஞ்சல் குறியீடு | PIN Code", loc.pinCode)

                            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                            Text(
                                text = "தூய்மை பணி நிலை | Smart Sweeper Telemetry",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = SafetyNavy
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                MetricPill(
                                    modifier = Modifier.weight(1f),
                                    value = "${record?.distanceCleanedKm ?: 0.0} km",
                                    label = "Cleaned",
                                    tamilLabel = "தூய்மை தூரம்",
                                    color = SafetyGreenDark
                                )
                                MetricPill(
                                    modifier = Modifier.weight(1f),
                                    value = "${record?.wasteCollectedKg ?: 0.0} kg",
                                    label = "Waste Gathered",
                                    tamilLabel = "கழிவு சேகரிப்பு",
                                    color = Color(0xFFD97706)
                                )
                                MetricPill(
                                    modifier = Modifier.weight(1f),
                                    value = "${machines.size}",
                                    label = "Machines",
                                    tamilLabel = "இயங்கும் வாகனம்",
                                    color = SafetyNavy
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "அடுத்த சுத்தம்: ${record?.nextCleaningTime ?: "To be scheduled"}",
                                fontSize = 12.sp,
                                color = SafetyGreenDark,
                                fontWeight = FontWeight.SemiBold
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Button(
                                    onClick = {
                                        viewModel.setSelectedPin(loc.pinCode)
                                        onNavigateToMyArea()
                                    },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.buttonColors(containerColor = SafetyGreenDark)
                                ) {
                                    Text("என் பகுதி | My Area", fontSize = 12.sp)
                                }

                                OutlinedButton(
                                    onClick = {
                                        viewModel.setSelectedPin(loc.pinCode)
                                        onNavigateToRouteMap()
                                    },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("ரூட் வரைபடம் | Routes", fontSize = 12.sp)
                                }
                            }
                        }
                    }
                } else {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF2F2)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Default.Info,
                                contentDescription = null,
                                tint = SafetyRed,
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "இந்த தகவல் தற்போது demo database-ல் கிடைக்கவில்லை.",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = SafetyRed
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "PIN '$searchQuery' டெமோ தரவுத்தளத்தில் பதிவு செய்யப்படவில்லை. தயவுசெய்து மேலே உள்ள 624306 (ஒட்டன்சத்திரம்) அல்லது பிற பட்டன்களை அழுத்தவும்.",
                                fontSize = 11.sp,
                                color = Color(0xFF7F1D1D),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InfoRowItem(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color(0xFF64748B),
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0F172A),
            modifier = Modifier.weight(1.3f)
        )
    }
}
