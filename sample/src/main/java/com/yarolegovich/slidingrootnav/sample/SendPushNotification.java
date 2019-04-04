package com.yarolegovich.slidingrootnav.sample;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.yarolegovich.slidingrootnav.sample.Firebase.EndPoints;
import com.yarolegovich.slidingrootnav.sample.Firebase.MyVolley;
import com.yarolegovich.slidingrootnav.sample.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SendPushNotification extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    private Button buttonSendPush;
    private RadioGroup radioGroup;
    private Spinner spinner;
    private ProgressDialog progressDialog;

    private EditText editTextTitle, editTextMessage, editTextImage;
    TextView tvNotificationImage;
    ImageView ivNotificationImage;
    private int PICK_IMAGE = 100,CAPTURE_IMAGE = 101;
    Bitmap bitmap = null;
    String bitmapPath,notification_image_string;
    ProgressDialog loading;

    private boolean isSendAllChecked;
    private List<String> devices;
    String type,textTitle,textMessage,userName;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_push_notification2);

        session = new SessionManager(getApplicationContext());
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        // name
        userName = user.get(SessionManager.KEY_NAME);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        spinner = (Spinner) findViewById(R.id.spinnerDevices);
        buttonSendPush = (Button) findViewById(R.id.buttonSendPush);

        editTextTitle = (EditText) findViewById(R.id.editTextTitle);
        editTextMessage = (EditText) findViewById(R.id.editTextMessage);
        editTextImage = (EditText) findViewById(R.id.editTextImageUrl);
        tvNotificationImage = findViewById(R.id.tvNotificationImage);
        ivNotificationImage = findViewById(R.id.ivNotificationImage);

        tvNotificationImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder notifyLocationServices = new AlertDialog.Builder(SendPushNotification.this);
                notifyLocationServices.setTitle("Choose the option");
                notifyLocationServices.setMessage("Select");
                notifyLocationServices.setPositiveButton("Galary", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        openGallery();
                    }
                });
                notifyLocationServices.setNegativeButton("Camera", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        openCameraPhoto();
                    }
                });
                notifyLocationServices.show();
            }
        });
        devices = new ArrayList<>();

        radioGroup.setOnCheckedChangeListener(this);
        buttonSendPush.setOnClickListener(this);

        loadRegisteredDevices();
    }
    public void openCameraPhoto() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Family";
        File file = new File(path);
        if(!file.exists()){
            file.mkdirs();
        }

        /*
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => "+c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
        String formattedDate = df.format(c.getTime());

        File imgae_file = new File(file,"IDOL_IMAGE"+formattedDate+".jpg");
        */
        File imgae_file = new File(file,"notification_image.jpg");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION| Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(getApplicationContext(), "com.project.naveen.family", imgae_file);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
        }else {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imgae_file));
        }
        startActivityForResult(intent,CAPTURE_IMAGE);
    }

    public void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION| Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == CAPTURE_IMAGE){
            String path,externalPath;
            externalPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            bitmapPath = "/Family/"+"notification_image.jpg";
            path = externalPath + bitmapPath;

            bitmap = BitmapFactory.decodeFile(path);
            FileOutputStream out = null;
            try {
                File file;
                file =new File(externalPath+"/Family","notification_image.jpg");
                out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 25, out); // bmp is your Bitmap instance

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            bitmapPath = path;
            bitmap = BitmapFactory.decodeFile(bitmapPath);
            Log.e("Bitmap",path );
            if(bitmap!=null){
                tvNotificationImage.setText(bitmapPath);
                ivNotificationImage.setVisibility(View.VISIBLE);
                ivNotificationImage.setImageBitmap(bitmap);
                Log.e("Camera","not null" );
            }
            else
                Log.e("Camera"," null" );
            //imageView.setImageBitmap(bitmap);
        }
        if(resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            //found mistake=> if(requestCode == RESULT_OK && requestCode == PICK_IMAGE)
            Uri imageUri = data.getData();
            // try1=>fail - Because of mistake
            String[] projection={MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(imageUri,projection,null,null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(projection[0]);
            String filePath = cursor.getString(columnIndex);
            cursor.close();

            bitmapPath = filePath;
            bitmap = BitmapFactory.decodeFile(filePath);

            String path,externalPath;
            externalPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            bitmapPath = "/Family/"+"notification_image.jpg";
            path = externalPath + bitmapPath;

            FileOutputStream out = null;
            try {
                File file;
                file =new File(externalPath+"/Family","notification_image.jpg");
                out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 25, out); // bmp is your Bitmap instance

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            bitmapPath = path;
            bitmap = BitmapFactory.decodeFile(bitmapPath);
            Log.e("path",path );
            Log.e("filePath",filePath );
            if(bitmap!=null){
                ivNotificationImage.setVisibility(View.VISIBLE);
                ivNotificationImage.setImageBitmap(bitmap);
                tvNotificationImage.setText(bitmapPath);
                Log.e("Galary","not null" );
            }
            else
                Log.e("Galery"," null" );
        }
    }
    @Override
    public void onBackPressed() {
        //finish();
        Intent intent = new Intent(SendPushNotification.this,SampleActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    public void goBack(View view) {
        //finish();
        Intent intent = new Intent(SendPushNotification.this,SampleActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    //method to load all the devices from database
    private void loadRegisteredDevices() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching Devices...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, EndPoints.URL_FETCH_DEVICES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                JSONArray jsonDevices = obj.getJSONArray("devices");

                                for (int i = 0; i < jsonDevices.length(); i++) {
                                    JSONObject d = jsonDevices.getJSONObject(i);
                                    devices.add(d.getString("email"));
                                }

                                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                                        SendPushNotification.this,
                                        android.R.layout.simple_spinner_dropdown_item,
                                        devices);

                                spinner.setAdapter(arrayAdapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {

        };
        MyVolley.getInstance(this).addToRequestQueue(stringRequest);
    }

    //this method will send the push
    //from here we will call sendMultiple() or sendSingle() push method
    //depending on the selection
    private void sendPush() {
        if (isSendAllChecked) {
            sendMultiplePush();
        } else {
            sendSinglePush();
        }
    }

    private void sendMultiplePush() {
        //Toast.makeText(SendPushNotification.this,"sendMultiplePush" ,Toast.LENGTH_LONG ).show();
        final String title = editTextTitle.getText().toString();
        final String message = editTextMessage.getText().toString();
        final String image = editTextImage.getText().toString();

        progressDialog.setMessage("Sending Push");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.URL_SEND_MULTIPLE_PUSH,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        //Toast.makeText(SendPushNotification.this, response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("title", title);
                params.put("message", message);

                if (!TextUtils.isEmpty(image))
                    params.put("image", image);
                return params;
            }
        };

        MyVolley.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void sendSinglePush() {
        //Toast.makeText(SendPushNotification.this,"sendSinglePush" ,Toast.LENGTH_LONG ).show();
        final String title = editTextTitle.getText().toString();
        final String message = editTextMessage.getText().toString();
        final String image = editTextImage.getText().toString();
        final String email = spinner.getSelectedItem().toString();

        progressDialog.setMessage("Sending Push");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.URL_SEND_SINGLE_PUSH,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        //Toast.makeText(SendPushNotification.this, response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("title", title);
                params.put("message", message);

                if (!TextUtils.isEmpty(image))
                    params.put("image", image);

                params.put("email", email);
                return params;
            }
        };

        MyVolley.getInstance(this).addToRequestQueue(stringRequest);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.radioButtonSendAll:
                isSendAllChecked = true;
                spinner.setEnabled(false);
                break;

            case R.id.radioButtonSendOne:
                isSendAllChecked = false;
                spinner.setEnabled(true);
                break;

        }
    }

    @Override
    public void onClick(View view) {
        isSendAllChecked = true;
        //calling the method send push on button click
        sendPush();

        textTitle = editTextTitle.getText().toString();
        textMessage = editTextMessage.getText().toString();

        loading = ProgressDialog.show(SendPushNotification.this, "Processing...","Please Wait...",true,true);

        bitmap = BitmapFactory.decodeFile(bitmapPath);
        if(bitmap!=null){
            Log.e("bitmapPath",bitmapPath );
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 25, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream .toByteArray();

            notification_image_string = Base64.encodeToString(byteArray, Base64.DEFAULT);
            Log.e("notification_image_str",notification_image_string );
            type = "add_notification";
            BackgroundWorker backgroundWorker = new BackgroundWorker();
            backgroundWorker.execute();
        }else {
            type = "add_notification1";
            BackgroundWorker backgroundWorker = new BackgroundWorker();
            backgroundWorker.execute();
            //Toast.makeText(getApplicationContext(),"bitmap null", Toast.LENGTH_LONG).show();
        }

    }

    public void onReadNotifications(View view) {
        Intent intent =  new Intent(SendPushNotification.this,Notification_read.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public class BackgroundWorker extends AsyncTask<String,Void,String> {

        android.app.AlertDialog alertDialog;

        @Override
        protected String doInBackground(String... params) {



            try {
                String post_data = null;
                String login_url = null;

                if(type.equals("add_notification")) {
                    login_url = "http://southern-electric.co.in/FamilyApp/add_notification.php";
                    post_data = URLEncoder.encode("textTitle","UTF-8")+"="+ URLEncoder.encode(textTitle,"UTF-8")
                            +"&"+ URLEncoder.encode("textMessage","UTF-8")+"="+ URLEncoder.encode(textMessage,"UTF-8")
                            +"&"+ URLEncoder.encode("userName","UTF-8")+"="+ URLEncoder.encode(userName,"UTF-8")
                            +"&"+ URLEncoder.encode("notification_image_string", "UTF-8") + "=" + URLEncoder.encode(notification_image_string, "UTF-8")
                    ;
                }
                if(type.equals("add_notification1")) {
                    login_url = "http://southern-electric.co.in/FamilyApp/add_notification.php";
                    post_data = URLEncoder.encode("textTitle","UTF-8")+"="+ URLEncoder.encode(textTitle,"UTF-8")
                            +"&"+ URLEncoder.encode("textMessage","UTF-8")+"="+ URLEncoder.encode(textMessage,"UTF-8")
                            +"&"+ URLEncoder.encode("userName","UTF-8")+"="+ URLEncoder.encode(userName,"UTF-8")
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
            Log.e("add_notification", result);
            if(type.equals("add_notification")){
                editTextTitle.setText("");
                editTextMessage.setText("");
                Toast.makeText(SendPushNotification.this,result ,Toast.LENGTH_LONG ).show();
                loading.dismiss();
            }
            if(type.equals("add_notification1")){
                editTextTitle.setText("");
                editTextMessage.setText("");
                Toast.makeText(SendPushNotification.this,result ,Toast.LENGTH_LONG ).show();
                loading.dismiss();
            }
        }


    }
}
