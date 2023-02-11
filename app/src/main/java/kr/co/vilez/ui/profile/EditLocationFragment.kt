package kr.co.vilez.ui.profile

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.vilez.R
import kr.co.vilez.databinding.FragmentEditLocationBinding
import kr.co.vilez.ui.MainActivity
import kr.co.vilez.ui.dialog.*
import kr.co.vilez.ui.user.ProfileMenuActivity
import kr.co.vilez.util.ApplicationClass
import kr.co.vilez.util.PermissionUtil
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapReverseGeoCoder
import kr.co.vilez.util.PermissionUtil.Companion.REQ_LOCATION
import net.daum.mf.map.api.MapView
import retrofit2.awaitResponse

private const val TAG = "빌리지_EditAreaFragment"

class EditLocation : Fragment(), MapReverseGeoCoder.ReverseGeoCodingResultListener {
    private lateinit var binding: FragmentEditLocationBinding
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
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_edit_location, container, false)
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

    private fun getUserLocation() {

        val userLat = ApplicationClass.prefs.getLat()
        val userLng = ApplicationClass.prefs.getLng()

        if (userLat.isEmpty() || userLng.isEmpty()) {
            binding.location = null
            return
        }
        val pos = MapPoint.mapPointWithGeoCoord(userLat.toDouble(), userLng.toDouble())
        val reverseGeoCoder = MapReverseGeoCoder(
            getString(R.string.kakao_app_key),
            pos,
            this,
            profileMenuActivity
        )
        reverseGeoCoder.startFindingAddress()
    }

    @SuppressLint("MissingPermission")
    private fun updateUserArea() {
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

            Log.d(
                TAG,
                "onAreaSetClick: id: ${ApplicationClass.prefs.getId()} , areaLat:${uLatitude.toString()} , areaLng:${uLongitude.toString()}"
            )

            val newUser = HashMap<String, String>()
            newUser["id"] = ApplicationClass.prefs.getId().toString()
            newUser["areaLat"] = uLatitude.toString()
            newUser["areaLng"] = uLongitude.toString()

            Log.d(TAG, "onAreaSetClick: 동네인증할 사용자: ${newUser.toString()}")
            val result = ApplicationClass.userApi.updateUserLocation(newUser).awaitResponse()
            Log.d(TAG, "onAreaSetClick: body: ${result.body()}")
            Log.d(
                TAG,
                "onAreaSetClick: raw: ${result.raw()} \nbody:${result.body()}\n header: ${result.headers()}"
            )
            if (result.body()?.flag == "success") {
                Log.d(TAG, "onAreaSetClick: 동네인증성공")
                val dialog = AlertDialogWithCallback(object : AlertDialogInterface {
                    override fun onYesButtonClick(id: String) {
                        val intent = Intent(profileMenuActivity, MainActivity::class.java)
                        intent.putExtra("target", "홈")
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    }
                }, "동네인증을 성공했습니다.\n등록된 동네의 공유글을 확인해보세요!", "")
                dialog.isCancelable = false
                dialog.show(profileMenuActivity.supportFragmentManager, "UserLocation")
            } else {
                Log.d(TAG, "onAreaSetClick: 동네 인증 실패")
                val dialog = MyAlertDialog(
                    profileMenuActivity,
                    "동네 인증을 실패했습니다.\n위치 정보를 가져올 수 있는 곳에서 다시 시도해주세요."
                )
                dialog.show(profileMenuActivity.supportFragmentManager, "UserLocation")
            }
        }
    }

    val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                // Precise location access granted.
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                // Only approximate location access granted.
            } else -> {
            // No location access granted.
        }
        }
    }

    fun onAreaSetClick(view: View) {
        // 위치 권한 가져오기
        if (PermissionUtil().checkLocationPermission(profileMenuActivity)) { // 권한 있는 경우
            updateUserArea()
        } else {
            PermissionUtil().requestLocationPermissions(profileMenuActivity) // 권한 없는경우 요청
        }
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult( // 권한 요청 후 콜백
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQ_LOCATION) {
            if (grantResults.isEmpty()) {
                // 권한 창 떴을 때 그냥 끈 경우
                Log.d(TAG, "onRequestPermissionsResult: 사용자 그냥 끔?!")
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "onRequestPermissionsResult: 권한 성공!!")
                updateUserArea()
            } else {
                Log.d(TAG, "onRequestPermissionsResult: 퍼미션 거부 ?!")
                for (permission in permissions) {
                    if ("android.permission.ACCESS_FINE_LOCATION" == permission) {
                        val dialog = ConfirmDialog(
                            object : ConfirmDialogInterface {
                                override fun onYesButtonClick(id: String) {
                                    val intent = Intent()
                                    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                                    intent.data = Uri.fromParts(
                                        "package",
                                        profileMenuActivity.packageName,
                                        null
                                    )
                                    startActivity(intent)
                                }
                            },
                            "동네 설정을 위해 위치 접근을 권한이 필요합니다.\n휴대폰 [설정]-[애플리케이션]-[vilez]에서 위치 권한을 켜주세요.",
                            ""
                        )
                        dialog.show(
                            profileMenuActivity.supportFragmentManager,
                            "LocationPermission"
                        )
                    }
                }
            }
        }
        else super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    override fun onReverseGeoCoderFoundAddress(p0: MapReverseGeoCoder?, p1: String?) {
        /** 숫자 필터링 */
        var result = ""
        var data = p1!!.split(" ")
        Log.d(TAG, "onReverseGeoCoderFoundAddress: data: $data")
        for (i in 0..data.size - 1 step (1)) {
            var tmp = data[i]
            Log.d(TAG, "onReverseGeoCoderFoundAddress: tmp:$tmp")
            if (tmp.contains(Regex("[0-9]"))) {
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