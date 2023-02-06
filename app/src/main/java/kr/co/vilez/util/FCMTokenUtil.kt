package kr.co.vilez.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.provider.Settings.Global.getString
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import kr.co.vilez.R
import kr.co.vilez.data.model.RESTResult
import kr.co.vilez.service.FCMToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.awaitResponse

private const val TAG = "빌리지_FCMTokenUtil"
class FCMTokenUtil  {
    // FCM Channel Id
    val CHANNEL_ID = "vilez_channel"

    @RequiresApi(Build.VERSION_CODES.O)
    fun createFcmToken(context: Context) {
        // FCM 토큰 수신
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "FCM 토큰 얻기에 실패하였습니다.", task.exception)
                return@OnCompleteListener
            }
            // token log 남기기
            Log.d(TAG, "################### createFcmToken: user_id : ${ApplicationClass.prefs.getId()}")
            Log.d(TAG, "token: ${task.result?:"task.result is null"}")
            if(task.result != null){
                //ApplicationClass.retrofitFCMService.uploadToken(FCMToken(ApplicationClass.prefs.getId(),task.result!!))
                // 새로운 토큰 수신 시 서버로 전송
                ApplicationClass.retrofitFCMService.uploadToken(FCMToken(ApplicationClass.prefs.getId(),task.result!!)).enqueue(object :
                    Callback<RESTResult> {
                    override fun onResponse(call: Call<RESTResult>, response: Response<RESTResult>) {
                        if(response.isSuccessful && response.body()?.flag == "success"){
                            val res = response.body()!!
                            Log.d(TAG, "@@@@@@@onResponse: 서버에 저장한 토큰 값@@@@@@ : ${res.data}")
                        } else {
                            Log.d(TAG, "onResponse: Error Code ${response.code()}")
                        }
                    }
                    override fun onFailure(call: Call<RESTResult>, t: Throwable) {
                        Log.d(TAG, t.message ?: "토큰 정보 등록 중 통신오류")
                    }
                })
            }
        })
        createNotificationChannel(context, CHANNEL_ID, "guffy")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    // Notification 수신을 위한 체널 추가
    private fun createNotificationChannel(context: Context, id: String, name: String) {
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(id, name, importance)

        val notificationManager: NotificationManager
                = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

}