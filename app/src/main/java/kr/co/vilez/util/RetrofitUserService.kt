package kr.co.vilez.util

import kr.co.vilez.data.model.RESTResult
import kr.co.vilez.data.model.User
import retrofit2.Call
import retrofit2.http.*

interface RetrofitUserService {

    @POST("/vilez/users/login")
    fun getLoginResult(@Body user: User): Call<RESTResult>

    @GET("/vilez/users/check")
    fun isUsedUserNickName(@Query("nickname") nickname : String):RESTResult


}