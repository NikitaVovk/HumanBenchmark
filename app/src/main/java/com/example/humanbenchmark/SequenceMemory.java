package com.example.humanbenchmark;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.humanbenchmark.service.ServiceSequenceTime;

import java.util.ArrayList;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SequenceMemory#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SequenceMemory extends Fragment {
    ArrayList<Integer> buttonSequence;
    ArrayList<Integer> temporarySequence;
   public ArrayList<Button> buttons;
   ServiceSequenceTime serviceSequenceTime;
   TextView textView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SequenceMemory() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SequenceMemory.
     */
    // TODO: Rename and change types and number of parameters
    public static SequenceMemory newInstance(String param1, String param2) {
        SequenceMemory fragment = new SequenceMemory();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sequence_memory, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        buttons = new ArrayList<>();
        buttons.add(view.findViewById(R.id.visualButton_5x5_2)) ;
        buttons.add(view.findViewById(R.id.visualButton_5x5_3)) ;
        buttons.add(view.findViewById(R.id.visualButton_5x5_4)) ;
        buttons.add(view.findViewById(R.id.visualButton_3x3_4)) ;
        buttons.add(view.findViewById(R.id.visualButton_3x3_5)) ;
        buttons.add(view.findViewById(R.id.visualButton_3x3_6)) ;
        buttons.add(view.findViewById(R.id.visualButton_3x3_7)) ;
        buttons.add(view.findViewById(R.id.visualButton_3x3_8)) ;
        buttons.add(view.findViewById(R.id.visualButton_3x3_9)) ;
        textView = view.findViewById(R.id.textVisualViewLevel_5x5);
        level = 1;
        //System.out.println( getResources().getIdentifier("R","id","sequenceButton1"));;
        this.generateSequence();
        serviceSequenceTime= new ServiceSequenceTime(buttonSequence,this);
        serviceSequenceTime.start();
        this.temporarySequence = new ArrayList<>(buttonSequence);

        addListeners();
    }
    public  void generateSequence(){
        if (buttonSequence==null)
            buttonSequence = new ArrayList<>();

         buttonSequence.add(new Random().nextInt(9));
    }
    int level;
    public void addListeners(){
        for (Button b: buttons) {
        b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (serviceSequenceTime.isHasShown()) {

                        if (getIdOfPressedButton(b.getId()) == temporarySequence.get(0)) {
                            temporarySequence.remove(0);
                            //colorButton(Color.rgb(255,255,255),b);

                            if (temporarySequence.isEmpty()) {
                                SequenceMemory.this.generateSequence();
                                serviceSequenceTime = new ServiceSequenceTime(buttonSequence, SequenceMemory.this);
                                serviceSequenceTime.start();
                                SequenceMemory.this.temporarySequence = new ArrayList<>(buttonSequence);
                                level++;
                                String levelStr = "Level: " + level;
                                textView.setText(levelStr);

                            }
                        } else {
                            b.setBackgroundColor(Color.RED);

                            System.out.println("GIviN LVLV "+level);
                            NavDirections action =  SequenceMemoryDirections.actionSequenceMemoryToSequenceMemoryStart().setReachedLvl(level);
                            NavHostFragment.findNavController(SequenceMemory.this)
                                    .navigate(action);
                        }
                        System.out.println("ID++++++" + getIdOfPressedButton(b.getId()));

                    }
                }
            });

            b.setOnTouchListener(new View.OnTouchListener() {

                @SuppressLint("ClickableViewAccessibility")
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        // reset the background color here
                      //  b.setBackgroundColor(Color.rgb(102, 165, 173));
                        changeToDefault(b);

                    }else{
                        // Change the background color here
                        b.setBackgroundColor(Color.WHITE);

                    }
                    return false;
                }
            });
        }
    }
    public  void changeToDefault( Button button){
        this.getActivity().runOnUiThread(new Runnable() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void run() {
synchronized (button){
    try {
        button.wait(300);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
}
                button.setBackgroundColor(Color.rgb(102, 165, 173));


            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    public int getIdOfPressedButton(int id){
        switch (id){
            case R.id.visualButton_5x5_2:
                return 0;
            case R.id.visualButton_5x5_3:
                return 1;

            case R.id.visualButton_5x5_4:
                return 2;
            case R.id.visualButton_3x3_4:
                return 3;
            case R.id.visualButton_3x3_5:
                return 4;
            case R.id.visualButton_3x3_6:
                return 5;
            case R.id.visualButton_3x3_7:
                return 6;
            case R.id.visualButton_3x3_8:
                return 7;
            case R.id.visualButton_3x3_9:
                return 8;
        }
        return -1;
    }
}