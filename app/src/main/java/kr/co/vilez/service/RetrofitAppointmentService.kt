package kr.co.vilez.service

import kr.co.vilez.data.model.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

data class RESTPointResult(
    val `data`: List<PointDto>,
    val flag: String
)

interface RetrofitAppointmentService {

    @GET("/vilez/appointments/my/point")
    fun getPointList(@Query("userId") int: Int): Call<RESTPointResult>


}

