package com.yarolegovich.slidingrootnav.sample;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

public class BloodActivity extends AppCompatActivity {
    Spinner spinner;
    ArrayAdapter<CharSequence> adapter;
    String BloodGroup;

    String json_string;
    JSONArray jsonArray;
    JSONObject jsonObject;

    BloodAdapter bloodAdapter;
    ListView listView;
    String type;

    TextView textView;
    int id = 10;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood);

        textView =  findViewById(R.id.tvBGShow);
        listView = (ListView)  findViewById(R.id.lvBlood);
        bloodAdapter = new BloodAdapter(this, R.layout.blood_row_layout);

        spinner = (Spinner)  findViewById(R.id.idSpinner);

        adapter = ArrayAdapter.createFromResource(this,R.array.Blood_Group_All,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                BloodGroup = adapterView.getItemAtPosition(i).toString();


                if(!BloodGroup.equals("All")){

                    if(id == 10){
                        //Log.e("Blood Group",BloodGroup );
                        /*
                        Fragment frg = null;
                        frg = getActivity().getSupportFragmentManager().findFragmentByTag("FilterFragment");
                        final FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                        ft.detach(frg);
                        ft.attach(frg);
                        ft.commit();
                        */
                        id++;
                    }

                }
                type = "getBloodDetails";
                BackgroundWorker_JSON backgroundWorker = new BackgroundWorker_JSON();
                backgroundWorker.execute();
                //Toast.makeText(this,BloodGroup,Toast.LENGTH_SHORT).show();

            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public class BackgroundWorker_JSON extends AsyncTask<String,Void,String> {
        String JSON_STRING;
        String json_url;

        @Override
        protected String doInBackground(String... params) {
            if(type.equals("getBloodDetails")){
                String donor_url;

                try {
                    URL url;
                    HttpURLConnection httpURLConnection;
                    donor_url = "http://southern-electric.co.in/FamilyApp/getBloodDetails.php";

                    url = new URL(donor_url);
                    httpURLConnection = (HttpURLConnection) url.openConnection();

                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data = URLEncoder.encode("BloodGroup","UTF-8")+"="+URLEncoder.encode(BloodGroup,"UTF-8");
                    bufferedWriter.write(post_data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();

                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder stringBuilder = new StringBuilder();
                    while ((JSON_STRING = bufferedReader.readLine()) != null){

                        stringBuilder.append(JSON_STRING);
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
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {

            if(type.equals("getBloodDetails")){

                json_string = result;

                if(json_string == null){
                    Toast.makeText(BloodActivity.this,"First Get JSON blood",Toast.LENGTH_LONG).show();
                }
                else {
                    bloodAdapter.list.clear();
                    try {
                        jsonObject = new JSONObject(json_string);
                        jsonArray = jsonObject.getJSONArray("getBloodDetails");

                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jo = jsonArray.getJSONObject(i);

                            String Name,  Phone_No, BloodGroup;

                            BloodGroup = jo.getString("BloodGroup");
                            Name = jo.getString("Name");
                            Phone_No = jo.getString("PhoneNo");

                            Blood blood = new Blood(Name, Phone_No, BloodGroup);
                            bloodAdapter.add(blood);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    listView.setAdapter(bloodAdapter);
                    bloodAdapter.notifyDataSetChanged();
                    id = 10;
                    if(!BloodGroup.equals("All")){
                        textView.setText(BloodGroup);
                    }
                    else {
                        textView.setText("All");
                    }
                }

            }

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
}

