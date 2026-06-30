package com.vladislavgrom.weatherinfohelper.presentation.weather.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.vladislavgrom.weatherinfohelper.R
import com.vladislavgrom.weatherinfohelper.presentation.theme.BlueBackground
import com.vladislavgrom.weatherinfohelper.presentation.theme.Typography
import com.vladislavgrom.weatherinfohelper.presentation.weather.WeatherState
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun CurrentWeatherCard(
    state: WeatherState.DataLoaded,
    modifier: Modifier = Modifier
) {
    state.weatherData?.currentWeatherData?.let { weatherData ->
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
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        state.addressLocation,
                        style = Typography.bodyMedium,
                    )
                    Text(
                        "${LocalDateTime.now().format(dateFormatter)}",
                        style = Typography.bodyMedium,
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "${weatherData.temperatureC} °C",
                    style = Typography.titleMedium
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    weatherData.weatherType.weatherDesc,
                    style = Typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    WeatherDetails(
                        value = weatherData.pressure.toInt(),
                        unit = "Па",
                        icon = ImageVector.vectorResource(R.drawable.ic_pressure),
                        iconColor = Color.White,
                        textStyle = Typography.bodyMedium
                    )
                    WeatherDetails(
                        value = weatherData.windSpeed.toInt(),
                        unit = "м/c",
                        icon = ImageVector.vectorResource(R.drawable.ic_wind),
                        iconColor = Color.White,
                        textStyle = Typography.bodyMedium
                    )
                    WeatherDetails(
                        value = weatherData.humidity.toInt(),
                        unit = "%",
                        icon = ImageVector.vectorResource(R.drawable.ic_drop),
                        iconColor = Color.White,
                        textStyle = Typography.bodyMedium
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}