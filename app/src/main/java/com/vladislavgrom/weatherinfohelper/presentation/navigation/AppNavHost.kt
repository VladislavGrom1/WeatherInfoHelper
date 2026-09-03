package com.vladislavgrom.weatherinfohelper.presentation.navigation
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vladislavgrom.weatherinfohelper.presentation.map.MapScreen
import com.vladislavgrom.weatherinfohelper.presentation.weather.WeatherScreen
import com.vladislavgrom.weatherinfohelper.presentation.weather.WeatherViewModel

private const val ROUTE_WEATHER = "weather"
private const val ROUTE_MAP = "map"

@Composable
fun AppNavHost(weatherViewModel: WeatherViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = ROUTE_WEATHER) {
        composable(ROUTE_WEATHER) {
            WeatherScreen(
                weatherViewModel = weatherViewModel,
                onOpenMap = { navController.navigate(ROUTE_MAP) }
            )
        }
        composable(ROUTE_MAP) {
            MapScreen()
        }
    }
}