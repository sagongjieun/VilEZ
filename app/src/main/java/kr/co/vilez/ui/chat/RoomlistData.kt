package kr.co.vilez.ui.chat

data class RoomlistData (val roomId: Int, val nickName: String, var content: String,
                         val area: String, val otherUserId: Int)