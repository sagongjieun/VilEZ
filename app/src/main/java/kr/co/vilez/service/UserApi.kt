package kr.co.vilez.service

import com.google.gson.JsonObject
import kr.co.vilez.data.model.RESTResult
import kr.co.vilez.data.model.RESTUserDetailResult
import kr.co.vilez.data.model.RESTUserResult
import kr.co.vilez.data.model.User
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface UserApi {


    @POST("/vilez/users/login")
    suspend fun getLoginResult(@Body user: JsonObject): Response<RESTUserResult>

    @GET("/vilez/users/check")
    fun isUsedUserNickName(@Query("nickname") nickname : String): Call<RESTResult>

    @POST("/vilez/users/join")
    fun getJoinResult(@Body user: User): Call<RESTUserResult>

    @GET("/vilez/users/detail/{id}")
    fun getUserDetail(@Path("id") id:Int) : Call<RESTUserDetailResult>

    // TODO : RequestBody, Return 타입 확인
    @PUT("/vilez/users/modify")
    fun modifyUser(@Header("access_token")token:String?, @Body user: User) : Call<RESTResult>

    // TODO : 리턴 타입 확인
    @Multipart
    @PUT("/vilez/users/profile")
    fun modifyProfileImage(@Header("access_token")token:String?, @Part("userId") userId: Int, @Part image: MultipartBody.Part): Call<RESTResult>

}