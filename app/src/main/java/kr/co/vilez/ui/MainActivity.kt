package kr.co.vilez.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import kr.co.vilez.R
import kr.co.vilez.databinding.ActivityMainBinding
import kr.co.vilez.ui.chat.ChatlistFragment
import kr.co.vilez.ui.share.ShareFragment
import kr.co.vilez.ui.user.ProfileFragment
import kr.co.vilez.util.StompClient

private const val TAG = "빌리지_MainActivity"
class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private var waitTime = 0L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        supportActionBar?.hide() // 액션바 숨김
        StompClient.stompClient.connect()
        val target = intent.getStringExtra("target")
        if(target == null) {
            changeFragment("홈")
        } else {
            changeFragment(target)
        }
        initView()
        StompClient.runStomp()

    }

    private fun changeFragment(name: String) {
        when(name) {
            "홈" -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.frame_layout_main, HomeFragment())
                    .commit()

                binding.bottomNavigation.menu.apply {
                    findItem(R.id.page_home).setIcon(R.drawable.home_fill)
                    findItem(R.id.page_share).setIcon(R.drawable.share_request_line)
                    findItem(R.id.page_chat).setIcon(R.drawable.message_line)
                    findItem(R.id.page_profile).setIcon(R.drawable.user_line)
                }
            }
            "공유 요청" -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.frame_layout_main, ShareFragment())
                    .commit()

                binding.bottomNavigation.menu.apply {
                    findItem(R.id.page_home).setIcon(R.drawable.home_line)
                    findItem(R.id.page_share).setIcon(R.drawable.share_request_fill)
                    findItem(R.id.page_chat).setIcon(R.drawable.message_line)
                    findItem(R.id.page_profile).setIcon(R.drawable.user_line)
                }
            }
            "채팅" -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.frame_layout_main, ChatlistFragment())
                    .commit()

                binding.bottomNavigation.menu.apply {
                    findItem(R.id.page_home).setIcon(R.drawable.home_line)
                    findItem(R.id.page_share).setIcon(R.drawable.share_request_line)
                    findItem(R.id.page_chat).setIcon(R.drawable.message_fill)
                    findItem(R.id.page_profile).setIcon(R.drawable.user_line)
                }
            }
            "나의 빌리지" -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.frame_layout_main, ProfileFragment())
                    .commit()

                binding.bottomNavigation.menu.apply {
                    findItem(R.id.page_home).setIcon(R.drawable.home_line)
                    findItem(R.id.page_share).setIcon(R.drawable.share_request_line)
                    findItem(R.id.page_chat).setIcon(R.drawable.message_line)
                    findItem(R.id.page_profile).setIcon(R.drawable.user_fill)
                }
            }
        }
    }

    private fun changeFragment(item:MenuItem):Boolean {
        when(item.itemId){
            R.id.page_home -> {
                // 아이콘 변경
                item.setIcon(R.drawable.home_fill)
                binding.bottomNavigation.menu.apply {
                    findItem(R.id.page_share).setIcon(R.drawable.share_request_line)
                    findItem(R.id.page_chat).setIcon(R.drawable.message_line)
                    findItem(R.id.page_profile).setIcon(R.drawable.user_line)
                }
                // Fragment 변경
                supportFragmentManager.beginTransaction()
                    .replace(R.id.frame_layout_main, HomeFragment())
                    .commit()
                return true
            }
            R.id.page_share -> {
                // 아이콘 변경
                item.setIcon(R.drawable.share_request_fill)
                binding.bottomNavigation.menu.apply {
                    findItem(R.id.page_home).setIcon(R.drawable.home_line)
                    findItem(R.id.page_chat).setIcon(R.drawable.message_line)
                    findItem(R.id.page_profile).setIcon(R.drawable.user_line)
                }

                // Fragment 변경
                supportFragmentManager.beginTransaction()
                    .replace(R.id.frame_layout_main, ShareFragment())
                    .commit()
                return true
            }
            R.id.page_chat -> {
                // 아이콘 변경
                item.setIcon(R.drawable.message_fill)
                binding.bottomNavigation.menu.apply {
                    findItem(R.id.page_home).setIcon(R.drawable.home_line)
                    findItem(R.id.page_share).setIcon(R.drawable.share_request_line)
                    findItem(R.id.page_profile).setIcon(R.drawable.user_line)
                }

                // Fragment 변경
                supportFragmentManager.beginTransaction()
                    .replace(R.id.frame_layout_main, ChatlistFragment())
                    .commit()
                return true
            }
            R.id.page_profile -> {
                // 아이콘 변경
                item.setIcon(R.drawable.user_fill)
                binding.bottomNavigation.menu.apply {
                    findItem(R.id.page_home).setIcon(R.drawable.home_line)
                    findItem(R.id.page_share).setIcon(R.drawable.share_request_line)
                    findItem(R.id.page_chat).setIcon(R.drawable.message_line)
                }

                // Fragment 변경
                supportFragmentManager.beginTransaction()
                    .replace(R.id.frame_layout_main, ProfileFragment())
                    .addToBackStack(null)
                    .commit()
                return true
            }

            else -> return false
        }
    }

    private fun initView() {
        // 가장 첫 화면은 홈 화면의 Fragment로 지정
        /*supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout_main, HomeFragment())
            .commit()

        binding.bottomNavigation.menu.apply {
            findItem(R.id.page_home).setIcon(R.drawable.home_fill)
            findItem(R.id.page_share).setIcon(R.drawable.share_request_line)
            findItem(R.id.page_chat).setIcon(R.drawable.message_line)
            findItem(R.id.page_profile).setIcon(R.drawable.user_line)
        }*/

        binding.bottomNavigation.apply {
            itemIconTintList = null // 클릭해도 아이콘 테마색으로 변경되는거 막기
            setOnItemSelectedListener { item ->
                changeFragment(item)
            }
        }

    }

    override fun onBackPressed() {
        if(System.currentTimeMillis() - waitTime >=1500 ) {
            waitTime = System.currentTimeMillis()
            Toast.makeText(this,"뒤로가기 버튼을 한번 더 누르면 종료됩니다.",Toast.LENGTH_SHORT).show()
        } else {
            finish() // 액티비티 종료
        }
    }

}