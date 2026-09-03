package com.vladislavgrom.weatherinfohelper.presentation.weather

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.vladislavgrom.weatherinfohelper.presentation.map.MapScreen
import com.vladislavgrom.weatherinfohelper.presentation.theme.BlueBackground
import com.vladislavgrom.weatherinfohelper.presentation.theme.WeatherInfoHelperTheme
import com.vladislavgrom.weatherinfohelper.presentation.theme.montserrat
import com.vladislavgrom.weatherinfohelper.presentation.weather.widgets.CurrentWeatherCard
import com.vladislavgrom.weatherinfohelper.presentation.weather.widgets.WeatherNextDays
import com.vladislavgrom.weatherinfohelper.presentation.weather.widgets.WeatherPerHour


@Composable
fun WeatherScreen(
    weatherViewModel: WeatherViewModel = hiltViewModel(),
    onOpenMap: () -> Unit
) {
    val weatherState by weatherViewModel.weatherState.collectAsState()

    when (val currentState = weatherState) {
        WeatherState.Initial,
        WeatherState.RequestingPermissions -> {
            WeatherLoading("Запрос разрешений")
        }

        WeatherState.DataLoading -> {
            WeatherLoading("Загрузка данных")
        }

        is WeatherState.DataLoaded -> {
            WeatherContent(
                weatherState = currentState,
                onOpenMap = onOpenMap
            )
        }

        is WeatherState.Error -> {
            WeatherError(
                message = currentState.error,
                onRetry = { weatherViewModel.getWeatherData() }
            )
        }
    }
}

@Composable
fun WeatherContent(
    weatherState: WeatherState.DataLoaded,
    onOpenMap: () -> Unit
) {
    // Волгоград: Ш(48.7138) Д(44.4976)
    WeatherInfoHelperTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(innerPadding)
            ) {
                CurrentWeatherCard(state = weatherState)
                Spacer(Modifier.height(20.dp))
                Button(onClick = onOpenMap) {
                    Text("Открыть карту")
                }
                Spacer(Modifier.height(20.dp))
                WeatherPerHour(state = weatherState)
                Spacer(Modifier.height(20.dp))
                WeatherNextDays(state = weatherState)
            }
        }
    }
}

@Composable
fun WeatherLoading(message: String){
    WeatherInfoHelperTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                CircularProgressIndicator()
                Text(
                    message,
                    fontFamily = montserrat,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(innerPadding),
                )
            }
        }
    }
}

@Composable
fun WeatherError(
    message: String,
    onRetry: () -> Unit
) {
    WeatherInfoHelperTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 24.dp)
            ) {
                Text(
                    text = message,
                    fontFamily = montserrat,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(32.dp))
                ElevatedButton(
                    onClick = onRetry,
                    modifier = Modifier
                        .height(56.dp)
                        .width(220.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonColors(
                        containerColor = BlueBackground,
                        contentColor = Color.White,
                        disabledContentColor = Color.White,
                        disabledContainerColor = BlueBackground
                    )
                ) {
                    Text(
                        text = "Повторить",
                        fontFamily = montserrat,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }
    }
}
