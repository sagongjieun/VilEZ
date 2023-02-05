package kr.co.vilez.service

import kr.co.vilez.data.model.*
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitAskService {
    @GET("/vilez/askboard")
    fun boardList(@Query(value = "cnt") cnt:Int, @Query(value = "low") low:Int, @Query(value = "high") high:Int, @Query(value =  "userId") id:Int): Call<RESTAskResult>

    @GET("/vilez/askboard/detail/{boardId}")
    fun getBoardDetail(@Path("boardId") boardId:Int):Call<RESTAskDetailResult>
}