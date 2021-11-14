package com.example.humanbenchmark;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.humanbenchmark.service.ServiceChimpTest;

import java.util.ArrayList;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChimpTest#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChimpTest extends Fragment {
    public ArrayList<Button> buttons;
    public ArrayList<Button> buttonsInUse;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ChimpTest() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChimpTest.
     */
    // TODO: Rename and change types and number of parameters
    public static ChimpTest newInstance(String param1, String param2) {
        ChimpTest fragment = new ChimpTest();
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
    int numbers,numOfStrikes;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chimp_test, container, false);
    }
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        declareButtonArray(view);

        numbers = ChimpTestArgs.fromBundle(getArguments()).getNumbers();
        numOfStrikes = ChimpTestArgs.fromBundle(getArguments()).getStrikes();

            serviceChimpTest= new ServiceChimpTest(numbers,numOfStrikes);



        currentStep =0;
        mapSequence = serviceChimpTest.generateMapSequence(numbers,buttons);
        buttonsInUse = new ArrayList<>();
        createEnviroment();
        addListeners();
    }
    public Button findById(int id){
        for (Button button : buttons){
            if (button.getId()==id)
                return button;
        }
        return null;
    }

    public void createEnviroment(){
        for (int i = 0 ; i<mapSequence.size();i++){
            Button b = findById(mapSequence.get(i));
            String seqNum = String.valueOf (i+1);
            b.setVisibility(View.VISIBLE);
            b.setText(seqNum);
            //this.buttons.get(mapSequence.get(i)).setVisibility(View.VISIBLE);
            //this.buttons.get(mapSequence.get(i)).setText(seqNum);
            buttonsInUse.add(b);


    }


    }

    public void addListeners() {
        for (Button b : buttonsInUse) {
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int idOfBut = b.getId();

                    System.out.println(" " +mapSequence.get(currentStep)+" "+currentStep+" "+b.getId());

                    if (mapSequence.get(currentStep)==idOfBut){
                        System.out.println("COOOOL");
                        b.setVisibility(View.INVISIBLE);
                        buttonsInUse.remove(0);
                        if (currentStep==0){
                            makeInvisibleText();
                        }
                        currentStep++;
                        if (currentStep==serviceChimpTest.getNumbers()){
                            NavDirections action =  ChimpTestDirections.
                                    actionChimpTestToChimpTestScore(serviceChimpTest.getNumbers(),serviceChimpTest.getStrikes());
                            NavHostFragment.findNavController(ChimpTest.this)
                                    .navigate(action);
                        }
                    }
                    else{
                        serviceChimpTest.setStrikes(serviceChimpTest.getStrikes()+1);
                        NavDirections action =  ChimpTestDirections.
                                actionChimpTestToChimpTestScore(serviceChimpTest.getNumbers(),serviceChimpTest.getStrikes()).setRepeat(true);

                        NavHostFragment.findNavController(ChimpTest.this)
                                .navigate(action);
                    }
                }
            });
        }
    }
    int currentStep;
    Map<Integer,Integer> mapSequence;
    ServiceChimpTest serviceChimpTest;

    public void makeInvisibleText(){
        for (Button b: buttonsInUse){
            b.setText("");
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
        this.buttons.add(view.findViewById(R.id.chimp17));
        this.buttons.add(view.findViewById(R.id.chimp18));
        this.buttons.add(view.findViewById(R.id.chimp19));
        this.buttons.add(view.findViewById(R.id.chimp20));
    }
}