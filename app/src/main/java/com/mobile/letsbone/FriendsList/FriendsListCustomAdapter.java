package com.mobile.letsbone.FriendsList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.mobile.letsbone.Chat.ChatBoxActivity;
import com.mobile.letsbone.Entities.UserProfile;
import com.mobile.letsbone.Matches.CardSwipe;
import com.mobile.letsbone.R;

import java.util.List;


public class FriendsListCustomAdapter extends RecyclerView.Adapter<FriendsListCustomAdapter.ViewHolder> {
    private List<UserProfile> userProfile;
    private Context context;


    public FriendsListCustomAdapter(List<UserProfile> userProfile, Context context) {
        this.userProfile = userProfile;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_list_layout_item, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        final ViewHolder vh = new ViewHolder(view);
        return vh;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder,final int i) {
        //setting views with userProfile information
        viewHolder.matchFirstName.setText(userProfile.get(i).getFirstName());
        viewHolder.matchKey.setText(userProfile.get(i).getUserId());
        viewHolder.matchImage.setImageResource(userProfile.get(i).getProfileImage());
        viewHolder.matchLastName.setText(userProfile.get(i).getLastName());
        
    }

    @Override
    public int getItemCount() {
        return userProfile.size();

    }

    public class ViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView matchFirstName, matchKey, matchLastName;
        public ImageView matchImage;
        CardView parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //Assigning
            matchFirstName   = itemView.findViewById(R.id.textViewItemFirstName);
            matchLastName    = itemView.findViewById(R.id.textViewItemLastName);
            matchKey         = itemView.findViewById(R.id.textViewItemDir);
            matchImage       = itemView.findViewById(R.id.imageViewItem);
            parentLayout     = itemView.findViewById(R.id.parent_Layout);

            itemView.setOnClickListener(this);


        }

        //go to chat activity when you click
        @Override
        public void onClick(View v) {

            Intent intent = new Intent(v.getContext(), ChatBoxActivity.class);
            Bundle bundle= new Bundle();
            bundle.putString("MatchKey", matchKey.getText().toString());
            bundle.putString("MatchFirstName", matchFirstName.getText().toString());
            bundle.putString("MatchLastName", matchLastName.getText().toString());
            intent.putExtras(bundle);
            v.getContext().startActivity(intent);

        }
    }
}

