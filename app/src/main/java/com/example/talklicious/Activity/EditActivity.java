package com.example.talklicious.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.talklicious.ApiRef;
import com.example.talklicious.Model.Student;
import com.example.talklicious.R;
import com.example.talklicious.databinding.ActivityEditBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class EditActivity extends AppCompatActivity {

    ActivityEditBinding binding;
    Intent intent;

    int IMAGE_REQUEST=1;
    int IMAGE_REQUEST2=2;

    private Uri imageUri;
    private Uri imageUri2;

    FirebaseDatabase database;
    DatabaseReference databaseReference;
    FirebaseAuth auth;
    FirebaseStorage strorage;

    String coverPicUrI;
    String profileImage="",coverImage="";
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressDialog=new ProgressDialog(this);
        intent=getIntent();
        auth=FirebaseAuth.getInstance();
        strorage=FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseReference=FirebaseDatabase.getInstance().getReference("student");

       String name= intent.getStringExtra("name");
       String email= intent.getStringExtra("email");
       String phone= intent.getStringExtra("phone");
       String userId= intent.getStringExtra("uid");
       profileImage= intent.getStringExtra("profilephoto");
       coverImage= intent.getStringExtra("coverPic");



       binding.name.setText(name);
       binding.email.setText(email);
       binding.phone.setText(phone);

       Picasso.get().load(coverImage).placeholder(R.drawable.image_gallery).into(binding.cover);
       Picasso.get().load(profileImage).placeholder(R.drawable.image_gallery).into(binding.profileImage);



       binding.profileImage.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent=new Intent();
               intent.setAction(Intent.ACTION_GET_CONTENT);
               intent.setType("image/*");
               startActivityForResult(intent,IMAGE_REQUEST2);
           }
       });
       binding.cover.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               Intent intent=new Intent();
               intent.setAction(Intent.ACTION_GET_CONTENT);
               intent.setType("image/*");
               startActivityForResult(intent,IMAGE_REQUEST);
           }
       });


       binding.update.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               uploadImages();
           }
       });


    }
    protected void onActivityResult(int requestCode, int resultCode,@Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==IMAGE_REQUEST && resultCode==RESULT_OK && data!=null  && data.getData()!=null)
        {
            imageUri=data.getData();
            binding.cover.setImageURI(imageUri);
        }
        if (requestCode==IMAGE_REQUEST2 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            imageUri2=data.getData();
            binding.profileImage.setImageURI(imageUri2);


        }
    }

    public void uploadImages(){

        final StorageReference reference=strorage.getReference().child("Images").child(auth.getUid()+System.currentTimeMillis());
        progressDialog.setTitle("Please Wait..");

        if(imageUri!=null){
            progressDialog.setMessage("Uploading Cover Photo...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            reference.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
                reference.getDownloadUrl().addOnSuccessListener(uri -> {
                        coverImage=uri.toString();
                    updateDataToDatabase(profileImage,coverImage);
                });
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                }
            });
        }

        if(imageUri2!=null){
            progressDialog.setMessage("Uploading Profile Image...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            reference.putFile(imageUri2).addOnSuccessListener(taskSnapshot -> {
                reference.getDownloadUrl().addOnSuccessListener(uri -> {
                    profileImage=uri.toString();
                    updateDataToDatabase(profileImage,coverImage);
                });
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                }
            });
        }
        if(imageUri==null && imageUri2==null){
            updateDataToDatabase(profileImage,coverImage);
        }



    }

    private void updateDataToDatabase(String profileImageUri,String coverImageUri){
                progressDialog.setMessage("Updating Your Profile...");
                progressDialog.setCancelable(false);
                progressDialog.show();
            HashMap<String,Object> profileMap=new HashMap<>();
            profileMap.put("coverPic",coverImageUri);
            profileMap.put("profilephoto",profileImageUri);

            String name_str=binding.name.getText().toString();
            String email_str=binding.email.getText().toString().trim();
            String phone_str=binding.phone.getText().toString().trim();

            if(!name_str.isEmpty()){
                profileMap.put("name",name_str);
            }
            if(!email_str.isEmpty()){
                profileMap.put("email",email_str);
            }
            if(!phone_str.isEmpty()) {
                profileMap.put("phone", phone_str);
            }

        ApiRef.getUserRef().child(auth.getCurrentUser().getUid())
                .updateChildren(profileMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Toast.makeText(EditActivity.this, "Profile Updated Successful", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(EditActivity.this, "Profile Update Failed.", Toast.LENGTH_SHORT).show();

            }
        });





    }

  /*  public void update(String userId){


        String name_str=binding.name.getText().toString();
        String email_str=binding.email.getText().toString().trim();
        String phone_str=binding.phone.getText().toString().trim();

        final StorageReference reference=strorage.getReference().child("profile_image").child(auth.getUid()+System.currentTimeMillis());
        final StorageReference reference2=strorage.getReference().child("cover_img").child(auth.getUid()+System.currentTimeMillis());



*//*        if (imageUri!=null && imageUri2!=null)
        {


            reference.putFile(imageUri2).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            //   database.getReference().child("student").child(auth.getUid()).child("coverPic").setValue(coverPicUrI);

                            HashMap<String,String> hashMap=new HashMap<>();
                            hashMap.put("name",name_str);
                            hashMap.put("email",email_str);
                            hashMap.put("phone",phone_str);
                            hashMap.put("profilephoto",uri.toString());

                            databaseReference.child(auth.getUid()).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(getApplicationContext(), "data is updated", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });

                }
            });


            reference2.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    reference2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            coverPicUrI=uri.toString();
                            //   database.getReference().child("student").child(auth.getUid()).child("coverPic").setValue(coverPicUrI);

                            HashMap<String,String> hashMap=new HashMap<>();
                            hashMap.put("name",name_str);
                            hashMap.put("email",email_str);
                            hashMap.put("phone",phone_str);
                            hashMap.put("coverPic",coverPicUrI);

                            databaseReference.child(auth.getUid()).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(getApplicationContext(), "data is updated", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });

                }
            });


        }

      else   if (imageUri2!=null)
        {

            reference.putFile(imageUri2).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            //   database.getReference().child("student").child(auth.getUid()).child("coverPic").setValue(coverPicUrI);

                            HashMap<String,String> hashMap=new HashMap<>();
                            hashMap.put("name",name_str);
                            hashMap.put("email",email_str);
                            hashMap.put("phone",phone_str);
                            hashMap.put("profilephoto",uri.toString());

                            databaseReference.child(auth.getUid()).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(getApplicationContext(), "data is updated", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });

                }
            });



        }

    else*//* if (imageUri!=null)
        {
            reference2.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    reference2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            coverPicUrI=uri.toString();
                            //   database.getReference().child("student").child(auth.getUid()).child("coverPic").setValue(coverPicUrI);

                            HashMap<String,String> hashMap=new HashMap<>();
                            hashMap.put("name",name_str);
                            hashMap.put("email",email_str);
                            hashMap.put("phone",phone_str);
                            hashMap.put("coverPic",coverPicUrI);

                            databaseReference.child(auth.getUid()).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(getApplicationContext(), "data is updated", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });

                }
            });


        }else {
            HashMap<String,Object> hashMap=new HashMap<>();
            hashMap.put("name",name_str);
            hashMap.put("email",email_str);
            hashMap.put("phone",phone_str);

            databaseReference.child(auth.getUid()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(getApplicationContext(), "data is updated", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
*/
}