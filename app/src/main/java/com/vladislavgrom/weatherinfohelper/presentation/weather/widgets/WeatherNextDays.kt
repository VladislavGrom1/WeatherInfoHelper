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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vladislavgrom.weatherinfohelper.presentation.theme.montserrat
import com.vladislavgrom.weatherinfohelper.presentation.weather.WeatherState
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale
import kotlin.collections.component1
import kotlin.collections.component2

@Composable
fun WeatherNextDays(
    state: WeatherState.DataLoaded,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
    ) {
        Text(
            "Следующие 7 дней",
            fontSize = 20.sp,
            fontFamily = montserrat,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(20.dp))
        state.weatherData?.weatherDataPerDay?.let { weatherData ->
            LazyColumn(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                state = listState
            ) {
                items(weatherData.entries.toList()) { (_, dayData) ->
                    val weather = dayData.minByOrNull {
                        kotlin.math.abs(it.time.hour - 12)
                    } ?: return@items
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = when {
                                    weather.time.toLocalDate() == LocalDate.now() -> "Сегодня"
                                    weather.time.toLocalDate() == LocalDate.now().plusDays(1) -> "Завтра"
                                    else -> weather.time.dayOfWeek.getDisplayName(
                                        TextStyle.FULL,
                                        Locale.forLanguageTag("ru")
                                    ).replaceFirstChar {
                                        if (it.isLowerCase()) it.titlecase(Locale.forLanguageTag("ru"))
                                        else it.toString()
                                    }
                                },
                                fontFamily = montserrat,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = weather.time.format(
                                    DateTimeFormatter.ofPattern("dd MMMM", Locale.forLanguageTag("ru"))
                                ),
                                color = Color.Gray,
                                fontSize = 13.sp
                            )
                        }
                        Text(
                            text = "${weather.temperatureC.toInt()}°",
                            modifier = Modifier.padding(end = 24.dp),
                            fontFamily = montserrat,
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp
                        )
                        Image(
                            painter = painterResource(weather.weatherType.iconRes),
                            contentDescription = null,
                            modifier = Modifier.size(42.dp)
                        )
                    }
                }
            }
        }
    }
}