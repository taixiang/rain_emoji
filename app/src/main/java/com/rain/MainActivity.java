package com.rain;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private RainView rainView;
    private Button btnCake;
    private Button btnDog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rainView = findViewById(R.id.testView);
        btnCake = findViewById(R.id.btn_cake);
        btnDog = findViewById(R.id.btn_dog);
        btnCake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rainView.setImgResId(R.mipmap.cake);
                rainView.start(true);
            }
        });
        btnDog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rainView.setImgResId(R.mipmap.dog);
                rainView.start(true);
            }
        });


    }
}
