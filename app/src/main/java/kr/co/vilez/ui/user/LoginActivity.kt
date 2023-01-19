package kr.co.vilez.ui.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.vilez.R
import kr.co.vilez.data.model.User
import kr.co.vilez.databinding.ActivityLoginBinding
import kr.co.vilez.ui.MainActivity
import kr.co.vilez.util.ApplicationClass
import retrofit2.awaitResponse

private const val TAG = "빌리지_LoginActivity"
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.activity = this

        supportActionBar?.hide() // 액션바 숨김
    }

    fun login(view:View) {
        Log.d(TAG, "login: ")
        // login
        val email = binding.inputLoginEmail.editText?.text.toString()
        val password = binding.inputLoginPassword.editText?.text.toString()

        Log.d(TAG, "login: email : $email, password: $password")

        CoroutineScope(Dispatchers.Main).launch {
            val user = User(email, password)
            val result = ApplicationClass.retrofitUserService.getLoginResult(user).awaitResponse().body()
            if (result == null) { // 로그인 실패
                Log.d(TAG, "login: 로그인 실패, result:$result")
            } else if(result.flag == "success") {  // 로그인 성공
                val data =result.data[0]
                Log.d(TAG, "로그인 성공, 받아온 user = ${data}")
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            }
        }


    }

    fun moveActivity(view: View) {
        when(view.id) {
            R.id.tv_login_findpw -> {
                startActivity(Intent(this@LoginActivity, FindPasswordActivity::class.java))
            }
            R.id.btn_register -> {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            }
        }
        //finish()
    }
}