package kr.co.vilez.data.model

data class ChatMsg(
    val roomId: Int,
    val toUserId: Int,
    val fromUserId: Int,
    val content: String,
    val time: Long,
    val system: Boolean
) {

}