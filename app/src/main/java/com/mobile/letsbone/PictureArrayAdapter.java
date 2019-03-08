package com.mobile.letsbone;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.Image;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class PictureArrayAdapter  extends ArrayAdapter<ProfileData> {

    private Context mContext;
    private List<ProfileData> profileList = new ArrayList<>();


    public PictureArrayAdapter(@NonNull Context context, @SuppressLint("SupportAnnotationUsage") @LayoutRes ArrayList<ProfileData> list) {
        super(context, 0, list);
        mContext = context;
        profileList = list;
    }


    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View listItem = convertView;

        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.card_item,parent,false);

        ProfileData currentProfile  = profileList.get(position);

        ImageView image =(ImageView)listItem.findViewById(R.id.picture);
        image.setImageResource(currentProfile.getmPhoto());

        TextView name = (TextView)listItem.findViewById(R.id.profileName);
        name.setText(currentProfile.getfName() + " " + currentProfile.getlName());

        TextView location = (TextView)listItem.findViewById(R.id.profileLocation);
        location.setText(currentProfile.getLocation());

        TextView age = (TextView)listItem.findViewById(R.id.profileAge);
        location.setText(currentProfile.getAge());

        return listItem;
    }

}
