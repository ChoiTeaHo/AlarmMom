package com.example.layup.alarmmom.useprogram;

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
import android.support.v4.app.NotificationCompat;

import com.example.layup.alarmmom.MainActivity;
import com.example.layup.alarmmom.R;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;
import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by layup on 2018-12-21.
 */

public class AlarmNotification {

    Context context;

    public AlarmNotification(Context context) {
        this.context = context;
        show();
    }

    //노티피케이션을 생성하는 커스텀함수를 작성.
    private void show() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("알람 제목");
        builder.setContentText("알람 세부 텍스트");

        //노티를 눌렀을때 이동 Intent
        Intent intent2 = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,  //request code 자리.
                intent2, //인텐트자리.
                FLAG_UPDATE_CURRENT);  //플래그자리. 노티피케이션이 이미 한번 실행되어있으면 코드실행시 내용을 업데이트

        builder.setContentIntent(pendingIntent); // Nortification을 클릭했을때 pending인텐트 안에 지정한 '인텐트' 가 실행.

        //큰아이콘설정 (비트맵으로 변환해줘야함)
        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(),
                R.mipmap.ic_launcher);
        builder.setLargeIcon(largeIcon);

        //색상설정
        builder.setColor(Color.RED);

        // 노티알람소리 설정
        Uri ringtoneUri = RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(ringtoneUri);

        // 노티 진동설정법
        long[] vibrate = {0, 100, 200, 300};    //진동규칙
        builder.setVibrate(vibrate);
        builder.setAutoCancel(true); //노티를 눌러서 날라가게 하려면 true;


        //매니저는 반드시 작성 참고로 오레오에선 채널id를 등록해야함.  NotificationManagerCompat.from(this).cancel(1);  <<<이렇게 해줘도된다. 같은코드
        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);


        //bulder 가 notification 객체를 리턴한다.
        manager.notify(1, builder.build());

        //오레오 이후버전인 경우.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(new NotificationChannel("default", "기본채널", NotificationManager.IMPORTANCE_DEFAULT));
        }


    }
}
