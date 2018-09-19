package com.test.networkvulnerablilitycheck;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import javax.xml.transform.Result;

public class MainActivity extends AppCompatActivity {

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.check_start);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SearchDevice.class));
            }
        });
    }

    public void check() {



    }

    public void log(View view) {

    }

    public void update(View view) {

    }

    public void onClickIot(View view){
        Intent intent = new Intent(this, ResultActivity.class);
        startActivity(intent);
    }


}
