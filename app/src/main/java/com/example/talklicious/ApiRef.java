package com.example.talklicious;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ApiRef {
    public static final DatabaseReference userRef=FirebaseDatabase.getInstance().getReference("student");

    public static DatabaseReference getUserRef() {
        return userRef;
    }
}
