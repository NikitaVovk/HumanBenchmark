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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link visualMemoryResult#newInstance} factory method to
 * create an instance of this fragment.
 */
public class visualMemoryResult extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public visualMemoryResult() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment visualMemoryResult.
     */
    // TODO: Rename and change types and number of parameters
    public static visualMemoryResult newInstance(String param1, String param2) {
        visualMemoryResult fragment = new visualMemoryResult();
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
        return inflater.inflate(R.layout.fragment_visual_memory_result, container, false);
    }

    TextView level;
    Button  tryAgain, saveResult;
    // ServiceChimpTest serviceChimpTest;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore fStore;

    int levelReached, numOfStrikes;
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        level = view.findViewById(R.id.vm_textView_result_level);
        tryAgain = view.findViewById(R.id.vm_tryAgain);
        saveResult =  view.findViewById(R.id.saveVisualMemResult);

        levelReached = visualMemoryResultArgs.fromBundle(getArguments()).getLevel();

        level.setText(new String("Level: "+levelReached));


        saveResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("INSIDE SAVING");
                firebaseAuth = FirebaseAuth.getInstance();
                fStore = FirebaseFirestore.getInstance();
                if (firebaseAuth.getCurrentUser() == null) {

                    Toast.makeText(getActivity(), "You have to login to save your result !!!", Toast.LENGTH_SHORT).show();
                } else {
                   //for (int i = 0; i < 25; i++) {
                       // System.out.println(i);
                        String uniqueID = UUID.randomUUID().toString();
                        DocumentReference documentReference = fStore.collection("visualMemory_results").document(uniqueID);
                        Map<String, Object> user = new HashMap<>();
                        user.put("result", levelReached);
                        user.put("userID", firebaseAuth.getCurrentUser().getUid());
                        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getActivity(), "Saved", Toast.LENGTH_SHORT).show();
                            }
                        });
                  //  }
                    saveResult.setVisibility(View.INVISIBLE);

                }
            }
        });

        tryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                NavDirections action =  visualMemoryResultDirections.
                        actionVisualMemoryResultToVisualMemoryStart();
                NavHostFragment.findNavController(visualMemoryResult.this)
                        .navigate(action);
            }
        });

    }

}