package kr.co.vilez.ui.user

import kr.co.vilez.data.model.User
import kr.co.vilez.data.model.UserLogin
import retrofit2.Call
import retrofit2.http.*

interface RetrofitUserService {

    @POST("/vilez/users/login")
    fun getLoginResult(@Body user: User): Call<UserLogin>

}