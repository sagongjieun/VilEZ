package kr.co.vilez.ui.user

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.vilez.R
import kr.co.vilez.data.model.Email
import kr.co.vilez.data.model.User
import kr.co.vilez.databinding.FragmentRegisterBinding
import kr.co.vilez.ui.dialog.AlertDialogInterface
import kr.co.vilez.ui.dialog.AlertDialogWithCallback
import kr.co.vilez.util.ApplicationClass
import kr.co.vilez.util.Common
import retrofit2.awaitResponse

private const val TAG = "빌리지_RegisterFragment"
class RegisterFragment : Fragment() {
    private lateinit var binding:FragmentRegisterBinding
    private lateinit var registerActivity: RegisterActivity

    private val viewModel:RegisterViewModel by lazy {
        ViewModelProvider(this)[RegisterViewModel::class.java] // 생명주기를 RegisterActivity 와 맞춰줌
    }

    private var isValidEmail = false
    private var isValidPassword = false
    private var isValidPasswordAgain = false
    private var isValidNickname = false

    private var emailAuthCheck = false  // 이메일 인증 확인
    private var nicknameCheck = false // 닉네임 중복 확인
    private var checkedNickname = "" // 제일 최근에 확인된 닉네임
    private var isEmailSent = false // 이메일 인증 번호 전송

