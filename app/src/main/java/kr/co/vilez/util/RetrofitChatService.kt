package kr.co.vilez.util

import kr.co.vilez.data.model.RESTChatListResult
import kr.co.vilez.data.model.RESTKakaoResult
import kr.co.vilez.data.model.RESTRoomListResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface RetrofitChatService {
    @GET("/vilez/appointments/map/{roomId}")
    fun loadLocationByRoomId(@Path("roomId") roomId : Int): Call<RESTKakaoResult>

    @GET("/vilez/appointments/room/{userId}")
    fun loadRoomList(@Path("userId") userId : Int): Call<RESTRoomListResult>

    @GET("/vilez/appointments/room/enter/{roomId}")
    fun loadChatList(@Path("roomId") roomId: Int): Call<RESTChatListResult>
}