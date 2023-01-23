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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.vilez.R
import kr.co.vilez.data.model.User
import kr.co.vilez.databinding.FragmentLoginBinding
import kr.co.vilez.ui.MainActivity
import kr.co.vilez.util.ApplicationClass
import kr.co.vilez.util.ApplicationClass.Companion.sharedPreferences
import kr.co.vilez.util.StompClient
import retrofit2.awaitResponse

private const val TAG = "빌리지_LoginFragment"
class LoginFragment : Fragment() {
    private lateinit var binding:FragmentLoginBinding
    private lateinit var loginActivity:LoginActivity
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
            val user = User(email, password)
            val result =
                ApplicationClass.retrofitUserService.getLoginResult(user).awaitResponse().body()
            if (result?.flag == "success") {
                val data = result.data[0]
                Log.d(TAG, "로그인 성공, 받아온 user = ${data}")
                StompClient.runStomp()

                // 자동로그인 : sharedPreference에 autoLogin true로 저장
                sharedPreferences.edit {
                    putBoolean("autoLogin", true)
                    apply()
                }
                Log.d(TAG, "sh) 사용자 autoLogin : ${sharedPreferences.getBoolean("autoLogin", false)}")
                Log.d(TAG, "sh) 사용자 autoLogin : ${sharedPreferences.getBoolean("autoLogin", false)}")
                val intent = Intent(loginActivity, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            } else {
                Log.d(TAG, "login: 로그인 실패, result:$result")
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