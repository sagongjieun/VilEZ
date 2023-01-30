package kr.co.vilez.ui.user

import android.app.Application
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
import kr.co.vilez.ui.chat.RoomlistData
import kr.co.vilez.util.ApplicationClass
import kr.co.vilez.util.NetworkResult
import kr.co.vilez.util.DataState
import kr.co.vilez.util.StompClient
import org.json.JSONArray
import org.json.JSONObject
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
        initToolBar()
        return binding.root
    }

    private fun initToolBar() {
        loginActivity.setSupportActionBar(binding.toolbar)
        loginActivity.supportActionBar?.setDisplayShowTitleEnabled(false) // 기본 타이틀 제거
        binding.title = "로그인"
    }

    fun login(view: View) {
        val email = binding.inputLoginEmail.editText?.text.toString()
        val password = binding.inputLoginPassword.editText?.text.toString()

        Log.d(TAG, "login: email : $email, password: $password")

        CoroutineScope(Dispatchers.Main).launch {
            println("1")
            val user = User(email, password)
            println("1-2")
            val result =
                ApplicationClass.retrofitUserService.getLoginResult(user).awaitResponse().body()
            println("2")
            if (result?.flag == "success") {
            println("3")
                Log.d(TAG, "로그인 성공, 받아온 user = ${result.data[0]}")
                ApplicationClass.prefs.setUser(result.data[0])
                ApplicationClass.prefs.setAutoLogin(user) // 로그인시 자동으로 자동로그인 넣기
            println("4")

                val resultDetail = ApplicationClass.retrofitUserService.getUserDetail(result.data[0].id).awaitResponse().body()
                if(resultDetail?.flag == "success") {
                    Log.d(TAG, "login: Detail조회도 로그인와 같이 성공~, result: ${resultDetail.data[0]}")
                    ApplicationClass.prefs.setUserDetail(resultDetail.data[0])
                }
            println("5")

                val intent = Intent(loginActivity, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            println("6")
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