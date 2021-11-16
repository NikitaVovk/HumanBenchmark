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
import android.widget.TextView;
import android.widget.Toast;

import com.example.humanbenchmark.service.ServiceChimpTest;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChimpTestScore#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChimpTestScore extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ChimpTestScore() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChimpTestScore.
     */
    // TODO: Rename and change types and number of parameters
    public static ChimpTestScore newInstance(String param1, String param2) {
        ChimpTestScore fragment = new ChimpTestScore();
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
        return inflater.inflate(R.layout.fragment_chimp_test_score, container, false);
    }

//    NumberMemoryResultFragmentArgs.fromBundle(getArguments()).getLevel();
    TextView nums,strikes;
    Button next, tryAgain,saveResult;
   // ServiceChimpTest serviceChimpTest;
    int numOfNums, numOfStrikes;
    boolean repeat;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore fStore;

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    nums = view.findViewById(R.id.iloscNum);
    strikes = view.findViewById(R.id.strikes);
    next = view.findViewById(R.id.chimpNext);
    tryAgain  = view.findViewById(R.id.chimpTryAgain);
    saveResult = view.findViewById(R.id.saveChimpResult);


    numOfNums = ChimpTestScoreArgs.fromBundle(getArguments()).getNumbers();
    numOfStrikes =  ChimpTestScoreArgs.fromBundle(getArguments()).getStrikes();
    repeat =  ChimpTestScoreArgs.fromBundle(getArguments()).getRepeat();
    nums.setText(String.valueOf(numOfNums));
    strikes.setText(new String(numOfStrikes+" of 3"));
    if (numOfStrikes<3)
        next.setVisibility(View.VISIBLE);
    else {
        tryAgain.setVisibility(View.VISIBLE);
        saveResult.setVisibility(View.VISIBLE);
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
                        DocumentReference documentReference = fStore.collection("chimpTest_results").document(uniqueID);
                        Map<String, Object> user = new HashMap<>();
                        user.put("result", numOfNums);
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
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavDirections action;

                if (repeat)
                 action =  ChimpTestScoreDirections.
                        actionChimpTestScoreToChimpTest().setNumbers(numOfNums).setStrikes(numOfStrikes);
                else
                    action =  ChimpTestScoreDirections.
                            actionChimpTestScoreToChimpTest().setNumbers(numOfNums+1).setStrikes(numOfStrikes);
                NavHostFragment.findNavController(ChimpTestScore.this)
                        .navigate(action);
            }
        });
        tryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                NavDirections action =  ChimpTestScoreDirections.
                        actionChimpTestScoreToChimpTestStart();
                NavHostFragment.findNavController(ChimpTestScore.this)
                        .navigate(action);
            }
        });

    }
}