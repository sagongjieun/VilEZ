package kr.co.vilez.ui.chat

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kr.co.vilez.databinding.FragmentChatBinding
import kr.co.vilez.util.StompClient
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import org.json.JSONObject


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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    @SuppressLint("CheckResult")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentChatBinding.inflate(inflater, container, false)
        context ?: return binding.root
        StompClient.runStomp()
        val mapView = MapView(context)
        binding.mapView.addView(mapView)
        StompClient.stompClient.topic("/sendmap/200/2").subscribe { topicMessage ->
            run {
                println("수신됨")
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
            data.put("roomId", "200")
            data.put("type", "2")
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
            data.put("roomId", "200")
            data.put("type", "2")
            data.put("lat", p1.mapPointGeoCoord.latitude)
            data.put("lng", p1.mapPointGeoCoord.longitude)
            data.put("isMarker",true)
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
            data.put("roomId", "200")
            data.put("type", "2")
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