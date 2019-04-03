package com.mobile.letsbone.Chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobile.letsbone.Entities.ChatBoxData;
import com.mobile.letsbone.Entities.UserProfile;
import com.mobile.letsbone.Matches.CardSwipe;
import com.mobile.letsbone.R;

import java.util.List;


public class ChatBoxCustomAdapter extends RecyclerView.Adapter<ChatBoxCustomAdapter.ViewHolder> {
    private List<ChatBoxData> chatBoxData;
    private Context context;


    public ChatBoxCustomAdapter(List<ChatBoxData> chatBoxData, Context context) {
        this.chatBoxData = chatBoxData;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.message_item, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        final ViewHolder vh = new ViewHolder(view);
        return vh;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder,final int i) {
        viewHolder.messages.setText(chatBoxData.get(i).getMessage());
        if(chatBoxData.get(i).getCurrentUser()){
            viewHolder.messageContainer.setGravity(Gravity.END);
        }else{
            viewHolder.messageContainer.setGravity(Gravity.START);
            viewHolder.messages.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.their_message));
        }
        
    }

    @Override
    public int getItemCount() {
        return this.chatBoxData.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder {
        public TextView messages;
        public LinearLayout messageContainer;
        public LinearLayout messageLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            messages = itemView.findViewById(R.id.textMessage);
            messageLayout = itemView.findViewById(R.id.messageLayout);
            messageContainer = itemView.findViewById(R.id.messageContainer);



        }

    }
}

