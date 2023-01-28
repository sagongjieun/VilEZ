package kr.co.vilez.ui.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import kr.co.vilez.R
import kr.co.vilez.databinding.ActivityProfileMenuBinding
import kr.co.vilez.ui.profile.*

private const val TAG = "빌리지_ProfileMyShareActivity"
class ProfileMenuActivity : AppCompatActivity() {
    private lateinit var binding:ActivityProfileMenuBinding
    private lateinit var fragmentName:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile_menu)
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
            "공유 내역" -> {
                transaction
                    .replace(R.id.frame_layout_profile_myshare, SharedListFragment())
            }
            "요청 내역" -> {
                transaction
                    .replace(R.id.frame_layout_profile_myshare, RequestListFragment())
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