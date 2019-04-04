package com.yarolegovich.slidingrootnav.sample;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SANKAR on 3/30/2018.
 */

public class BloodAdapter extends ArrayAdapter {

    List list = new ArrayList();
    public BloodAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
    }


    public void add(Blood object) {
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
        BloodHolder bloodHolder;
        if(row == null){
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.blood_row_layout,parent,false);
            bloodHolder = new BloodHolder();

            bloodHolder.tvName = (TextView) row.findViewById(R.id.tvName);
            bloodHolder.tvPhNo = (TextView) row.findViewById(R.id.tvPhNo);
            bloodHolder.tvBG = (TextView) row.findViewById(R.id.tvBG);

            row.setTag(bloodHolder);
        }
        else {
            bloodHolder = (BloodHolder) row.getTag();
        }
        Blood blood = (Blood) this.getItem(position);
        bloodHolder.tvName.setText(blood.getName());
        bloodHolder.tvPhNo.setText(blood.getPhone_No());
        bloodHolder.tvBG.setText(blood.getBloodGroup());

        return row;
    }
    static class  BloodHolder
    {
        TextView tvName,tvPhNo,tvBG;
    }

}
