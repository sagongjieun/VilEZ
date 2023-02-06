package kr.co.vilez.ui.profile

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.Intent.*
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.vilez.R
import kr.co.vilez.data.model.User
import kr.co.vilez.databinding.FragmentEditProfileBinding
import kr.co.vilez.ui.MainActivity
import kr.co.vilez.util.BindingAdapter
import kr.co.vilez.ui.user.LoginActivity
import kr.co.vilez.ui.user.ProfileMenuActivity
import kr.co.vilez.util.ApplicationClass
import kr.co.vilez.util.ChangeMultipartUtil
import kr.co.vilez.util.Common
import kr.co.vilez.util.Common.Companion.DEFAULT_PROFILE_IMG
import kr.co.vilez.util.PermissionUtil
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.awaitResponse
import java.io.File

private const val TAG = "빌리지_EditProfileFragment"
class EditProfileFragment : Fragment() {

    private lateinit var binding:FragmentEditProfileBinding
    private lateinit var profileMenuActivity: ProfileMenuActivity
    private lateinit var mContext: Context

    private var isCorrectCurrentPassword = false
    private var isValidPassword = false
    private var isValidPasswordAgain = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        profileMenuActivity = context as ProfileMenuActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_profile, container, false)
        binding.fragment = this
        binding.user = ApplicationClass.prefs.getUser()
        Log.d(TAG, "onCreateView: 유저 정보: ${binding.user}")
        getUserDetail(ApplicationClass.prefs.getId())
        initView()
        initToolBar()

        return binding.root
    }

    private fun initToolBar() {
        profileMenuActivity.setSupportActionBar(binding.toolbar)
        profileMenuActivity.supportActionBar?.setDisplayShowTitleEnabled(false) // 기본 타이틀 제거
        binding.toolbarTitle.text = "프로필 수정"
    }
    fun onBackPressed(view: View) {
        profileMenuActivity.finish()
    }

    private fun getUserDetail(userId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val result =
                ApplicationClass.retrofitUserService.getUserDetail(userId).awaitResponse().body()
            if (result?.flag == "success") {
                val data = result.data[0]
                Log.d(TAG, "user detail 조회 성공, 받아온 user = $data")
                binding.userDetail = data // user detail data binding
            } else {
                Log.d(TAG, "user detail 조회 실패, result:$result")
            }
        }
    }
    // 이미지 갤러리에서 선택할 시  콜백
    private val imageResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->

        if (result.resultCode == RESULT_OK) {
            val imageUri = result.data?.data ?: return@registerForActivityResult

            val file = File(ChangeMultipartUtil().changeAbsolutelyPath(imageUri, mContext))
            val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
            val body = MultipartBody.Part.createFormData("image", file.name, requestFile)

            Log.d(TAG, "body: $body")
            CoroutineScope(Dispatchers.Main).launch {
//                val result = ApplicationClass.retrofitUserService.modifyProfileImage(
//                    ApplicationClass.prefs.getUserAccessToken(),
//                    ApplicationClass.prefs.getId(),
//                    body
//                ).awaitResponse().body()
                val result = ApplicationClass.hRetrofitUserService.modifyProfileImage(
                    ApplicationClass.prefs.getId(),
                    body
                ).awaitResponse().body()
                if (result?.flag == "success") {
                    Log.d(TAG, "프로필이미지변경성공: ")
                    binding.ivProfileImg.setImageURI(imageUri) // 일단 미리보기 이미지 바뀐 이미지로 변경
                    Toast.makeText(profileMenuActivity, "프로필 이미지 변경을 성공했습니다.", Toast.LENGTH_SHORT).show()
                    // 디테일 갱신
                    val resultDetail = ApplicationClass.retrofitUserService.getUserDetail(ApplicationClass.prefs.getId()).awaitResponse().body()
                    if(resultDetail?.flag == "success") {
                        Log.d(TAG, "프로필 이미지 수정: Detail조회 성공~, result: ${resultDetail.data[0]}")
                        ApplicationClass.prefs.setUserDetail(resultDetail.data[0])
                    }
                    refreshActivity()
                } else {
                    Log.d(TAG, "프로필이미지변경 실패: ")
                }
            }
        }
    } // End of registerForActivityResult

    private fun refreshActivity(data: String? = null) {
        val intent = Intent(profileMenuActivity, MainActivity::class.java)
        intent.putExtra("target", "나의 빌리지")
        intent.addFlags(FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(FLAG_ACTIVITY_NO_ANIMATION)
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
    fun changeProfileImage(view: View) {
        view.isClickable = false
        view.isEnabled = false
        if (view.id == R.id.btn_profile_img_change) {
            // 바뀐 이미지 가져와서 수정
            PermissionUtil().galleryPermission(false, mContext, imageResult)
        } else {
            CoroutineScope(Dispatchers.Main).launch {
//                val result = ApplicationClass.retrofitUserService.removeProfileImage(
//                    ApplicationClass.prefs.getUserAccessToken(),
//                    ApplicationClass.prefs.getId(),
//                ).awaitResponse().body()
                val result = ApplicationClass.hRetrofitUserService.removeProfileImage(ApplicationClass.prefs.getId(),).awaitResponse().body()

                if (result?.flag == "success") {
                    BindingAdapter.bindCircleImageFromUrl(binding.ivProfileImg, DEFAULT_PROFILE_IMG) // 일단 미리보기 이미지기본 이미지로 수정
                    Log.d(TAG, "프로필이미지 삭제성공: ")
                    Toast.makeText(profileMenuActivity, "프로필 이미지 변경을 성공했습니다.", Toast.LENGTH_SHORT).show()

                    ApplicationClass.prefs.setProfileImg(DEFAULT_PROFILE_IMG) // prefs도 수정

                    refreshActivity()
                } else {
                    Log.d(TAG, "프로필이미지 삭제 실패: ")
                    Toast.makeText(profileMenuActivity, "프로필 이미지 변경을 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
        view.isClickable = true
        view.isEnabled = true

    }

    fun changeNickName(view: View) {
        view.isClickable = false
        view.isEnabled = false

        val newNickname = binding.inputProfileNickname.editText?.text.toString()
        if(newNickname == ApplicationClass.prefs.getNickName()) {
            binding.inputProfileNickname.helperText = null
            binding.inputProfileNickname.error = "현재 닉네임과 동일합니다."
            view.isClickable = true
            view.isEnabled = true
            return
        }

        CoroutineScope(Dispatchers.Main).launch {
            val result = ApplicationClass.retrofitUserService.isUsedUserNickName(newNickname).awaitResponse().body()
            Log.d(TAG, "checkNickName: 닉네임 중복 확인, result : $result")
            if(result?.flag == "success") {
                Log.d(TAG, "checkNickName: 사용가능한 닉네임")

                binding.inputProfileNickname.error = null

                val newUser = User(id=ApplicationClass.prefs.getId(), nickName = newNickname, password = "")
                val modifyResult = ApplicationClass.hRetrofitUserService.modifyUser(newUser).awaitResponse().body()
                Log.d(TAG, "changeNickName: modifyResult: $modifyResult")
                if(modifyResult?.flag == "success") {
                    Log.d(TAG, "changeNickName: 닉네임 변경 성공")
                    binding.inputProfileNickname.helperText = null
                    binding.inputProfileNickname.error = null

                    Toast.makeText(profileMenuActivity, "닉네임 변경을 성공했습니다.", Toast.LENGTH_SHORT).show()
                    ApplicationClass.prefs.setNickName(newNickname)
                    refreshActivity()
                } else {
                    Log.d(TAG, "changeNickName: 닉네임 변경 실패")
                }
            } else {
                binding.inputProfileNickname.helperText = null
                binding.inputProfileNickname.error = "이미 사용중인 닉네임입니다."
                Log.d(TAG, "checkNickName: 이미 사용중인 닉네임")
            }
        }
        view.isClickable = true
        view.isEnabled = true
    }


    fun changePassword(view: View) {
        val newPassword = binding.inputProfileNewPassword.editText?.text.toString()
        val newUser = User(id = ApplicationClass.prefs.getId(), nickName = "", password = newPassword)
        // 현재 비밀번호 체크
        val curPassword = binding.inputProfileCurrentPassword.editText?.text.toString()

        CoroutineScope(Dispatchers.Main).launch {
            val user = User(email = ApplicationClass.prefs.getEmail(), password = curPassword)
            val result =
                ApplicationClass.retrofitUserService.getLoginResult(user).awaitResponse().body()
            if (result?.flag == "success") {
                Log.d(TAG, "checkCurrentPassword: 현재 비밀번호 맞음")
                binding.inputProfileCurrentPassword.error = null
                val resultModify = ApplicationClass.hRetrofitUserService.modifyUser(newUser).awaitResponse().body()

                if(resultModify?.flag == "success") {
                    Toast.makeText(mContext, "비밀번호 수정이 완료되었습니다.\n재로그인 해주세요", Toast.LENGTH_SHORT).show()
                    ApplicationClass.prefs.removeAll() // Shared Preference 삭제
                    Log.d(TAG, "logout: 로그아웃 성공")

                    // 로그아웃 후 로그인 화면이동
                    val intent = Intent(mContext, LoginActivity::class.java)
                    intent.addFlags(FLAG_ACTIVITY_CLEAR_TASK)
                    intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
                    intent.addFlags(FLAG_ACTIVITY_NO_ANIMATION)

                    startActivity(intent)
                } else {
                    Toast.makeText(mContext, "비밀번호 수정을 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                }

            } else {
                Log.d(TAG, "login: 현재 비밀번호 틀림, result:$result")
                isCorrectCurrentPassword = false
                binding.inputProfileCurrentPassword.error = "현재 비밀번호가 틀립니다."
            }
        }
    }

    private fun initView() {
        // 신규 비밀번호 양식 확인
        binding.inputProfileNewPassword.editText?.addTextChangedListener {
            isValidPassword = Common.verifyPassword(it.toString())
            if(isValidPassword) {
                binding.inputProfileNewPassword.error= null
            } else {
                binding.inputProfileNewPassword.error="8~16자 사이 숫자, 문자 사용 필요"
            }
        }
        
        // 신규 비밀번호 재확인 확인
        binding.inputProfileNewPasswordAgain.editText?.addTextChangedListener {
            //Log.d(TAG, "initView: 비번 : ${binding.inputProfileNewPassword.editText?.text.toString()}, 비번확인:${it.toString()}")
            isValidPasswordAgain = binding.inputProfileNewPassword.editText?.text.toString() == it.toString()
            if(isValidPasswordAgain) {
                binding.inputProfileNewPasswordAgain.error = null
                binding.inputProfileNewPasswordAgain.helperText="비밀번호가 일치합니다."
            } else {
                binding.inputProfileNewPasswordAgain.error="입력한 비밀번호와 불일치 합니다"
                binding.inputProfileNewPasswordAgain.helperText=null
            }
        }
    }
}