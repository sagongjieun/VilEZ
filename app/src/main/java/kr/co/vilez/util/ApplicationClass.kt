package kr.co.vilez.util

import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.kakao.sdk.common.KakaoSdk
import com.navercorp.nid.NaverIdLoginSDK
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.vilez.R
import kr.co.vilez.data.model.RESTResult
import kr.co.vilez.service.*
import kr.co.vilez.ui.LoginActivity
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
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
//        lateinit var userApi: UserApi
//
//        lateinit var chatApi: ChatApi
//        lateinit var shareApi: ShareApi
//        lateinit var askApi: AskApi
//        lateinit var appointmentApi: AppointmentApi
//        lateinit var emailApi: EmailApi
        lateinit var fcmTokenApi: FCMTokenApi

        // header에 accessTocken 넣는 레트로핏
        lateinit var hRetrofit : Retrofit
        lateinit var hTokenApi: TokenApi
        lateinit var hUserApi: UserApi
        lateinit var hEmailApi: EmailApi
        lateinit var hChatApi: ChatApi
        lateinit var hShareApi: ShareApi
        lateinit var hAskApi: AskApi
        lateinit var hAppointmentApi: AppointmentApi
        private lateinit var interceptor: Interceptor

        // 정보를 담기 위한 sharedPreference
        lateinit var prefs: SharedPreferencesUtil
        lateinit var editor : SharedPreferences.Editor


        // 네트워크 상태
        var isNetworkConnected = false
        lateinit var networkMonitor: NetworkMonitor

    }

    override fun onCreate() {
        super.onCreate()
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        prefs = SharedPreferencesUtil(applicationContext)

        // 네트워크에 연결되어있는지 확인 후 없으면 앱 종료 시키기위해 네트워크 연결상태 감지 콜백 생성시켜두기
        //isNetworkConnected = CheckNetwork.checkNetworkState(applicationContext)

        // Naver OAuth 초기화
        NaverIdLoginSDK.showDevelopersLog(true)
        NaverIdLoginSDK.initialize(this,
            getString(R.string.naver_client_id),
            getString(R.string.naver_client_secret)
            , getString(R.string.naver_client_name))

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
            //.addInterceptor(NoConnectionInterceptor(applicationContext))
            .build()

        // 앱이 처음 생성되는 순간, retrofit 인스턴스를 생성
        wRetrofit = Retrofit.Builder()
            .baseUrl(SERVER_URL)
            .addConverterFactory(nullOnEmptyConverterFactory)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()
//        userApi = wRetrofit.create(UserApi::class.java)
//        emailApi = wRetrofit.create(EmailApi::class.java)
//        chatApi = wRetrofit.create(ChatApi::class.java)
//        shareApi = wRetrofit.create(ShareApi::class.java)
//        askApi = wRetrofit.create(AskApi::class.java)
//        appointmentApi = wRetrofit.create(AppointmentApi::class.java)
        fcmTokenApi = wRetrofit.create(FCMTokenApi::class.java)

        // 네트워크에 연결되어있는지 확인 후 없으면 앱 종료 시키기위해 네트워크 연결상태 감지 콜백 생성시켜두기
        val network = CheckNetworkState(applicationContext)
        network.registerNetworkCallback()


        // 여기서부터는 accessToken 추가한 헤더 있는 레트로핏 서비스
        interceptor = AppInterceptor()

        val okHttpHeaderClient: OkHttpClient = OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .writeTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            //.addInterceptor(NoConnectionInterceptor(applicationContext))
            .build()

        // 앱이 처음 생성되는 순간, retrofit 인스턴스를 생성
        hRetrofit = Retrofit.Builder()
            .baseUrl(SERVER_URL)
            .addConverterFactory(nullOnEmptyConverterFactory)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpHeaderClient)
            .build()

        hUserApi = hRetrofit.create(UserApi::class.java)
        hTokenApi = hRetrofit.create(TokenApi::class.java)
        hEmailApi = hRetrofit.create(EmailApi::class.java)
        hChatApi = hRetrofit.create(ChatApi::class.java)
        hShareApi = hRetrofit.create(ShareApi::class.java)
        hAskApi = hRetrofit.create(AskApi::class.java)
        hAppointmentApi = hRetrofit.create(AppointmentApi::class.java)
    }

    private fun newRequestWithRefreshToken(accessToken: String?, request: Request): Request =
        request.newBuilder()
            .addHeader("refresh-token", prefs.getRefreshToken())
            .build()


    private fun refreshToken(): Boolean{
        lateinit var result:RESTResult
        CoroutineScope(Dispatchers.IO).launch {
            result = hTokenApi.refreshToken().awaitResponse().body()!!
        }
        return result?.flag == "success"
    }


    inner class AppInterceptor : Interceptor { // End of AppInterceptor inner class
        override fun intercept(chain: Interceptor.Chain): Response {
            var accessToken = prefs.getUserAccessToken()

            val request = chain.request().newBuilder()
                .addHeader("access-token", accessToken)
                .build()

            val response = chain.proceed(newRequestWithRefreshToken(accessToken, request))

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
                return chain.proceed(newRequestWithRefreshToken(accessToken, request))
            }
//            else if (!networkMonitor.isConnected) {
//                AlertDialog.Builder(this@ApplicationClass)
//                    .setTitle("네트워크 오류")
//                    .setMessage("네트워크에 접속할 수 없습니다.\n네트워크 연결상태를 확인해주세요.")
//                    .create()
//                    .show()
//            }
            return response
        }
    }

    inner class NoConnectionInterceptor(
        private val context: Context
    ) : Interceptor {

        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        override fun intercept(chain: Interceptor.Chain): Response {
            return if (!isConnectionOn()) {
                throw NoConnectivityException()
            } else {
                chain.proceed(chain.request())
            }
        }

        private fun preAndroidMInternetCheck(
            connectivityManager: ConnectivityManager
        ): Boolean {
            val activeNetwork = connectivityManager.activeNetworkInfo

            if (activeNetwork != null) {
                return (activeNetwork.type == ConnectivityManager.TYPE_WIFI ||
                        activeNetwork.type == ConnectivityManager.TYPE_MOBILE)
            }
            return false
        }

        private fun postAndroidMInternetCheck(
            connectivityManager: ConnectivityManager
        ): Boolean {
            val network = connectivityManager.activeNetwork
            val connection = connectivityManager.getNetworkCapabilities(network)

            return connection != null && (
                    connection.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                            connection.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
        }

        private fun isConnectionOn(): Boolean {
            val connectivityManager = context.getSystemService(
                Context.CONNECTIVITY_SERVICE
            ) as ConnectivityManager

            return if (android.os.Build.VERSION.SDK_INT >=
                android.os.Build.VERSION_CODES.M) {
                postAndroidMInternetCheck(connectivityManager)
            } else {
                preAndroidMInternetCheck(connectivityManager)
            }
        }

        inner class NoConnectivityException : IOException() {

            override val message: String
                get() = "인터넷 연결이 끊겼습니다. WIFI나 데이터 연결을 확인해주세요"

            fun showAlert() {
                AlertDialog.Builder(this@ApplicationClass)
                    .setTitle("네트워크 오류")
                    .setMessage("네트워크에 접속할 수 없습니다.\n네트워크 연결상태를 확인해주세요.")
                    .create()
                    .show()
            }
        }

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