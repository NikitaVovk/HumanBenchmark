package com.example.humanbenchmark;

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
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NumberMemoryResultFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NumberMemoryResultFragment extends Fragment {
    TextView correctNum, answerNum, level;
    Button nextBut, tryAgainBut;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NumberMemoryResultFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NumberMemoryResultFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NumberMemoryResultFragment newInstance(String param1, String param2) {
        NumberMemoryResultFragment fragment = new NumberMemoryResultFragment();
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
        return inflater.inflate(R.layout.fragment_number_memory_result, container, false);
    }
    int reachedLvl;
    String numC , numA;
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tryAgainBut = view.findViewById(R.id.chimpTryAgain);
        nextBut = view.findViewById(R.id.chimpNext);
        correctNum = view.findViewById(R.id.iloscNum);
        answerNum = view.findViewById(R.id.strikes);
        level = view.findViewById(R.id.resultLvlTxt);
         reachedLvl = NumberMemoryResultFragmentArgs.fromBundle(getArguments()).getLevel();
         numC =NumberMemoryResultFragmentArgs.fromBundle(getArguments()).getCorrectNumber();
         numA =  NumberMemoryResultFragmentArgs.fromBundle(getArguments()).getAnswerNumber();
         correctNum.setText(numC);
        answerNum.setText(numA);
        level.setText(new String("Level "+reachedLvl));

         if (numC.equals(numA)){
             nextBut.setVisibility(View.VISIBLE);

         }
         else {
             tryAgainBut.setVisibility(View.VISIBLE);
             answerNum.setTextColor(Color.RED);
         }
        addListeners();
    }
    public void addListeners(){
        nextBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                NavDirections action =  NumberMemoryResultFragmentDirections.
                        actionNumberMemoryResultFragmentToNumberMemoryTest2().setLvl(++reachedLvl);
                NavHostFragment.findNavController(NumberMemoryResultFragment.this)
                        .navigate(action);
            }
        });
        tryAgainBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                NavDirections action =  NumberMemoryResultFragmentDirections.
                        actionNumberMemoryResultFragmentToNumberMemoryStart();
                NavHostFragment.findNavController(NumberMemoryResultFragment.this)
                        .navigate(action);
            }
        });

    }
}