package kr.co.vilez.data.dto

import java.util.Calendar

data class CalendarDto (
    val title:String,
    val startDate: String,
    val endDate: String,
    val state: Int, // 1 : 내가 공유자 입장, 2 : 내가 피공유자 입장
    val type: Int, // 1 : 요청(ask), 2 : 공유(share)
    val startCalendar: Calendar,
    val endCalendar : Calendar,
    )