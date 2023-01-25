package kr.co.vilez.util

import kr.co.vilez.data.model.RESTChatListResult
import kr.co.vilez.data.model.RESTKakaoResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitChatService {
    @GET("/vilez/appointments/map")
    fun loadLocationByRoomId(@Query("roomId") roomId : String): Call<RESTKakaoResult>

    @GET("/vilez/appointments/room/{userId}")
    fun loadRoomList(@Path("userId") userId : Int): Call<RESTChatListResult>
}