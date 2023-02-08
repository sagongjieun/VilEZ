package kr.co.vilez.data.dto

data class AppointmentDto(
    val appointmentEnd: String,
    val appointmentId: Int,
    val appointmentStart: String,
    val boardId: Int,
    val date: String,
    val notShareUserId: Int,
    val roomId: Int,
    val shareUserId: Int,
    val state: Int,
    val status: String,
    val title: String,
    val type: Int
)