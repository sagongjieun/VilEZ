package kr.co.vilez.service

import kr.co.vilez.data.model.RESTResult
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

data class FCMToken(val userId:Int, val token:String)
interface FCMApi {

    // Token정보 서버로 전송
    @POST("/vilez/token")
    fun uploadToken(@Body token:FCMToken): Call<RESTResult>

}