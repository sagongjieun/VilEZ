package kr.co.vilez.data.model

data class AppointVO(val boardId: Int,
                     val type: Int,
                     val title: String,
                     val shareUserId: Int,
                     val notShareUserId: Int,
                     val appointmentStart: String,
                     val appointmentEnd: String,
                     val date: String,)