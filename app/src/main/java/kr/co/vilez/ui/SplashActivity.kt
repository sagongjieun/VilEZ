package kr.co.vilez.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.coroutines.*
import kr.co.vilez.R
import kr.co.vilez.ui.dialog.*
import kr.co.vilez.util.ApplicationClass
import kr.co.vilez.util.ApplicationClass.Companion.isNetworkConnected
import retrofit2.awaitResponse

private const val TAG = "빌리지_SplashActivity"
class SplashActivity : AppCompatActivity() {
    var isReady = false
    var isLogin = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //requestWindowFeature(Window.FEATURE_NO_TITLE) // 타이틀 바 제거
        supportActionBar?.hide() // 액션바 숨김
        setContentView(R.layout.activity_splash)


        if(!isNetworkConnected) { // 네트워크 연결 확인 후 없으면 종료
            val dialog = AlertDialogWithCallback( object:AlertDialogInterface{
                override fun onYesButtonClick(id: String) {
                    finish() // 앱 종료
                }
            }, "네트워크에 접속할 수 없습니다.\n" +
                    "네트워크 연결상태를 확인할 수 없어 앱을 종료합니다.", "")
            dialog.isCancelable = false // 창닫기 금지시키기
            dialog.show(supportFragmentManager,"NetworkFail" )
        } else {
            CoroutineScope(Dispatchers.IO).launch {
                val autoLogin = ApplicationClass.prefs.isAutoLogin()
                Log.d(TAG, "openLoginActivity: 현재 sharedPreference에 저장된 autoLogin = $autoLogin")
                if (autoLogin) { // 자동로그인 되어있는 경우 : 바로 메인
                    // 로그인해서 유저 정보 넣기
                    val user = ApplicationClass.prefs.getAutoLogin()
                    Log.d(TAG, "onCreate: 자동 로그인 할 email:${user.email}, password:${user.password}")
                    val result = ApplicationClass.hUserApi.getLoginResult(user).awaitResponse().body()
                    if (result?.flag == "success" && !result.data.isNullOrEmpty()) {  // 로그인 성공
                        Log.d(TAG, "로그인 성공, 받아온 user = ${result.data[0]}")
                        ApplicationClass.prefs.setUser(result.data[0])
                        val resultDetail = ApplicationClass.hUserApi.getUserDetail(result.data[0].id).awaitResponse().body()
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


    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onStart() {
        super.onStart()

        if(isNetworkConnected) {
            GlobalScope.launch() {
                delay(1000)
                while(!isReady) {
                    delay(200)
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
}