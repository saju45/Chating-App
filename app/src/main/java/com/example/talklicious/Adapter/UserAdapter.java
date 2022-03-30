package com.example.talklicious.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.talklicious.Activity.ChatActivity;
import com.example.talklicious.Model.Student;
import com.example.talklicious.R;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.viewHolder> {

    Context context;
    List<Student> studentList;

    public UserAdapter(Context context, List<Student> studentList) {
        this.context = context;
        this.studentList = studentList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.user_rv_layout,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        Student student=studentList.get(position);

        holder.textView.setText(student.getName());
        Picasso.get().load(student.getProfilephoto()).placeholder(R.drawable.profile_img).into(holder.profile);

        holder.sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, ChatActivity.class);
                intent.putExtra("userId",student.getUserId());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        ImageView sendMessage;
        CircleImageView profile;
        TextView textView,lastmessage;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            sendMessage=itemView.findViewById(R.id.messageImg);
            profile=itemView.findViewById(R.id.profile_image);
            textView=itemView.findViewById(R.id.simple_username);
            lastmessage=itemView.findViewById(R.id.simple_lastmessage);

        }
    }
}
