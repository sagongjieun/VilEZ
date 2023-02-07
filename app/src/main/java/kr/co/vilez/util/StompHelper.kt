package kr.co.vilez.util

import android.content.Context
import com.gmail.bishoybasily.stomp.lib.Event
import com.gmail.bishoybasily.stomp.lib.StompClient
import io.reactivex.disposables.Disposable
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

class StompHelper {
    companion object {
        private var instance: StompHelper? = null

        private lateinit var context: Context

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

        fun runStomp() {
            var topic: Disposable

//          .addInterceptor { it.proceed(it.request().newBuilder().header("Authorization", "bearer 68d20faa-54c4-11e8-8195-98ded0151692").build()) } 헤더값
            val url = "https://i8d111.p.ssafy.io/vilez/chat/websocket"
            val intervalMillis = 1000L
            val client = OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .pingInterval(10L,TimeUnit.SECONDS)
                .build()

            stompClient = StompClient(client, intervalMillis).apply { this@apply.url = url }

            stompConnection = stompClient.connect().subscribe {
                when (it.type) {
                    Event.Type.OPENED -> {

                    }
                    Event.Type.CLOSED -> {

                    }
                    Event.Type.ERROR -> {

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