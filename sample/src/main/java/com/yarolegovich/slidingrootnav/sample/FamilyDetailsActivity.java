package com.yarolegovich.slidingrootnav.sample;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Timer;
import java.util.TimerTask;

public class FamilyDetailsActivity extends AppCompatActivity {
    String type,headId;
    //ListView listView;
    ProgressDialog loading;
    //FamilyDetailsAdapter familyDetailsAdapter;

    TextView tvHeadName,tvOccupation,tvNativePlace,tvHeadEmail,tvHeadMobile,tvId,
            tvBgHead,tvWifeName,tvBgWife,tvFatherName,tvGhotre,tvOfficeAddress,tvOfficePhNo,tvResidenceAddress,tvResidencePhNo;
    ImageView ivHeadPhoto;

    TextView tvChildName1,tvWifeName1,tvMobNo1,tvGrand1Child1,tvGrand2Child1,tvGrand3Child1,tvGrand4Child1,tvChildBg1;
    TextView tvChildName2,tvWifeName2,tvMobNo2,tvGrand1Child2,tvGrand2Child2,tvGrand3Child2,tvGrand4Child2,tvChildBg2;
    TextView tvChildName3,tvWifeName3,tvMobNo3,tvGrand1Child3,tvGrand2Child3,tvGrand3Child3,tvGrand4Child3,tvChildBg3;
    ImageView ivChildPhoto1,ivChildPhoto2,ivChildPhoto3;
    LinearLayout llChild1,llChild2,llChild3,llFamilyData;


