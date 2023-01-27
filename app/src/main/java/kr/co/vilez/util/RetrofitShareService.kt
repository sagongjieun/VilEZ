package kr.co.vilez.util

import kr.co.vilez.data.model.RestShare
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitShareService {
    @GET("/vilez/shareboard")
    fun boardList(@Query(value = "cnt") cnt:Int, @Query(value = "high") high:Int): Call<RestShare>
    
}