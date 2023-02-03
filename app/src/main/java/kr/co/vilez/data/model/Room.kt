package kr.co.vilez.data.model

data class Room(val id: Int, val type : Int,
                       val boardId: Int,
                        val shareUserId: Int,
                       val notShareUserId: Int)