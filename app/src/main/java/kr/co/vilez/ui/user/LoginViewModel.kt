package kr.co.vilez.ui.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel: ViewModel() {
    private val _email = MutableLiveData<String>().apply { // mutable은 값 변경 가능
        value = ""
    }
    private val _password = MutableLiveData<String>().apply {
        value = ""
    }

    val email:LiveData<String>
        get() = _email

    val password:LiveData<String>
        get() = _password




}