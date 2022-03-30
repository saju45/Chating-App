package com.example.talklicious.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.talklicious.Fragments.HomeFragment;
import com.example.talklicious.Model.Post;
import com.example.talklicious.R;
import com.example.talklicious.databinding.ActivityAddPostBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.logging.SimpleFormatter;

public class AddPostActivity extends AppCompatActivity {

    ActivityAddPostBinding binding;
    int IMAGE_REQUEST_CODE=101;
    Uri imageUri;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    FirebaseDatabase database;
    String creatorId;
    String post_img;
    String postId;

    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    String time;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAddPostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        creatorId=FirebaseAuth.getInstance().getUid();
        calendar=Calendar.getInstance();
        simpleDateFormat=new SimpleDateFormat("hh:mm a");
        time=simpleDateFormat.format(calendar.getTime());
        storageReference= FirebaseStorage.getInstance().getReference("UploadImg").child(creatorId);
        databaseReference= FirebaseDatabase.getInstance().getReference("Post");
        creatorId=FirebaseAuth.getInstance().getUid();
        postId=databaseReference.push().getKey();

        binding.galleryImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,IMAGE_REQUEST_CODE);
            }
        });



        binding.addPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String postText=binding.postText.getText().toString();
                StorageReference reference=storageReference.child("img_"+System.currentTimeMillis());

                if(postText.isEmpty() && imageUri==null)
                {
                    Toast.makeText(getApplicationContext(), "please enter text or img", Toast.LENGTH_SHORT).show();
                }
               else if(imageUri!=null)
            {
                reference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        if(task.isSuccessful())
                        {
                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    post_img=uri.toString();

                                    Post post=new Post(postText,post_img,postId,creatorId,time);

                                    databaseReference.child(postId).setValue(post).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if (task.isSuccessful())
                                            {
                                                Toast.makeText(getApplicationContext(), "post id uploaded", Toast.LENGTH_SHORT).show();
                                              //  startActivity(new Intent(getApplicationContext(),HomeFragment.class));
                                            }else {
                                                Toast.makeText(getApplicationContext(), "Error : "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            });
                        }
                    }
                });
            }
            else {

                Post post=new Post(postText,"",postId,creatorId,time);

                databaseReference.child(postId).setValue(post).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful())
                        {
                            Toast.makeText(getApplicationContext(), "post is uploaded", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(AddPostActivity.this,HomeFragment.class);
                            finish();
                        }else {
                            Toast.makeText(getApplicationContext(), "Error : "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==IMAGE_REQUEST_CODE && resultCode==RESULT_OK && data!=null && data.getData()!=null)
        {
            imageUri=data.getData();

            binding.postStatusImg.setImageURI(imageUri);

        }
    }
}