package kr.co.vilez.ui.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.vilez.R
import kr.co.vilez.databinding.ActivityChatRoomBinding
import kr.co.vilez.databinding.ActivityMainBinding
import kr.co.vilez.util.ApplicationClass
import kr.co.vilez.util.StompClient
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import org.json.JSONObject
import retrofit2.awaitResponse

class ChatRoomActivity : AppCompatActivity(),MapView.MapViewEventListener {
    private lateinit var binding: ActivityChatRoomBinding
    private var zoom: Boolean? = false
    private var isMarkerOn: Boolean? = false
    private var markertouch: Boolean? = false
    private var zoomLvl: Int? = 0
    private val marker = MapPOIItem()
    private val itemList = ArrayList<ChatlistData>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat_room)


        initView()
    }

    fun initView() {
        val rv_chat = binding.root.findViewById(R.id.rv_chat) as RecyclerView
        val txt_edit = binding.root.findViewById(R.id.editText1) as EditText
        val roomAdapter = ChatAdapter(itemList)
        txt_edit.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                val data = JSONObject()
                data.put("roomId",10)
                data.put("boardId",55)
                data.put("type",2)
                data.put("fromUserId",29)
                data.put("toUserId",28)
                data.put("content",txt_edit.text)
                data.put("time",System.currentTimeMillis())
                StompClient.stompClient.send("/recvchat", data.toString()).subscribe()
                txt_edit.setText("")
                true
            }
           false
        }

        rv_chat.adapter = roomAdapter
        rv_chat.layoutManager = LinearLayoutManager(this)
        val mapView = MapView(this)
        binding.mapView.addView(mapView)
        CoroutineScope(Dispatchers.Main).launch {
            val result = ApplicationClass.retrofitChatService.loadLocationByRoomId(10).awaitResponse().body()
            if (result?.flag == "success") {
                println(result.data)
                var kakao = result.data[0];
                var pos = MapPoint.mapPointWithGeoCoord(kakao.lat,kakao.lng)
                mapView.setMapCenterPoint(pos,true)
                mapView.setZoomLevel(kakao.zoomLevel,true)
                if(kakao.isMarker){
                    isMarkerOn = true
                    marker.itemName = "hope area"
                    marker.tag = 0
                    marker.mapPoint = pos;
                    marker.markerType = MapPOIItem.MarkerType.BluePin // 기본으로 제공하는 BluePin 마커 모양.

                    marker.selectedMarkerType =
                        MapPOIItem.MarkerType.RedPin // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
                    mapView.addPOIItem(marker)
                    markertouch = true
                }
                zoom = true
            }
        }
//
        StompClient.stompClient.topic("/sendmap/10/29").subscribe { topicMessage ->
            run {
                val json = JSONObject(topicMessage.payload)
                mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(json.getDouble("lat"),json.getDouble("lng")),true);
                if(mapView.zoomLevel != json.getInt("zoomLevel")) {
                    zoom = true;
                    mapView.setZoomLevel(json.getInt("zoomLevel"),true);
                }
                if(json.getBoolean("isMarker")) {
                    if(isMarkerOn == true) {
                        if (mapView != null) {
                            mapView.removePOIItem(marker)
                        }
                        isMarkerOn = false
                    }
                    marker.itemName = "hope area"
                    marker.tag = 0
                    marker.mapPoint = mapView.mapCenterPoint;
                    marker.markerType = MapPOIItem.MarkerType.BluePin // 기본으로 제공하는 BluePin 마커 모양.

                    marker.selectedMarkerType =
                        MapPOIItem.MarkerType.RedPin // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
                }
            }
        }
        StompClient.stompClient.topic("/sendmy/10/29").subscribe { topicMessage ->
            run {
                CoroutineScope(Dispatchers.Main).launch {
                    val json = JSONObject(topicMessage.payload)
                    itemList.add(ChatlistData(json.getString("content"), 2))
                    roomAdapter.notifyDataSetChanged()
                }
            }
        }
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