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
import android.widget.ListView;
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

import me.leolin.shortcutbadger.ShortcutBadger;

public class Notification_read extends AppCompatActivity {
    ListView listView;
    ProgressDialog loading;
    NotificationAdapter notificationAdapter;
    String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_read);

        SessionManager session;
        session = new SessionManager(getApplicationContext());
        session.createSession(0);

        ShortcutBadger.applyCount(getApplicationContext(), 0);//for 1.1.4+
        listView = (ListView) findViewById(R.id.lvNotification);

        notificationAdapter = new NotificationAdapter(this, R.layout.notification_read_row_layout);
        loading = ProgressDialog.show(Notification_read.this, "Fetching Data...","Please Wait...",true,true);
        type = "getNotificationDetails";
        BackgroundWorkerJson backgroundWorker = new BackgroundWorkerJson();
        backgroundWorker.execute();

    }

    @Override
    public void onBackPressed() {
        //finish();
        Intent intent = new Intent(Notification_read.this,SendPushNotification.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    public void goBack(View view) {
        //finish();
        Intent intent = new Intent(Notification_read.this,SendPushNotification.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    @Override
    protected void onPause() {
        super.onPause();
        loading.dismiss();
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
                if(type.equals("getNotificationDetails")){
                    websiteUrl = "http://southern-electric.co.in/FamilyApp/getNotificationDetails.php";
                    Log.e(type,type );
                }

                URL url = new URL(websiteUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                /*
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                */

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

            if(type.equals("getNotificationDetails")){
                Log.e(type,result );
                json_string = result;

                if(json_string != null)
                {
                    try {
                        jsonObject = new JSONObject(json_string);
                        jsonArray = jsonObject.getJSONArray("getNotificationDetails");
                        
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jo = jsonArray.getJSONObject(i);

                            String child_image;
                            Bitmap childPhoto;
                            String title,message,user_name,date;
                            title = jo.getString("title");
                            child_image = jo.getString("image1");
                            message = jo.getString("message");
                            user_name = jo.getString("user_name");
                            date = jo.getString("date");

                            byte[] decodedString = Base64.decode(child_image, Base64.DEFAULT);
                            childPhoto = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                            NotificationData notificationData = new NotificationData( title,message,user_name,date,childPhoto);
                            notificationAdapter.add(notificationData);
                        }
                        loading.dismiss();
                        listView.setAdapter(notificationAdapter);


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
