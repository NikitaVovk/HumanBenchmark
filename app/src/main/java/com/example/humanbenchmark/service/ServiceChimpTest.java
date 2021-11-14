package com.example.humanbenchmark.service;

import android.widget.Button;

import androidx.versionedparcelable.VersionedParcelize;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ServiceChimpTest {

    int numbers , strikes;

    public int getNumbers() {
        return numbers;
    }

    public void setNumbers(int numbers) {
        this.numbers = numbers;
    }

    public int getStrikes() {
        return strikes;
    }

    public void setStrikes(int strikes) {
        this.strikes = strikes;
    }

    public ServiceChimpTest(int numbers, int strikes) {
        this.numbers = numbers;
        this.strikes = strikes;
    }

    public  Map<Integer,Integer> generateMapSequence(int numbers, ArrayList<Button>buttons){
        Map<Integer,Integer> mapSequence = new HashMap<>();
        ArrayList<Integer> tempList = new ArrayList<>();

        for (int i = 0 ; i < numbers;i++){

            int random ;
            do {
                random =  new Random().nextInt(20);
            }while (tempList.contains(buttons.get(random).getId()));
            tempList.add(buttons.get(random).getId());

            mapSequence.put(i,buttons.get(random).getId());


        }
        System.out.println("RETURNING MAP"+mapSequence);
        return mapSequence;
    }

}
