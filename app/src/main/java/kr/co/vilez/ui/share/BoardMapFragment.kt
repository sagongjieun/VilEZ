package kr.co.vilez.ui.share

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.contains
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import kr.co.vilez.R
import kr.co.vilez.databinding.FragmentBoardMapBinding
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapReverseGeoCoder
import net.daum.mf.map.api.MapReverseGeoCoder.ReverseGeoCodingResultListener
import net.daum.mf.map.api.MapView
import org.json.JSONObject


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val MAP_LAT = "param1"
private const val MAP_LNG = "param2"

private const val TAG = "빌리지_BoardMapFragment"
class BoardMapFragment : Fragment(), MapView.MapViewEventListener, ReverseGeoCodingResultListener {
    // TODO: Rename and change types of parameters
    private var param1: Double? = null
    private var param2: Double? = null

    private lateinit var binding:FragmentBoardMapBinding
    private lateinit var activity: ShareDetailActivity

    private lateinit var mapView:MapView
    private var zoom: Boolean? = false
    private var zoomLvl: Int? = 0
    private val marker = MapPOIItem()

    private lateinit var pos:MapPoint
    private lateinit var addr:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 =
                it.getDouble(MAP_LAT)
            param2 = it.getDouble(MAP_LNG)
        }
        activity = context as ShareDetailActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_board_map, container, false)
        binding.fragment = this

        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 latitude
         * @param param2 longitude
         * @return A new instance of fragment BoardMapFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: Double, param2: Double) =
            BoardMapFragment().apply {
                arguments = Bundle().apply {
                    putDouble(MAP_LAT, param1)
                    putDouble(MAP_LNG, param2)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMap()
    }

    private fun initMap() {
        mapView = MapView(activity)
        binding.flMap.addView(mapView)
        mapView.setMapViewEventListener(this)
        mapView.setShowCurrentLocationMarker(false)

        Log.d(TAG, "initMap: lat: $param1, lng: $param2")
        pos = MapPoint.mapPointWithGeoCoord(param1!!, param2!!)

        val reverseGeoCoder = MapReverseGeoCoder(
            getString(R.string.kakao_app_key),
            pos,
            this,
            activity
        )
        reverseGeoCoder.startFindingAddress()


        mapView.apply {
            setMapCenterPointAndZoomLevel(pos, 3, true) // 중심점 변경 + 줌 레벨 변경
            zoomIn(true) // 줌 인
            zoomOut(true)  // 줌 아웃
        }


    }


    override fun onResume() {
        super.onResume()
//        if(binding.flMap.contains(mapView)) {
//            try {
//                binding.flMap.removeView(mapView)
//                initMap()
//            } catch (e: java.lang.RuntimeException) {
//                Log.d(TAG, "onRestart: 맵뷰 초기화 에러 $e")
//            }
//        }
    }

    override fun onDetach() {
        Log.d(TAG, "onDetach: 맵 종료")
        super.onDetach()
        if(binding.flMap.contains(mapView))
            binding.flMap.removeView(mapView)
    }


    override fun onMapViewInitialized(p0: MapView?) {

    }

    override fun onMapViewCenterPointMoved(p0: MapView?, p1: MapPoint?) {

    }

    override fun onMapViewZoomLevelChanged(p0: MapView?, p1: Int) {
        if(zoom == true) {
            zoom = false
            return
        }
        if (p0 != null) {
            if(p0.zoomLevel == zoomLvl) return
            zoomLvl = p0.zoomLevel
        }
    }

    override fun onMapViewSingleTapped(p0: MapView?, p1: MapPoint?) {
        Log.d(TAG, "onMapViewSingleTapped: p1: ${p1!!.mapPointGeoCoord.latitude} ${p1!!.mapPointGeoCoord.longitude}")
    }

    override fun onMapViewDoubleTapped(p0: MapView?, p1: MapPoint?) {
    }

    override fun onMapViewLongPressed(p0: MapView?, p1: MapPoint?) {
    }

    override fun onMapViewDragStarted(p0: MapView?, p1: MapPoint?) {
    }

    override fun onMapViewDragEnded(p0: MapView?, p1: MapPoint?) {
    }

    override fun onMapViewMoveFinished(p0: MapView?, p1: MapPoint?) {
    }

    override fun onReverseGeoCoderFoundAddress(p0: MapReverseGeoCoder?, p1: String?) {
        if (p1 != null) {
            addr = p1
        }
        val marker = MapPOIItem()
        marker.apply {
            itemName = p1
            tag = 0
            mapPoint = pos
            markerType = MapPOIItem.MarkerType.BluePin // 기본으로 제공하는 BluePin 마커 모양.
            selectedMarkerType =
                MapPOIItem.MarkerType.RedPin // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
        }
        mapView.addPOIItem(marker)
    }

    override fun onReverseGeoCoderFailedToFindAddress(p0: MapReverseGeoCoder?) {
        val marker = MapPOIItem()
        marker.apply {
            itemName = "불러올 수 없음"
            tag = 0
            mapPoint = pos
            markerType = MapPOIItem.MarkerType.BluePin // 기본으로 제공하는 BluePin 마커 모양.
            selectedMarkerType =
                MapPOIItem.MarkerType.RedPin // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
        }
        mapView.addPOIItem(marker)
    }


}