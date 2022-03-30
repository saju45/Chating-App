package com.example.talklicious.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.talklicious.R;
import com.example.talklicious.databinding.ActivitySignUpactivityBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignUPActivity extends AppCompatActivity {

    ActivitySignUpactivityBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance();
        dialog = new ProgressDialog(this);
        dialog.setTitle("Create account");
        dialog.setMessage("We are creating your account");

        binding.signInNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUPActivity.this, SignInActivity.class));
                finish();
            }
        });

        binding.signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = binding.fullName.getText().toString();
                String email = binding.email.getText().toString();
                String phone = binding.phone.getText().toString();
                String createpass = binding.createPassword.getText().toString();
                String confirmpass = binding.confirmPassword.getText().toString();


                if (name.isEmpty()) {
                    binding.fullName.setError("please enter your name");
                    binding.fullName.requestFocus();
                } else if (email.isEmpty()) {
                    binding.email.setError("Enter your name");
                    binding.email.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    binding.email.setError("please enter valid email");
                    binding.email.requestFocus();
                } else if (createpass.isEmpty()) {
                    binding.createPassword.setError("please enter create password");
                    binding.createPassword.requestFocus();

                } else if (phone.isEmpty()) {

                    binding.phone.setError("enter your phone");
                    binding.phone.requestFocus();
                } else if (confirmpass.isEmpty()) {
                    binding.confirmPassword.setError("please enter confirm password");
                    binding.confirmPassword.requestFocus();

                } else {
                    dialog.show();

                    auth.createUserWithEmailAndPassword(email, confirmpass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            dialog.dismiss();
                            if (task.isSuccessful()) {

                                String id=auth.getUid();
                                HashMap<String,String> hashMap=new HashMap<>();

                                hashMap.put("name",name);
                                hashMap.put("email",email);
                                hashMap.put("phone",phone);
                                hashMap.put("userId",id);
                                hashMap.put("password",createpass);

                                database.getReference("student").child(id).setValue(hashMap);

                                Toast.makeText(getApplicationContext(), "your registration is successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SignUPActivity.this, SignInActivity.class));
                                finish();
                            }

                        }
                    });
                }
            }

        });


    }
}