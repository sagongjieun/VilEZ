package kr.co.vilez.util

import kr.co.vilez.data.model.RESTKakaoResult
import kr.co.vilez.data.model.RESTResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitKakaoService {
    @GET("/vilez/appoint/map")
    fun loadLocationByRoomId(@Query("roomId") roomId : String): Call<RESTKakaoResult>
}