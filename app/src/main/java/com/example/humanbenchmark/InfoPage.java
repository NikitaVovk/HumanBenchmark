package com.example.humanbenchmark;

import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidplot.xy.CatmullRomInterpolator;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYGraphWidget;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.example.humanbenchmark.service.ServiceInfoPage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InfoPage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InfoPage extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public InfoPage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InfoPage.
     */
    // TODO: Rename and change types and number of parameters
    public static InfoPage newInstance(String param1, String param2) {
        InfoPage fragment = new InfoPage();
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
        return inflater.inflate(R.layout.fragment_info_page, container, false);
    }

    ArrayList<Integer> tempList;
    public ArrayList<Integer> getRepetitionList(ArrayList<Integer> results){
         ArrayList<Integer> repetition  = new ArrayList<>();
          tempList  = new ArrayList<>();
         for (Integer i : results){
             if (!tempList.contains(i)){
             tempList.add(i);
             repetition.add(1);
             }
             else {

                 int howMuch = repetition.get(repetition.size()-1)+1;
                 repetition.remove(repetition.size()-1);
                 repetition.add(howMuch);
             }
         }
         results = tempList;
         return repetition;
     }
    FirebaseFirestore fStore;
    ServiceInfoPage serviceInfoPage;
    XYPlot plot;
    boolean isTime;
    String collectionName;
    ProgressBar progressBar;

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView about = view.findViewById(R.id.aboutTextView);
        progressBar =  view.findViewById(R.id.progressBarInfo);

        isTime =  InfoPageArgs.fromBundle(getArguments()).getIsTime();
        collectionName = InfoPageArgs.fromBundle(getArguments()).getCollectionName();

        plot = view.findViewById(R.id.plot);

        serviceInfoPage= new ServiceInfoPage(this);

        ArrayList<Integer> results = new ArrayList<>();

        fStore = FirebaseFirestore.getInstance();

        DocumentReference documentReference =     fStore.collection("info_about_test").document(collectionName);
        documentReference.addSnapshotListener(getActivity(), new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                about.setText(value.getString("aboutText").replaceAll("\\n", "\n"));
            }
        });
        CollectionReference collectionReference = fStore.collection(collectionName);
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){

                    List<DocumentSnapshot> documentSnapshotList= task.getResult().getDocuments();
                  //  System.out.println("snap__+ :"+documentSnapshotList);
                    for (DocumentSnapshot ds:documentSnapshotList){
                        results.add( ds.getLong("result").intValue());

                    }
                    Collections.sort(results);

                    ArrayList <Integer> allDomains, resultList;
                    if (isTime){
                        allDomains  = serviceInfoPage.generateDomainsForReaction(700);
                        resultList= serviceInfoPage.roundSpeedResults(results);
                    }
                    else {
                        allDomains = serviceInfoPage.generateDomainsForLevel(results.get(0), results.get(results.size() - 1));
                        resultList = results;
                    }
                    System.out.println("TEST FUNCTION111: "+ results);
                    System.out.println("TEST FUNCTION: "+ resultList);
                    System.out.println("TEST FUNCTION: "+ allDomains);
                    System.out.println("TEST FUNCTION: "+ serviceInfoPage.generateSeriesForReaction(serviceInfoPage.roundSpeedResults(results),allDomains));


//                    synchronized (InfoPage.this){
//                        InfoPage.this.notifyAll();
//                    }
                    //  getRepetitionList(results);
                   // ArrayList <Integer> series1 = getRepetitionList(results);
                   // System.out.println("YYYYYYYY: "+ series1);
                    //System.out.println("THIS IS RESULT LIST : "+ tempList);



                    XYSeries series = new SimpleXYSeries(serviceInfoPage.generateSeriesForReaction(resultList,allDomains),SimpleXYSeries.ArrayFormat.Y_VALS_ONLY,"Series1");
                    LineAndPointFormatter seriesFormat = new LineAndPointFormatter(Color.rgb(7,87,91),Color.rgb(7,87,91),null,null);
                    Paint paint = seriesFormat.getLinePaint();
                    paint.setStrokeWidth(8);
                    seriesFormat.setLinePaint(paint);
                    seriesFormat.setInterpolationParams(new CatmullRomInterpolator.Params(10,CatmullRomInterpolator.Type.Centripetal));

                    plot.addSeries(series,seriesFormat);

                    plot.getGraph().getBackgroundPaint().setColor(Color.rgb(196,223,230));
                    plot.getGraph().getGridBackgroundPaint().setColor(Color.rgb(196,223,230));
                    plot.getGraph().getDomainGridLinePaint().setColor(Color.rgb(196,223,230));
                    //plot.getGraph().setMarginLeft(0);
                  //  plot.getGraph().get;

                    //plot.getLayoutManager().remove(plot.getLegend());
                  //
                    //  plot.getLayoutManager().setDrawMarginsEnabled(false);

                    //plot.getGraph().setMargins(0.0f,0.0f,0.0f,0.0f);


                    plot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).setFormat(new Format() {
                        @Override
                        public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
                            int i = Math.round(((Number)obj).floatValue());

                            return toAppendTo.append(allDomains.get(i));
                        }

                        @Override
                        public Object parseObject(String source, ParsePosition pos) {
                            return null;
                        }
                    });
//                    plot.getGraph().refreshLayout();
//                    plot.getLayoutManager().refreshLayout();
                    plot.redraw();
                    progressBar.setVisibility(View.GONE);
                    plot.setVisibility(View.VISIBLE);
                }

            }
        });
        serviceInfoPage.start();
        System.out.println("THIS IS RESULT LIST : "+ tempList);









    }

    public  void plot(ArrayList<Integer> domains,ArrayList<Integer> series){
        this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {


            }
        });
    }
}