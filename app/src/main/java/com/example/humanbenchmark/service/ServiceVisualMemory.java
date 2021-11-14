package com.example.humanbenchmark.service;

import android.graphics.Color;
import android.widget.Button;

import com.example.humanbenchmark.SequenceMemory;
import com.example.humanbenchmark.VisualMemoryTest3x3;

import java.util.ArrayList;
import java.util.Random;

public class ServiceVisualMemory extends Thread{
    ArrayList<Integer> buttonSequence;
    ArrayList<Button> buttonArrayList;
  //  VisualMemoryTest3x3 handler;
    boolean hasShown;

    public boolean isHasShown() {
        return hasShown;
    }

    public ServiceVisualMemory( ) {
        buttonArrayList= new ArrayList<>();
        //this.handler = handler;
        hasShown = false;
    }
    public  ArrayList<Button> generatePattern(int patternSize, ArrayList<Button>buttons){
        if (buttonSequence==null)
            buttonSequence = new ArrayList<>();

        ArrayList<Integer> tempList = new ArrayList<>();

        for (int i = 0 ; i < patternSize;i++){

            int random ;
            do {
                random =  new Random().nextInt(buttons.size());
            }while (tempList.contains(buttons.get(random).getId()));
            tempList.add(buttons.get(random).getId());

            buttonSequence.add(buttons.get(random).getId());

            buttonArrayList.add(buttons.get(random));

        }
        System.out.println("RETURNING Button Sequence : "+buttonSequence);

        return buttonArrayList;
    }

    @Override
    public void run(){
        synchronized (this){
            try {
                this.wait(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for (Button b  : buttonArrayList){

            b.setBackgroundColor(Color.WHITE);
        }
            synchronized (this){
                try {
                    this.wait(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        for (Button b  : buttonArrayList){
            b.setBackgroundColor(Color.rgb(102, 165, 173));
        }

        hasShown = true;
    }

}