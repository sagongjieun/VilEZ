package kr.co.vilez.data.dto

data class MyAppointListVO(
    val appointmentEnd: String,
    val appointmentId: Int,
    val appointmentStart: String,
    val boardId: Int,
    val date: String,
    val notShareUserId: Int,
    val roomId: Int,
    val shareUserId: Int,
    val state: Int,
    val status: Int,
    val title: String,
    val type: Int
)