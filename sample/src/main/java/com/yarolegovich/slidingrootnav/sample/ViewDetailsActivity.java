package com.yarolegovich.slidingrootnav.sample;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.ListView;
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

public class ViewDetailsActivity extends AppCompatActivity {

    String type,group_id,searchKey;
    ProgressDialog loading;
    ListView listView;
    ViewDetailsAdapter viewDetailsAdapter;
    ViewDetailsAdapter searchCancelAdapter;
    AutoCompleteTextView suggestion_box;
    private List<String> listName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_details);

        suggestion_box = findViewById(R.id.suggestion_box);
        listName = new ArrayList<>();
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
        Bundle extras = getIntent().getExtras();
        group_id = extras.getString("group_id");

        listView = (ListView) findViewById(R.id.lvViewDetails);
        viewDetailsAdapter = new ViewDetailsAdapter(this, R.layout.view_details_row_layout);
        searchCancelAdapter = new ViewDetailsAdapter(this, R.layout.view_details_row_layout);
        loading = ProgressDialog.show(ViewDetailsActivity.this, "Fetching Data...","Please Wait...",true,true);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                type = "getHeadDetails";
                BackgroundWorkerJson backgroundWorker = new BackgroundWorkerJson();
                backgroundWorker.execute();
            }
        });
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tvId;
                String headId;
                tvId = (TextView)view.findViewById(R.id.tvId);
                headId = tvId.getText().toString();
                Intent intent = new Intent(ViewDetailsActivity.this,FamilyDetailsActivity.class);
                Bundle extras = new Bundle();
                extras.putString("headId",headId );
                intent.putExtras(extras);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onBackPressed() {
        finish();
    }

    public void goBack(View view) {
        finish();
    }

    public void cancelSearch(View view) {
        suggestion_box.clearListSelection();
        suggestion_box.setText("",null );
        //suggestion_box.setHint("Type to search");
        viewDetailsAdapter.list.clear();
        viewDetailsAdapter.list = searchCancelAdapter.list;
        listView.setAdapter(viewDetailsAdapter);
        viewDetailsAdapter.notifyDataSetChanged();
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
                if(type.equals("getHeadDetails")){
                    websiteUrl = "http://southern-electric.co.in/FamilyApp/getHeadDetails.php";
                    Log.w(type,type );

                    post_data = URLEncoder.encode("group_id", "UTF-8") + "=" + URLEncoder.encode(group_id, "UTF-8")
                            +"&"+ URLEncoder.encode("searchKey", "UTF-8") + "=" + URLEncoder.encode("All", "UTF-8")
                    ;
                }
                if(type.equals("Select")){
                    websiteUrl = "http://southern-electric.co.in/FamilyApp/getHeadDetails.php";
                    Log.w(type,type );

                    post_data = URLEncoder.encode("group_id", "UTF-8") + "=" + URLEncoder.encode(group_id, "UTF-8")
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

            if(type.equals("getHeadDetails")){
                Log.w(type,result );
                json_string = result;

                if(json_string != null)
                {
                    try {
                        jsonObject = new JSONObject(json_string);
                        jsonArray = jsonObject.getJSONArray("getHeadDetails");

                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jo = jsonArray.getJSONObject(i);

                            Bitmap headPhoto;
                            String headPhotoString,headName,headMobile,occupation,nativePlace,headEmail,id;

                            id = jo.getString("id");
                            headPhotoString = jo.getString("headPhoto");
                            headName = jo.getString("headName");
                            headMobile = jo.getString("headMobile");
                            occupation = jo.getString("occupation");
                            nativePlace = jo.getString("nativePlace");
                            headEmail = jo.getString("headEmail");

                            byte[] decodedString = Base64.decode(headPhotoString, Base64.DEFAULT);
                            headPhoto = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                            listName.add(headName);
                            ViewDetailsData viewDetailsData = new ViewDetailsData( headPhoto,headName,headMobile,occupation,nativePlace,headEmail,id);
                            viewDetailsAdapter.add(viewDetailsData);
                            searchCancelAdapter.add(viewDetailsData);
                        }
                        loading.dismiss();
                        listView.setAdapter(viewDetailsAdapter);

                        suggestion_box.setVisibility(View.VISIBLE);
                        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(
                                ViewDetailsActivity.this,
                                android.R.layout.simple_spinner_dropdown_item,
                                listName);
                        suggestion_box.setAdapter(arrayAdapter1);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else {
                    Log.w("JSON","null" );
                }
            }
            if(type.equals("Select")){
                Log.w(type,result );
                json_string = result;

                if(json_string != null)
                {
                    try {
                        viewDetailsAdapter.list.clear();
                        jsonObject = new JSONObject(json_string);
                        jsonArray = jsonObject.getJSONArray("getHeadDetails");

                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jo = jsonArray.getJSONObject(i);

                            Bitmap headPhoto;
                            String headPhotoString,headName,headMobile,occupation,nativePlace,headEmail,id;

                            id = jo.getString("id");
                            headPhotoString = jo.getString("headPhoto");
                            headName = jo.getString("headName");
                            headMobile = jo.getString("headMobile");
                            occupation = jo.getString("occupation");
                            nativePlace = jo.getString("nativePlace");
                            headEmail = jo.getString("headEmail");

                            byte[] decodedString = Base64.decode(headPhotoString, Base64.DEFAULT);
                            headPhoto = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                            ViewDetailsData viewDetailsData = new ViewDetailsData( headPhoto,headName,headMobile,occupation,nativePlace,headEmail,id);
                            viewDetailsAdapter.add(viewDetailsData);
                        }
                        loading.dismiss();
                        listView.setAdapter(viewDetailsAdapter);
                        viewDetailsAdapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else {
                    Log.w("JSON","null" );
                }
            }
        }
    }
}
