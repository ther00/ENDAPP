package com.example.endapp.ui.music

import android.Manifest
import android.app.Activity
import android.content.*
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.endapp.R
import kotlinx.android.synthetic.main.fragment_music.*
import kotlin.concurrent.thread


class MusicFragment : Fragment(), ServiceConnection {

    companion object {
        fun newInstance() = MusicFragment()
        const val Music_Broadcast = "com.example.endapp.receiver"
    }

    private lateinit var viewModel: MusicViewModel

    val TAG = "MainActivity"
    var binder:MusicService.MusicBinder? = null
    val receiver = MusicReceiver()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_music, container, false)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MusicViewModel::class.java)
        // TODO: Use the ViewModel
        val intentFilter = IntentFilter()
        intentFilter.addAction(Music_Broadcast)
        requireActivity().registerReceiver(receiver,intentFilter)

        //请求权限的判断
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),0) //请求获取权限
        }else{
            startMusicService()
        }
        val intent = Intent(requireActivity(),MusicService::class.java)
        requireActivity().startService(intent)
        requireActivity().bindService(intent,this, Context.BIND_AUTO_CREATE)
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
        val intent = Intent(requireActivity(),MusicService::class.java)
        intent.putExtra(MusicService.Commond,1)
        requireActivity().startService(intent)
        requireActivity().bindService(intent,this,Context.BIND_AUTO_CREATE)
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
        requireActivity().unregisterReceiver(receiver)
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
                requireActivity().runOnUiThread {
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