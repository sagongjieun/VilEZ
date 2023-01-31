package kr.co.vilez.ui.chat

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.KeyEvent.KEYCODE_ENTER
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.vilez.R
import kr.co.vilez.data.model.KakaoMap
import kr.co.vilez.data.model.User
import kr.co.vilez.databinding.FragmentChatBinding
import kr.co.vilez.util.ApplicationClass
import kr.co.vilez.util.ApplicationClass.Companion.retrofitChatService
import kr.co.vilez.util.DataState
import kr.co.vilez.util.StompClient
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.awaitResponse


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ChatFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChatFragment : Fragment(), MapView.MapViewEventListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var zoom: Boolean? = false
    private var isMarkerOn: Boolean? = false
    private var markertouch: Boolean? = false
    private var zoomLvl: Int? = 0
    private val marker = MapPOIItem()
    private val itemList = ArrayList<ChatlistData>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onResume() {
        super.onResume()
        StompClient.stompClient.topic("/send_room_list/"+ ApplicationClass.prefs.getId()).subscribe { topicMessage ->
            run {
                val json = JSONArray(topicMessage.payload)
                CoroutineScope(Dispatchers.Main).launch {

                    DataState.itemList = ArrayList<RoomlistData>()
                    for (index in 0 until json.length()) {
                        val chat = JSONObject(json.get(index).toString())
                        val chatData = chat.getJSONObject("chatData")
                        DataState.itemList.add(
                            RoomlistData(
                                chatData.getInt("roomId"), chat.getString("nickName"),
                                chatData.getString("content"),
                                chat.getString("area"),
                                if(chatData.getInt("fromUserId") == ApplicationClass.prefs.getId())
                                    chatData.getInt("toUserId")
                                else
                                    chatData.getInt("fromUserId")

                            )
                        )
                        DataState.set.add(chatData.getInt("roomId"))
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
        val binding = FragmentChatBinding.inflate(inflater, container, false)
        context ?: return binding.root
        val rv_chat = binding.root.findViewById(R.id.rv_chat) as RecyclerView
        val txt_edit = binding.root.findViewById(R.id.editText1) as EditText
        val roomAdapter = ChatAdapter(itemList)
        txt_edit.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KEYCODE_ENTER) {
                val data = JSONObject()
                data.put("roomId",10)
                data.put("boardId",55)
                data.put("type",2)
                data.put("fromUserId",29)
                data.put("toUserId",28)
                data.put("content",txt_edit.text)
                data.put("time",System.currentTimeMillis())
                //StompClient.stompClient.send("/recvchat", data.toString()).subscribe()
            }
            true
        }

        rv_chat.adapter = roomAdapter
        rv_chat.layoutManager = LinearLayoutManager(requireContext())
        val mapView = MapView(context)
        binding.mapView.addView(mapView)

//        StompClient.stompClient.topic("/sendmy/10/29").subscribe { topicMessage ->
//            run {
//                CoroutineScope(Dispatchers.Main).launch {
//                    val json = JSONObject(topicMessage.payload)
//                    itemList.add(ChatlistData(json.getString("content"), 2))
//                    roomAdapter.notifyDataSetChanged()
//                }
//            }
//        }
        StompClient.stompClient.topic("/sendchat/10/29").subscribe { topicMessage ->
            run {
                CoroutineScope(Dispatchers.Main).launch {
                    val json = JSONObject(topicMessage.payload)
                    itemList.add(ChatlistData(json.getString("content"), 1))
                    roomAdapter.notifyDataSetChanged()
                }
            }
        }

//
        mapView.setMapViewEventListener(this)
        return binding.root
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_chat, container, false)
    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ChatFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ChatFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onMapViewInitialized(p0: MapView?) {

    }

    override fun onMapViewCenterPointMoved(p0: MapView?, p1: MapPoint?) {

    }

    override fun onMapViewZoomLevelChanged(p0: MapView?, p1: Int) {
        val data = JSONObject()
        if(zoom == true) {
            zoom = false
            return
        }
        if (p0 != null) {
            if(p0.zoomLevel == zoomLvl) return
            data.put("roomId", 10)
            data.put("toUserId", 29)
            data.put("lat", p0.getMapCenterPoint().mapPointGeoCoord.latitude)
            data.put("lng", p0.getMapCenterPoint().mapPointGeoCoord.longitude)
            data.put("zoomLevel", p0.zoomLevel)
            data.put("isMarker",false)
            zoomLvl = p0.zoomLevel
            StompClient.stompClient.send("/recvmap", data.toString()).subscribe()
        }
    }

    override fun onMapViewSingleTapped(p0: MapView?, p1: MapPoint?) {

    }

    override fun onMapViewDoubleTapped(p0: MapView?, p1: MapPoint?) {

    }

    override fun onMapViewLongPressed(p0: MapView?, p1: MapPoint?) {
        if(isMarkerOn == true) {
            if (p0 != null) {
                p0.removePOIItem(marker)
            }
            isMarkerOn = false
        }
        marker.itemName = "hope area"
        marker.tag = 0
        marker.mapPoint = p1;
        marker.markerType = MapPOIItem.MarkerType.BluePin // 기본으로 제공하는 BluePin 마커 모양.

        marker.selectedMarkerType =
            MapPOIItem.MarkerType.RedPin // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.

        markertouch = true
        val data = JSONObject()
        if (p0 != null && p1 != null) {
            p0.addPOIItem(marker)
            isMarkerOn = true;
            data.put("roomId", 10)
            data.put("toUserId", 29)
            data.put("lat", p1.mapPointGeoCoord.latitude)
            data.put("lng", p1.mapPointGeoCoord.longitude)
            data.put("isMarker",true)
            data.put("zoomLevel", p0.zoomLevel)
            StompClient.stompClient.send("/recvmap", data.toString()).subscribe()
            p0.setMapCenterPoint(p1,true)
        }


    }

    override fun onMapViewDragStarted(p0: MapView?, p1: MapPoint?) {

    }

    override fun onMapViewDragEnded(p0: MapView?, p1: MapPoint?) {
        val data = JSONObject()
        if(markertouch == true) {
            markertouch = false
            return
        }
        if (p0 != null) {
            data.put("roomId", 10)
            data.put("toUserId", 29)
            data.put("lat", p0.getMapCenterPoint().mapPointGeoCoord.latitude)
            data.put("lng", p0.getMapCenterPoint().mapPointGeoCoord.longitude)
            data.put("zoomLevel", p0.zoomLevel)
            data.put("isMarker",false)
        }

        StompClient.stompClient.send("/recvmap", data.toString()).subscribe()
    }

    override fun onMapViewMoveFinished(p0: MapView?, p1: MapPoint?) {

    }


}