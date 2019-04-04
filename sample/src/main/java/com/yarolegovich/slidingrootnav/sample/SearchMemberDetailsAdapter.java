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

public class SearchMemberDetailsAdapter extends ArrayAdapter {

    List list = new ArrayList();
    public SearchMemberDetailsAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
    }


    public void add(SearchMemberDetailsData object) {
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
        SearchMemberDetailsAdapter.SearchMemberDetailsDataHolder searchMemberDetailsDataHolder;
        if(row == null){
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.search_member_details_row_layout,parent,false);
            searchMemberDetailsDataHolder = new SearchMemberDetailsAdapter.SearchMemberDetailsDataHolder();

            searchMemberDetailsDataHolder.tvHeadName = (TextView) row.findViewById(R.id.tvHeadName);
            searchMemberDetailsDataHolder.tvHeadMobile = (TextView) row.findViewById(R.id.tvHeadMobile);
            searchMemberDetailsDataHolder.tvOfficeAddress = (TextView) row.findViewById(R.id.tvOfficeAddress);
            searchMemberDetailsDataHolder.tvOfficePhNo = (TextView) row.findViewById(R.id.tvOfficePhNo);
            searchMemberDetailsDataHolder.tvResidenceAddress = (TextView) row.findViewById(R.id.tvResidenceAddress);
            searchMemberDetailsDataHolder.tvResidencePhNo = (TextView) row.findViewById(R.id.tvResidencePhNo);

            row.setTag(searchMemberDetailsDataHolder);
        }
        else {
            searchMemberDetailsDataHolder = (SearchMemberDetailsAdapter.SearchMemberDetailsDataHolder) row.getTag();
        }
        SearchMemberDetailsData searchMemberDetailsData = (SearchMemberDetailsData) this.getItem(position);

        searchMemberDetailsDataHolder.tvHeadName.setText(searchMemberDetailsData.getHeadName());
        searchMemberDetailsDataHolder.tvHeadMobile.setText(searchMemberDetailsData.getHeadMobile());
        searchMemberDetailsDataHolder.tvOfficeAddress.setText(searchMemberDetailsData.getOfficeAddress());
        searchMemberDetailsDataHolder.tvOfficePhNo.setText(searchMemberDetailsData.getOfficePhNo());
        searchMemberDetailsDataHolder.tvResidenceAddress.setText(searchMemberDetailsData.getResidenceAddress());
        searchMemberDetailsDataHolder.tvResidencePhNo.setText(searchMemberDetailsData.getResidencePhNo());

        return row;
    }
    static class  SearchMemberDetailsDataHolder
    {
        TextView tvHeadName,tvHeadMobile,tvResidencePhNo,tvOfficeAddress,tvOfficePhNo,tvResidenceAddress;
    }



}
