package kr.co.vilez.service

import kr.co.vilez.data.model.RESTResult
import kr.co.vilez.data.model.Token
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface TokenApi {

    // refreshToken을 서버에 보내면 accessToken을 갱신해줌
    @POST("/vilez/users/refresh")
    fun refreshToken(@Body refreshToken: Token): Call<RESTResult>

}