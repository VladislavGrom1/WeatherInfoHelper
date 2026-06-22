package com.vladislavgrom.weatherinfohelper.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.vladislavgrom.weatherinfohelper.presentation.theme.Pink40
import com.vladislavgrom.weatherinfohelper.presentation.theme.Purple40
import com.vladislavgrom.weatherinfohelper.presentation.theme.WeatherInfoHelperTheme
import kotlinx.coroutines.launch

@Composable
fun WeatherScreen(weatherViewModel: WeatherViewModel = hiltViewModel()) {
    val weatherState by weatherViewModel.weatherState.collectAsState()
    val scope = rememberCoroutineScope()

    WeatherInfoHelperTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Header(
                    headerText = "Погода",
                    modifier = Modifier.padding(innerPadding)
                )
                /*
                TextFormField(
                    value = weatherViewModel.searchQuery,
                    onValueChange = weatherViewModel::updateSearchQuery
                )
                */
                MyButton(
                    onClick = { scope.launch{ weatherViewModel.getWeatherData() } },
                )

                val nextDayData = weatherState.weatherData?.weatherDataPerDay?.get(1)
                val nextDayTemp = nextDayData?.firstOrNull()?.temperatureC
                val nextDayHum = nextDayData?.firstOrNull()?.humidity
                val nextDayPressure = nextDayData?.firstOrNull()?.pressure
                val nextDayTime = nextDayData?.firstOrNull()?.time
                val nextDayWindSpeed = nextDayData?.firstOrNull()?.windSpeed

                Text(
                    nextDayTemp.toString(),
                    fontSize = 15.sp,
                    textAlign = TextAlign.Center,
                )
                Text(
                    nextDayHum.toString(),
                    fontSize = 15.sp,
                    textAlign = TextAlign.Center,
                )
                Text(
                    nextDayPressure.toString(),
                    fontSize = 15.sp,
                    textAlign = TextAlign.Center,
                )
                Text(
                    nextDayTime.toString(),
                    fontSize = 15.sp,
                    textAlign = TextAlign.Center,
                )
                Text(
                    nextDayWindSpeed.toString(),
                    fontSize = 15.sp,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Composable
fun TextFormField(
    value: String,
    onValueChange: (String) -> Unit,
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Запрос") }
    )
}

@Composable
fun Header(headerText: String, modifier: Modifier = Modifier){
    Text(
        headerText,
        fontSize = 40.sp,
        textAlign = TextAlign.Center,
        modifier = modifier
            .fillMaxWidth()
    )
}

@Composable
fun MyButton(onClick: () -> Unit){
    Button(
        onClick = onClick,
        colors = ButtonColors(
            containerColor = Purple40,
            contentColor = Color.White,
            disabledContentColor = Pink40,
            disabledContainerColor = Color.Green
        ),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentWidth(Alignment.CenterHorizontally)
    ) {
        Text(
            "Получить данные"
        )
    }
}