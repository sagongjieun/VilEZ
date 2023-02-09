package kr.co.vilez.data.model

import kr.co.vilez.data.dto.MyAppointListVO

data class MyAppointmentData(
    val bookmarkCnt: Int,
    val imgPathList: List<ImgPath>,
    val myAppointListVO: MyAppointListVO
)