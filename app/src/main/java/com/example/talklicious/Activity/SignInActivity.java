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
import com.example.talklicious.databinding.ActivitySignInBinding;
import com.example.talklicious.databinding.ActivitySignUpactivityBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {

    ActivitySignInBinding binding;
    FirebaseAuth auth;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth=FirebaseAuth.getInstance();
        dialog=new ProgressDialog(this);
        dialog.setTitle("Login account");
        dialog.setMessage("please wait..");

        binding.keepSigned.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this,SignUPActivity.class));
                finish();
            }
        });

        binding.signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=binding.email.getText().toString();
                String password=binding.password.getText().toString();

                if (email.isEmpty())
                {
                    binding.email.setError("please enter your email");
                    binding.email.requestFocus();
                }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {
                    binding.email.setText("please enter valid email");
                    binding.email.requestFocus();
                }else if (password.isEmpty()){

                    binding.password.setError("please enter password");
                    binding.password.requestFocus();

                }else if (password.length()<6){

                    binding.password.setError("password length should be 6");
                    binding.password.requestFocus();

                }else {
                    dialog.show();

                    auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            dialog.dismiss();
                            if (task.isSuccessful())
                            {
                                Toast.makeText(getApplicationContext(), "login successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SignInActivity.this,MainActivity.class));
                                finish();
                            }
                        }
                    });
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (auth.getCurrentUser()!=null)
        {
            startActivity(new Intent(SignInActivity.this,MainActivity.class));
            finish();
        }
    }
}