package com.example.humanbenchmark;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.humanbenchmark.service.ServiceVisualMemory;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link visualMemoryTest4x4#newInstance} factory method to
 * create an instance of this fragment.
 */
public class visualMemoryTest4x4 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public visualMemoryTest4x4() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment visualMemoryTest4x4.
     */
    // TODO: Rename and change types and number of parameters
    public static visualMemoryTest4x4 newInstance(String param1, String param2) {
        visualMemoryTest4x4 fragment = new visualMemoryTest4x4();
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
        return inflater.inflate(R.layout.fragment_visual_memory_test4x4, container, false);
    }


    ArrayList<Button> buttons;
    ArrayList <Button> patternButtons,patternButtonsTemp;
    int level, lives;
    boolean strike;
    TextView levelView;
    ArrayList<ImageView> imageViews;
    ServiceVisualMemory serviceVisualMemory ;
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        strike = false;
        lives = visualMemoryTest4x4Args.fromBundle(getArguments()).getLives();
        level = visualMemoryTest4x4Args.fromBundle(getArguments()).getLevel();
        imageViews=new ArrayList<>();
        buttons = new ArrayList<>();
        this.declareButtonArray(view);
        drawHearts();
        levelView.setText(new String("Level: "+level));
        serviceVisualMemory = new ServiceVisualMemory();
        patternButtons = serviceVisualMemory.generatePattern(level+2,buttons);
        patternButtonsTemp = new ArrayList<>(patternButtons);
        serviceVisualMemory.start();

        addListeners();

    }
    public void addListeners(){
        for (Button b: buttons) {
            b.setOnClickListener(new View.OnClickListener() {
                                     @SuppressLint("ResourceAsColor")
                                     @Override
                                     public void onClick(View view) {
                                         if (serviceVisualMemory.isHasShown()){
                                             if (patternButtons.contains(b)){
                                                 b.setBackgroundColor(Color.rgb(196,223,230));
                                                 patternButtonsTemp.remove(b);
                                                 if (patternButtonsTemp.isEmpty()){
                                                     level++;
                                                     if (level<6){
                                                         NavDirections action =  visualMemoryTest4x4Directions.
                                                                 actionVisualMemoryTest4x4Self(level,lives);

                                                         NavHostFragment.findNavController(visualMemoryTest4x4.this)
                                                                 .navigate(action);
                                                     }
                                                     else {
                                                         NavDirections action =  visualMemoryTest4x4Directions.
                                                                 actionVisualMemoryTest4x4ToVisualMemoryTest5x5(level,lives);

                                                         NavHostFragment.findNavController(visualMemoryTest4x4.this)
                                                                 .navigate(action);

                                                     }
//                        NavDirections action =  VisualMemoryTest3x3Directions.
//                                actionVisualMemoryTest3x3ToVisualMemoryResult(level);
//                        NavHostFragment.findNavController(VisualMemoryTest3x3.this)
//                                .navigate(action);
                                                 }
                                             }
                                             else {
                                                 b.setBackgroundColor(Color.rgb(0,59,70));
                                                 if (strike){
                                                     lives--;
                                                     System.out.println("LIVES + "+ lives);
                                                     if (lives==0){
                                                         NavDirections action =  visualMemoryTest4x4Directions.
                                                                 actionVisualMemoryTest4x4ToVisualMemoryResult(level);

                                                         NavHostFragment.findNavController(visualMemoryTest4x4.this)
                                                                 .navigate(action);
                                                     }
                                                     else {
                                                         NavDirections action =  visualMemoryTest4x4Directions.
                                                                 actionVisualMemoryTest4x4Self(level,lives);

                                                         NavHostFragment.findNavController(visualMemoryTest4x4.this)
                                                                 .navigate(action);
                                                     }
                                                 }

                                                 strike = true;
                                             }
                                         }

                                     }
                                 }
            );

        }
    }

    public void drawHearts(){
        for (int i = 0; i<lives;i++){
            imageViews.get(i).setVisibility(View.VISIBLE);
        }
    }

    public void declareButtonArray(View view){
        this.buttons = new ArrayList<>();
        this.buttons.add(view.findViewById(R.id.visualButton_4x4_1));
        this.buttons.add(view.findViewById(R.id.visualButton_4x4_2));
        this.buttons.add(view.findViewById(R.id.visualButton_4x4_3));
        this.buttons.add(view.findViewById(R.id.visualButton_4x4_4));
        this.buttons.add(view.findViewById(R.id.visualButton_4x4_5));
        this.buttons.add(view.findViewById(R.id.visualButton_4x4_6));
        this.buttons.add(view.findViewById(R.id.visualButton_4x4_7));
        this.buttons.add(view.findViewById(R.id.visualButton_4x4_8));
        this.buttons.add(view.findViewById(R.id.visualButton_4x4_9));
        this.buttons.add(view.findViewById(R.id.visualButton_4x4_10));
        this.buttons.add(view.findViewById(R.id.visualButton_4x4_11));
        this.buttons.add(view.findViewById(R.id.visualButton_4x4_12));
        this.buttons.add(view.findViewById(R.id.visualButton_4x4_13));
        this.buttons.add(view.findViewById(R.id.visualButton_4x4_14));
        this.buttons.add(view.findViewById(R.id.visualButton_4x4_15));
        this.buttons.add(view.findViewById(R.id.visualButton_4x4_16));

        this.imageViews. add(view.findViewById(R.id.heart_4x4_1));
        this.imageViews. add(view.findViewById(R.id.heart_4x4_2));
        this.imageViews. add(view.findViewById(R.id.heart_4x4_3));

        this.levelView = view.findViewById(R.id.textVisualViewLevel_4x4);



    }
}