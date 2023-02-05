package kr.co.vilez.ui.dialog

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
import kr.co.vilez.R
import kr.co.vilez.databinding.DialogSignBinding
import java.io.ByteArrayOutputStream

/***
 * 취소, 제출하기 버튼 있는 게시글 신고 다이얼로그
 */

class SignDialog(
    context: Context,
    boardId: String
) : DialogFragment() {

    // 뷰 바인딩 정의
    private var _binding: DialogSignBinding? = null
    private val binding get() = _binding!!

    private var reportDialogInterface: SginDialogInterface? = null
    private var boardId: String? = null
    private var message: String? = null
    private lateinit var myView: MyView
    init {
        this.boardId = boardId
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

        // 취소 버튼 클릭 : 다이얼로그 닫기
        binding.btnReportNo.setOnClickListener {
            convert()
            dismiss()
        }
        binding.btnReportYes.setOnClickListener {
//            val str = "iVBORw0KGgoAAAANSUhEUgAAAMgAAABkCAYAAADDhn8LAAAAAXNSR0IArs4c6QAABQRJREFUeF7tmjEvZFEYhr/1A6hEgShQCJEo0ZKQKESBUJMohII/IErR0IpCIRQaESJaFDSERicK0yGhtjk32Ul2LWbPzjvMd56bTCYrc7573+e9j3vnrh+5XO7V2CAAgb8S+IEgnBkQeJ8AgnB2QOADAgjC6QEBBOEcgEAcAa4gcdxYlQgBBEmkaGLGEUCQOG6sSoQAgiRSNDHjCCBIHDdWJUIAQRIpmphxBBAkjhurEiGAIIkUTcw4AggSx41ViRBAkESKJmYcAQSJ48aqRAggSCJFEzOOAILEcWNVIgQQJJGiiRlHAEHiuLEqEQIIkkjRxIwjgCBx3FiVCAEESaRoYsYRQJA4bqxKhACCOCl6aGgoS7Kzs/Nlie7u7uzk5MSOjo6sp6fHRkZGvuxYirVjBCkWyS+cs7S0ZOEVtrm5uexVii0IcX19bff397a5uWmXl5e/7fbi4sJqampKcSiyfSCIDG1pBoeTc3Bw0G5vb62+vt7Ozs5kOw5CnJ6e2vHxcXalCP/+aMvlcrJjKdVgBCkVadF+FhcXbXV1NZve399v6+vrRd3T1dWVbW9v2/7+/rtC1NbW2vT0tIX33t7e7HNBVg8bgpRxi7u7uzYxMZFPUKzf2IVIEXba2dlpo6OjLr5rvHcaIEiZChJ+S09NTdn5+XmWYHJy0hYWFqLThCvEwcFBQbdOw8PD2f7a2tqi91cuCxGkXJr64zhnZmZsa2sr+2l7e7sdHh7+U5LwHSJ8wQ7v4fX09PTp+vDlPzyZ8nL79GlgM0OQQih9s8+8vLxYY2Nj/qiWl5dtbGzs3aMMV5uVlRWrqKiwm5ubTIhCt8rKyuxqEW7lqqqqCl3m5nMIUoZVbmxs2Pz8fP7Iq6urbXZ21lpaWvJfpMOtV3iy9fj4+ObxayGRW1tbMzH6+vqSFOMXIwQp5Gz5Zp8JT63C06tib0GKcAsVnoaldBv1EUcEKfZZVqJ5HR0d2X/Q/c9WV1dnXV1d1t3dnb0jxVuaCPI/Z9gXrx0YGMg/xSrkUJqbm62hocHGx8ezJ1AI8Tk1BPmc0bf+xN7enq2trdnz83N2nA8PD9mfdzQ1Ndnr62t2ZQi3Tik8klUUhSAKqsx0QwBB3FRJEAUBBFFQZaYbAgjipkqCKAggiIIqM90QQBA3VRJEQQBBFFSZ6YYAgripkiAKAgiioMpMNwQQxE2VBFEQQBAFVWa6IYAgbqokiIIAgiioMtMNAQRxUyVBFAQQREGVmW4IIIibKgmiIIAgCqrMdEMAQdxUSRAFAQRRUGWmGwII4qZKgigIIIiCKjPdEEAQN1USREEAQRRUmemGAIK4qZIgCgIIoqDKTDcEEMRNlQRREEAQBVVmuiGAIG6qJIiCAIIoqDLTDQEEcVMlQRQEEERBlZluCCCImyoJoiCAIAqqzHRDAEHcVEkQBQEEUVBlphsCCOKmSoIoCCCIgioz3RBAEDdVEkRBAEEUVJnphgCCuKmSIAoCCKKgykw3BBDETZUEURBAEAVVZrohgCBuqiSIggCCKKgy0w0BBHFTJUEUBBBEQZWZbgggiJsqCaIggCAKqsx0QwBB3FRJEAUBBFFQZaYbAgjipkqCKAggiIIqM90QQBA3VRJEQQBBFFSZ6YYAgripkiAKAgiioMpMNwQQxE2VBFEQQBAFVWa6IYAgbqokiIIAgiioMtMNAQRxUyVBFAQQREGVmW4I/ARLg0X/TrBbIwAAAABJRU5ErkJggg=="
//            val decoded = Base64.decode(str, Base64.DEFAULT)
//            val decodedBitmap = BitmapFactory.decodeByteArray(decoded, 0, decoded.size)
//            binding.boxSign.bitmap = decodedBitmap
        }
        /*// 확인 버튼 클릭 - 인터페이스 등록 (나중에 호출시 구현필요함)
        binding.btnReportYes.setOnClickListener {
            this.reportDialogInterface?.onYesButtonClick(boardId!!) // 파라미터 필요할 경우 대비해서 생성함 (걍 창 종료만 할거면 아무값이나 넣어도 됨)
            dismiss()
        }*/

//        binding.boxSign.setOnTouchListener{ view: View, motionEvent: MotionEvent ->
//            when(motionEvent.action) {
//                MotionEvent.ACTION_DOWN -> {
//                    mX = motionEvent.x
//                    mY = motionEvent.y
//                    binding.boxSign.invalidate()
//                    return@setOnTouchListener true
//                }
//                MotionEvent.ACTION_MOVE -> {
//                    mX = motionEvent.x
//                    mY = motionEvent.y
//                    binding.boxSign.invalidate()
//                    return@setOnTouchListener true
//                }
//                else -> return@setOnTouchListener false
//            }
//            true
//        }
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun onOkButtonClick(view: View) {
        this.reportDialogInterface?.onYesButtonClick(boardId!!) // 파라미터 필요할 경우 대비해서 생성함 (걍 창 종료만 할거면 아무값이나 넣어도 됨)
        dismiss()
    }

    fun onCancelButtonClick(view: View) {
        dismiss()
    }
    private val dataUrl = ""
    fun convert() {
// Draw something on the canvas
        val byteArrayOutputStream = ByteArrayOutputStream()
        binding.boxSign.bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        val base64String = Base64.encodeToString(byteArray, Base64.DEFAULT)
        val dataUrl = "data:image/png;base64,$base64String"
        println(dataUrl)

    }
}

interface SginDialogInterface {
    fun onYesButtonClick(id: String) // 확인 버튼 클릭을 처리해줄 인터페이스 (호출한 곳에서 나중에 구현해줘야 합니다!)
}

data class Point (var x:Float, var y:Float)

class MyView : View {
    private var mX: Float = 0f
    private var mY: Float = 0f
    private val mPaint = Paint()
    public lateinit var save_canvas : Canvas
    public var bitmap = Bitmap.createBitmap(400, 200, Bitmap.Config.ARGB_8888)
    private var list= ArrayList<Point>()
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)



    init {
        save_canvas = Canvas(bitmap)
        setOnTouchListener { view, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    list.add(Point(event.x,event.y))
                    invalidate()
                    return@setOnTouchListener true
                }
                MotionEvent.ACTION_MOVE -> {
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
//        bitmap = Bitmap.createScaledBitmap(bitmap,400,200,true)
//        canvas.drawBitmap(bitmap,0f,0f,mPaint)
        for(i in 1 until  list.size) {
            canvas.drawLine(list[i-1].x, list[i-1].y, list[i].x, list[i].y, mPaint)
            save_canvas.drawLine(list[i-1].x, list[i-1].y, list[i].x, list[i].y, mPaint)
        }
    }
}