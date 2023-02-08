package kr.co.vilez.ui.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import kr.co.vilez.R
import kr.co.vilez.databinding.DialogAlertBinding


/***
 * 취소 없이 확인 버튼만 있는 다이얼로그
 */
class MyAlertDialog(
    context: Context,
    message: String
) : DialogFragment() {

    // 뷰 바인딩 정의
    private var _binding: DialogAlertBinding? = null
    private val binding get() = _binding!!

    private var id: String? = null
    private var message: String? = null

    init {
        this.id = id
        this.message = message
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.dialog_alert, container, false)
        binding.dialog = this
        val view = binding.root

        // 레이아웃 배경을 투명하게 해줌, 필수 아님
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.message = message

        // 확인 버튼 클릭 - 인터페이스 등록 (나중에 호출시 구현필요함)
        /*binding.alertYesBtn.setOnClickListener {
            dismiss()
        }*/

        return view
    }

    fun onOkButtonClick(view: View) {
        dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}