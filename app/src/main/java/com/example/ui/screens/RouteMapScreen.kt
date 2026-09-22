package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.RouteEntity
import com.example.ui.MainViewModel
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteMapScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit,
    onNavigateToMapsGrounding: () -> Unit = {}
) {
    val allRoutes by viewModel.allRoutes.collectAsState()
    var selectedStatusFilter by remember { mutableStateOf("All") }
    var selectedRoute by remember { mutableStateOf<RouteEntity?>(null) }

    val filteredRoutes = remember(allRoutes, selectedStatusFilter) {
        if (selectedStatusFilter == "All") allRoutes
        else allRoutes.filter { it.status.equals(selectedStatusFilter, ignoreCase = true) }
    }

    LaunchedEffect(filteredRoutes) {
        if (selectedRoute == null || !filteredRoutes.contains(selectedRoute)) {
            selectedRoute = filteredRoutes.firstOrNull()
        }
    }

    // Animation for active sweeper route pulse
    val infiniteTransition = rememberInfiniteTransition(label = "sweeperPulse")
    val pulseProgress by infiniteTransition.animateFloat(
        initialValue = 0.1f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(2800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "activeSweeperMove"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cleaning Route Map", fontSize = 16.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("back_button")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onNavigateToMapsGrounding) {
                        Icon(Icons.Default.Explore, contentDescription = "Maps Grounding AI", tint = Color.White)
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
                .testTag("route_map_screen")
        ) {
            // Top Filter & Legend bar
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF1F5F9))
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "பாதை நிலை வடிகட்டி | Route Status:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = SafetyNavy
                    )
                    DemoDataBadge()
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Legend / Filters mandated by user prompt:
                // 🟢 Completed, 🔵 Active, 🟡 Scheduled
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    item {
                        FilterChip(
                            selected = (selectedStatusFilter == "All"),
                            onClick = { selectedStatusFilter = "All" },
                            label = { Text("அனைத்தும் | All (${allRoutes.size})", fontSize = 11.sp) }
                        )
                    }
                    item {
                        FilterChip(
                            selected = (selectedStatusFilter == "Completed"),
                            onClick = { selectedStatusFilter = "Completed" },
                            label = { Text("🟢 Completed (நிறைவு)", fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = SafetyGreenDark,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                    item {
                        FilterChip(
                            selected = (selectedStatusFilter == "Active"),
                            onClick = { selectedStatusFilter = "Active" },
                            label = { Text("🔵 Active (இயங்குகிறது)", fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = SafetyNavy,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                    item {
                        FilterChip(
                            selected = (selectedStatusFilter == "Scheduled"),
                            onClick = { selectedStatusFilter = "Scheduled" },
                            label = { Text("🟡 Scheduled (திட்டம்)", fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = SafetyAmber,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }

            // Interactive Map Canvas Area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .background(Color(0xFF0F172A))
                    .testTag("route_canvas_map")
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val canvasWidth = size.width
                    val canvasHeight = size.height

                    // Draw grid/city roads background
                    val gridColor = Color(0xFF1E293B)
                    for (i in 0..10) {
                        val y = (canvasHeight / 10f) * i
                        drawLine(
                            color = gridColor,
                            start = Offset(0f, y),
                            end = Offset(canvasWidth, y),
                            strokeWidth = 1f
                        )
                    }
                    for (i in 0..10) {
                        val x = (canvasWidth / 10f) * i
                        drawLine(
                            color = gridColor,
                            start = Offset(x, 0f),
                            end = Offset(x, canvasHeight),
                            strokeWidth = 1f
                        )
                    }

                    // Draw all routes on map
                    filteredRoutes.forEach { route ->
                        val isSelected = route.routeId == selectedRoute?.routeId
                        val routeColor = when (route.status.lowercase()) {
                            "completed" -> Color(0xFF10B981) // Green
                            "active" -> Color(0xFF38BDF8)    // Blue
                            else -> Color(0xFFFBBF24)        // Yellow/Amber
                        }

                        val path = Path()
                        val strokeW = if (isSelected) 10f else 6f

                        // Simple geographic road layout projection based on routeId
                        when (route.routeId) {
                            "RT-624306-01" -> {
                                path.moveTo(canvasWidth * 0.15f, canvasHeight * 0.35f)
                                path.cubicTo(
                                    canvasWidth * 0.25f, canvasHeight * 0.42f,
                                    canvasWidth * 0.4f, canvasHeight * 0.38f,
                                    canvasWidth * 0.55f, canvasHeight * 0.55f
                                )
                            }
                            "RT-624306-02" -> {
                                path.moveTo(canvasWidth * 0.4f, canvasHeight * 0.55f)
                                path.cubicTo(
                                    canvasWidth * 0.55f, canvasHeight * 0.68f,
                                    canvasWidth * 0.7f, canvasHeight * 0.62f,
                                    canvasWidth * 0.82f, canvasHeight * 0.75f
                                )
                            }
                            "RT-624306-03" -> {
                                path.moveTo(canvasWidth * 0.82f, canvasHeight * 0.75f)
                                path.cubicTo(
                                    canvasWidth * 0.88f, canvasHeight * 0.45f,
                                    canvasWidth * 0.72f, canvasHeight * 0.3f,
                                    canvasWidth * 0.6f, canvasHeight * 0.2f
                                )
                            }
                            "RT-625001-01" -> {
                                path.moveTo(canvasWidth * 0.2f, canvasHeight * 0.25f)
                                path.lineTo(canvasWidth * 0.5f, canvasHeight * 0.22f)
                                path.lineTo(canvasWidth * 0.75f, canvasHeight * 0.4f)
                                path.lineTo(canvasWidth * 0.45f, canvasHeight * 0.6f)
                            }
                            else -> {
                                path.moveTo(canvasWidth * 0.3f, canvasHeight * 0.2f)
                                path.lineTo(canvasWidth * 0.45f, canvasHeight * 0.45f)
                                path.lineTo(canvasWidth * 0.6f, canvasHeight * 0.7f)
                                path.lineTo(canvasWidth * 0.75f, canvasHeight * 0.85f)
                            }
                        }

                        if (route.status.equals("Scheduled", ignoreCase = true)) {
                            drawPath(
                                path = path,
                                color = routeColor,
                                style = Stroke(
                                    width = strokeW,
                                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 12f), 0f)
                                )
                            )
                        } else {
                            drawPath(
                                path = path,
                                color = routeColor,
                                style = Stroke(width = strokeW)
                            )
                        }

                        // Draw start marker
                        drawCircle(
                            color = Color.White,
                            radius = if (isSelected) 8f else 5f,
                            center = Offset(canvasWidth * 0.4f, canvasHeight * 0.55f)
                        )
                    }

                    // Animated Active Sweeper simulation icon position
                    val activeRoute = filteredRoutes.find { it.status.equals("Active", ignoreCase = true) }
                    if (activeRoute != null) {
                        val sweeperX = canvasWidth * (0.4f + (0.42f * pulseProgress))
                        val sweeperY = canvasHeight * (0.55f + (0.2f * pulseProgress))

                        drawCircle(
                            color = Color(0x6638BDF8),
                            radius = 24f,
                            center = Offset(sweeperX, sweeperY)
                        )
                        drawCircle(
                            color = Color(0xFF0284C7),
                            radius = 12f,
                            center = Offset(sweeperX, sweeperY)
                        )
                        drawCircle(
                            color = Color.White,
                            radius = 5f,
                            center = Offset(sweeperX, sweeperY)
                        )
                    }
                }

                // Overlay Map Info Pill
                Surface(
                    color = Color(0xCC000000),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp)
                ) {
                    Text(
                        text = "GPS Route Canvas | Live GIS",
                        fontSize = 10.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            // Route Details Inspector (Mandated by user prompt)
            // When Route select: Route ID, Area, Distance, Date, Start Time, End Time, Status
            val route = selectedRoute
            if (route != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .testTag("selected_route_card"),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFCBD5E1)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "தேர்ந்தெடுக்கப்பட்ட பாதை விவரம் | Route Inspector",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = SafetyNavy
                            )

                            val (statusBg, statusFg, statusSymbol) = when (route.status.lowercase()) {
                                "completed" -> Triple(Color(0xFFDCFCE7), SafetyGreenDark, "🟢 Completed")
                                "active" -> Triple(Color(0xFFE0F2FE), SafetyNavy, "🔵 Active Sweeping")
                                else -> Triple(Color(0xFFFEF3C7), Color(0xFF92400E), "🟡 Scheduled")
                            }

                            Surface(
                                color = statusBg,
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = statusSymbol,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Black,
                                    color = statusFg,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Route Name
                        Text(
                            text = route.routeName,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF0F172A)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Prompt-mandated fields:
                        // Route ID, Area, Distance, Date, Start Time, End Time, Status
                        Row(modifier = Modifier.fillMaxWidth()) {
                            InfoColItem(modifier = Modifier.weight(1f), label = "Route ID", value = route.routeId)
                            InfoColItem(modifier = Modifier.weight(1.2f), label = "Area / பகுதி", value = route.area)
                            InfoColItem(modifier = Modifier.weight(0.8f), label = "Distance", value = "${route.distanceKm} km")
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(modifier = Modifier.fillMaxWidth()) {
                            InfoColItem(modifier = Modifier.weight(1f), label = "Date / தேதி", value = route.date)
                            InfoColItem(modifier = Modifier.weight(1f), label = "Start Time", value = route.startTime)
                            InfoColItem(modifier = Modifier.weight(1f), label = "End Time", value = route.endTime)
                        }
                    }
                }
            }

            // Route List to tap and inspect
            Text(
                text = "பாதைகளின் பட்டியல் | Select Route (${filteredRoutes.size})",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = SafetyNavy,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredRoutes) { itemRoute ->
                    val isSelected = (itemRoute.routeId == selectedRoute?.routeId)
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedRoute = itemRoute },
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) Color(0xFFEFF6FF) else MaterialTheme.colorScheme.surface
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = if (isSelected) SafetyNavy else Color(0xFFE2E8F0)
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val dotColor = when (itemRoute.status.lowercase()) {
                                "completed" -> SafetyGreenDark
                                "active" -> Color(0xFF0284C7)
                                else -> SafetyAmber
                            }
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(dotColor)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "${itemRoute.routeId} • ${itemRoute.area}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0F172A)
                                )
                                Text(
                                    text = "${itemRoute.distanceKm} km | ${itemRoute.startTime} - ${itemRoute.endTime}",
                                    fontSize = 11.sp,
                                    color = Color.Gray
                                )
                            }
                            Text(
                                text = itemRoute.status,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = dotColor
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InfoColItem(modifier: Modifier = Modifier, label: String, value: String) {
    Column(modifier = modifier) {
        Text(text = label, fontSize = 9.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
        Text(text = value, fontSize = 11.sp, color = Color(0xFF0F172A), fontWeight = FontWeight.Bold)
    }
}
