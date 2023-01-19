package kr.co.vilez.ui.user

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.vilez.R
import kr.co.vilez.data.model.User
import kr.co.vilez.databinding.FragmentProfileBinding
import kr.co.vilez.ui.IntroActivity
import kr.co.vilez.ui.MainActivity
import kr.co.vilez.util.ApplicationClass
import retrofit2.awaitResponse

private const val TAG = "빌리지_ProfileFragment"
class ProfileFragment : Fragment() {
    private lateinit var binding:FragmentProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                Log.d(TAG, "로그인 성공, 받아온 user = ${result}")
                binding.tvProfileUserInfo.text = result.data.toString()

            }
        }
    }

    fun logout(view:View) {

    }


}