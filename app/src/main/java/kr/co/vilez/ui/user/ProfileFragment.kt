package kr.co.vilez.ui.user

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.edit
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.*
import kr.co.vilez.R
import kr.co.vilez.data.dto.AskData
import kr.co.vilez.data.dto.BoardData
import kr.co.vilez.data.model.User
import kr.co.vilez.databinding.FragmentProfileBinding
import kr.co.vilez.ui.MainActivity
import kr.co.vilez.ui.ask.AskListAdapter
import kr.co.vilez.ui.dialog.*
import kr.co.vilez.ui.profile.BoardListAdapter
import kr.co.vilez.ui.share.ShareListAdapter
import kr.co.vilez.util.ApplicationClass
import kr.co.vilez.util.Common
import retrofit2.awaitResponse

private const val TAG = "빌리지_ProfileFragment"
class ProfileFragment : Fragment() {
    private lateinit var binding:FragmentProfileBinding
    private lateinit var mainActivity: MainActivity

    private lateinit var myAdapter: BoardListAdapter
    private lateinit var myList:ArrayList<BoardData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        binding.fragment = this

        binding.user = ApplicationClass.prefs.getUser()
        binding.userDetail = ApplicationClass.prefs.getUserDetail()
        
        initData() // 현재 대여중인 정보 가져오기

