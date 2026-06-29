package com.vladislavgrom.weatherinfohelper.presentation.weather

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.vladislavgrom.weatherinfohelper.presentation.theme.BlueBackground
import com.vladislavgrom.weatherinfohelper.presentation.theme.Pink40
import com.vladislavgrom.weatherinfohelper.presentation.theme.Purple40
import com.vladislavgrom.weatherinfohelper.presentation.theme.WeatherInfoHelperTheme
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun WeatherScreen(weatherViewModel: WeatherViewModel = hiltViewModel()) {
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
            WeatherContent(currentState)
        }
    }
}

@Composable
fun WeatherContent(
    weatherState: WeatherState.DataLoaded
) {
    // Волгоград: Ш(48.7138) Д(44.4976)
    WeatherInfoHelperTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Header(
                    headerText = "Погода",
                    modifier = Modifier.padding(innerPadding)
                )
                CurrentWeather(state = weatherState)
                Spacer(Modifier.height(20.dp))
                WeatherPerHour(state = weatherState)
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
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(innerPadding),
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
fun CurrentWeather(
    state: WeatherState.DataLoaded,
    modifier: Modifier = Modifier
) {
    state.weatherData.currentWeatherData?.let { weatherData ->
        Card(
            colors = CardColors(
                containerColor = BlueBackground,
                contentColor = Color.White,
                disabledContainerColor = BlueBackground,
                disabledContentColor = Color.White,
            ),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .padding(horizontal = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val dateFormatter = DateTimeFormatter.ofPattern("d MMMM", Locale.forLanguageTag("ru"))
                Text(
                    "Сегодня ${LocalDateTime.now().format(dateFormatter)}",
                    modifier = Modifier.align(Alignment.End)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "${weatherData.temperatureC} °C",
                    fontSize = 50.sp,
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    weatherData.weatherType.weatherDesc,
                    fontSize = 20.sp,
                )
            }
        }
    }
}

@Composable
fun WeatherPerHour(
    state: WeatherState.DataLoaded,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val currentHour = LocalDateTime.now().hour
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
    ) {
        Text(
            "Сегодня",
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(5.dp))
        state.weatherData.weatherDataPerDay[0]?.let { weatherData ->
            LazyRow(
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                state = listState,
                content = {
                    items(weatherData) { weatherData ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text(
                                weatherData.time.format(
                                    DateTimeFormatter.ofPattern("HH:mm")
                                ),
                                fontSize = 20.sp,
                                textAlign = TextAlign.Center,
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            Image(
                                painter = painterResource(id = weatherData.weatherType.iconRes),
                                contentDescription = null,
                                modifier = Modifier.width(60.dp)
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(
                                "${weatherData.temperatureC}°C",
                                fontSize = 20.sp,
                                textAlign = TextAlign.Center,
                            )
                        }
                    }
                    coroutineScope.launch {
                        val currentWeatherDataIndex = weatherData.indexOfFirst { it.time.hour == currentHour }
                        listState.scrollToItem(currentWeatherDataIndex)
                    }
                }
            )
        }
    }
}

@Composable
fun Header(
    headerText: String,
    modifier: Modifier = Modifier)
{
    Text(
        headerText,
        fontSize = 40.sp,
        textAlign = TextAlign.Center,
        modifier = modifier
            .fillMaxWidth()
    )
}

@Composable
fun MyButton(
    onClick: () -> Unit
){
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
            "Узнать о погоде"
        )
    }
}