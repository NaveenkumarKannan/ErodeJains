package com.yarolegovich.slidingrootnav.sample;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PersonalDetailsActivity extends AppCompatActivity {
    EditText etHeadName,etBgHead,etWifeName,etBgWife,etFatherName,etGhotre,etHeadEmail,etHeadMobile,etNativePlace,etOccupation;
    TextView tvHeadPhoto;
    String headName,bgHead,wifeName,bgWife,fatherName,ghotre,headEmail,headMobile,nativePlace,occupation;
    String[] headArray=new String[11];
    ImageView ivHeadPhoto;
    //Runtime permission
    final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 100,MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 101, CAMERA_PERMISSION_REQ = 102;

    private int PICK_IMAGE = 100,CAPTURE_IMAGE = 101;

    String bitmapPath;
    Bitmap bitmap = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_details);

        etHeadName= findViewById(R.id.etHeadName);
        etBgHead= findViewById(R.id.etBgHead);
        etWifeName= findViewById(R.id.etWifeName);
        etBgWife= findViewById(R.id.etBgWife);
        etFatherName= findViewById(R.id.etFatherName);
        etGhotre= findViewById(R.id.etGhotre);
        etHeadEmail= findViewById(R.id.etHeadEmail);
        etHeadMobile= findViewById(R.id.etHeadMobile);
        etNativePlace= findViewById(R.id.etNativePlace);
        etOccupation= findViewById(R.id.etOccupation);
        tvHeadPhoto = findViewById(R.id.tvHeadPhoto);

        ivHeadPhoto = findViewById(R.id.ivHeadPhoto);

        tvHeadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder notifyLocationServices = new AlertDialog.Builder(PersonalDetailsActivity.this);
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
        
        askPermission();
    }
    @Override
    public void onBackPressed() {
        finish();
        /*
        Intent intent = new Intent(PersonalDetailsActivity.this,SampleActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        */
    }
    public void goBack(View view) {
        finish();
    }
    public void nextPage(View view) {
        headName = etHeadName.getText().toString();
        bgHead = etBgHead.getText().toString();
        wifeName = etWifeName.getText().toString();
        bgWife = etBgWife.getText().toString();
        fatherName = etFatherName.getText().toString();
        ghotre = etGhotre.getText().toString();
        headEmail = etHeadEmail.getText().toString();
        headMobile = etHeadMobile.getText().toString();
        nativePlace = etNativePlace.getText().toString();
        occupation = etOccupation.getText().toString();
        headArray[0] = headName;
        headArray[1] = bgHead;
        headArray[2] = wifeName;
        headArray[3] = bgWife;
        headArray[4] = fatherName;
        headArray[5] = ghotre;
        headArray[6] = headEmail;
        headArray[7] = headMobile;
        headArray[8] = nativePlace;
        headArray[9] = occupation;
        headArray[10] = bitmapPath;

        if(headName.isEmpty()){
            Toast.makeText(getApplicationContext(), "Fill the name",
                    Toast.LENGTH_SHORT).show();
        }
        else if (bgHead.isEmpty()){
            Toast.makeText(getApplicationContext(), "Fill the head blood group",
                    Toast.LENGTH_SHORT).show();
        }
        else if (wifeName.isEmpty()){
            Toast.makeText(getApplicationContext(), "Fill the wife name",
                    Toast.LENGTH_SHORT).show();
        }else if (bgWife.isEmpty()){
            Toast.makeText(getApplicationContext(), "Fill the wife blood group",
                    Toast.LENGTH_SHORT).show();
        }else if (fatherName.isEmpty()){
            Toast.makeText(getApplicationContext(), "Fill the father name",
                    Toast.LENGTH_SHORT).show();
        }else if (ghotre.isEmpty()){
            Toast.makeText(getApplicationContext(), "Fill the ghotre",
                    Toast.LENGTH_SHORT).show();
        }
        else if (headEmail.isEmpty()){
            Toast.makeText(getApplicationContext(), "Fill the email",
                    Toast.LENGTH_SHORT).show();
        }
        else if (headMobile.isEmpty()){
            Toast.makeText(getApplicationContext(), "Fill the Mobile number",
                    Toast.LENGTH_SHORT).show();
        }
        else if (nativePlace.isEmpty()){
            Toast.makeText(getApplicationContext(), "Fill native place",
                    Toast.LENGTH_SHORT).show();
        }
        else if (occupation.isEmpty()){
            Toast.makeText(getApplicationContext(), "Fill the occupation",
                    Toast.LENGTH_SHORT).show();
        }else if (bitmapPath.isEmpty()){
            Toast.makeText(getApplicationContext(), "Select or Capture the image",
                    Toast.LENGTH_SHORT).show();
        }
        else {
            Intent intent = new Intent(PersonalDetailsActivity.this, AddressActivity.class);
            Bundle extras = new Bundle();
            extras.putStringArray("headArray", headArray);
            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtras(extras);
            startActivity(intent);
        }

        /*
//        if(Name.trim().length()>0 && DOB.trim().length()>0&&PlaceOfBirth.trim().length()>0
  //              && Business.trim().length()>0 && TimeOfBirth.trim().length()>0 ){

            //Intent intent = new Intent(PersonalDetailsActivity.this,Family.class);
            Intent intent = new Intent(PersonalDetailsActivity.this,PdfCreationActivity.class);
            Bundle extras = new Bundle();
            extras.putString("Name",Name);
            extras.putString("DOB",DOB);
            extras.putString("PlaceOfBirth",PlaceOfBirth);
            extras.putString("TimeOfBirth",TimeOfBirth);
            extras.putString("Business",Business);
            extras.putString("Gender",Gender);
            intent.putExtras(extras);
            startActivity(intent);
    //    }
      //  else {
            Toast.makeText(getApplicationContext(),"Enter all the details", Toast.LENGTH_LONG).show();
        //}
        */
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
        File imgae_file = new File(file,"head_image.jpg");
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
    private void askPermission() {
        //WRITE_EXTERNAL_STORAGE Permission
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            if( Environment.getExternalStorageDirectory().canWrite()) {
                //   Toast.makeText(getApplicationContext(), "The path is writable", Toast.LENGTH_LONG).show();
            }
            else {
                //   Toast.makeText(getApplicationContext(), "The path is not writable and asking permission", Toast.LENGTH_LONG).show();

                // Here, thisActivity is the current activity
                if (ContextCompat.checkSelfPermission(PersonalDetailsActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Permission is not granted
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(PersonalDetailsActivity.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.
                        //         Toast.makeText(getApplicationContext(), "Grant the permission otherwise the app doesn't work", Toast.LENGTH_LONG).show();
                    } else {
                        // No explanation needed; request the permission
                        ActivityCompat.requestPermissions(PersonalDetailsActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
                } else {
                    // Permission has already been granted
                }

            }
        }
        else {
            //  Toast.makeText(getApplicationContext(), "MEDIA_MOUNTED not equal", Toast.LENGTH_LONG).show();
        }

        //READ_EXTERNAL_STORAGE Permission
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            if( Environment.getExternalStorageDirectory().canRead()) {
                //   Toast.makeText(getApplicationContext(), "The path is writable", Toast.LENGTH_LONG).show();
            }
            else {
                //   Toast.makeText(getApplicationContext(), "The path is not writable and asking permission", Toast.LENGTH_LONG).show();

                // Here, thisActivity is the current activity
                if (ContextCompat.checkSelfPermission(PersonalDetailsActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Permission is not granted
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(PersonalDetailsActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.
                        //         Toast.makeText(getApplicationContext(), "Grant the permission otherwise the app doesn't work", Toast.LENGTH_LONG).show();
                    } else {
                        // No explanation needed; request the permission
                        ActivityCompat.requestPermissions(PersonalDetailsActivity.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
                } else {
                    // Permission has already been granted
                }

            }
        }
        else {
            //  Toast.makeText(getApplicationContext(), "MEDIA_MOUNTED not equal", Toast.LENGTH_LONG).show();
        }

        //Camera Permission
        if (ContextCompat.checkSelfPermission(PersonalDetailsActivity.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(PersonalDetailsActivity.this,
                    Manifest.permission.CAMERA)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                //         Toast.makeText(getApplicationContext(), "Grant the permission otherwise the app doesn't work", Toast.LENGTH_LONG).show();
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(PersonalDetailsActivity.this,
                        new String[]{Manifest.permission.CAMERA},CAMERA_PERMISSION_REQ);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            case CAMERA_PERMISSION_REQ: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == CAPTURE_IMAGE){
            String path,externalPath;
            externalPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            bitmapPath = "/Family/head_image.jpg";
            path = externalPath + bitmapPath;

            bitmap = BitmapFactory.decodeFile(path);
            FileOutputStream out = null;
            try {
                File file;
                file =new File(externalPath+"/Family","head_image.jpg");
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
                tvHeadPhoto.setText(bitmapPath);
                ivHeadPhoto.setVisibility(View.VISIBLE);
                ivHeadPhoto.setImageBitmap(bitmap);
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
            bitmapPath = "/Family/head_image.jpg";
            path = externalPath + bitmapPath;
            
            FileOutputStream out = null;
            try {
                File file;
                file =new File(externalPath+"/Family","head_image.jpg");
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
                ivHeadPhoto.setVisibility(View.VISIBLE);
                ivHeadPhoto.setImageBitmap(bitmap);
                tvHeadPhoto.setText(bitmapPath);
                Log.e("Galary","not null" );
            }
            else
                Log.e("Galery"," null" );
            //imageView.setImageBitmap(bitmap);
            //bitmapPhoto = decodeSampledBitmapFromPath(filePath, getPx(219), getPx(283));

            /*
            try {
                //try3=>fail - Because of mistake
                //InputStream inputStream = getContentResolver().openInputStream(imageUri);
                //bitmap = BitmapFactory.decodeStream(inputStream);

                //try2=>fail - Because of mistake
                //bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            */
        }
    }


}
