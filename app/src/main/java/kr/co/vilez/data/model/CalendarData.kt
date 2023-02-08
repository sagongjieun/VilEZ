package kr.co.vilez.data.model

data class CalendarData(
    val appointmentEnd: String,
    val appointmentId: Int,
    val appointmentStart: String,
    val boardId: Int,
    val date: Any,
    val notShareUserId: Int,
    val roomId: Int,
    val shareUserId: Int,
    val state: Int,
    val status: Any,
    val title: String,
    val type: Int
)