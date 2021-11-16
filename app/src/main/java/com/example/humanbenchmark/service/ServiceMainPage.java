package com.example.humanbenchmark.service;

import java.util.ArrayList;

public class ServiceMainPage {

    public float getAvarage(ArrayList<Integer> list){
        int sum = 0;
        for (Integer item: list){
            sum+=item;
        }
        return (float)sum/list.size();
    }
    public float getPercentile(ArrayList<Integer> list, float av){
        int licz = 0 ;
        for ( int i = 0; i<list.size();i++){

        if (av<list.get(i))
                break;
            licz++;
        }
        System.out.println(list+"\t"+licz);


        return (float)((licz)*100)/(list.size());
    }
}
