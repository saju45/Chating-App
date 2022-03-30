package com.example.talklicious.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.talklicious.Model.Student;
import com.example.talklicious.R;
import com.example.talklicious.databinding.ActivitySetting2Binding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class SettingActivity2 extends AppCompatActivity {

    ActivitySetting2Binding binding;
    FirebaseUser firebaseUser;;
    DatabaseReference databaseReference;
    FirebaseDatabase database;
    FirebaseAuth auth;
    String userId;
    Student student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySetting2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        database=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();


        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser!=null)
        {
            userId=firebaseUser.getUid();
            databaseReference= FirebaseDatabase.getInstance().getReference("student");
        }

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingActivity2.this,MainActivity.class));
                finish();
            }
        });

        binding.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if  (student!=null)
                {
                    Intent intent=new Intent(SettingActivity2.this,EditActivity.class);
                    intent.putExtra("name",student.getName());
                    intent.putExtra("email",student.getEmail());
                    intent.putExtra("phone",student.getPhone());
                    intent.putExtra("uid",student.getUserId());
                    intent.putExtra("coverPic",student.getCoverPic());
                    intent.putExtra("profilephoto",student.getProfilephoto());
                    startActivity(intent);
                }


            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseReference.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists())
                {
                    String userId=snapshot.getKey();
                    student=snapshot.getValue(Student.class);
                    student.setUserId(userId);

               /*     String email=snapshot.child("email").getValue(String.class);
                    String name=snapshot.child("name").getValue(String.class);
                    String phone=snapshot.child("phone").getValue(String.class);
                    String password=snapshot.child("password").getValue(String.class);

                    Student student=new Student(email,name,phone,password);*/

                    binding.name.setText(""+student.getName());
                    binding.email.setText(""+student.getEmail());
                    binding.phone.setText(""+student.getPhone());
                    Picasso.get().load(student.getCoverPic()).placeholder(R.drawable.image_gallery).into(binding.cover);
                    Picasso.get().load(student.getProfilephoto()).placeholder(R.drawable.image_gallery).into(binding.profileImage);
                }




            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}