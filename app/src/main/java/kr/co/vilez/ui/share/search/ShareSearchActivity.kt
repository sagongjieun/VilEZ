package kr.co.vilez.ui.share.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import kr.co.vilez.R
import kr.co.vilez.databinding.ActivityShareSearchBinding

class ShareSearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShareSearchBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_share_search)
        binding.activity = this
    }

    fun onBackPressed(view: View) {
        finish()
    }
}