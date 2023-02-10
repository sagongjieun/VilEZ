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
import kr.co.vilez.ui.SplashActivity
import kr.co.vilez.util.ApplicationClass
import kr.co.vilez.util.FCMTokenUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
        if(ApplicationClass.isForeground) {
            Log.d(TAG, "onMessageReceived: 현재 앱 켜져있음")
        } else {
            Log.d(TAG, "onMessageReceived: 현재 앱 꺼져있음")
        }

        /*if (remoteMessage.data.isNotEmpty() && remoteMessage.data["roomId"] != null && !ApplicationClass.isAppRunning) {
            // 채팅 왔을때는 백그라운드에서만 FCM 수신
            var data = remoteMessage.data
            Log.d(TAG, "data.message: ${data}")
            Log.d(TAG, "data.message: ${data["title"]}")
            Log.d(TAG, "data.message: ${data["body"]}")
            Log.d(TAG, "data.message roomId: ${data["roomId"]}")
            Log.d(TAG, "data.message otherUserId: ${data["otherUserId"]}")

            val messageTitle = data["title"].toString()
            val messageContent = data["body"].toString()
            val chatRoomId = data["roomId"].toString().toInt()
            val otherUserId = data["otherUserId"].toString().toInt()
            //알림의 탭 작업 설정 -----------------------------------------------------------------------
           val tapResultIntent = Intent(this, ChatRoomActivity::class.java).apply {
               putExtra("roomId", chatRoomId)
               putExtra("otherUserId", otherUserId)
               // TODO : 닉네임 ,프로필은 수정 필요
               putExtra("nickName", "알수없음")
               putExtra("profile", "https://kr.object.ncloudstorage.com/vilez/basicProfile.png")

               //현재 액티비티에서 새로운 액티비티를 실행한다면 현재 액티비티를 새로운 액티비티로 교체하는 플래그
               //flags = Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT
               //이전에 실행된 액티비티들을 모두 없엔 후 새로운 액티비티 실행 플래그
               flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
           }
           val pendingIntent: PendingIntent = PendingIntent.getActivity(
               this,
               0,
               tapResultIntent,
               PendingIntent.FLAG_IMMUTABLE
           )
            val builder1 = NotificationCompat.Builder(this, FCMTokenUtil().CHANNEL_ID)
                .setSmallIcon(R.drawable.img_default_profile)
                .setContentTitle(messageTitle)
                .setContentText(messageContent)
                .setAutoCancel(true) // 사용자가 클릭시 자동으로 메시지 삭제
                .setContentIntent(pendingIntent)

            NotificationManagerCompat.from(this).apply {
                notify(101, builder1.build())
            }
        }*/

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty() && !ApplicationClass.isForeground) { // 백그라운드에서만 FCM 수신
            var data = remoteMessage.data
            Log.d(TAG, "data.message: ${data}")
            Log.d(TAG, "data.message: ${data["title"]}")
            Log.d(TAG, "data.message: ${data["body"]}")
            Log.d(TAG, "data.message roomId: ${data["roomId"]}")

            val messageTitle = data["title"].toString()
            val messageContent = data["body"].toString()
            var chatRoomId:Int? = null
            if(data["roomId"] != null) {
                chatRoomId = data["roomId"].toString().toInt()
                Log.d(TAG, "onMessageReceived: 채팅메시지임 룸Id: $chatRoomId")
            }

            Log.d(TAG, "Message data payload: ${remoteMessage.data}")

            /*//알림의 탭 작업 설정 -----------------------------------------------------------------------
            val tapResultIntent = Intent(this, ChatRoomActivity::class.java).apply {
                //현재 액티비티에서 새로운 액티비티를 실행한다면 현재 액티비티를 새로운 액티비티로 교체하는 플래그
                //flags = Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT
                //이전에 실행된 액티비티들을 모두 없엔 후 새로운 액티비티 실행 플래그
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val pendingIntent: PendingIntent = PendingIntent.getActivity(
                this,
                0,
                tapResultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )*/

            val splashIntent = Intent(this, SplashActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            // requestCode가 다르면 별개의 메시지로 처리
            val splashPendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, splashIntent, PendingIntent.FLAG_IMMUTABLE)

            val builder1 = NotificationCompat.Builder(this, FCMTokenUtil().CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_vilez_round)
                .setContentTitle(messageTitle)
                .setContentText(messageContent)
                .setAutoCancel(true) // 사용자가 클릭시 자동으로 메시지 삭제
                .setContentIntent(splashPendingIntent)

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

        } else if (remoteMessage.data.isNotEmpty()) { // 포그라운드에서도 수신

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
        ApplicationClass.fcmTokenApi.uploadToken(FCMToken(ApplicationClass.prefs.getId(), token!!)).enqueue(object :
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
        private const val TAG = "빌리지_FCMService"
    }


}