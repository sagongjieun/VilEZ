package kr.co.vilez.ui.chat.room

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.vilez.R
import kr.co.vilez.databinding.FragmentRoomlistBinding
import kr.co.vilez.ui.NotifyInterface
import kr.co.vilez.ui.chat.ChatRoomActivity
import kr.co.vilez.util.ApplicationClass
import kr.co.vilez.util.DataState

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private const val TAG = "빌리지_RoomListFragment"
class RoomListFragment : Fragment(), NotifyInterface {
    private lateinit var binding:FragmentRoomlistBinding
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var mContext: Context
    private lateinit var rootView: View
    private lateinit var rv_room: RecyclerView
    private lateinit var topic: Disposable
    private var data: Int? = 0
    var roomAdapter = RoomAdapter(DataState.itemList)
    private var set = HashSet<Int>()
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        println("onAttach")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        println("onCreate")
    }

    override fun onResume() {
        super.onResume()
        data = 0;
        ApplicationClass.prefs.setRoomId(-1)
        roomAdapter.notifyDataSetChanged()
        println("onResume")
    }

    override fun onDestroy() {
        super.onDestroy()
        println("생명주기 확인")
//        topic.dispose()
    }


    @SuppressLint("CheckResult")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_roomlist, container, false)
        binding.fragment = this
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_roomlist, container, false)
        rv_room = rootView.findViewById(R.id.rv_room) as RecyclerView
        roomAdapter = RoomAdapter(DataState.itemList)
        rv_room.adapter = roomAdapter
        rv_room.layoutManager = LinearLayoutManager(requireContext())


        for (index in 0 until DataState.itemList.size) {
            val chat = DataState.itemList.get(index)
            set.add(chat.roomId)
        }
        var mLastClickTime = 0L
        roomAdapter.setItemClickListener(object : RoomAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                if(SystemClock.elapsedRealtime() - mLastClickTime > 1000) {
                    if(DataState.itemList.size == 0) {
                        Log.d(TAG, "onClick: @@@@@@@@@@@@채팅 목록 없슴")
                        binding.tvWarnNoResult.visibility = View.VISIBLE
                        roomAdapter.notifyDataSetChanged()
                        return
                    }
                    binding.tvWarnNoResult.visibility = View.GONE
                    var intent = Intent(mContext, ChatRoomActivity::class.java)
                    intent.putExtra("roomId", DataState.itemList[position].roomId)
                    intent.putExtra("otherUserId", DataState.itemList[position].otherUserId)
                    intent.putExtra("nickName", DataState.itemList[position].nickName)
                    intent.putExtra("profile", DataState.itemList[position].profile)
                    DataState.itemList[position].noReadCnt = 0
                    data = DataState.itemList[position].roomId
                    ApplicationClass.prefs.setRoomId(DataState.itemList[position].roomId)
                    startActivity(intent)
                    mLastClickTime = SystemClock.elapsedRealtime();
                }
            }
        })

//        topic = StompHelper.stompClient.join("/sendlist/" + ApplicationClass.prefs.getId())
//            .subscribe { topicMessage ->
//                run {
//                    CoroutineScope(Dispatchers.Main).launch {
//                        val json = JSONObject(topicMessage)
//                        val roomId = json.getInt("roomId")
//                        var index = -1;
//                        if (roomId in DataState.set) {
//                            for (i in 0 until DataState.itemList.size) {
//                                if (DataState.itemList[i].roomId == roomId) {
//                                    index = i
//                                    break
//                                }
//                            }
//                            if(data != roomId) {
//                                DataState.itemList[index].noReadCnt++
//                            } else {
//                                DataState.itemList[index].noReadCnt = 0
//                            }
//                            DataState.itemList[index].content = json.getString("content")
//
//                            roomAdapter.notifyItemChanged(index)
//
//                            val item = DataState.itemList.get(index)
//                            if (index != 0) {
//                                DataState.itemList.removeAt(index)
//                                DataState.itemList.add(0, item)
//                            }
//                            roomAdapter.notifyDataSetChanged()
//                        } else {
//                            DataState.set.add(roomId)
//                            if(json.getString("profile") == null) {
//                                json.put("profile","")
//                            }
//                            DataState.itemList.add(
//                                0, RoomlistData(
//                                    json.getInt("roomId"),
//                                    json.getString("nickName"),
//                                    json.getString("content"),
//                                    "",
//                                    json.getInt("fromUserId"),
//                                    1,
//                                    json.optString("profile","")
//
//                                )
//                            )
//                            roomAdapter.notifyItemInserted(0)
//                        }
//                    }
//                }
//            }
//
//        var datax = JSONObject()
//        datax.put("userId", ApplicationClass.prefs.getId())
//        StompHelper.stompClient.send("/room_list", datax.toString()).subscribe()
        return rootView
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ChatlistFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RoomListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onNotify() {
        CoroutineScope(Dispatchers.Main).launch {
            roomAdapter.notifyDataSetChanged()
        }
    }
}