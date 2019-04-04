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

public class FamilyDetailsAdapter extends ArrayAdapter {

    List list = new ArrayList();
    public FamilyDetailsAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
    }


    public void add(FamilyDetailsData object) {
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
        FamilyDetailsAdapter.FamilyDetailsDataHolder familyDetailsDataHolder;
        if(row == null){
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.family_details_row_layout,parent,false);
            familyDetailsDataHolder = new FamilyDetailsAdapter.FamilyDetailsDataHolder();

            familyDetailsDataHolder.ivChildPhoto1 = (ImageView) row.findViewById(R.id.ivChildPhoto1);
            familyDetailsDataHolder.tvChildName1 = (TextView) row.findViewById(R.id.tvChildName1);
            familyDetailsDataHolder.tvWifeName1 = (TextView) row.findViewById(R.id.tvWifeName1);
            familyDetailsDataHolder.tvMobNo1 = (TextView) row.findViewById(R.id.tvMobNo1);
            familyDetailsDataHolder.tvGrand1Child1 = (TextView) row.findViewById(R.id.tvGrand1Child1);
            familyDetailsDataHolder.tvGrand2Child1 = (TextView) row.findViewById(R.id.tvGrand2Child1);
            familyDetailsDataHolder.tvGrand3Child1 = (TextView) row.findViewById(R.id.tvGrand3Child1);
            familyDetailsDataHolder.tvGrand4Child1 = (TextView) row.findViewById(R.id.tvGrand4Child1);

            row.setTag(familyDetailsDataHolder);
        }
        else {
            familyDetailsDataHolder = (FamilyDetailsAdapter.FamilyDetailsDataHolder) row.getTag();
        }
        FamilyDetailsData familyDetailsData = (FamilyDetailsData) this.getItem(position);

        familyDetailsDataHolder.ivChildPhoto1.setImageBitmap(familyDetailsData.getChildPhoto());
        familyDetailsDataHolder.tvChildName1.setText(familyDetailsData.getChild_name());
        familyDetailsDataHolder.tvWifeName1.setText(familyDetailsData.getChild_hus_or_wife_name());
        familyDetailsDataHolder.tvMobNo1.setText(familyDetailsData.getChild_phone_no());
        familyDetailsDataHolder.tvGrand1Child1.setText(familyDetailsData.getGrand_son_or_daug_name1());
        familyDetailsDataHolder.tvGrand2Child1.setText(familyDetailsData.getGrand_son_or_daug_name2());
        familyDetailsDataHolder.tvGrand3Child1.setText(familyDetailsData.getGrand_son_or_daug_name3());
        familyDetailsDataHolder.tvGrand4Child1.setText(familyDetailsData.getGrand_son_or_daug_name4());

        return row;
    }
    static class  FamilyDetailsDataHolder
    {
        TextView tvChildName1,tvWifeName1,tvMobNo1,tvGrand1Child1,tvGrand2Child1,tvGrand3Child1,tvGrand4Child1;
        ImageView ivChildPhoto1;
    }



}

