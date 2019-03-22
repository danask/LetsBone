package com.mobile.letsbone;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;

public class ListCustomAdapter extends BaseAdapter {

    ArrayList<String> dataList = new ArrayList<>();
    ArrayList<Integer> imageList = new ArrayList<>();
    ArrayList<String> dataListDir = new ArrayList<>();

    public ListCustomAdapter(ArrayList<String> anyDataList,
                             ArrayList<Integer> anyImageList,
                             ArrayList<String> anyDataListDir){
        this.dataList = anyDataList;
        this.imageList = anyImageList;
        this.dataListDir = anyDataListDir;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if(convertView == null)
        {
            LayoutInflater myInflater = LayoutInflater.from
                    (parent.getContext());
            convertView = myInflater.inflate(R.layout.user_list_layout_item, parent, false);
        }

        ImageView itemImageView = (ImageView)convertView.findViewById(R.id.imageViewItem);
        itemImageView.setImageResource(imageList.get(position));

        TextView itemTextView = (TextView)convertView.findViewById(R.id.textViewItem);
        itemTextView.setText(dataList.get(position));
//        if(dataList.get(position).equals())


        TextView itemTextViewDir = (TextView)convertView.findViewById(R.id.textViewItemDir);
        itemTextViewDir.setText(dataListDir.get(position));

        return convertView;
    }
}
