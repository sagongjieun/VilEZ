package kr.co.vilez.ui.profile

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import coil.size.Dimension
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.vilez.R
import kr.co.vilez.data.model.User
import kr.co.vilez.databinding.FragmentEditAreaBinding
import kr.co.vilez.ui.dialog.AlertDialog
import kr.co.vilez.ui.user.ProfileMenuActivity
import kr.co.vilez.util.ApplicationClass
import kr.co.vilez.util.Common
import kr.co.vilez.util.PermissionUtil
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapReverseGeoCoder
import net.daum.mf.map.api.MapView
import retrofit2.awaitResponse
import java.util.regex.Pattern

private const val TAG = "빌리지_EditAreaFragment"
class EditAreaFragment : Fragment(), MapReverseGeoCoder.ReverseGeoCodingResultListener {
    private lateinit var binding : FragmentEditAreaBinding
    private lateinit var profileMenuActivity: ProfileMenuActivity


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
        profileMenuActivity = context as ProfileMenuActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_area, container, false)
        binding.fragment = this
        initToolBar()
        getUserLocation()

        return binding.root
    }

    private fun initToolBar() {
        profileMenuActivity.setSupportActionBar(binding.toolbar)
        profileMenuActivity.supportActionBar?.setDisplayShowTitleEnabled(false) // 기본 타이틀 제거
        binding.title = "내 동네 설정"
    }

    fun onBackPressed(view: View) {
        profileMenuActivity.finish()
    }

    fun getUserLocation() {
        val userLat = ApplicationClass.prefs.getLat().toDouble()
        val userLng = ApplicationClass.prefs.getLng().toDouble()

        if(userLat == 0.0 && userLng == 0.0) {
            binding.location = null
            return
        }
        val pos = MapPoint.mapPointWithGeoCoord(userLat, userLng)
        val reverseGeoCoder = MapReverseGeoCoder(
            getString(R.string.kakao_app_key),
            pos,
            this,
            profileMenuActivity
        )
        reverseGeoCoder.startFindingAddress()
    }

    fun onAreaSetClick(view: View) {
        val locationFinePermission = ContextCompat.checkSelfPermission(
            profileMenuActivity,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        val locationCoarsePermission = ActivityCompat.checkSelfPermission(
            profileMenuActivity,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        if(locationFinePermission == PackageManager.PERMISSION_DENIED || locationCoarsePermission == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(
                context as Activity, arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), PermissionUtil().REQ_LOCATION
            )
        }
        else {
            val lm = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val userNowLocation: Location? =
                lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

            // 현재 위치를 가져올 수 있음
            //위도 , 경도
            val uLatitude = userNowLocation?.latitude
            val uLongitude = userNowLocation?.longitude
            ApplicationClass.prefs.setLat(uLatitude.toString())
            ApplicationClass.prefs.setLng(uLongitude.toString())
            Log.d(TAG, "initMap: 위도: $uLatitude 경도: $uLongitude")

            // 가져온 정보 다시 뿌리기
            val pos = MapPoint.mapPointWithGeoCoord(uLatitude!!, uLongitude!!)
            val reverseGeoCoder = MapReverseGeoCoder(
                getString(R.string.kakao_app_key),
                pos,
                this,
                profileMenuActivity
            )
            reverseGeoCoder.startFindingAddress()

            // DB도 업데이트
            CoroutineScope(Dispatchers.Main).launch {
                val newUser = User(id = ApplicationClass.prefs.getId(), areaLat = uLatitude.toString(), areaLng = uLongitude.toString())
                val result = ApplicationClass.retrofitUserService.updateUserLocation(newUser).awaitResponse().body()
                Log.d(TAG, "onAreaSetClick: result: $result")
                if(result?.flag == "success") {
                    Log.d(TAG, "onAreaSetClick: 동네인증성공")
                    var dialog = AlertDialog(profileMenuActivity, "동네인증을 성공했습니다.")
                    dialog.show(profileMenuActivity.supportFragmentManager, "UserLocation")
                } else {
                    Log.d(TAG, "onAreaSetClick: 동네 인증 실패")
                    var dialog = AlertDialog(profileMenuActivity, "동네 인증을 실패했습니다.\n위치 정보를 가져올 수 있는 곳에서 다시 시도해주세요.")
                    dialog.show(profileMenuActivity.supportFragmentManager, "UserLocation")
                }
            }
        }

    }

    override fun onReverseGeoCoderFoundAddress(p0: MapReverseGeoCoder?, p1: String?) {
        /** 숫자 필터링 */
        var result = ""
        var data = p1!!.split(" ")
        Log.d(TAG, "onReverseGeoCoderFoundAddress: data: $data")
        for (i in 0 .. data.size-1 step(1)) {
            var tmp = data[i]
            Log.d(TAG, "onReverseGeoCoderFoundAddress: tmp:$tmp")
            if(tmp.contains(Regex("[0-9]"))) {
                break;
            } else {
                result += (tmp + " ")
            }
        }
        Log.d(TAG, "onReverseGeoCoderFoundAddress: 필터링된 동네 : $result")
        binding.location = result
    }

    override fun onReverseGeoCoderFailedToFindAddress(p0: MapReverseGeoCoder?) {
        binding.location = null
    }




}