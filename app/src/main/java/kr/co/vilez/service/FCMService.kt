package kr.co.vilez.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kr.co.vilez.ui.MainActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kr.co.vilez.R
import kr.co.vilez.data.model.RESTResult
import kr.co.vilez.util.ApplicationClass
import kr.co.vilez.util.FCMTokenUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "빌리지_FCMService"
class FCMService : FirebaseMessagingService() {


    // 새로운 토큰이 생성될 때 마다 해당 콜백이 호출된다.
    override fun onNewToken(token: String) {
        Log.d(TAG, "################### onNewToken: $token")

        // shared preference에 토큰 저장
        ApplicationClass.prefs.setFCMToken(token) // 현재 유저의 Token 저장

        // 새로운 토큰 수신 시 서버로 전송
        sendRegistrationToServer(token)
    }

    override fun getStartCommandIntent(originalIntent: Intent?): Intent {
        return super.getStartCommandIntent(originalIntent)
    }

    override fun handleIntent(intent: Intent?) {
        super.handleIntent(intent)
    }



    // [START receive_message]
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: ${remoteMessage.from}")

        val messageTitle = remoteMessage.notification!!.title
        val messageContent = remoteMessage.notification!!.body

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: ${remoteMessage.data}")
            val mainIntent = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val mainPendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, mainIntent, PendingIntent.FLAG_IMMUTABLE)

            val builder1 = NotificationCompat.Builder(this, FCMTokenUtil().CHANNEL_ID)
                .setSmallIcon(R.drawable.img_default_profile)
                .setContentTitle(messageTitle)
                .setContentText(messageContent)
                .setAutoCancel(true)
                .setContentIntent(mainPendingIntent)

            NotificationManagerCompat.from(this).apply {
                notify(101, builder1.build())
            }
//            if (/* Check if data needs to be processed by long running job */ true) {
//                // For long-running tasks (10 seconds or more) use WorkManager.
//                //scheduleJob()
//            } else {
//                // Handle message within 10 seconds
//                //handleNow()
//            }

        }

        // Check if message contains a notification payload.
        remoteMessage.notification?.let {
            Log.d(TAG, "Message Notification Body: ${it.body}")
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]


    // 새로운 토큰 수신 시 서버로 전송
    private fun sendRegistrationToServer(token: String?) {

        Log.d(TAG, "sendRegistrationTokenToServer($token)")

        ApplicationClass.retrofitFCMService.uploadToken(FCMToken(ApplicationClass.prefs.getId(), token!!)).enqueue(object :
            Callback<RESTResult> {
            override fun onResponse(call: Call<RESTResult>, response: Response<RESTResult>) {
                if(response.isSuccessful){
                    val res = response.body()
                    Log.d(TAG, "onResponse: $res")
                } else {
                    Log.d(TAG, "onResponse: Error Code ${response.code()}")
                }
            }
            override fun onFailure(call: Call<RESTResult>, t: Throwable) {
                Log.d(TAG, t.message ?: "토큰 정보 등록 중 통신오류")
            }
        })
    }


    private fun sendNotification(messageBody: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
            PendingIntent.FLAG_IMMUTABLE)

        val channelId = "fcm_default_channel"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle("FCM Message")
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        val channel = NotificationChannel(channelId,
            "Channel human readable title",
            NotificationManager.IMPORTANCE_DEFAULT)
        notificationManager.createNotificationChannel(channel)

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }
    companion object {
        private const val TAG = "MyFirebaseMsgService"
    }


}