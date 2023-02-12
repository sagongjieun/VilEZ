package kr.co.vilez.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import kr.co.vilez.data.model.Email
import kr.co.vilez.data.model.RESTResult
import kr.co.vilez.service.EmailApi
import kr.co.vilez.service.UserApi
import kr.co.vilez.util.ApplicationClass
import kr.co.vilez.util.NetworkResult

private const val TAG = "빌리지_FindPasswordRepository"
class FindPasswordRepository {

    private val emailApi = ApplicationClass.wRetrofit.create(EmailApi::class.java)
    private val _emailResponseLiveData = MutableLiveData<NetworkResult<RESTResult>>()

    val emailResponseData: LiveData<NetworkResult< RESTResult>>
        get() = _emailResponseLiveData

    suspend fun sendTempPassword(email:String) {
        val userEmail = Email(email)
        try {
            val response = emailApi.getTempPassword(userEmail)
            _emailResponseLiveData.postValue(NetworkResult.Loading())
            if(response.isSuccessful && response.body()!=null) {
                Log.d(TAG, "sendTempPassword: ${response.body()}")
                _emailResponseLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody()!=null) {
                _emailResponseLiveData.postValue(NetworkResult.Error(response.errorBody()!!.toString()))
            } else {
                _emailResponseLiveData.postValue(NetworkResult.Error(response.headers().toString()))
            }
        } catch (e:Exception) {
            Log.d(TAG, "sendTempPassword: 에러발생")
        }
    }


}