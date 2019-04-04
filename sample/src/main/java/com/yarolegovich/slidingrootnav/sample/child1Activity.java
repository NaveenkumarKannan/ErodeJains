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
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class child1Activity extends AppCompatActivity {
    int noOfChildrenInt ,countChild;
    TextView tvCountChild;
    String headName,type,child_type;

    Bitmap bitmap = null;
    ProgressDialog loading;

    EditText etChildName,etBgChild,etChildMobNo,etWifeName,etBgWife,etGsonDtr1,etBgGsonDtr1,etGsonDtr2;
    EditText etBgGsonDtr2,etGsonDtr3,etBgGsonDtr3,etGsonDtr4,etBgGsonDtr4,etHomeMobNo;
    
    TextView tvChildPhoto;
    ImageView ivChildPhoto;

    String childName,bgChild,childMobNo,wifeName,bgWife,gsonDtr1,bgGsonDtr1,gsonDtr2, 
            bgGsonDtr2,gsonDtr3,bgGsonDtr3,gsonDtr4,bgGsonDtr4,homeMobNo,bitmapPath,child_image;
    private int PICK_IMAGE = 100,CAPTURE_IMAGE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child1);
        
        etChildName= findViewById(R.id.etChildName);
        etBgChild= findViewById(R.id.etBgChild);
        etChildMobNo= findViewById(R.id.etChildMobNo);
        etWifeName= findViewById(R.id.etWifeName);
        etBgWife= findViewById(R.id.etBgWife);
        etGsonDtr1= findViewById(R.id.etGsonDtr1);
        etBgGsonDtr1= findViewById(R.id.etBgGsonDtr1);
        etGsonDtr2= findViewById(R.id.etGsonDtr2);
        tvChildPhoto = findViewById(R.id.tvChildPhoto);

        ivChildPhoto = findViewById(R.id.ivChildPhoto);

        etBgGsonDtr2= findViewById(R.id.etBgGsonDtr2);
        etGsonDtr3= findViewById(R.id.etGsonDtr3);
        etBgGsonDtr3= findViewById(R.id.etBgGsonDtr3);
        etGsonDtr4= findViewById(R.id.etGsonDtr4);
        etBgGsonDtr4 = findViewById(R.id.etBgGsonDtr4);
        etHomeMobNo = findViewById(R.id.etHomeMobNo);

        Bundle extras = getIntent().getExtras();
        noOfChildrenInt = extras.getInt("noOfChildrenInt");
        countChild = extras.getInt("countChild");
        headName = extras.getString("headName");

        tvCountChild = findViewById(R.id.tvCountChild);
        child_type = "Child "+countChild;
        tvCountChild.setText(child_type+" Details");
        child_type = "Child_"+countChild;
        /*
        noOfChildrenInt = 1;
        countChild = 1;
        headName = "Rajini";
        
        bitmapPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Family/"+child_type+"_image.jpg";
        bitmap = BitmapFactory.decodeFile(bitmapPath);
        ivChildPhoto.setVisibility(View.VISIBLE);
        ivChildPhoto.setImageBitmap(bitmap);
        
        */
        Log.e("countChild","countChild" +String.valueOf(countChild)+"\n"+
                "noOfChildrenInt" +String.valueOf(noOfChildrenInt));

        tvChildPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder notifyLocationServices = new AlertDialog.Builder(child1Activity.this);
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
    }
    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(),"You can't go back. Kindly fill all the details.", Toast.LENGTH_LONG ).show();
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
        File imgae_file = new File(file,child_type+"_image.jpg");
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
            bitmapPath = "/Family/"+child_type+"_image.jpg";
            path = externalPath + bitmapPath;

            bitmap = BitmapFactory.decodeFile(path);
            FileOutputStream out = null;
            try {
                File file;
                file =new File(externalPath+"/Family",child_type+"_image.jpg");
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
                tvChildPhoto.setText(bitmapPath);
                ivChildPhoto.setVisibility(View.VISIBLE);
                ivChildPhoto.setImageBitmap(bitmap);
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
            bitmapPath = "/Family/"+child_type+"_image.jpg";
            path = externalPath + bitmapPath;

            FileOutputStream out = null;
            try {
                File file;
                file =new File(externalPath+"/Family",child_type+"_image.jpg");
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
                ivChildPhoto.setVisibility(View.VISIBLE);
                ivChildPhoto.setImageBitmap(bitmap);
                tvChildPhoto.setText(bitmapPath);
                Log.e("Galary","not null" );
            }
            else
                Log.e("Galery"," null" );
        }
    }
    public void nextPage(View view) {
        childName = etChildName.getText().toString();
        bgChild = etBgChild.getText().toString();
        childMobNo = etChildMobNo.getText().toString();
        wifeName = etWifeName.getText().toString();
        bgWife = etBgWife.getText().toString();
        gsonDtr1 = etGsonDtr1.getText().toString();
        bgGsonDtr1 = etBgGsonDtr1.getText().toString();
        gsonDtr2 = etGsonDtr2.getText().toString();

        bgGsonDtr2 = etBgGsonDtr2.getText().toString();
        gsonDtr3 = etGsonDtr3.getText().toString();
        bgGsonDtr3 = etBgGsonDtr3.getText().toString();
        gsonDtr4 = etGsonDtr4.getText().toString();
        bgGsonDtr4 = etBgGsonDtr4.getText().toString();

        homeMobNo = etHomeMobNo.getText().toString();
        
        loading = ProgressDialog.show(child1Activity.this, "Processing...","Please Wait...",true,true);

        Log.e("bitmapPath",bitmapPath );
        bitmap = BitmapFactory.decodeFile(bitmapPath);
        if(bitmap!=null){
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 25, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream .toByteArray();

            child_image = Base64.encodeToString(byteArray, Base64.DEFAULT);
            Log.e("child_image",child_image );
            type = "insertChild";
            BackgroundWorkerJson backgroundWorker = new BackgroundWorkerJson();
            backgroundWorker.execute();
        }else {
            loading.dismiss();
            Toast.makeText(getApplicationContext(),"bitmap null", Toast.LENGTH_LONG).show();
        }
    }
    public class BackgroundWorkerJson extends AsyncTask<String,Void,String> {


        @Override
        protected String doInBackground(String... params) {

            try {
                String post_data = null;
                String websiteUrl = null;
                if(type.equals("insertChild")){
                    websiteUrl = "http://southern-electric.co.in/FamilyApp/insertChild.php";
                    Log.e(type,type );

                    post_data = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(headName, "UTF-8")
                            +"&"+ URLEncoder.encode("child_type", "UTF-8") + "=" + URLEncoder.encode(child_type, "UTF-8")
                            +"&"+ URLEncoder.encode("child_name", "UTF-8") + "=" + URLEncoder.encode(childName, "UTF-8")
                            +"&"+ URLEncoder.encode("child_blood_grp", "UTF-8") + "=" + URLEncoder.encode(bgChild, "UTF-8")
                            +"&"+ URLEncoder.encode("child_phone_no", "UTF-8") + "=" + URLEncoder.encode(childMobNo, "UTF-8")
                            +"&"+ URLEncoder.encode("child_hus_or_wife_name", "UTF-8") + "=" + URLEncoder.encode(wifeName, "UTF-8")
                            +"&"+ URLEncoder.encode("child_hus_or_wife_blood_grp", "UTF-8") + "=" + URLEncoder.encode(bgWife, "UTF-8")
                            +"&"+ URLEncoder.encode("grand_son_or_daug_name1", "UTF-8") + "=" + URLEncoder.encode(gsonDtr1, "UTF-8")
                            +"&"+ URLEncoder.encode("grand_son_or_daug_blood_grp1", "UTF-8") + "=" + URLEncoder.encode(bgGsonDtr1, "UTF-8")
                            +"&"+ URLEncoder.encode("grand_son_or_daug_name2", "UTF-8") + "=" + URLEncoder.encode(gsonDtr2, "UTF-8")
                            +"&"+ URLEncoder.encode("grand_son_or_daug_blood_grp2", "UTF-8") + "=" + URLEncoder.encode(bgGsonDtr2, "UTF-8")
                            +"&"+ URLEncoder.encode("grand_son_or_daug_name3", "UTF-8") + "=" + URLEncoder.encode(gsonDtr3, "UTF-8")
                            +"&"+ URLEncoder.encode("grand_son_or_daug_blood_grp3", "UTF-8") + "=" + URLEncoder.encode(bgGsonDtr3, "UTF-8")
                            +"&"+ URLEncoder.encode("grand_son_or_daug_name4", "UTF-8") + "=" + URLEncoder.encode(gsonDtr4, "UTF-8")
                            +"&"+ URLEncoder.encode("grand_son_or_daug_blood_grp4", "UTF-8") + "=" + URLEncoder.encode(bgGsonDtr4, "UTF-8")
                            +"&"+ URLEncoder.encode("home_phone_no", "UTF-8") + "=" + URLEncoder.encode(homeMobNo, "UTF-8")
                            +"&"+ URLEncoder.encode("child_image", "UTF-8") + "=" + URLEncoder.encode(child_image, "UTF-8")
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

            if(type.equals("insertChild")){
                if(result !=null)
                    Log.e(type,result );
                else
                    Log.e(type,"null");
                loading.dismiss();
                Toast.makeText(getApplicationContext(),result, Toast.LENGTH_LONG ).show();
                if(!(noOfChildrenInt == 1)){
                    noOfChildrenInt --;
                    countChild++;

                    Intent intent = new Intent(child1Activity.this,child1Activity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    Bundle extras = new Bundle();
                    extras.putInt("noOfChildrenInt", noOfChildrenInt);
                    extras.putInt("countChild", countChild);
                    extras.putString("headName",headName );
                    intent.putExtras(extras);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(child1Activity.this,SampleActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        }
    }
}
