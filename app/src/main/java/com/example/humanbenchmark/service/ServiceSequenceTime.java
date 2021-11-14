package com.example.humanbenchmark.service;

import android.graphics.Color;

import com.example.humanbenchmark.SequenceMemory;

import java.util.ArrayList;

public class ServiceSequenceTime extends Thread{
    ArrayList<Integer> buttonSequence;
    SequenceMemory handler;
    boolean hasShown;

    public boolean isHasShown() {
        return hasShown;
    }

    public ServiceSequenceTime(ArrayList<Integer> buttonSequence, SequenceMemory handler) {
        this.buttonSequence = buttonSequence;
        this.handler = handler;
        hasShown = false;
    }
    @Override
    public void run(){
        for (Integer i : buttonSequence){
            synchronized (this){
                try {
                    this.wait(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            handler.buttons.get(i).setBackgroundColor(Color.WHITE);
            synchronized (this){
                try {
                    this.wait(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            handler.buttons.get(i).setBackgroundColor(Color.rgb(102, 165, 173));
        }
        hasShown = true;
    }

}
