package com.picke.app.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.picke.app.MainActivity
import com.picke.app.R

class FCMService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "FCM 토큰 갱신: $token")
        // TODO: 서버 FCM 토큰 등록 API 연동
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d(TAG, "FCM 메시지 수신 - from: ${message.from}, data: ${message.data}")

        val title = message.notification?.title ?: message.data["title"] ?: "픽케"
        val body = message.notification?.body ?: message.data["body"] ?: ""
        val type = message.data["type"] ?: TYPE_ALARM
        val battleId = message.data["battleId"]
        val commentId = message.data["commentId"]

        showNotification(title, body, type, battleId, commentId)
    }

    private fun showNotification(
        title: String,
        body: String,
        type: String,
        battleId: String?,
        commentId: String?
    ) {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            putExtra(EXTRA_FCM_TYPE, type)
            battleId?.let { putExtra(EXTRA_FCM_BATTLE_ID, it) }
            commentId?.let { putExtra(EXTRA_FCM_COMMENT_ID, it) }
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            System.currentTimeMillis().toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_bell)
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
        const val EXTRA_FCM_COMMENT_ID = "fcm_comment_id"

        const val TYPE_BATTLE = "BATTLE"
        const val TYPE_COMMENT = "COMMENT"
        const val TYPE_ALARM = "ALARM"
    }
}