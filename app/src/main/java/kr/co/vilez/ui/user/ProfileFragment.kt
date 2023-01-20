package kr.co.vilez.ui.user

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.edit
import androidx.databinding.DataBindingUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.vilez.R
import kr.co.vilez.data.model.User
import kr.co.vilez.databinding.FragmentProfileBinding
import kr.co.vilez.ui.IntroActivity
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
        return binding.root
    }

    fun login(view:View) {
        Log.d(TAG, "login: adf")
        CoroutineScope(Dispatchers.Main).launch {
            val user = User("test", "12345")
            val result = ApplicationClass.retrofitUserService.getLoginResult(user).awaitResponse().body()
            if (result == null) { // 로그인 실패
                Log.d(TAG, "login: 로그인 실패, result:$result")
                binding.tvProfileUserInfo.text = "로그인 실패"
            } else if(result.flag == "success") {  // 로그인 성공
                val dialog = AlertDialogWithCallback(object : AlertDialogInterface {
                    override fun onYesButtonClick(id: String) {
                        Log.d(TAG, "로그인 성공, 받아온 user = ${result.data[0]}")
                        binding.tvProfileUserInfo.text = result.data[0].toString()
                    }
                }, "로그인 성공", "")
                dialog.isCancelable = false // 알림창이 띄워져있는 동안 배경 클릭 막기
                dialog.show(mainActivity.supportFragmentManager, "RegisterSucceeded")


            }
        }
    }

    fun logout(view: View){ // 로그아웃 preference 지우기
        val dialog = ConfirmDialog(object: ConfirmDialogInterface {
            override fun onYesButtonClick(id: String) {
                Log.d(TAG, "logout: 삭제 전 autoLogin = ${ApplicationClass.sharedPreferences.getBoolean("autoLogin",false)}")
                ApplicationClass.sharedPreferences.edit {
                    remove("autoLogin")
                    apply()
                }

                Log.d(TAG, "logout: 로그아웃 성공")
                Log.d(TAG, "logout: 삭제 후 autoLogin = ${ApplicationClass.sharedPreferences.getBoolean("autoLogin", false)}")

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

}