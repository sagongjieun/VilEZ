package kr.co.vilez.ui.user

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import kotlinx.coroutines.*
import kr.co.vilez.R
import kr.co.vilez.data.dto.BoardData
import kr.co.vilez.data.model.User
import kr.co.vilez.databinding.FragmentProfileBinding
import kr.co.vilez.ui.LoginActivity
import kr.co.vilez.ui.MainActivity
import kr.co.vilez.ui.dialog.*
import kr.co.vilez.ui.share.ShareToChatAdapter
import kr.co.vilez.util.ApplicationClass
import kr.co.vilez.util.Common
import retrofit2.awaitResponse

private const val TAG = "빌리지_ProfileFragment"
class ProfileFragment : Fragment() {
    private lateinit var binding:FragmentProfileBinding
    private lateinit var mainActivity: MainActivity

    private lateinit var myAdapter: ShareToChatAdapter
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
        myAdapter = ShareToChatAdapter(myList)
        binding.rvImminentList.apply {
            adapter = myAdapter
            layoutManager =
                LinearLayoutManager(mainActivity, LinearLayoutManager.HORIZONTAL, false)
        }

        var index = 0
        CoroutineScope(Dispatchers.Main).launch {
            val result = ApplicationClass.hAppointmentApi.getMyImminent(ApplicationClass.prefs.getId()).awaitResponse().body()
            Log.d(TAG, "initData: result: $result")
            if (result?.flag == "success") {
                Log.d(TAG, "initList: success!!!!!  검색 결과 : ${result.data[0].size}  result: $result")
                if (result.data.isEmpty() || result.data[0].isEmpty()) {
                    Log.d(TAG, "onViewCreated: 데이터 0개")
                    binding.tvReminder.visibility = View.GONE // 잊지마세요!! 문구 안보이게 하기
                } else {
                    binding.tvReminder.visibility = View.VISIBLE
                }
                for (data in result.data[0]) {
                    Log.d(TAG, "initData: data: $data")
                    if(data.appointmentDto.title.isNullOrEmpty()) {
                        Log.d(TAG, "initData: 타이틀 널이라 스킵")
                        continue
                    };
                    val boardData = BoardData(
                        data.appointmentDto.boardId,
                        data.appointmentDto.roomId,
                        if (data.imgPath.isNullOrEmpty()) Common.DEFAULT_PROFILE_IMG else data.imgPath[0].path,
                        data.appointmentDto.title,
                        "", // date는 필요없음
                        data.appointmentDto.appointmentStart+ " ~ " + data.appointmentDto.appointmentEnd,
                        data.bookmarkCnt.toString(),
                        data.appointmentDto.shareUserId, // TODO : 내꺼냐 남꺼냐에 따라서 id 바꿔서 넣어줘야함
                        data.appointmentDto.shareUserId,
                        data.appointmentDto.notShareUserId,
                        type = data.appointmentDto.type,
                        sDay = data.appointmentDto.appointmentStart,
                        eDay = data.appointmentDto.appointmentEnd,
                        status = data.appointmentDto.status // 시작일 임박! or 종료일 임박!
                    )
                    Log.d(TAG, "추가?: $boardData")
                    myList.add(boardData)
                    myAdapter.notifyItemInserted(myList.size)
                }
            } else {
                Log.d(TAG, "initData: 실패!!")
            }
            Log.d(TAG, "추가완료: myList: $myList")
            //myAdapter.notifyItemInserted(index - 1)
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
        val dialog = MyAlertDialog(mainActivity, "매너점수는 빌리지 사용자로부터 받은 후기,\n운영자 제재 등을 종합해서 만든 매너 지표입니다.")
        dialog.show(mainActivity.supportFragmentManager, "MannerHelp")
    }


    fun login(view:View) {
        CoroutineScope(Dispatchers.Main).launch {
            val user = User("test@naver.com", "12345")
            val result = ApplicationClass.userApi.getLoginResult(user).awaitResponse().body()
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
                when(ApplicationClass.prefs.getOAuth()) {
                    "kakao" -> {  // 카카오인 경우 로그아웃
//                        UserApiClient.instance.unlink { error ->
//                            if (error != null) {
//                                Log.e(TAG, "연결 끊기 실패", error)
//                            }
//                            else {
//                                Log.i(TAG, "연결 끊기 성공. SDK에서 토큰 삭제 됨")
//                            }
//                        }
                    }
                    "naver" -> {  // 네이버 연동 해제
                        NaverIdLoginSDK.logout()
                        NidOAuthLogin().callDeleteTokenApi(mainActivity, object :
                            OAuthLoginCallback {
                            override fun onSuccess() {
                                //서버에서 토큰 삭제에 성공한 상태입니다.
                                Log.d(TAG, "onSuccess: 서버에 토큰 삭제 [연동 해제 완료]")
                            }
                            override fun onFailure(httpStatus: Int, message: String) {
                                // 서버에서 토큰 삭제에 실패했어도 클라이언트에 있는 토큰은 삭제되어 로그아웃된 상태입니다.
                                // 클라이언트에 토큰 정보가 없기 때문에 추가로 처리할 수 있는 작업은 없습니다.
                                Log.d(TAG, "errorCode: ${NaverIdLoginSDK.getLastErrorCode().code}")
                                Log.d(TAG, "errorDesc: ${NaverIdLoginSDK.getLastErrorDescription()}")
                            }
                            override fun onError(errorCode: Int, message: String) {
                                // 서버에서 토큰 삭제에 실패했어도 클라이언트에 있는 토큰은 삭제되어 로그아웃된 상태입니다.
                                // 클라이언트에 토큰 정보가 없기 때문에 추가로 처리할 수 있는 작업은 없습니다.
                                onFailure(errorCode, message)
                            }
                        })
                    }
                }

                CoroutineScope(Dispatchers.Main).launch {
                    val result = ApplicationClass.userApi.postLogout(ApplicationClass.prefs.getUser()).awaitResponse().body()
                    if(result?.flag == "success") {
                        Log.d(TAG, "logout: 로그아웃 성공")
                        // 로그아웃 후 로그인 화면이동
                        ApplicationClass.prefs.removeAll() // Shared Preference 삭제
                        val intent = Intent(mainActivity, LoginActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    } else {
                        val networkDialog = MyAlertDialog(mainActivity,"네트워크에 접속할 수 없습니다.\n네트워크 연결상태를 확인해주세요.")
                        networkDialog.show(mainActivity.supportFragmentManager, "NetworkFail")
                        Log.d(TAG, "onYesButtonClick: 네트워크 없어서 로그아웃 실패함. 그래도 일단 시켜주기.")
                        ApplicationClass.prefs.removeAll() // Shared Preference 삭제
                        val intent = Intent(mainActivity, LoginActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    }
                }
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