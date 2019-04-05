package com.mobile.letsbone.Matches;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mobile.letsbone.Entities.ProfileData;
import com.mobile.letsbone.R;

import java.util.ArrayList;

public class PictureArrayAdapter  extends ArrayAdapter<ProfileData> {

    private Context mContext;



    public PictureArrayAdapter(@NonNull Context context, int resourceId, @SuppressLint("SupportAnnotationUsage") @LayoutRes ArrayList<ProfileData> list) {
        super(context, resourceId, list);

    }


    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        ProfileData profile_item = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.card_item, parent,false);
        }

        ImageView image =(ImageView)convertView.findViewById(R.id.picture);
        switch (profile_item.getImageUrl()){
            case"default":
                Glide.with(convertView.getContext()).load(R.drawable.nophoto1).into(image);
                break;
                default:
                    Glide.clear(image);
                    Glide.with(convertView.getContext()).load(profile_item.getImageUrl()).into(image);
                    break;
        }

        TextView name = (TextView)convertView.findViewById(R.id.profileName);
        name.setText(profile_item.getfName() + " " + profile_item.getlName());

        TextView age = (TextView)convertView.findViewById(R.id.profileAge);
        age.setText(profile_item.getAge());

        return convertView;

        /*
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.card_item,parent,false);

        ProfileData currentProfile  = profileList.get(position);

        ImageView image =(ImageView)listItem.findViewById(R.id.picture);
        image.setImageResource(currentProfile.getmPhoto());

        TextView name = (TextView)listItem.findViewById(R.id.profileName);
        name.setText(currentProfile.getfName() + " " + currentProfile.getlName());

        TextView age = (TextView)listItem.findViewById(R.id.profileAge);
        age.setText(currentProfile.getAge());

        return listItem;

        */
    }

}
