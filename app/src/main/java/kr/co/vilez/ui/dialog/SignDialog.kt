package kr.co.vilez.ui.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.AttributeSet
import android.util.Base64
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.vilez.R
import kr.co.vilez.data.model.Sign
import kr.co.vilez.databinding.DialogSignBinding
import kr.co.vilez.data.chat.NickData
import kr.co.vilez.util.ApplicationClass
import retrofit2.awaitResponse
import java.io.ByteArrayOutputStream

/***
 * 취소, 제출하기 버튼 있는 게시글 신고 다이얼로그
 */

class SignDialog(
    context: Context,
    roomId: Int,
    notShareUserId: Int,
    notShareUserNick: String
) : DialogFragment() {

    // 뷰 바인딩 정의
    private var _binding: DialogSignBinding? = null
    private val binding get() = _binding!!
    private var sign : Sign? = null
    private var reportDialogInterface: SginDialogInterface? = null
    private var roomId: Int = 0
    private var notShareUserId: Int = 0
    private var notShareUserNick: String = "여섯글자까지"
    private var message: String? = null
    private lateinit var myView: MyView

    init {
        this.roomId = roomId
        this.notShareUserId = notShareUserId
        this.notShareUserNick = notShareUserNick
        this.reportDialogInterface = reportDialogInterface
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.dialog_sign, container, false)
        binding.dialog = this
        val view = binding.root
        // 레이아웃 배경을 투명하게 해줌, 필수 아님
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        var nick = NickData("","")
        if(notShareUserId == ApplicationClass.prefs.getId())
            nick = NickData(notShareUserNick,ApplicationClass.prefs.getNickName())
        else
            nick = NickData(ApplicationClass.prefs.getNickName(), notShareUserNick)
        binding.nick = nick
        //분기 나누기
        btnSetting()
        return view
    }

    fun btnSetting() {
        // 취소 버튼 클릭 : 다이얼로그 닫기
        binding.btnReportNo.setOnClickListener {
            dismiss()
        }
        binding.btnReportYes.setOnClickListener {
            sendSgin()
        }
        binding.btnCheckBtn.setOnClickListener {
            dismiss()
        }

        if(ApplicationClass.prefs.getId() == notShareUserId) { // 피공유자면
            CoroutineScope(Dispatchers.Main).launch {
                val result = ApplicationClass.hChatApi.getSign(roomId).awaitResponse().body()
                if (result?.flag == "success") {
                    sign = result.data.get(0)
                    if(sign != null) {
                        var str = sign!!.sign.split(",")[1]
                        val decoded = Base64.decode(str, Base64.DEFAULT)
                        val decodedBitmap = BitmapFactory.decodeByteArray(decoded, 0, decoded.size)
                        binding.boxSign.check = true
                        binding.boxSign.bitmap = decodedBitmap
                        binding.btnReportNo.visibility = View.GONE
                        binding.btnReportYes.visibility = View.GONE
                        binding.btnCheckBtn.visibility = View.VISIBLE
                        //서명 불가능하게 처리
                        binding.boxSign.invalidate()
                    }
                }
            }
        } else {
            binding.btnReportNo.visibility = View.GONE
            binding.btnReportYes.visibility = View.GONE
            binding.btnCheckBtn.visibility = View.VISIBLE
            binding.boxSign.check = true
            CoroutineScope(Dispatchers.Main).launch {
                val result = ApplicationClass.hChatApi.getSign(roomId).awaitResponse().body()
                if (result?.flag == "success") {
                    sign = result.data.get(0)
                    if(sign != null) {
                        var str = sign!!.sign.split(",")[1]
                        val decoded = Base64.decode(str, Base64.DEFAULT)
                        val decodedBitmap = BitmapFactory.decodeByteArray(decoded, 0, decoded.size)
                        binding.boxSign.bitmap = decodedBitmap
                        binding.boxSign.invalidate()
                    }
                }
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun onOkButtonClick(view: View) {
        this.reportDialogInterface?.onYesButtonClick(roomId) // 파라미터 필요할 경우 대비해서 생성함 (걍 창 종료만 할거면 아무값이나 넣어도 됨)
        dismiss()
    }

    fun onCancelButtonClick(view: View) {
        dismiss()
    }

    fun sendSgin() {
// Draw something on the canvas
        val byteArrayOutputStream = ByteArrayOutputStream()
        binding.boxSign.bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        val base64String = Base64.encodeToString(byteArray, Base64.DEFAULT)
        val dataUrl = "data:image/png;base64,$base64String"
        //TODO  : sign 크기 확인해야함! (추가기능)
        var sign = Sign(roomId,dataUrl)
        CoroutineScope(Dispatchers.Main).launch {
            val result = ApplicationClass.hChatApi.addSign(sign).awaitResponse().body()
            if (result?.flag == "success") {
                dismiss()
            }
        }

    }
}

interface SginDialogInterface {
    fun onYesButtonClick(id: Int) // 확인 버튼 클릭을 처리해줄 인터페이스 (호출한 곳에서 나중에 구현해줘야 합니다!)
}

data class Point (var x:Float, var y:Float)

@SuppressLint("ClickableViewAccessibility")
class MyView : View {
    lateinit var save_canvas : Canvas
    var check: Boolean = false
    var bitmap = Bitmap.createBitmap(400, 200, Bitmap.Config.ARGB_8888)
    private var list= ArrayList<Point>()
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)



    init {
        save_canvas = Canvas(bitmap)
        setOnTouchListener { view, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    if(check) return@setOnTouchListener false
                    list.add(Point(event.x,event.y))
                    invalidate()
                    return@setOnTouchListener true
                }
                MotionEvent.ACTION_MOVE -> {
                    if(check) return@setOnTouchListener false
                    list.add(Point(event.x,event.y))
                    invalidate()
                    return@setOnTouchListener true
                }
                else -> return@setOnTouchListener true
            }
            return@setOnTouchListener true
        }

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // Draw something on the canvas
        val mPaint = Paint()
        mPaint.color = Color.BLACK
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = 5f
        if(check) {
            bitmap = Bitmap.createScaledBitmap(bitmap,400,200,true)
            canvas.drawBitmap(bitmap,0f,0f,mPaint)
        }
        for(i in 1 until  list.size) {
            canvas.drawLine(list[i-1].x, list[i-1].y, list[i].x, list[i].y, mPaint)
            save_canvas.drawLine(list[i-1].x, list[i-1].y, list[i].x, list[i].y, mPaint)
        }
    }
}