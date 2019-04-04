package com.yarolegovich.slidingrootnav.sample;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
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

public class Sign_UP extends AppCompatActivity {
    String type;
    EditText etUserName,etUserPwd1,etUserPwd2,etPhoneNumber,etEmail;
    String userName,userPwd1,userPwd2,phoneNumber,email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign__up);

        etUserName = findViewById(R.id.etUserName);
        etUserPwd1 = findViewById(R.id.etUserPwd1);
        etUserPwd2 = findViewById(R.id.etUserPwd2);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etEmail = findViewById(R.id.etEmail);

        TextView t1 = (TextView) findViewById(R.id.signup);
        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(Sign_UP.this,login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }

    public void onSignUp(View view) {
        userName = etUserName.getText().toString();
        userPwd1 = etUserPwd1.getText().toString();
        userPwd2 = etUserPwd2.getText().toString();
        phoneNumber = etPhoneNumber.getText().toString();
        email = etEmail.getText().toString();

        if(userName.trim().length()>0 && phoneNumber.trim().length()>0
                && userPwd1.trim().length()>0&& userPwd2.trim().length()>0
                && email.trim().length()>0){
            if(userPwd1.equals(userPwd2)){
                type = "sign_up";
                BackgroundWorker backgroundWorker = new BackgroundWorker();
                backgroundWorker.execute();
            }else {
                Toast.makeText(Sign_UP.this,"Password does not match" ,Toast.LENGTH_LONG ).show();
            }
        }else{
            Toast.makeText(Sign_UP.this,"Please enter all the details" ,Toast.LENGTH_LONG ).show();
        }
    }

    public class BackgroundWorker extends AsyncTask<String,Void,String> {

        android.app.AlertDialog alertDialog;
        @Override
        protected String doInBackground(String... params) {

            try {
                String post_data = null;
                String login_url = null;

                if(type.equals("sign_up")) {
                    login_url = "http://southern-electric.co.in/FamilyApp/sign_up.php";
                    post_data = URLEncoder.encode("userName","UTF-8")+"="+ URLEncoder.encode(userName,"UTF-8")
                            +"&"+ URLEncoder.encode("password","UTF-8")+"="+ URLEncoder.encode(userPwd1,"UTF-8")
                            +"&"+ URLEncoder.encode("confirm_password","UTF-8")+"="+ URLEncoder.encode(userPwd2,"UTF-8")
                            +"&"+ URLEncoder.encode("phNo","UTF-8")+"="+ URLEncoder.encode(phoneNumber,"UTF-8")
                            +"&"+ URLEncoder.encode("email","UTF-8")+"="+ URLEncoder.encode(email,"UTF-8")
                    ;
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
            Log.e("sign_up", result);
            if(type.equals("sign_up")){
                Toast.makeText(Sign_UP.this,result ,Toast.LENGTH_LONG ).show();

                Intent intent =  new Intent(Sign_UP.this,login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        }


    }
}
