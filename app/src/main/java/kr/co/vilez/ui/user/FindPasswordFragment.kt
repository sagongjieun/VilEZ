package kr.co.vilez.ui.user

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.vilez.R
import kr.co.vilez.data.model.Email
import kr.co.vilez.data.model.User
import kr.co.vilez.databinding.FragmentFindPasswordBinding
import kr.co.vilez.ui.LoginActivity
import kr.co.vilez.util.ApplicationClass
import kr.co.vilez.util.Common
import kr.co.vilez.util.NetworkResult
import kr.co.vilez.viewmodel.FindPasswordViewModel
import retrofit2.awaitResponse

private const val TAG = "빌리지_FindPasswordFragment"
class FindPasswordFragment : Fragment() {
    private lateinit var binding:FragmentFindPasswordBinding
    private lateinit var activity:LoginActivity
    //private val viewModel: FindPasswordViewModel by viewModels()
    private lateinit var viewModel: FindPasswordViewModel

    private var emailAuthCheck = false  // 이메일 인증 확인
    private var isEmailSent = false // 이메일 인증 번호 전송
    private lateinit var tempPwHashed:String // 임시 비번

    private var isValidEmail = false // 이메일 유효성 확인
    private var isValidPassword = false // 비밀번호 유효성 확인
    private var isValidPasswordAgain = false // 비밀번호 재입력 동일성 확인

    private val findPasswordViewModel by activityViewModels<FindPasswordViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = context as LoginActivity
        viewModel = ViewModelProvider(this).get(FindPasswordViewModel::class.java)

