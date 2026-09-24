package com.vladislavgrom.weatherinfohelper.presentation.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.vladislavgrom.weatherinfohelper.presentation.theme.BlueBackground
import com.vladislavgrom.weatherinfohelper.presentation.theme.PrimaryTheme
import com.vladislavgrom.weatherinfohelper.presentation.theme.WeatherInfoHelperTheme
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.InputListener
import java.lang.ref.WeakReference

@Composable
fun MapScreen(
    viewModel: MapViewModel = hiltViewModel(),
    onLocationSelected: (latitude: Double, longitude: Double) -> Unit = { _, _ -> }
) {
    val state by viewModel.state.collectAsState()
    val mapView = rememberMapViewWithLifecycle()
    var query by remember { mutableStateOf("") }

    val inputListener: InputListener = remember {
        object : InputListener {
            override fun onMapTap(map: com.yandex.mapkit.map.Map, point: Point) {
                viewModel.onMapTap(point.latitude, point.longitude)
            }

            override fun onMapLongTap(map: com.yandex.mapkit.map.Map, point: Point) {
                viewModel.onMapTap(point.latitude, point.longitude)
            }
        }
    }

    DisposableEffect(mapView, inputListener) {
        val listenerRef = WeakReference<InputListener>(inputListener)
        mapView.map.addInputListener(listenerRef)
        onDispose { mapView.map.removeInputListener(listenerRef) }
    }

    WeatherInfoHelperTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = PrimaryTheme
        ) { innerPadding ->
            Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                AndroidView(
                    modifier = Modifier.fillMaxSize(),
                    factory = { mapView }
                )
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    shadowElevation = 6.dp,
                    tonalElevation = 2.dp,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    OutlinedTextField(
                        value = query,
                        onValueChange = { query = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Поиск мест") },
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        trailingIcon =  {
                            IconButton(
                                onClick = { viewModel.onSearch(query) },
                                enabled = !state.isSearching
                            ) {
                                Icon(Icons.Default.Search, contentDescription = "Искать")
                            }
                        }
                    )
                }

                val selectedLatitude = state.selectedLatitude
                val selectedLongitude = state.selectedLongitude
                if (selectedLatitude != null && selectedLongitude != null) {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = BlueBackground),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = Color.White
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    text = state.selectedAddress
                                        ?: if (state.isResolvingAddress) "Определяем адрес..." else "Точка на карте",
                                    color = Color.White,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = "%.5f, %.5f".format(selectedLatitude, selectedLongitude),
                                color = Color.White.copy(alpha = 0.7f),
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(start = 32.dp)
                            )
                            Spacer(Modifier.height(12.dp))
                            Button(
                                onClick = { onLocationSelected(selectedLatitude, selectedLongitude) },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Показать погоду здесь")
                            }
                        }
                    }
                }

                LaunchedEffect(state.latitude, state.longitude, state.zoom) {
                    mapView.map.move(
                        CameraPosition(Point(state.latitude, state.longitude), state.zoom, 0f, 0f)
                    )
                }
                LaunchedEffect(state.markers, state.selectedLatitude, state.selectedLongitude) {
                    mapView.map.mapObjects.clear()
                    state.markers.forEach { place ->
                        mapView.map.mapObjects.addPlacemark(Point(place.latitude, place.longitude))
                    }
                    if (selectedLatitude != null && selectedLongitude != null) {
                        mapView.map.mapObjects.addPlacemark(Point(selectedLatitude, selectedLongitude))
                    }
                }
            }
        }
    }
}