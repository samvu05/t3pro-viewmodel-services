package com.example.updateviewmodel.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.updateviewmodel.MusicManager
import com.example.updateviewmodel.R
import com.example.updateviewmodel.model.SongSearch
import com.example.updateviewmodel.viewmodel.SongViewModel
import io.reactivex.disposables.Disposable


class MusicService : Service() {
    private lateinit var model: SongViewModel
    private lateinit var mpManager: MusicManager
    private var mDispose: Disposable? = null

    //khi tao service thi oncreate se duoc chay dau tien
    override fun onCreate() {
        super.onCreate()
        model = SongViewModel()
        mpManager = MusicManager()
        registerChanel()
    }

    fun getModel() = model
    fun getMpManager() = mpManager

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_NOT_STICKY
    }
    override fun onBind(intent: Intent?): IBinder? {

        return MyBinder(this)
    }

    class MyBinder : Binder {
        val musicService: MusicService

        constructor(musicService: MusicService) {
            this.musicService = musicService
        }
    }

    fun searchSong(name: String) {
        mDispose?.dispose()
        mDispose = model.searchSong(name)
    }

    fun getFullLinkSong(position: Int) {
        model.getFullLinkSong(
            model.songs.value!![position].linkSong!!
        )
    }

    fun play(item: SongSearch) {
        mpManager.setData(this, item.linkMusic!!)
        createNotificationMusic(item)
    }

    private fun registerChanel(){
        val mNotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "MusicService",
                "MusicService",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = "YOUR_NOTIFICATION_CHANNEL_DESCRIPTION"
            mNotificationManager.createNotificationChannel(channel)
        }
    }

    private fun createNotificationMusic(item: SongSearch) {
        //tao notification build de setup thong so
        val noti = NotificationCompat.Builder(
            this,
            "MusicService"
        )
        noti.setContentTitle("Music")
        noti.setContentText(item.songName)
        noti.setSmallIcon(R.drawable.baseline_library_music_white_36dp)
        noti.setLargeIcon(
            BitmapFactory.decodeResource(
                resources,
                R.drawable.aodai
            )
        )
//        val manager = getSystemService(Context.NOTIFICATION_SERVICE)
//                as NotificationManager
//        manager.notify(10, noti.build())
        startForeground(10, noti.build())
    }

}