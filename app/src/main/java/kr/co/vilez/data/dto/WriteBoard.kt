package kr.co.vilez.data.dto

data class WriteBoard(
    val category: String,
    val endDay: String,
    val hopeAreaLat: String,
    val hopeAreaLng: String,
    val content: String,
    val startDay: String,
    val title: String,
    val userId: Int,
//    val address: String,
)