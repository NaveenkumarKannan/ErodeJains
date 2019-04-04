package com.yarolegovich.slidingrootnav.sample;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class SearchMember extends AppCompatActivity {
    String type,searchOption,searchKey = "hi";
    ListView listView;
    //ProgressDialog //loading;
    SearchMemberDetailsAdapter searchMemberDetailsAdapter;
    private List<String> listName,listPhNo;
    Spinner spinnerSearchOption,spinnerSearch;
    AutoCompleteTextView suggestion_box;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_member);

        suggestion_box = findViewById(R.id.suggestion_box);
        listName = new ArrayList<>();
        listPhNo = new ArrayList<>();
        spinnerSearchOption = (Spinner) findViewById(R.id.spinnerSearchOption);
        spinnerSearch = (Spinner) findViewById(R.id.spinnerSearch);

        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this,R.array.searchOption,android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSearchOption.setAdapter(arrayAdapter);

        listView = (ListView) findViewById(R.id.lvViewDetails);
        searchMemberDetailsAdapter = new SearchMemberDetailsAdapter(this, R.layout.search_member_details_row_layout);

        spinnerSearchOption.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                searchOption = adapterView.getItemAtPosition(i).toString();

                if(searchOption.equals("Select")){
                    type = "Select";
                    suggestion_box.setVisibility(View.GONE);
                }else if(searchOption.equals("Search By Name")){
                    type = "Search";
                    suggestion_box.setVisibility(View.VISIBLE);
                    ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(
                            SearchMember.this,
                            android.R.layout.simple_spinner_dropdown_item,
                            listName);
                    spinnerSearch.setAdapter(arrayAdapter1);
                    suggestion_box.setAdapter(arrayAdapter1);
                }else if(searchOption.equals("Search By Phone number")){
                    type = "Search";
                    suggestion_box.setVisibility(View.VISIBLE);
                    ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(
                            SearchMember.this,
                            android.R.layout.simple_spinner_dropdown_item,
                            listPhNo);
                    spinnerSearch.setAdapter(arrayAdapter1);
                    suggestion_box.setAdapter(arrayAdapter1);
                }
                //loading = ProgressDialog.show(SearchMember.this, "Fetching Data...","Please Wait...",true,true);

                BackgroundWorkerJson backgroundWorker = new BackgroundWorkerJson();
                backgroundWorker.execute();

            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        suggestion_box.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                searchKey = adapterView.getItemAtPosition(i).toString();

                //loading = ProgressDialog.show(SearchMember.this, "Fetching Data...","Please Wait...",true,true);

                Log.e("searchKey",searchKey );
                type = "Select";
                BackgroundWorkerJson backgroundWorker = new BackgroundWorkerJson();
                backgroundWorker.execute();
            }
        });
        /*
        spinnerSearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                searchKey = adapterView.getItemAtPosition(i).toString();

                //loading = ProgressDialog.show(SearchMember.this, "Fetching Data...","Please Wait...",true,true);

                Log.e("searchKey",searchKey );
                type = "Select";
                BackgroundWorkerJson backgroundWorker = new BackgroundWorkerJson();
                backgroundWorker.execute();

            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tvId;
                String headId;
                tvId = (TextView)view.findViewById(R.id.tvId);
                headId = tvId.getText().toString();
                Intent intent = new Intent(SearchMember.this,FamilyDetailsActivity.class);
                Bundle extras = new Bundle();
                extras.putString("headId",headId );
                intent.putExtras(extras);
                startActivity(intent);
            }
        });
        */
    }
    @Override
    public void onBackPressed() {
        finish();
    }

    public void goBack(View view) {
        finish();
    }

    public class BackgroundWorkerJson extends AsyncTask<String,Void,String> {
        String json_string;
        JSONArray jsonArray;
        JSONObject jsonObject;

        @Override
        protected String doInBackground(String... params) {

            try {
                String post_data = null;
                String websiteUrl = null;
                if(type.equals("Select")){
                    websiteUrl = "http://southern-electric.co.in/FamilyApp/SearchDetails.php";
                    Log.e(type,type );

                    post_data = URLEncoder.encode("searchOption", "UTF-8") + "=" + URLEncoder.encode(searchOption, "UTF-8")
                            +"&"+ URLEncoder.encode("searchKey", "UTF-8") + "=" + URLEncoder.encode(searchKey, "UTF-8")
                    ;
                }

                URL url = new URL(websiteUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {

            if(type.equals("Select")){
                Log.e(type,result );
                json_string = result;

                if(json_string != null)
                {
                    try {
                        searchMemberDetailsAdapter.list.clear();
                        jsonObject = new JSONObject(json_string);
                        jsonArray = jsonObject.getJSONArray("getHeadDetails");

                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jo = jsonArray.getJSONObject(i);


                            String headName,headMobile,officeAddress,officePhNo,residenceAddress,residencePhNo;
                            headName = jo.getString("headName");
                            headMobile = jo.getString("headMobile");

                            officeAddress = jo.getString("officeAddress");
                            officePhNo = jo.getString("officePhNo");
                            residenceAddress = jo.getString("residenceAddress");
                            residencePhNo = jo.getString("residencePhNo");

                            listName.add(headName);
                            listPhNo.add(headMobile);

                            SearchMemberDetailsData searchMemberDetailsData = new SearchMemberDetailsData( headName,headMobile,officeAddress,officePhNo,residenceAddress,residencePhNo);
                            searchMemberDetailsAdapter.add(searchMemberDetailsData);
                        }
                        //loading.dismiss();

                        listView.setAdapter(searchMemberDetailsAdapter);
                        searchMemberDetailsAdapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else {
                    Log.e("JSON","null" );
                }
            }else if(type.equals("insertData")){
                Toast.makeText(getApplicationContext(),result , Toast.LENGTH_LONG ).show();
            }
            if(type.equals("Select")){
                Log.e(type,result );
                json_string = result;

                if(json_string != null)
                {
                    try {
                        searchMemberDetailsAdapter.list.clear();
                        jsonObject = new JSONObject(json_string);
                        jsonArray = jsonObject.getJSONArray("getHeadDetails");

                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jo = jsonArray.getJSONObject(i);


                            String headName,headMobile,officeAddress,officePhNo,residenceAddress,residencePhNo;
                            headName = jo.getString("headName");
                            headMobile = jo.getString("headMobile");

                            officeAddress = jo.getString("officeAddress");
                            officePhNo = jo.getString("officePhNo");
                            residenceAddress = jo.getString("residenceAddress");
                            residencePhNo = jo.getString("residencePhNo");

                            SearchMemberDetailsData searchMemberDetailsData = new SearchMemberDetailsData( headName,headMobile,officeAddress,officePhNo,residenceAddress,residencePhNo);
                            searchMemberDetailsAdapter.add(searchMemberDetailsData);
                        }
                        //loading.dismiss();

                        listView.setAdapter(searchMemberDetailsAdapter);
                        searchMemberDetailsAdapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else {
                    Log.e("JSON","null" );
                }
            }else if(type.equals("insertData")){
                Toast.makeText(getApplicationContext(),result , Toast.LENGTH_LONG ).show();
            }
        }
    }
}
