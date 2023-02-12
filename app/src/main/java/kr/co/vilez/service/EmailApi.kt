package kr.co.vilez.service

import kr.co.vilez.data.model.Email
import kr.co.vilez.data.model.RESTResult
import retrofit2.Response
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface EmailApi {

    @POST("/vilez/emailConfirm")
    fun checkEmail(@Body email: Email): Call<RESTResult>

    @POST("/vilez/passowrd")
    fun getTempPassword(@Body email: Email): Response<RESTResult>

    @POST("/vilez/passowrd")
    fun getTempPassword2(@Body email: Email): Call<RESTResult>


}