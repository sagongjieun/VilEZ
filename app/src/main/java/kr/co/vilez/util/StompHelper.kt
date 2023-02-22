package kr.co.vilez.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import com.gmail.bishoybasily.stomp.lib.Event
import com.gmail.bishoybasily.stomp.lib.StompClient
import io.reactivex.disposables.Disposable
import kr.co.vilez.ui.SplashActivity
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

private const val TAG = "빌리지_StompHelper"
class StompHelper {
    @SuppressLint("StaticFieldLeak")
    companion object {
        private var instance: StompHelper? = null

        private var context: Context? = null

        fun getInstance(_context: Context): StompHelper {
            return instance ?: synchronized(this) {
                instance ?: StompHelper().also {
                    context = _context
                    instance = it
                }
            }
        }
        lateinit var stompClient: StompClient
        var stompConnection: Disposable?? = null

        fun reconnect() {

        }

        fun runStomp(mContext: Context) {
            context = mContext
            var topic: Disposable

//          .addInterceptor { it.proceed(it.request().newBuilder().header("Authorization", "bearer 68d20faa-54c4-11e8-8195-98ded0151692").build()) } 헤더값
            val url = "https://i8d111.p.ssafy.io/vilez/chat/websocket"
            val intervalMillis = 1000L
            val client = OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.HOURS)
                .pingInterval(10L,TimeUnit.SECONDS)
                .build()



            stompClient = StompClient(client, intervalMillis).apply { this@apply.url = url }

            stompConnection = stompClient.connect().subscribe {
                when (it.type) {
                    Event.Type.OPENED -> {

                    }
                    Event.Type.CLOSED -> {
                        Log.d(TAG, "runStomp: CLOSED @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@Stomp 끊김!@@@@@@@@@@@@@@@@@@@@")
                        //stompClient.connect()
                    }
                    Event.Type.ERROR -> {
                        Log.d(TAG, "runStomp: ERRROR @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@Stomp 끊김!@@@@@@@@@@@@@@@@@@@@")
                        if(context != null) {
                            val intent = Intent(context!!, SplashActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            context!!.startActivity(intent)
                        }
                    }
                    else -> {}
                }
            }
        }

        fun dispose() {
            stompConnection?.dispose()
        }
    }

}