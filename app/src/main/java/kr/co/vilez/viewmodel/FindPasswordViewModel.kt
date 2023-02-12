package kr.co.vilez.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.co.vilez.data.model.RESTResult
import kr.co.vilez.repository.FindPasswordRepository
import kr.co.vilez.util.NetworkResult

class FindPasswordViewModel:ViewModel() {

    private val findPasswordRepository = FindPasswordRepository()
    val findPasswordResponseLiveData: LiveData<NetworkResult<RESTResult>>
        get() = findPasswordRepository.emailResponseData

    suspend fun sendEmailAuth(email:String) {
        viewModelScope.launch { findPasswordRepository.sendTempPassword(email) }
    }


    var isEmailSentLiveData = MutableLiveData<Boolean>()
    var isAuthSucceededLiveData = MutableLiveData<Boolean>()
    private var isEmailSent = false
    private var isAuthSucceeded = false





}