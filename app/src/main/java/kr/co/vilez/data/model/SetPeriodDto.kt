package kr.co.vilez.data.model

data class SetPeriodDto(val boardId: Int, val shareUserId: Int,
                        val notShareUserId: Int, val startDay: String,
                        val endDay: String, val type: Int)