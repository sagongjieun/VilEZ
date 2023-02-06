package kr.co.vilez.service

import kr.co.vilez.data.appointment.MyShare
import kr.co.vilez.data.model.*
import retrofit2.Call
import retrofit2.http.*

data class RESTPointResult(
    val `data`: List<PointDto>,
    val flag: String
)
data class RESTMyGiveResult( // 내가 빌려준 물품 (공유중 + 공유 예정)
    val `data`: List<List<MyShare>>,
    val flag: String
)
interface RetrofitAppointmentService {

    // TODO : 이거 맞는지 모르겠음
    // 내 약속 정보 (공유 예정) 내역을 조회한다. type = 1 요청, type = 2 공유
    @GET("/vilez/appointments/my/appointlist/{userId}")
    fun getMyReserveList(@Path("userId")id:Int): Call<RESTMyGiveResult>

    // 내가 공유해준 내역(나의 공유물품)을 조회한다. type = 1 요청게시글, type = 2 공유 게시글
    @GET("/vilez/appointments/my/give/{userId}")
    fun getMyGiveList(@Path("userId")id:Int): Call<RESTMyGiveResult>


    // 포인트 내역 조회
    @GET("/vilez/appointments/my/point")
    fun getPointList(@Query("userId") int: Int): Call<RESTPointResult>


    // 나의 작성글
    //@GET("/vilez/appointments/my/{userId}")

}

