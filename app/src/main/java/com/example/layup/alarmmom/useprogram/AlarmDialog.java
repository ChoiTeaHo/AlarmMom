package com.example.layup.alarmmom.useprogram;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.layup.alarmmom.MainActivity;
import com.example.layup.alarmmom.R;

public class AlarmDialog extends Activity {

    private String notiMessage;
    private static final String INTENT_ACTION = "com.example.layup.alarmmom.useprogram.ALARM_START";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_alarm_dialog);

        TextView adMessage = (TextView) findViewById(R.id.message);
        Button adButton = (Button) findViewById(R.id.submit);


        /** 락스크린풀기 */
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);


        Bundle bun = getIntent().getExtras();
        notiMessage = bun.getString("notiMessage");

        adMessage.setText(notiMessage);

        adButton.setOnClickListener(new SubmitOnClickListener());

    } //절대영역



    // SubmitOnClickListenr 메소드
    private class SubmitOnClickListener implements View.OnClickListener {
        public void onClick(View v) {


            Intent main_intent = new Intent(getApplicationContext(), MainActivity.class);
            main_intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            main_intent.putExtra("stopDialogClick", "alarm stop");
            startActivity(main_intent);


            /*
            main_intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            main_intent.putExtra("stopDialogClick", "alarm stop");
            startActivity(main_intent);*/



                finish();
            }
        }



    @Override
    public void onBackPressed() {
    }

}


