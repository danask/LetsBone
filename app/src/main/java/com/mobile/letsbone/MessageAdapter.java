package com.mobile.letsbone;


import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private List<Message> mMessages;
    private int userType;
    private int[] mUsernameColors;
    SharedPreferences sharedPref;


    public MessageAdapter(List<Message> messages) {
        mMessages = messages;
//          mUsernameColors = context.getResources().getIntArray(R.array.username_colors);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout = -1;
        /*switch (viewType) {
            case Message.TYPE_MESSAGE:
                layout = R.layout.item_message;
                break;
            case Message.TYPE_LOG:
                layout = R.layout.item_log;
                break;
            case Message.TYPE_ACTION:
                layout = R.layout.item_action;
                break;
        }*/
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.layout_message, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position)
    {
        Message message = mMessages.get(position);

        Log.d("message : ", "=========onBindViewHolder:position======="+ position);
        Log.d("message : ", "=========onBindViewHolder:Type======="+ userType);
        viewHolder.setMessage(message.getMessage());
        viewHolder.setImage(message.getImage());
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mMessages.get(position).getType();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView mImageView;
        private TextView mMessageViewTheir;
        private TextView mMessageViewBound;
        private TextView mMessageViewMe;

        public ViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.image);
            mMessageViewTheir = (TextView) itemView.findViewById(R.id.messageSender);
//            mMessageViewBound = (TextView) itemView.findViewById(R.id.messageBoundary);
            mMessageViewMe = (TextView) itemView.findViewById(R.id.messageMe);
        }

        public void setMessage(String message) {
            if (mMessageViewTheir == null) return;
            if (mMessageViewMe == null) return;

//            Drawable image= (Drawable) getResources().getDrawable(R.drawable.my_message);
            String formattedDate = new SimpleDateFormat("mm:ss:a", Locale.getDefault()).format(new Date());

            if(message.substring(0,1).equals("R")) {

                message = message.substring(1);
                if(message.equals("")) return;
                mMessageViewTheir.setVisibility(View.VISIBLE);
                mMessageViewTheir.setText("" + message);
                mMessageViewMe.setVisibility(View.INVISIBLE);
                mMessageViewTheir.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
            }
            else
            {
                message = message.substring(1);
                if(message.equals("")) return;
                mMessageViewMe.setVisibility(View.VISIBLE);
                mMessageViewMe.setText("" + message);
                mMessageViewTheir.getLayoutParams().width = 800 - message.length()*20;
                mMessageViewTheir.setVisibility(View.INVISIBLE);
            }
        }

        public void setImage(Bitmap bmp){
            if(null == mImageView) return;
            if(null == bmp) return;
            mImageView.setImageBitmap(bmp);
        }
        private int getUsernameColor(String username) {
            int hash = 7;
            for (int i = 0, len = username.length(); i < len; i++) {
                hash = username.codePointAt(i) + (hash << 5) - hash;
            }
            int index = Math.abs(hash % mUsernameColors.length);
            return mUsernameColors[index];
        }
    }
}
