package kr.co.vilez.util

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import kr.co.vilez.ui.user.RegisterActivity

class PermissionUtil {
    companion object{
        const val REQ_GALLERY = 1
        const val REQ_LOCATION = 2
    }

    /**
     * @param multiple : 이미지를 여러장 요청하는가
     * @param context : 퍼미션 요청하는 컨텍스트
     * @param imageResult : 갤러리에서 이미지 받아온 후 실행할 콜백
     */
    fun galleryPermission(multiple:Boolean, context: Context, imageResult: ActivityResultLauncher<Intent>) {
        val writePermission = ContextCompat.checkSelfPermission(
            context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val readPermission = ContextCompat.checkSelfPermission(
            context, android.Manifest.permission.READ_EXTERNAL_STORAGE
        )

        if (writePermission == PackageManager.PERMISSION_DENIED || readPermission == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(
                context as Activity, arrayOf(
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ), REQ_GALLERY
            )
        } else {
            val intent = Intent(Intent.ACTION_PICK)
            if(multiple) intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*"
            )
            imageResult.launch(intent)
        }
    }

    fun locationPermission(context: Context):Boolean {
        val locationFinePermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        val locationCoarsePermission = ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        if(locationFinePermission == PackageManager.PERMISSION_DENIED || locationCoarsePermission == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(
                context as Activity, arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), REQ_LOCATION
            )
            return false
        } else {
            return true
        }
    }



    fun checkLocationPermission(context: Context) :Boolean{
        val locationFinePermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        val locationCoarsePermission = ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        if(locationFinePermission == PackageManager.PERMISSION_DENIED || locationCoarsePermission == PackageManager.PERMISSION_DENIED) {
            return false
        }
        return true
    }


    fun requestLocationPermissions(context: Context) {
        ActivityCompat.requestPermissions(
            context as Activity, arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ), REQ_LOCATION
        )
    }

}