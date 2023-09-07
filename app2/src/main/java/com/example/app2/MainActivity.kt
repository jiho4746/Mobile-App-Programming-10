package com.example.app2

import android.content.Context
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.*
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.app2.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //RingtoneManager : 소리를 가져와 재생(기본 제공)
        binding.button1.setOnClickListener{
            val notification : Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val rington = RingtoneManager.getRingtone(applicationContext, notification)
            rington.play()
        }

        //MediaPlayer : 음악 파일 res - raw 디렉토리 생성(사용자 제공)
        binding.button2.setOnClickListener{
            val play : MediaPlayer = MediaPlayer.create(this, R.raw.funny_voices)
            play.start()
        }

        //진동 울리기
        //AndroidManifest.xml에 <uses-permission android:name="android.permission.VIBRATE"/> 작성
        binding.button3.setOnClickListener{
            //Vibrator 객체 획득 (버전마다 방법이 다름)
            //(1) API30 이상이면
            val vibrator = if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.S){
                val vibratorManager = this.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                vibratorManager.defaultVibrator
            }
            else{
                getSystemService(VIBRATOR_SERVICE) as Vibrator
            }
            //Vibrator를 이용한 진동을 주기
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
                Log.d("mobileApp", "vibrator")
            }
            else {
                vibrator.vibrate(500)
                Log.d("mobileApp", "vibrator")
            }

        }

    }
}