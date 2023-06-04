package com.example.humanbenchmark;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.humanbenchmark.service.ServiceAimTrainer;
import com.example.humanbenchmark.service.ServiceReactionTimeTest;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AimTrainer#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AimTrainer extends Fragment {
    public ImageView targetAim;
    public TextView textViewInfo;
    public TextView textViewRemaining;
    ConstraintLayout constraintLayout;
    ConstraintSet constraintSet;
    int widthLayout, heightLayout;
    ServiceAimTrainer serviceAimTrainer;
    public ConstraintLayout.LayoutParams params;
    Button saveResult;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AimTrainer() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AimTrainer.
     */
    // TODO: Rename and change types and number of parameters
    public static AimTrainer newInstance(String param1, String param2) {
        AimTrainer fragment = new AimTrainer();
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
        return inflater.inflate(R.layout.fragment_aim_trainer, container, false);
    }


    FirebaseAuth firebaseAuth;
    FirebaseFirestore fStore;
    public ImageView infoView;
    long result;

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        infoView = view.findViewById(R.id.infoAim);

        targetAim=  view.findViewById(R.id.targetAim);
        textViewInfo= view.findViewById(R.id.textView4);
        textViewRemaining = view.findViewById(R.id.textView3);
        constraintLayout =  view.findViewById(R.id.constraintLayout);
        saveResult = view.findViewById(R.id.saveAimResult);
        constraintSet = new ConstraintSet();
        heightLayout=  getViewMeasure(view,true);
        widthLayout = getViewMeasure(view,false);
        serviceAimTrainer = new ServiceAimTrainer(this);
        //System.out.println("ONheight+++++++++++++++" + getViewMeasure(view,true));
        //System.out.println("ONwidth+++++++++++++++" + getViewMeasure(view,false));

        //serviceRTT = new ServiceReactionTimeTest(this);
        addListeners();
    }
    public void addListeners(){

        infoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NavDirections action =  AimTrainerDirections.
                        actionAimTrainerToInfoPage(true,"aimTrainer_results");

                NavHostFragment.findNavController(AimTrainer.this)
                        .navigate(action);


            }
        });

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
                        DocumentReference documentReference = fStore.collection("aimTrainer_results").document(uniqueID);
                        Map<String, Object> user = new HashMap<>();
                        user.put("result", result);
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


        targetAim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (serviceAimTrainer.getStateService()>=0&&serviceAimTrainer.getStateService()<30){
                    System.out.println("tyta");
                    synchronized (serviceAimTrainer){
                        serviceAimTrainer.notifyAll();
                    }


                }
                if (serviceAimTrainer.getStateService()==-1){

                textViewInfo.setVisibility(View.INVISIBLE);
                saveResult.setVisibility(View.GONE);
                    infoView.setVisibility(View.GONE);
                 params = (ConstraintLayout.LayoutParams)targetAim.getLayoutParams();
                // serviceAimTrainer.setState(0);
                 serviceAimTrainer.start();
                }

                Random random = new Random();
                if (random.nextBoolean()){
                    params.setMargins(random.nextInt(widthLayout-340), random.nextInt(heightLayout+100-12)+12, 0, 0);
                }
                else
                {
                    params.setMargins(0, random.nextInt(heightLayout-200-12)+12, random.nextInt(widthLayout-340), 0);
                }
            //    params.setMargins(0, 1247, 0, 0);


                if (serviceAimTrainer.getStateService()==29){
                    params.setMargins(0, 12, 0, 60);
                    textViewInfo.setVisibility(View.VISIBLE);
                    infoView.setVisibility(View.VISIBLE);
                    saveResult.setVisibility(View.VISIBLE);
                    result = serviceAimTrainer.allTime/30;
                    textViewRemaining.setText(serviceAimTrainer.getWynik());
                    serviceAimTrainer = new ServiceAimTrainer(AimTrainer.this);

                }

                //substitute parameters for left, top, right, bottom
                targetAim.setLayoutParams(params);
//                String remaining = "Remaining: "+ (30-serviceAimTrainer.getStateService());
//                textViewRemaining.setText(remaining);

            }
        });


    }
    public static int getViewMeasure(View view,boolean flag) {
        WindowManager wm =
                (WindowManager) view.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        int deviceWidth;

        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2){
            Point size = new Point();
            display.getSize(size);
            deviceWidth = size.x;
        } else {
            deviceWidth = display.getWidth();
        }

        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(deviceWidth, View.MeasureSpec.AT_MOST);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(widthMeasureSpec, heightMeasureSpec);
        if (flag){
           // System.out.println("Height");
            return view.getMeasuredHeight();
        }
      // System.out.println("width");
        return view.getMeasuredWidth(); //        view.getMeasuredWidth();
    }
}