        arguments?.let {

        }
    }

    private fun initBinding() {
        binding.isEmailSent = isEmailSent
        binding.isValidEmail = isValidEmail
        binding.isValidPassword = isValidPassword
        binding.isValidPasswordAgain = isValidPasswordAgain
    }


    private fun findPasswordObserver() {
        findPasswordViewModel.findPasswordResponseLiveData.observe(viewLifecycleOwner) {
            when(it) {
                is NetworkResult.Success -> {
                    if (it.data?.flag == "success") {
                        Log.d(TAG, "findPasswordObserver: 성공  ${it.data.data}")
                    } else {

                    }
                }
                is NetworkResult.Error-> {
                    Log.d(TAG, "findPasswordObserver: 에러  ${it.data}")
                }
                is NetworkResult.Loading -> {
                    // progress bar
                }
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_find_password, container, false)
        binding.fragment = this
        initBinding()
        findPasswordObserver()
        initView()
        return binding.root
    }

    fun onBackPressed(view: View) {
        activity.supportFragmentManager.popBackStack()
    }

    @SuppressLint("ResourceAsColor")
    private fun initView() {
        binding.inputFindpwPassword.editText?.addTextChangedListener {
            isValidPassword = Common.verifyPassword(it.toString())
            if(isValidPassword) {
                binding.inputFindpwPassword.error= null
            } else {
                binding.inputFindpwPassword.error="8~16자 사이 숫자, 문자 사용 필요"
            }
        }

        binding.inputFindpwPasswordCheck.editText?.addTextChangedListener {
            //Log.d(TAG, "initView: 비번 : ${binding.inputFindpwPassword.editText?.text.toString()}, 비번확인:${it.toString()}")
            isValidPasswordAgain = binding.inputFindpwPassword.editText?.text.toString() == it.toString()
            if(isValidPasswordAgain) {
                binding.inputFindpwPasswordCheck.error = null
                binding.inputFindpwPasswordCheck.helperText="비밀번호가 일치합니다."
            } else {
                binding.inputFindpwPasswordCheck.error="입력한 비밀번호와 불일치 합니다"
                binding.inputFindpwPasswordCheck.helperText=null
            }
        }

        binding.inputFindpwEmailAuth.editText?.addTextChangedListener {
            binding.btnFindpwEmailAuthCheck.isClickable = it.toString().length >= 8
        }

    }

    fun sendEmailAuth(view: View) {
        view.isClickable = false
        val email = binding.inputFindpwEmail.editText?.text.toString()
        isValidEmail = Common.verifyEmail(email)
        if(!isValidEmail) { // 이메일 형식이 아닌 경우 => 에러 메시지
            binding.inputFindpwEmail.error = "올바르지 않은 이메일 형식입니다"
            view.isClickable = true
            return
        }
        // 올바른 이메일인 경우 비번 전송 시작
        CoroutineScope(Dispatchers.Main).launch {
            //findPasswordViewModel.sendEmailAuth(email)
            val result = ApplicationClass.hEmailApi.getTempPassword2(Email(email)).awaitResponse().body()
            if(result?.flag=="success") {
                Log.d(TAG, "sendEmailAuth: 비번 전송 성공! $result")
                val data = result.data as ArrayList<*>
                Log.d(TAG, "checkEmail: 이메일 전송 완료, data: ${data[0]}")
                tempPwHashed = data[0] as String
                isEmailSent = true // 이메일 전송 완료

                binding.inputFindpwEmail.error = null // 에러 메시지 삭제

                binding.inputFindpwEmailAuth.apply {
                    visibility = View.VISIBLE
                    isEnabled = true
                }
                binding.btnFindpwEmailAuthCheck.apply {
                    visibility = View.VISIBLE
                    isEnabled = true
                    isClickable = false
                }
                binding.btnFindpwEmailAuth.apply { // 이메일 인증 버튼 재클릭 불가
                    text = "다시 전송"
                    backgroundTintList = context.resources.getColorStateList(R.color.main_1)
                    setTextColor(context.resources.getColorStateList(R.color.white))
                }
            } else {
                isEmailSent = false
                Toast.makeText(activity, "임시 비밀번호 전송을 실패했습니다.\n이메일 확인 후 다시 시도해주세요",Toast.LENGTH_SHORT).show()
                view.isClickable = true
            }
        }
    }

    @SuppressLint("UseCompatLoadingForColorStateLists")
    fun checkEmailAuth(view: View) {
        val data = binding.inputFindpwEmailAuth.editText?.text.toString()
        Log.d(TAG, "checkEmailAuth: 입력받은 비밀번호 : $data, 해싱 : ${Common.getHash(data)}")
        if (Common.getHash(data) == tempPwHashed) { // SHA-256 으로 해싱해서 비교
            binding.apply {
                inputFindpwEmail.editText?.isEnabled = false // 이메일 수정 불가
                inputFindpwEmail.hint="이메일 인증 완료"
                inputFindpwEmailAuth.editText?.isEnabled = false // 인증번호 수정 불가
            }
            
            binding.btnFindpwEmailAuth.apply { // 이메일 인증 버튼 재클릭 불가
                text = "인증 완료"
                isEnabled = false
                isClickable = false
                binding.inputFindpwEmailAuth.error = null
                binding.inputFindpwEmailAuth.hint = "인증번호 확인 완료"
                backgroundTintList = context.resources.getColorStateList(R.color.main_1)
                setTextColor(context.resources.getColorStateList(R.color.white))
            }

            // 인증 완료 버튼 클릭 불가
            binding.btnFindpwEmailAuthCheck.apply {
                text = "인증 완료"
                isEnabled = false
                isClickable = false
                backgroundTintList = context.resources.getColorStateList(R.color.main_1)
                setTextColor(context.resources.getColorStateList(R.color.white))
            }
            emailAuthCheck = true
            Log.d(TAG, "checkEmailAuth: 임시비번 인증번호 맞음")

            // 비밀번호 보이기
            binding.isAuthSucceeded = true
            binding.inputFindpwPassword.visibility = View.VISIBLE
            binding.inputFindpwPasswordCheck.visibility = View.VISIBLE

            // 비밀번호 재설정 버튼 보이기
            binding.btnFindpwChangePw.visibility = View.VISIBLE

        } else {
            Log.d(TAG, "checkEmailAuth: 임시비번 틀림")
            binding.inputFindpwEmailAuth.error = "인증번호가 올바르지 않습니다."
        }
    }

    fun changePassword(view: View) { // 비밀번호 변경 버튼
        if(!emailAuthCheck) { // 이메일 확인
            Log.d(TAG, "register: 이메일 인증 안됨")
            Toast.makeText(activity, "이메일 인증을 해주세요", Toast.LENGTH_SHORT).show()
            return;
        } else if (!isValidPassword or !isValidPasswordAgain) { // 비밀번호 두개 확인
            Log.d(TAG, "register: 비밀번호 안됨")
            Toast.makeText(activity, "비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show()
            return;
        } else { // 모두 통과
            val email = binding.inputFindpwEmail.editText?.text.toString()
            val password = binding.inputFindpwPassword.editText?.text.toString()
            val hashedpassword = Common.getHash(password)
            Log.d(TAG, "changePassword: 이메일 : $email, 비번: $password, 해시비번: $hashedpassword")
            CoroutineScope(Dispatchers.Main).launch {
                val result = ApplicationClass.hUserApi.changePassword(email = email, password = hashedpassword).awaitResponse().body()
                if (result?.flag == "success") {
                    Log.d(TAG, "changePassword: 비밀번호 변경 결과:$result")

                    Toast.makeText(activity, "비밀번호 재설정이 완료되었습니다.\n재로그인 해주세요", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "logout: 로그아웃 성공")

                    // 로그아웃 후 로그인 화면이동
                    ApplicationClass.prefs.removeAll() // Shared Preference 삭제
                    val intent = Intent(activity, LoginActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    startActivity(intent)
                } else {
                    Toast.makeText(activity, "비밀번호 재설정을 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }


}