package kr.co.vilez.ui.chat.map

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.contains
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.vilez.databinding.FragmentKakaoMapBinding
import kr.co.vilez.util.ApplicationClass
import kr.co.vilez.util.StompHelper
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import org.json.JSONObject
import retrofit2.awaitResponse

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class KakaoMapFragment : Fragment(), MapView.MapViewEventListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var zoom: Boolean? = false
    private var isMarkerOn: Boolean? = false
    private var markertouch: Boolean? = false
    private var zoomLvl: Int? = 0
    private val marker = MapPOIItem()
    private var otherUserId  = 0
    private var topic: Disposable? = null
    private var roomId: Int = 0
    private lateinit var mapView :MapView
    private var binding: FragmentKakaoMapBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentKakaoMapBinding.inflate(inflater, container, false)
        context ?: return binding!!.root
        var bundle = arguments as Bundle
        roomId = bundle.getInt("roomId")
        otherUserId = bundle.getInt("otherUserId")
        mapView = MapView(context)
        binding!!.mapView.addView(mapView)
        subMap(mapView)
        mapView.setMapViewEventListener(this)
        return binding!!.root
    }

    override fun onDestroy() {
        super.onDestroy()
        if(binding!!.mapView.contains(mapView))
            binding!!.mapView.removeView(mapView)
        topic?.dispose()
    }

    fun subMap(mapView : MapView){
        CoroutineScope(Dispatchers.Main).launch {
            val result = ApplicationClass.chatApi.loadLocationByRoomId(roomId).awaitResponse().body()
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
                    markertouch = true
                    mapView.addPOIItem(marker)
                }
                zoom = true
            }
        }

        topic = StompHelper.stompClient.join("/sendmap/"+roomId+"/"+ApplicationClass.prefs.getId()).subscribe { topicMessage ->
            run {
                val json = JSONObject(topicMessage)
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
                    markertouch = true
                    isMarkerOn = true
                    marker.itemName = "hope area"
                    marker.tag = 0
                    marker.mapPoint = MapPoint.mapPointWithGeoCoord(json.getDouble("lat"),json.getDouble("lng"))
                    marker.markerType = MapPOIItem.MarkerType.BluePin // 기본으로 제공하는 BluePin 마커 모양.

                    marker.selectedMarkerType =
                        MapPOIItem.MarkerType.RedPin // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
                    mapView.addPOIItem(marker)
                }
                mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(json.getDouble("lat"),json.getDouble("lng")),true);
            }
        }
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment KakaoMapFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            KakaoMapFragment().apply {
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
            data.put("roomId", roomId)
            data.put("toUserId", otherUserId)
            data.put("lat", p0.getMapCenterPoint().mapPointGeoCoord.latitude)
            data.put("lng", p0.getMapCenterPoint().mapPointGeoCoord.longitude)
            data.put("zoomLevel", p0.zoomLevel)
            data.put("isMarker",false)
            zoomLvl = p0.zoomLevel
            StompHelper.stompClient.send("/recvmap", data.toString()).subscribe()
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
            isMarkerOn = true;
            data.put("roomId", roomId)
            data.put("toUserId", otherUserId)
            println(otherUserId)
            data.put("lat", p1.mapPointGeoCoord.latitude)
            data.put("lng", p1.mapPointGeoCoord.longitude)
            data.put("isMarker",true)
            data.put("zoomLevel", p0.zoomLevel)
            StompHelper.stompClient.send("/recvmap", data.toString()).subscribe()
            p0.setMapCenterPoint(p1,true)
        }
        p0!!.addPOIItem(marker)

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
            data.put("roomId", roomId)
            data.put("toUserId", otherUserId)
            data.put("lat", p0.getMapCenterPoint().mapPointGeoCoord.latitude)
            data.put("lng", p0.getMapCenterPoint().mapPointGeoCoord.longitude)
            data.put("zoomLevel", p0.zoomLevel)
            data.put("isMarker",false)
        }

        StompHelper.stompClient.send("/recvmap", data.toString()).subscribe()
    }

    override fun onMapViewMoveFinished(p0: MapView?, p1: MapPoint?) {

    }
}