package kr.co.vilez.service

import kr.co.vilez.data.model.Email
import kr.co.vilez.data.model.RESTResult
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface EmailApi {

    @POST("/vilez/emailConfirm")
    fun checkEmail(@Body email: Email): Call<RESTResult>


}