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
import kotlinx.coroutines.*
import kr.co.vilez.R
import kr.co.vilez.data.model.User
import kr.co.vilez.databinding.FragmentProfileBinding
import kr.co.vilez.ui.MainActivity
import kr.co.vilez.ui.dialog.*
import kr.co.vilez.util.ApplicationClass
import retrofit2.awaitResponse

private const val TAG = "빌리지_ProfileFragment"
class ProfileFragment : Fragment() {
    private lateinit var binding:FragmentProfileBinding
    private lateinit var mainActivity: MainActivity
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
        //getUserDetail(ApplicationClass.prefs.getId()) // 현재 로그인한 유저 id로 user detail 가져오기

        return binding.root
    }

    private fun getUserDetail(userId: Int) {
        Log.d(TAG, "getUserDetail: 넘어온 유저 id: $userId")
        CoroutineScope(Dispatchers.IO).launch {
            val result =
                ApplicationClass.retrofitUserService.getUserDetail(userId).awaitResponse().body()
            if (result?.flag == "success") {
                val data = result.data[0]
                Log.d(TAG, "user detail 조회 성공, 받아온 user = $data")
                binding.userDetail = data // user detail data binding
                ApplicationClass.prefs.setUserDetail(data) // prefs도 갱신
            } else {
                Log.d(TAG, "user detail 조회 실패, result:$result")
            }
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
        val myShareBoxMenuName = arrayOf("나의 공유중 물품" , "나의 공유예정 물품", "나의 대여 물품", "나의 대여예정 물품")
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
                if(i == 1) { //로그아웃
                    logout(view)
                } else {
                    moveEditActivity(accountMenuName[i])
                }
            }
        }
    }

    fun mannerHelp(view: View) { // 매너 지수 클릭했을 때 알림 메시지
        val dialog = AlertDialog(mainActivity, "매너점수는 빌리지 사용자로부터 받은 후기,\n운영자 제재 등을 종합해서 만든 매너 지표입니다.")
        dialog.show(mainActivity.supportFragmentManager, "MannerHelp")
    }


    fun login(view:View) {
        Log.d(TAG, "login: adf")
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
                ApplicationClass.prefs.removeAll() // Shared Preference 삭제
                Log.d(TAG, "logout: 로그아웃 성공")
                // 로그아웃 후 로그인 화면이동
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