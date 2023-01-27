package kr.co.vilez.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.view.Window
import kotlinx.coroutines.*
import kr.co.vilez.R
import kr.co.vilez.data.model.User
import kr.co.vilez.ui.dialog.AlertDialogInterface
import kr.co.vilez.ui.dialog.AlertDialogWithCallback
import kr.co.vilez.ui.user.LoginActivity
import kr.co.vilez.util.ApplicationClass
import kr.co.vilez.util.Common
import retrofit2.awaitResponse
import kotlin.concurrent.thread

private const val TAG = "빌리지_SplashActivity"
class SplashActivity : AppCompatActivity() {
    var isReady = false
    var isLogin = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //requestWindowFeature(Window.FEATURE_NO_TITLE) // 타이틀 바 제거
        supportActionBar?.hide() // 액션바 숨김
        setContentView(R.layout.activity_splash)

        CoroutineScope(Dispatchers.IO).launch {
            var autoLogin = ApplicationClass.sharedPreferences.getBoolean("autoLogin", false)
            Log.d(TAG, "openLoginActivity: 현재 sharedPreference에 저장된 autoLogin = $autoLogin")
            if (autoLogin) { // 자동로그인 되어있는 경우 : 바로 메인
                // 로그인해서 유저 정보 넣기
                val email = ApplicationClass.sharedPreferences.getString("email", "test@naver.com")
                val password = ApplicationClass.sharedPreferences.getString("password", "12345")
                val user = User(email ?: "test@naver.com", password ?: "12345")
                val result =
                    ApplicationClass.retrofitUserService.getLoginResult(user).awaitResponse().body()
                Thread.sleep(1000)
                if (result?.flag == "success") {  // 로그인 성공
                    Log.d(TAG, "로그인 성공, 받아온 user = ${result.data[0]}")
                    ApplicationClass.user = result.data[0] as User
                    isLogin = true
                } else { // 로그인 실패
                    isLogin = false
                    Log.d(TAG, "login: 로그인 실패, result:$result")
                }
            } else { // 로그인 화면
                isLogin = false
            }
            isReady = true
        }
    }

    override fun onStart() {
        super.onStart()
        GlobalScope.launch() {
            while(!isReady) {
                delay(500)
            }
            Log.d(TAG, "onStart: isReady = true")
            if (isLogin) { // 메인 화면으로 이동
                overridePendingTransition(com.google.android.material.R.anim.abc_fade_in, com.google.android.material.R.anim.abc_fade_out)
                val intent = Intent(this@SplashActivity, MainActivity::class.java)
                startActivity(intent)
            } else { // 로그인 화면
                startActivity(Intent(this@SplashActivity, IntroActivity::class.java))
            }
            finish()
       }
    }
}