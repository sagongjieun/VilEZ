package kr.co.vilez.ui.chat

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
    private val itemList = ArrayList<RoomlistData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        //StompClient.stompClient.send("/room_list")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //StompClient.runStomp()
        val rootView = inflater.inflate(R.layout.fragment_roomlist, container, false)
        val rv_room = rootView.findViewById(R.id.rv_room) as RecyclerView


//        val data = JSONObject()
//        data.put("userId",29)
//        StompClient.stompClient.topic("/send_room_list/29").subscribe { topicMessage ->
//            run {
//
//            }
//        }

        val roomAdapter = RoomAdapter(itemList)


        rv_room.adapter = roomAdapter
        rv_room.layoutManager = LinearLayoutManager(requireContext())
        roomAdapter.setItemClickListener(object: RoomAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                // 클릭 시 이벤트 작성
                println(itemList.get(position).nickName)
            }
        })
        CoroutineScope(Dispatchers.Main).launch {
            val result = ApplicationClass.retrofitChatService.loadRoomList(28).awaitResponse().body()
            if (result?.flag == "success") {
                //val data = Gson().fromJson(result.data.toString(),ChatlistData::class.java)
                for (index in 0 until result.data.size){
                    val chat = result.data.get(index)
                    itemList.add(RoomlistData(chat.nickName,
                        chat.chatData.content,
                        chat.area))
                }
                roomAdapter.notifyDataSetChanged()
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