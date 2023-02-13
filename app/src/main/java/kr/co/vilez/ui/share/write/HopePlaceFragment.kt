package kr.co.vilez.ui.share.write

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsClient.getPackageName
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.contains
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import kr.co.vilez.R
import kr.co.vilez.databinding.FragmentHopePlaceBinding
import kr.co.vilez.ui.dialog.ConfirmDialog
import kr.co.vilez.ui.dialog.ConfirmDialogInterface
import kr.co.vilez.ui.dialog.MyAlertDialog
import kr.co.vilez.util.PermissionUtil
import kr.co.vilez.util.PermissionUtil.Companion.REQ_LOCATION
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapPoint.mapPointWithGeoCoord
import net.daum.mf.map.api.MapReverseGeoCoder
import net.daum.mf.map.api.MapView


private const val TAG = "빌리지_지도피커_HopePlaceFragment"

private const val ARG_LAT = "editLat"
private const val ARG_LNG = "editLng"

class HopePlaceFragment : Fragment(), MapView.MapViewEventListener,
    MapReverseGeoCoder.ReverseGeoCodingResultListener {
    lateinit var listener : HopePlaceInterface // 선언은 인터페이스로 (런타임때는 구현체가 동작함)
    private lateinit var binding:FragmentHopePlaceBinding
    private lateinit var activity:PlacePickerActivity

    private var isMarkerOn: Boolean? = false
    private lateinit var mapView: MapView
    private var zoom: Boolean? = false
    private var zoomLvl: Int? = 0
    private var marker = MapPOIItem()

    // 최종 선택한 위치 데이터
    private lateinit var pos: MapPoint
    private var addr:String? = null

    // 수정 모드일경우 디폴트로 띄울 마커위치
    private var editLat: Double ?=0.0
    private var editLng: Double?=0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            editLat = it.getDouble(ARG_LAT)
            editLng = it.getDouble(ARG_LNG)
        }
        activity = context as PlacePickerActivity
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: Double, param2: Double) =
            HopePlaceFragment().apply {
                arguments = Bundle().apply {
                    putDouble(ARG_LAT, param1)
                    putDouble(ARG_LNG, param2)
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_hope_place, container, false)
        binding.fragment = this
        initMap()
        return binding.root
    }

    override fun onDetach() {
        Log.d(TAG, "onDetach: 맵 종료")
        super.onDetach()
        if(binding.flMap.contains(mapView)) {
            mapView.currentLocationTrackingMode = null
            binding.flMap.removeView(mapView)
        }
    }

    fun onConfirmClicked(view: View) {
        if(addr == null) {
            Toast.makeText(activity, "희망 공유 장소를 선택해서 마커를 찍어주세요.", Toast.LENGTH_SHORT).show()
        } else { // 선택한 주소 담아서 게시글 작성 액티비티로 보내기
            listener.setAddress(addr!!)
            //Snackbar.make(view, "선택한 주소: ${addr}", Snackbar.LENGTH_SHORT).show()

            val lat = pos.mapPointGeoCoord.latitude
            val lng = pos.mapPointGeoCoord.longitude

            val intent = Intent()
            intent.putExtra("address", addr)
            intent.putExtra("lat", lat.toString())
            intent.putExtra("lng", lng.toString())
            activity.setResult(AppCompatActivity.RESULT_OK, intent)
            activity.finish()
        }
    }



    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        val lm = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val userNowLocation: Location? = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

        // 현재 위치를 가져올 수 있음
        //위도 , 경도
        val uLatitude = userNowLocation?.latitude
        val uLongitude = userNowLocation?.longitude
        Log.d(TAG, "initMap: 위도: $uLatitude 경도: $uLongitude")
        pos = MapPoint.mapPointWithGeoCoord(uLatitude!!, uLongitude!!)


        mapView.apply {
            setMapCenterPointAndZoomLevel(pos, 3, true) // 중심점 변경 + 줌 레벨 변경
            currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading
            zoomIn(true) // 줌 인
            zoomOut(true)  // 줌 아웃
        }
    }


    private fun initMap() {
        mapView = MapView(activity)
        binding.flMap.addView(mapView)
        mapView.setMapViewEventListener(this)

        mapView.setShowCurrentLocationMarker(true)

        // 위치 권한 가져오기
        if(PermissionUtil().checkLocationPermission(activity)) {
            getCurrentLocation()
        } else {
            PermissionUtil().requestLocationPermissions(activity)
        }
    }


    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ_LOCATION) {
            if (grantResults.isEmpty()) {
                // 권한 창 떴을 때 그냥 끈 경우
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation()
            } else {
                // Permission denied.
                for (permission in permissions) {
                    if ("android.permission.ACCESS_FINE_LOCATION" == permission) {
                        val dialog = ConfirmDialog(object:ConfirmDialogInterface {
                            override fun onYesButtonClick(id: String) {
                                val intent = Intent()
                                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                                intent.data = Uri.fromParts("package", activity.packageName, null)
                                startActivity(intent)
                            }
                        }, "동네 설정을 위해 위치 접근을 권한이 필요합니다.\n휴대폰 [설정]-[애플리케이션]-[vilez]에서 위치 권한을 켜주세요.", "")
                        dialog.show(activity.supportFragmentManager, "LocationPermission")
                    }
                }
            }
        }
    }

    override fun onMapViewInitialized(p0: MapView?) {
        p0?.removeAllPOIItems()
        if(editLat!=null && editLng != null) { // 기존 위치에 마커 찍기
            Log.d(TAG, "onMapViewInitialized: 수정모드 lat:$editLat, lng:$editLng")
            pos = mapPointWithGeoCoord(editLat!!, editLng!!)

            val reverseGeoCoder = MapReverseGeoCoder(
                getString(R.string.kakao_app_key),
                pos,
                this,
                activity,
            )
            reverseGeoCoder.startFindingAddress()

            marker.itemName = "hope area"
            marker.tag = 0
            marker.mapPoint = pos;
            marker.markerType = MapPOIItem.MarkerType.BluePin // 기본으로 제공하는 BluePin 마커 모양.*/
            marker.selectedMarkerType = MapPOIItem.MarkerType.RedPin // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.

            if (p0 != null && pos != null) {
                p0.removeAllPOIItems()
                p0.addPOIItem(marker) // 마커찍기
                isMarkerOn = true;
                p0.setMapCenterPoint(pos,true) // 마커 중심으로 위치 이동
            }

        }
    }

    override fun onMapViewCenterPointMoved(p0: MapView?, p1: MapPoint?) {

    }

    override fun onMapViewZoomLevelChanged(p0: MapView?, p1: Int) {

    }

    override fun onMapViewSingleTapped(p0: MapView?, p1: MapPoint?) {
        if(p0 != null && isMarkerOn == true) p0.removePOIItem(marker)

        if (p1 != null) {
            pos = p1
        }

        val reverseGeoCoder = MapReverseGeoCoder(
            getString(R.string.kakao_app_key),
            p1,
            this,
            activity,
        )
        reverseGeoCoder.startFindingAddress()

        marker.itemName = "hope area"
        marker.tag = 0
        marker.mapPoint = p1;
        marker.markerType = MapPOIItem.MarkerType.BluePin // 기본으로 제공하는 BluePin 마커 모양.*/
        marker.selectedMarkerType = MapPOIItem.MarkerType.RedPin // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.

        if (p0 != null && p1 != null) {
            p0.removeAllPOIItems()
            p0.addPOIItem(marker) // 마커찍기
            isMarkerOn = true;
            p0.setMapCenterPoint(p1,true) // 마커 중심으로 위치 이동
        }

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
        if(p1 != null) { // 액티비티에 전달할 주소
            listener.setAddress(p1)
            addr = p1
        }

        marker = MapPOIItem()
        marker.apply {
            itemName = p1
            tag = 0
            mapPoint = pos
            markerType = MapPOIItem.MarkerType.BluePin // 기본으로 제공하는 BluePin 마커 모양.
            selectedMarkerType =
                MapPOIItem.MarkerType.RedPin // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
        }
        mapView.addPOIItem(marker)
        Log.d(TAG, "onReverseGeoCoderFoundAddress: 주소변환 성공: $p1")
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
        Log.d(TAG, "onReverseGeoCoderFailedToFindAddress: 주소변환실패")
        //Toast.makeText(activity, "현재 위치에 해당하는 주소를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
    }

}