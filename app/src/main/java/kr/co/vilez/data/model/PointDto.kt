package kr.co.vilez.data.model

data class PointDto (
    val title: String,
    val boardId:Int,
    val userId:Int,
    val type:Int,
    val point:Int,
    val date: String,
    val reason:Int,
)