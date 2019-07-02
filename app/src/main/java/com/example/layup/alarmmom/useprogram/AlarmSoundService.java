package com.example.layup.alarmmom.useprogram;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.layup.alarmmom.R;

/**
 * Created by layup on 2018-11-26.
 */

public class AlarmSoundService extends Service {
    private static boolean isRunning;
    private MediaPlayer mediaPlayer;
    private int startId;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        Log.d("tag","#####################################################  AlarmSound.class onCreat........ ##################################################### ");
        Log.d("tag","#####################################################  이거 절대로 쓰지마 태호야....... ##################################################### ");

        super.onCreate();



    }

    public AlarmSoundService() {
        Log.d("tag","#####################################################  AlarmSound.class 생성자 실행. ##################################################### ");
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("tag","#####################################################  AlarmService.class: onStartCommand 실행.: #####################################################  ");


        /*//test
        if (intent.getExtras() == null) {
            Log.d("tag", "##### getExtras값없음: ");
            System.exit(0);
        }*/


        //인텐트 키값 받아오기
        String getState = intent.getExtras().getString("state");
        Log.d("tag","##### getState값: " + getState);




        assert getState != null;
        switch (getState) {
            case "music on":
                startId = 1;
                break;
            case "music off":
                startId = 0;
                break;
            default:
                startId = 0;
                break;
        }



        // 알람음 재생 X , 알람음시작 클릭 (1번)
        if (!this.isRunning && startId == 1 ) {
            Log.d("tag", "##### 1");

            //mediaPlayer.start();
            mediaPlayer= MediaPlayer.create(this, R.raw.ouu);
            mediaPlayer.setLooping(true);
                mediaPlayer.start();
                Toast.makeText(AlarmSoundService.this,"알람음 재생중입니다.",Toast.LENGTH_SHORT).show();
                this.isRunning = true;
                this.startId = 0;

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            }else{
                Bundle bun = new Bundle();
                bun.putString("notiMessage", "aa");


                Intent popupIntent = new Intent(getApplicationContext(), AlarmDialog.class);

                popupIntent.putExtras(bun);
                PendingIntent pie= PendingIntent.getActivity(getApplicationContext(), 0, popupIntent, PendingIntent.FLAG_ONE_SHOT);


                try {
                    pie.send();
                } catch (Exception e) {
                }
            }





        }



        // 알람음 재생 O , 알람음 종료 클릭 (2번)
        else if (this.isRunning && startId == 0){
            Log.d("tag","##### 2" );

            Toast.makeText(AlarmSoundService.this," 재생하던거 종료할게 굳굳.",Toast.LENGTH_SHORT).show();
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();



            this.isRunning = false;
            this.startId = 0;
            stopSelf();
        }



        // 알람음 재생 X, 알람음 종료버튼클릭 (3번)
        else if(!this.isRunning && startId == 0 ){
            Log.d("tag","##### 3" );


            Toast.makeText(AlarmSoundService.this,"재생도안되는데 왜자꾸 종료눌러.",Toast.LENGTH_SHORT).show();

            this.isRunning = false;
            this.startId = 0;
        }



        // 알람음 재생 O, 알람음 시작버튼클릭 (4번)
        else if(this.isRunning && startId == 1 ){
            Log.d("tag","##### 4" );

            Toast.makeText(AlarmSoundService.this,"이미 실행중인데 왜자꾸 실행눌러.",Toast.LENGTH_SHORT).show();
            this.isRunning = true;
            this.startId = 1;
        }

        else { }

        return START_REDELIVER_INTENT; //서비스 재실행하지않음.
    }



    @Override
    public void onDestroy() {
        Log.d("tag","#####################################################  AlarmService.class: 파괴 발동!!!!!! #####################################################  ");
    }



}
