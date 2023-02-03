package kr.co.vilez.ui.chat

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.vilez.R
import kr.co.vilez.data.model.SetPeriodDto
import kr.co.vilez.data.model.Room
import kr.co.vilez.databinding.ActivityChatRoomBinding
import kr.co.vilez.ui.chat.map.KakaoMapFragment
import kr.co.vilez.ui.dialog.AlertDialog
import kr.co.vilez.util.ApplicationClass
import kr.co.vilez.util.StompClient2
import org.json.JSONObject
import retrofit2.awaitResponse
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

private const val TAG = "빌리지_채팅_ChatRoomActivity"
class ChatRoomActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatRoomBinding
    private var roomId = 0
    private var otherUserId = 0
    private var nickName = "알수없음"
    private var profile = "https://kr.object.ncloudstorage.com/vilez/basicProfile.png"
    lateinit var topic : Disposable
    private var now : Int = 0
    private val itemList = ArrayList<ChatlistData>()
    private val kakaoMapFragment = KakaoMapFragment()
    private var setPeriodDto : SetPeriodDto? = null
    private var pickedDate : Pair<Long, Long>? = null // 약속 시간 저장하는 변수
    private lateinit var room: Room

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat_room)
        roomId = intent.getIntExtra("roomId", 0)
        otherUserId = intent.getIntExtra("otherUserId", 0)
        nickName = intent.getStringExtra("nickName")!!
        profile = intent.getStringExtra("profile")!!
        var bundle = Bundle(2)
        bundle.putInt("roomId", roomId)
        bundle.putInt("otherUserId", otherUserId)
        CoroutineScope(Dispatchers.Main).launch {
            println(roomId)
            val result = ApplicationClass.retrofitChatService.getRoomData(roomId).awaitResponse().body()
            if (result?.flag == "success") {
                room = result.data.get(0)
                CoroutineScope(Dispatchers.Main).launch {
                    val result = ApplicationClass.retrofitChatService.getAppointment(
                        room.boardId, room.notShareUserId,
                        room.shareUserId,room.type
                    ).awaitResponse().body()
                    if (result?.flag == "success") {
                        setPeriodDto = result.data.get(0)
                    }
                }
            }
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        initView()

//        kakaoMapFragment.arguments = bundle
//
//
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.frameLayout, kakaoMapFragment)
//            .commit()


    }

    override fun onDestroy() {
        super.onDestroy()
        topic.dispose()
    }

    @SuppressLint("CheckResult")
    fun initView() {
        val toolbar_title = binding.root.findViewById(R.id.toolbar_title) as TextView
        val rv_chat = binding.root.findViewById(R.id.rv_chat) as RecyclerView
        val txt_edit = binding.root.findViewById(R.id.editText1) as EditText
        val chat_plus = binding.root.findViewById(R.id.chat_plus) as TextView
        val chat_menu = binding.root.findViewById(R.id.chat_menu) as LinearLayout
        val txt_send = binding.root.findViewById(R.id.sendText) as ImageView
        val btn_back = binding.root.findViewById(R.id.btn_back) as ImageButton

        val roomAdapter = ChatAdapter(itemList)
        toolbar_title.text = nickName
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
                itemList.add(ChatlistData(data.getString("content"), 2,""))
                roomAdapter.notifyDataSetChanged()
                StompClient2.stompClient.send("/recvchat", data.toString()).subscribe()
                txt_edit.setText("")
                rv_chat.scrollToPosition(itemList.size - 1)
            }
        )
        btn_back.setOnClickListener(View.OnClickListener {
            finish()
        })

        binding.btnMap.setOnClickListener {
            if(now == 0) {
                binding.frameLayout.visibility = View.VISIBLE
                binding.chatMenu.visibility = View.GONE
                binding.chatPlus.text = "+"
                now = 3
            } else if(now == 3){
                binding.frameLayout.visibility = View.GONE
                now = 0
            }
        }
        chat_plus.setOnClickListener(View.OnClickListener {
            if(chat_plus.text == "X") {
                chat_plus.text = "+"
                chat_menu.visibility = View.GONE
                rv_chat.scrollToPosition(itemList.size - 1)
            } else {
                chat_plus.text = "X"
                chat_menu.visibility = View.VISIBLE
                rv_chat.scrollToPosition(itemList.size - 1)
            }
        })
        rv_chat.adapter = roomAdapter
        rv_chat.layoutManager = LinearLayoutManager(this)


        CoroutineScope(Dispatchers.Main).launch {
            val result =
                ApplicationClass.retrofitChatService.loadChatList(roomId).awaitResponse().body()
            if (result?.flag == "success") {
                var prev_type = -1
                for (i in 0 until result.data.size) {
                    var chat = result.data.get(i)
                    if (chat.fromUserId == ApplicationClass.prefs.getId()) {
                        itemList.add(ChatlistData(chat.content, 2,null))
                        prev_type = 2
                    }
                    else {
                        if(prev_type == 1)
                            itemList.add(ChatlistData(chat.content, 1,null))
                        else
                            itemList.add(ChatlistData(chat.content, 1,profile))
                        prev_type = 1
                    }
                }
                roomAdapter.notifyDataSetChanged()
                rv_chat.scrollToPosition(itemList.size - 1)
                val data = JSONObject()
                data.put("roomId", roomId)
                data.put("userId", ApplicationClass.prefs.getId())
                StompClient2.stompClient.send("/room_enter", data.toString()).subscribe()
            }
        }

        topic = StompClient2.stompClient.join("/sendchat/" + roomId + "/" + ApplicationClass.prefs.getId())
            .subscribe { topicMessage ->
                run {
                    CoroutineScope(Dispatchers.Main).launch {
                        val json = JSONObject(topicMessage)
                        itemList.add(ChatlistData(json.getString("content"), 1,profile))
                        roomAdapter.notifyDataSetChanged()
                        rv_chat.scrollToPosition(itemList.size - 1)
                        val data = JSONObject()
                        data.put("roomId", roomId)
                        data.put("userId", ApplicationClass.prefs.getId())
                        StompClient2.stompClient.send("/room_enter", data.toString()).subscribe()
                    }
                }
            }


            initScheduleButton()
    }

    private fun initScheduleButton() {
        binding.btnChatCalendar.setOnClickListener {
            if(ApplicationClass.prefs.getId() == room.shareUserId) {
                onCalendarClick()
            } else {
                println("공유자만 가능한 페이집니다.")
                if(setPeriodDto == null) {
                    var dialog = AlertDialog(this, "설정된 예약일자가 없습니다.")
                    dialog.show(this.supportFragmentManager, "Appoint")
                } else {
                    var dialog = AlertDialog(this, "예약 일자 : ${setPeriodDto?.startDay} \n~ ${setPeriodDto?.endDay}")
                    dialog.show(this.supportFragmentManager, "Appoint")
                }
            }
            // 선택된 날짜는 pickedDate 에 저장됨
        }
    }

    fun onCalendarClick() { // 캘린더 버튼클릭
        var startDay:Long = 0
        var endDay:Long = 0
        val SDF = SimpleDateFormat ("yyyy-MM-dd")

        Log.d(TAG, "onCalendarClick: 캘린더 클릭")

        val calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"), Locale.KOREA)
        SDF.timeZone = TimeZone.getTimeZone("Asia/Seoul")
        SDF.calendar = calendar
        if(setPeriodDto == null) {
            calendar.time = Date()
            startDay = calendar.timeInMillis
            calendar.add(Calendar.DATE, 1) // 디폴트 end날짜 : 내일
            endDay = calendar.timeInMillis
        } else {
            var date = SDF.parse(setPeriodDto?.startDay)
            calendar.time = date
            startDay = calendar.timeInMillis + 32400000
            date = SDF.parse(setPeriodDto?.endDay)
            calendar.time = date
            endDay = calendar.timeInMillis + 32400000
        }


        // Build constraints.
        val constraintsBuilder =
            CalendarConstraints.Builder()
                .setStart(Date().time).setValidator(DateValidatorPointForward.now())

        val datePicker =
            MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText("공유기간 선택")
                .setSelection(androidx.core.util.Pair(
                    startDay
                    , endDay))
                .setCalendarConstraints(constraintsBuilder.build())
                .build()

        datePicker.addOnPositiveButtonClickListener {
            pickedDate = Pair<Long, Long>(it.first!!, it.second!!)
            if(pickedDate == null)
                return@addOnPositiveButtonClickListener
            var start : String = SDF.format(Date(pickedDate!!.first))
            var end : String = SDF.format(Date(pickedDate!!.second))
            setPeriodDto  = SetPeriodDto(room.boardId,room.shareUserId,room.notShareUserId,start,end,room.type)
            CoroutineScope(Dispatchers.Main).launch {
                val result = ApplicationClass.retrofitChatService.setAppointment(setPeriodDto!!).awaitResponse().body()
                if (result?.flag == "success") {
                    println("success")
                }
            }


        }
        datePicker.show(supportFragmentManager, "Date")

    }

}