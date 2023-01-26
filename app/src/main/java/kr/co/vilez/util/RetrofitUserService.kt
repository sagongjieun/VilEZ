package kr.co.vilez.util

import kr.co.vilez.data.model.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface RetrofitUserService {

    @POST("/vilez/users/login")
    fun getLoginResult(@Body user: User): Call<RESTUserResult>

    @GET("/vilez/users/check")
    fun isUsedUserNickName(@Query("nickname") nickname : String):Call<RESTResult>

    @POST("/vilez/users/join")
    fun getJoinResult(@Body user:User): Call<RESTUserResult>

    @GET("/vilez/users/detail/{id}")
    fun getUserDetail(@Path("id") id:Int) : Call<RESTUserDetailResult>

    // TODO : RequestBody, Return 타입 확인
    @PUT("/vilez/users/modify")
    fun modifyUser(@Header("access_token")token:String?, @Body user:User) : Call<RESTResult>

    // TODO : 리턴 타입 확인
    @Multipart
    @PUT("/vilez/users/profile")
    fun modifyProfileImage(@Header("access_token")token:String?, @Part("userId") userId: Int, @Part image: MultipartBody.Part): Call<RESTResult>

}

