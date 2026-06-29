package com.vladislavgrom.weatherinfohelper.presentation.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vladislavgrom.weatherinfohelper.domain.location.use_case.GetCurrentLocationUseCase
import com.vladislavgrom.weatherinfohelper.domain.weather.use_case.GetWeatherDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getWeatherDataUseCase: GetWeatherDataUseCase,
    private val getCurrentLocationUseCase: GetCurrentLocationUseCase
) : ViewModel() {

    private val _weatherState = MutableStateFlow<WeatherState>(
        WeatherState.Initial
    )
    val weatherState: StateFlow<WeatherState> = _weatherState.asStateFlow()

    fun onPermissionResult(granted: Boolean) {
        getWeatherData(granted)
    }

    fun showPermissionRequest() {
        _weatherState.value = WeatherState.RequestingPermissions
    }

    fun onPermissionResultAlreadyGranted() {
        getWeatherData(true)
    }

    private fun getWeatherData(isUseGPS: Boolean) {
        _weatherState.value = WeatherState.DataLoading
        viewModelScope.launch {
            try {
                var latitude = 48.7138
                var longitude = 44.4976
                if(isUseGPS) {
                    val location = getCurrentLocationUseCase.call()
                    latitude = location?.latitude ?: latitude
                    longitude = location?.longitude ?: longitude
                }

                val weather = getWeatherDataUseCase.call(
                    latitude = latitude,
                    longitude = longitude
                )

                _weatherState.value = WeatherState.DataLoaded(
                    weatherData = weather,
                    latitude = latitude,
                    longitude = longitude
                )
            } catch (e: Exception) {
                _weatherState.value = WeatherState.Initial
            }
        }
    }
}

//    private val _searchQuery = mutableStateOf("")
//    private var _responseText = mutableStateOf("")
//
//    val searchQuery: String by _searchQuery
//    var responseText: String by _responseText
//
//    private val model = GenerativeModel(
//        modelName = "gemini-3.5-flash",
//        apiKey = "API KEY",
//        generationConfig = generationConfig {
//            temperature = 0.7f
//        }
//    )
//
//    fun updateSearchQuery(query: String) {
//        _searchQuery.value = query
//    }
//
//    suspend fun sendRequest(text: String): String {
//        if (text.isBlank()) return ""
//
//        responseText = "Загрузка..."
//
//        return try {
//            val response = model.generateContent(text)
//            val result = response.text ?: "Пустой ответ от модели"
//            responseText = result
//            result
//        } catch (e: Exception) {
//            val errorMessage = "Ошибка: ${e.localizedMessage ?: "Неизвестная ошибка"}"
//            responseText = errorMessage
//            errorMessage
//        }
//    }