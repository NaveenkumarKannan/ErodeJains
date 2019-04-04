package com.yarolegovich.slidingrootnav.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddressActivity extends AppCompatActivity {
    EditText etOfficeAddress,etOfficePhNo,etResidenceAddress,etResidencePhNo,etNoOfChildren,etGroup_id;
    String officeAddress,officePhNo,residenceAddress,residencePhNo,noOfChildren,group_id;
    String[] headArray=new String[11],addressArray=new String[6];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        Bundle Extras = getIntent().getExtras();
        headArray = Extras.getStringArray("headArray");
        Log.w("headArray: ",headArray[10]);

        etOfficeAddress= findViewById(R.id.etOfficeAddress);
        etOfficePhNo= findViewById(R.id.etOfficePhNo);
        etResidenceAddress= findViewById(R.id.etResidenceAddress);
        etResidencePhNo= findViewById(R.id.etResidencePhNo);
        etNoOfChildren = findViewById(R.id.etNoOfChildren);
        etGroup_id = findViewById(R.id.etGroup_id);
    }
    public void nextPage(View view) {
        officeAddress = etOfficeAddress.getText().toString();
        officePhNo = etOfficePhNo.getText().toString();
        residenceAddress = etResidenceAddress.getText().toString();
        residencePhNo = etResidencePhNo.getText().toString();
        noOfChildren = etNoOfChildren.getText().toString();
        group_id = etGroup_id.getText().toString();

        addressArray[0] = officeAddress;
        addressArray[1] = officePhNo;
        addressArray[2]= residenceAddress;
        addressArray[3] = residencePhNo;
        addressArray[4] = noOfChildren;
        addressArray[5] = group_id;
        if(officeAddress.trim().length()>0 && officePhNo.trim().length()>0
                && residenceAddress.trim().length()>0 && residencePhNo.trim().length()>0
                &&noOfChildren.trim().length()>0&&group_id.trim().length()>0){
            if(Integer.parseInt(noOfChildren)>3){
                Toast.makeText(getApplicationContext(),"Maximum 3 Children only", Toast.LENGTH_LONG ).show();
            }else{
                //Intent intent = new Intent(AddressActivity.this,child1Activity.class);
                Intent intent = new Intent(AddressActivity.this,ConfirmActivity.class);
                Bundle extras = new Bundle();
                extras.putStringArray("headArray",headArray);
                extras.putStringArray("addressArray",addressArray);
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtras(extras);
                startActivity(intent);
            }
        }else{
            Toast.makeText(this,"Enter all the details" ,Toast.LENGTH_LONG ).show();
        }

    }
    @Override
    public void onBackPressed() {
        finish();
    }

    public void goBack(View view) {
        finish();
    }
}
