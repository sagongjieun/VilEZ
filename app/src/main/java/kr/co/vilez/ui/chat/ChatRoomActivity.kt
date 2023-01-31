package kr.co.vilez.ui.chat

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.vilez.R
import kr.co.vilez.databinding.ActivityChatRoomBinding
import kr.co.vilez.ui.chat.map.KakaoMapFragment
import kr.co.vilez.util.ApplicationClass
import kr.co.vilez.util.StompClient2
import org.json.JSONObject
import retrofit2.awaitResponse


class ChatRoomActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatRoomBinding
    private var roomId = 0
    private var otherUserId = 0
    lateinit var topic : Disposable
    private val itemList = ArrayList<ChatlistData>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat_room)
        roomId = intent.getIntExtra("roomId", 0)
        otherUserId = intent.getIntExtra("otherUserId", 0)
        var bundle = Bundle(2)
        bundle.putInt("roomId", roomId)
        bundle.putInt("otherUserId", otherUserId)
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        initView()
        var kakaoMapFragment = KakaoMapFragment()
        kakaoMapFragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, kakaoMapFragment)
            .commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        topic.dispose()
    }

    @SuppressLint("CheckResult")
    fun initView() {
        val rv_chat = binding.root.findViewById(R.id.rv_chat) as RecyclerView
        val txt_edit = binding.root.findViewById(R.id.editText1) as EditText
        val chat_plus = binding.root.findViewById(R.id.chat_plus) as TextView
        val chat_menu = binding.root.findViewById(R.id.chat_menu) as LinearLayout
        val plus_chat_send_layout = binding.root.findViewById(R.id.plus_chat_send_layout) as LinearLayout
        val txt_send = binding.root.findViewById(R.id.sendText) as ImageView
        val btn_back = binding.root.findViewById(R.id.btn_back) as ImageButton
        val layoutParams = plus_chat_send_layout.layoutParams as LinearLayout.LayoutParams
        val roomAdapter = ChatAdapter(itemList)
        txt_send.setOnClickListener(
            View.OnClickListener {
                if(txt_edit.text.length <= 0)
                    return@OnClickListener
                println(txt_edit.text.length)
                val data = JSONObject()
                data.put("roomId", roomId)
                data.put("fromUserId", ApplicationClass.prefs.getId())
                data.put("toUserId", otherUserId)
                data.put("content", txt_edit.text)
                data.put("time", System.currentTimeMillis())
                itemList.add(ChatlistData(data.getString("content"), 2))
                roomAdapter.notifyDataSetChanged()
                StompClient2.stompClient.send("/recvchat", data.toString()).subscribe()
                txt_edit.setText("")
                rv_chat.scrollToPosition(itemList.size - 1)
            }
        )
        btn_back.setOnClickListener(View.OnClickListener {
            finish()
        })
        chat_plus.setOnClickListener(View.OnClickListener {
            if(chat_plus.text == "X") {
                chat_plus.text = "+"
                chat_menu.visibility = View.GONE
                layoutParams.weight = 1F
                rv_chat.scrollToPosition(itemList.size - 1)
            } else {
                chat_plus.text = "X"
                chat_menu.visibility = View.VISIBLE
                layoutParams.weight = 1.6F
                rv_chat.scrollToPosition(itemList.size - 1)
            }
        })
        rv_chat.adapter = roomAdapter
        rv_chat.layoutManager = LinearLayoutManager(this)


        CoroutineScope(Dispatchers.Main).launch {
            val result =
                ApplicationClass.retrofitChatService.loadChatList(roomId).awaitResponse().body()
            if (result?.flag == "success") {
                for (i in 0 until result.data.size) {
                    var chat = result.data.get(i)
                    if (chat.fromUserId == ApplicationClass.prefs.getId())
                        itemList.add(ChatlistData(chat.content, 2))
                    else
                        itemList.add(ChatlistData(chat.content, 1))
                }
                roomAdapter.notifyDataSetChanged()
                rv_chat.scrollToPosition(itemList.size - 1)
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
        topic = StompClient2.stompClient.join("/sendchat/" + roomId + "/" + ApplicationClass.prefs.getId())
            .subscribe { topicMessage ->
                run {
                    CoroutineScope(Dispatchers.Main).launch {
                        val json = JSONObject(topicMessage)
                        itemList.add(ChatlistData(json.getString("content"), 1))
                        roomAdapter.notifyDataSetChanged()
                        rv_chat.scrollToPosition(itemList.size - 1)
                    }
                }
            }


    }
}