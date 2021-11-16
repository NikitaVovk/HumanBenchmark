package com.example.humanbenchmark;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.humanbenchmark.service.ServiceMainPage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainProfilePage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainProfilePage extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MainProfilePage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainProfilePage.
     */
    // TODO: Rename and change types and number of parameters
    public static MainProfilePage newInstance(String param1, String param2) {
        MainProfilePage fragment = new MainProfilePage();
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
        return inflater.inflate(R.layout.fragment_main_profile_page, container, false);
    }

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    String userId;
    private static final DecimalFormat df = new DecimalFormat("0.00");

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView textView = view.findViewById(R.id.userName);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();

        DocumentReference documentReference =     firebaseFirestore.collection("users").document(userId);
        documentReference.addSnapshotListener(getActivity(), new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                textView.setText(value.getString("uName"));
            }
        });

        ServiceMainPage serviceMainPage = new ServiceMainPage();

        CollectionReference collectionReference = firebaseFirestore.collection("visualMemory_results");
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    ArrayList<Integer> resultsUser = new ArrayList<>();
                    ArrayList<Integer> resultsAll = new ArrayList<>();

                    List<DocumentSnapshot> documentSnapshotList= task.getResult().getDocuments();
                    //  System.out.println("snap__+ :"+documentSnapshotList);
                    for (DocumentSnapshot ds:documentSnapshotList){
                        resultsAll.add( ds.getLong("result").intValue());
                        if (ds.getString("userID").equals(userId))
                            resultsUser.add(ds.getLong("result").intValue());

                    }
                   TextView textViewAv =  view.findViewById(R.id.textView_22r4);
                    float avarage = serviceMainPage.getAvarage(resultsUser);
                    System.out.println("AVERAGE!!!!!!!!!!!!! "+avarage);
                    textViewAv.setText(df.format(avarage));
                    Collections.sort(resultsAll);
                     textViewAv =  view.findViewById(R.id.textView_23r4);
                    textViewAv.setText(new String(df.format(serviceMainPage.getPercentile(resultsAll,avarage))+" %"));
            }
        }});
        collectionReference = firebaseFirestore.collection("aimTrainer_results");
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    ArrayList<Integer> resultsUser = new ArrayList<>();
                    ArrayList<Integer> resultsAll = new ArrayList<>();

                    List<DocumentSnapshot> documentSnapshotList= task.getResult().getDocuments();
                    //  System.out.println("snap__+ :"+documentSnapshotList);
                    for (DocumentSnapshot ds:documentSnapshotList){
                        resultsAll.add( ds.getLong("result").intValue());
                        if (ds.getString("userID").equals(userId))
                            resultsUser.add(ds.getLong("result").intValue());

                    }
                    TextView textViewAv =  view.findViewById(R.id.textView_22r5);
                    float avarage = serviceMainPage.getAvarage(resultsUser);
                    textViewAv.setText(df.format(avarage));
                    Collections.sort(resultsAll);
                    textViewAv =  view.findViewById(R.id.textView_23r5);
                    textViewAv.setText(new String(df.format(serviceMainPage.getPercentile(resultsAll,avarage))+" %"));
                }
            }});
        collectionReference = firebaseFirestore.collection("sequenceMemory_results");
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    ArrayList<Integer> resultsUser = new ArrayList<>();
                    ArrayList<Integer> resultsAll = new ArrayList<>();
                    List<DocumentSnapshot> documentSnapshotList= task.getResult().getDocuments();
                    //  System.out.println("snap__+ :"+documentSnapshotList);
                    for (DocumentSnapshot ds:documentSnapshotList){
                        resultsAll.add( ds.getLong("result").intValue());
                        if (ds.getString("userID").equals(userId))
                            resultsUser.add(ds.getLong("result").intValue());

                    }
                    TextView textViewAv =  view.findViewById(R.id.textView_22r6);
                    float avarage = serviceMainPage.getAvarage(resultsUser);
                    textViewAv.setText(df.format(avarage));
                    Collections.sort(resultsAll);
                    textViewAv =  view.findViewById(R.id.textView_23r6);
                    textViewAv.setText(new String(df.format(serviceMainPage.getPercentile(resultsAll,avarage))+" %"));
                }
            }});
        collectionReference = firebaseFirestore.collection("chimpTest_results");
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    ArrayList<Integer> resultsUser = new ArrayList<>();
                    ArrayList<Integer> resultsAll = new ArrayList<>();
                    List<DocumentSnapshot> documentSnapshotList= task.getResult().getDocuments();
                    //  System.out.println("snap__+ :"+documentSnapshotList);
                    for (DocumentSnapshot ds:documentSnapshotList){
                        resultsAll.add( ds.getLong("result").intValue());
                        if (ds.getString("userID").equals(userId))
                            resultsUser.add(ds.getLong("result").intValue());

                    }
                    TextView textViewAv =  view.findViewById(R.id.textView_22r7);
                    float avarage = serviceMainPage.getAvarage(resultsUser);
                    textViewAv.setText(df.format(avarage));
                    Collections.sort(resultsAll);
                    textViewAv =  view.findViewById(R.id.textView_23r7);
                    textViewAv.setText(new String(df.format(serviceMainPage.getPercentile(resultsAll,avarage))+" %"));
                }
            }});
        collectionReference = firebaseFirestore.collection("numberMemory_results");
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    ArrayList<Integer> resultsUser = new ArrayList<>();
                    ArrayList<Integer> resultsAll = new ArrayList<>();
                    List<DocumentSnapshot> documentSnapshotList= task.getResult().getDocuments();
                    //  System.out.println("snap__+ :"+documentSnapshotList);
                    for (DocumentSnapshot ds:documentSnapshotList){
                        resultsAll.add( ds.getLong("result").intValue());
                        if (ds.getString("userID").equals(userId))
                            resultsUser.add(ds.getLong("result").intValue());

                    }
                    TextView textViewAv =  view.findViewById(R.id.textView_22r);
                    float avarage = serviceMainPage.getAvarage(resultsUser);
                    textViewAv.setText(df.format(avarage));
                    Collections.sort(resultsAll);
                    textViewAv =  view.findViewById(R.id.textView_23r);
                    textViewAv.setText(new String(df.format(serviceMainPage.getPercentile(resultsAll,avarage))+" %"));
                }
            }});
        collectionReference = firebaseFirestore.collection("reactionTime_results");
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    ArrayList<Integer> resultsUser = new ArrayList<>();
                    ArrayList<Integer> resultsAll = new ArrayList<>();
                    List<DocumentSnapshot> documentSnapshotList= task.getResult().getDocuments();
                    //  System.out.println("snap__+ :"+documentSnapshotList);
                    for (DocumentSnapshot ds:documentSnapshotList){
                        resultsAll.add( ds.getLong("result").intValue());
                        if (ds.getString("userID").equals(userId))
                            resultsUser.add(ds.getLong("result").intValue());

                    }
                    TextView textViewAv =  view.findViewById(R.id.textView_22r3);
                    float avarage = serviceMainPage.getAvarage(resultsUser);
                    textViewAv.setText(df.format(avarage));
                    Collections.sort(resultsAll);
                    textViewAv =  view.findViewById(R.id.textView_23r3);
                    textViewAv.setText(new String(df.format(serviceMainPage.getPercentile(resultsAll,avarage))+" %"));
                }
            }});


//
//
//
//
//
//        view.findViewById(R.id.buttonLogOut).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FirebaseAuth.getInstance().signOut();
//                NavHostFragment.findNavController(MainProfilePage.this)
//                        .navigate(R.id.action_mainProfilePage_to_loginForm);
//            }
//        });

    }
}