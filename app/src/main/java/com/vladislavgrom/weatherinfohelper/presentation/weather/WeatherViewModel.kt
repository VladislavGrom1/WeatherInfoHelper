package com.vladislavgrom.weatherinfohelper.presentation.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vladislavgrom.weatherinfohelper.domain.location.use_case.GetAddressLocationUseCase
import com.vladislavgrom.weatherinfohelper.domain.location.use_case.GetCurrentLocationUseCase
import com.vladislavgrom.weatherinfohelper.domain.util.Resource
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
    private val getCurrentLocationUseCase: GetCurrentLocationUseCase,
    private val getAddressLocationUseCase: GetAddressLocationUseCase
) : ViewModel() {

    private val _weatherState = MutableStateFlow<WeatherState>(
        WeatherState.Initial
    )
    val weatherState: StateFlow<WeatherState> = _weatherState.asStateFlow()

    fun onPermissionResult(granted: Boolean) {
        if (granted) {
            getWeatherData()
        } else {
            _weatherState.value = WeatherState.Error("Для работы приложения необходимо предоставить доступ к местоположению.")
        }
    }

    fun showPermissionRequest() {
        _weatherState.value = WeatherState.RequestingPermissions
    }

    fun onPermissionResultAlreadyGranted() {
        getWeatherData()
    }

    fun getWeatherData() {
        _weatherState.value = WeatherState.DataLoading
        viewModelScope.launch {
            try {
                val location = getCurrentLocationUseCase.call()
                
                if (location == null) {
                    _weatherState.value = WeatherState.Error("Не удалось получить доступ к геопозиции. Убедитесь, что GPS включен и разрешения даны.")
                    return@launch
                }

                val latitude = location.latitude
                val longitude = location.longitude

                val addressLocation = getAddressLocationUseCase.call(latitude, longitude)

                val result = getWeatherDataUseCase.call(
                    latitude = latitude,
                    longitude = longitude
                )

                when (result) {
                    is Resource.Success -> {
                        _weatherState.value = WeatherState.DataLoaded(
                            weatherData = result.data,
                            latitude = latitude,
                            longitude = longitude,
                            addressLocation = addressLocation
                        )
                    }
                    is Resource.Error -> {
                        _weatherState.value = WeatherState.Error(result.message ?: "Неизвестная ошибка")
                    }
                }
            } catch (e: Exception) {
                _weatherState.value = WeatherState.Error(e.localizedMessage ?: "Ошибка при загрузке данных")
            }
        }
    }
}