    private lateinit var emailAuthHashed:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerActivity = context as RegisterActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_register, container, false)
        binding.fragment = this
        initToolBar()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initBinding()
        initView()
    }

    private fun initToolBar() {
        registerActivity.setSupportActionBar(binding.toolbar)
        registerActivity.supportActionBar?.setDisplayShowTitleEnabled(false) // 기본 타이틀 제거
        binding.title = "빌리지 주민 신청서"
    }

    fun onBackPressed(view: View) {
        registerActivity.finish()
    }

    private fun initBinding() {
        binding.isEmailSent = isEmailSent
        binding.isValidEmail = isValidEmail
        binding.isValidPassword = isValidPassword
        binding.isValidPasswordAgain = isValidPasswordAgain
    }

    @SuppressLint("ResourceAsColor")
    private fun initView() {

//        binding.inputRegisterEmail.editText?.addTextChangedListener {
//            isValidEmail = Common.verifyEmail(it.toString())
//            if(isValidEmail) { // 인증하기 버튼 활성화 (회색 -> 흰색)
//                binding.inputRegisterEmail.error = null
//                binding.btnRegisterEmailAuth.apply {
//                    isClickable = true
//                    //setBackgroundResource(R.drawable.btn_stroke_fill)
//                    backgroundTintList = context.resources.getColorStateList(R.color.main_1)
//                    setTextColor(context.resources.getColorStateList(R.color.white))
//                }
//            } else { // 에러 메시지
//                binding.inputRegisterEmail.error = "올바르지 않은 이메일 형식입니다"
//                binding.btnRegisterEmailAuth.isClickable = false // 버튼 클릭 불가
//            }
//        }

        binding.inputRegisterPassword.editText?.addTextChangedListener {
            isValidPassword = Common.verifyPassword(it.toString())
            if(isValidPassword) {
                binding.inputRegisterPassword.error= null
            } else {
                binding.inputRegisterPassword.error="8~16자 사이 숫자, 문자 사용 필요"
            }
        }

        binding.inputRegisterPasswordCheck.editText?.addTextChangedListener {
            //Log.d(TAG, "initView: 비번 : ${binding.inputRegisterPassword.editText?.text.toString()}, 비번확인:${it.toString()}")
            isValidPasswordAgain = binding.inputRegisterPassword.editText?.text.toString() == it.toString()
            if(isValidPasswordAgain) {
                binding.inputRegisterPasswordCheck.error = null
                binding.inputRegisterPasswordCheck.helperText="비밀번호가 일치합니다."
            } else {
                binding.inputRegisterPasswordCheck.error="입력한 비밀번호와 불일치 합니다"
                binding.inputRegisterPasswordCheck.helperText=null
            }
        }

        binding.inputRegisterEmailAuth.editText?.addTextChangedListener {
            binding.btnRegisterEmailAuthCheck.isClickable = it.toString().length >= 8
        }

        binding.inputRegisterNickname.editText?.addTextChangedListener {
            isValidNickname = Common.verifyNickname(it.toString())
            if(!isValidNickname) {
                Log.d(TAG, "initView: 불가 닉네임 체크! ${it.toString()}")
                binding.inputRegisterNickname.error = "2~6자 사이 한글,영어,숫자만 가능합니다."
            } else {
                Log.d(TAG, "initView: 가능 닉네임 체크! ${it.toString()}")
                binding.inputRegisterNickname.error = null
            }
        }
    }


    fun register(view: View) { // 회원가입
        if(!emailAuthCheck) { // 이메일 확인
            Log.d(TAG, "register: 이메일 인증 안됨")
            Toast.makeText(registerActivity, "이메일 인증을 해주세요", Toast.LENGTH_SHORT).show()
            return;
        } else if (!isValidPassword or !isValidPasswordAgain) { // 비밀번호 두개 확인
            Log.d(TAG, "register: 비밀번호 안됨")
            Toast.makeText(registerActivity, "비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show()
            return;
        } else if (!nicknameCheck) { // 닉네임 확인 (가장 최근에 인증 완료된 닉네임 vs 지금 입력된 닉네임)
            Log.d(TAG, "register: 닉네임 인증 안됨")
            Toast.makeText(registerActivity, "닉네임 확인 해주세요", Toast.LENGTH_SHORT).show()
            return;
        } else { // 모두 통과
            val email = binding.inputRegisterEmail.editText?.text.toString()
            val password = binding.inputRegisterPassword.editText?.text.toString()
            CoroutineScope(Dispatchers.Main).launch {
                val user = User(email=email, password = password, nickName = checkedNickname)
                Log.d(TAG, "register: 회원가입 할 유저 정보 : $user")
                val result = ApplicationClass.retrofitUserService.getJoinResult(user).awaitResponse().body()
                Log.d(TAG, "register: $result")
                if(result?.flag == "success") {
                    val dialog = AlertDialogWithCallback(object :AlertDialogInterface {
                        override fun onYesButtonClick(id: String) {
                            Log.d(TAG, "register: 회원가입 success")
                            registerActivity.moveToLogin() // 로그인 화면으로 이동
                        }
                    }, "회원가입에 성공했습니다.\n\n가입한 정보로 로그인 해주세요", "")
                    dialog.isCancelable = false // 알림창이 띄워져있는 동안 배경 클릭 막기
                    dialog.show(registerActivity.supportFragmentManager, "RegisterSucceeded")
                } else {
                    Log.d(TAG, "register: 회원가입 POST 요청 실패")
                    val dialog = AlertDialogWithCallback(object :AlertDialogInterface {
                        override fun onYesButtonClick(id: String) {
                            registerActivity.moveToLogin() // 로그인 화면으로 이동
                        }
                    }, "회원가입에 실패했습니다.\n다시 시도 해주세요", "")
                    dialog.show(registerActivity.supportFragmentManager, "RegisterFailed")
                }
            }
        }
    }

    @SuppressLint("UseCompatLoadingForColorStateLists", "ResourceAsColor")
    fun checkEmailAuth(view:View) {
        val data = binding.inputRegisterEmailAuth.editText?.text.toString()
        Log.d(TAG, "checkEmailAuth: 입력받은 비밀번호 : $data, 해싱 : ${Common.getHash(data)}")
        if (Common.getHash(data) == emailAuthHashed) { // SHA-256 으로 해싱해서 비교

            binding.apply {
                inputRegisterEmail.editText?.isEnabled = false // 이메일 수정 불가
                btnRegisterEmailAuth.isEnabled = false
                btnRegisterEmailAuth.isClickable = false
                inputRegisterEmailAuth.editText?.isEnabled = false // 인증번호 수정 불가
            }

            // 인증 완료 버튼 클릭 불가
            binding.btnRegisterEmailAuthCheck.apply {
                text = "인증 완료"
                isEnabled = false
                isClickable = false
                backgroundTintList = context.resources.getColorStateList(R.color.main_1)
                setTextColor(context.resources.getColorStateList(R.color.white))
            }
            emailAuthCheck = true
            Log.d(TAG, "checkEmailAuth: 이메일 인증번호 맞음")
        } else {
            Log.d(TAG, "checkEmailAuth: 이메일 인증번호 틀림")
        }
    }

    fun onEmailChangeListener(s:CharSequence, start:Int, before:Int, count:Int) {
        isValidEmail = Common.verifyEmail(s.toString())
    }

    fun onPasswordChangeLister(s:CharSequence, start:Int, before:Int, count:Int) {
        isValidPassword = Common.verifyPassword(s.toString())
    }

    fun onPasswordAgainChangeLister(s:CharSequence, start:Int, before:Int, count:Int) {
        isValidPasswordAgain = isValidPassword and (binding.inputRegisterPassword.editText.toString() == s.toString())
    }

    fun sendEmailAuth(view:View) {
        view.isClickable = false
        val email = binding.inputRegisterEmail.editText?.text.toString()
        Log.d(TAG, "checkEmail: 이메일 인증 버튼 클릭, email:${email}")
        isValidEmail = Common.verifyEmail(email)
        if(!isValidEmail) { // 이메일 형식이 아닌 경우 => 에러 메시지
            binding.inputRegisterEmail.error = "올바르지 않은 이메일 형식입니다"
            view.isClickable = true
            return
        }
        // 올바른 이메일 형식이면
        CoroutineScope(Dispatchers.Main).launch {
            val result = ApplicationClass.retrofitEmailService.checkEmail(Email(email)).awaitResponse().body()
            if(result != null) {
                when(result.flag) {
                    "success" -> {
                        val data = result.data as ArrayList<*>
                        Log.d(TAG, "checkEmail: 이메일 전송 완료, data: ${data[0]}")
                        emailAuthHashed = data[0] as String
                        isEmailSent = true // 이메일 전송 완료
                        binding.inputRegisterEmail.error = null // 에러 메시지 삭제

                        binding.inputRegisterEmailAuth.apply {
                            visibility = View.VISIBLE
                            isEnabled = true
                        }
                        binding.btnRegisterEmailAuthCheck.apply {
                            visibility = View.VISIBLE
                            isEnabled = true
                            isClickable = false
                        }
                        binding.btnRegisterEmailAuth.text = "다시전송"
                    }
                    "duplicate" -> {
                        isEmailSent = false
                        binding.inputRegisterEmail.error = "이미 사용중인 이메일입니다."
                        Log.d(TAG, "checkEmail: 이미 사용중인 이메일입니다.")
                    }
                    else -> {
                        isEmailSent = false
                        Log.d(TAG, "checkEmail: 이메일 전송 실패")
                        binding.inputRegisterEmail.error = "인증번호 전송을 실패했습니다."
                    }
                }
            } else {
                Log.d(TAG, "checkEmail: Email POST 요청 실패")
                binding.inputRegisterEmail.error = "인증번호 전송을 실패했습니다."
            }
            view.isClickable = true // 다시 전송 버튼 클릭 가능
        }

    }

    fun checkNickName(view:View) {
        view.isClickable = false
        val nickname = binding.inputRegisterNickname.editText?.text.toString()
        Log.d(TAG, "checkNickName: 닉네임 중복 버튼 클릭, nickName:$nickname")
        // TODO : 닉네임 예쁜말 썼는지 확인
        if(false) {
            nicknameCheck = false
            binding.inputRegisterNickname.helperText = null
            binding.inputRegisterNickname.error = "사용 불가한 닉네임입니다."
            Log.d(TAG, "checkNickName: 나쁜말 닉네임")
            view.isClickable = true // 다시 클릭 가능
        }

        if(isValidNickname) {
            CoroutineScope(Dispatchers.Main).launch {
                val result = ApplicationClass.retrofitUserService.isUsedUserNickName(nickname).awaitResponse().body()
                Log.d(TAG, "checkNickName: 닉네임 중복 확인, result : $result")
                if(result != null && result.flag == "success") {
                    nicknameCheck = true
                    checkedNickname = nickname
                    Log.d(TAG, "checkNickName: 사용가능한 닉네임")
                    binding.inputRegisterNickname.helperText = "사용가능한 닉네임입니다."
                    binding.inputRegisterNickname.error = null
                    binding.btnRegisterNicknameCheck.apply {
                        isEnabled = false
                        isClickable = false
                        text = "확인완료"
                        backgroundTintList = context.resources.getColorStateList(R.color.main_1)
                        setTextColor(context.resources.getColorStateList(R.color.white))
                    }
                    binding.inputRegisterNickname.editText?.isEnabled = false
                } else {
                    nicknameCheck = false
                    binding.inputRegisterNickname.helperText = null
                    binding.inputRegisterNickname.error = "이미 사용중인 닉네임입니다."
                    Log.d(TAG, "checkNickName: 사용 불가한 닉네임")
                }
                view.isClickable = true // 다시 클릭 가능
            }
        } else {
            binding.inputRegisterNickname.error = "사용 불가한 닉네임입니다."
            view.isClickable = true // 다시 클릭 가능
        }
    }

}