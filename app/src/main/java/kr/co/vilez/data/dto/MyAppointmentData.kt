package kr.co.vilez.data.dto

import kr.co.vilez.data.model.ImgPath

data class MyAppointmentData(
    val bookmarkCnt: Int,
    val imgPathList: List<ImgPath>,
    val myAppointListVO: MyAppointListVO
)