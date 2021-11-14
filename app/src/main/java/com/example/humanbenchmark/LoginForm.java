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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginForm#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginForm extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LoginForm() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginForm.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginForm newInstance(String param1, String param2) {
        LoginForm fragment = new LoginForm();
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
        return inflater.inflate(R.layout.fragment_login_form, container, false);
    }


    EditText  eMail, passwd;
    Button buttonLogin;
    TextView registerHere;
    FirebaseAuth firebaseAuth;


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        eMail =  view.findViewById(R.id.loginEmail);
        passwd= view.findViewById(R.id.loginPasswd);

        buttonLogin= view.findViewById(R.id.buttonLogin);
        registerHere= view.findViewById(R.id.registerHere);

        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null){

            NavHostFragment.findNavController(LoginForm.this)
                    .navigate(R.id.action_loginForm_to_mainProfilePage);
        }

        registerHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(LoginForm.this)
                        .navigate(R.id.action_loginForm_to_registrationForm);
            }
        });


        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = eMail.getText().toString().trim();
                String password = passwd.getText().toString().trim();

                if (TextUtils.isEmpty(email)){
                    eMail.setError("Email is Required.");
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    passwd.setError("Email is Required.");
                    return;
                }


                firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(),"Logined!!!",Toast.LENGTH_SHORT).show();
                            System.out.println("USER login!!!!!");

                            NavHostFragment.findNavController(LoginForm.this)
                                    .navigate(R.id.action_loginForm_to_mainProfilePage);
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