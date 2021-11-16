package com.example.humanbenchmark.service;

import androidx.annotation.NonNull;

import com.example.humanbenchmark.AimTrainer;

import java.util.ArrayList;

public class ServiceAimTrainer  extends Thread {

    public int state = -1;
    ArrayList<Long> arrayListOfTimes;
    AimTrainer handler;
   public long allTime;

   public boolean isServed;

    public ServiceAimTrainer(   AimTrainer handler) {
        this.state = -1;
        this.arrayListOfTimes = new ArrayList<>();
        allTime= 0 ;
        this.handler = handler;
        this.isServed = false;

    }

    public int getStateService() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public void run(){
       // ArrayList <Long> lista = new ArrayList<>();
        int licznik =0;
        while(state<29) {
            licznik++;
                state++;

                String remaining = "Remaining: "+ (30-state);
                handler.textViewRemaining.setText(remaining);
            long testResult = System.currentTimeMillis();

            synchronized (this) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


            testResult = System.currentTimeMillis() - testResult;
            allTime+=testResult;
            //lista.add(testResult);
        }
       // System.out.println("KONIEEEC+ "+lista+"\n"+lista.size());
        isServed= true;
        String wynik = "Avarage time per target: "+ (allTime/30)+"ms";
        handler.textViewRemaining.setText(wynik);


    }
}
