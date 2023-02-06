package kr.co.vilez.data.appointment

import kr.co.vilez.data.model.ImgPath

data class MyShare(
    val bookmarkCnt: Int,
    val imgPathList: List<ImgPath>,
    val myAppointListVO: MyAppointListVO
)