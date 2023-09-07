package com.example.myapplication10

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.myapplication10.databinding.ActivityMainBinding
import com.example.myapplication10.databinding.DialogInputBinding

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        //view binding 사용
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button1.setOnClickListener{
            //토스트(Toast) : 화면 아래쪽에 잠깐 보였다가 사라지는 문자열
            //Toast.makeText(this, "첫번째 버튼의 토스트입니다", Toast.LENGTH_LONG).show()
            //세부적으로 설정하려면 변수를 이용
            val toast = Toast.makeText(this, "첫번째 버튼의 토스트입니다", Toast.LENGTH_LONG)
            toast.setText("수정된 토스트입니다") //처음에 설정한 내용을 수정 
            toast.duration = Toast.LENGTH_SHORT //처음에 설정한 시간을 수정

            //API 30 > toast.setGravity(Gravity.TOP, 20, 20) //토스트의 위치를 변경 가능 (API 30 이하, 실습 X)
   
            //토스트가 화면에 보이거나 사라지는 순간을 콜백으로 감지해 특정 로직을 수행하게 할 수 있음
            //API 30이상에서만 가능 (호환성 추가) -> @RequiresApi(Build.VERSION_CODES.R) 추가
            toast.addCallback(
                object:Toast.Callback(){
                    override fun onToastHidden() {
                        super.onToastHidden()
                        Log.d("mobileApp", "토스트가 사라집니다")
                    }

                    override fun onToastShown() {
                        super.onToastShown()
                        Log.d("mobileApp", "토스트가 나타납니다")
                    }
                }
            )
            toast.show()
        }
        
        //DatePickerDialog : 날짜를 입력받을 때
        binding.button2.setOnClickListener{
            DatePickerDialog(this,
                object:DatePickerDialog.OnDateSetListener{
                    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
                        Log.d("mobileApp", "$p1 년, ${p2+1} 월, $p3 일")
                    }
                                                         },
                2022,2,30).show() //month 0~11 //초기값
        }
        //TimePickerDialog : 시간을 입력받을 때
        binding.button3.setOnClickListener{
            TimePickerDialog(this,
                object:TimePickerDialog.OnTimeSetListener{
                    override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {
                        Log.d("mobileApp", "$p1 시 $p2 분")
                    }
                                                         } ,
                13, 0, true).show() //true 24, false 12 //초기값
        }

        //Alertdialog.Builder의 이벤트 헨들러 (이벤트 처리)
        val eventHandler = object:DialogInterface.OnClickListener{
            override fun onClick(p0: DialogInterface?, p1: Int) {
                if(p1==DialogInterface.BUTTON_POSITIVE){
                    Log.d("mobileApp", "positive button")
                }
                else if(p1==DialogInterface.BUTTON_NEGATIVE){
                    Log.d("mobileApp", "negative button")
                }
            }
        }

        //Alertdialog.Builder : 알림창 띄우기
        binding.button4.setOnClickListener{
            AlertDialog.Builder(this).run {
                setTitle("알림창 테스트")
                setIcon(android.R.drawable.ic_dialog_info)
                setMessage("정말 종료하시겠습니까?")
                setPositiveButton("YES", eventHandler) //이벤트 처리가 공통으로 들어가 변수 이용
                setNegativeButton("NO", eventHandler) 
                setNeutralButton("MORE", null) //일반적인 버튼 (MORE)
                setCancelable(false)
                show()
            }.setCanceledOnTouchOutside(false)
        }

        val items = arrayOf<String>("사과", "딸기", "복숭아", "토마토")//배열로 선언
        // [1. 목록을 보여줌]
        binding.button5.setOnClickListener{
            AlertDialog.Builder(this).run {
                setTitle("아이템 목록 선택")
                setIcon(android.R.drawable.ic_dialog_info)
                //리스트 형식으로 목록을 보여줌
                setItems(items, object:DialogInterface.OnClickListener{
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        Log.d("mobileApp", "${items[p1]}")
                    }
                })
                setPositiveButton("닫기", null)
                setCancelable(false) //뒤로가기 버튼을 눌렀을 때 닫지 않음
                show()
            }
        }
        /*
        setCancelabel() : 사용자가 기기의 뒤로가기 버튼을 눌렀을 때
        setCancelOnTouchOutside() : 알림창의 바깥 영역을 터치했을 때
        매개변수가 true(닫음), false(닫지 않음)
         */

        // [2. 목록을 보여주고 다중 선택 (체크박스)]
        binding.button6.setOnClickListener{
            AlertDialog.Builder(this).run {
                setTitle("멀티 아이템 목록 선택")
                setIcon(android.R.drawable.ic_dialog_info)
                setMultiChoiceItems(items, booleanArrayOf(false, true, false, false), //초기 설정값
                    object:DialogInterface.OnMultiChoiceClickListener{
                        //p1은 새로 선택됨, p2는 그대로 선택되었는지 해제되었는지 Log 창에 출력
                        override fun onClick(p0: DialogInterface?, p1: Int, p2: Boolean) {
                            Log.d("mobileApp", "${items[p1]} ${if(p2) "선택" else "해제"}")
                        }
                    }
                    )
                setPositiveButton("닫기", null)
                show()
            }.setCanceledOnTouchOutside(false) //알림창의 바깥 영역을 터치할 때 닫지 않음
        }
        // [3. 목록을 보여주고 단일 선택 (라디오버튼)]
        binding.button7.setOnClickListener{
            AlertDialog.Builder(this).run {
                setTitle("싱글 아이템 목록 선택")
                setIcon(android.R.drawable.ic_dialog_info)
                setSingleChoiceItems(items,1, object:DialogInterface.OnClickListener{ //초기 설정값
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        Log.d("mobileApp", "${items[p1]}")
                    }
                })
                setPositiveButton("닫기", null)
                show()
            }
        }

        //커스텀 다이얼로그 (AlertDialog.Builder)
        //dialog_input.xml을 생성한 후에 ViewBinding으로 불러옴
        //뷰를 이미 만든 상태에서 뷰를 또 생성하면 오류 발생!!
        
        //alertDialog를 미리 만들어 놓고 버튼을 누르면 show를 함
        //alert 변수를 추가
        val dialogBinding = DialogInputBinding.inflate(layoutInflater)
        val alert = AlertDialog.Builder(this)
            .setTitle("입력")
            .setView(dialogBinding.root)
            .setPositiveButton("닫기", null)
            .create()

        binding.button8.setOnClickListener{
            alert.show()
        }
    }
}