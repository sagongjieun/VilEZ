package kr.co.vilez.util

import android.app.Application
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.kakao.sdk.common.KakaoSdk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.vilez.R
import kr.co.vilez.data.model.RESTResult
import kr.co.vilez.data.model.Token
import kr.co.vilez.service.*
import kr.co.vilez.ui.user.LoginActivity
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.net.HttpURLConnection
import java.util.concurrent.TimeUnit

private const val TAG = "빌리지_ApplicationClass"
class ApplicationClass: Application(), LifecycleObserver {

    val SERVER_URL = "http://i8d111.p.ssafy.io:8081"
//    val SERVER_URL = "http://192.168.0.28:8086" // 로컬

    companion object {
        var isForeground = false // 앱이 현재 포그라운드인지 확인 (FCM에서 사용)

        // 전역변수 문법을 통해 Retrofit 인스턴스를 앱 실행 시 1번만 생성하여 사용 (싱글톤 객체)
        lateinit var wRetrofit : Retrofit
        lateinit var retrofitUserService: RetrofitUserService
        lateinit var retrofitEmailService: RetrofitEmailService
        lateinit var retrofitChatService: RetrofitChatService
        lateinit var retrofitShareService: RetrofitShareService
        lateinit var retrofitAskService: RetrofitAskService
        lateinit var retrofitAppointmentService: RetrofitAppointmentService

        lateinit var retrofitFCMService: RetrofitFCMService

        // header에 accessTocken 넣는 레트로핏
        lateinit var hRetrofit : Retrofit
        lateinit var hRetrofitTokenService: RetrofitTokenService
        lateinit var hRetrofitUserService: RetrofitUserService
        lateinit var hRetrofitEmailService: RetrofitEmailService
        lateinit var hRetrofitChatService: RetrofitChatService
        lateinit var hRetrofitShareService: RetrofitShareService
        lateinit var hRetrofitAskService: RetrofitAskService
        lateinit var hRetrofitAppointmentService: RetrofitAppointmentService
        private lateinit var interceptor: Interceptor

        // 정보를 담기 위한 sharedPreference
        lateinit var prefs: SharedPreferencesUtil
        lateinit var editor : SharedPreferences.Editor


    }

    override fun onCreate() {
        super.onCreate()
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        prefs = SharedPreferencesUtil(applicationContext)

        // Naver OAuth 초기화
//        NaverIdLoginSDK.showDevelopersLog(true)
//        NaverIdLoginSDK.initialize(this,
//            getString(R.string.naver_client_id),
//            getString(R.string.naver_client_secret)
//            , getString(R.string.naver_client_name))

        // Kakao OAuth 초기화
        KakaoSdk.init(this, getString(R.string.kakao_oauth_app_key))

        val gson: Gson = GsonBuilder()
            .setDateFormat("yyyy-mm-dd HH:mm:ss")
            .setLenient()
            .serializeNulls()
            .create()

        val okHttpClient: OkHttpClient = OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .build()

        // 앱이 처음 생성되는 순간, retrofit 인스턴스를 생성
        wRetrofit = Retrofit.Builder()
            .baseUrl(SERVER_URL)
            .addConverterFactory(nullOnEmptyConverterFactory)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()

        retrofitUserService = wRetrofit.create(RetrofitUserService::class.java)
        retrofitEmailService = wRetrofit.create(RetrofitEmailService::class.java)
        retrofitChatService = wRetrofit.create(RetrofitChatService::class.java)
        retrofitShareService = wRetrofit.create(RetrofitShareService::class.java)
        retrofitFCMService = wRetrofit.create(RetrofitFCMService::class.java)
        retrofitAskService = wRetrofit.create(RetrofitAskService::class.java)
        retrofitAppointmentService = wRetrofit.create(RetrofitAppointmentService::class.java)

        // 네트워크에 연결되어있는지 확인 후 없으면 앱 종료 시키기위해 네트워크 연결상태 감지 콜백 생성시켜두기
//        val network: CheckNetwork = CheckNetwork(applicationContext)
//        network.registerNetworkCallback()


        // 여기서부터는 accessToken 추가한 헤더 있는 레트로핏 서비스
        interceptor = AppInterceptor()

        val okHttpHeaderClient: OkHttpClient = OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .writeTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .build()

        // 앱이 처음 생성되는 순간, retrofit 인스턴스를 생성
        hRetrofit = Retrofit.Builder()
            .baseUrl(SERVER_URL)
            .addConverterFactory(nullOnEmptyConverterFactory)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpHeaderClient)
            .build()

        hRetrofitUserService = hRetrofit.create(RetrofitUserService::class.java)
        hRetrofitTokenService = hRetrofit.create(RetrofitTokenService::class.java)
        hRetrofitEmailService = hRetrofit.create(RetrofitEmailService::class.java)
        hRetrofitChatService = hRetrofit.create(RetrofitChatService::class.java)
        hRetrofitShareService = hRetrofit.create(RetrofitShareService::class.java)
        hRetrofitAskService = hRetrofit.create(RetrofitAskService::class.java)
        hRetrofitAppointmentService = hRetrofit.create(RetrofitAppointmentService::class.java)
    }

    inner class AppInterceptor : Interceptor { // End of AppInterceptor inner class
        override fun intercept(chain: Interceptor.Chain): Response {
            var accessToken = prefs.getUserAccessToken()

            val request = chain.request().newBuilder()
                .addHeader("access_token", accessToken)
                .build()

            val response = chain.proceed(newRequestWithAccessToken(accessToken, request))

            if (response.code == HttpURLConnection.HTTP_UNAUTHORIZED) {
                Log.d(TAG, "intercept: @@@@@@권한없음 토큰 갱신 필요")
                if (!refreshToken()) { // 갱신 실패한 경우

                    // 로그아웃 하고 로그인 화면으로 이동
                    prefs.removeAll() // Shared Preference 삭제
                    val intent = Intent(this@ApplicationClass, LoginActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    return response
                }
                return chain.proceed(newRequestWithAccessToken(accessToken, request))
            }
            return response
        }
        /*override fun intercept(chain: Interceptor.Chain): okhttp3.Response = with(chain) {
            val newRequest = request().newBuilder()
                .addHeader("Authorization", prefs.getUserAccessToken())
                .build()
            proceed(newRequest)
        }*/

    }

    private fun newRequestWithAccessToken(accessToken: String?, request: Request): Request =
        request.newBuilder()
            .header("Authorization", "Bearer $accessToken")
            .build()

    private fun refreshToken(): Boolean{
        lateinit var result:RESTResult
        CoroutineScope(Dispatchers.IO).launch {
            result = hRetrofitTokenService.refreshToken(Token(prefs.getRefreshToken())).awaitResponse().body()!!
        }
        return result?.flag == "success"
    }

    private val nullOnEmptyConverterFactory = object : Converter.Factory() {
        fun converterFactory() = this
        override fun responseBodyConverter(type: Type, annotations: Array<out Annotation>, retrofit: Retrofit) = object :
            Converter<ResponseBody, Any?> {
            val nextResponseBodyConverter = retrofit.nextResponseBodyConverter<Any?>(converterFactory(), type, annotations)
            override fun convert(value: ResponseBody) = if (value.contentLength() != 0L) {
                try{
                    nextResponseBodyConverter.convert(value)
                }catch (e:Exception){
                    e.printStackTrace()
                    null
                }
            } else{
                null
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onAppBackgrounded() { isForeground = false }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onAppForegrounded() { isForeground = true}

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onAppCreated() {  }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onAppResumed() {}

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onAppDestroyed() { }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onAppPaused() {  }

    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    fun onAppAny() {  }
}