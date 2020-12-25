package com.example.endapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.endapp.ui.music.MusicService

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_helloworld, R.id.navigation_music,R.id.navigation_card,R.id.navigation_photo,R.id.navigation_weather))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
    fun onPlay(v: View){
        val intent = Intent(this, MusicService::class.java)
        intent.putExtra(MusicService.Commond,1)
        startService(intent)
    }
    fun onPause(v: View){
        val intent = Intent(this, MusicService::class.java)
        intent.putExtra(MusicService.Commond,2)
        startService(intent)
    }
    fun onStop(v: View) {
        val intent = Intent(this, MusicService::class.java)
        intent.putExtra(MusicService.Commond,3)
        startService(intent)
    }
    fun onNext(v: View){
        val intent = Intent(this, MusicService::class.java)
        intent.putExtra(MusicService.Commond,4)
        startService(intent)
    }
    fun onPrev(v: View){
        val intent = Intent(this, MusicService::class.java)
        intent.putExtra(MusicService.Commond,5)
        startService(intent)
    }
}