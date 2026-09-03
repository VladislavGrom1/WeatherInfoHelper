package com.vladislavgrom.weatherinfohelper.presentation.map

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.vladislavgrom.weatherinfohelper.presentation.theme.WeatherInfoHelperTheme
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition

@Composable
fun MapScreen(viewModel: MapViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    val mapView = rememberMapViewWithLifecycle()
    var query by remember { mutableStateOf("") }
    WeatherInfoHelperTheme() {
        Scaffold(modifier = Modifier.fillMaxWidth()) { innerPadding ->
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
            ) {
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    label = { Text("Поиск мест") },
                    trailingIcon = {
                        IconButton(onClick = { viewModel.onSearch(query) }) {
                            Icon(Icons.Default.Search, contentDescription = "Искать")
                        }
                    }
                )
                AndroidView(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    factory = { mapView }
                )
            }
            LaunchedEffect(state.latitude, state.longitude, state.zoom) {
                mapView.map.move(
                    CameraPosition(Point(state.latitude, state.longitude), state.zoom, 0f, 0f)
                )
            }
            LaunchedEffect(state.markers) {
                mapView.map.mapObjects.clear()
                state.markers.forEach { place ->
                    mapView.map.mapObjects.addPlacemark(Point(place.latitude, place.longitude))
                }
            }
        }
    }
}