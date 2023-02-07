package kr.co.vilez.ui.profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.vilez.R
import kr.co.vilez.data.dto.BoardData
import kr.co.vilez.databinding.FragmentMyShareBinding
import kr.co.vilez.ui.user.ProfileMenuActivity
import kr.co.vilez.util.ApplicationClass
import kr.co.vilez.util.Common
import kr.co.vilez.util.Common.Companion.APPOINTMENT_TYPE_RESERVE
import kr.co.vilez.util.Common.Companion.APPOINTMENT_TYPE_SHARE
import kr.co.vilez.util.Common.Companion.getBoardState
import retrofit2.awaitResponse

private const val TAG = "빌리지_프로필_MyShareFragment"
class MyShareFragment : Fragment() { // 내가 빌려주는것들 목록
    private lateinit var binding: FragmentMyShareBinding
    private lateinit var activity: ProfileMenuActivity

    private val STATE_SHARING = 0 // 공유 중
    private val STATE_RESERVE = 1 // 공유 예정 (예약된 것)

    private lateinit var boardAdapter: BoardListAdapter
    private lateinit var boardList:ArrayList<BoardData>
    private var index = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = context as ProfileMenuActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_share, container, false)
        binding.fragment = this
        initToolBar()
        initTabLayout()
        initSharingData() // 디폴트 : 공유중
        return binding.root
    }


    private fun initTabLayout() {
        binding.tabLayoutMyShareList.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab!!.position) {
                    STATE_SHARING -> {
                        Log.d(TAG, "onTabSelected: 공유중 클릭")
                        initSharingData()
                    }
                    STATE_RESERVE -> {
                        Log.d(TAG, "onTabSelected: 공유예정(예약) 클릭")
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
        index = 0
        boardList = arrayListOf()
        boardAdapter = BoardListAdapter(boardList)
        // 리사이클러뷰에 어댑터 등록
        binding.rvMyShareList.apply {
            adapter = boardAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        }
        CoroutineScope(Dispatchers.Main).launch {
            val result = ApplicationClass.retrofitAppointmentService.getMyAppointment(ApplicationClass.prefs.getId()).awaitResponse().body()
            if (result?.flag == "success") {
                Log.d(TAG, "initShareData: success!!!!! 검색 결과 개수 : ${result.data[0].size}")

                Log.d(TAG, "initList: result: $result")
                if (result.data.isEmpty()) {
                    Log.d(TAG, "onViewCreated: 데이터 0개")
                }
                for (data in result.data[0]) {
                    val state = getBoardState(data.myAppointListVO.startDay)
                    if(state == APPOINTMENT_TYPE_RESERVE) continue // 공유 예정인 것은 skip

                    val boardData = BoardData(
                        data.myAppointListVO.id,
                        if (data.imgPathList.isNullOrEmpty()) Common.DEFAULT_PROFILE_IMG else data.imgPathList[0].path,
                        data.myAppointListVO.title,
                        data.myAppointListVO.date,
                        data.myAppointListVO.startDay+ " ~ " + data.myAppointListVO.endDay,
                        data.bookmarkCnt.toString(),
                        data.myAppointListVO.userId,
                        data.myAppointListVO.type,
                        state = APPOINTMENT_TYPE_SHARE, // 공유중인 데이터만 넣을거임
                        sDay = data.myAppointListVO.startDay,
                        eDay = data.myAppointListVO.endDay,
                    )
                    Log.d(TAG, "추가?: $boardData")
                    boardList.add(boardData)
                }
            }
            boardAdapter.notifyItemInserted(index - 1)
        }
    }

    private fun initReserveData() {
        index = 0
        boardList = arrayListOf()
        boardAdapter = BoardListAdapter(boardList)
        // 리사이클러뷰에 어댑터 등록
        binding.rvMyShareList.apply {
            adapter = boardAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        }
        CoroutineScope(Dispatchers.Main).launch {
            val result = ApplicationClass.retrofitAppointmentService.getMyAppointment(ApplicationClass.prefs.getId()).awaitResponse().body()
            if (result?.flag == "success") {
                Log.d(TAG, "initShareData: success!!!!! 검색 결과 개수 : ${result.data[0].size}")

                Log.d(TAG, "initList: result: $result")
                if (result.data.isEmpty()) {
                    Log.d(TAG, "onViewCreated: 데이터 0개")
                }
                for (data in result.data[0]) {
                    val state = getBoardState(data.myAppointListVO.startDay)
                    if(state == APPOINTMENT_TYPE_SHARE) continue // 공유 중인 것은 skip

                    val boardData = BoardData(
                        data.myAppointListVO.id,
                        if (data.imgPathList.isNullOrEmpty()) Common.DEFAULT_PROFILE_IMG else data.imgPathList[0].path,
                        data.myAppointListVO.title,
                        data.myAppointListVO.date,
                        data.myAppointListVO.startDay+ " ~ " + data.myAppointListVO.endDay,
                        data.bookmarkCnt.toString(),
                        data.myAppointListVO.userId,
                        data.myAppointListVO.type,
                        state = APPOINTMENT_TYPE_RESERVE, // 공유 예정인 데이터만 넣을거임
                        sDay = data.myAppointListVO.startDay,
                        eDay = data.myAppointListVO.endDay,
                    )
                    Log.d(TAG, "추가?: $boardData")
                    boardList.add(boardData)
                }
            }
            boardAdapter.notifyItemInserted(index - 1)
        }
    }

    private fun initToolBar() {
        activity.setSupportActionBar(binding.toolbar)
        activity.supportActionBar?.setDisplayShowTitleEnabled(false) // 기본 타이틀 제거
    }

    fun onBackPressed(view: View) {
        activity.finish()
    }
}