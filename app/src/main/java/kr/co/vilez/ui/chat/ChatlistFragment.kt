package kr.co.vilez.ui.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.vilez.R
import kr.co.vilez.data.model.ChatList
import kr.co.vilez.data.model.ChatMsg
import kr.co.vilez.databinding.ChatlistItemBinding
import kr.co.vilez.ui.MainActivity
import kr.co.vilez.util.ApplicationClass
import kr.co.vilez.util.StompClient
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
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
    private val itemList = ArrayList<ChatlistData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        CoroutineScope(Dispatchers.Main).launch {
            val result = ApplicationClass.retrofitChatService.loadRoomList(29).awaitResponse().body()
            if (result?.flag == "success") {
                //val data = Gson().fromJson(result.data.toString(),ChatlistData::class.java)
           // println(result.data)
//                for (index in 0 until data.size){
//                    println(result.data?.get(0))
//                    val chat = Gson().fromJson(result.data.get(index).toString(),ChatList::class.java)
//                    itemList.add(ChatlistData(chat.nickName,
//                        chat.chatData.content,
//                        chat.area))
//                }

            }
        }
        //StompClient.stompClient.send("/room_list")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //StompClient.runStomp()
        val rootView = inflater.inflate(R.layout.fragment_chatlist, container, false)
        val rv_chat = rootView.findViewById(R.id.rv_chat) as RecyclerView


//        val data = JSONObject()
//        data.put("userId",29)
//        StompClient.stompClient.topic("/send_room_list/29").subscribe { topicMessage ->
//            run {
//
//            }
//        }

        val chatAdapter = ChatAdapter(itemList)
        chatAdapter.notifyDataSetChanged()
        rv_chat.adapter = chatAdapter
        rv_chat.layoutManager = LinearLayoutManager(requireContext())
//        StompClient.stompClient.send("/room_list",data.toString()).subscribe()
//        itemList.add(ChatlistData("타이어 빌려드립니다.","즈기요 계시나요","구미 진평동"))
//        itemList.add(ChatlistData("연필좀 빌려주세요ㅠㅠ","안녕하세요","구미 진평동"))
//        itemList.add(ChatlistData("공책 빌리실분?","이거 빌릴래요","구미 진평동"))

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