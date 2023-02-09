package kr.co.vilez.ui.user

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.vilez.R
import kr.co.vilez.data.model.User
import kr.co.vilez.databinding.FragmentLoginBinding
import kr.co.vilez.util.ApplicationClass
import kr.co.vilez.ui.dialog.MyAlertDialog
import kr.co.vilez.util.*
import retrofit2.awaitResponse

private const val TAG = "빌리지_LoginFragment"
class LoginFragment : Fragment() {
    private lateinit var binding:FragmentLoginBinding
    private lateinit var loginActivity:LoginActivity
    private val userViewModel by activityViewModels<UserViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginActivity = context as LoginActivity

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        binding.fragment = this
        initToolBar()
        return binding.root
    }

    private fun initToolBar() {
        loginActivity.setSupportActionBar(binding.toolbar)
        loginActivity.supportActionBar?.setDisplayShowTitleEnabled(false) // 기본 타이틀 제거
        binding.title = "로그인"
    }

    fun loginBtnClicked(view: View) {
        val email = binding.inputLoginEmail.editText?.text.toString()
        val password = binding.inputLoginPassword.editText?.text.toString()

        Log.d(TAG, "login: email : $email, password: $password")

        if(email == "" || password == "") {
            val dialog = MyAlertDialog(loginActivity, "아이디 비밀번호를 확인해주세요.")
            dialog.show(loginActivity.supportFragmentManager, "NaverRegisterFailed")
        } else {
            loginActivity.login(User(email, password))
        }
    }


    fun moveFragment(view: View) {
        when (view.id) {
            R.id.tv_login_findpw -> { // 비밀번호 찾기
                loginActivity.openFragment(3)
            }
            R.id.btn_register -> { // 회원가입
                loginActivity.openFragment(2)
            }
        }
    }

    fun kakaoLogin(view: View) {
        // 카카오계정으로 로그인
        UserApiClient.instance.loginWithKakaoAccount(loginActivity) { token, error ->
            if (token != null) {
                Log.i(TAG, "로그인 성공 ${token.accessToken}")
                // 사용자 정보 요청 (기본)
                UserApiClient.instance.me { user, error ->
                    if (user != null) {

                        Log.i(TAG, "사용자 정보 요청 성공" +
                                "\n회원번호: ${user.id}" +
                                "\n이메일: ${user.kakaoAccount?.email}" +
                                "\n닉네임: ${user.kakaoAccount?.profile?.nickname}" +
                                "\n프로필사진: ${user.kakaoAccount?.profile?.thumbnailImageUrl}")
                        val userId = user.id.toString()
                        CoroutineScope(Dispatchers.Main).launch {
                            Log.d(TAG, "kakaoLogin: 이미 있는 유저인지 확인하기")
                            val result = ApplicationClass.userApi.isUsedEmail(userId).awaitResponse().body()
                            Log.d(TAG, "oauth register: $result")
                            if (result?.flag == "success") {
                                if (!(result.data as List<Boolean> )[0]) { // 새로운 회원
                                    Log.d(TAG, "onSuccess: 새로운 회원 , id: $userId")
                                    loginActivity.openFragment(6, userId)
                                } else { // 이미 존재하는 회원
                                    Log.d(TAG, "onSuccess: 이미 있는 회원, id: $userId")
                                    val user = User(email = userId, password = Common.makeRandomPassword(userId))
                                    user.oauth = "kakao"

                                    loginActivity.login(user)
                                }
                            } else {
                                Log.d(TAG, "fail: email check 실패, result:$result")
                                Toast.makeText(loginActivity, "네이버 로그인을 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            } else {
                Log.d(TAG, "kakaoLogin: 카카오 로그인 실패 에러 : : $error")
                val dialog = MyAlertDialog(loginActivity, "카카오 로그인을 실패했습니다.")
                dialog.show(loginActivity.supportFragmentManager, "KakaoLoginFailed")
            }
        }
    }

    fun cancelAuth(view: View) {
        // 카카오 연동 해제 (테스트용)
        // 연결 끊기
//        UserApiClient.instance.unlink { error ->
//            if (error != null) {
//                Log.e(TAG, "연결 끊기 실패", error)
//            }
//            else {
//                Log.i(TAG, "연결 끊기 성공. SDK에서 토큰 삭제 됨")
//            }
//        }

        // 네이버 연동 해제 (삭제 테스트용)
//        NaverIdLoginSDK.logout()
//        NidOAuthLogin().callDeleteTokenApi(loginActivity, object : OAuthLoginCallback {
//            override fun onSuccess() {
//                //서버에서 토큰 삭제에 성공한 상태입니다.
//                Log.d(TAG, "onSuccess: 서버에 토큰 삭제 [연동 해제 완료]")
//            }
//            override fun onFailure(httpStatus: Int, message: String) {
//                // 서버에서 토큰 삭제에 실패했어도 클라이언트에 있는 토큰은 삭제되어 로그아웃된 상태입니다.
//                // 클라이언트에 토큰 정보가 없기 때문에 추가로 처리할 수 있는 작업은 없습니다.
//                Log.d(TAG, "errorCode: ${NaverIdLoginSDK.getLastErrorCode().code}")
//                Log.d(TAG, "errorDesc: ${NaverIdLoginSDK.getLastErrorDescription()}")
//            }
//            override fun onError(errorCode: Int, message: String) {
//                // 서버에서 토큰 삭제에 실패했어도 클라이언트에 있는 토큰은 삭제되어 로그아웃된 상태입니다.
//                // 클라이언트에 토큰 정보가 없기 때문에 추가로 처리할 수 있는 작업은 없습니다.
//                onFailure(errorCode, message)
//            }
//        })
    }



    val oauthLoginCallback = object : OAuthLoginCallback {
        override fun onSuccess() {
            Log.d(TAG, "onSuccess: 네이버 로그인 성공! accessToken: ${NaverIdLoginSDK.getAccessToken()}")
            // 네이버 로그인 인증이 성공했을 때 수행할 코드 추가
            NidOAuthLogin().callProfileApi(profileCallback)
        }
        override fun onFailure(httpStatus: Int, message: String) {
            val errorCode = NaverIdLoginSDK.getLastErrorCode().code
            val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
            Log.d(TAG, "onFailure: 네이버 로그인 실패, errorCode:$errorCode, errorDesc:$errorDescription")
            Toast.makeText(loginActivity, "네이버 로그인을 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
        }
        override fun onError(errorCode: Int, message: String) {
            onFailure(errorCode, message)
            Log.d(TAG, "onError: 로그인 실패")
            Toast.makeText(loginActivity, "네이버 로그인을 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
        }
    }

    val profileCallback = object : NidProfileCallback<NidProfileResponse> {
        override fun onSuccess(response: NidProfileResponse) {
            Log.d(TAG, "onSuccess: 불러온 네이버 프로필 result: $response")
            val profileId = response.profile?.id!!
            Log.d(TAG, "onSuccess: id: $profileId")
            CoroutineScope(Dispatchers.Main).launch {
                val result = ApplicationClass.userApi.isUsedEmail(profileId).awaitResponse().body()
                Log.d(TAG, "oauth register: $result")
                if (result?.flag == "success") {
                    if (!(result.data as List<Boolean> )[0]) { // 새로운 회원
                        Log.d(TAG, "onSuccess: 새로운 회원 , id: $profileId")
                        loginActivity.openFragment(5, profileId)
                    } else { // 이미 존재하는 회원
                        Log.d(TAG, "onSuccess: 이미 있는 회원, id: $profileId")
                        val user = User(profileId, Common.makeRandomPassword(profileId))
                        user.oauth = "naver"
                        loginActivity.login(user)
                    }
                } else {
                    Log.d(TAG, "fail: email check 실패, result:$result")
                    Toast.makeText(loginActivity, "네이버 로그인을 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                }
            }
        }
        override fun onFailure(httpStatus: Int, message: String) {
            Log.d(TAG, "onFailure: 네이버 프로필 불러오기 실패, httpStatus:$httpStatus, 메시지:$message")
            Toast.makeText(loginActivity, "네이버 로그인을 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
            val errorCode = NaverIdLoginSDK.getLastErrorCode().code
            val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
            Log.d(TAG, "onFailure: errorCode: $errorCode, errorDesc: $errorDescription")
        }
        override fun onError(errorCode: Int, message: String) {
            Log.d(TAG, "onError: 네이버 프로필 불러오기 에러 $")
            Toast.makeText(loginActivity, "네이버 로그인을 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
        }
    }


    fun naverLogin(view: View) {
        NaverIdLoginSDK.showDevelopersLog(true)

        NaverIdLoginSDK.initialize(loginActivity,
            getString(R.string.naver_client_id),
            getString(R.string.naver_client_secret)
            , getString(R.string.naver_client_name))

        NaverIdLoginSDK.authenticate(loginActivity, oauthLoginCallback)
    }



    fun kakaoUserJoin(id: String) {

    }

}

