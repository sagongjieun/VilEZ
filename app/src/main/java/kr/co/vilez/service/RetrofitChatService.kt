package kr.co.vilez.util

import kr.co.vilez.data.model.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
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
data class RESTPeriodResult(
    val `data`: List<SetPeriodDto>,
    val flag: String
)
data class RESTAppointResult(
    val `data`: List<AppointDto>,
    val flag: String
)
data class RESTSignResult(
    val `data`: List<Sign>,
    val flag: String
)
data class bool(val state: Boolean)
data class RESTReturnResult(
    val `data`: List<bool>,
    val flag: String
)

data class State(val state: Int)
data class RESTStateResult(
    val `data`: List<State>,
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

    @GET("/vilez/appointments/room/board/{roomId}")
    fun getRoomData(@Path("roomId") roomId: Int): Call<RESTRoomResult>

    @PUT("/vilez/appointments/set/period")
    fun setPeriodDto(@Body setPeriodDto: SetPeriodDto): Call<RESTResult>

    @GET("/vilez/appointments/set/check")
    fun getPeriodDto(@Query("boardId") boardId: Int, @Query("notShareUserId") notShareUserId : Int
                    ,@Query("shareUserId") shareUserId: Int,@Query("type") type: Int): Call<RESTPeriodResult>

    @GET("/vilez/appointments/date")
    fun getAppointMent(@Query("boardId") boardId: Int, @Query("notShareUserId") notShareUserId : Int
                     ,@Query("shareUserId") shareUserId: Int,@Query("type") type: Int): Call<RESTAppointResult>

    @POST("/vilez/appointments/")
    fun setAppointment(@Body appointDto: AppointmentDto): Call<RESTResult>

    @GET("/vilez/signs/{roomId}")
    fun getSign(@Path("roomId") roomId: Int): Call<RESTSignResult>

    @POST("/vilez/signs")
    fun addSign(@Body sign: Sign) : Call<RESTResult>

    @DELETE("/vilez/appointments/chat")
    fun closeRoom(@Query("roomId") roomId: Int,@Query("userId") userId: Int): Call<RESTResult>

    @GET("/vilez/returns")
    fun getReturns(@Query("roomId") roomId: Int): Call<RESTReturnResult>

    @POST("/vilez/returns")
    fun returnRequest(@Body returnRequestDto: ReturnRequestDto) : Call<RESTResult>

    @POST("/vilez/returns/confirmed")
    fun returnEnd(@Body returnRequestDto: ReturnRequestDto) : Call<RESTResult>

    @GET("/vilez/returns/state")
    fun getState(@Query("roomId") roomId: Int) : Call<RESTStateResult>
}