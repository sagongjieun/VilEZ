package kr.co.vilez.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
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
import kr.co.vilez.util.DataState
import kr.co.vilez.util.StompClient
import org.json.JSONArray
import org.json.JSONObject
import kr.co.vilez.ui.share.ShareDetailActivity

private const val TAG = "빌리지_HomeFragment"
class HomeFragment : Fragment() {
    private lateinit var binding:FragmentHomeBinding
    private lateinit var mainActivity: MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = context as MainActivity
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding.fragment = this

        initToolBar()

        var data = JSONObject()
        data.put("userId", 29)
        StompClient.runStomp()

        StompClient.stompClient.topic("/send_room_list/29").subscribe { topicMessage ->
            run {
                val json = JSONArray(topicMessage.payload)
                println(json.toString())
                CoroutineScope(Dispatchers.Main).launch {

                    DataState.itemList = ArrayList<RoomlistData>()
                    for (index in 0 until json.length()) {
                        val chat = JSONObject(json.get(index).toString())
                        val chatData = chat.getJSONObject("chatData")
                        DataState.itemList.add(
                            RoomlistData(
                                chatData.getInt("roomId"), chat.getString("nickName"),
                                chatData.getString("content"),
                                chat.getString("area")
                            )
                        )
                        DataState.set.add(chatData.getInt("roomId"))
                    }
                }
            }
        }
        StompClient.stompClient.send("/room_list", data.toString()).subscribe()
        return binding.root
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.top_app_bar, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.option_menu_menu -> {
                Log.d(TAG, "onContextItemSelected: ${item.title} clicked")
                Toast.makeText(mainActivity, "${item.title} 클릭", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun initToolBar() {
        mainActivity.setSupportActionBar(binding.toolbar)
        mainActivity.supportActionBar?.setDisplayShowTitleEnabled(false) // 기본 타이틀 제거
        binding.title = "진평동"
    }

    companion object {

    }

    fun moveToShareActivity(view: View) {
        val intent = Intent(mainActivity, ShareDetailActivity::class.java)
        mainActivity.startActivity(intent)
    }
}