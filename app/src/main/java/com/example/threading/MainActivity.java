package com.example.threading;

import androidx.appcompat.app.AppCompatActivity;
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
    ImageView yellow, green, red;
    TextView Indicator;
    private volatile boolean stopThreadflag = false;
    private Handler mainHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        start = findViewById(R.id.startButton);
        stop = findViewById(R.id.stopButton);
        yellow = findViewById(R.id.imgYellow);
        green = findViewById(R.id.imgRed);
        red = findViewById(R.id.imgRed);
        Indicator = findViewById(R.id.indicator);
        green.setVisibility(View.GONE);
        yellow.setVisibility(View.GONE);
        red.setVisibility(View.GONE);
        clear = findViewById(R.id.clrButton);

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                green.setVisibility(View.GONE);
                yellow.setVisibility(View.GONE);
                red.setVisibility(View.GONE);
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
        green.setVisibility(View.GONE);
        yellow.setVisibility(View.GONE);
        red.setVisibility(View.GONE);

    }

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
                if(i == 3){
                    mainHandler.post(() -> {
                        red.setVisibility(View.VISIBLE);
                        Indicator.setText("STOP!");
                    });
                }

                if(i == 6){
                    mainHandler.post(() -> {
                        yellow.setVisibility(View.VISIBLE);
                        Indicator.setText("Faster!");
                    });

                if(i == 9){
                    mainHandler.post(() -> {
                        green.setVisibility(View.VISIBLE);
                        Indicator.setText("GO!");
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
}