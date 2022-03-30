package com.example.talklicious.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.talklicious.Adapter.ChatAdapter;
import com.example.talklicious.Model.Student;
import com.example.talklicious.Model.messageModel;
import com.example.talklicious.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {

    Toolbar toolbar;

    String senderId,receiverId;
    String chatId;
    ImageView profile;
    TextView name;
    FirebaseDatabase database;
    EditText sendMessage;
    ImageView send;
    RecyclerView recyclerView ;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    String time;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        calendar=Calendar.getInstance();
        simpleDateFormat=new SimpleDateFormat("hh:mm a");
        time=simpleDateFormat.format(calendar.getTime());

        toolbar=findViewById(R.id.tollbar);
        setSupportActionBar(toolbar);

        profile=findViewById(R.id.tollbar_profile);
        name=findViewById(R.id.tollbar_name);
        sendMessage=findViewById(R.id.senMessgeId);
        send=findViewById(R.id.send);
        database=FirebaseDatabase.getInstance();
        recyclerView=findViewById(R.id.message_recyclerview);

        Intent intent=getIntent();

        receiverId=intent.getStringExtra("userId");
        senderId= FirebaseAuth.getInstance().getUid();
        chatId= database.getReference().push().getKey();

        final String senderRoom= senderId+receiverId;
        final String receiverRoom=receiverId+senderId;

       ArrayList<messageModel> messageModels= new ArrayList<>();
      ChatAdapter chatAdapter= new ChatAdapter(messageModels,this,receiverId);

        LinearLayoutManager layoutManager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(chatAdapter);
        database.getReference().child("chats")
                .child(senderRoom)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        messageModels.clear();
                        for (DataSnapshot snapshot1: snapshot.getChildren())
                        {
                            messageModel model= snapshot1.getValue(messageModel.class);

                            model.setMessageId(snapshot1.getKey());

                            messageModels.add(model);
                        }
                        chatAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String message= sendMessage.getText().toString();
                final messageModel model=new messageModel(senderId,message);
                model.setTime(time);
               sendMessage.setText("");

                database.getReference().child("chats")
                        .child(senderRoom)
                        .push()
                        .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        database.getReference().child("chats")
                                .child(receiverRoom)
                                .push()
                                .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                            }
                        });

                    }
                });
            }
        });



        database.getReference().child("student").child(receiverId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Student student=snapshot.getValue(Student.class);

                name.setText(student.getName());
                Picasso.get().load(student.getProfilephoto()).placeholder(R.drawable.profile_img).into(profile);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}