    //imageSlider
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_details);

        Bundle extras = getIntent().getExtras();
        headId = extras.getString("headId");

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
        tvId = (TextView) findViewById(R.id.tvId);

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

        //familyDetailsAdapter = new FamilyDetailsAdapter(this, R.layout.family_details_row_layout);
        loading = ProgressDialog.show(FamilyDetailsActivity.this, "Fetching Data...","Please Wait...",true,true);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                type = "getFamilyDetails";
                BackgroundWorkerJson backgroundWorker = new BackgroundWorkerJson();
                backgroundWorker.execute();
            }
        });
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();
    }
    @Override
    public void onBackPressed() {
        finish();
    }

    public void goBack(View view) {
        finish();
    }

    private void init() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        sliderDotspanel = (LinearLayout) findViewById(R.id.SliderDots);

        viewPager.setAdapter(viewPagerAdapter);

        dotscount = viewPagerAdapter.getCount();
        dots = new ImageView[dotscount];

        Log.e("dotscount", String.valueOf(dotscount));
        for(int i = 0; i < dotscount; i++){

            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(8, 0, 8, 0);

            sliderDotspanel.addView(dots[i], params);

        }

        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                for(int i = 0; i< dotscount; i++){
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));
                }

                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new MyTimerTask(), 2000, 4000);
    }


    public class MyTimerTask extends TimerTask {

        @Override
        public void run() {

            FamilyDetailsActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dotscount = viewPagerAdapter.getCount();

                    for(int i = 0; i <= dotscount; i++){

                        if(viewPager.getCurrentItem() == i){
                            if(viewPager.getCurrentItem() == (dotscount-1)){
                                viewPager.setCurrentItem(0);
                                break;
                            }else{
                                viewPager.setCurrentItem(i+1);
                                break;
                            }
                        }

                    }
                }
            });

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
                if(type.equals("getFamilyDetails")){
                    websiteUrl = "http://southern-electric.co.in/FamilyApp/getFamilyDetails.php";
                    Log.e(type,type );

                    post_data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(headId, "UTF-8")
                            +"&"+URLEncoder.encode("adType", "UTF-8") + "=" + URLEncoder.encode("type1", "UTF-8")
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

            if(type.equals("getFamilyDetails")){
                Log.e(type,result );
                json_string = result;

                if(json_string != null)
                {
                    try {
                        jsonObject = new JSONObject(json_string);
                        jsonArray = jsonObject.getJSONArray("getFamilyDetails");

                        int noOfChildren = 0;
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jo = jsonArray.getJSONObject(i);

                            Bitmap headPhoto;
                            String headPhotoString,headName,headMobile,occupation,nativePlace,headEmail,id;
                            id = jo.getString("id");
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
                            tvId.setText(id);

                            String bgHead,wifeName,bgWife,fatherName,ghotre,officeAddress,officePhNo,residenceAddress,residencePhNo;
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

                                String child_image;
                                Bitmap childPhoto;
                                String child_name,child_blood_grp,child_phone_no,child_hus_or_wife_name,
                                        grand_son_or_daug_name1,grand_son_or_daug_name2,grand_son_or_daug_name3,grand_son_or_daug_name4;

                                child_name = jo.getString("child_name");
                                child_image = jo.getString("child_image");
                                child_phone_no = jo.getString("child_phone_no");
                                child_hus_or_wife_name = jo.getString("child_hus_or_wife_name");
                                grand_son_or_daug_name1 = jo.getString("grand_son_or_daug_name1");
                                grand_son_or_daug_name2 = jo.getString("grand_son_or_daug_name2");
                                grand_son_or_daug_name3 = jo.getString("grand_son_or_daug_name3");
                                grand_son_or_daug_name4 = jo.getString("grand_son_or_daug_name4");
                                child_blood_grp = jo.getString("child_blood_grp");
                                byte[] decodedString = Base64.decode(child_image, Base64.DEFAULT);
                                childPhoto = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                /*
                                FamilyDetailsData familyDetailsData = new FamilyDetailsData( childPhoto,child_name,child_phone_no,child_hus_or_wife_name,
                                        grand_son_or_daug_name1,grand_son_or_daug_name2,grand_son_or_daug_name3,grand_son_or_daug_name4);
                                familyDetailsAdapter.add(familyDetailsData);
                                */
                                if(i==0){
                                    llChild1.setVisibility(View.VISIBLE);
                                    ivChildPhoto1.setImageBitmap(childPhoto);
                                    tvChildName1.setText(child_name);
                                    tvChildBg1.setText(child_blood_grp);
                                    tvWifeName1.setText(child_hus_or_wife_name);
                                    tvMobNo1.setText(child_phone_no);
                                    tvGrand1Child1.setText("Grand Child 1: "+grand_son_or_daug_name1);
                                    tvGrand2Child1.setText("Grand Child 2: "+grand_son_or_daug_name2);
                                    tvGrand3Child1.setText("Grand Child 3: "+grand_son_or_daug_name3);
                                    tvGrand4Child1.setText("Grand Child 4: "+grand_son_or_daug_name4);

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
                                    ivChildPhoto2.setImageBitmap(childPhoto);
                                    tvChildName2.setText(child_name);
                                    tvChildBg2.setText(child_blood_grp);
                                    tvWifeName2.setText(child_hus_or_wife_name);
                                    tvMobNo2.setText(child_phone_no);
                                    tvGrand1Child2.setText("Grand Child 1: "+grand_son_or_daug_name1);
                                    tvGrand2Child2.setText("Grand Child 2: "+grand_son_or_daug_name2);
                                    tvGrand3Child2.setText("Grand Child 3: "+grand_son_or_daug_name3);
                                    tvGrand4Child2.setText("Grand Child 4: "+grand_son_or_daug_name4);
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
                                    ivChildPhoto3.setImageBitmap(childPhoto);
                                    tvChildName3.setText(child_name);
                                    tvChildBg3.setText(child_blood_grp);
                                    tvWifeName3.setText(child_hus_or_wife_name);
                                    tvMobNo3.setText(child_phone_no);
                                    tvGrand1Child3.setText("Grand Child 1: "+grand_son_or_daug_name1);
                                    tvGrand2Child3.setText("Grand Child 2: "+grand_son_or_daug_name2);
                                    tvGrand3Child3.setText("Grand Child 3: "+grand_son_or_daug_name3);
                                    tvGrand4Child3.setText("Grand Child 4: "+grand_son_or_daug_name4);

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

                        jsonArray = jsonObject.getJSONArray("getImagesForSlider");

                        Bitmap[] images = new Bitmap[jsonArray.length()];
                        String[] title = new String[jsonArray.length()];
                        Log.e("jsonArray.length()", String.valueOf(jsonArray.length()));
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jo = jsonArray.getJSONObject(i);

                            Bitmap adImage;
                            String adImageString,adTitle,adType,id;

                            id = jo.getString("id");
                            adImageString = jo.getString("adImage");
                            adTitle = jo.getString("adTitle");
                            adType = jo.getString("adType");

                            byte[] decodedString = Base64.decode(adImageString, Base64.DEFAULT);
                            adImage = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                            images[i]=adImage;
                            title[i]=adTitle;
                        }
                        viewPagerAdapter = new ViewPagerAdapter(FamilyDetailsActivity.this,images,title);

                        init();
                        loading.dismiss();
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
        }
    }
}
