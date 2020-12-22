package com.example.endapp.ui.weather.we

data class Data(
    val forecast: List<Forecast>,
    val ganmao: String,
    val pm10: Double,
    val pm25: Double,
    val quality: String,
    val shidu: String,
    val wendu: String,
    val yesterday: Yesterday
)