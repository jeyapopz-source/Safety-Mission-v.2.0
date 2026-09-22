package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ui.MainViewModel
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    SafetyMissionApp(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun SafetyMissionApp(viewModel: MainViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash") {
            SplashScreen(
                onNavigateToHome = {
                    navController.navigate("home") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }

        composable("home") {
            HomeScreen(
                onNavigateToCurrentLocation = { navController.navigate("current_location") },
                onNavigateToPinSearch = { navController.navigate("pin_search") },
                onNavigateToVoiceAi = { navController.navigate("voice_ai") },
                onNavigateToRouteMap = { navController.navigate("route_map") },
                onNavigateToMyArea = { navController.navigate("my_area") },
                onNavigateToReportIssue = { navController.navigate("report_issue") },
                onNavigateToGovDashboard = { navController.navigate("gov_dashboard") },
                onNavigateToAbout = { navController.navigate("about") },
                onNavigateToLiveVoice = { navController.navigate("live_voice") },
                onNavigateToMapsGrounding = { navController.navigate("maps_grounding") }
            )
        }

        composable("current_location") {
            CurrentLocationScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onNavigateToRouteMap = { navController.navigate("route_map") },
                onNavigateToMyArea = { navController.navigate("my_area") }
            )
        }

        composable("pin_search") {
            PinCodeSearchScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onNavigateToRouteMap = { navController.navigate("route_map") },
                onNavigateToMyArea = { navController.navigate("my_area") }
            )
        }

        composable("voice_ai") {
            VoiceAiScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onNavigateToLiveVoice = { navController.navigate("live_voice") }
            )
        }

        composable("live_voice") {
            LiveVoiceConversationScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable("route_map") {
            RouteMapScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onNavigateToMapsGrounding = { navController.navigate("maps_grounding") }
            )
        }

        composable("maps_grounding") {
            MapsGroundingScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onNavigateToRouteMap = { navController.navigate("route_map") }
            )
        }

        composable("my_area") {
            MyAreaScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onNavigateToRouteMap = { navController.navigate("route_map") }
            )
        }

        composable("report_issue") {
            CitizenReportScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable("gov_dashboard") {
            GovernmentDashboardScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable("about") {
            AboutScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}
