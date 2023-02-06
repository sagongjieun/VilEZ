package kr.co.vilez.service

import com.google.gson.JsonObject
import kr.co.vilez.data.model.*
import okhttp3.MultipartBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.*

interface RetrofitUserService {

    /**
     * access token을 헤더에 넣지 않고 보내는 API or 파라미터에 직접 넣어주기
     */

    @POST("/vilez/users/login")
    fun getLoginResult(@Body user: User): Call<RESTUserResult>

    @GET("/vilez/users/check")
    fun isUsedUserNickName(@Query("nickname") nickname : String):Call<RESTResult>

    @POST("/vilez/users/join")
    fun getJoinResult(@Body user:User): Call<RESTResult>

    @GET("/vilez/users/check/{email}")
    fun isUsedEmail(@Path("email") email:String) : Call<RESTResult>

    @GET("/vilez/users/detail/{id}")
    fun getUserDetail(@Path("id") id:Int) : Call<RESTUserDetailResult>

    @PUT("/vilez/users/locationMobile")
    fun updateUserLocation(@Body user:HashMap<String, String>) : Call <RESTResult>

    // 헤더 직접 넣는 방법 이제 쓰지말기~
    /*@PUT("/vilez/users/modify")
    fun modifyUser(@Header("access_token")token:String?, @Body user:User) : Call<RESTResult>*/


    /**
     * 아래는 헤더에 access token을 같이 보낼때 사용하는 API : 자동으로 header에 넣어보내고 만료시 refreshtoken을 보내서 갱신시켜주고 재전송함
     */

    // 유저 정보를 수정한다
    @PUT("/vilez/users/modify")
    fun modifyUser(@Body user:User) : Call<RESTResult> //


    // 프로필 이미지를 변경한다
    @Multipart
    @PUT("/vilez/users/profile")
    fun modifyProfileImage(@Part("userId") userId: Int, @Part image: MultipartBody.Part?): Call<RESTResult>

    // 프로필 이미지를 기본 이미지로 변경한다 (삭제)
    @Multipart
    @PUT("/vilez/users/profile")
    fun removeProfileImage(@Part("userId") userId: Int): Call<RESTResult>

    // 카카오 로그인
    @GET
    fun getKakaoOAuth()

}

