package com.yarolegovich.slidingrootnav.sample;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {
    String type;
    //ListView listView;
    ProgressDialog loading;
    //FamilyDetailsAdapter familyDetailsAdapter;

    TextView tvHeadName,tvOccupation,tvNativePlace,tvHeadEmail,tvHeadMobile,
            tvBgHead,tvWifeName,tvBgWife,tvFatherName,tvGhotre,tvOfficeAddress,tvOfficePhNo,tvResidenceAddress,tvResidencePhNo;
    ImageView ivHeadPhoto;

    TextView tvChildName1,tvWifeName1,tvMobNo1,tvGrand1Child1,tvGrand2Child1,tvGrand3Child1,tvGrand4Child1,tvChildBg1;
    TextView tvChildName2,tvWifeName2,tvMobNo2,tvGrand1Child2,tvGrand2Child2,tvGrand3Child2,tvGrand4Child2,tvChildBg2;
    TextView tvChildName3,tvWifeName3,tvMobNo3,tvGrand1Child3,tvGrand2Child3,tvGrand3Child3,tvGrand4Child3,tvChildBg3;
    ImageView ivChildPhoto1,ivChildPhoto2,ivChildPhoto3;
    LinearLayout llChild1,llChild2,llChild3,llFamilyData;

    String userName;
    SessionManager session;
    Dialog myDialog;

    int noOfChildren = 0;
    String headPhotoString,headName,headMobile,occupation,nativePlace,headEmail;
    String bgHead,wifeName,bgWife,fatherName,ghotre,officeAddress,officePhNo,residenceAddress,residencePhNo;

    TextView tvHeadPhotoPopup,tvChildPhoto1Popup,tvChildPhoto2Popup,tvChildPhoto3Popup;
    ImageView ivHeadPhotoPopup=null,ivChildPhoto1Popup = null,ivChildPhoto2Popup=null,ivChildPhoto3Popup=null;
    Bitmap headPhoto,childPhoto1,childPhoto2,childPhoto3;
    String child_name1,child_name2,child_name3,child_phone_no1,child_phone_no2,child_phone_no3;

    private int PICK_IMAGE = 100,CAPTURE_IMAGE = 200;
    private int PICK_IMAGE_C1 = 101,CAPTURE_IMAGE_C1 = 201;
    private int PICK_IMAGE_C2 = 102,CAPTURE_IMAGE_C2 = 202;
    private int PICK_IMAGE_C3 = 103,CAPTURE_IMAGE_C3 = 203;

    String bitmapPathHead,bitmapPathC1,bitmapPathC2,bitmapPathC3;
    String stringHeadPhoto,stringChildPhoto1,stringChildPhoto2,stringChildPhoto3;

    TextView tvIdHead,tvIdChild1,tvIdChild2,tvIdChild3;
    String IdHead,IdChild1,IdChild2,IdChild3;
    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        loading.dismiss();
        myDialog.dismiss();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Session class instance
        session = new SessionManager(getApplicationContext());
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        // name
        userName = user.get(SessionManager.KEY_NAME);
        /*
        listView = (ListView) findViewById(R.id.lvFamilyDetails);
        listView.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        setListViewHeightBasedOnChildren(listView);
        */
        ivHeadPhoto = (ImageView) findViewById(R.id.ivHeadPhoto);
        tvHeadName = (TextView) findViewById(R.id.tvHeadName);
        tvOccupation = (TextView) findViewById(R.id.tvOccupation);
        tvNativePlace = (TextView) findViewById(R.id.tvNativePlace);
        tvHeadEmail = (TextView) findViewById(R.id.tvHeadEmail);
        tvHeadMobile = (TextView) findViewById(R.id.tvHeadMobile);

        tvBgHead= (TextView) findViewById(R.id.tvBgHead);
        tvWifeName= (TextView) findViewById(R.id.tvWifeName);
        tvBgWife= (TextView) findViewById(R.id.tvBgWife);
        tvFatherName= (TextView) findViewById(R.id.tvFatherName);
        tvGhotre= (TextView) findViewById(R.id.tvGhotre);
        tvOfficeAddress= (TextView) findViewById(R.id.tvOfficeAddress);
        tvOfficePhNo= (TextView) findViewById(R.id.tvOfficePhNo);
        tvResidenceAddress= (TextView) findViewById(R.id.tvResidenceAddress);
        tvResidencePhNo= (TextView) findViewById(R.id.tvResidencePhNo);

        llFamilyData = findViewById(R.id.llFamilyData);
        //child1
        ivChildPhoto1 = (ImageView) findViewById(R.id.ivChildPhoto1);
        tvChildName1 = (TextView) findViewById(R.id.tvChildName1);
        tvChildBg1 = findViewById(R.id.tvChildBg1);
        tvWifeName1 = (TextView) findViewById(R.id.tvWifeName1);
        tvMobNo1 = (TextView) findViewById(R.id.tvMobNo1);
        tvGrand1Child1 = (TextView) findViewById(R.id.tvGrand1Child1);
        tvGrand2Child1 = (TextView) findViewById(R.id.tvGrand2Child1);
        tvGrand3Child1 = (TextView) findViewById(R.id.tvGrand3Child1);
        tvGrand4Child1 = (TextView) findViewById(R.id.tvGrand4Child1);
        llChild1 = findViewById(R.id.llChild1);
        //child2
        ivChildPhoto2 = (ImageView) findViewById(R.id.ivChildPhoto2);
        tvChildName2 = (TextView) findViewById(R.id.tvChildName2);
        tvChildBg2 = findViewById(R.id.tvChildBg2);
        tvWifeName2 = (TextView) findViewById(R.id.tvWifeName2);
        tvMobNo2 = (TextView) findViewById(R.id.tvMobNo2);
        tvGrand1Child2 = (TextView) findViewById(R.id.tvGrand1Child2);
        tvGrand2Child2 = (TextView) findViewById(R.id.tvGrand2Child2);
        tvGrand3Child2 = (TextView) findViewById(R.id.tvGrand3Child2);
        tvGrand4Child2 = (TextView) findViewById(R.id.tvGrand4Child2);
        llChild2 = findViewById(R.id.llChild2);

        //child3
        ivChildPhoto3 = (ImageView) findViewById(R.id.ivChildPhoto3);
        tvChildName3 = (TextView) findViewById(R.id.tvChildName3);
        tvChildBg3 = findViewById(R.id.tvChildBg3);
        tvWifeName3 = (TextView) findViewById(R.id.tvWifeName3);
        tvMobNo3 = (TextView) findViewById(R.id.tvMobNo3);
        tvGrand1Child3 = (TextView) findViewById(R.id.tvGrand1Child3);
        tvGrand2Child3 = (TextView) findViewById(R.id.tvGrand2Child3);
        tvGrand3Child3 = (TextView) findViewById(R.id.tvGrand3Child3);
        tvGrand4Child3 = (TextView) findViewById(R.id.tvGrand4Child3);
        llChild3 = findViewById(R.id.llChild3);

        tvIdHead= (TextView) findViewById(R.id.tvIdHead);
        tvIdChild1= (TextView) findViewById(R.id.tvIdChild1);
        tvIdChild2= (TextView) findViewById(R.id.tvIdChild2);
        tvIdChild3= (TextView) findViewById(R.id.tvIdChild3);

        //familyDetailsAdapter = new FamilyDetailsAdapter(this, R.layout.family_details_row_layout);
        loading = ProgressDialog.show(ProfileActivity.this, "Fetching Data...","Please Wait...",true,true);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                type = "getProfile";
                BackgroundWorkerJson backgroundWorker = new BackgroundWorkerJson();
                backgroundWorker.execute();
            }
        });
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();

        myDialog = new Dialog(this);
    }
    @Override
    public void onBackPressed() {
        finish();
    }

    public void goBack(View view) {
        finish();
    }

    public void ShowPopup(View view) {
        TextView txtclose;
        int rID = R.layout.edit_profile_popup;
        myDialog.setContentView(rID);
        txtclose =(TextView) myDialog.findViewById(R.id.txtclose);
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final EditText etHeadName,etPhNo, etEmail,etOfficeAddress,etOfficePhNo,etResidenceAddress,etResidencePhNo,
                etChildName1Popup,etChildName2Popup,etChildName3Popup,
                etMobNo1Popup,etMobNo2Popup,etMobNo3Popup
                ;

        Button btnEditProfile;

        etHeadName = myDialog.findViewById(R.id.etHeadName);
        etPhNo =(EditText) myDialog.findViewById(R.id.etPhNo);
        etEmail =(EditText) myDialog.findViewById(R.id.etEmail);

        etOfficeAddress =(EditText) myDialog.findViewById(R.id.etOfficeAddress);
        etOfficePhNo =(EditText) myDialog.findViewById(R.id.etOfficePhNo);
        etResidenceAddress =(EditText) myDialog.findViewById(R.id.etResidenceAddress);
        etResidencePhNo =(EditText) myDialog.findViewById(R.id.etResidencePhNo);

        etChildName1Popup =(EditText) myDialog.findViewById(R.id.etChildName1Popup);
        etChildName2Popup=(EditText) myDialog.findViewById(R.id.etChildName2Popup);
        etChildName3Popup=(EditText) myDialog.findViewById(R.id.etChildName3Popup);

        etMobNo1Popup=(EditText) myDialog.findViewById(R.id.etMobNo1Popup);
        etMobNo2Popup=(EditText) myDialog.findViewById(R.id.etMobNo2Popup);
        etMobNo3Popup=(EditText) myDialog.findViewById(R.id.etMobNo3Popup);

        tvHeadPhotoPopup = myDialog.findViewById(R.id.tvHeadPhoto);
        tvChildPhoto1Popup = myDialog.findViewById(R.id.tvChildPhoto1Popup);
        tvChildPhoto2Popup = myDialog.findViewById(R.id.tvChildPhoto2Popup);
        tvChildPhoto3Popup = myDialog.findViewById(R.id.tvChildPhoto3Popup);

        tvHeadPhotoPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder notifyLocationServices = new AlertDialog.Builder(ProfileActivity.this);
                notifyLocationServices.setTitle("Choose the option");
                notifyLocationServices.setMessage("Select");
                notifyLocationServices.setPositiveButton("Galary", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION| Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                        }
                        startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE);
                    }
                });
                notifyLocationServices.setNegativeButton("Camera", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Family";
                        File file = new File(path);
                        if(!file.exists()){
                            file.mkdirs();
                        }
                        File imgae_file = new File(file,"popup_head_image.jpg");
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
                });
                notifyLocationServices.show();
            }
        });
        tvChildPhoto1Popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder notifyLocationServices = new AlertDialog.Builder(ProfileActivity.this);
                notifyLocationServices.setTitle("Choose the option");
                notifyLocationServices.setMessage("Select");
                notifyLocationServices.setPositiveButton("Galary", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION| Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                        }
                        startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE_C1);
                    }
                });
                notifyLocationServices.setNegativeButton("Camera", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Family";
                        File file = new File(path);
                        if(!file.exists()){
                            file.mkdirs();
                        }
                        File imgae_file = new File(file,"popup_child_image1.jpg");
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION| Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                            Uri contentUri = FileProvider.getUriForFile(getApplicationContext(), "com.project.naveen.family", imgae_file);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
                        }else {
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imgae_file));
                        }
                        startActivityForResult(intent,CAPTURE_IMAGE_C1);
                    }
                });
                notifyLocationServices.show();
            }
        });
        tvChildPhoto2Popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder notifyLocationServices = new AlertDialog.Builder(ProfileActivity.this);
                notifyLocationServices.setTitle("Choose the option");
                notifyLocationServices.setMessage("Select");
                notifyLocationServices.setPositiveButton("Galary", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION| Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                        }
                        startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE_C2);
                    }
                });
                notifyLocationServices.setNegativeButton("Camera", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Family";
                        File file = new File(path);
                        if(!file.exists()){
                            file.mkdirs();
                        }
                        File imgae_file = new File(file,"popup_child_image2.jpg");
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION| Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                            Uri contentUri = FileProvider.getUriForFile(getApplicationContext(), "com.project.naveen.family", imgae_file);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
                        }else {
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imgae_file));
                        }
                        startActivityForResult(intent,CAPTURE_IMAGE_C2);
                    }
                });
                notifyLocationServices.show();
            }
        });
        tvChildPhoto3Popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder notifyLocationServices = new AlertDialog.Builder(ProfileActivity.this);
                notifyLocationServices.setTitle("Choose the option");
                notifyLocationServices.setMessage("Select");
                notifyLocationServices.setPositiveButton("Galary", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION| Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                        }
                        startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE_C3);
                    }
                });
                notifyLocationServices.setNegativeButton("Camera", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Family";
                        File file = new File(path);
                        if(!file.exists()){
                            file.mkdirs();
                        }
                        File imgae_file = new File(file,"popup_child_image3.jpg");
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION| Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                            Uri contentUri = FileProvider.getUriForFile(getApplicationContext(), "com.project.naveen.family", imgae_file);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
                        }else {
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imgae_file));
                        }
                        startActivityForResult(intent,CAPTURE_IMAGE_C3);
                    }
                });
                notifyLocationServices.show();
            }
        });

        ivHeadPhotoPopup = myDialog.findViewById(R.id.ivHeadPhoto);
        ivChildPhoto1Popup=myDialog.findViewById(R.id.ivChildPhoto1Popup);
        ivChildPhoto2Popup=myDialog.findViewById(R.id.ivChildPhoto2Popup);
        ivChildPhoto3Popup=myDialog.findViewById(R.id.ivChildPhoto3Popup);

        btnEditProfile =(Button) myDialog.findViewById(R.id.btnEditProfile);

        ivHeadPhotoPopup.setImageBitmap(headPhoto);
        etHeadName.setText(headName);
        etEmail.setText( headEmail);
        etPhNo.setText( headMobile);

        etOfficeAddress.setText( officeAddress);
        etOfficePhNo.setText( officePhNo);
        etResidenceAddress.setText( residenceAddress);
        etResidencePhNo.setText( residencePhNo);

        etChildName1Popup.setText( child_name1);
        etChildName2Popup.setText( child_name2);
        etChildName3Popup.setText( child_name3);

        etMobNo1Popup.setText( child_phone_no1);
        etMobNo2Popup.setText( child_phone_no2);
        etMobNo3Popup.setText( child_phone_no3);

        ivChildPhoto1Popup.setImageBitmap(childPhoto1);
        ivChildPhoto2Popup.setImageBitmap(childPhoto2);
        ivChildPhoto3Popup.setImageBitmap(childPhoto3);

        LinearLayout llChild1Popup,llChild2Popup,llChild3Popup,llFamilyDataPopup;

        llChild1Popup = myDialog.findViewById(R.id.llChild1Popup);
        llChild2Popup = myDialog.findViewById(R.id.llChild2Popup);
        llChild3Popup = myDialog.findViewById(R.id.llChild3Popup);
        llFamilyDataPopup = myDialog.findViewById(R.id.llFamilyDataPopup);

        if(noOfChildren>0){

            if(noOfChildren == 1){
                llChild1Popup.setVisibility(View.VISIBLE);
                llChild2Popup.setVisibility(View.GONE);
                llChild3Popup.setVisibility(View.GONE);
            }
            if(noOfChildren == 2){
                llChild1Popup.setVisibility(View.VISIBLE);
                llChild2Popup.setVisibility(View.VISIBLE);
                llChild3Popup.setVisibility(View.GONE);
            }
            if(noOfChildren == 3){
                llChild1Popup.setVisibility(View.VISIBLE);
                llChild2Popup.setVisibility(View.VISIBLE);
                llChild3Popup.setVisibility(View.VISIBLE);
            }
        }else {
            llFamilyDataPopup.setVisibility(View.GONE);
        }
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                headName = etHeadName.getText().toString();
                headMobile = etPhNo.getText().toString();
                headEmail = etEmail.getText().toString();

                officeAddress = etOfficeAddress.getText().toString();
                officePhNo = etOfficePhNo.getText().toString();
                residenceAddress = etResidenceAddress.getText().toString();
                residencePhNo = etResidencePhNo.getText().toString();

                child_name1 = etChildName1Popup.getText().toString();
                child_name2 = etChildName2Popup.getText().toString();
                child_name3 = etChildName3Popup.getText().toString();

                child_phone_no1 = etMobNo1Popup.getText().toString();
                child_phone_no2 = etMobNo2Popup.getText().toString();
                child_phone_no3 = etMobNo3Popup.getText().toString();

                loading = ProgressDialog.show(ProfileActivity.this, "Processing...","Please Wait...",true,true);

                if(noOfChildren>0){

                    if(noOfChildren == 1){
                        type = "edit_profile1";
                        BackgroundWorkerJson backgroundWorker = new BackgroundWorkerJson();
                        backgroundWorker.execute();
                    }
                    if(noOfChildren == 2){
                        type = "edit_profile2";
                        BackgroundWorkerJson backgroundWorker = new BackgroundWorkerJson();
                        backgroundWorker.execute();
                    }
                    if(noOfChildren == 3){
                        type = "edit_profile3";
                        BackgroundWorkerJson backgroundWorker = new BackgroundWorkerJson();
                        backgroundWorker.execute();
                    }
                }else {
                    type = "edit_profile0";
                    BackgroundWorkerJson backgroundWorker = new BackgroundWorkerJson();
                    backgroundWorker.execute();
                }
            }
        });

        myDialog.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == CAPTURE_IMAGE){
            String path,externalPath;
            externalPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            bitmapPathHead = "/Family/popup_head_image.jpg";
            path = externalPath + bitmapPathHead;

            headPhoto = BitmapFactory.decodeFile(path);
            FileOutputStream out = null;
            try {
                File file;
                file =new File(externalPath+"/Family","popup_head_image.jpg");
                out = new FileOutputStream(file);
                headPhoto.compress(Bitmap.CompressFormat.JPEG, 25, out); // bmp is your Bitmap instance

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
            bitmapPathHead = path;
            headPhoto = BitmapFactory.decodeFile(bitmapPathHead);

            Log.e("Bitmap",path );
            if(headPhoto!=null){
                ivHeadPhotoPopup.setImageBitmap(headPhoto);
                tvHeadPhotoPopup.setText(bitmapPathHead);
                Log.e("Camera","not null" );
            }
            else
                Log.e("Camera"," null" );
            //imageView.setImageBitmap(headPhoto);

            headPhoto = BitmapFactory.decodeFile(bitmapPathHead);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            headPhoto.compress(Bitmap.CompressFormat.JPEG, 25, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream .toByteArray();

            stringHeadPhoto = Base64.encodeToString(byteArray, Base64.DEFAULT);
            Log.e("stringHeadPhoto",stringHeadPhoto);
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

            bitmapPathHead = filePath;
            headPhoto = BitmapFactory.decodeFile(filePath);

            String path,externalPath;
            externalPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            bitmapPathHead = "/Family/popup_head_image.jpg";
            path = externalPath + bitmapPathHead;

            FileOutputStream out = null;
            try {
                File file;
                file =new File(externalPath+"/Family","popup_head_image.jpg");
                out = new FileOutputStream(file);
                headPhoto.compress(Bitmap.CompressFormat.JPEG, 25, out); // bmp is your Bitmap instance

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
            bitmapPathHead = path;
            headPhoto = BitmapFactory.decodeFile(bitmapPathHead);
            Log.e("path",path );

            Log.e("filePath",filePath );
            if(headPhoto!=null){
                ivHeadPhotoPopup.setImageBitmap(headPhoto);
                tvHeadPhotoPopup.setText(bitmapPathHead);
                Log.e("Galary","not null" );
            }
            else
                Log.e("Galery"," null" );

            headPhoto = BitmapFactory.decodeFile(bitmapPathHead);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            headPhoto.compress(Bitmap.CompressFormat.JPEG, 25, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream .toByteArray();

            stringHeadPhoto = Base64.encodeToString(byteArray, Base64.DEFAULT);
            Log.e("stringHeadPhoto",stringHeadPhoto);
        }


        if(resultCode == RESULT_OK && requestCode == CAPTURE_IMAGE_C1){
            String path,externalPath;
            externalPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            bitmapPathC1 = "/Family/popup_child_image1.jpg";
            path = externalPath + bitmapPathC1;

            childPhoto1 = BitmapFactory.decodeFile(path);
            FileOutputStream out = null;
            try {
                File file;
                file =new File(externalPath+"/Family","popup_child_image1.jpg");
                out = new FileOutputStream(file);
                childPhoto1.compress(Bitmap.CompressFormat.JPEG, 25, out); // bmp is your Bitmap instance

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
            bitmapPathC1 = path;
            childPhoto1 = BitmapFactory.decodeFile(bitmapPathC1);
            Log.e("Bitmap",path );
            if(childPhoto1!=null){
                ivChildPhoto1Popup.setImageBitmap(childPhoto1);
                tvChildPhoto1Popup.setText(bitmapPathC1);
                Log.e("Camera","not null" );
            }
            else
                Log.e("Camera"," null" );
            //imageView.setImageBitmap(childPhoto1);

            childPhoto1 = BitmapFactory.decodeFile(bitmapPathC1);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            childPhoto1.compress(Bitmap.CompressFormat.JPEG, 25, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream .toByteArray();

            stringChildPhoto1= Base64.encodeToString(byteArray, Base64.DEFAULT);
            Log.e("stringChildPhoto1",stringChildPhoto1);
        }
        if(resultCode == RESULT_OK && requestCode == PICK_IMAGE_C1){
            //found mistake=> if(requestCode == RESULT_OK && requestCode == PICK_IMAGE)

            Uri imageUri = data.getData();
            // try1=>fail - Because of mistake
            String[] projection={MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(imageUri,projection,null,null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(projection[0]);
            String filePath = cursor.getString(columnIndex);
            cursor.close();

            bitmapPathC1 = filePath;
            childPhoto1 = BitmapFactory.decodeFile(filePath);

            String path,externalPath;
            externalPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            bitmapPathC1 = "/Family/popup_child_image1.jpg";
            path = externalPath + bitmapPathC1;

            FileOutputStream out = null;
            try {
                File file;
                file =new File(externalPath+"/Family","popup_child_image1.jpg");
                out = new FileOutputStream(file);
                childPhoto1.compress(Bitmap.CompressFormat.JPEG, 25, out); // bmp is your Bitmap instance

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
            bitmapPathC1 = path;
            childPhoto1 = BitmapFactory.decodeFile(bitmapPathC1);
            Log.e("path",path );

            Log.e("filePath",filePath );
            if(childPhoto1!=null){
                ivChildPhoto1Popup.setImageBitmap(childPhoto1);
                tvChildPhoto1Popup.setText(bitmapPathC1);
                Log.e("Galary","not null" );
            }
            else
                Log.e("Galery"," null" );

            childPhoto1 = BitmapFactory.decodeFile(bitmapPathC1);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            childPhoto1.compress(Bitmap.CompressFormat.JPEG, 25, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream .toByteArray();

            stringChildPhoto1= Base64.encodeToString(byteArray, Base64.DEFAULT);
            Log.e("stringChildPhoto1",stringChildPhoto1);
        }
        //child2
        if(resultCode == RESULT_OK && requestCode == CAPTURE_IMAGE_C2){
            String path,externalPath;
            externalPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            bitmapPathC2 = "/Family/popup_child_image2.jpg";
            path = externalPath + bitmapPathC2;

            childPhoto2 = BitmapFactory.decodeFile(path);
            FileOutputStream out = null;
            try {
                File file;
                file =new File(externalPath+"/Family","popup_child_image2.jpg");
                out = new FileOutputStream(file);
                childPhoto2.compress(Bitmap.CompressFormat.JPEG, 25, out); // bmp is your Bitmap instance

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
            bitmapPathC2 = path;
            childPhoto2 = BitmapFactory.decodeFile(bitmapPathC2);
            Log.e("Bitmap",path );
            if(childPhoto2!=null){
                ivChildPhoto2Popup.setImageBitmap(childPhoto2);
                tvChildPhoto2Popup.setText(bitmapPathC2);
                Log.e("Camera","not null" );
            }
            else
                Log.e("Camera"," null" );
            //imageView.setImageBitmap(childPhoto1);

            childPhoto2 = BitmapFactory.decodeFile(bitmapPathC2);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            childPhoto2.compress(Bitmap.CompressFormat.JPEG, 25, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream .toByteArray();

            stringChildPhoto2= Base64.encodeToString(byteArray, Base64.DEFAULT);
            Log.e("stringChildPhoto2",stringChildPhoto2);
        }
        if(resultCode == RESULT_OK && requestCode == PICK_IMAGE_C2){
            //found mistake=> if(requestCode == RESULT_OK && requestCode == PICK_IMAGE)
            Uri imageUri = data.getData();
            // try1=>fail - Because of mistake
            String[] projection={MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(imageUri,projection,null,null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(projection[0]);
            String filePath = cursor.getString(columnIndex);
            cursor.close();

            bitmapPathC2 = filePath;
            childPhoto2 = BitmapFactory.decodeFile(filePath);

            String path,externalPath;
            externalPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            bitmapPathC2 = "/Family/popup_child_image2.jpg";
            path = externalPath + bitmapPathC2;

            FileOutputStream out = null;
            try {
                File file;
                file =new File(externalPath+"/Family","popup_child_image2.jpg");
                out = new FileOutputStream(file);
                childPhoto2.compress(Bitmap.CompressFormat.JPEG, 25, out); // bmp is your Bitmap instance

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
            bitmapPathC2 = path;
            childPhoto2 = BitmapFactory.decodeFile(bitmapPathC2);
            Log.e("path",path );

            Log.e("filePath",filePath );
            if(childPhoto2!=null){
                ivChildPhoto2Popup.setImageBitmap(childPhoto2);
                tvChildPhoto2Popup.setText(bitmapPathC2);
                Log.e("Galary","not null" );
            }
            else
                Log.e("Galery"," null" );

            childPhoto2 = BitmapFactory.decodeFile(bitmapPathC2);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            childPhoto2.compress(Bitmap.CompressFormat.JPEG, 25, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream .toByteArray();

            stringChildPhoto2= Base64.encodeToString(byteArray, Base64.DEFAULT);
            Log.e("stringChildPhoto2",stringChildPhoto2);
        }
        //child3

        if(resultCode == RESULT_OK && requestCode == CAPTURE_IMAGE_C3){
            String path,externalPath;
            externalPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            bitmapPathC3 = "/Family/popup_child_image3.jpg";
            path = externalPath + bitmapPathC3;

            childPhoto3 = BitmapFactory.decodeFile(path);
            FileOutputStream out = null;
            try {
                File file;
                file =new File(externalPath+"/Family","popup_child_image3.jpg");
                out = new FileOutputStream(file);
                childPhoto3.compress(Bitmap.CompressFormat.JPEG, 25, out); // bmp is your Bitmap instance

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
            bitmapPathC3 = path;
            childPhoto3 = BitmapFactory.decodeFile(bitmapPathC3);
            Log.e("Bitmap",path );
            if(childPhoto1!=null){
                ivChildPhoto3Popup.setImageBitmap(childPhoto3);
                tvChildPhoto3Popup.setText(bitmapPathC3);
                Log.e("Camera","not null" );
            }
            else
                Log.e("Camera"," null" );
            //imageView.setImageBitmap(childPhoto1);

            childPhoto3 = BitmapFactory.decodeFile(bitmapPathC3);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            childPhoto3.compress(Bitmap.CompressFormat.JPEG, 25, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream .toByteArray();

            stringChildPhoto3= Base64.encodeToString(byteArray, Base64.DEFAULT);
            Log.e("stringChildPhoto3",stringChildPhoto3);
        }
        if(resultCode == RESULT_OK && requestCode == PICK_IMAGE_C3){
            //found mistake=> if(requestCode == RESULT_OK && requestCode == PICK_IMAGE)
            Uri imageUri = data.getData();
            // try1=>fail - Because of mistake
            String[] projection={MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(imageUri,projection,null,null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(projection[0]);
            String filePath = cursor.getString(columnIndex);
            cursor.close();

            bitmapPathC3 = filePath;
            childPhoto3 = BitmapFactory.decodeFile(filePath);

            String path,externalPath;
            externalPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            bitmapPathC3 = "/Family/popup_child_image3.jpg";
            path = externalPath + bitmapPathC3;

            FileOutputStream out = null;
            try {
                File file;
                file =new File(externalPath+"/Family","popup_child_image3.jpg");
                out = new FileOutputStream(file);
                childPhoto3.compress(Bitmap.CompressFormat.JPEG, 25, out); // bmp is your Bitmap instance

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
            bitmapPathC3 = path;
            childPhoto3 = BitmapFactory.decodeFile(bitmapPathC3);
            Log.e("path",path );

            Log.e("filePath",filePath );
            if(childPhoto3!=null){
                ivChildPhoto3Popup.setImageBitmap(childPhoto3);
                tvChildPhoto3Popup.setText(bitmapPathC3);
                Log.e("Galary","not null" );
            }
            else
                Log.e("Galery"," null" );
            childPhoto3 = BitmapFactory.decodeFile(bitmapPathC3);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            childPhoto3.compress(Bitmap.CompressFormat.JPEG, 25, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream .toByteArray();

            stringChildPhoto3= Base64.encodeToString(byteArray, Base64.DEFAULT);
            Log.e("stringChildPhoto3",stringChildPhoto3);
        }

    }

    /**** Method for Setting the Height of the ListView dynamically.
     **** Hack to fix the issue of not showing all the items of the ListView
     **** when placed inside a ScrollView  ****/
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, LinearLayout.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
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
                if(type.equals("getProfile")){
                    websiteUrl = "http://southern-electric.co.in/FamilyApp/getProfile.php";
                    Log.e(type,type );

                    post_data = URLEncoder.encode("user_name", "UTF-8") + "=" + URLEncoder.encode(userName, "UTF-8")
                    ;
                }else if(type.equals("edit_profile0")){
                    websiteUrl = "http://southern-electric.co.in/FamilyApp/edit_profile.php";
                    Log.e(type,type );

                    post_data = URLEncoder.encode("IdHead", "UTF-8") + "=" + URLEncoder.encode(IdHead, "UTF-8")

                            +"&"+ URLEncoder.encode("bitmapPath", "UTF-8") + "=" + URLEncoder.encode(stringHeadPhoto, "UTF-8")
                            +"&"+ URLEncoder.encode("headName", "UTF-8") + "=" + URLEncoder.encode(headName, "UTF-8")
                            +"&"+ URLEncoder.encode("headEmail", "UTF-8") + "=" + URLEncoder.encode(headEmail, "UTF-8")
                            +"&"+ URLEncoder.encode("headMobile", "UTF-8") + "=" + URLEncoder.encode(headMobile, "UTF-8")

                            +"&"+ URLEncoder.encode("officeAddress", "UTF-8") + "=" + URLEncoder.encode(officeAddress, "UTF-8")
                            +"&"+ URLEncoder.encode("officePhNo", "UTF-8") + "=" + URLEncoder.encode(officePhNo, "UTF-8")
                            +"&"+ URLEncoder.encode("residenceAddress", "UTF-8") + "=" + URLEncoder.encode(residenceAddress, "UTF-8")
                            +"&"+ URLEncoder.encode("residencePhNo", "UTF-8") + "=" + URLEncoder.encode(residencePhNo, "UTF-8")
                    ;
                }else if(type.equals("edit_profile1")){
                    websiteUrl = "http://southern-electric.co.in/FamilyApp/edit_profile.php";
                    Log.e(type,type );

                    post_data = URLEncoder.encode("IdHead", "UTF-8") + "=" + URLEncoder.encode(IdHead, "UTF-8")
                            +"&"+ URLEncoder.encode("IdChild1", "UTF-8") + "=" + URLEncoder.encode(IdChild1, "UTF-8")

                            +"&"+ URLEncoder.encode("bitmapPath", "UTF-8") + "=" + URLEncoder.encode(stringHeadPhoto, "UTF-8")
                            +"&"+ URLEncoder.encode("headName", "UTF-8") + "=" + URLEncoder.encode(headName, "UTF-8")
                            +"&"+ URLEncoder.encode("headEmail", "UTF-8") + "=" + URLEncoder.encode(headEmail, "UTF-8")
                            +"&"+ URLEncoder.encode("headMobile", "UTF-8") + "=" + URLEncoder.encode(headMobile, "UTF-8")

                            +"&"+ URLEncoder.encode("officeAddress", "UTF-8") + "=" + URLEncoder.encode(officeAddress, "UTF-8")
                            +"&"+ URLEncoder.encode("officePhNo", "UTF-8") + "=" + URLEncoder.encode(officePhNo, "UTF-8")
                            +"&"+ URLEncoder.encode("residenceAddress", "UTF-8") + "=" + URLEncoder.encode(residenceAddress, "UTF-8")
                            +"&"+ URLEncoder.encode("residencePhNo", "UTF-8") + "=" + URLEncoder.encode(residencePhNo, "UTF-8")

                            +"&"+ URLEncoder.encode("child_name1", "UTF-8") + "=" + URLEncoder.encode(child_name1, "UTF-8")

                            +"&"+ URLEncoder.encode("child_phone_no1", "UTF-8") + "=" + URLEncoder.encode(child_phone_no1, "UTF-8")

                            +"&"+ URLEncoder.encode("child_image1", "UTF-8") + "=" + URLEncoder.encode(stringChildPhoto1, "UTF-8")

                    ;
                }else if(type.equals("edit_profile2")){
                    websiteUrl = "http://southern-electric.co.in/FamilyApp/edit_profile.php";
                    Log.e(type,type );

                    post_data = URLEncoder.encode("IdHead", "UTF-8") + "=" + URLEncoder.encode(IdHead, "UTF-8")
                            +"&"+ URLEncoder.encode("IdChild1", "UTF-8") + "=" + URLEncoder.encode(IdChild1, "UTF-8")
                            +"&"+ URLEncoder.encode("IdChild2", "UTF-8") + "=" + URLEncoder.encode(IdChild2, "UTF-8")

                            +"&"+ URLEncoder.encode("bitmapPath", "UTF-8") + "=" + URLEncoder.encode(stringHeadPhoto, "UTF-8")
                            +"&"+ URLEncoder.encode("headName", "UTF-8") + "=" + URLEncoder.encode(headName, "UTF-8")
                            +"&"+ URLEncoder.encode("headEmail", "UTF-8") + "=" + URLEncoder.encode(headEmail, "UTF-8")
                            +"&"+ URLEncoder.encode("headMobile", "UTF-8") + "=" + URLEncoder.encode(headMobile, "UTF-8")

                            +"&"+ URLEncoder.encode("officeAddress", "UTF-8") + "=" + URLEncoder.encode(officeAddress, "UTF-8")
                            +"&"+ URLEncoder.encode("officePhNo", "UTF-8") + "=" + URLEncoder.encode(officePhNo, "UTF-8")
                            +"&"+ URLEncoder.encode("residenceAddress", "UTF-8") + "=" + URLEncoder.encode(residenceAddress, "UTF-8")
                            +"&"+ URLEncoder.encode("residencePhNo", "UTF-8") + "=" + URLEncoder.encode(residencePhNo, "UTF-8")

                            +"&"+ URLEncoder.encode("child_name1", "UTF-8") + "=" + URLEncoder.encode(child_name1, "UTF-8")
                            +"&"+ URLEncoder.encode("child_name2", "UTF-8") + "=" + URLEncoder.encode(child_name2, "UTF-8")

                            +"&"+ URLEncoder.encode("child_phone_no1", "UTF-8") + "=" + URLEncoder.encode(child_phone_no1, "UTF-8")
                            +"&"+ URLEncoder.encode("child_phone_no2", "UTF-8") + "=" + URLEncoder.encode(child_phone_no2, "UTF-8")

                            +"&"+ URLEncoder.encode("child_image1", "UTF-8") + "=" + URLEncoder.encode(stringChildPhoto1, "UTF-8")
                            +"&"+ URLEncoder.encode("child_image2", "UTF-8") + "=" + URLEncoder.encode(stringChildPhoto2, "UTF-8")

                    ;
                }else if(type.equals("edit_profile3")){
                    websiteUrl = "http://southern-electric.co.in/FamilyApp/edit_profile.php";
                    Log.e(type,type );

                    post_data = URLEncoder.encode("IdHead", "UTF-8") + "=" + URLEncoder.encode(IdHead, "UTF-8")
                            +"&"+ URLEncoder.encode("IdChild1", "UTF-8") + "=" + URLEncoder.encode(IdChild1, "UTF-8")
                            +"&"+ URLEncoder.encode("IdChild2", "UTF-8") + "=" + URLEncoder.encode(IdChild2, "UTF-8")
                            +"&"+ URLEncoder.encode("IdChild3", "UTF-8") + "=" + URLEncoder.encode(IdChild3, "UTF-8")

                            +"&"+ URLEncoder.encode("bitmapPath", "UTF-8") + "=" + URLEncoder.encode(stringHeadPhoto, "UTF-8")
                            +"&"+ URLEncoder.encode("headName", "UTF-8") + "=" + URLEncoder.encode(headName, "UTF-8")
                            +"&"+ URLEncoder.encode("headEmail", "UTF-8") + "=" + URLEncoder.encode(headEmail, "UTF-8")
                            +"&"+ URLEncoder.encode("headMobile", "UTF-8") + "=" + URLEncoder.encode(headMobile, "UTF-8")

                            +"&"+ URLEncoder.encode("officeAddress", "UTF-8") + "=" + URLEncoder.encode(officeAddress, "UTF-8")
                            +"&"+ URLEncoder.encode("officePhNo", "UTF-8") + "=" + URLEncoder.encode(officePhNo, "UTF-8")
                            +"&"+ URLEncoder.encode("residenceAddress", "UTF-8") + "=" + URLEncoder.encode(residenceAddress, "UTF-8")
                            +"&"+ URLEncoder.encode("residencePhNo", "UTF-8") + "=" + URLEncoder.encode(residencePhNo, "UTF-8")

                            +"&"+ URLEncoder.encode("child_name1", "UTF-8") + "=" + URLEncoder.encode(child_name1, "UTF-8")
                            +"&"+ URLEncoder.encode("child_name2", "UTF-8") + "=" + URLEncoder.encode(child_name2, "UTF-8")
                            +"&"+ URLEncoder.encode("child_name3", "UTF-8") + "=" + URLEncoder.encode(child_name3, "UTF-8")

                            +"&"+ URLEncoder.encode("child_phone_no1", "UTF-8") + "=" + URLEncoder.encode(child_phone_no1, "UTF-8")
                            +"&"+ URLEncoder.encode("child_phone_no2", "UTF-8") + "=" + URLEncoder.encode(child_phone_no2, "UTF-8")
                            +"&"+ URLEncoder.encode("child_phone_no3", "UTF-8") + "=" + URLEncoder.encode(child_phone_no3, "UTF-8")

                            +"&"+ URLEncoder.encode("child_image1", "UTF-8") + "=" + URLEncoder.encode(stringChildPhoto1, "UTF-8")
                            +"&"+ URLEncoder.encode("child_image2", "UTF-8") + "=" + URLEncoder.encode(stringChildPhoto2, "UTF-8")
                            +"&"+ URLEncoder.encode("child_image3", "UTF-8") + "=" + URLEncoder.encode(stringChildPhoto3, "UTF-8")
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

            if(type.equals("getProfile")){
                Log.e(type,result );
                json_string = result;

                if(json_string != null)
                {
                    try {
                        jsonObject = new JSONObject(json_string);
                        jsonArray = jsonObject.getJSONArray("getProfile");

                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jo = jsonArray.getJSONObject(i);

                            IdHead = jo.getString("id");
                            headPhotoString = jo.getString("headPhoto");
                            headName = jo.getString("headName");
                            headMobile = jo.getString("headMobile");
                            occupation = jo.getString("occupation");
                            nativePlace = jo.getString("nativePlace");
                            headEmail = jo.getString("headEmail");

                            byte[] decodedString = Base64.decode(headPhotoString, Base64.DEFAULT);
                            headPhoto = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                            ivHeadPhoto.setImageBitmap(headPhoto);
                            tvHeadName.setText(headName);
                            tvOccupation.setText(occupation);
                            tvNativePlace.setText(nativePlace);
                            tvHeadEmail.setText(headEmail);
                            tvHeadMobile.setText(headMobile);
                            tvIdHead.setText(IdHead);

                            bgHead = jo.getString("bgHead");
                            wifeName = jo.getString("wifeName");
                            bgWife = jo.getString("bgWife");
                            fatherName = jo.getString("fatherName");
                            ghotre = jo.getString("ghotre");
                            officeAddress = jo.getString("officeAddress");
                            officePhNo = jo.getString("officePhNo");
                            residenceAddress = jo.getString("residenceAddress");
                            residencePhNo = jo.getString("residencePhNo");
                            noOfChildren = jo.getInt("noOfChildren");

                            tvBgHead.setText(bgHead);
                            tvWifeName.setText(wifeName);
                            tvBgWife.setText(bgWife);
                            tvFatherName.setText(fatherName);
                            tvGhotre.setText(ghotre);
                            tvOfficeAddress.setText(officeAddress);
                            tvOfficePhNo.setText(officePhNo);
                            tvResidenceAddress.setText(residenceAddress);
                            tvResidencePhNo.setText(residencePhNo);

                        }

                        if(noOfChildren>0){
                            jsonArray = jsonObject.getJSONArray("getChildDetails");

                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject jo = jsonArray.getJSONObject(i);

                                String child_image,child_blood_grp,child_hus_or_wife_name,
                                        grand_son_or_daug_name1,grand_son_or_daug_name2,grand_son_or_daug_name3,grand_son_or_daug_name4;

                                child_hus_or_wife_name = jo.getString("child_hus_or_wife_name");
                                grand_son_or_daug_name1 = jo.getString("grand_son_or_daug_name1");
                                grand_son_or_daug_name2 = jo.getString("grand_son_or_daug_name2");
                                grand_son_or_daug_name3 = jo.getString("grand_son_or_daug_name3");
                                grand_son_or_daug_name4 = jo.getString("grand_son_or_daug_name4");
                                child_blood_grp = jo.getString("child_blood_grp");
                                child_image = jo.getString("child_image");
                                /*
                                FamilyDetailsData familyDetailsData = new FamilyDetailsData( childPhoto,child_name,child_phone_no,child_hus_or_wife_name,
                                        grand_son_or_daug_name1,grand_son_or_daug_name2,grand_son_or_daug_name3,grand_son_or_daug_name4);
                                familyDetailsAdapter.add(familyDetailsData);
                                */
                                if(i==0){
                                    child_name1 = jo.getString("child_name");
                                    child_phone_no1 = jo.getString("child_phone_no");

                                    byte[] decodedString = Base64.decode(child_image, Base64.DEFAULT);
                                    childPhoto1 = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                                    IdChild1 = jo.getString("IdChild");
                                    tvIdChild1.setText(IdChild1);
                                    llChild1.setVisibility(View.VISIBLE);
                                    ivChildPhoto1.setImageBitmap(childPhoto1);
                                    tvChildName1.setText(child_name1);
                                    tvChildBg1.setText(child_blood_grp);
                                    tvWifeName1.setText(child_hus_or_wife_name);
                                    tvMobNo1.setText(child_phone_no1);
                                    tvGrand1Child1.setText(grand_son_or_daug_name1);
                                    tvGrand2Child1.setText(grand_son_or_daug_name2);
                                    tvGrand3Child1.setText(grand_son_or_daug_name3);
                                    tvGrand4Child1.setText(grand_son_or_daug_name4);

                                    if(child_blood_grp != null)
                                    {
                                        tvChildBg1.setVisibility(View.VISIBLE);
                                        Log.e("child_blood_grp","not null" );
                                    }
                                    else
                                        Log.e("child_blood_grp","null" );

                                    if(grand_son_or_daug_name1 != null)
                                        tvGrand1Child1.setVisibility(View.VISIBLE);
                                    if(grand_son_or_daug_name2 != null)
                                        tvGrand2Child1.setVisibility(View.VISIBLE);
                                    if(grand_son_or_daug_name3 != null)
                                        tvGrand2Child1.setVisibility(View.VISIBLE);
                                    if(grand_son_or_daug_name4 != null)
                                        tvGrand2Child1.setVisibility(View.VISIBLE);
                                }
                                if(i==1){
                                    child_name2 = jo.getString("child_name");
                                    child_phone_no2 = jo.getString("child_phone_no");

                                    byte[] decodedString = Base64.decode(child_image, Base64.DEFAULT);
                                    childPhoto2 = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                                    IdChild2 = jo.getString("IdChild");
                                    tvIdChild2.setText(IdChild2);
                                    ivChildPhoto2.setImageBitmap(childPhoto2);
                                    tvChildName2.setText(child_name2);
                                    tvChildBg2.setText(child_blood_grp);
                                    tvWifeName2.setText(child_hus_or_wife_name);
                                    tvMobNo2.setText(child_phone_no2);
                                    tvGrand1Child2.setText(grand_son_or_daug_name1);
                                    tvGrand2Child2.setText(grand_son_or_daug_name2);
                                    tvGrand3Child2.setText(grand_son_or_daug_name3);
                                    tvGrand4Child2.setText(grand_son_or_daug_name4);

                                    if(grand_son_or_daug_name1 != null)
                                        tvGrand1Child2.setVisibility(View.VISIBLE);
                                    if(grand_son_or_daug_name2 != null)
                                        tvGrand2Child2.setVisibility(View.VISIBLE);
                                    if(grand_son_or_daug_name3 != null)
                                        tvGrand2Child2.setVisibility(View.VISIBLE);
                                    if(grand_son_or_daug_name4 != null)
                                        tvGrand2Child2.setVisibility(View.VISIBLE);
                                }
                                if(i==2){
                                    child_name3 = jo.getString("child_name");
                                    child_phone_no3 = jo.getString("child_phone_no");

                                    byte[] decodedString = Base64.decode(child_image, Base64.DEFAULT);
                                    childPhoto3 = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                                    IdChild3 = jo.getString("IdChild");
                                    tvIdChild3.setText(IdChild3);
                                    ivChildPhoto3.setImageBitmap(childPhoto3);
                                    tvChildName3.setText(child_name3);
                                    tvChildBg3.setText(child_blood_grp);
                                    tvWifeName3.setText(child_hus_or_wife_name);
                                    tvMobNo3.setText(child_phone_no3);
                                    tvGrand1Child3.setText(grand_son_or_daug_name1);
                                    tvGrand2Child3.setText(grand_son_or_daug_name2);
                                    tvGrand3Child3.setText(grand_son_or_daug_name3);
                                    tvGrand4Child3.setText(grand_son_or_daug_name4);

                                    if(grand_son_or_daug_name1 != null)
                                        tvGrand1Child3.setVisibility(View.VISIBLE);
                                    if(grand_son_or_daug_name2 != null)
                                        tvGrand2Child3.setVisibility(View.VISIBLE);
                                    if(grand_son_or_daug_name3 != null)
                                        tvGrand2Child3.setVisibility(View.VISIBLE);
                                    if(grand_son_or_daug_name4 != null)
                                        tvGrand2Child3.setVisibility(View.VISIBLE);
                                }

                            }

                            if(noOfChildren == 1){
                                llChild1.setVisibility(View.VISIBLE);
                                llChild2.setVisibility(View.GONE);
                                llChild3.setVisibility(View.GONE);
                            }
                            if(noOfChildren == 2){
                                llChild1.setVisibility(View.VISIBLE);
                                llChild2.setVisibility(View.VISIBLE);
                                llChild3.setVisibility(View.GONE);
                            }
                            if(noOfChildren == 3){
                                llChild1.setVisibility(View.VISIBLE);
                                llChild2.setVisibility(View.VISIBLE);
                                llChild3.setVisibility(View.VISIBLE);
                            }
                        }else {
                            llFamilyData.setVisibility(View.GONE);
                        }

                        loading.dismiss();
                        myDialog.dismiss();
                        //listView.setAdapter(familyDetailsAdapter);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else {
                    Log.e("JSON","null" );
                }
            }else if(type.equals("insertData")){
                Toast.makeText(getApplicationContext(),result , Toast.LENGTH_LONG ).show();
            }
            if(type.equals("edit_profile0")){
                Log.e(type,result );
                Toast.makeText(getApplicationContext(),result , Toast.LENGTH_LONG ).show();
                type = "getProfile";
                BackgroundWorkerJson backgroundWorker = new BackgroundWorkerJson();
                backgroundWorker.execute();
            }
            if(type.equals("edit_profile1")){
                Log.e(type,result );
                Toast.makeText(getApplicationContext(),result , Toast.LENGTH_LONG ).show();
                type = "getProfile";
                BackgroundWorkerJson backgroundWorker = new BackgroundWorkerJson();
                backgroundWorker.execute();
            }
            if(type.equals("edit_profile2")){
                Log.e(type,result );
                Toast.makeText(getApplicationContext(),result , Toast.LENGTH_LONG ).show();
                type = "getProfile";
                BackgroundWorkerJson backgroundWorker = new BackgroundWorkerJson();
                backgroundWorker.execute();
            }
            if(type.equals("edit_profile3")){
                Log.e(type,result );
                Toast.makeText(getApplicationContext(),result , Toast.LENGTH_LONG ).show();
                type = "getProfile";
                BackgroundWorkerJson backgroundWorker = new BackgroundWorkerJson();
                backgroundWorker.execute();
            }
        }
    }
}
