package kr.co.vilez.data.appointment

data class MyAppointListVO(
    val appointmentId: String,
    val endDay: String,
    val hopeAreaLat: String,
    val hopeAreaLng: String,
    val id: Int,
    val notShareUserId: String,
    val shareUserId: String,
    val startDay: String,
    val title: String,
    val type: Int,
    val date:String,
    val userId: Int
)