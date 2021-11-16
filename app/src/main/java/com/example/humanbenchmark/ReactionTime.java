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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.humanbenchmark.service.ServiceReactionTimeTest;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReactionTime#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReactionTime extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public TextView textView;
    public Button saveResult;
    public ImageView infoView;
    ServiceReactionTimeTest serviceRTT;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ReactionTime() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReactionTime.
     */
    // TODO: Rename and change types and number of parameters
    public static ReactionTime newInstance(String param1, String param2) {
        ReactionTime fragment = new ReactionTime();
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
        return inflater.inflate(R.layout.fragment_reaction_time, container, false);
    }

    FirebaseAuth firebaseAuth;
    FirebaseFirestore fStore;

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textView=  view.findViewById(R.id.textView);
        saveResult = view.findViewById(R.id.saveResult);
        infoView = view.findViewById(R.id.infoView);
        appendText("\n\nWhen the red box turns green,\nclick as quickly as you can.\nClick anywhere to start");


        serviceRTT = new ServiceReactionTimeTest(this);
        addListeners();
    }
    public void addListeners(){
        infoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NavDirections action =  ReactionTimeDirections.
                        actionReactionTimeToInfoPage(true,"reactionTime_results");

                NavHostFragment.findNavController(ReactionTime.this)
                        .navigate(action);


            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (serviceRTT.getCurrentState().equals("GREEN")){
                synchronized (serviceRTT){
                    serviceRTT.notifyAll();
                }
                }
                if (serviceRTT.getCurrentState().equals("HOME")){
                    serviceRTT = new ServiceReactionTimeTest(ReactionTime.this);
                    serviceRTT.start();
                    saveResult.setVisibility(View.INVISIBLE);
                    infoView.setVisibility(View.INVISIBLE);
                }



            }
        });
        saveResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth = FirebaseAuth.getInstance();
                fStore= FirebaseFirestore.getInstance();
                if (firebaseAuth.getCurrentUser() == null){

                    Toast.makeText(getActivity(),"You have to login to save your result !!!" ,Toast.LENGTH_SHORT).show();
                }
                else {
                    for (int i = 0 ; i < 3;i++){
                    String uniqueID = UUID.randomUUID().toString();
                    DocumentReference documentReference = fStore.collection("reactionTime_results").document(uniqueID);
                    Map<String,Object> user = new HashMap<>();
                    user.put("result",serviceRTT.testResult);
                    user.put("userID",firebaseAuth.getCurrentUser().getUid());
                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getActivity(),"Saved" ,Toast.LENGTH_SHORT).show();
                        }
                    });
                    }
                    saveResult.setVisibility(View.INVISIBLE);

                }

            }
        });


    }
    public  void setText(final String value){
        this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setText(value);

            }
        });
    }
    public  void setVisibleSave(){
        this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                saveResult.setVisibility(View.VISIBLE);
                infoView.setVisibility(View.VISIBLE);

            }
        });
    }
    public  void appendText(final String value){
        this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                SpannableString ss1=  new SpannableString(value);
//                ss1.setSpan(new RelativeSizeSpan(4f), 0,5, 0); // set size
////                textView.setTextColor(color);
////                textView.append(value);
//
                int start = textView.getText().length();
                textView.append("\n"+value);
                int end = textView.getText().length();
                Spannable spannableText = (Spannable) textView.getText();
                spannableText.setSpan(new RelativeSizeSpan(0.5f), start, end, 0);

                textView.append("\n");

            }
        });
    }
}