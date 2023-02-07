package kr.co.vilez.ui.chat

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.core.view.get
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
import kr.co.vilez.data.chat.ChatlistData
import kr.co.vilez.data.model.*
import kr.co.vilez.databinding.ActivityChatRoomBinding
import kr.co.vilez.ui.chat.map.KakaoMapFragment
import kr.co.vilez.ui.dialog.*
import kr.co.vilez.util.ApplicationClass
import kr.co.vilez.util.DataState
import kr.co.vilez.util.StompHelper
import org.json.JSONObject
import retrofit2.awaitResponse
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

private const val TAG = "빌리지_채팅_ChatRoomActivity"
class ChatRoomActivity : AppCompatActivity(), AppointConfirmDialogInterface,
    ReturnConfirmDialogInterface {
    private lateinit var binding: ActivityChatRoomBinding
    private var roomId = 0
    private var otherUserId = 0
    private var nickName = "알수없음"
    private var profile = "https://kr.object.ncloudstorage.com/vilez/basicProfile.png"
    var topic : Disposable?? =  null
    var topic2 : Disposable?? =  null

    private var now : Int = 0
    private val itemList = ArrayList<ChatlistData>()
    private var kakaoMapFragment : KakaoMapFragment?? = null
    private var appointDto: AppointDto? = null
    private var setPeriodDto : SetPeriodDto? = null
    private var pickedDate : Pair<Long, Long>? = null // 약속 시간 저장하는 변
    lateinit var roomAdapter :ChatAdapter
    private lateinit var room: Room

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat_room)
        roomId = intent.getIntExtra("roomId", 0)
        otherUserId = intent.getIntExtra("otherUserId", 0)
        nickName = intent.getStringExtra("nickName")!!
        profile = intent.getStringExtra("profile")!!

        val data = JSONObject()
        data.put("roomId", roomId)
        data.put("userId", ApplicationClass.prefs.getId())
        topic2 = StompHelper.stompClient.join("/sendend/$roomId")
            .subscribe { topicMessage ->
                run {
                    room.state = -1
                    if(topic != null)
                        topic!!.dispose()
                    topic2!!.dispose()
                }
            }

        StompHelper.stompClient.send("/room_enter", data.toString()).subscribe()
        CoroutineScope(Dispatchers.Main).launch {
            val result = ApplicationClass.retrofitChatService.getRoomData(roomId).awaitResponse().body()
            if (result?.flag == "success") {
                room = result.data.get(0)
                // 룸 정보 가져왔고 그다음엔
                // 약속 정보 가져오기

                var result = ApplicationClass.retrofitChatService.getAppointMent(
                    room.boardId, room.notShareUserId,
                    room.shareUserId,room.type
                ).awaitResponse().body()

                if(result?.flag == "success") {
                    appointDto = result.data.get(0)
                    if(appointDto == null) {
                        val result = ApplicationClass.retrofitChatService.getPeriodDto(
                            room.boardId, room.notShareUserId,
                            room.shareUserId,room.type
                        ).awaitResponse().body()
                        if (result?.flag == "success") {
                            setPeriodDto = result.data.get(0)
                        }
                    }
                }

                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                initView()
                initAcceptButton()
            }
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        if(topic != null)
            topic!!.dispose()
    }
       lateinit var rv_chat : RecyclerView

    @SuppressLint("CheckResult")
    fun initView() {
        val toolbar_title = binding.root.findViewById(R.id.toolbar_title) as TextView
        val txt_edit = binding.root.findViewById(R.id.editText1) as EditText
        val chat_plus = binding.root.findViewById(R.id.chat_plus) as TextView
        val chat_menu = binding.root.findViewById(R.id.chat_menu) as LinearLayout
        val txt_send = binding.root.findViewById(R.id.sendText) as ImageView
        val btn_back = binding.root.findViewById(R.id.btn_back) as ImageButton
        rv_chat = binding.root.findViewById(R.id.rv_chat) as RecyclerView
        roomAdapter = ChatAdapter(itemList)
        toolbar_title.text = nickName
            txt_send.setOnClickListener(
                View.OnClickListener {
                    if(room.state == 0) {
                        if(txt_edit.text.length <= 0)
                            return@OnClickListener
                        println(txt_edit.text.length)
                        var data = JSONObject()
                        data.put("roomId", roomId)
                        data.put("fromUserId", ApplicationClass.prefs.getId())
                        data.put("toUserId", otherUserId)
                        data.put("content", txt_edit.text)
                        data.put("time", System.currentTimeMillis())
                        data.put("system",false)
                        itemList.add(ChatlistData(data.getString("content"), 2,""))
                        roomAdapter.notifyDataSetChanged()
                        StompHelper.stompClient.send("/recvchat", data.toString()).subscribe()
                        data = JSONObject()
                        data.put("roomId", roomId)
                        data.put("userId", ApplicationClass.prefs.getId())
                        StompHelper.stompClient.send("/room_enter", data.toString()).subscribe()
                        txt_edit.setText("")
                        rv_chat.scrollToPosition(itemList.size - 1)
                    }
                }
            )

        btn_back.setOnClickListener(View.OnClickListener {
            finish()
        })

        binding.btnMap.setOnClickListener {

            if(kakaoMapFragment == null) {
                var bundle = Bundle(2)
                bundle.putInt("roomId", roomId)
                bundle.putInt("otherUserId", otherUserId)
                kakaoMapFragment = KakaoMapFragment()
                kakaoMapFragment!!.arguments = bundle
                supportFragmentManager.beginTransaction()
                    .replace(R.id.frameLayout, kakaoMapFragment!!)
                    .commit()
            }
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


        //나가기 버튼 처리
        binding.toolbar.menu.get(0).setOnMenuItemClickListener { item ->
            when(item.itemId) {
                R.id.close_room -> {
                    CoroutineScope(Dispatchers.Main).launch {
                        val result = ApplicationClass.retrofitChatService.getState(roomId).awaitResponse().body()
                        if(result?.flag == "success") {
                            var data = result.data.get(0)
                            if(data.state == 0) {
                                showDialog("약속된 정보가 있습니다.\n만남을 취소하고 해주세요!")
                                return@launch
                            } else {
                                val result =
                                    ApplicationClass.retrofitChatService.closeRoom(roomId,ApplicationClass.prefs.getId()).awaitResponse().body()
                                if(result?.flag == "success") {
                                    var data = JSONObject()
                                    data.put("roomId", roomId)
                                    data.put("fromUserId", -1)
                                    data.put("toUserId", otherUserId)
                                    data.put("content", "상대방이 채팅방을 나갔습니다")
                                    data.put("time", System.currentTimeMillis())
                                    data.put("system",true)

                                    if(room.state != -1)
                                        StompHelper.stompClient.send("/recvchat", data.toString()).subscribe()
                                    if(topic!=null)
                                        topic!!.dispose()
                                    var index = 0
                                    for(i in 0 until DataState.itemList.size) {
                                        if(roomId==DataState.itemList[i].roomId) {
                                            index = i
                                            break
                                        }
                                    }
                                    if(DataState.itemList.size == 0) {
                                        DataState.itemList.clear()
                                        DataState.itemList.clear()
                                    } else {
                                        DataState.itemList.removeAt(index)
                                        DataState.set.remove(room.id)
                                    }
                                    finish()
                                }
                            }
                        }
                    }
                 true
                }
            }
            true
        }
        CoroutineScope(Dispatchers.Main).launch {
            val result =
                ApplicationClass.retrofitChatService.loadChatList(roomId).awaitResponse().body()
            if (result?.flag == "success") {
                var prev_type = -1
                for (i in 0 until result.data.size) {
                    var chat = result.data.get(i)
                    if (chat.system) {
                        itemList.add(ChatlistData(chat.content, 0,null))
                        prev_type = 0;
                    } else {
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
                }
                roomAdapter.notifyDataSetChanged()
                rv_chat.scrollToPosition(itemList.size - 1)
                val data = JSONObject()
                data.put("roomId", roomId)
                data.put("userId", ApplicationClass.prefs.getId())
                StompHelper.stompClient.send("/room_enter", data.toString()).subscribe()
            }
        }
        if(room.state == 0) {
            topic = StompHelper.stompClient.join("/sendchat/" + roomId + "/" + ApplicationClass.prefs.getId())
                .subscribe { topicMessage ->
                    run {
                        CoroutineScope(Dispatchers.Main).launch {
                            val json = JSONObject(topicMessage)
                            if(json.getInt("fromUserId") == -1) {
                                room.state = -1
                                topic!!.dispose()
                            }
                            if(json.getBoolean("system"))
                                itemList.add(ChatlistData(json.getString("content"), 0,"none"))
                            else
                                itemList.add(ChatlistData(json.getString("content"), 1,profile))
                            roomAdapter.notifyDataSetChanged()
                            rv_chat.scrollToPosition(itemList.size - 1)
                            val data = JSONObject()
                            data.put("roomId", roomId)
                            data.put("userId", ApplicationClass.prefs.getId())
                            StompHelper.stompClient.send("/room_enter", data.toString()).subscribe()
                        }
                    }
                }
        }

        initScheduleButton()
        initSignButton()
        binding.btnChatReturn.setOnClickListener {
            initReturnButton()
        }
    }

    private fun initSignButton() {
        binding.btnChatSign.setOnClickListener {
            showSignDialog("피공유자 ")
//            if(ApplicationClass.prefs.getId() == room.shareUserId) {
//
//                showDialog("피공유자에게 서명을 요청하세요!!")
//            } else {
//                // 서명이 없다면
//
//            }
        }
    }

    private fun initScheduleButton() {
        binding.btnChatCalendar.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                if(ApplicationClass.prefs.getId() == room.shareUserId) {
                    var result = ApplicationClass.retrofitChatService.getAppointMent(room.boardId,room.notShareUserId,room.shareUserId,room.type)
                        .awaitResponse().body()
                    if(result?.flag == "success") {
                        appointDto = result.data.get(0)
                        if(appointDto != null) {
                            showDialog("예약 일자 : ${appointDto?.appointmentStart} \n~ ${appointDto?.appointmentEnd}")
                        } else {
                            onCalendarClick()
                        }
                    }
                } else {
                    var result = ApplicationClass.retrofitChatService.getPeriodDto(room.boardId,room.notShareUserId,room.shareUserId,room.type)
                        .awaitResponse().body()
                    if(result?.flag == "success") {
                        setPeriodDto = result.data.get(0)
                        if(setPeriodDto == null) {
                            showDialog("설정된 예약일자가 없습니다.")
                        } else {
                            if(appointDto != null) {
                                showDialog("예약 일자 : ${appointDto?.appointmentStart} \n~ ${appointDto?.appointmentEnd}")
                            } else {
                                showDialog("예약 일자 : ${setPeriodDto?.startDay} \n~ ${setPeriodDto?.endDay}")
                            }
                        }
                    }

                }
            }

        }
    }
    private fun initAcceptButton() {
        if(room.state != 0) {
            showDialog("종료된 대화방입니다.")
            return
        }
        binding.btnChatAccept.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                val result = ApplicationClass.retrofitChatService.getAppointMent(
                    room.boardId, room.notShareUserId,
                    room.shareUserId,room.type
                ).awaitResponse().body()
                if(result?.flag == "success") {
                    if(ApplicationClass.prefs.getId() == room.shareUserId) {
                        showDialog("만남 확정은 피공유자만 할 수 있습니다.")
                        return@launch
                    }
                    appointDto = result.data.get(0)
                    if(appointDto != null) {
                        showDialog("이미 약속된 정보가 있습니다.")
                        return@launch
                    } else {
                        val result = ApplicationClass.retrofitChatService.getPeriodDto(
                            room.boardId, room.notShareUserId,
                            room.shareUserId,room.type
                        ).awaitResponse().body()
                        if (result?.flag == "success") {
                            setPeriodDto = result.data.get(0)
                            if(setPeriodDto != null) {
                                var resultSign = ApplicationClass.retrofitChatService.getSign(roomId).awaitResponse().body()
                                if(resultSign?.flag == "success") {
                                    var sign = resultSign.data.get(0)
                                    if(sign == null) {
                                        showDialog("서약서에 서명하세요!!")
                                    } else {
                                        //만남 확정하기 버튼 클릭
                                        showAcceptDialog()
                                    }
                                }
                            } else {
                                showDialog("공유자에게 날짜 설정을 요청하세요!!")
                            }
                        }
                    }
                }


            }
        }
    }
    fun showAcceptDialog() {
        var dialog = AppointConfirmDialog(this, room, nickName,setPeriodDto!!.startDay,setPeriodDto!!.endDay)
        dialog.show(this.supportFragmentManager, "Appoint")
    }

    fun showSignDialog(msg: String) {
        var dialog = SignDialog(this, room.id, room.notShareUserId, nickName)
        dialog.show(this.supportFragmentManager, "Appoint")
    }

    fun showReturnDialog() {
        var dialog = ReturnConfirmDialog(this, nickName)
        dialog.show(this.supportFragmentManager, "Appoint")
    }

    fun showDialog(msg: String) {
        var dialog = AlertDialog(this, msg)
        dialog.show(this.supportFragmentManager, "Appoint")
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
            if(room.state != 0) return@addOnPositiveButtonClickListener
            pickedDate = Pair<Long, Long>(it.first!!, it.second!!)
            if(pickedDate == null)
                return@addOnPositiveButtonClickListener
            var start : String = SDF.format(Date(pickedDate!!.first))
            var end : String = SDF.format(Date(pickedDate!!.second))
            setPeriodDto  = SetPeriodDto(room.boardId,room.shareUserId,room.notShareUserId,start,end,room.type)
            CoroutineScope(Dispatchers.Main).launch {
                val result = ApplicationClass.retrofitChatService.setPeriodDto(setPeriodDto!!).awaitResponse().body()
                if (result?.flag == "success") {
                    var data = JSONObject()
                    data.put("roomId", roomId)
                    data.put("fromUserId", ApplicationClass.prefs.getId())
                    data.put("toUserId", otherUserId)
                    data.put("content", "공유자가 날짜 선택을 완료했어요")
                    data.put("time", System.currentTimeMillis())
                    data.put("system",true)
                    StompHelper.stompClient.send("/recvchat", data.toString()).subscribe()
                    itemList.add(ChatlistData(data.getString("content"), 0,"none"))
                    roomAdapter.notifyDataSetChanged()
                    rv_chat.scrollToPosition(itemList.size - 1)
                }
            }
        }
        datePicker.show(supportFragmentManager, "Date")

    }

    // 반납 로직
    // 공유자가 먼저 한 후 피공유자 시작
    fun initReturnButton() {

        CoroutineScope(Dispatchers.Main).launch {
            var result = ApplicationClass.retrofitChatService.getAppointMent(
                room.boardId, room.notShareUserId,
                room.shareUserId,room.type
            ).awaitResponse().body()

            if(result?.flag == "success") {
                appointDto = result.data.get(0)
                if(appointDto == null) {
                    return@launch
                }
                var result = ApplicationClass.retrofitChatService.getReturns(room.id).awaitResponse().body()
                if(result?.flag == "success") {
                    var data = result.data.get(0)
                    if(ApplicationClass.prefs.getId() == room.shareUserId) { // 공유자 입장
                        if(data.state) { // 이미 했으면
                            showDialog("평가를 완료 하였습니다.\n피공유자에게 종료를 요청하세요!")
                        } else { // 평가 다이어로그 띄우기
                            showReturnDialog()
                        }
                    } else {
                        if(data.state) { // 이미 했으면
                            showDialog("공유가 종료되었습니다.")
                            var datad = JSONObject()
                            datad.put("roomId", roomId)
                            datad.put("fromUserId", ApplicationClass.prefs.getId())
                            datad.put("toUserId", otherUserId)
                            datad.put("content", "공유가 종료 되었습니다.")
                            datad.put("time", System.currentTimeMillis())
                            datad.put("system",true)
                            itemList.add(ChatlistData(datad.getString("content"), 0,"none"))

                            roomAdapter.notifyDataSetChanged()
                            StompHelper.stompClient.send("/recvchat", datad.toString()).subscribe()

                            var data = JSONObject()
                            data.put("roomId",room.id)
                            StompHelper.stompClient.send("/recvend",data.toString()).subscribe()
                        } else {
                            showDialog("공유자에게 평가를 요청하세요!")
                        }
                    }
                    rv_chat.scrollToPosition(itemList.size - 1)
                }
            }
        }

    }

    override fun onYesButtonClick() {
        val SDF = SimpleDateFormat ("yyyy-MM-dd")

        Log.d(TAG, "onCalendarClick: 캘린더 클릭")

        val calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"), Locale.KOREA)
        SDF.timeZone = TimeZone.getTimeZone("Asia/Seoul")
        SDF.calendar = calendar

        var date = SDF.format(Date(System.currentTimeMillis()))

        var appointVO = AppointmentDto(room.boardId,room.type,"asd",
                                    room.shareUserId,room.notShareUserId,
                                   setPeriodDto!!.startDay,setPeriodDto!!.endDay,
                                             date,room.id)
        var data = JSONObject()
        data.put("roomId",appointVO.roomId)
        data.put("appointmentId",0)
        data.put("boardId",appointVO.boardId)
        data.put("shareUserId",appointVO.shareUserId)
        data.put("notShareUserId",appointVO.notShareUserId)
        data.put("appointmentStart",appointVO.appointmentStart)
        data.put("appointmentEnd",appointVO.appointmentEnd)
        data.put("state",0)
        data.put("date",appointVO.date)
        data.put("type",appointVO.type)
        StompHelper.stompClient.send("/recvappoint", data.toString()).subscribe()

        data = JSONObject()
        data.put("roomId", roomId)
        data.put("fromUserId", ApplicationClass.prefs.getId())
        data.put("toUserId", otherUserId)
        data.put("content", "공유를 시작 했어요")
        data.put("time", System.currentTimeMillis())
        data.put("system",true)
        StompHelper.stompClient.send("/recvchat", data.toString()).subscribe()
        itemList.add(ChatlistData(data.getString("content"), 0,"none"))
        roomAdapter.notifyDataSetChanged()
        rv_chat.scrollToPosition(itemList.size - 1)
    }

    override fun onYesButtonClick(cnt: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            var map = HashMap<String,Int>()
            map.put("userId",room.notShareUserId)
            map.put("degree",cnt)
            val result = ApplicationClass.retrofitUserService.setManner(map).awaitResponse().body()
            if (result?.flag == "success") {
                val result = ApplicationClass.retrofitChatService.returnRequest(ReturnRequestDto(roomId)).awaitResponse().body()
                if(result?.flag == "success") {
                    var data = JSONObject()
                    data.put("roomId", roomId)
                    data.put("fromUserId", ApplicationClass.prefs.getId())
                    data.put("toUserId", otherUserId)
                    data.put("content", "반납이 확인됐어요 \uD83D\uDE42")
                    data.put("time", System.currentTimeMillis())
                    data.put("system",true)
                    StompHelper.stompClient.send("/recvchat", data.toString()).subscribe()
                    itemList.add(ChatlistData(data.getString("content"), 0,"none"))
                    roomAdapter.notifyDataSetChanged()
                    rv_chat.scrollToPosition(itemList.size - 1)
                }
            }
        }
    }


}