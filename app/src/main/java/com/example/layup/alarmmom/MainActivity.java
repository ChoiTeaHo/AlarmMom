package com.example.layup.alarmmom;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.layup.alarmmom.useprogram.AlarmReciever;
import com.example.layup.alarmmom.useprogram.DatabaseSQLite;

import java.util.Calendar;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;

public class MainActivity extends AppCompatActivity {

    private static final String INTENT_ACTION = "com.example.layup.alarmmom.useprogram.ALARM_START";


    static private AlarmManager alarm_manager = null;
    static private PendingIntent mPendingIntent = null;
    private TimePicker alarm_timepicker;
    private Calendar calendar, calendarClone;

    private Intent my_intent;

    private DatabaseSQLite databaseSQL;
    String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("tag", "################################# MainActivity.class onCreate. . . . . 객체 생성중. ");

        //DBaseSQLite 생성
        databaseSQL = new DatabaseSQLite(getApplicationContext());


        //타임피커 설정
        alarm_timepicker = (TimePicker) findViewById(R.id.time_picker);

        //Calendar  객체생성
        calendar = Calendar.getInstance();

        //Calendar 객체복사
        calendarClone = (Calendar) calendar.clone();

        // AlarmManager 서비스 onCreate 시작에 받아오기.
        alarm_manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);


        // 알람리시버 액션 intent 객체생성
        my_intent = new Intent(INTENT_ACTION);


            /*if(data == null){
            }else {
                stopAlarm();
            }*/


        try{
            Intent i = getIntent();
             name = i.getExtras().getString("stopDialogClick");
            Log.d("TAG", "################################################################################     :" + name);
            if(name.equals("alarm stop")){
            }else {
                stopAlarm();
            }
        }catch (Exception e) {


            /** 알람 정지버튼 */
            Button free = (Button) findViewById(R.id.free);
            free.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Log.d("tag", "################################# 현재 getPowerMode : " + databaseSQL.getPowerMode());
                    if (databaseSQL.getPowerMode() == true) {
                        databaseSQL.delete_values();
                        Log.d("tag", "################################# 삭제완료.");

                        stopAlarm();

                    } else {
                        Log.d("tag", "################################# 삭제할 알람이 없습니다. ");
                        Toast.makeText(MainActivity.this, "Alarm을 설정하지 않았습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            });


            /** 알람시작버튼 */
            Button alarm_on = (Button) findViewById(R.id.alarm_on);
            alarm_on.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {
                    Log.d("tag", "################################# 알람설정 버튼을 눌렀습니다. ");
                    //TimePiker 입력한 시간을 Set
                    calendarClone.set(Calendar.HOUR_OF_DAY, alarm_timepicker.getHour()); //시
                    calendarClone.set(Calendar.MINUTE, alarm_timepicker.getMinute()); //분
                    calendarClone.set(Calendar.SECOND, 0); //초

                    // 현재시간과 입력시간 비교
                    if (calendarClone.compareTo(calendar) <= 0) {
                        Toast.makeText(MainActivity.this, "현재 시간보다 작습니다. Error", Toast.LENGTH_SHORT).show();
                        Log.d("tag", "#######  현재시간보다 작은 값을 입력했군요. ");

                    } else {
                        Log.d("tag", "#######  현재시간보다 높은 값을 입력했군요. 정상입니다.");
                        Log.d("tag", "################################# 현재 powerMode 상태 : " + databaseSQL.getPowerMode());

                        //DB 내용있는지 검사.
                        if (databaseSQL.getPowerMode() == false) {
                            int hour = alarm_timepicker.getHour();
                            int minute = alarm_timepicker.getMinute();
                            Toast.makeText(MainActivity.this, "저장 알람예정: " + hour + "시 " + minute + "분", Toast.LENGTH_SHORT).show();


                            databaseSQL.save_values(hour, minute);
                            Log.d("tag", "################################# false모드 이므로 DB에 알람을 새로 저장합니다..");

                            setAlarm(getApplicationContext(), my_intent);  //알람매니저셋팅메소드 실행


                        } else {
                            Log.d("tag", "################################# true모드 이므로 DB에 이미 등록되어있습니다. 아무것도 하지않습니다.");
                        }
                    }
                }
            });
        }

    }//절대영역







    /** setAlarm 메소드 */
    private void setAlarm(Context context, Intent my_intent) {

        // 알람매니저 설정
        alarm_manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        //Broadcast Receiver클래스의 intent생성


        //Broadcast Receiver클래스의 intent 명시적으로 지정. ### 중요 ### Oreo버전 이후에 브로드캐스트는 반드시 명시적인텐트 해야함.


        my_intent.setClass(context, AlarmReciever.class);
        my_intent.putExtra("state", "music on");

        //펜딩인텐트 생성 (Broadcast Receiver 에게 실행을 부탁할 인텐트. 알람매니저는 반드시 이걸로 감싸서 인텐팅함. ( 어떤리시버를 호출할지 인텐트삽입. )
        mPendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                my_intent,
                0);


        alarm_manager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 5000, mPendingIntent);
        //alarm_manager.set(AlarmManager.RTC_WAKEUP, calendarClone.getTimeInMillis(), mPendingIntent);
    }

    /** stopAlarm 메소드 */
    private void stopAlarm(){
        databaseSQL.delete_values();

        Toast.makeText(MainActivity.this, "Alarm 정지버튼 클릭!!!!!!!! .  /  "  +  "펜딩인텐트상태:  "  + mPendingIntent, Toast.LENGTH_SHORT).show();
        my_intent.putExtra("state", "music off");   // 키값을 가진 통신장치를 만들고
        sendBroadcast(my_intent); //감싸서 보내야한다. 이놈이 플레이를 멈출것이다.
        alarm_manager.cancel(mPendingIntent);
    }






    /**
     * 앱 종료 이벤트
     **/
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("tag", "#####################################################  앱 서비스종료 ##################################################### .");
        AlarmReciever.wakeLockOff();
    }
























    /**
     * --------------------------------------------------노티피케이션-----------------------------------------------
     **/
    //노티 생성버튼 이벤트
    public void createNotification(View view) {
        show();
    }

    //노티피케이션을 생성하는 커스텀함수를 작성.
    private void show() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());

        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("알람 제목");
        builder.setContentText("알람 세부 텍스트");

        //노티를 눌렀을때 이동 Intent
        Intent intent2 = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,  //request code 자리.
                intent2, //인텐트자리.
                FLAG_UPDATE_CURRENT);  //플래그자리. 노티피케이션이 이미 한번 실행되어있으면 코드실행시 내용을 업데이트

        builder.setContentIntent(pendingIntent); // Nortification을 클릭했을때 pending인텐트 안에 지정한 '인텐트' 가 실행.

        //큰아이콘설정 (비트맵으로 변환해줘야함)
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(),
                R.mipmap.ic_launcher);
        builder.setLargeIcon(largeIcon);

        //색상설정
        builder.setColor(Color.RED);

        // 노티알람소리 설정
        Uri ringtoneUri = RingtoneManager.getActualDefaultRingtoneUri(this, RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(ringtoneUri);

        // 노티 진동설정법
        long[] vibrate = {0, 100, 200, 300};    //진동규칙
        builder.setVibrate(vibrate);
        builder.setAutoCancel(true); //노티를 눌러서 날라가게 하려면 true;


        //매니저는 반드시 작성 참고로 오레오에선 채널id를 등록해야함.  NotificationManagerCompat.from(this).cancel(1);  <<<이렇게 해줘도된다. 같은코드
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        //오레오 이후버전인 경우.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(new NotificationChannel("default", "기본채널", NotificationManager.IMPORTANCE_DEFAULT));
        }

        //bulder 가 notification 객체를 리턴한다.
        manager.notify(1, builder.build());
    }


    //노티제거
    public void removeNotification(View view) {
        hide();
    }

    //노티피케이션을 제거하는 커스텀함수를 작성.
    private void hide() {
        NotificationManagerCompat.from(this).cancel(1);
    }


    /**
     * -----------------------------------------------------------------------------------------------------
     **/


    @Override
    public void onBackPressed() {
        super.onBackPressed();
            Toast.makeText(MainActivity.this, "종료됩니다!.", Toast.LENGTH_SHORT).show();

            //exit(0);

    }
}
