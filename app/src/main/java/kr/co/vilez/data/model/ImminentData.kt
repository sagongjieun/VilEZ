package kr.co.vilez.data.model

import kr.co.vilez.data.dto.AppointmentDto

data class ImminentData(
    val appointmentDto: AppointmentDto,
    val bookmarkCnt: Int,
    val imgPath: List<ImgPath>
)