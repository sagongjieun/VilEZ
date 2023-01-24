package kr.co.vilez.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
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
            if(autoLogin) { // 자동로그인 되어있는 경우 : 바로 메인
                // 로그인해서 유저 정보 넣기
                CoroutineScope(Dispatchers.IO).launch {
                    val email = ApplicationClass.sharedPreferences.getString("email", "test@naver.com")
                    val password = ApplicationClass.sharedPreferences.getString("password", "12345")
                    val user = User(email?: "test@naver.com", password ?: "12345")

                    val request = ApplicationClass.retrofitUserService.getLoginResult(user).awaitResponse().raw()
                    Log.d(TAG, "onCreate: request: $request")
                    val result = ApplicationClass.retrofitUserService.getLoginResult(user).awaitResponse().body()
                    
                    if(result?.flag == "success") {  // 로그인 성공
                        Log.d(TAG, "로그인 성공, 받아온 user = ${result.data[0]}")
                        ApplicationClass.user = result.data[0] as User
                        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                    } else { // 로그인 실패
                        Log.d(TAG, "login: 로그인 실패, result:$result")
                        startActivity(Intent(this@SplashActivity, IntroActivity::class.java))
                    }
                }
            } else { // 로그인 화면
                startActivity(Intent(this@SplashActivity, IntroActivity::class.java))
            }
            finish()
        }
    }
}