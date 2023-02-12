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
import kr.co.vilez.databinding.FragmentMyAskBinding
import kr.co.vilez.ui.ask.AskToChatAdapter
import kr.co.vilez.ui.user.ProfileMenuActivity
import kr.co.vilez.util.ApplicationClass
import kr.co.vilez.util.Common
import retrofit2.awaitResponse

private const val TAG = "빌리지_프로필_MyAskFragment"
class MyAskFragment : Fragment() { // 대여 물품 (appointments/my/give/{userId} : 내가 빌린거, 내가 빌릴거
    private lateinit var binding : FragmentMyAskBinding
    private lateinit var profileMenuActivity: ProfileMenuActivity

    private lateinit var boardAdapter: AskToChatAdapter
    private lateinit var boardList:ArrayList<BoardData>
    private var index = 0

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
        index = 0
        boardList = arrayListOf()
        boardAdapter = AskToChatAdapter(boardList)
        // 리사이클러뷰에 어댑터 등록
        binding.rvMyAskList.apply {
            adapter = boardAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        }
        CoroutineScope(Dispatchers.Main).launch {
            val result = ApplicationClass.hAppointmentApi.getMyGiveList(
                ApplicationClass.prefs.getId()).awaitResponse().body()
            if (result?.flag == "success") {
                Log.d(TAG, "initShareData: success!!!!! 검색 결과 개수 : ${result.data[0].size}")

                Log.d(TAG, "initList: result: $result")
                if (result.data.isEmpty()) {
                    Log.d(TAG, "onViewCreated: 데이터 0개")
                }
                for (data in result.data[0]) {
                    val state = Common.getBoardState(data.myAppointListVO.appointmentStart)
                    if(state == Common.APPOINTMENT_TYPE_RESERVE) continue // 공유 예정인 것은 skip
                    if(data.myAppointListVO.id == 0) continue // TODO : api 에러같음 id 0인것은 데이터 다 null로 들어와서 skip하기
                    Log.d(TAG, "initReserveData: 이 약속 data: $data")
                    val boardData = BoardData(
                        data.myAppointListVO.id,
                        data.myAppointListVO.roomId,
                        if (data.imgPathList.isNullOrEmpty()) Common.DEFAULT_PROFILE_IMG else data.imgPathList[0].path,
                        data.myAppointListVO.title,
                        "",
                        data.myAppointListVO.appointmentStart+ " ~ " + data.myAppointListVO.appointmentEnd,
                        data.bookmarkCnt.toString(),
                        data.myAppointListVO.userId, // 글 작성자가 들어감
                        data.myAppointListVO.shareUserId.toInt(),
                        data.myAppointListVO.notShareUserId.toInt(),
                        type = data.myAppointListVO.type,
                        state = Common.APPOINTMENT_TYPE_SHARE, // 공유중인 데이터만 넣을거임
                        sDay = data.myAppointListVO.appointmentStart,
                        eDay = data.myAppointListVO.appointmentEnd,
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
        boardAdapter = AskToChatAdapter(boardList)
        // 리사이클러뷰에 어댑터 등록
        binding.rvMyAskList.apply {
            adapter = boardAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        }
        CoroutineScope(Dispatchers.Main).launch {
            val result = ApplicationClass.hAppointmentApi.getMyGiveList(ApplicationClass.prefs.getId()).awaitResponse().body()
            if (result?.flag == "success") {
                Log.d(TAG, "initShareData: success!!!!! 검색 결과 개수 : ${result.data[0].size}")

                Log.d(TAG, "initList: result: $result")
                if (result.data.isEmpty()) {
                    Log.d(TAG, "onViewCreated: 데이터 0개")
                }
                for (data in result.data[0]) {
                    val state = Common.getBoardState(data.myAppointListVO.appointmentStart)
                    if(state == Common.APPOINTMENT_TYPE_SHARE) continue // 공유 중인 것은 skip
                    if(data.myAppointListVO.id == 0) continue // TODO : api 에러같음 id 0인것은 데이터 다 null로 들어와서 skip하기
                    Log.d(TAG, "initReserveData: 이 약속 data: $data")
                    val boardData = BoardData(
                        data.myAppointListVO.id,
                        data.myAppointListVO.roomId,
                        if (data.imgPathList.isNullOrEmpty()) Common.DEFAULT_PROFILE_IMG else data.imgPathList[0].path,
                        data.myAppointListVO.title,
                        "",
                        data.myAppointListVO.appointmentStart+ " ~ " + data.myAppointListVO.appointmentEnd,
                        data.bookmarkCnt.toString(),
                        data.myAppointListVO.userId, //  글 작성자가 들어가는 자리 
                        data.myAppointListVO.shareUserId.toInt(),
                        data.myAppointListVO.notShareUserId.toInt(),
                        type = data.myAppointListVO.type,
                        state = Common.APPOINTMENT_TYPE_RESERVE, // 공유 예정인 데이터만 넣을거임
                        sDay = data.myAppointListVO.appointmentStart,
                        eDay = data.myAppointListVO.appointmentEnd,
                    )
                    Log.d(TAG, "추가?: $boardData")
                    boardList.add(boardData)
                }
            }
            boardAdapter.notifyItemInserted(index - 1)
        }
    }

    private fun initToolBar() {
        profileMenuActivity.setSupportActionBar(binding.toolbar)
        profileMenuActivity.supportActionBar?.setDisplayShowTitleEnabled(false) // 기본 타이틀 제거
    }

    fun onBackPressed(view: View) {
        profileMenuActivity.finish()
    }
    
}