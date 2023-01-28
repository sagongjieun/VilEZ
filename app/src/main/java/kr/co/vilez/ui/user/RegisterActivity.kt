package kr.co.vilez.ui.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kr.co.vilez.R

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        supportActionBar?.hide() // 액션바 숨김

        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout_register, RegisterFragment())
            .commit()
    }

    fun moveToLogin() {
        startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
    }
}