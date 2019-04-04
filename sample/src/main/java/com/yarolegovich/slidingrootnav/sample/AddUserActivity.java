package com.yarolegovich.slidingrootnav.sample;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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

public class AddUserActivity extends AppCompatActivity {

    EditText etPhoneNumber,etUserName;
    String userName,phoneNumber,type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etUserName = findViewById(R.id.etUserName);

    }


    public void onAddUser(View view) {
        userName = etUserName.getText().toString();
        phoneNumber = etPhoneNumber.getText().toString();
        type = "Add User";
        BackgroundWorker backgroundWorker = new BackgroundWorker();
        backgroundWorker.execute();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void goBack(View view) {
        finish();
    }
    public class BackgroundWorker extends AsyncTask<String,Void,String> {

        android.app.AlertDialog alertDialog;

        @Override
        protected String doInBackground(String... params) {



            try {
                String post_data = null;
                String login_url = null;

                if(type.equals("Add User")) {
                    login_url = "http://southern-electric.co.in/FamilyApp/AddUser.php";
                    post_data = URLEncoder.encode("userName","UTF-8")+"="+ URLEncoder.encode(userName,"UTF-8")+"&"
                            + URLEncoder.encode("phoneNumber","UTF-8")+"="+ URLEncoder.encode(phoneNumber,"UTF-8");
                }

                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
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
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result="";
                String line="";
                while((line = bufferedReader.readLine())!= null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }




            return null;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String result) {

            String f = result;
            Log.e("Login", result);
            if(type.equals("Add User")){
                Intent intent =  new Intent(AddUserActivity.this,login.class);
                startActivity(intent);

                Toast.makeText(AddUserActivity.this,result ,Toast.LENGTH_LONG ).show();

            }
        }


    }
}
