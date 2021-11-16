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
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NumberMemoryResultFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NumberMemoryResultFragment extends Fragment {
    TextView correctNum, answerNum, level;
    Button nextBut, tryAgainBut, saveResult;

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
    FirebaseAuth firebaseAuth;
    FirebaseFirestore fStore;
    String numC , numA;
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tryAgainBut = view.findViewById(R.id.chimpTryAgain);
        saveResult = view.findViewById(R.id.saveNumberMemResult);
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
             saveResult.setVisibility(View.VISIBLE);
             answerNum.setTextColor(Color.RED);
         }
        addListeners();
    }
    public void addListeners(){
        saveResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth = FirebaseAuth.getInstance();
                fStore = FirebaseFirestore.getInstance();
                if (firebaseAuth.getCurrentUser() == null) {

                    Toast.makeText(getActivity(), "You have to login to save your result !!!", Toast.LENGTH_SHORT).show();
                } else {
                    for (int i = 0; i < 3; i++) {
                        String uniqueID = UUID.randomUUID().toString();
                        DocumentReference documentReference = fStore.collection("numberMemory_results").document(uniqueID);
                        Map<String, Object> user = new HashMap<>();
                        user.put("result", reachedLvl);
                        user.put("userID", firebaseAuth.getCurrentUser().getUid());
                        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getActivity(), "Saved", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    saveResult.setVisibility(View.INVISIBLE);

                }
            }
        });
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