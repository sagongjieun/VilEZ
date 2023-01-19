package kr.co.vilez.ui.user

import kr.co.vilez.data.model.User
import retrofit2.Call
import retrofit2.http.*

interface RetrofitUserService {

    // @GET( EndPoint-자원위치(URI) )
    @POST("/vilez/users/login")
    fun getLoginResult(@Body user: User): Call<User>

}