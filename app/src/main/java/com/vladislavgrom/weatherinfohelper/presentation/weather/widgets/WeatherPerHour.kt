package com.vladislavgrom.weatherinfohelper.presentation.weather.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vladislavgrom.weatherinfohelper.presentation.theme.montserrat
import com.vladislavgrom.weatherinfohelper.presentation.weather.WeatherState
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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
            fontFamily = montserrat,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(20.dp))
        state.weatherData?.weatherDataPerDay[0]?.let { weatherData ->
            LazyRow(
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                state = listState,
                content = {
                    items(weatherData) { weatherData ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.height(140.dp)
                        ) {
                            Text(
                                weatherData.time.format(
                                    DateTimeFormatter.ofPattern("HH:mm")
                                ),
                                fontFamily = montserrat,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                            )
                            Image(
                                painter = painterResource(id = weatherData.weatherType.iconRes),
                                contentDescription = null,
                                modifier = Modifier.width(60.dp)
                            )
                            Text(
                                "${weatherData.temperatureC}°C",
                                fontFamily = montserrat,
                                fontWeight = FontWeight.Bold,
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