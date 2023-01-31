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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.vilez.R
import kr.co.vilez.util.ApplicationClass
import kr.co.vilez.util.DataState
import kr.co.vilez.util.StompClient
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.awaitResponse

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
    private lateinit var rootView : View
    private lateinit var rv_room : RecyclerView


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
        StompClient.stompClient.topic("/sendlist/"+ApplicationClass.prefs.getId()).subscribe { topicMessage ->
            run {
                CoroutineScope(Dispatchers.Main).launch {
                    val json = JSONObject(topicMessage.payload)
                    println(json.toString())
                    val roomId = json.getInt("roomId")
                    var index = -1;
                    if (roomId in DataState.set) {
                        for (i in 0 until DataState.itemList.size) {
                            if (DataState.itemList[i].roomId == roomId) {
                                index = i
                                break
                            }
                        }

                        DataState.itemList[index].content = json.getString("content")

                        val item = DataState.itemList.get(index)
                        if(index != 0 ) {
                            DataState.itemList.removeAt(index)
                            DataState.itemList.add(0, item)
                            roomAdapter.notifyItemMoved(index, 0)
                        }
                        roomAdapter.notifyItemChanged(0)
                    } else {
                        DataState.set.add(roomId)

                        DataState.itemList.add(
                            0, RoomlistData(
                                json.getInt("roomId"),
                                json.getString("nickName"),
                                json.getString("content"),
                                json.getString("area"),
                                json.getInt("fromUserId")
                            )
                        )
                        roomAdapter.notifyItemInserted(0)
                    }
                }
            }
        }
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
        println("dddd")

        roomAdapter.setItemClickListener(object : RoomAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                // 클릭 시 이벤트 작성
                var intent = Intent(mContext,ChatRoomActivity::class.java)
                intent.putExtra("roomId",DataState.itemList[position].roomId)
                intent.putExtra("otherUserId",DataState.itemList[position].otherUserId)
                startActivity(intent)
            }
        })
        roomAdapter.notifyDataSetChanged()
        StompClient.stompClient.topic("/sendlist/"+ApplicationClass.prefs.getId()).subscribe { topicMessage ->
            run {
                CoroutineScope(Dispatchers.Main).launch {
                    val json = JSONObject(topicMessage.payload)
                    println(json.toString())
                    val roomId = json.getInt("roomId")
                    var index = -1;
                    if (roomId in DataState.set) {
                        for (i in 0 until DataState.itemList.size) {
                            if (DataState.itemList[i].roomId == roomId) {
                                index = i
                                break
                            }
                        }

                        DataState.itemList[index].content = json.getString("content")

                        val item = DataState.itemList.get(index)
                        if(index != 0 ) {
                            DataState.itemList.removeAt(index)
                            DataState.itemList.add(0, item)
                            roomAdapter.notifyItemMoved(index, 0)
                        }
                        roomAdapter.notifyItemChanged(0)
                    } else {
                        DataState.set.add(roomId)

                        DataState.itemList.add(
                            0, RoomlistData(
                                json.getInt("roomId"),
                                json.getString("nickName"),
                                json.getString("content"),
                                json.getString("area"),
                                json.getInt("fromUserId")
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