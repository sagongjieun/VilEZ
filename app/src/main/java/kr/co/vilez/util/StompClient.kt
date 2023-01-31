package kr.co.vilez.util

import android.content.Context
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.vilez.ui.chat.RoomlistData
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.awaitResponse
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.dto.LifecycleEvent
import ua.naiksoftware.stomp.dto.StompHeader

class StompClient {
    companion object {
        private var instance: StompClient? = null

        private lateinit var context: Context

        fun getInstance(_context: Context): StompClient {
            return instance ?: synchronized(this) {
                instance ?: StompClient().also {
                    context = _context
                    instance = it
                }
            }
        }
        val url = "ws://i8d111.p.ssafy.io:8082/vilez/chat/websocket" // 소켓에 연결하는 엔드포인트가 /socket일때 다음과 같음
        val stompClient =  Stomp.over(Stomp.ConnectionProvider.OKHTTP, url)

        fun runStomp() {
            val headerList = arrayListOf<StompHeader>()
            stompClient.connect(headerList)

            stompClient.lifecycle().subscribe { lifecycleEvent ->
                when (lifecycleEvent.type) {
                    LifecycleEvent.Type.OPENED -> {
                        Log.i("OPEND", "!!")
                    }
                    LifecycleEvent.Type.CLOSED -> {
                        stompClient.connect(headerList)
                        Log.i("CLOSED", "!!")

                    }
                    LifecycleEvent.Type.ERROR -> {
                        Log.i("ERROR", "!!")
                        Log.e("CONNECT ERROR", lifecycleEvent.exception.toString())
                    }
                    else ->{
                        Log.i("ELSE", lifecycleEvent.message)
                    }
                }
            }

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