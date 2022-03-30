package com.example.talklicious.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.talklicious.Adapter.UserAdapter;
import com.example.talklicious.Model.Student;
import com.example.talklicious.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserFragment extends Fragment {


    List<Student> studentList;
    RecyclerView recyclerView;

    FirebaseDatabase database;
    FirebaseAuth auth;

    public UserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_user, container, false);

        studentList=new ArrayList<>();
        recyclerView=view.findViewById(R.id.recycler);
        database=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();



        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        UserAdapter adapter=new UserAdapter(getContext(),studentList);
        recyclerView.setAdapter(adapter);


        database.getReference().child("student").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                studentList.clear();
                for (DataSnapshot snapshot1: snapshot.getChildren())
                {
                    Student student=snapshot1.getValue(Student.class);

                    student.setUserId(snapshot1.getKey());

                    if (!student.getUserId().equals(auth.getUid()))
                    studentList.add(student);

                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });






        return view;
    }
}