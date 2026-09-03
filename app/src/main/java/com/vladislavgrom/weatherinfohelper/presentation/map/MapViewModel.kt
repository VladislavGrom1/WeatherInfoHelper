package com.vladislavgrom.weatherinfohelper.presentation.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vladislavgrom.weatherinfohelper.domain.map.model.MapPlaceInfo
import com.vladislavgrom.weatherinfohelper.domain.map.use_case.SearchPlacesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MapUiState(
    val latitude: Double = 55.751244,
    val longitude: Double = 37.618423,
    val zoom: Float = 14f,
    val markers: List<MapPlaceInfo> = emptyList(),
    val isSearching: Boolean = false
)

@HiltViewModel
class MapViewModel @Inject constructor(
    private val searchPlacesUseCase: SearchPlacesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(MapUiState())
    val state: StateFlow<MapUiState> = _state.asStateFlow()

    fun onSearch(query: String) {
        if (query.isBlank()) return
        viewModelScope.launch {
            _state.update { it.copy(isSearching = true) }
            try {
                val places = searchPlacesUseCase.call(query, _state.value.latitude, _state.value.longitude)
                val firstPlace = places.firstOrNull()
                _state.update {
                    it.copy(
                        markers = places,
                        isSearching = false,
                        latitude = firstPlace?.latitude ?: it.latitude,
                        longitude = firstPlace?.longitude ?: it.longitude
                    )
                }
            } catch (e: Exception) {
                _state.update { it.copy(isSearching = false) }
            }
        }
    }

    fun onCameraMoved(latitude: Double, longitude: Double, zoom: Float) {
        _state.update { it.copy(latitude = latitude, longitude = longitude, zoom = zoom) }
    }
}