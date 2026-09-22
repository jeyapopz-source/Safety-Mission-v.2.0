package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.ui.MainViewModel
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitizenReportScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    var locationInput by remember { mutableStateOf("Kamarajar Bus Stand Road, Oddanchatram") }
    var pinCodeInput by remember { mutableStateOf("624306") }
    var selectedCategory by remember { mutableStateOf("Road Not Cleaned (சாலை சுத்தம் இல்லை)") }
    var descriptionInput by remember { mutableStateOf("") }
    var photoUri by remember { mutableStateOf<Uri?>(null) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    val reportStatus by viewModel.reportSubmissionStatus.collectAsState()
    val allReports by viewModel.allReports.collectAsState()

    // Android Photo Picker (zero-permission, Play Store compliant)
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        photoUri = uri
    }

    LaunchedEffect(reportStatus) {
        if (reportStatus != null) {
            showSuccessDialog = true
        }
    }

    val categories = listOf(
        "Road Not Cleaned (சாலை சுத்தம் இல்லை)",
        "Waste Accumulation (குப்பை தேக்கம்)",
        "Route Missed (விடுபட்ட பாதை)",
        "Overflowing Waste (வழிந்தோடும் குப்பை)",
        "Sweeper Telemetry Issue (இயந்திர குறைபாடு)",
        "Other Issue (இதர)"
    )

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = {
                showSuccessDialog = false
                viewModel.clearReportStatus()
            },
            icon = { Icon(Icons.Default.CheckCircle, contentDescription = null, tint = SafetyGreenDark, modifier = Modifier.size(36.dp)) },
            title = { Text("புகார் வெற்றிகரமாக பதிவானது!", fontSize = 16.sp, fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text(
                        text = "உங்கள் புகார் SAFETY MISSION TN கண்காணிப்பு தளத்தில் பதிவு செய்யப்பட்டுள்ளது.",
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        color = Color(0xFFEFF6FF),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "Reference No: ${reportStatus ?: "TN-REP-2026-101"}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black,
                            color = SafetyNavy,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "உள்ளாட்சி சாலை பராமரிப்பு குழு மற்றும் ஸ்மார்ட் ஸ்வீப்பர் குழுவினருக்கு அறிவிப்பு அனுப்பப்பட்டுள்ளது.",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSuccessDialog = false
                        viewModel.clearReportStatus()
                        descriptionInput = ""
                        photoUri = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SafetyNavy)
                ) {
                    Text("சரி | OK")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("புகார் பதிவு | Citizen Grievance", fontSize = 16.sp, fontWeight = FontWeight.Bold) },
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
                .testTag("citizen_report_screen"),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                DemoDataBadge()
            }

            // Form Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "பொதுமக்கள் புகார் படிவம் | Issue Submission",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = SafetyNavy
                        )
                        Spacer(modifier = Modifier.height(14.dp))

                        // 1. Location
                        Text("1. இடம் & பகுதி | Location Address", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = locationInput,
                            onValueChange = { locationInput = it },
                            modifier = Modifier.fillMaxWidth().testTag("report_location_input"),
                            placeholder = { Text("Enter street or landmark") },
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // PIN Code
                        Text("அஞ்சல் குறியீடு | PIN Code", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = pinCodeInput,
                            onValueChange = { if (it.length <= 6) pinCodeInput = it },
                            modifier = Modifier.fillMaxWidth().testTag("report_pin_input"),
                            placeholder = { Text("e.g. 624306") },
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp)
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // 2. Problem Category
                        Text("2. புகாரின் வகை | Problem Category", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
                        Spacer(modifier = Modifier.height(6.dp))
                        var expanded by remember { mutableStateOf(false) }
                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = { expanded = it }
                        ) {
                            OutlinedTextField(
                                value = selectedCategory,
                                onValueChange = {},
                                readOnly = true,
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor()
                                    .testTag("report_category_dropdown"),
                                shape = RoundedCornerShape(8.dp)
                            )
                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                categories.forEach { cat ->
                                    DropdownMenuItem(
                                        text = { Text(cat, fontSize = 12.sp) },
                                        onClick = {
                                            selectedCategory = cat
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // 3. Photo Upload (Android Photo Picker)
                        Text("3. புகைப்பட ஆதாரம் | Photo Evidence", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
                        Spacer(modifier = Modifier.height(6.dp))

                        if (photoUri != null) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(140.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .border(1.dp, Color(0xFFCBD5E1), RoundedCornerShape(8.dp))
                            ) {
                                AsyncImage(
                                    model = ImageRequest.Builder(context)
                                        .data(photoUri)
                                        .crossfade(true)
                                        .build(),
                                    contentDescription = "Uploaded evidence",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                                IconButton(
                                    onClick = { photoUri = null },
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(4.dp)
                                        .background(Color(0x99000000), RoundedCornerShape(4.dp))
                                ) {
                                    Icon(Icons.Default.Close, contentDescription = "Remove", tint = Color.White)
                                }
                            }
                        } else {
                            OutlinedButton(
                                onClick = {
                                    photoPickerLauncher.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("upload_photo_button"),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(Icons.Default.AddPhotoAlternate, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("புகைப்படம் சேர்க்க | Pick Photo", fontSize = 12.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // 4. Description
                        Text("4. புகார் விவரம் | Problem Description", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = descriptionInput,
                            onValueChange = { descriptionInput = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                                .testTag("report_description_input"),
                            placeholder = { Text("விவரத்தை உள்ளிடவும் (Provide details of cleaning or road issue)") },
                            shape = RoundedCornerShape(8.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // 5. Submit Button
                        Button(
                            onClick = {
                                if (locationInput.isNotBlank()) {
                                    viewModel.submitReport(
                                        pinCode = pinCodeInput,
                                        address = locationInput,
                                        category = selectedCategory,
                                        description = descriptionInput.ifBlank { "Citizen observed road cleanliness defect." },
                                        photoUri = photoUri?.toString()
                                    )
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("submit_report_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = SafetyGreenDark),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.Send, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("புகாரை சமர்ப்பிக்க | Submit Issue Report", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Reports History Tracking
            item {
                Text(
                    text = "பதிவு செய்யப்பட்ட புகார்கள் | Reported Issues Tracking (${allReports.size})",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = SafetyNavy
                )
            }

            items(allReports) { rep ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = rep.referenceNumber,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black,
                                color = SafetyNavy
                            )

                            val statusColor = when (rep.status) {
                                "Resolved" -> SafetyGreenDark
                                "Action Dispatched" -> Color(0xFF0284C7)
                                "Under Inspection" -> SafetyAmber
                                else -> Color(0xFF64748B)
                            }

                            Surface(
                                color = statusColor.copy(alpha = 0.12f),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = rep.status,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = statusColor,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = rep.category, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                        Text(text = rep.locationAddress, fontSize = 11.sp, color = Color(0xFF475569))
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(text = rep.description, fontSize = 11.sp, color = Color.Gray, maxLines = 2)
                    }
                }
            }
        }
    }
}
