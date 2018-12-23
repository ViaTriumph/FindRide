package com.example.pruthvi.carride;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class SignUpActivity extends AppCompatActivity {

    public EditText mEmail;
    public EditText mPassword;
    private Button mRegister;
    private EditText mFullName;
    private EditText mPhoneNo;

    private DatabaseReference mReference;

    private Spinner locationSpinner;
    private ArrayAdapter<String> locationAdapter;

    private String mLocationSelect;
    private String mFullNameText;
    private String mPhoneText;
    private String email;
    private String password;

    private FirebaseAuth mAuth;


    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        mEmail=findViewById(R.id.emailText);
       // mPassword=findViewById(R.id.passwordText);
        mRegister=findViewById(R.id.registerButton);
        mFullName=findViewById(R.id.nameText);
        mPhoneNo=findViewById(R.id.phoneText);

        mReference=FirebaseDatabase.getInstance().getReference();

        List<String> locationList;

        locationSpinner=findViewById(R.id.locationSpinner);
        locationList=new ArrayList<>();
        locationList.add("Panjim");
        locationList.add("Vasco Da Gama");
        locationList.add("Margao");
        locationList.add("Verna");

        locationAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,locationList);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(locationAdapter);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //email=mEmail.getText().toString();
                //password=mPassword.getText().toString();
                //Log.d("SignUpActivity", "email: "+email);
                //Log.d("SignUpActivity", "password: "+password);
                Log.d("SignUpActivity", "ref : "+mReference.toString());
                mFullNameText=mFullName.getText().toString();
                mPhoneText=mPhoneNo.getText().toString();
                mLocationSelect=locationSpinner.getSelectedItem().toString();

                UserProfile currUser=new UserProfile(mFullNameText,mPhoneText,mLocationSelect);
                Log.d("SignUpActivity", "location : "+mLocationSelect);

                UserProfile user=new UserProfile(mFullNameText,mPhoneText,mLocationSelect);

                mReference.child("Users").child(mFullNameText).setValue(currUser);

                startActivity(new Intent(SignUpActivity.this,LoginActivity.class));
            }
        });



        }






}
