package kr.co.vilez.ui.chat

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.vilez.R
import kr.co.vilez.databinding.ActivityChatRoomBinding
import kr.co.vilez.databinding.ActivityMainBinding
import kr.co.vilez.ui.HomeFragment
import kr.co.vilez.ui.chat.map.KakaoMapFragment
import kr.co.vilez.util.ApplicationClass
import kr.co.vilez.util.StompClient
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.awaitResponse

//TODO ROOM 정보 저장

class ChatRoomActivity : AppCompatActivity(){
    private lateinit var binding: ActivityChatRoomBinding
    private var roomId = 0
    private var otherUserId = 0
    private val itemList = ArrayList<ChatlistData>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat_room)
        roomId = intent.getIntExtra("roomId",0)
        otherUserId = intent.getIntExtra("otherUserId",0)
        var bundle  = Bundle(2)
        bundle.putInt("roomId",roomId)
        bundle.putInt("otherUserId",otherUserId)
        initView()

        var kakaoMapFragment = KakaoMapFragment()
        kakaoMapFragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, kakaoMapFragment)
            .commit()
    }

    @SuppressLint("CheckResult")
    fun initView() {
        val rv_chat = binding.root.findViewById(R.id.rv_chat) as RecyclerView
        val txt_edit = binding.root.findViewById(R.id.editText1) as EditText
        val roomAdapter = ChatAdapter(itemList)
        txt_edit.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                val data = JSONObject()
                data.put("roomId",roomId)
                data.put("fromUserId",ApplicationClass.prefs.getId())
                data.put("toUserId",otherUserId)
                data.put("content",txt_edit.text)
                data.put("time",System.currentTimeMillis())
                itemList.add(ChatlistData(data.getString("content"), 2))
                roomAdapter.notifyDataSetChanged()
                StompClient.stompClient.send("/recvchat", data.toString()).subscribe()
                txt_edit.setText(null)
                rv_chat.scrollToPosition(itemList.size-1)
                true
            }
           false
        }

        rv_chat.adapter = roomAdapter
        rv_chat.layoutManager = LinearLayoutManager(this)


        CoroutineScope(Dispatchers.Main).launch {
            val result = ApplicationClass.retrofitChatService.loadChatList(roomId).awaitResponse().body()
            println(result)
            if (result?.flag == "success") {
                for(i in 0 until result.data.size) {
                    var chat = result.data.get(i)
                    if(chat.fromUserId == ApplicationClass.prefs.getId())
                        itemList.add(ChatlistData(chat.content, 2))
                    else
                        itemList.add(ChatlistData(chat.content, 1))
                }
                roomAdapter.notifyDataSetChanged()
                rv_chat.scrollToPosition(itemList.size-1)
            }
        }

//        StompClient.stompClient.topic("/sendmy/"+roomId+"/"+ApplicationClass.prefs.getId()).subscribe { topicMessage ->
//            run {
//                CoroutineScope(Dispatchers.Main).launch {
//                    val json = JSONObject(topicMessage.payload)
//                    itemList.add(ChatlistData(json.getString("content"), 2))
//                    roomAdapter.notifyDataSetChanged()
//                }
//            }
//        }
        StompClient.stompClient.topic("/sendchat/"+roomId+"/"+ApplicationClass.prefs.getId()).subscribe { topicMessage ->
            run {
                CoroutineScope(Dispatchers.Main).launch {
                    val json = JSONObject(topicMessage.payload)
                    itemList.add(ChatlistData(json.getString("content"), 1))
                    roomAdapter.notifyDataSetChanged()
                    rv_chat.scrollToPosition(itemList.size-1)
                }
            }
        }


    }
}