package com.example.threading;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import kotlinx.coroutines.internal.ThreadSafeHeap;

public class MainActivity extends AppCompatActivity {

    Button start, stop, clear;
    TextView Indicator;
    private volatile boolean stopThreadflag = false;
    private final Handler mainHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        start = findViewById(R.id.startButton);
        stop = findViewById(R.id.stopButton);
        Indicator = findViewById(R.id.indicator);
        clear = findViewById(R.id.clrButton);

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Indicator.setVisibility(View.INVISIBLE);
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startThread(10);
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopThread();
            }
        });

    }

    public void startThread(int sec){
        TrafficRunnable runnable = new TrafficRunnable(10);
        new Thread(runnable).start();
            Indicator.setVisibility(View.VISIBLE);

    }

    @SuppressLint("SetTextI18n")
    public void stopThread(){
        stopThreadflag=true;
        Indicator.setText("Traffic Light Jammed");
    }

    class TrafficRunnable implements Runnable{
        int seconds;

        TrafficRunnable(int seconds){
            this.seconds = seconds;
        }

        @Override
        public void run() {
            for(int i = 0; i < seconds; i++){
                if(stopThreadflag){
                    return;
                }
                else if(i == 3){
                    mainHandler.post(() -> {
                        Indicator.setText("STOP!");
                    });
                }

                else if(i == 6) {
                    mainHandler.post(() -> {
                        Indicator.setText("GO!");
                    });
                }
                else if(i == 9){
                    mainHandler.post(() -> {
                        Indicator.setText("FASTER!");
                    });
                }
                Log.d("THREAD ACTIVITY", "START THREAD: " + i);
                try{
                    Thread.sleep(1000);
                }
                    catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        }
    }