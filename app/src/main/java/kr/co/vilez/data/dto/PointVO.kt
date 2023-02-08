package kr.co.vilez.data.dto

data class PointVO(
    val boardId: Int,
    val date: String,
    val point: Int,
    val reason: Int,
    val type: Int,
    val userId: Int
)