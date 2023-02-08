package kr.co.vilez.data.dto

data class MyAppointListDTO(
    val appointmentEnd: String,
    val appointmentId: String,
    val appointmentStart: String,
    val date: String,
    val endDay: String,
    val hopeAreaLat: String,
    val hopeAreaLng: String,
    val id: Int,
    val notShareUserId: String,
    val shareUserId: String,
    val startDay: String,
    val title: String,
    val type: Int,
    val userId: Int
)