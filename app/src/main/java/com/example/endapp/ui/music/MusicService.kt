package com.example.endapp.ui.music

import android.app.*
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.provider.MediaStore
import android.util.Log
import com.example.endapp.R
import java.io.IOException

class MusicService : Service() {

    companion object{
        val Commond = "Operate"
    }
    val TAG = "MusicService"
    val mediaPlayer = MediaPlayer()
    val musicList = mutableListOf<String>() //音乐列表
    val musicNameList = mutableListOf<String>() //音乐名列表
    var current = 0 //当前音乐的位置
    var isPause = false
    val binder = MusicBinder()
    val MyChannel1 = "music channel"

    inner class MusicBinder: Binder(){
        val musicName
            get() = musicNameList.get(current)
        var currentPosition
            get() = mediaPlayer.currentPosition
            set(value) = mediaPlayer.seekTo(value)

        val duration
            get() = mediaPlayer.duration //持续时间

        val size
            get() = musicList.size

        val currentIndex
            get() = current
    }
    override fun onCreate() {
        super.onCreate()
        getMusicList()
        val intent2 = Intent(MusicFragment.Music_Broadcast)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val builder : Notification.Builder
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel1 = NotificationChannel(MyChannel1,"this is mychannel", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel1)
            builder = Notification.Builder(this,MyChannel1)
        }else{
            builder = Notification.Builder(this)
        }
        val intent = Intent(this,MusicFragment::class.java)
        val pendingIntent = PendingIntent.getActivity(this,1,intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val notification = builder.setContentTitle("music notification")
            .setContentText("This is music service.")
            .setSmallIcon(R.drawable.ic_music_24)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        startForeground(1,notification)

        mediaPlayer.setOnPreparedListener{
            it.start()
            sendBroadcast(intent2)
            val notification = builder.setContentTitle("${current+1}/${musicNameList.size}")
                .setContentText(musicNameList.get(current))
                .setSmallIcon(R.drawable.ic_music_24)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()
            notificationManager.notify(1,notification)
        }
        mediaPlayer.setOnCompletionListener {
            next()
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //意图调用参数
        val operate = intent?.getIntExtra(Commond,1) ?: 1
        when(operate){
            1->play()
            2->pause()
            3->stop()
            4->next()
            5->prev()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        mediaPlayer.stop()
        super.onDestroy()
    }

    fun pause(){
        if(isPause){
            mediaPlayer.start()
            isPause = false
        }else{
            mediaPlayer.pause()
            isPause = true
        }
    }
    fun stop(){
        mediaPlayer.stop()
        stopSelf()
    }
    fun next(){
        current++
        if(current >= musicList.size){
            current = 0
        }
        play()
    }
    fun prev(){
        current--
        if(current < 0){
            current = musicList.size - 1
        }
        play()
    }

    fun play() {
        if (musicList.size == 0) return
        val path = musicList[current]
        mediaPlayer.reset()
        try {
            mediaPlayer.setDataSource(path)
            mediaPlayer.prepareAsync()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    override fun onBind(intent: Intent): IBinder {
        return binder
    }
    private fun getMusicList(){
        val cursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,null,null,null)
        if(cursor != null){ //游标
            while(cursor.moveToNext()){
                val musicPath = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
                musicList.add(musicPath) //data
                val musicName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
                musicNameList.add(musicName) //title
                Log.d(TAG,"getMusicList: $musicPath name: $musicName")
            }
            cursor.close()
        }
//        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        val builder : Notification.Builder
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            val notificationChannel = NotificationChannel(Channel_ID,"test", NotificationManager.IMPORTANCE_DEFAULT)
//            notificationManager.createNotificationChannel(notificationChannel)
//            builder = Notification.Builder(this,Channel_ID)
//        }else{
//            builder = Notification.Builder(this)
//        }
//        val intent = Intent(this,MainActivity::class.java)
//        val pendingIntent = PendingIntent.getActivity(this,1,intent, PendingIntent.FLAG_UPDATE_CURRENT)
//        val notification = builder.setSmallIcon(R.drawable.ic_music)
//            .setContentTitle("当前正在播放：")
//            .setContentText(musicNameList[current])
//            .setContentIntent(pendingIntent)
//            .setAutoCancel(true)
//            .build()
//        notificationManager.notify(Notification_ID,notification)
    }
}
