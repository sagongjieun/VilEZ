package kr.co.vilez.ui.profile

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.Intent.*
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.vilez.R
import kr.co.vilez.data.model.User
import kr.co.vilez.databinding.FragmentEditProfileBinding
import kr.co.vilez.ui.MainActivity
import kr.co.vilez.ui.user.BindingAdapter
import kr.co.vilez.ui.user.ProfileMyShareActivity
import kr.co.vilez.util.ApplicationClass
import kr.co.vilez.util.ChangeMultipartUtil
import kr.co.vilez.util.Common
import kr.co.vilez.util.Common.Companion.DEFAULT_PROFILE_IMG
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.awaitResponse
import java.io.File

private const val TAG = "빌리지_EditProfileFragment"
class EditProfileFragment : Fragment() {

    private lateinit var binding:FragmentEditProfileBinding
    private lateinit var profileActivity: ProfileMyShareActivity
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
        profileActivity = context as ProfileMyShareActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_profile, container, false)
        binding.fragment = this

        binding.user = ApplicationClass.user
        getUserDetail(ApplicationClass.user.id)
        initView()

        return binding.root
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

    private val REQ_GALLERY = 1

    // 이미지 갤러리에서 선택할 시  콜백
    private val imageResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->

        if (result.resultCode == RESULT_OK) {
            val imageUri = result.data?.data ?: return@registerForActivityResult
            binding.ivProfileImg.setImageURI(imageUri)

            val file = File(ChangeMultipartUtil().changeAbsolutelyPath(imageUri, mContext))
            val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
            val body = MultipartBody.Part.createFormData("image", file.name, requestFile)

            Log.d(TAG, "body: $body")
            CoroutineScope(Dispatchers.Main).launch {
                // TODO : 이거 바꿔줘야 함
                val result = ApplicationClass.retrofitUserService.modifyProfileImage(
                    ApplicationClass.user.accessToken,
                    ApplicationClass.user.id,
                    body
                ).awaitResponse().body()
                if (result?.flag == "success") {
                    Log.d(TAG, "프로필이미지변경성공: ")
                    // TODO : 토스트 띄우고 프로필 메인 다시 띄우기
                    Toast.makeText(profileActivity, "프로필 이미지 변경을 성공했습니다.", Toast.LENGTH_SHORT).show()
                    refreshActivity()

                } else {
                    Log.d(TAG, "프로필이미지변경 실패: ")
                }
            }
        }
    } // End of registerForActivityResult

    private fun refreshActivity(data: String? = null) {
        val intent = Intent(profileActivity, MainActivity::class.java)
        intent.putExtra("target", "나의 빌리지")
        intent.addFlags(FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(FLAG_ACTIVITY_NO_ANIMATION)
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
    fun changeProfileImage(view: View) {
        view.isClickable = false
        view.isEnabled = false
        val curUser = ApplicationClass.user
        val newProfileImage = ""

        if (view.id == R.id.btn_profile_img_change) {
            // 바뀐 이미지 가져와서 수정
            val writePermission = ContextCompat.checkSelfPermission(
                mContext, android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            val readPermission = ContextCompat.checkSelfPermission(
                mContext, android.Manifest.permission.READ_EXTERNAL_STORAGE
            )

            if (writePermission == PackageManager.PERMISSION_DENIED || readPermission == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(
                    mContext as Activity, arrayOf(
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE
                    ), REQ_GALLERY
                )
            } else {
                val intent = Intent(Intent.ACTION_PICK)
                intent.setDataAndType(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*"
                )
                imageResult.launch(intent)
            }
        } else {
            // 기본 이미지로 수정
            BindingAdapter.bindImageFromUrl(binding.ivProfileImg, DEFAULT_PROFILE_IMG)

            CoroutineScope(Dispatchers.Main).launch {
                // TODO : 이거 바꿔줘야 함
                val result = ApplicationClass.retrofitUserService.removeProfileImage(
                    ApplicationClass.user.accessToken,
                    ApplicationClass.user.id,
                ).awaitResponse().body()

                val result2 =  ApplicationClass.retrofitUserService.removeProfileImage(
                    ApplicationClass.user.accessToken,
                    ApplicationClass.user.id,
                ).awaitResponse().raw()

                val result3 =  ApplicationClass.retrofitUserService.removeProfileImage(
                    ApplicationClass.user.accessToken,
                    ApplicationClass.user.id,
                ).awaitResponse().errorBody()

                val result4 =  ApplicationClass.retrofitUserService.removeProfileImage(
                    ApplicationClass.user.accessToken,
                    ApplicationClass.user.id,
                ).awaitResponse().headers()
                Log.d(TAG, "changeProfileImage: body:$result\n raw:$result2\n error body:$result3\n headers: $result4")

                if (result?.flag == "success") {
                    Log.d(TAG, "프로필이미지 삭제성공: ")
                    // TODO : 토스트 띄우고 프로필 메인 다시 띄우기
                    Toast.makeText(profileActivity, "프로필 이미지 변경을 성공했습니다.", Toast.LENGTH_SHORT).show()
                    refreshActivity()
                } else {
                    Log.d(TAG, "프로필이미지 삭제 실패: ")
                }
            }
        }
        view.isClickable = true
        view.isEnabled = true

//        CoroutineScope(Dispatchers.Main).launch {
//            val result =
//                ApplicationClass.retrofitUserService.modifyUser(ApplicationClass.user.accessToken, newUser).awaitResponse().body()
//            if (result?.flag == "success") {
//                Log.d(TAG, "changeProfileImage: result: $result")
//                // 수정했다고 토스트 띄우고 창 돌아가기 or 수정되었습니다 다이얼로그 띄우고 창 닫기
//                // TODO: 수정 완료 후 수정되었습니다. 다이얼로그 띄우고 창 뒤로가기, ApplicationClass.user data도 갱신
//            } else {
//                Log.d(TAG, "changeProfileImage: 수정 실패: $result")
//                // TODO: 수정 실패 다이얼로그 띄우기
//            }
//        }
    }

    fun changeNickName(view: View) {
        view.isClickable = false
        view.isEnabled = false

        val newNickname = binding.inputProfileNickname.editText?.text.toString()
        if(newNickname == ApplicationClass.user.nickName) {
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

                // TODO: 닉네임 수정 in here
                val newUser = User(id=ApplicationClass.user.id, nickName = newNickname, password = "")
                val modifyResult = ApplicationClass.retrofitUserService.modifyUser(ApplicationClass.user.accessToken, newUser).awaitResponse().body()
                Log.d(TAG, "changeNickName: modifyResult: $modifyResult")
                if(modifyResult?.flag == "success") {
                    Log.d(TAG, "changeNickName: 닉네임 변경 성공")
                    binding.inputProfileNickname.helperText = null
                    binding.inputProfileNickname.error = null
//                binding.inputProfileNickname.helperText = "변경가능한 닉네임입니다."
                    // TODO: 수정 완료 후 수정되었습니다. 토스트 띄우고 창 뒤로가기, ApplicationClass.user data도 갱신
                    Toast.makeText(profileActivity, "닉네임 변경을 성공했습니다.", Toast.LENGTH_SHORT).show()
                    ApplicationClass.user.nickName = newNickname
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
        val curUser = ApplicationClass.user
        val newPassword = binding.inputProfileNewPassword.editText?.text.toString()

        if(checkCurrentPassword()) {
            // TODO: 입력받은 신규 비밀번호로 회원정보 수정
            /*CoroutineScope(Dispatchers.Main).launch {

            }*/
            // TODO: 수정 완료 후 수정되었습니다. "변경된 비밀번호로 재로그인 해주세요" 다이얼로그 띄우고 로그인 화면으로 넘어가기
        } else {
            Log.d(TAG, "changePassword: 비밀번호 변경 실패")
            // TODO : 실패 다이얼로그 띄우기
        }
    }

    private fun checkCurrentPassword():Boolean {
        // 현재 비밀번호 체크
        val curUser = ApplicationClass.user
        val curPassword = binding.inputProfileCurrentPassword.editText?.text.toString()
        CoroutineScope(Dispatchers.Main).launch {
            val user = User(email = curUser.email, password = curPassword)
            val result =
                ApplicationClass.retrofitUserService.getLoginResult(user).awaitResponse().body()
            if (result?.flag == "success") {
                // 현재 비밀번호 맞게 입력함 => TODO: 변경한 비밀번호가 올바른지 확인 후 비밀번호 변경
                val data = result.data[0]
                isCorrectCurrentPassword = true
                binding.inputProfileCurrentPassword.error = null
            } else {
                Log.d(TAG, "login: 현재 비밀번호 틀림, result:$result")
                isCorrectCurrentPassword = false
                binding.inputProfileCurrentPassword.error = "현재 비밀번호가 틀립니다."
            }
        }
        return isCorrectCurrentPassword and isValidPassword and isValidPasswordAgain
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