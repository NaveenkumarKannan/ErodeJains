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
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class ConfirmActivity extends AppCompatActivity {
    TextView tvHeadName,tvBgHead,tvWifeName,tvBgWife,tvFatherName,tvGhotre,tvHeadEmail,tvHeadMobile,tvNativePlace,tvOccupation
            ,tvOfficeAddress,tvOfficePhNo,tvResidenceAddress,tvResidencePhNo,tvNoOfChildren,tvGroup_id;
    ;
    TextView tvHeadPhoto;
    String[] headArray=new String[11],addressArray=new String[6];

    String headName,bgHead,wifeName,bgWife,fatherName,ghotre,headEmail,headMobile,nativePlace,occupation,bitmapPath;
    String officeAddress,officePhNo,residenceAddress,residencePhNo,noOfChildren,group_id;

    String type;

    Bitmap bitmap = null;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);

        Bundle Extras = getIntent().getExtras();
        headArray = Extras.getStringArray("headArray");
        Log.w("headArray: ",headArray[10]);
        addressArray = Extras.getStringArray("addressArray");
        Log.w("addressArray: ",addressArray[4]);

        tvHeadName= findViewById(R.id.tvHeadName);
        tvBgHead= findViewById(R.id.tvBgHead);
        tvWifeName= findViewById(R.id.tvWifeName);
        tvBgWife= findViewById(R.id.tvBgWife);
        tvFatherName= findViewById(R.id.tvFatherName);
        tvGhotre= findViewById(R.id.tvGhotre);
        tvHeadEmail= findViewById(R.id.tvHeadEmail);
        tvHeadMobile= findViewById(R.id.tvHeadMobile);
        tvNativePlace= findViewById(R.id.tvNativePlace);
        tvOccupation= findViewById(R.id.tvOccupation);
        tvHeadPhoto = findViewById(R.id.tvHeadPhoto);

        tvOfficeAddress= findViewById(R.id.tvOfficeAddress);
        tvOfficePhNo= findViewById(R.id.tvOfficePhNo);
        tvResidenceAddress= findViewById(R.id.tvResidenceAddress);
        tvResidencePhNo= findViewById(R.id.tvResidencePhNo);
        tvNoOfChildren = findViewById(R.id.tvNoOfChildren);
        tvGroup_id = findViewById(R.id.tvGroup_id);

        headName = headArray[0];
        tvHeadName.setText(headName);

        bgHead = headArray[1];
        tvBgHead.setText(bgHead);

        wifeName = headArray[2];
        tvWifeName.setText(wifeName);

        bgWife = headArray[3];
        tvBgWife.setText(bgWife);

        fatherName = headArray[4];
        tvFatherName.setText(fatherName);

        ghotre = headArray[5];
        tvGhotre.setText(ghotre);

        headEmail=headArray[6];
        tvHeadEmail.setText(headEmail);

        headMobile = headArray[7];
        tvHeadMobile.setText(headMobile);

        nativePlace = headArray[8];
        tvNativePlace.setText(nativePlace);

        occupation = headArray[9];
        tvOccupation.setText(occupation);

        bitmapPath = headArray[10];
        tvHeadPhoto.setText(bitmapPath);


        officeAddress = addressArray[0];
        tvOfficeAddress.setText(officeAddress);

        officePhNo=addressArray[1];
        tvOfficePhNo.setText(officePhNo);

        residenceAddress=addressArray[2];
        tvResidenceAddress.setText(residenceAddress);

        residencePhNo=addressArray[3];
        tvResidencePhNo.setText(residencePhNo);

        noOfChildren=addressArray[4];
        tvNoOfChildren.setText(noOfChildren);

        group_id = addressArray[5];
        tvGroup_id.setText(group_id);
    }
    public void nextPage(View view) {
        Intent intent = new Intent(ConfirmActivity.this,child2Activity.class);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {
        finish();
    }

    public void goBack(View view) {
        finish();
    }
    public void onConfirm(View view) {
        loading = ProgressDialog.show(ConfirmActivity.this, "Processing...","Please Wait...",true,true);

        Log.w("bitmapPath",bitmapPath );
        bitmap = BitmapFactory.decodeFile(bitmapPath);
        if(bitmap!=null){
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 25, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream .toByteArray();

            bitmapPath = Base64.encodeToString(byteArray, Base64.DEFAULT);
            Log.w("bitmapPath",bitmapPath );
            type = "insertData";
            BackgroundWorkerJson backgroundWorker = new BackgroundWorkerJson();
            backgroundWorker.execute();
        }else {
            loading.dismiss();
            Toast.makeText(getApplicationContext(),"bitmap null", Toast.LENGTH_LONG).show();
        }
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
                if(type.equals("getUrl")){
                    //websiteUrl = "http://arulaudios.com/android/wideview/getUrl.php";
                    websiteUrl = "http://southern-electric.co.in/FamilyApp/getUrl.php";
                    Log.w(type,type );

//                    post_data = URLEncoder.encode("phNo", "UTF-8") + "=" + URLEncoder.encode(phNo, "UTF-8")
                    //                          +"&"+URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8")
                    //                ;
                }else if(type.equals("insertData")){
                    websiteUrl = "http://southern-electric.co.in/FamilyApp/insertUrl.php";
                    Log.w(type,type );

                    post_data = URLEncoder.encode("headName", "UTF-8") + "=" + URLEncoder.encode(headName, "UTF-8")
                            +"&"+ URLEncoder.encode("bgHead", "UTF-8") + "=" + URLEncoder.encode(bgHead, "UTF-8")
                            +"&"+ URLEncoder.encode("wifeName", "UTF-8") + "=" + URLEncoder.encode(wifeName, "UTF-8")
                            +"&"+ URLEncoder.encode("bgWife", "UTF-8") + "=" + URLEncoder.encode(bgWife, "UTF-8")
                            +"&"+ URLEncoder.encode("fatherName", "UTF-8") + "=" + URLEncoder.encode(fatherName, "UTF-8")
                            +"&"+ URLEncoder.encode("ghotre", "UTF-8") + "=" + URLEncoder.encode(ghotre, "UTF-8")
                            +"&"+ URLEncoder.encode("headEmail", "UTF-8") + "=" + URLEncoder.encode(headEmail, "UTF-8")
                            +"&"+ URLEncoder.encode("headMobile", "UTF-8") + "=" + URLEncoder.encode(headMobile, "UTF-8")
                            +"&"+ URLEncoder.encode("nativePlace", "UTF-8") + "=" + URLEncoder.encode(nativePlace, "UTF-8")
                            +"&"+ URLEncoder.encode("occupation", "UTF-8") + "=" + URLEncoder.encode(occupation, "UTF-8")
                            +"&"+ URLEncoder.encode("bitmapPath", "UTF-8") + "=" + URLEncoder.encode(bitmapPath, "UTF-8")
                            +"&"+ URLEncoder.encode("officeAddress", "UTF-8") + "=" + URLEncoder.encode(officeAddress, "UTF-8")
                            +"&"+ URLEncoder.encode("officePhNo", "UTF-8") + "=" + URLEncoder.encode(officePhNo, "UTF-8")
                            +"&"+ URLEncoder.encode("residenceAddress", "UTF-8") + "=" + URLEncoder.encode(residenceAddress, "UTF-8")
                            +"&"+ URLEncoder.encode("residencePhNo", "UTF-8") + "=" + URLEncoder.encode(residencePhNo, "UTF-8")
                            +"&"+ URLEncoder.encode("noOfChildren", "UTF-8") + "=" + URLEncoder.encode(noOfChildren, "UTF-8")
                            +"&"+ URLEncoder.encode("group_id", "UTF-8") + "=" + URLEncoder.encode(group_id, "UTF-8")
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

            if(type.equals("getUrl")){
                Log.w(type,result );
                json_string = result;

                if(json_string != null)
                {
                    try {
                        String liveVideoURI=null;
                        jsonObject = new JSONObject(json_string);
                        jsonArray = jsonObject.getJSONArray("getUrl");

                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jo = jsonArray.getJSONObject(i);

                            liveVideoURI = jo.getString("url");

                        }
                        if(liveVideoURI==null){
                            Toast.makeText(getApplicationContext(),"Url not found" , Toast.LENGTH_LONG ).show();
                        }else {
                            //setupExoPlayer(bundle);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else {
                    Log.w("JSON","null" );
                }
            }else if(type.equals("insertData")){
                if(result !=null)
                    Log.w(type,result );
                else
                    Log.w(type,"null");
                loading.dismiss();
                Toast.makeText(getApplicationContext(),result, Toast.LENGTH_LONG ).show();
                int noOfChildrenInt = Integer.parseInt(noOfChildren);
                if(noOfChildrenInt > 0){
                    Intent intent = new Intent(ConfirmActivity.this,child1Activity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    Bundle extras = new Bundle();
                    extras.putInt("noOfChildrenInt", noOfChildrenInt);
                    extras.putInt("countChild", 1);
                    extras.putString("headName",headName );
                    intent.putExtras(extras);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(ConfirmActivity.this,SampleActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }

            }
        }
    }
}
