package kr.co.vilez.ui.dialog

import android.content.Context
import android.graphics.Bitmap
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
import kr.co.vilez.databinding.DialogAreaQrBinding


/***
 * 닫기 버튼만 있는 QR코드 다이얼로그 (동네 인증용)
 */
class AreaQRDialog(
    context:Context,
    id: String,
    qrCodeBitmap: Bitmap
) : DialogFragment() {

    // 뷰 바인딩 정의
    private var _binding: DialogAreaQrBinding? = null
    private val binding get() = _binding!!

    private var id: String? = null
    private var qrCodeBitmap: Bitmap? = null

    init {
        this.id = id
        this.qrCodeBitmap = qrCodeBitmap
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.dialog_area_qr, container, false)
        binding.dialog = this
        val view = binding.root

        // 레이아웃 배경을 투명하게 해줌, 필수 아님
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // 확인 버튼 클릭 - 인터페이스 등록 (나중에 호출시 구현필요함)
        binding.btnAreaQrNo.setOnClickListener {
            dismiss()
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}