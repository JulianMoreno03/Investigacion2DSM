package edu.udb.investigacion02dsm

data class WeatherResponse(
    val main: Main,
    val weather: List<Weather>,
    val name: String
)

//datos de la api que capturamos
data class Main(
    val temp: Double,
    val pressure: Int,
    val humidity: Int
)

data class Weather(
    val description: String,
    val icon: String
)
