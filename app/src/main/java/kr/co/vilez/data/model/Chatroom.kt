package kr.co.vilez.data.model

data class Chatroom(
    val boardId: Int,
    var id: Int,
    val notShareUserId: Int,
    val shareUserId: Int,
    val type: Int // share : 2, ask : 1
)