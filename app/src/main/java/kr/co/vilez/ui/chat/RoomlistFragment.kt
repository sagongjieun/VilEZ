package kr.co.vilez.ui.chat

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.vilez.R
import kr.co.vilez.data.model.RoomlistData
import kr.co.vilez.util.ApplicationClass
import kr.co.vilez.util.DataState
import kr.co.vilez.util.StompClient2
import org.json.JSONObject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ChatlistFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChatlistFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var mContext: Context
    private lateinit var rootView: View
    private lateinit var rv_room: RecyclerView
    private lateinit var topic: Disposable
    private var data: Int? = 0
    val roomAdapter = RoomAdapter(DataState.itemList)
    private var set = HashSet<Int>()
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onResume() {
        super.onResume()
        data = 0;
    }

    override fun onDestroy() {
        super.onDestroy()
        topic.dispose()
    }

    @SuppressLint("CheckResult")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_roomlist, container, false)
        rv_room = rootView.findViewById(R.id.rv_room) as RecyclerView

        rv_room.adapter = roomAdapter
        rv_room.layoutManager = LinearLayoutManager(requireContext())
        for (index in 0 until DataState.itemList.size) {
            val chat = DataState.itemList.get(index)
            set.add(chat.roomId)
        }

        roomAdapter.setItemClickListener(object : RoomAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                // 클릭 시 이벤트 작성
                var intent = Intent(mContext, ChatRoomActivity::class.java)
                intent.putExtra("roomId", DataState.itemList[position].roomId)
                intent.putExtra("otherUserId", DataState.itemList[position].otherUserId)
                intent.putExtra("nickName", DataState.itemList[position].nickName)
                intent.putExtra("profile", DataState.itemList[position].profile)
                DataState.itemList[position].noReadCnt = 0
                data = DataState.itemList[position].roomId
                startActivity(intent)
            }
        })
        roomAdapter.notifyDataSetChanged()
        topic = StompClient2.stompClient.join("/sendlist/" + ApplicationClass.prefs.getId())
            .subscribe { topicMessage ->
                run {
                    CoroutineScope(Dispatchers.Main).launch {
                        val json = JSONObject(topicMessage)
                        val roomId = json.getInt("roomId")
                        var index = -1;
                        if (roomId in DataState.set) {
                            for (i in 0 until DataState.itemList.size) {
                                if (DataState.itemList[i].roomId == roomId) {
                                    index = i
                                    break
                                }
                            }
                            if(data != roomId) {
                                DataState.itemList[index].noReadCnt++
                            } else {
                                DataState.itemList[index].noReadCnt = 0
                            }
                            DataState.itemList[index].content = json.getString("content")

                            roomAdapter.notifyItemChanged(index)

                            val item = DataState.itemList.get(index)
                            if (index != 0) {
                                DataState.itemList.removeAt(index)
                                DataState.itemList.add(0, item)
                            }
                            roomAdapter.notifyDataSetChanged()
                        } else {
                            DataState.set.add(roomId)

                            DataState.itemList.add(
                                0, RoomlistData(
                                    json.getInt("roomId"),
                                    json.getString("nickName"),
                                    json.getString("content"),
                                    json.getString("area"),
                                    json.getInt("fromUserId"),
                                    1,
                                    json.getString("profile")
                                )
                            )
                            roomAdapter.notifyItemInserted(0)
                        }
                    }
                }
            }
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
            ChatlistFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}