package com.vladislavgrom.weatherinfohelper.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vladislavgrom.weatherinfohelper.domain.weather.use_case.GetWeatherDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getWeatherDataUseCase: GetWeatherDataUseCase
) : ViewModel() {

    private val _weatherState = MutableStateFlow(WeatherState())
    val weatherState: StateFlow<WeatherState> = _weatherState.asStateFlow()

    fun getWeatherData() {
        viewModelScope.launch {
            val result = getWeatherDataUseCase.call(latitude = 48.7138, longitude = 44.4976)
            _weatherState.value = WeatherState(
                weatherData = result
            )
        }
    }
    /*
    private val _searchQuery = mutableStateOf("")
    private val _weatherState = MutableStateFlow(WeatherState())
    private var _responseText = mutableStateOf("")

    val searchQuery: String by _searchQuery
    val weatherState: StateFlow<WeatherState> = _weatherState.asStateFlow()
    var responseText: String by _responseText

    private val model = GenerativeModel(
        modelName = "gemini-3.5-flash",
        apiKey = "API KEY",
        generationConfig = generationConfig {
            temperature = 0.7f
        }
    )

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    suspend fun sendRequest(text: String): String {
        if (text.isBlank()) return ""

        responseText = "Загрузка..."

        return try {
            val response = model.generateContent(text)
            val result = response.text ?: "Пустой ответ от модели"
            responseText = result
            result
        } catch (e: Exception) {
            Log.e("WeatherViewModel", "Error: ${e.message}", e)
            val errorMessage = "Ошибка: ${e.localizedMessage ?: "Неизвестная ошибка"}"
            responseText = errorMessage
            errorMessage
        }
    }
    */
}