package com.example.humanbenchmark;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VisualMemoryStart#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VisualMemoryStart extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public VisualMemoryStart() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VisualMemoryStart.
     */
    // TODO: Rename and change types and number of parameters
    public static VisualMemoryStart newInstance(String param1, String param2) {
        VisualMemoryStart fragment = new VisualMemoryStart();
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
        return inflater.inflate(R.layout.fragment_visual_memory_start, container, false);
    }

    public ImageView infoView;
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        infoView =  view.findViewById(R.id.infoVisual);

        infoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NavDirections action =  VisualMemoryStartDirections.
                        actionVisualMemoryStartToInfoPage(false,"visualMemory_results");

                NavHostFragment.findNavController(VisualMemoryStart.this)
                        .navigate(action);


            }
        });


        view.findViewById(R.id.startVisualMemory).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                System.out.println("CLIIIIIIICK");
                NavHostFragment.findNavController(VisualMemoryStart.this)
                        .navigate(R.id.action_visualMemoryStart_to_visualMemoryTest3x3);
            }
        });
    }
}