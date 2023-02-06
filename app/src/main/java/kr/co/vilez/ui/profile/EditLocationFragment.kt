package kr.co.vilez.ui.profile

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.vilez.R
import kr.co.vilez.databinding.FragmentEditLocationBinding
import kr.co.vilez.ui.MainActivity
import kr.co.vilez.ui.dialog.AlertDialog
import kr.co.vilez.ui.dialog.AlertDialogInterface
import kr.co.vilez.ui.dialog.AlertDialogWithCallback
import kr.co.vilez.ui.user.ProfileMenuActivity
import kr.co.vilez.util.ApplicationClass
import kr.co.vilez.util.PermissionUtil
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapReverseGeoCoder
import retrofit2.awaitResponse

private const val TAG = "빌리지_EditAreaFragment"
class EditLocation : Fragment(), MapReverseGeoCoder.ReverseGeoCodingResultListener {
    private lateinit var binding : FragmentEditLocationBinding
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_location, container, false)
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

        if(userLat.isEmpty() || userLng.isEmpty()) {
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

                Log.d(TAG, "onAreaSetClick: id: ${ApplicationClass.prefs.getId()} , areaLat:${ uLatitude.toString()} , areaLng:${uLongitude.toString()}")

                val newUser = HashMap<String, String>()
                newUser["id"] = ApplicationClass.prefs.getId().toString()
                newUser["areaLat"] = uLatitude.toString()
                newUser["areaLng"] = uLongitude.toString()

                Log.d(TAG, "onAreaSetClick: 동네인증할 사용자: ${newUser.toString()}")
                val result = ApplicationClass.retrofitUserService.updateUserLocation(newUser).awaitResponse()
                Log.d(TAG, "onAreaSetClick: body: ${result.body()}")
                Log.d(TAG, "onAreaSetClick: raw: ${result.raw()} \nbody:${result.body()}\n header: ${result.headers()}")
                if(result.body()?.flag == "success") {
                    Log.d(TAG, "onAreaSetClick: 동네인증성공")
                    val dialog = AlertDialogWithCallback(object :AlertDialogInterface{
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
                    val dialog = AlertDialog(profileMenuActivity, "동네 인증을 실패했습니다.\n위치 정보를 가져올 수 있는 곳에서 다시 시도해주세요.")
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