        return binding.root
    }
    
    private fun initData() {
        myList = arrayListOf()
        myAdapter = BoardListAdapter(myList)
        binding.rvAppointmentList.apply {
            adapter = myAdapter
            layoutManager =
                LinearLayoutManager(mainActivity, LinearLayoutManager.HORIZONTAL, false)
        }

        var index = 0
        CoroutineScope(Dispatchers.Main).launch {
            val result = ApplicationClass.retrofitAppointmentService.getMyAppointment(ApplicationClass.prefs.getId()).awaitResponse().body()
            Log.d(TAG, "initData: result: $result")
            if (result?.flag == "success") {
                Log.d(TAG, "initList: success!!!!!  검색 결과 : ${result.data[0].size}  result: $result")
                if (result.data.isEmpty()) {
                    Log.d(TAG, "onViewCreated: 데이터 0개")
                }
                for (data in result.data[0]) {
                    val boardData = BoardData(
                        data.myAppointListVO.id,
                        if (data.imgPathList.isNullOrEmpty()) Common.DEFAULT_PROFILE_IMG else data.imgPathList[0].path,
                        data.myAppointListVO.title,
                        "2023-02-07 13:55:03", // TODO : DATE 넣어줘야함
                        data.myAppointListVO.startDay+ " ~ " + data.myAppointListVO.endDay,
                        data.bookmarkCnt.toString(),
                        data.myAppointListVO.userId,
                        data.myAppointListVO.type
                    )
                    Log.d(TAG, "추가?: $boardData")
                    myList.add(boardData)
                }
            } else {
                Log.d(TAG, "initData: 실패!!")
            }
            Log.d(TAG, "추가완료: myList: $myList")
            myAdapter.notifyItemInserted(index - 1)
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMenus()

        CoroutineScope(Dispatchers.Main).launch {
            delay(150) // 뷰가 다 뜨면 화면에 보여주기
            binding.clProfile.visibility = View.VISIBLE
        }
    }

    private fun initMenus() {
        // 나의 공유 메뉴 초기화
        val sharedMenuName = arrayOf("공유 캘린더", "나의 작성글", "나의 관심글", "포인트 내역")
        val shareSimpleAdapter = ArrayAdapter(mainActivity, android.R.layout.simple_list_item_1, sharedMenuName)
        binding.lvMenuShare.apply {
            divider = null
            adapter = shareSimpleAdapter
            setOnItemClickListener { _, _, i, _ ->
                // addToBackStack(null) 동작 안돼서 ACTIVITY 사용
                moveEditActivity(sharedMenuName[i])
            }
        }

        // 나의 공유 박스 메뉴 초기화
        val myShareBoxMenuName = arrayOf("나의 공유 물품", "나의 대여 물품")
        val myShareBoxSimpleAdapter =  ArrayAdapter(mainActivity, android.R.layout.simple_list_item_1, myShareBoxMenuName)
        binding.lvMenuMyBox.apply {
            divider = null
            adapter = myShareBoxSimpleAdapter
            setOnItemClickListener { _, _, i, _ ->
                moveEditActivity(myShareBoxMenuName[i])
            }
        }

        // 설정 메뉴 초기화
        val settingsMenuName = arrayOf("내 동네 설정")
        val settingsSimpleAdapter =  ArrayAdapter(mainActivity, android.R.layout.simple_list_item_1, settingsMenuName)
        binding.lvMenuSettings.apply {
            divider = null
            adapter = settingsSimpleAdapter
            setOnItemClickListener { _, _, i, _ ->
                moveEditActivity(settingsMenuName[i])
            }
        }

        // 계정 메뉴 초기화
        val accountMenuName = arrayOf("로그아웃")
        val accountSimpleAdapter =  ArrayAdapter(mainActivity, android.R.layout.simple_list_item_1, accountMenuName)
        binding.lvMenuAccount.apply {
            divider = null
            adapter = accountSimpleAdapter
            setOnItemClickListener { _, view, i, _ ->
                if(i == 0) { //로그아웃
                    Log.d(TAG, "initMenus: 로그아웃 버튼 클릭~")
                    logout(view)
                }
            }
        }
    }

    fun mannerHelp(view: View) { // 매너 지수 클릭했을 때 알림 메시지
        val dialog = AlertDialog(mainActivity, "매너점수는 빌리지 사용자로부터 받은 후기,\n운영자 제재 등을 종합해서 만든 매너 지표입니다.")
        dialog.show(mainActivity.supportFragmentManager, "MannerHelp")
    }


    fun login(view:View) {
        CoroutineScope(Dispatchers.Main).launch {
            val user = User("test@naver.com", "12345")
            val result = ApplicationClass.retrofitUserService.getLoginResult(user).awaitResponse().body()
            if (result == null) { // 로그인 실패
                Log.d(TAG, "login: 로그인 실패, result:$result")
            } else if(result.flag == "success") {  // 로그인 성공
                val dialog = AlertDialogWithCallback(object : AlertDialogInterface {
                    override fun onYesButtonClick(id: String) {
                        Log.d(TAG, "로그인 성공, 받아온 user = ${result.data[0]}")
                        val userInfo:User = result.data[0]
                        binding.user = userInfo
                    }
                }, "로그인 성공", "")
                dialog.isCancelable = false // 알림창이 띄워져있는 동안 배경 클릭 막기
                dialog.show(mainActivity.supportFragmentManager, "RegisterSucceeded")
            }
        }
    }

    private fun logout(view: View){ // 로그아웃 preference 지우기
        val dialog = ConfirmDialog(object: ConfirmDialogInterface {
            override fun onYesButtonClick(id: String) {
                Log.d(TAG, "logout: 로그아웃 성공")
                // 로그아웃 후 로그인 화면이동
                ApplicationClass.prefs.removeAll() // Shared Preference 삭제
                val intent = Intent(mainActivity, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }, "정말로 로그아웃 하시겠습니까?", "")

        dialog.isCancelable = false // 알림창이 띄워져있는 동안 배경 클릭 막기
        dialog.show(mainActivity.supportFragmentManager, "Logout")
    }


    fun getMannerLevel(manner : Int): String {
        return if(manner <= 10) {
            "Lv.1"
        } else if (manner <= 20) {
            "Lv.2"
        } else if (manner <= 30) {
            "Lv.3"
        } else if (manner <= 40) {
            "Lv.4"
        } else {
            "Lv.5"
        }
    }

    fun editProfile(view: View) { // 프로필 이미지, 닉네임 변경하는 곳으로 이동
        moveEditActivity("프로필 수정")
    }

    private fun moveEditActivity(fragment: String) {
        val intent = Intent(mainActivity, ProfileMenuActivity::class.java)
        intent.putExtra("fragment", fragment)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        mainActivity.startActivity(intent)
    }

}