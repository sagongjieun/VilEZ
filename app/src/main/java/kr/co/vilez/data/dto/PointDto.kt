package kr.co.vilez.data.dto

data class PointDto(
    val shareUserId:Int,
    val notShareUserId:Int,
    val boardId:Int,
    val title: String,
    val date: String,
    val type:Int,
    val isIncrease :Boolean
)

