package com.example.humanbenchmark.service;

import com.example.humanbenchmark.InfoPage;

import java.util.ArrayList;

public class ServiceInfoPage extends Thread{
    InfoPage handler;
    ArrayList<Integer> series,domains;

    public ArrayList<Integer> getSeries() {
        return series;
    }

    public void setSeries(ArrayList<Integer> series) {
        this.series = series;
    }

    public ArrayList<Integer> getDomains() {
        return domains;
    }

    public void setDomains(ArrayList<Integer> domains) {
        this.domains = domains;
    }

    public ServiceInfoPage(InfoPage handler) {
        this.handler = handler;
    }

    public ArrayList<Integer> generateDomainsForReaction(int bound){
        ArrayList<Integer> generatedArray = new ArrayList<>();
        for (int i = 0; i<bound;i+=25){
            generatedArray.add(i);
        }
        return generatedArray;
    }
    public ArrayList<Integer> generateDomainsForLevel(int lowerBound,int upperBound){
        ArrayList<Integer> generatedArray = new ArrayList<>();
        for (int i = lowerBound-1; i<=upperBound+1;i++){
            generatedArray.add(i);
        }
        return generatedArray;
    }

    public ArrayList<Integer> roundSpeedResults(ArrayList<Integer> results){
        ArrayList<Integer> roundedValues = new ArrayList<>();
        for (Integer res : results){
            int modulo = res%25;
            if (modulo<(25-modulo))
            roundedValues.add(res-modulo);
            else
                roundedValues.add(res+(25-modulo));
        }

        return roundedValues;
    }


    public ArrayList<Integer> generateSeriesForReaction(ArrayList<Integer> results,ArrayList<Integer> generatedDomains){
        ArrayList<Integer> generatedArray = generatedDomains;
        ArrayList<Integer> domains = new ArrayList<>();

        int j = 0;
        for (int i = 0; i<generatedArray.size();i++){
            int licznik= 0;

                    while (!results.isEmpty()&&results.get(0).equals(generatedArray.get(i))){
                        results.remove(0);
                        licznik++;
                        j++;
                    }

                    domains.add(licznik);


        }
        return domains;
    }
        @Override
    public void run(){

        synchronized (handler){
            try {
                handler.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
            System.out.println("HEREEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE");
        handler.plot(domains,series);

    }

}
