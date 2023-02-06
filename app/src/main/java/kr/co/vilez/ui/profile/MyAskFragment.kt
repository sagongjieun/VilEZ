package kr.co.vilez.ui.profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.tabs.TabLayout
import kr.co.vilez.R
import kr.co.vilez.data.dto.AskData
import kr.co.vilez.databinding.FragmentMyAskBinding
import kr.co.vilez.ui.ask.AskListAdapter
import kr.co.vilez.ui.user.ProfileMenuActivity

private const val TAG = "빌리지_프로필_MyAskFragment"
class MyAskFragment : Fragment() {
    private lateinit var binding : FragmentMyAskBinding
    private lateinit var profileMenuActivity: ProfileMenuActivity

    private var index = 0
    private lateinit var askAdapter: AskListAdapter
    private lateinit var askList:ArrayList<AskData>

    private val STATE_SHARING = 0 // 공유 중인 요청글
    private val STATE_RESERVE = 1  // 공유 예정인 요청글
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
        profileMenuActivity = context as ProfileMenuActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_ask, container, false)
        binding.fragment = this
        initToolBar()
        initTabLayout()
        initSharingData() // 디폴트 대여중인 데이터 보여주기
        return binding.root
    }

    private fun initTabLayout() {
        binding.tabLayoutMyAskList.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab!!.position) {
                    STATE_SHARING-> { // 대여중
                        Log.d(TAG, "onTabSelected: 대여중 클릭")
                        initSharingData()
                    }
                    STATE_RESERVE -> { // 대여 예정
                        Log.d(TAG, "onTabSelected: 대여 예정(예약) 클릭")
                        initReserveData()
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }

    private fun initSharingData() {

    }

    private fun initReserveData() {

    }

    private fun initToolBar() {
        profileMenuActivity.setSupportActionBar(binding.toolbar)
        profileMenuActivity.supportActionBar?.setDisplayShowTitleEnabled(false) // 기본 타이틀 제거
    }

    fun onBackPressed(view: View) {
        profileMenuActivity.finish()
    }
    
}