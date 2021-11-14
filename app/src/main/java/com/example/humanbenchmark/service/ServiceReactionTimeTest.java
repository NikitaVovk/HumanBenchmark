package com.example.humanbenchmark.service;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.humanbenchmark.AimTrainer;
import com.example.humanbenchmark.ReactionTime;


import java.time.LocalDateTime;
import java.util.Random;

public class ServiceReactionTimeTest extends Thread {
    ReactionTime handler;
    String currentState;


    public String getCurrentState() {
        return currentState;
    }

    public void setCurrentState(String currentState) {
        this.currentState = currentState;
    }

    public ServiceReactionTimeTest(ReactionTime handler) {
        this.handler = handler;
        currentState = "HOME";
    }
    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void run(){
        if (currentState.equals("HOME")){
            handler.textView.setBackgroundColor(Color.RED);
            handler.textView.setText("...");
            handler.appendText("Wait for Green");
            handler.textView.setTextColor(Color.WHITE);

            currentState ="RED";
            Random random = new Random();
            int timeOut = random.nextInt(1000)+4000;
            try {
                Thread.currentThread().sleep(timeOut);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            handler.textView.setBackgroundColor(Color.GREEN);
            handler.textView.setText("...");
            handler.appendText("Click!");
            currentState ="GREEN";
            long testResult = System.currentTimeMillis();

            synchronized (this)
            {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            testResult=System.currentTimeMillis()- testResult;

            handler.textView.setBackgroundColor(Color.rgb(102,165,173));
            handler.textView.setText(testResult+" ms");
            handler.appendText("Click to keep going");

            currentState ="HOME";
        }
    }
}