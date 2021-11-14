package com.example.humanbenchmark;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import android.text.Spannable;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.humanbenchmark.service.ServiceNumberMemory;
import com.example.humanbenchmark.service.ServiceSequenceTime;

import java.util.ArrayList;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NumberMemoryTest#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NumberMemoryTest extends Fragment {
   public TextView number, whatTheNumber, timer;
    EditText editText;
    Button submit;
    ServiceNumberMemory serviceNumberMemory;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NumberMemoryTest() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NumberMemoryTest.
     */
    // TODO: Rename and change types and number of parameters
    public static NumberMemoryTest newInstance(String param1, String param2) {
        NumberMemoryTest fragment = new NumberMemoryTest();
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
        return inflater.inflate(R.layout.fragment_number_memory_test, container, false);
    }
    String numberString;
    int level;

    public void makeAnswerView(){
        this.getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {

                number.setVisibility(View.INVISIBLE);
                whatTheNumber.setVisibility(View.VISIBLE);
                editText.setVisibility(View.VISIBLE);
                submit.setVisibility(View.VISIBLE);

            }
        });

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        number = view.findViewById(R.id.number);
        whatTheNumber = view.findViewById(R.id.whatNum);
        timer = view.findViewById(R.id.timer);
        editText = view.findViewById(R.id.numberAnswer);
        submit =view.findViewById(R.id.submitNumberButton);
        level = NumberMemoryTestArgs.fromBundle(getArguments()).getLvl();
        serviceNumberMemory =new ServiceNumberMemory(this,level);
        numberString = serviceNumberMemory.generateNumberString();
        number.setText(numberString);
        serviceNumberMemory.start();
      //  makeTimer(level);



        addListeners();
    }
    public  void makeTimer(){
        System.out.println("Making timer");
        this.getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {


                    timer.setText(timer.getText().toString().
                            substring(0, timer.getText().toString().length() - 1));

            }
        });
    }

    public  void generateSequence(){

    }
    public void addListeners(){
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                NavDirections action =  NumberMemoryTestDirections.
                        actionNumberMemoryTestToNumberMemoryResultFragment(level,numberString,editText.getText().toString());
                NavHostFragment.findNavController(NumberMemoryTest.this)
                        .navigate(action);
                System.out.println("CLIIIIIIICK");

            }
        });
    }
}