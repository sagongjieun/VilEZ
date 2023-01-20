package kr.co.vilez.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kr.co.vilez.R
import kr.co.vilez.ui.user.LoginActivity
import kr.co.vilez.util.ApplicationClass

private const val TAG = "빌리지_SplashActivity"
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //requestWindowFeature(Window.FEATURE_NO_TITLE) // 타이틀 바 제거
        supportActionBar?.hide() // 액션바 숨김
        setContentView(R.layout.activity_splash)

        GlobalScope.launch() {
            delay(1000)
            var autoLogin = ApplicationClass.sharedPreferences.getBoolean("autoLogin",false)
            Log.d(TAG, "openLoginActivity: 현재 sharedPreference에 저장된 autoLogin = $autoLogin")
            if(!autoLogin){ // 로그인 화면
                startActivity(Intent(this@SplashActivity, IntroActivity::class.java))
            } else { // 자동로그인 되어있는 경우 : 바로 메인
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            }
            finish()
        }
    }
}