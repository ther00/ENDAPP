package com.example.endapp.ui.music

import android.Manifest
import android.content.*
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.view.View
import android.widget.SeekBar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.endapp.R
import kotlinx.android.synthetic.main.activity_music.*

import kotlin.concurrent.thread

class MusicActivity : AppCompatActivity(),ServiceConnection {
    companion object{
        const val Music_Broadcast = "com.example.endapp.receiver"
    }
    val TAG = "MainActivity"
    var binder:MusicService.MusicBinder? = null
    val receiver = MusicReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music)

        val intentFilter = IntentFilter()
        intentFilter.addAction(Music_Broadcast)
        registerReceiver(receiver,intentFilter)

        //请求权限的判断
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),0) //请求获取权限
        }else{
            startMusicService()
        }
        val intent = Intent(this,MusicService::class.java)
        startService(intent)
        bindService(intent,this, Context.BIND_AUTO_CREATE)
        //拖拽
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(
                seekBar: SeekBar,
                progress: Int,
                fromUser: Boolean
            ) {
                if(fromUser){
                    binder?.currentPosition =  progress
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })




    }

    fun startMusicService(){
        val intent = Intent(this,MusicService::class.java)
        intent.putExtra(MusicService.Commond,1)
        startService(intent)
        bindService(intent,this, Context.BIND_AUTO_CREATE)
    }
    fun onPlay(v: View){
        val intent = Intent(this,MusicService::class.java)
        intent.putExtra(MusicService.Commond,1)
        startService(intent)
    }
    fun onPause(v: View){
        val intent = Intent(this,MusicService::class.java)
        intent.putExtra(MusicService.Commond,2)
        startService(intent)
    }
    fun onStop(v: View){
        val intent = Intent(this,MusicService::class.java)
        intent.putExtra(MusicService.Commond,3)
        startService(intent)
    }
    fun onNext(v: View){
        val intent = Intent(this,MusicService::class.java)
        intent.putExtra(MusicService.Commond,4)
        startService(intent)
    }
    fun onPrev(v: View){
        val intent = Intent(this,MusicService::class.java)
        intent.putExtra(MusicService.Commond,5)
        startService(intent)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        startMusicService()
    }

    override fun onDestroy() {
        super.onDestroy()
//        unbindService(this)
        unregisterReceiver(receiver)
    }
    override fun onServiceDisconnected(p0: ComponentName?) {
        binder = null
    }

    override fun onServiceConnected(p0: ComponentName?, service: IBinder?) {
        binder = service as MusicService.MusicBinder
        binder?.apply {
            seekBar.max = duration
            textView_mn.text = musicName
            textView_count.text = "${currentIndex+1}/${size}"
        }
        //线程
        thread {
            while (binder != null){
                Thread.sleep(1000)
                runOnUiThread {
                    seekBar.progress = binder!!.currentPosition

                }
            }
        }
    }
    inner class MusicReceiver: BroadcastReceiver(){
        override fun onReceive(p0: Context?, intent: Intent?) {
            binder?.apply {
                seekBar.max = duration
                textView_mn.text = musicName
                textView_count.text = "${currentIndex+1}/${size}"
            }

        }

    }
}