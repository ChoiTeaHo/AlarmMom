package com.example.layup.alarmmom.useprogram;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by layup on 2018-11-26.
 *  Oreo 이전버전 : setartService로 한방에 해당Service를 인텐트하여 실행.
 *  Oreo 이후버전 : Service를 백그라운드에서 실행을 금지함. -> 포그라운드(Foreground) 에서 실행해야한다. 즉, 알람은 백그라운드이기에 서비스를 실행할수 없다.
 *                  따라서 알림에서 포그라운드인 setForgroundService(Notification) 으로 서비스를 실행하고 이 서비스안에서 해당 서비스를 실행한다.
 *                  그리고 startForegroundService으로 실행한 서비스는 죽이는 방식으로 구현한다.
 *
 *
 *
 *                 안드로이드는 새로운 메서드를 제공합니다.
 *
             Context.startForegroundService() 를 통해서 실행된 서비스는 생성 후 5초 이내에
                Service.startForeground(int, Notification) 를 호출해야 살아남습니다.

            "내가 여기서 딴짓을 하고있습니다. 알지요?"
            그렇지 않으면 Exception을 던집니다.



 */

public class AlarmReciever extends BroadcastReceiver {

    private static PowerManager.WakeLock mWakeLock;
    private static final String TAG = "Alarm WakeLock";  //구분할수있는 문자열을 넣기위해.


    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("tag","##################################################### onReceive호출. Oreo 버전 전후 구별 분석중 . . . . . . . . . . . . ");

        // intent 로부터 전달받은 string 넣기
        String get_yout_string = intent.getExtras().getString("state");





        //  정적구현 브로드캐스트리시버 원리 실험용.
        String name = intent.getAction();
        if(name.equals("com.example.layup.alarmmom.useprogram.ALARM_START")){
            Log.d("tag","##################################################### AlarmReciver.class: 정상적인 Action값 수신했습니다. 브로드캐스트를 실행합니다.: ");
        }




        // Oreo 이후버전이면 Android에서 제공하는 죽지않는(Foreground) 서비스인 해당  xxSerivce.class 를 setartForegroundService하여 실행한다.
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            Log.d("tag","##################################################### AlarmReciver.class: 오레오 이후의 리시버실행: ");
            Toast.makeText(context, "오레오 이후의 리시버실행", Toast.LENGTH_SHORT).show();

            wakeLockOn(context);



            wakeLockOff();

            Intent mServiceintent1 = new Intent(context, AlarmOreoRestartService.class);

            // extra string값 보내기
            mServiceintent1.putExtra("state", get_yout_string);
            context.startForegroundService(mServiceintent1);
        }


        //Oreo 이전버전이면 서비스인 xxService.class 를 실행하면 끝이다.
        else{
            Log.d("tag","##################################################### AlarmReciver.class: 오레오 이전의 리시버실행: ");
            Toast.makeText(context, "오레오 이전의 리시버실행", Toast.LENGTH_SHORT).show();

            wakeLockOn(context);



            wakeLockOff();

            Intent mServiceintent2 = new Intent(context, AlarmSoundService.class);

            String get_yout_string2 = intent.getExtras().getString("state");

            // extra String값 보내기
            mServiceintent2.putExtra("state", get_yout_string2);

            context.startService(mServiceintent2);
        }

        Log.d("tag","##################################################### Oreo 버전 전후 구별완료");

    }







/**-------------------------------------------------------- WakeLock 코드--------------------------------------------------------------**/
    // wakeLock acquire() 메소드로 화면켜짐 등록하기
    public static void wakeLockOn(Context context){
        if(mWakeLock != null){return;}

        Context contexts = context;

        //디바이스 Power 관련코드
        PowerManager pm = (PowerManager)contexts.getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.
                SCREEN_BRIGHT_WAKE_LOCK |
                PowerManager.ACQUIRE_CAUSES_WAKEUP |
                PowerManager.ON_AFTER_RELEASE, TAG);

        mWakeLock.acquire(); //휴대폰이 대기상태로 가지않도록 유지

        Log.d("TAG","##############현재 WakeLock: " + mWakeLock);
    }

    // wakeLock rlease() 메소드 등록해제하기
    public static void wakeLockOff(){
        Log.d("TAG","############## 선행 WakeLockOFF 상태: " + mWakeLock);
        //디바이스 Power Off
        if(mWakeLock != null) {
            mWakeLock.release(); //켜짐과 동시에 종료시키기.
            mWakeLock = null;
        }
        Log.d("TAG","############## 후행 WakeLocOFF 상태: " + mWakeLock);
    }




}
