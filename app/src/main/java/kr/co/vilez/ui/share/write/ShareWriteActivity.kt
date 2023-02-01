package kr.co.vilez.ui.share.write

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import kr.co.vilez.R
import kr.co.vilez.databinding.ActivityShareWriteBinding

class ShareWriteActivity : AppCompatActivity() {
    private lateinit var binding:ActivityShareWriteBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         binding = DataBindingUtil.setContentView(this, R.layout.activity_share_write)
        binding.activity = this
        initToolBar()
    }

    private fun initToolBar() {
        this.setSupportActionBar(binding.toolbar)
        this.supportActionBar?.setDisplayShowTitleEnabled(false) // 기본 타이틀 제거
        binding.title = "공유 글쓰기"
    }

    fun onBackPressed(view: View) {
        finish()
    }

}