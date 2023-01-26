package kr.co.vilez.ui.user

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.vilez.R
import kr.co.vilez.data.model.User
import kr.co.vilez.databinding.FragmentLoginBinding
import kr.co.vilez.ui.MainActivity
import kr.co.vilez.util.ApplicationClass
import kr.co.vilez.util.ApplicationClass.Companion.sharedPreferences
import kr.co.vilez.util.NetworkResult
import kr.co.vilez.util.StompClient
import retrofit2.awaitResponse

private const val TAG = "빌리지_LoginFragment"
class LoginFragment : Fragment() {
    private lateinit var binding:FragmentLoginBinding
    private lateinit var loginActivity:LoginActivity
    private val userViewModel by activityViewModels<UserViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginActivity = context as LoginActivity

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        binding.fragment = this
        return binding.root
    }

    fun login(view: View) {
        val email = binding.inputLoginEmail.editText?.text.toString()
        val password = binding.inputLoginPassword.editText?.text.toString()

        Log.d(TAG, "login: email : $email, password: $password")

        CoroutineScope(Dispatchers.Main).launch {
            userViewModel.login(email, password)
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userLoginObserver()

    }

    fun userLoginObserver() {
        userViewModel.userLoginResponseLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    if (it.data?.flag == "success") {
                        Log.d(TAG, "userLoginObserver: data: ${it.data?.data?.get(0)}")
                    }
                    else {
                        
                    }
                }
                is NetworkResult.Error -> {
                    Log.d(TAG, "이메일 체크 Error: ${it.data}")
                }
                is NetworkResult.Loading -> {
                    // progressbar 빙글빙글
                }
            }
        }
    }

    fun moveFragment(view: View) {
        when (view.id) {
            R.id.tv_login_findpw -> { // 비밀번호 찾기
                loginActivity.openFragment(3)
            }
            R.id.btn_register -> { // 회원가입
                loginActivity.openFragment(2)
            }
        }
    }

}