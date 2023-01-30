package kr.co.vilez.ui.user

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
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
import kr.co.vilez.ui.MainActivity
import kr.co.vilez.ui.chat.RoomlistData
import kr.co.vilez.ui.dialog.AlertDialog
import kr.co.vilez.ui.dialog.AlertDialogInterface
import kr.co.vilez.ui.dialog.AlertDialogWithCallback
import kr.co.vilez.util.*
import org.json.JSONArray
import org.json.JSONObject
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
            val dialog = AlertDialog(loginActivity, "아이디 비밀번호를 확인해주세요.")
            dialog.show(loginActivity.supportFragmentManager, "NaverRegisterFailed")
        } else {
            loginActivity.login(User(email, password))
        }
    }

    /*fun login(user: User) {
        CoroutineScope(Dispatchers.Main).launch {
            val result =
                ApplicationClass.retrofitUserService.getLoginResult(user).awaitResponse().body()
            if (result?.flag == "success") {
                Log.d(TAG, "로그인 성공, 받아온 user = ${result.data[0]}")
                ApplicationClass.prefs.setUser(result.data[0])
                ApplicationClass.prefs.setAutoLogin(user) // 로그인시 자동으로 자동로그인 넣기

                StompClient.runStomp()

                val resultDetail = ApplicationClass.retrofitUserService.getUserDetail(result.data[0].id).awaitResponse().body()
                if(resultDetail?.flag == "success") {
                    Log.d(TAG, "login: Detail조회도 로그인와 같이 성공~, result: ${resultDetail.data[0]}")
                    ApplicationClass.prefs.setUserDetail(resultDetail.data[0])
                }

                val intent = Intent(loginActivity, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            } else {
                Log.d(TAG, "login: 로그인 실패, result:$result")

            }
        }
    }*/

    fun loginToProfile(user: User) {
        // 닉네임을 변경하지 않은 oauth 사용자의 경우 닉네임 변경 유도를 위해 프로필 창으로 이동시킴
        CoroutineScope(Dispatchers.Main).launch {
            val result =
                ApplicationClass.retrofitUserService.getLoginResult(user).awaitResponse().body()
            if (result?.flag == "success") {
                Log.d(TAG, "로그인 성공, 받아온 user = ${result.data[0]}")
                ApplicationClass.prefs.setUser(result.data[0])
                ApplicationClass.prefs.setAutoLogin(user) // 로그인시 자동으로 자동로그인 넣기

                StompClient.runStomp()

                val resultDetail = ApplicationClass.retrofitUserService.getUserDetail(result.data[0].id).awaitResponse().body()
                if(resultDetail?.flag == "success") {
                    Log.d(TAG, "login: Detail조회도 로그인와 같이 성공~, result: ${resultDetail.data[0]}")
                    ApplicationClass.prefs.setUserDetail(resultDetail.data[0])
                }

                val intent = Intent(loginActivity, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            } else {
                Log.d(TAG, "login: 로그인 실패, result:$result")

            }
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
        //NaverIdLoginSDK.logout()
        NidOAuthLogin().callDeleteTokenApi(loginActivity, object : OAuthLoginCallback {
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

    fun naverLogin(view: View) {
        NaverIdLoginSDK.showDevelopersLog(true)

        NaverIdLoginSDK.initialize(loginActivity,
            getString(R.string.naver_client_id),
            getString(R.string.naver_client_secret)
            , getString(R.string.naver_client_name))

        NaverIdLoginSDK.authenticate(loginActivity, oauthLoginCallback)

    }
    val oauthLoginCallback = object : OAuthLoginCallback {
        override fun onSuccess() {
            Log.d(TAG, "onSuccess: 네이버 로그인 성공!")
            // 네이버 로그인 인증이 성공했을 때 수행할 코드 추가
            /*binding.tvAccessToken.text = NaverIdLoginSDK.getAccessToken()
            binding.tvRefreshToken.text = NaverIdLoginSDK.getRefreshToken()
            binding.tvExpires.text = NaverIdLoginSDK.getExpiresAt().toString()
            binding.tvType.text = NaverIdLoginSDK.getTokenType()
            binding.tvState.text = NaverIdLoginSDK.getState().toString()*/
            NidOAuthLogin().callProfileApi(object: NidProfileCallback<NidProfileResponse> {
                override fun onError(errorCode: Int, message: String) {
                    Log.d(TAG, "onError: 네이버 프로필 불러오기 에러 $")
                    Toast.makeText(loginActivity, "네이버 로그인을 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(httpStatus: Int, message: String) {
                    Log.d(TAG, "onFailure: 네이버 프로필 불러오기 실패, httpStatus:$httpStatus, 메시지:$message")
                    Toast.makeText(loginActivity, "네이버 로그인을 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                }

                override fun onSuccess(result: NidProfileResponse) {
                    Log.d(TAG, "onSuccess: 불러온 네이버 프로필 result: $result")
                    // TODO: 넘어오는 id를 #naver{키값} 으로 변경해서 DB에 register 시키기.
                    // 홈 화면으로 넘어가기 =>
                    val profileId = result.profile?.id!!
                    Log.d(TAG, "onSuccess: id: $profileId")

                    CoroutineScope(Dispatchers.Main).launch {
                        val result = ApplicationClass.retrofitUserService.isUsedEmail(profileId).awaitResponse().body()
                        Log.d(TAG, "oauth register: $result")
                        if (result?.flag == "success") {
                            if (!(result.data as List<Boolean> )[0]) { // 새로운 회원
                                Log.d(TAG, "onSuccess: 새로운 회원 , id: $profileId")
                                // TODO : 닉네임 입력받아서 회원가입 시키기
                                loginActivity.openFragment(5, profileId)
                            } else { // 이미 존재하는 회원
                                Log.d(TAG, "onSuccess: 이미 있는 회원, id: $profileId")
                                val user = User(profileId, Common.makeRandomPassword(profileId))
                                loginActivity.login(user)
                            }
                        } else {
                            Log.d(TAG, "fail: email check 실패, result:$result")
                            Toast.makeText(loginActivity, "네이버 로그인을 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                        }
                    }
                    
                }

            })
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



    fun kakaoUserJoin(id: String) {

    }

}

