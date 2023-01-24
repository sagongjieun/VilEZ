package kr.co.vilez.ui.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import kr.co.vilez.R
import kr.co.vilez.databinding.ActivityProfileMyShareBinding
import kr.co.vilez.ui.profile.*

private const val TAG = "빌리지_ProfileMyShareActivity"
class ProfileMyShareActivity : AppCompatActivity() {
    private lateinit var binding:ActivityProfileMyShareBinding
    private lateinit var fragmentName:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile_my_share)
        supportActionBar?.hide() // 액션바 숨김

        fragmentName = intent!!.getStringExtra("fragment").toString()
        moveFragment(fragmentName)
    }

    private fun moveFragment(item:String) {
        val transaction = supportFragmentManager.beginTransaction()

        when(item) {
            "프로필 수정" -> {
                transaction
                    .replace(R.id.frame_layout_profile_myshare, EditProfileFragment())
            }
            "공유 캘린더" -> {
                transaction
                    .replace(R.id.frame_layout_profile_myshare, CalendarFragment())
            }
            "공유 목록" -> {
                transaction
                    .replace(R.id.frame_layout_profile_myshare, SharedListFragment())
            }
            "관심 목록" -> {
                transaction
                    .replace(R.id.frame_layout_profile_myshare, InterestFragment())
            }
            "포인트 내역" -> {
                transaction
                    .replace(R.id.frame_layout_profile_myshare, PointFragment())
            }
            "내 동네 설정" -> {
                transaction
                    .replace(R.id.frame_layout_profile_myshare, EditAreaFragment())
            }
            "내 정보 수정" -> {
                transaction
                    .replace(R.id.frame_layout_profile_myshare, EditAccountFragment())
            }
        }
        transaction.commit()
    }
}