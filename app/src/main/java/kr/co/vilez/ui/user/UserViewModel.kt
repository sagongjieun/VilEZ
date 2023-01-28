package kr.co.vilez.ui.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.co.vilez.data.model.RESTUserResult

import kr.co.vilez.util.NetworkResult

class UserViewModel: ViewModel() {
   /* val userRepository = UserRepository()

    val userLoginResponseLiveData: LiveData<NetworkResult<RESTUserResult>>
        get() = userRepository.userResponseLiveData


    suspend fun login(email:String, password: String) {
        viewModelScope.launch { userRepository.userLogin(email, password) }
    }*/
}