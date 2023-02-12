package kr.co.vilez.ui.chat

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewTreeObserver
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.datepicker.*
import com.google.android.material.datepicker.CalendarConstraints.DateValidator
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.vilez.R
import kr.co.vilez.data.chat.ChatlistData
import kr.co.vilez.data.model.*
import kr.co.vilez.databinding.ActivityChatRoomBinding
import kr.co.vilez.ui.ask.AskDetailActivity
import kr.co.vilez.ui.chat.map.KakaoMapFragment
import kr.co.vilez.ui.dialog.*
import kr.co.vilez.ui.share.ShareDetailActivity
import kr.co.vilez.util.*
import org.json.JSONObject
import retrofit2.awaitResponse
import java.text.SimpleDateFormat
import java.util.*


private const val TAG = "빌리지_채팅_ChatRoomActivity"
class ChatRoomActivity : AppCompatActivity(), AppointConfirmDialogInterface,
    ReturnConfirmDialogInterface, DatePickerDialog.OnDateSetListener {
    private lateinit var binding: ActivityChatRoomBinding
    private var roomId = 0
    private var otherUserId = 0
    private var nickName = "알수없음"
    private var profile = "https://kr.object.ncloudstorage.com/vilez/basicProfile.png"
    var boardStart = ""
    var boardEnd = ""
    var topic : Disposable?? =  null
    var topic2 : Disposable?? =  null
    var init = 0
    var chatSizeCheck = false
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

        init = intent.getIntExtra("init",0)
        intent.removeExtra("init")
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat_room)
        roomId = intent.getIntExtra("roomId", 0)
        otherUserId = intent.getIntExtra("otherUserId", 0)
        nickName = intent.getStringExtra("nickName")!!
        profile = intent.getStringExtra("profile")!!

        binding.activity = this
        println(otherUserId)
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
            val result = ApplicationClass.chatApi.getRoomData(roomId).awaitResponse().body()
            if (result?.flag == "success") {
                room = result.data.get(0)
                // 룸 정보 가져왔고 그다음엔
                // 약속 정보 가져오기

                var result = ApplicationClass.chatApi.getAppointMent(
                    room.boardId, room.notShareUserId,
                    room.shareUserId,room.type
                ).awaitResponse().body()

                if(result?.flag == "success") {
                    appointDto = result.data.get(0)
                    if(appointDto == null) {
                        val result = ApplicationClass.chatApi.getPeriodDto(
                            room.boardId, room.notShareUserId,
                            room.shareUserId,room.type
                        ).awaitResponse().body()
                        if (result?.flag == "success") {
                            setPeriodDto = result.data.get(0)
                        }
                    }
                }

                if(room.type == 2) {
                    var result = ApplicationClass.hShareApi.getBoardDetail(room.boardId).awaitResponse().body()
                    if(result?.flag == "success") {
                        boardStart = result.data.get(0).startDay
                        boardEnd = result.data.get(0).endDay
                    }
                } else {
                    var result = ApplicationClass.hAskApi.getBoardDetail(room.boardId).awaitResponse().body()
                    if(result?.flag == "success") {
                        boardStart = result.data.get(0).startDay
                        boardEnd = result.data.get(0).endDay
                    }
                }
                initView()
                initAcceptButton()
                initCancelButton()
            }
        }

        val activityRootView = binding.chatroomActivityRoot
        activityRootView.viewTreeObserver.addOnGlobalLayoutListener( ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            fun onGlobalLayout() {
                val heightDiff = activityRootView.rootView.height - activityRootView.height;
                if (heightDiff > dpToPx(this@ChatRoomActivity, 200f)) { // if more than 200 dp, it's probably a keyboard...
                    // ... do something here
                    Log.d(TAG, "@@@@@@@@@@@@@@@@2onGlobalLayout: 키보드 높이입니다 : $heightDiff")
                }
            }
        })
    }

    private fun dpToPx(context: Context, valueInDp: Float): Float {
        val metrics = context.resources.displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics)
    }

    override fun onBackPressed() {
        if(isOpen) {
            binding.chatPlus.background = resources.getDrawable(R.drawable.ic_chat_add)
            binding.chatMenu.visibility = View.GONE
            binding.rvChat.scrollToPosition(itemList.size - 1)
            isOpen = !isOpen
        } else {
            super.onBackPressed()
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
        //val chat_plus = binding.root.findViewById(R.id.chat_plus) as TextView
        //val chat_menu = binding.root.findViewById(R.id.chat_menu) as LinearLayout
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
                        itemList.add(ChatlistData(data.getString("content"), 2,"",ApplicationClass.prefs.getId()))
                        roomAdapter.notifyDataSetChanged()
                        StompHelper.stompClient.send("/recvchat", data.toString()).subscribe()
                        data = JSONObject()
                        data.put("roomId", roomId)
                        data.put("userId", ApplicationClass.prefs.getId())
                        StompHelper.stompClient.send("/room_enter", data.toString()).subscribe()
                        txt_edit.setText("")
                        rv_chat.scrollToPosition(itemList.size - 1)
                        if(itemList.size>=5 && !chatSizeCheck) {
                            chatSizeCheck = true

//                            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                        }
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
                binding.chatPlus.background = resources.getDrawable(R.drawable.ic_chat_add)
                // binding.chatPlus.text = "+"
                now = 3
            } else if(now == 3){
                binding.frameLayout.visibility = View.GONE
                now = 0
            }
        }

//        binding.chatPlus.setOnClickListener(View.OnClickListener {
//            /*if(chat_plus.background == resources.getDrawable(R.drawable.ic_chat_close)) {
//                binding.chatPlus.background = resources.getDrawable(R.drawable.ic_chat_add)
//                chat_menu.visibility = View.GONE
//                rv_chat.scrollToPosition(itemList.size - 1)
//            } else {
//                binding.chatPlus.background = resources.getDrawable(R.drawable.ic_chat_close)
//                chat_menu.visibility = View.VISIBLE
//                rv_chat.scrollToPosition(itemList.size - 1)
//            }
//            *//*if(chat_plus.text == "X") {
//                chat_plus.text = "+"
//                chat_menu.visibility = View.GONE
//                rv_chat.scrollToPosition(itemList.size - 1)
//            } else {
//                chat_plus.text = "X"
//                chat_menu.visibility = View.VISIBLE
//                rv_chat.scrollToPosition(itemList.size - 1)
//            }*/
//        })
        rv_chat.adapter = roomAdapter
        rv_chat.layoutManager = LinearLayoutManager(this)

        binding.toolbar.menu.get(0).setOnMenuItemClickListener { item ->
            when(item.itemId) {
                R.id.board -> {
                    var intent: Intent? = null
                    when(room.type) {
                        Common.BOARD_TYPE_SHARE -> {
                            intent = Intent(this, ShareDetailActivity::class.java)
                        }
                        Common.BOARD_TYPE_ASK -> {
                            intent = Intent(this, AskDetailActivity::class.java)
                        }
                    }
                    intent?.putExtra("userId", ApplicationClass.prefs.getId())
                    intent?.putExtra("boardId", room.boardId)
                    startActivity(intent)
                }
            }
            true
        }
        //나가기 버튼 처리
        binding.toolbar.menu.get(1).setOnMenuItemClickListener { item ->
            when(item.itemId) {
                R.id.close_room -> {
                    CoroutineScope(Dispatchers.Main).launch {
                        val result = ApplicationClass.chatApi.getState(roomId).awaitResponse().body()
                        if(result?.flag == "success") {
                            var data = result.data.get(0)
                            if(data.state == 0) {
                                showDialog("약속된 정보가 있습니다.\n만남을 취소하고 해주세요!")
                                return@launch
                            } else {
                                val result =
                                    ApplicationClass.chatApi.closeRoom(roomId,ApplicationClass.prefs.getId()).awaitResponse().body()
                                if(result?.flag == "success") {
                                    var data = JSONObject()
                                    data.put("roomId", roomId)
                                    data.put("fromUserId", -1)
                                    data.put("toUserId", otherUserId)
                                    data.put("content", "대화가 종료됐어요 \uD83D\uDE25")
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
                ApplicationClass.chatApi.loadChatList(roomId).awaitResponse().body()
            if (result?.flag == "success") {
                var prev_type = -1
                for (i in 0 until result.data.size) {
                    var chat = result.data.get(i)
                    if (chat.system) {
                        itemList.add(ChatlistData(chat.content, 0,null,otherUserId))
                        prev_type = 0;
                    } else {
                        if (chat.fromUserId == ApplicationClass.prefs.getId()) {
                            itemList.add(ChatlistData(chat.content, 2,null,ApplicationClass.prefs.getId()))
                            prev_type = 2
                        }
                        else {
                            itemList.add(ChatlistData(chat.content, 1,profile,otherUserId))
                        }
                    }
                }
                roomAdapter.notifyDataSetChanged()
                rv_chat.scrollToPosition(itemList.size - 1)
                if(itemList.size <=4) {
                    window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                } else {
                        chatSizeCheck = true
                        //window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
                }
                val data = JSONObject()
                data.put("roomId", roomId)
                data.put("userId", ApplicationClass.prefs.getId())
                StompHelper.stompClient.send("/room_enter", data.toString()).subscribe()
            }
        }

        if (init == 1) {
            if(room.notShareUserId == ApplicationClass.prefs.getId()) {
                otherUserId = room.shareUserId
            } else {
                otherUserId = room.notShareUserId
            }
            var data = JSONObject()
            data.put("roomId", room.id)
            data.put("fromUserId", ApplicationClass.prefs.getId())
            data.put("toUserId", otherUserId)
            data.put("content", "대화를 시작해보세요 \uD83D\uDE0A")
            data.put("time", System.currentTimeMillis())
            data.put("system",true)
            StompHelper.stompClient.send("/recvchat", data.toString()).subscribe()
            init = 0
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
                                itemList.add(ChatlistData(json.getString("content"), 0,"none",otherUserId))
                            else{
                                itemList.add(ChatlistData(json.getString("content"), 1,profile,otherUserId))
                            }
                            roomAdapter.notifyItemInserted(itemList.size-1)
                            rv_chat.scrollToPosition(itemList.size - 1)
                            var data = JSONObject()
                            data.put("roomId", roomId)
                            data.put("userId", ApplicationClass.prefs.getId())
                            StompHelper.stompClient.send("/room_enter", data.toString()).subscribe()
                            if(itemList.size>=5 && !chatSizeCheck) {
                                chatSizeCheck = true
                                window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                            }
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
            showSignDialog()
        }
    }

    fun closeKeyboard(context: Context, view: EditText) {
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun initScheduleButton() {
        binding.btnChatCalendar.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                if(ApplicationClass.prefs.getId() == room.shareUserId) {
                    var result = ApplicationClass.chatApi.getAppointMent(room.boardId,room.notShareUserId,room.shareUserId,room.type)
                        .awaitResponse().body()
                    if(result?.flag == "success") {
                        appointDto = result.data.get(0)
                        if(appointDto != null) {
                            showDialog("예약 일자\n${appointDto?.appointmentStart} \n~ ${appointDto?.appointmentEnd}")
                        } else {
                            onCalendarClick()
                        }
                    }
                } else {
                    var result = ApplicationClass.chatApi.getPeriodDto(room.boardId,room.notShareUserId,room.shareUserId,room.type)
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

    /**
     * @return 포인트 충분하면 true, 부족시 토스트 띄우고 false 리턴
     */
    fun checkMyPoint(): Boolean{
        if(ApplicationClass.prefs.getPoint() < 30) { // 대여할 때 무조건 30 포인트 삭감
            showDialog("포인트가 부족해서 채팅하기가 불가합니다." +
                    "\n다른 주민들에게 공유하기를 통해 포인트를 모아보세요!")
            return false
        }
        return true
    }

    private fun initAcceptButton() {
        if(room.state != 0) {
            showDialog("종료된 대화방입니다.")
            return
        }

        binding.btnChatAccept.setOnClickListener {
            if(ApplicationClass.prefs.getId() == room.shareUserId) { // 내가 대여자인 경우 포인트 확인
                showDialog("만남 확정은 피공유자만 할 수 있습니다.")
            } else if (!checkMyPoint()){ // 피공유자인 경우 포인트 확인
                return@setOnClickListener
            }
            CoroutineScope(Dispatchers.Main).launch {
                val result = ApplicationClass.chatApi.getAppointMent(
                    room.boardId, room.notShareUserId,
                    room.shareUserId,room.type
                ).awaitResponse().body()
                if(result?.flag == "success") {
                    appointDto = result.data.get(0)
                    if(appointDto != null) {
                        showDialog("이미 약속된 정보가 있습니다.")
                        return@launch
                    } else {
                        val result = ApplicationClass.chatApi.getPeriodDto(
                            room.boardId, room.notShareUserId,
                            room.shareUserId,room.type
                        ).awaitResponse().body()
                        if (result?.flag == "success") {
                            setPeriodDto = result.data.get(0)
                            if(setPeriodDto != null) {
                                var resultSign = ApplicationClass.hChatApi.getSign(roomId).awaitResponse().body()
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

    fun showSignDialog() {
        var dialog = SignDialog(this, room.id, room.notShareUserId, nickName)
        dialog.show(this.supportFragmentManager, "Appoint")
    }

    fun showReturnDialog() {
        var dialog = ReturnConfirmDialog(this, nickName)
        dialog.show(this.supportFragmentManager, "Appoint")
    }

    fun showDialog(msg: String) {
        var dialog = MyAlertDialog(this, msg)
        dialog.show(this.supportFragmentManager, "Appoint")
    }

    private var isOpen = false
    fun toggleBottomMenu(view: View) {
        if(isOpen) {
            binding.chatPlus.background = resources.getDrawable(R.drawable.ic_chat_add)
            binding.chatMenu.visibility = View.GONE
            binding.rvChat.scrollToPosition(itemList.size - 1)
        } else {
            binding.chatPlus.background = resources.getDrawable(R.drawable.ic_chat_close)
            binding.chatMenu.visibility = View.VISIBLE
            binding.rvChat.scrollToPosition(itemList.size - 1)
        }
        isOpen = !isOpen
    }

    @SuppressLint("ResourceAsColor")
    private fun onCalendarClick() { // 캘린더 버튼클릭
        Log.d(TAG, "onCalendarClick: 캘린더 클릭")
        val listValidators = ArrayList<DateValidator>()
        var startDay:Long = 0
        var endDay:Long = 0
        var startDate:Calendar
        var endDate:Calendar
        val SDF = SimpleDateFormat ("yyyy-MM-dd")

        val calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"), Locale.KOREA)
        SDF.timeZone = TimeZone.getTimeZone("Asia/Seoul")
        SDF.calendar = calendar
        var realStart = SDF.parse(boardStart).time + 32400000

        if(setPeriodDto == null) {
            calendar.time = Date()
            startDay = calendar.timeInMillis
            startDate = calendar
            if(startDay < realStart) {
                startDay = realStart
            }
            calendar.add(Calendar.DATE, 0) // 디폴트 end날짜 : 오늘(하루도 대여 가능)
            endDay = calendar.timeInMillis
            endDate = calendar
        } else {
            var date = SDF.parse(setPeriodDto?.startDay)
            calendar.time = date
            startDay = calendar.timeInMillis + 32400000
            startDate = calendar

            date = SDF.parse(setPeriodDto?.endDay)
            calendar.time = date
            endDay = calendar.timeInMillis + 32400000
            endDate = calendar

        }


        listValidators.add(DateValidatorPointForward.from(realStart))
        listValidators.add(DateValidatorPointBackward.before(SDF.parse(boardEnd).time + 32400000))
        // Build constraints.
        val constraintsBuilder =
            CalendarConstraints.Builder()
                .setStart(Date().time)
                .setValidator(CompositeDateValidator.allOf(listValidators))


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


                val appointmentList = ApplicationClass.hAppointmentApi.getBoardAppointments(room.boardId, room.type).awaitResponse().body()
                if(appointmentList?.flag=="success") {
                    for(element in appointmentList.data[0]) {
                        Log.d(TAG, "onCalendarClick: 이미 예약된 날짜 :${element.appointmentStart} ~ ${element.appointmentEnd}")

                        val startTime = Common.dateToMillis(element.appointmentStart)
                        val endTime = Common.dateToMillis(element.appointmentEnd)
                        // 선택한 날짜가 이 날짜 사이에 있으면 안됩니다.

                        // 선택한 start(it.first)가 endTime보다 커야함
                        // 그리고
                        // 선택한 end(it.second)가 startTime보다 작아야함
                        // : it.first <= endTime || it.second >= startTime
                        if(it.first <= endTime || it.second >= startTime) { // 안된다고 하기
                            val failDialog = MyAlertDialog(this@ChatRoomActivity, "이미 대여중인 날짜(${element.appointmentStart}~${element.appointmentEnd})는 선택할 수 없습니다.")
                            failDialog.show(supportFragmentManager, "CalenderPickerFail")
                            return@launch
                        }
                    }
                    // 이제 여기서 실제 캘린더 날짜 잡아주기
                    val result = ApplicationClass.chatApi.setPeriodDto(setPeriodDto!!).awaitResponse().body()
                    if (result?.flag == "success") {
                        var data = JSONObject()
                        data.put("roomId", roomId)
                        data.put("fromUserId", ApplicationClass.prefs.getId())
                        data.put("toUserId", otherUserId)
                        data.put("content", "공유기간이 설정됐어요! 예약 확정을 해주세요 \uD83D\uDE00")
                        data.put("time", System.currentTimeMillis())
                        data.put("system",true)
                        StompHelper.stompClient.send("/recvchat", data.toString()).subscribe()
                        itemList.add(ChatlistData(data.getString("content"), 0,"none",otherUserId))
                        roomAdapter.notifyDataSetChanged()
                        rv_chat.scrollToPosition(itemList.size - 1)
                    }
                } else {
                    Toast.makeText(this@ChatRoomActivity, "캘린더를 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
















            }
        }

        datePicker.show(supportFragmentManager, "Date")


        /*val now = Calendar.getInstance()

        val dpd: DatePickerDialog = DatePickerDialog.newInstance(
            this@ChatRoomActivity,
            now[Calendar.YEAR],
            now[Calendar.MONTH],
            now[Calendar.DAY_OF_MONTH]
        )
        dpd.minDate = Common.dateToCalendar(boardStart)
        dpd.maxDate = Common.dateToCalendar(boardEnd)
        dpd.accentColor = R.color.main_1
        dpd.version = DatePickerDialog.Version.VERSION_2
        dpd.setTitle("공유할 날짜를 선택해주세요.")
        dpd.disabledDays
        //dpd.setTitle("게시글에 작성한 희망 공유 날짜 범위 안에서 선택 가능해요.\n이미 다른 사람에게 대여중인 날짜는 선택할 수 없어요.")
        dpd.show(supportFragmentManager, "Datepickerdialog");

        CoroutineScope(Dispatchers.Main).launch {
            val appointmentList = ApplicationClass.retrofitAppointmentService.getBoardAppointments(room.boardId, room.type).awaitResponse().body()
            if(appointmentList?.flag=="success") {
                for(element in appointmentList.data[0]) {
                    Log.d(TAG, "onCalendarClick: 이미 예약된 날짜 :${element.appointmentStart} ~ ${element.appointmentEnd}")

                    val startTime = Common.dateToMillis(element.appointmentStart)
                    val endTime = Common.dateToMillis(element.appointmentEnd)
                    // 이 날자 millisecond로 변환해서 constraints에 추가


                }



            } else {
                Toast.makeText(this@ChatRoomActivity, "캘린더를 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
        }*/

    }

    // 반납 로직
    // 공유자가 먼저 한 후 피공유자 시작
    fun initReturnButton() {

        CoroutineScope(Dispatchers.Main).launch {
            var result = ApplicationClass.chatApi.getAppointMent(
                room.boardId, room.notShareUserId,
                room.shareUserId,room.type
            ).awaitResponse().body()

            if(result?.flag == "success") {
                appointDto = result.data.get(0)
                if(appointDto == null) {
                    return@launch
                }
                var result = ApplicationClass.chatApi.getReturns(room.id).awaitResponse().body()
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
                            var datad = JSONObject()
                            datad.put("roomId", roomId)
                            datad.put("fromUserId", ApplicationClass.prefs.getId())
                            datad.put("toUserId", otherUserId)
                            datad.put("content", "공유가 종료되었어요 \uD83D\uDE0A")
                            datad.put("time", System.currentTimeMillis())
                            datad.put("system",true)
                            itemList.add(ChatlistData(datad.getString("content"), 0,"none",otherUserId))

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
    fun initCancelButton() {
        binding.btnChatCancel.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                val result =
                    ApplicationClass.chatApi.getState(roomId).awaitResponse().body()

                if(result?.flag == "success") {
                    if(result.data.size == 0) {
                        room.state = 0
                    } else
                        room.state = result.data.get(0).state
                    println(result)
                    println(roomId)
                    if(room.state == 0) {
                        val result =
                            ApplicationClass.chatApi.getNotShareUserCancel(roomId).awaitResponse().body()
                        if(result?.flag == "success") {
                            println("ddddddddd" + result.data.size)
                            var da = result.data.get(0)
                            if(da == null) { // 피공유자 요청이 없으면
                                if(ApplicationClass.prefs.getId() == room.shareUserId) { // 공유자 일방 취소
                                    var datad = JSONObject()
                                    datad.put("roomId", roomId)
                                    datad.put("fromUserId", ApplicationClass.prefs.getId())
                                    datad.put("toUserId", otherUserId)
                                    datad.put("content", "예약이 취소되어 대화가 종료됩니다.")
                                    datad.put("time", System.currentTimeMillis())
                                    datad.put("system",true)
                                    itemList.add(ChatlistData(datad.getString("content"), 0,"none",otherUserId))
                                    roomAdapter.notifyDataSetChanged()
                                    StompHelper.stompClient.send("/recvchat", datad.toString()).subscribe()

                                    var data = JSONObject()
                                    data.put("roomId",room.id)
                                    data.put("reason",1)
                                    StompHelper.stompClient.send("recvcancel",data.toString()).subscribe()

                                } else { // 피공유자 신청
                                    val result =
                                        ApplicationClass.chatApi.notShareUserCancel(
                                            CancelAppointmentDto(roomId)
                                        ).awaitResponse().body()
                                    if(result?.flag == "success") {
                                        var datad = JSONObject()
                                        datad.put("roomId", roomId)
                                        datad.put("fromUserId", ApplicationClass.prefs.getId())
                                        datad.put("toUserId", otherUserId)
                                        datad.put("content", "피공유자가 예약 취소를 요청했어요")
                                        datad.put("time", System.currentTimeMillis())
                                        datad.put("system",true)
                                        itemList.add(ChatlistData(datad.getString("content"), 0,"none",otherUserId))
                                        roomAdapter.notifyDataSetChanged()
                                        StompHelper.stompClient.send("/recvchat", datad.toString()).subscribe()
                                    }
                                }
                            } else { // 있는거임
                                if(ApplicationClass.prefs.getId() == room.shareUserId) {
                                    var datad = JSONObject()
                                    datad.put("roomId", roomId)
                                    datad.put("fromUserId", ApplicationClass.prefs.getId())
                                    datad.put("toUserId", otherUserId)
                                    datad.put("content", "예약이 취소되어 대화가 종료됩니다.")
                                    datad.put("time", System.currentTimeMillis())
                                    datad.put("system",true)
                                    itemList.add(ChatlistData(datad.getString("content"), 0,"none",otherUserId))
                                    roomAdapter.notifyDataSetChanged()
                                    StompHelper.stompClient.send("/recvchat", datad.toString()).subscribe()

                                    var data = JSONObject()
                                    data.put("roomId",room.id)
                                    data.put("reason",2)
                                    StompHelper.stompClient.send("recvcancel",data.toString()).subscribe()
                                } else {
                                    showDialog("공유자에게 취소를 요청하세요!")
                                }
                            }
                        }
                    } else {
                        showDialog("예약된 정보가 없습니다.")
                    }
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
        data.put("content", "예약이 확정됐어요 \uD83D\uDE42")
        data.put("time", System.currentTimeMillis())
        data.put("system",true)
        StompHelper.stompClient.send("/recvchat", data.toString()).subscribe()
        itemList.add(ChatlistData(data.getString("content"), 0,"none",otherUserId))
        roomAdapter.notifyDataSetChanged()
        rv_chat.scrollToPosition(itemList.size - 1)
    }

    override fun onYesButtonClick(cnt: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            var map = HashMap<String,Int>()
            map.put("userId",room.notShareUserId)
            map.put("degree",cnt)
            val result = ApplicationClass.userApi.setManner(map).awaitResponse().body()
            if (result?.flag == "success") {
                val result = ApplicationClass.chatApi.returnRequest(ReturnRequestDto(roomId)).awaitResponse().body()
                if(result?.flag == "success") {
                    var data = JSONObject()
                    data.put("roomId", roomId)
                    data.put("fromUserId", ApplicationClass.prefs.getId())
                    data.put("toUserId", otherUserId)
                    data.put("content", "반납이 확인됐어요 \uD83D\uDE42")
                    data.put("time", System.currentTimeMillis())
                    data.put("system",true)
                    StompHelper.stompClient.send("/recvchat", data.toString()).subscribe()
                    itemList.add(ChatlistData(data.getString("content"), 0,"none",otherUserId))
                    roomAdapter.notifyDataSetChanged()
                    rv_chat.scrollToPosition(itemList.size - 1)
                }
            }
        }
    }

    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        TODO("Not yet implemented")
    }


}