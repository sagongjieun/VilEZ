package kr.co.vilez.ui.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import kr.co.vilez.R
import kr.co.vilez.databinding.DialogFullProgressBinding

/***
 * 로딩중임을 나타내는 꽉 찬 다이얼로그
 */
class FullProgressDialog(
    context: Context,
) : DialogFragment() {

    // 뷰 바인딩 정의
    private var _binding: DialogFullProgressBinding? = null
    private val binding get() = _binding!!

    private var id: String? = null

    init {
        this.id = id
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.dialog_full_progress, container, false)
        binding.dialog = this

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        // 전체화면으로 띄우기
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
    }
}