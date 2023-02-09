package kr.co.vilez.data.model

data class Chatroom(
    val boardId: Int,
    var id: Int,
    val notShareUserId: Int, // 피공유자
    val shareUserId: Int, // 공유자
    val type: Int // share : 2, ask : 1
)