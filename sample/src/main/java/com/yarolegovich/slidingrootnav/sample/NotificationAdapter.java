package com.yarolegovich.slidingrootnav.sample;

import android.content.Context;
import android.graphics.Bitmap;
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

public class NotificationAdapter  extends ArrayAdapter {

    List list = new ArrayList();
    public NotificationAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
    }


    public void add(NotificationData object) {
        super.add(object);
        list.add(object);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row;
        row = convertView;
        NotificationAdapter.NotificationDataHolder notificationDataHolder;
        if(row == null){
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.notification_read_row_layout,parent,false);
            notificationDataHolder = new NotificationAdapter.NotificationDataHolder();


            notificationDataHolder.tvTitle = (TextView) row.findViewById(R.id.tvTitle);
            notificationDataHolder.tvMessage = (TextView) row.findViewById(R.id.tvMessage);
            notificationDataHolder.tvNameDate = (TextView) row.findViewById(R.id.tvNameDate);
            notificationDataHolder.ivNotificationImage = row.findViewById(R.id.ivNotificationImage);

            row.setTag(notificationDataHolder);
        }
        else {
            notificationDataHolder = (NotificationAdapter.NotificationDataHolder) row.getTag();
        }
        NotificationData notificationData = (NotificationData) this.getItem(position);

        notificationDataHolder.tvTitle.setText(notificationData.getTitle());
        notificationDataHolder.tvMessage.setText(notificationData.getMessage());
        notificationDataHolder.tvNameDate.setText(notificationData.getUser_name()+" "+notificationData.getDate());

        Bitmap image = notificationData.getBitmap();
        if(image != null){
            notificationDataHolder.ivNotificationImage.setImageBitmap(image);
            notificationDataHolder.ivNotificationImage.setVisibility(View.VISIBLE);
        }else {
            notificationDataHolder.ivNotificationImage.setVisibility(View.GONE);
        }
        return row;
    }
    static class  NotificationDataHolder
    {
        TextView tvTitle,tvMessage,tvNameDate;
        ImageView ivNotificationImage;
    }



}

