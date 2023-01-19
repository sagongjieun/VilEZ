package kr.co.vilez.util

import kr.co.vilez.data.model.RESTResult
import kr.co.vilez.data.model.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface RetrofitEmailService {

    @POST("/vilez/emailConfirm")
    fun checkEmail(@Body user: User): RESTResult


}