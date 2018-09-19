package com.test.networkvulnerablilitycheck;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;

import javax.xml.transform.Result;

public class Progress extends AppCompatActivity{

    Handler handler = new Handler();
    int value = 0; // progressBar 값
    int add = 1; // 증가량, 방향
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress_activity);

        final Intent intent = new Intent(this, ResultActivity.class);
        final ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar);

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() { // Thread 로 작업할 내용을 구현
                while(true) {
                    value = value + add;
                    if (value>=100 || value<=0) {
                        add = -add;
                    }

                    handler.post(new Runnable() {
                        @Override
                        public void run() { // 화면에 변경하는 작업을 구현
                            pb.setProgress(value);
                            if(value == 100)
                            {
                                startActivity(intent);
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                Thread.currentThread().interrupt();
                            }
                        }
                    });

                    try {
                        Thread.sleep(100); // 시간지연
                    } catch (InterruptedException e) {    }
                } // end of while
            }
        });
        t.start(); // 쓰레드 시작
    }
}
