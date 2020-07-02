package com.example.updateviewmodel.view

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.database.Observable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.telecom.ConnectionService
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.updateviewmodel.MusicManager
import com.example.updateviewmodel.R
import com.example.updateviewmodel.model.SongSearch
import com.example.updateviewmodel.databinding.ActivityMainBinding
import com.example.updateviewmodel.service.MusicService
import com.example.updateviewmodel.viewmodel.SongViewModel
import io.reactivex.disposables.Disposable
import java.text.SimpleDateFormat

class MainActivity : AppCompatActivity(), SongAdapter.ISongAdapter {
    private lateinit var binding: ActivityMainBinding

    private var musicService: MusicService? = null
    private lateinit var conn:ServiceConnection

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_main
        )
        startStarservice()
        createConnection()
        binding.rcSong.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = SongAdapter(
                if (musicService == null ||
                    musicService!!.getModel().songs.value == null
                )
                    mutableListOf<SongSearch>() else
                    musicService!!.getModel().songs.value!!,
                this@MainActivity
            )
        }
        binding.lifecycleOwner = this



        binding.btnSearch.setOnClickListener {
            if (musicService == null) {
                return@setOnClickListener
            }
            musicService!!.searchSong(binding.edtSearch.text.toString().trim())
        }

    }

    private fun createConnection() {
        //tao cau connection
        conn = object : ServiceConnection {
            override fun onServiceDisconnected(name: ComponentName?) {

            }

            override fun onServiceConnected(
                name: ComponentName?,
                service: IBinder?
            ) {
                musicService =
                    (service as MusicService.MyBinder).musicService

                binding.data = musicService!!.getModel()
                if (musicService!!.getModel().songs.value == null){
                    musicService!!.searchSong(binding.edtSearch.text.toString().trim())
                }

                register()
            }
        }
        //tao ket noi
        val intent = Intent()
        intent.setClass(this, MusicService::class.java)
        bindService(intent, conn, Context.BIND_AUTO_CREATE)
    }

    private fun register() {
        musicService!!.getModel().finishGetLinkSong.observe(this, Observer {
//            it la full link song
            binding.tvName.setText(it.songName)
            binding.tvArtist.setText(it.artistName)
            musicService?.play(it)
        })

        musicService!!.getMpManager().prepare.observe(this, Observer {
            val format = SimpleDateFormat("mm:ss")
            binding.tvTotalTime.setText(format.format(it))
        })
    }


    override fun onItemClick(position: Int) {
        musicService!!.getFullLinkSong(position)

    }

    override fun onDestroy() {
        unbindService(conn)
        super.onDestroy()
    }

    private fun startStarservice(){
        val intent = Intent()
        intent.setClass(this, MusicService::class.java)
        startService(intent)
    }
}
