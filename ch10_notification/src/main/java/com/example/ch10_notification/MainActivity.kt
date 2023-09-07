package com.example.ch10_notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.AudioAttributes
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.NotificationCompat
import com.example.ch10_notification.databinding.ActivityMainBinding
import androidx.core.app.RemoteInput

//알림을 띄우려면 file-new-other-BroadCast Receiver 추가 (ReplyReceiver)

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Notification :NotificationManager, NotificationCompat.Builder 필요
        binding.button1.setOnClickListener{
            //선언만 되어진 상태
            val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val builder : NotificationCompat.Builder

            //버전에 따라 다르기 때문에 호환성 해결해줘야 함)
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                val ch_id = "one-channel"
                val channel = NotificationChannel(ch_id, "My Channel One", NotificationManager.IMPORTANCE_DEFAULT)
                //[채널의 속성 설정]
                channel.description = "My Channel One 소개" //채널의 설명 문자열
                channel.setShowBadge(true) //홈 화면의 아이콘에 배지 아이콘 출력 여부
                channel.enableLights(true) //불빛 표시 여부
                channel.lightColor = Color.RED //불빛이 표시된다면 불빛의 색상
                channel.enableVibration(true) //진동을 울릴지 여부
                channel.vibrationPattern = longArrayOf(100, 200, 100, 200) //진동을 울린다면 진도의 패턴

                // (100, 200) (100, 200) : (진동 X 시간, 진동 시간) : msec
                
                val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                val audio_attr = AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build()
                
                channel.setSound(uri, audio_attr) //채널의 사운드 설정

                manager.createNotificationChannel(channel) //manager에 등록
                builder = NotificationCompat.Builder(this, ch_id) //bulder 생성(채널 아이디도 같이 넣음)
            }
            else{
                builder = NotificationCompat.Builder(this)
            }

            //알림 객체 설정 (builder 속성 설정)
            builder.setSmallIcon(R.drawable.small) //이미지 설정
            builder.setWhen(System.currentTimeMillis()) //현재 시간으로 설정
            builder.setContentTitle("안녕하세요")
            builder.setContentText("모바일앱프로그래밍 시간입니다.")

            //이미지도 출력 (변수 이용)
            val bigpic = BitmapFactory.decodeResource(resources, R.drawable.big)
            val builderStyle = NotificationCompat.BigPictureStyle()
            builderStyle.bigPicture(bigpic)
            builder.setStyle(builderStyle)

            //호출을 요청
            val replyIntent = Intent(this, ReplyReceiver::class.java)
            //수정될 수 있으므로 PendingIntent.FLAG_MUTABLE 로 설정
            val replyPendingIntent = PendingIntent.getBroadcast(this, 30, replyIntent, PendingIntent.FLAG_MUTABLE)
            
            //builder.setContentIntent(replyPendingIntent) //builder에 등록

            //원격 입력 : 알림에서 사용자 입력을 직접 받는 기법
            //import androidx.core.app.RemoteInput 추가 작성
            val remoteInput = RemoteInput.Builder("key_text_reply").run{
                setLabel("답장")
                build()
            }

            //액션을 최대 3개까지 추가
            //알림에 엑션 추가
            builder.addAction(
                NotificationCompat.Action.Builder(
                    android.R.drawable.stat_notify_more,
                    "Action",
                    replyPendingIntent
                ).build()
            )
            builder.addAction(
                NotificationCompat.Action.Builder(
                    R.drawable.send,
                    "답장",
                    replyPendingIntent
                ).addRemoteInput(remoteInput).build()
            )
            manager.notify(11,builder.build())
        }
    }
}