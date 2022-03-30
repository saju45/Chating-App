package com.example.talklicious.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.talklicious.Model.messageModel;
import com.example.talklicious.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter {

    ArrayList<messageModel> messageModels;
    Context context;
    String recId;

    int SENDER_VIEW_TYPE=1;
    int RECEIVER_VIEW_TYPE=2;


    public ChatAdapter(ArrayList<messageModel> messageModels, Context context) {
        this.messageModels = messageModels;
        this.context = context;
    }

    public ChatAdapter(ArrayList<messageModel> messageModels, Context context, String recId) {
        this.messageModels = messageModels;
        this.context = context;
        this.recId = recId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType==SENDER_VIEW_TYPE)
        {
            View view= LayoutInflater.from(context).inflate(R.layout.simple_sender,parent,false);
            return new SenderviewHolder(view);

        }else {
            View view= LayoutInflater.from(context).inflate(R.layout.simple_receiver,parent,false);
            return new ReceiverViewHolder(view);
        }

    }

    @Override
    public int getItemViewType(int position) {

        if (messageModels.get(position).getuId().equals(FirebaseAuth.getInstance().getUid()))
        {
            return SENDER_VIEW_TYPE;
        }else
            {
               return RECEIVER_VIEW_TYPE;
            }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        messageModel messageModel=messageModels.get(position);



        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

              new AlertDialog.Builder(context)
                      .setTitle("Delete")
                      .setMessage("Are you sure you want to delete this message")
                      .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                          @Override
                          public void onClick(DialogInterface dialog, int which) {

                              FirebaseDatabase database=FirebaseDatabase.getInstance();
                              String sender=FirebaseAuth.getInstance().getUid()+recId;
                              database.getReference().child("chats").child(sender)
                                      .child(messageModel.getMessageId())
                                      .setValue(null);


                          }
                      }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialog, int which) {

                     dialog.dismiss();

                  }
              }).show();


                return false;
            }
        });

        if(holder.getClass()==SenderviewHolder.class)
        {
            ((SenderviewHolder)holder).senderMessage.setText(messageModel.getMessage());
            ((SenderviewHolder) holder).senderTime.setText(messageModel.getTime());
        }else {
            ((ReceiverViewHolder)holder).recevierMessage.setText(messageModel.getMessage());
            ((ReceiverViewHolder) holder).receiveTime.setText(messageModel.getTime());
        }

    }

    @Override
    public int getItemCount() {
        return messageModels.size();
    }

    public class ReceiverViewHolder extends RecyclerView.ViewHolder {

        TextView recevierMessage,receiveTime;

        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            recevierMessage=itemView.findViewById(R.id.receiveText);
            receiveTime=itemView.findViewById(R.id.receiveTime);

        }
    }
    public class SenderviewHolder extends RecyclerView.ViewHolder {

        TextView senderMessage,senderTime;

        public SenderviewHolder(@NonNull View itemView) {
            super(itemView);

            senderMessage=itemView.findViewById(R.id.sendersms);
            senderTime=itemView.findViewById(R.id.sendertime);
        }
    }
}
