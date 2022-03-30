package com.example.talklicious.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.talklicious.Activity.AddPostActivity;
import com.example.talklicious.Adapter.PostAdapter;
import com.example.talklicious.Model.Post;
import com.example.talklicious.Model.Student;
import com.example.talklicious.R;
import com.example.talklicious.databinding.FragmentHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class HomeFragment extends Fragment {

    FragmentHomeBinding binding;
    DatabaseReference databaseReference;
    FirebaseDatabase database;

    List<Post> postList;
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentHomeBinding.inflate(inflater, container, false);

        database=FirebaseDatabase.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference("Post");
        postList=new ArrayList<>();

        database.getReference().child("student").child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    Student student=snapshot.getValue(Student.class);

                 Picasso.get().load(student.getProfilephoto()).placeholder(R.drawable.image_gallery).into(binding.profileImage);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        PostAdapter adapter=new PostAdapter(getContext(),postList);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setReverseLayout(true);

        binding.postRecyclerview.setAdapter(adapter);
        binding.postRecyclerview.setLayoutManager(layoutManager);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                for (DataSnapshot snapshot1: snapshot.getChildren())
                {

                    Post post=snapshot1.getValue(Post.class);

                    postList.add(post);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.addPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),AddPostActivity.class));
            }
        });


        return binding.getRoot();
    }
}