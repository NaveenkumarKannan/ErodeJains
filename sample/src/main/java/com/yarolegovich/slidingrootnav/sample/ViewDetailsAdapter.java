package com.yarolegovich.slidingrootnav.sample;

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

import java.util.ArrayList;
import java.util.List;

public class ViewDetailsAdapter extends ArrayAdapter {

    List list = new ArrayList();
    public ViewDetailsAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
    }


    public void add(ViewDetailsData object) {
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
        ViewDetailsAdapter.ViewDetailsDataHolder viewDetailsDataHolder;
        if(row == null){
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.view_details_row_layout,parent,false);
            viewDetailsDataHolder = new ViewDetailsAdapter.ViewDetailsDataHolder();

            viewDetailsDataHolder.ivHeadPhoto = (ImageView) row.findViewById(R.id.ivHeadPhoto);
            viewDetailsDataHolder.tvHeadName = (TextView) row.findViewById(R.id.tvHeadName);
            viewDetailsDataHolder.tvOccupation = (TextView) row.findViewById(R.id.tvOccupation);
            viewDetailsDataHolder.tvNativePlace = (TextView) row.findViewById(R.id.tvNativePlace);
            viewDetailsDataHolder.tvHeadEmail = (TextView) row.findViewById(R.id.tvHeadEmail);
            viewDetailsDataHolder.tvHeadMobile = (TextView) row.findViewById(R.id.tvHeadMobile);
            viewDetailsDataHolder.tvId = (TextView) row.findViewById(R.id.tvId);

            row.setTag(viewDetailsDataHolder);
        }
        else {
            viewDetailsDataHolder = (ViewDetailsAdapter.ViewDetailsDataHolder) row.getTag();
        }
        ViewDetailsData viewDetailsData = (ViewDetailsData) this.getItem(position);

        viewDetailsDataHolder.ivHeadPhoto.setImageBitmap(viewDetailsData.getHeadPhoto());
        viewDetailsDataHolder.tvHeadName.setText(viewDetailsData.getHeadName());
        viewDetailsDataHolder.tvOccupation.setText(viewDetailsData.getOccupation());
        viewDetailsDataHolder.tvNativePlace.setText(viewDetailsData.getNativePlace());
        viewDetailsDataHolder.tvHeadEmail.setText(viewDetailsData.getHeadEmail());
        viewDetailsDataHolder.tvHeadMobile.setText(viewDetailsData.getHeadMobile());
        viewDetailsDataHolder.tvId.setText(viewDetailsData.getId());

        return row;
    }
    static class  ViewDetailsDataHolder
    {
        TextView tvHeadName,tvOccupation,tvNativePlace,tvHeadEmail,tvHeadMobile,tvId;
        ImageView ivHeadPhoto;
    }



}
