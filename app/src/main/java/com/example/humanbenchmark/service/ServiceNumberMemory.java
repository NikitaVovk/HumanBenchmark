package com.example.humanbenchmark.service;

import com.example.humanbenchmark.NumberMemoryTest;

import java.util.Random;

public class ServiceNumberMemory  extends Thread{

    NumberMemoryTest numberMemoryTest;
    int level;

    public ServiceNumberMemory(NumberMemoryTest numberMemoryTest, int level) {
        this.numberMemoryTest = numberMemoryTest;
        this.level = level;
    }
    public String generateNumberString( ){
        String value = "";
        for (int i =0 ; i <level; i++){
            value+=new Random().nextInt(9)+1;

        }
        return value;
    }


    @Override
    public void run(){

        long timeOut = (level*1000)/10;

        for (int i = 0; i < 10;i++) {
            synchronized (this) {
                try {
                    this.wait(timeOut);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            numberMemoryTest.makeTimer();
//            numberMemoryTest.timer.setText(numberMemoryTest.timer.getText().toString().
//                    substring(0, numberMemoryTest.timer.getText().toString().length() - 1));
        }
        numberMemoryTest.makeAnswerView();
    }
}
