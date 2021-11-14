package com.example.humanbenchmark;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegistrationForm#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegistrationForm extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RegistrationForm() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegistrationForm.
     */
    // TODO: Rename and change types and number of parameters
    public static RegistrationForm newInstance(String param1, String param2) {
        RegistrationForm fragment = new RegistrationForm();
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
    EditText userName, eMail, passwd1,passwd2;
    Button buttonRegister;
    TextView loginHere;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore fStore;
    String userID;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_registration_form, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userName = view.findViewById(R.id.registrUserName);
        eMail =  view.findViewById(R.id.registrEmail);
        passwd1= view.findViewById(R.id.registrPasswd);
        passwd2= view.findViewById(R.id.registrPasswd2);
        buttonRegister= view.findViewById(R.id.buttonRegister);
        loginHere= view.findViewById(R.id.loginHere);

        firebaseAuth = FirebaseAuth.getInstance();
        fStore= FirebaseFirestore.getInstance();
        if (firebaseAuth.getCurrentUser() != null){

            NavHostFragment.findNavController(RegistrationForm.this)
                    .navigate(R.id.action_registrationForm_to_mainProfilePage);
        }

        loginHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(RegistrationForm.this)
                        .navigate(R.id.action_registrationForm_to_loginForm);
            }
        });


        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = eMail.getText().toString().trim();
                String userName1 = userName.getText().toString().trim();
                String password = passwd1.getText().toString().trim();
                String password2 = passwd2.getText().toString().trim();

                if (TextUtils.isEmpty(email)){
                    eMail.setError("Email is Required.");
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    passwd1.setError("Email is Required.");
                    return;
                }
                if (password.length()<6){
                    passwd1.setError("Password must be >=6 charakters");
                    return;
                }
                if (!password2.equals(password)){
                    passwd2.setError("Passwords must be same");
                    return;
                }

                firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(),"User Created",Toast.LENGTH_SHORT).show();
                            userID = firebaseAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fStore.collection("users").document(userID);
                            Map<String,Object> user = new HashMap<>();
                            user.put("uName",userName1);
                            user.put("eMail",email);
                            user.put("password",password);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    System.out.println("everything is fine");
                                }
                            });
                            System.out.println("USER CREATED!!!!!");

                            NavHostFragment.findNavController(RegistrationForm.this)
                                    .navigate(R.id.action_registrationForm_to_mainProfilePage);
                        }
                        else {
                            Toast.makeText(getActivity(),"Error ! "+ task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });



            }
        });
    }
}