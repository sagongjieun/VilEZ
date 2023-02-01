package kr.co.vilez.ui.share.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kr.co.vilez.R

class ShareSearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share_search)
    }

    fun onBackPressed(view: View) {
        finish()
    }
}