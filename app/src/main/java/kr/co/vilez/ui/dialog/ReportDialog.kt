package kr.co.vilez.ui.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import kr.co.vilez.R
import kr.co.vilez.databinding.DialogReportBinding

/***
 * 취소, 제출하기 버튼 있는 게시글 신고 다이얼로그
 */

class ReportDialog(
    reportDialogInterface: ReportDialogInterface,
    boardId: String
) : DialogFragment() {

    // 뷰 바인딩 정의
    private var _binding: DialogReportBinding? = null
    private val binding get() = _binding!!

    private var reportDialogInterface: ReportDialogInterface? = null

    private var boardId: String? = null
    private var message: String? = null

    init {
        this.boardId = boardId
        this.reportDialogInterface = reportDialogInterface
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.dialog_report, container, false)
        binding.dialog = this
        val view = binding.root

        // 레이아웃 배경을 투명하게 해줌, 필수 아님
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // 취소 버튼 클릭 : 다이얼로그 닫기
        binding.btnReportNo.setOnClickListener {
            dismiss()
        }
        /*// 확인 버튼 클릭 - 인터페이스 등록 (나중에 호출시 구현필요함)
        binding.btnReportYes.setOnClickListener {
            this.reportDialogInterface?.onYesButtonClick(boardId!!) // 파라미터 필요할 경우 대비해서 생성함 (걍 창 종료만 할거면 아무값이나 넣어도 됨)
            dismiss()
        }*/

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun onOkButtonClick(view: View) {
        this.reportDialogInterface?.onYesButtonClick(boardId!!) // 파라미터 필요할 경우 대비해서 생성함 (걍 창 종료만 할거면 아무값이나 넣어도 됨)
         binding.inputReportContent.editText?.text.toString()
        dismiss()
    }

    fun onCancelButtonClick(view: View) {
        dismiss()
    }
}

interface ReportDialogInterface {
    fun onYesButtonClick(id: String) // 확인 버튼 클릭을 처리해줄 인터페이스 (호출한 곳에서 나중에 구현해줘야 합니다!)
}