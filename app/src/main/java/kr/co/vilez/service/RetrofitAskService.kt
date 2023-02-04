package kr.co.vilez.service

import kr.co.vilez.data.model.DetailTest
import kr.co.vilez.data.model.RestShare
import kr.co.vilez.data.model.RestShareDetail
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitAskService {
    @GET("/vilez/askboard")
    fun boardList(@Query(value = "cnt") cnt:Int, @Query(value = "high") high:Int): Call<RestShare>

    @GET("/vilez/askboard/detail/{boardId}")
    fun detail(@Path("boardId") boardId:Int):Call<DetailTest>
}