package kr.co.vilez.util

import android.content.Context
import android.util.Log
import com.gmail.bishoybasily.stomp.lib.Event
import com.gmail.bishoybasily.stomp.lib.StompClient
import io.reactivex.disposables.Disposable
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

class StompClient2 {
    companion object {
        private var instance: StompClient2? = null

        private lateinit var context: Context

        fun getInstance(_context: Context): StompClient2 {
            return instance ?: synchronized(this) {
                instance ?: StompClient2().also {
                    context = _context
                    instance = it
                }
            }
        }
        val url = "ws://i8d111.p.ssafy.io:8082/vilez/chat/websocket" // 소켓에 연결하는 엔드포인트가 /socket일때 다음과 같음
        lateinit var stompClient: StompClient
        fun runStomp() {
            var stompConnection: Disposable
            var topic: Disposable

            val url = "ws://i8d111.p.ssafy.io:8082/vilez/chat/websocket"
            val intervalMillis = 5000L
            val client = OkHttpClient.Builder()
//                .addInterceptor { it.proceed(it.request().newBuilder().header("Authorization", "bearer 68d20faa-54c4-11e8-8195-98ded0151692").build()) }
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .build()

            stompClient = StompClient(client, intervalMillis).apply { this@apply.url = url }

            // connect
            stompConnection = stompClient.connect().subscribe {
                when (it.type) {
                    Event.Type.OPENED -> {
                        println("opppppppppppppppen")
                        // subscribe

//                // unsubscribe
//                topic.dispose()

                        // send


//                stomp.send("/app/hello", Base64.getEncoder().encodeToString(File("/home/bishoybasily/Desktop/input.jpg").readBytes())).subscribe {
//                    if (it) {
//                    }
//                }


                    }
                    Event.Type.CLOSED -> {
                        println("Cloooooooosed")
                    }
                    Event.Type.ERROR -> {

                    }
                    else -> {}
                }
            }
//            val headerList = arrayListOf<StompHeader>()
//            stompClient.connect(headerList)
//
//            stompClient.lifecycle().subscribe { lifecycleEvent ->
//                when (lifecycleEvent.type) {
//                    LifecycleEvent.Type.OPENED -> {
//                        Log.i("OPEND", "!!")
//                        stompClient.withClientHeartbeat(1000)
//                        stompClient.withServerHeartbeat(1000)
//                    }
//                    LifecycleEvent.Type.CLOSED -> {
//                        stompClient.connect(headerList)
//                        Log.i("CLOSED", "!!")
//
//                    }
//                    LifecycleEvent.Type.ERROR -> {
//                        Log.i("ERROR", "!!")
//                        Log.e("CONNECT ERROR", lifecycleEvent.exception.toString())
//                    }
//                    else ->{
//                        Log.i("ELSE", lifecycleEvent.message)
//                    }
//                }
//            }

//            CoroutineScope(Dispatchers.Main).launch {
//                val result =
//                    ApplicationClass.retrofitChatService.loadRoomList(29).awaitResponse().body()
//                if (result?.flag == "success") {
//                    DataState.itemList = ArrayList<RoomlistData>()
//                    for (index in 0 until result.data.size) {
//                        val chat = result.data.get(index)
//                        DataState.itemList.add(
//                            RoomlistData(
//                                chat.chatData.roomId, chat.nickName,
//                                chat.chatData.content,
//                                chat.area
//                            )
//                        )
//                    }
//                }
//            }


        }
    }

}