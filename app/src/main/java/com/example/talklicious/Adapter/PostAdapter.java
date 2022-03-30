package com.example.talklicious.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.talklicious.Model.Post;
import com.example.talklicious.Model.Student;
import com.example.talklicious.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.viewHolder> {

    Context context;
    List<Post> postList;
    FirebaseDatabase database;

    public PostAdapter(Context context, List<Post> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.simple_postlayout,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        Post post=postList.get(position);

        holder.likeText.setText(post.getPostLike()+"");

         if (!post.getStatusText().equals("") && !post.getStatusImg().equals(""))
        {
            holder.postText.setText(post.getStatusText());
            holder.time.setText(post.getCreateOn());
            Picasso.get().load(post.getStatusImg()).placeholder(R.drawable.image_gallery).into(holder.postImg);
        }else if (!post.getStatusText().equals(""))
        {
            holder.postText.setText(post.getStatusText());
            holder.time.setText(post.getCreateOn());
            holder.postImg.setVisibility(View.GONE);
        }else if (!post.getStatusImg().equals(""))
        {
            holder.postImg.setVisibility(View.VISIBLE);
            holder.time.setText(post.getCreateOn());
            Picasso.get().load(post.getStatusImg()).placeholder(R.drawable.image_gallery).into(holder.postImg);
            holder.postText.setVisibility(View.GONE);
        }

         database=FirebaseDatabase.getInstance();

         database.getReference().child("student")
                 .child(post.getCreatorId())
                 .addListenerForSingleValueEvent(new ValueEventListener() {
                     @Override
                     public void onDataChange(@NonNull DataSnapshot snapshot) {

                         if (snapshot.exists())
                         {
                             Student student=snapshot.getValue(Student.class);
                             holder.username.setText(student.getName());

                             Picasso.get().load(student.getProfilephoto()).placeholder(R.drawable.image_gallery).into(holder.profile);
                         }
                     }

                     @Override
                     public void onCancelled(@NonNull DatabaseError error) {

                     }
                 });


         FirebaseDatabase.getInstance().getReference()
                 .child("Post")
                 .child(post.getPostId())
                 .child("likes")
                 .child(FirebaseAuth.getInstance().getUid())
                 .addListenerForSingleValueEvent(new ValueEventListener() {
                     @Override
                     public void onDataChange(@NonNull DataSnapshot snapshot) {

                         if (snapshot.exists())
                         {
                             holder.like.setImageResource(R.drawable.heartred);

                         }else {
                             holder.like.setOnClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View v) {


                                     FirebaseDatabase.getInstance().getReference()
                                             .child("Post")
                                             .child(post.getPostId())
                                             .child("likes")
                                             .child(FirebaseAuth.getInstance().getUid())
                                             .setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                                         @Override
                                         public void onComplete(@NonNull Task<Void> task) {

                                             FirebaseDatabase.getInstance().getReference()
                                                     .child("Post")
                                                     .child(post.getPostId())
                                                     .child("postLike")
                                                     .setValue(post.getLike()+1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                 @Override
                                                 public void onComplete(@NonNull Task<Void> task) {

                                                     holder.like.setImageResource(R.drawable.heartred);


                                                 }
                                             });
                                         }
                                     });

                                 }
                             });

                         }

                     }

                     @Override
                     public void onCancelled(@NonNull DatabaseError error) {

                     }
                 });




    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        ImageView profile,postImg,like;
        TextView username,time,postText,likeText;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            profile=itemView.findViewById(R.id.post_profile);
            postImg=itemView.findViewById(R.id.post_postImg);
            username=itemView.findViewById(R.id.post_username);
            time=itemView.findViewById(R.id.post_time);
            postText=itemView.findViewById(R.id.post_statustext);
            like=itemView.findViewById(R.id.like);
            likeText=itemView.findViewById(R.id.liketext);

        }
    }
}
