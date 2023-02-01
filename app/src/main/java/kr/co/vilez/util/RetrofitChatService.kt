package kr.co.vilez.util

import kr.co.vilez.data.model.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

data class RESTBookmarkResult(
    val `data`: List<Bookmark>,
    val flag: String
)
data class RESTChatroomResult(
    val `data`: List<Chatroom>,
    val flag: String
)
interface RetrofitChatService {
    @GET("/vilez/appointments/map/{roomId}")
    fun loadLocationByRoomId(@Path("roomId") roomId : Int): Call<RESTKakaoResult>

    @GET("/vilez/appointments/room/{userId}")
    fun loadRoomList(@Path("userId") userId : Int): Call<RESTRoomListResult>

    @GET("/vilez/appointments/room/enter/{roomId}")
    fun loadChatList(@Path("roomId") roomId: Int): Call<RESTChatListResult>

    // 새로운 채팅방을 생성한다.
    @POST("/vilez/appointments/room")
    fun createChatroom(@Body chatRoom: Chatroom) : Call<RESTChatroomResult>

    // 기존에 존재하는 채팅방인지 확인한다
    @GET("/vilez/appointments/board/checkroom")
    fun isExistChatroom(@Query("boardId")boardId: Int, @Query("type")type:Int, @Query("userId")userId:Int): Call<RESTChatroomResult>

}