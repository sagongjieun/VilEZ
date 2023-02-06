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


            // 공유 목록
            "공유 캘린더" -> {
                transaction
                    .replace(R.id.frame_layout_profile_myshare, CalendarFragment())
            }
            "나의 작성글" -> {
                transaction
                    .replace(R.id.frame_layout_profile_myshare, MyArticleFragment())
            }
            "나의 관심글" -> {
                transaction
                    .replace(R.id.frame_layout_profile_myshare, InterestFragment())
            }
            "포인트 내역" -> {
                transaction
                    .replace(R.id.frame_layout_profile_myshare, PointFragment())
            }


            // 나의 공유 박스
            "나의 공유 물품" -> {
                transaction
                    .replace(R.id.frame_layout_profile_myshare, MyShareFragment())
            }
            "나의 대여 물품" -> {
                transaction
                    .replace(R.id.frame_layout_profile_myshare, MyAskFragment())
            }


            // 내 정보
            "내 동네 설정" -> {
                transaction
                    .replace(R.id.frame_layout_profile_myshare, EditLocation())
            }


        }
        transaction.commit()
    }
}