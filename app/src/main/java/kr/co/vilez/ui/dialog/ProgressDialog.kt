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
import kr.co.vilez.databinding.DialogProgressBinding

/***
 * 로딩중임을 나타내는 다이얼로그
 */
class ProgressDialog(
    context: Context,
    message: String
) : DialogFragment() {

    // 뷰 바인딩 정의
    private var _binding: DialogProgressBinding? = null
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
        _binding = DataBindingUtil.inflate(inflater, R.layout.dialog_progress, container, false)
        binding.dialog = this
        val view = binding.root

        // 레이아웃 배경을 투명하게 해줌, 필수 아님
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.message = message

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}