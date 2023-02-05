package kr.co.vilez.data.model

data class AppointDto(val appointmentId: Int,
                      val boardId: Int,
                      val title: String,
                      val shareUserId: Int,
                      val notShareUserId: Int,
                      val appointmentStart: String,
                      val appointmentEnd: String,
                      val state: Int,
                      val date: String,
                      val type: Int)