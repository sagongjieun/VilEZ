package kr.co.vilez.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.vilez.R
import kr.co.vilez.databinding.FragmentHomeBinding
import kr.co.vilez.ui.chat.RoomlistData
import kr.co.vilez.ui.share.ShareActivity
import kr.co.vilez.util.ApplicationClass
import kr.co.vilez.util.DataState
import retrofit2.awaitResponse

class HomeFragment : Fragment() {
    private lateinit var binding:FragmentHomeBinding
    private lateinit var mainActivity: MainActivity
override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding.fragment = this


        return binding.root
    }

    companion object {

    }

    fun moveToShareActivity(view: View) {
        val intent = Intent(mainActivity, ShareActivity::class.java)
        mainActivity.startActivity(intent)
    }
}