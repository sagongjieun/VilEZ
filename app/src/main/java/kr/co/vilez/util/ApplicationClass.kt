package kr.co.vilez.util

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kr.co.vilez.data.model.User
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.io.IOException
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit

private const val TAG = "빌리지_ApplicationClass"
class ApplicationClass: Application() {

    val SERVER_URL = "http://i8d111.p.ssafy.io:8082"

    companion object {
        // 전역변수 문법을 통해 Retrofit 인스턴스를 앱 실행 시 1번만 생성하여 사용 (싱글톤 객체)
        lateinit var wRetrofit : Retrofit
        lateinit var retrofitUserService: RetrofitUserService
        lateinit var retrofitEmailService: RetrofitEmailService
        lateinit var retrofitChatService: RetrofitChatService
        lateinit var retrofitShareService: RetrofitShareService

        // header에 accessTocken 넣는 레트로핏
        lateinit var hRetrofit : Retrofit
        lateinit var hRetrofitUserService: RetrofitUserService
        lateinit var hRetrofitEmailService: RetrofitEmailService
        lateinit var hRetrofitChatService: RetrofitChatService
        private lateinit var interceptor: Interceptor

        // 로그인 정보를 담기 위한 sharedPreference
        lateinit var sharedPreferences: SharedPreferences
        lateinit var editor : SharedPreferences.Editor
        lateinit var user: User
    }

    override fun onCreate() {
        super.onCreate()

        sharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE)
        editor = sharedPreferences.edit()
        editor.commit() // data 저장

        val gson: Gson = GsonBuilder()
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
        Log.d(TAG, "onCreate: ")

        // 네트워크에 연결되어있는지 확인 후 없으면 앱 종료 시키기위해 네트워크 연결상태 감지 콜백 생성시켜두기
//        val network: CheckNetwork = CheckNetwork(applicationContext)
//        network.registerNetworkCallback()


        //interceptor = AppInterceptor()

    }

//    inner class AppInterceptor : Interceptor {
//        @Throws(IOException::class)
//        override fun intercept(chain: Interceptor.Chain): Response = with(chain) {
//            val newRequest = request().newBuilder()
//                .addHeader("Authorization", sharedPreferencesUtil.getUserAccessToken())
//                .build()
//            proceed(newRequest)
//        }
//    } // End of AppInterceptor inner class

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


}