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
import kr.co.vilez.databinding.ActivityRegisterBinding
import kr.co.vilez.ui.MainActivity
import kr.co.vilez.util.ApplicationClass
import retrofit2.awaitResponse

private const val TAG = "빌리지_RegisterActivity"
class RegisterActivity : AppCompatActivity() {
    private lateinit var binding:ActivityRegisterBinding
    private lateinit var user:User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register)


    }

    fun register(view: View) {
        // 회원가입
    }

    fun checkEmail(view:View) {
        Log.d(TAG, "checkEmail: 이메일 인증 버튼 클릭")
        val result = ApplicationClass.retrofitEmailService.checkEmail(user)
        if (result.flag == "success") {
            val data = result.data as ArrayList<*>
            Log.d(TAG, "checkEmail: 이메일 전송 완료, data: ${data[0]}")
        } else {
            Log.d(TAG, "checkEmail: 이메일 전송 완료")
        }
    }

    fun checkNickName(view:View) {
        Log.d(TAG, "checkNickName: 닉네임 중복 버튼 클릭")
        val result = ApplicationClass.retrofitUserService.isUsedUserNickName(user.nickName)
        if (result.flag == "success") {
            Log.d(TAG, "checkNickName: 사용가능한 닉네임")
        } else {
            Log.d(TAG, "checkNickName: 사용 불가한 닉네임")
        }
    }
}