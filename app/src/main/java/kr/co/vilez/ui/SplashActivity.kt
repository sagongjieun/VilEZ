package kr.co.vilez.ui

import android.content.Intent
import android.content.SharedPreferences
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
import kr.co.vilez.util.SharedPreferencesUtil
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
            val autoLogin = ApplicationClass.prefs.isAutoLogin()
            Log.d(TAG, "openLoginActivity: 현재 sharedPreference에 저장된 autoLogin = $autoLogin")
            if (autoLogin) { // 자동로그인 되어있는 경우 : 바로 메인
                // 로그인해서 유저 정보 넣기
                val user = ApplicationClass.prefs.getAutoLogin()
                Log.d(TAG, "onCreate: 자동 로그인 할 email:${user.email}, password:${user.password}")
                val result =
                    ApplicationClass.userApi.getLoginResult(user).awaitResponse().body()
                if (result?.flag == "success" && !result.data.isNullOrEmpty()) {  // 로그인 성공
                    Log.d(TAG, "로그인 성공, 받아온 user = ${result.data[0]}")
                    ApplicationClass.prefs.setUser(result.data[0])
                    val resultDetail = ApplicationClass.userApi.getUserDetail(result.data[0].id).awaitResponse().body()
                    if(resultDetail?.flag == "success") {
                        Log.d(TAG, "login: Detail조회도 로그인와 같이 성공~, result: ${resultDetail.data[0]}")
                        ApplicationClass.prefs.setUserDetail(resultDetail.data[0])
                    }
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