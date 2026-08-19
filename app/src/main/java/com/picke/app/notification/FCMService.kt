package com.picke.app.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Log
import androidx.core.app.NotificationCompat
import com.picke.app.BuildConfig
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.picke.app.MainActivity
import com.picke.app.R
import com.picke.app.data.local.TokenManager
import com.picke.app.domain.repository.DeviceRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FCMService : FirebaseMessagingService() {

    @Inject lateinit var deviceRepository: DeviceRepository
    @Inject lateinit var tokenManager: TokenManager

    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        tokenManager.saveFcmToken(token)

        // 로그인 상태일 때만 서버에 등록 (비로그인 시 로그인 이후 등록)
        if (tokenManager.getAccessToken() != null) {
            serviceScope.launch {
                deviceRepository.registerDevice(token)
                    .onFailure { if (BuildConfig.DEBUG) Log.e(TAG, "FCM 토큰 서버 등록 실패", it) }
            }
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        if (BuildConfig.DEBUG) Log.d(TAG, "FCM 메시지 수신 - from: ${message.from}, data: ${message.data}")

        // 서버는 data-only 메시지로 전송 (notification 블록 없음)
        val data = message.data
        val title = data["title"] ?: "픽케"
        val body = data["body"] ?: ""
        val type = data["type"] ?: TYPE_ALARM
        val battleId = data["battleId"]
        val perspectiveId = data["perspectiveId"]
        val commentId = data["commentId"]

        showNotification(title, body, type, battleId, perspectiveId, commentId)
    }

    private fun showNotification(
        title: String,
        body: String,
        type: String,
        battleId: String?,
        perspectiveId: String?,
        commentId: String?
    ) {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            putExtra(EXTRA_FCM_TYPE, type)
            battleId?.let { putExtra(EXTRA_FCM_BATTLE_ID, it) }
            perspectiveId?.let { putExtra(EXTRA_FCM_PERSPECTIVE_ID, it) }
            commentId?.let { putExtra(EXTRA_FCM_COMMENT_ID, it) }
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            System.currentTimeMillis().toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val largeIcon = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_bell)
            .setLargeIcon(largeIcon)
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }

    companion object {
        private const val TAG = "FCMService"
        const val CHANNEL_ID = "picke_default"
        const val EXTRA_FCM_TYPE = "fcm_type"
        const val EXTRA_FCM_BATTLE_ID = "fcm_battle_id"
        const val EXTRA_FCM_PERSPECTIVE_ID = "fcm_perspective_id"
        const val EXTRA_FCM_COMMENT_ID = "fcm_comment_id"

        const val TYPE_BATTLE = "BATTLE"
        const val TYPE_COMMENT = "COMMENT"
        const val TYPE_ALARM = "ALARM"
        const val TYPE_DAILY_MESSAGE = "DAILY_MESSAGE"
    }
}