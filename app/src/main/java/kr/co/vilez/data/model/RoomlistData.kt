package kr.co.vilez.data.model

data class RoomlistData (val roomId: Int, val nickName: String, var content: String,
                         val area: String, val otherUserId: Int, var noReadCnt: Int,
                         val profile: String, var time: Long)