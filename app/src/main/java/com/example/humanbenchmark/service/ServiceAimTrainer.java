package com.example.humanbenchmark.service;

import androidx.annotation.NonNull;

import com.example.humanbenchmark.AimTrainer;

import java.util.ArrayList;

public class ServiceAimTrainer  extends Thread {

    public int state = -1;
    ArrayList<Long> arrayListOfTimes;
    AimTrainer handler;
    String wynik;
   public long allTime;

    public String getWynik() {
        return wynik;
    }

    public void setWynik(String wynik) {
        this.wynik = wynik;
    }

    public boolean isServed;

    public ServiceAimTrainer(   AimTrainer handler) {
        this.state = -1;
        this.arrayListOfTimes = new ArrayList<>();
        allTime= 0 ;
        this.handler = handler;
        this.isServed = false;
        this.wynik = "";

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
            //System.out.println("STATE : "+ state);
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
            System.out.println("WYNIIIIIK!!!!: " + allTime);
            //lista.add(testResult);
        }
       // System.out.println("KONIEEEC+ "+lista+"\n"+lista.size());
        isServed= true;
         wynik = "Avarage time per target: "+ (allTime/30)+"ms";
        System.out.println("WYNIIIIIK!!!!: " + wynik);
//        handler.textViewRemaining.setText(wynik);
        //wynik wyswietlic w aimtrainer w button gdzie if(==29)!!!


    }
}
