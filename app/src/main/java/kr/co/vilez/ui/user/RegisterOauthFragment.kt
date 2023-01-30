package kr.co.vilez.ui.user

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.vilez.R
import kr.co.vilez.data.model.User
import kr.co.vilez.databinding.FragmentRegisterOauthBinding
import kr.co.vilez.ui.dialog.AlertDialog
import kr.co.vilez.ui.dialog.AlertDialogInterface
import kr.co.vilez.ui.dialog.AlertDialogWithCallback
import kr.co.vilez.ui.profile.PointFragment
import kr.co.vilez.util.ApplicationClass
import kr.co.vilez.util.Common
import retrofit2.awaitResponse
private const val ARG_PARAM1 = "id"

private const val TAG = "빌리지_RegisterOauthFragment"
class RegisterOauthFragment : Fragment() {
    private lateinit var binding:FragmentRegisterOauthBinding
    private lateinit var loginActivity: LoginActivity

    private var isValidNickname = false
    private var nicknameCheck = false
    private var checkedNickname = ""

    private var profileId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            profileId = it.getString(ARG_PARAM1)
        }
        loginActivity = context as LoginActivity

    }

    companion object {
        @JvmStatic
        fun newInstance(id: String) =
            RegisterOauthFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, id)
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register_oauth, container, false)

        binding.fragment = this
        initToolBar()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.inputOauthRegisterNickname.editText?.addTextChangedListener {
            isValidNickname = Common.verifyNickname(it.toString())
            if(!isValidNickname) {
                Log.d(TAG, "initView: 불가 닉네임 체크! ${it.toString()}")
                binding.inputOauthRegisterNickname.error = "2~6자 사이 한글,영어,숫자만 가능합니다."
            } else {
                Log.d(TAG, "initView: 가능 닉네임 체크! ${it.toString()}")
                binding.inputOauthRegisterNickname.error = null
            }
        }
    }

    private fun initToolBar() {
        loginActivity.setSupportActionBar(binding.toolbar)
        loginActivity.supportActionBar?.setDisplayShowTitleEnabled(false) // 기본 타이틀 제거
        binding.title = "닉네임 입력"
    }

    fun checkNickName(view:View) {
        view.isClickable = false
        val nickname = binding.inputOauthRegisterNickname.editText?.text.toString()
        Log.d(TAG, "checkNickName: 닉네임 중복 버튼 클릭, nickName:$nickname")

        if(isValidNickname) {
            CoroutineScope(Dispatchers.Main).launch {
                val result = ApplicationClass.retrofitUserService.isUsedUserNickName(nickname).awaitResponse().body()
                Log.d(TAG, "checkNickName: 닉네임 중복 확인, result : $result")
                if(result != null && result.flag == "success") {
                    nicknameCheck = true
                    checkedNickname = nickname
                    Log.d(TAG, "checkNickName: 사용가능한 닉네임")
                    binding.inputOauthRegisterNickname.helperText = "사용가능한 닉네임입니다."
                    binding.inputOauthRegisterNickname.error = null
                    binding.btnRegisterOauthNicknameCheck.apply {
                        isEnabled = false
                        isClickable = false
                        text = "확인완료"
                    }
                    binding.inputOauthRegisterNickname.editText?.isEnabled = false
                } else {
                    nicknameCheck = false
                    binding.inputOauthRegisterNickname.helperText = null
                    binding.inputOauthRegisterNickname.error = "이미 사용중인 닉네임입니다."
                    Log.d(TAG, "checkNickName: 사용 불가한 닉네임")
                }
                view.isClickable = true // 다시 클릭 가능
            }
        } else {
            binding.inputOauthRegisterNickname.error = "사용 불가한 닉네임입니다."
            view.isClickable = true // 다시 클릭 가능
        }
    }
    private fun naverUserRegister(id: String) {
        CoroutineScope(Dispatchers.Main).launch {
            val naverUser = User(email=id, password = Common.makeRandomPassword(id), nickName = "$checkedNickname", oauth = "naver")
            Log.d(TAG, "register: 회원가입 할 네이버 유저 정보 : $naverUser")
            val result = ApplicationClass.retrofitUserService.getJoinResult(naverUser).awaitResponse().body()
            Log.d(TAG, "register: $result")
            if(result?.flag == "success") {
                val dialog = AlertDialogWithCallback(object : AlertDialogInterface {
                    override fun onYesButtonClick(id: String) {
                        Log.d(TAG, "register: 회원가입 success")
                        // loginActivity.openFragment(4) // 프로필 화면으로 넘어가기
                        // TODO : 로그인 데이터 넣은 후 메인화면으로 넘어가기
                        loginActivity.login(naverUser)
                    }
                }, "회원가입에 성공했습니다.", "")
                dialog.isCancelable = false // 알림창이 띄워져있는 동안 배경 클릭 막기
                dialog.show(loginActivity.supportFragmentManager, "NaverRegisterSucceeded")
            } else {
                Log.d(TAG, "register: 회원가입 POST 요청 실패")
                val dialog = AlertDialog(loginActivity, "네이버 로그인에 실패했습니다.\n다시 시도 해주세요")
                dialog.show(loginActivity.supportFragmentManager, "NaverRegisterFailed")
            }
        }
    }



    fun register(view: View) {
        // 넘어온 네이버 프로필 id 정보로 회원가입 실시
        if (!nicknameCheck) { // 닉네임 확인 (가장 최근에 인증 완료된 닉네임 vs 지금 입력된 닉네임)
            Log.d(TAG, "register: 닉네임 인증 안됨")
            Toast.makeText(loginActivity, "닉네임 중복확인을 해주세요", Toast.LENGTH_SHORT).show()
            return;
        } else { // 모두 통과
            CoroutineScope(Dispatchers.Main).launch {
                val result = ApplicationClass.retrofitUserService.isUsedEmail(profileId!!).awaitResponse().body()
                Log.d(TAG, "oauth register: $result")
                if (result?.flag == "success") {
                    if (!(result.data as List<Boolean> )[0]) { // 새로운 회원
                        Log.d(TAG, "onSuccess: 새로운 회원 , id: $profileId")
                        naverUserRegister(profileId!!)
                    } else { // 이미 존재하는 회원
                        Log.d(TAG, "onSuccess: 이미 있는 회원, id: $profileId")
                        val user = User(profileId!!, Common.makeRandomPassword(profileId!!))
                        loginActivity.login(user)
                    }
                } else {
                    Log.d(TAG, "onSuccess: email check 실패, result:$result")
                    val dialog = AlertDialog(loginActivity, "로그인에 실패했습니다.\n다시 시도 해주세요")
                    dialog.show(loginActivity.supportFragmentManager, "OAuthRegisterFailed")
                }
            }
        }

    }
}