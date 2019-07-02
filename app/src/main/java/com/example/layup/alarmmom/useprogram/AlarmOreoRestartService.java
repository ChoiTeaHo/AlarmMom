package com.example.layup.alarmmom.useprogram;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.layup.alarmmom.R;

/**
 * Created by layup on 2018-12-08.
 */

public class AlarmOreoRestartService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("tag", "#####################################################  AlarmOreoRestartService.class onCreat........ ##################################################### ");
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {



        startForegroundService(intent);  //내가만든메소드
        return START_REDELIVER_INTENT;
        /**
        (*** 참고2 ***)
        1) Service.START_STICKY : 서비스 시작 후 (onStartCommand() 호출 이후) 강제로 종료될 경우, 자동으로 서비스를 다시 실행함.
        2) Service.START_NOT_STICKY : 서비스가 강제로 종료되었다 할지라도 다시 서비스를 시작하지 않음.
        3) Service.START_REDELIVER_INTENT : 서비스 시작 후 강제로 종료될 경우, 자동으로 다시 서비스를 시작하며
        마지막으로 전달된 인텐트를 onStartCommand()의 인자로 전달함.*/
    }




    // Oreo 부터는 Background 상태에서 서비스를 시작할 수 없다. 즉, 서비스 시작 후 이를 Foreground 서비스로 설정이 불가능하다.
    //startForegroundServiced 를 사용하면 해결가능하다.
    //단, 5초 내에 Service.startForeground()를 통해 Notification과 연결되지 않으면 즉시 해당 서비스를 중지한다..


    public ComponentName startForegroundService(Intent intent){
        //오레오 전용 Notification
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setContentTitle("알람 제목");
            builder.setContentText("알람 세부 텍스트");

            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(new NotificationChannel("default", "기본 채널", NotificationManager.IMPORTANCE_DEFAULT));



            //인텐트 부분 핵심
            Intent soundServiceIntent = new Intent(this, AlarmSoundService.class);

            String get_yout_string2 = intent.getExtras().getString("state");    //인텐트 두번 껑충해서 송신하기.
            soundServiceIntent.putExtra("state", get_yout_string2);

            startService(soundServiceIntent);

            startForeground(1, builder.build()); //이 메소드를 지우면 동작하지 않는다.
                                                 // 즉, Oreo 버전이후에는 서비스실행을위해
                                                // forground로 Notificaition으로 알림을 반드시 해줘야한다. ( 0을제외한 10이하숫자.)

            Log.d("tag","##################################################### StartForegroundService 커스텀 메소드 실행.");


            stopForeground(true);
            stopSelf();
            // OreoRestartService 의 코드는 Android에서 제공하는 죽지않는(Foreground) 서비스의 코드이다.
            // 포그라운드(Foreground)로 실행되지만, Android 알림창에 표시되는 문제가 있다.
            // 따라서, RestartService를 startForeground로 실행한뒤에, OreoRealService를 실행한다 (startService).

            // 그리고 실행된 RestartService를 stopForeground와 stopSelf를 실행하여 RestartService를 종료한다.
            // RestartService가 실행되고 종료되는 시간이 짧기 때문에 알림창에는 표시가 생기지 않는다.

        }
        return null;
    }

    @Override
    public void onDestroy() {
        Log.d("tag","#####################################################  AlarmOreoRestartService.class: 파괴 발동!!!!!! #####################################################  ");

        //System.exit(0);
    }
}
