package com.sajidur.notificationtest

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService: FirebaseMessagingService() {

    private val TAG="MyFirebaseMessagingService"
    private val channelID="fcm_default_channel"
    private val channelName="fcm_default_channel"

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        val pref= MySharedPreferences(applicationContext)
        pref.setPushToken(p0)
    }
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Toast.makeText(applicationContext,"Message Recived",Toast.LENGTH_LONG).show()
        val notification=remoteMessage.notification
        if (notification!=null){
            showNotification(notification.title!!, notification.body!!)
        }
    }

    private fun showNotification(title:String,body:String){
        val intent=Intent(this,MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pi=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT)

        var builder:NotificationCompat.Builder=NotificationCompat.Builder(applicationContext,channelID)
            .setSmallIcon(R.drawable.ic_notifications)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000,1000,1000,100))
            .setOnlyAlertOnce(true)
            .setContentIntent(pi)
        builder=builder.setContent(getRemoteView(title,body))

        val notificationManager=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            val notificationChannel=NotificationChannel(channelID,channelName,NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        notificationManager.notify(0,builder.build())
    }

    @SuppressLint("RemoteViewLayout")
    private fun getRemoteView(title:String, body:String):RemoteViews{
        val remoteViews=RemoteViews("com.doctormd.doctor",R.layout.notification_layout)
        remoteViews.setTextViewText(R.id.title,title)
        remoteViews.setTextViewText(R.id.body,body)
        return  remoteViews
    }
}