package com.yarolegovich.slidingrootnav.sample;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;
import com.yarolegovich.slidingrootnav.sample.menu.DrawerAdapter;
import com.yarolegovich.slidingrootnav.sample.menu.DrawerItem;
import com.yarolegovich.slidingrootnav.sample.menu.SimpleItem;
import com.yarolegovich.slidingrootnav.sample.menu.SpaceItem;
import com.yarolegovich.slidingrootnav.sample.fragment.CenteredTextFragment;
import com.yarolegovich.slidingrootnav.sample.receiver.NetworkStateChangeReceiver;

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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import static com.yarolegovich.slidingrootnav.sample.receiver.NetworkStateChangeReceiver.IS_NETWORK_AVAILABLE;

/**
 * Created by yarolegovich on 25.03.2017.
 */

public class SampleActivity extends AppCompatActivity implements DrawerAdapter.OnItemSelectedListener {

    private static final int POS_DASHBOARD = 0;
    private static final int POS_OUR_PROFILE = 1;
    private static final int POS_CO_PARTNERS = 2;
    private static final int POS_ADD_MEMBERS = 3;
    private static final int POS_PRINT = 4;
    private static final int POS_SEND_PUSH = 5;
    private static final int POS_LOGOUT = 7;

    private String[] screenTitles;
    private Drawable[] screenIcons;

    private SlidingRootNav slidingRootNav;

    String type,userName;
    TextView tvUserName,tvHome,tvOurProfile,tvCoPartners,tvAddMembers,tvNotification,tvPrint,tvLogout,tvMembersInfo,tvBlood,tvTicketBooking;

    String download_url;
    SessionManager session;

    //imageSlider
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;

    ProgressDialog loading;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);

        IntentFilter intentFilter = new IntentFilter(NetworkStateChangeReceiver.NETWORK_AVAILABLE_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean isNetworkAvailable = intent.getBooleanExtra(IS_NETWORK_AVAILABLE, false);
                String networkStatus = isNetworkAvailable ? "connected" : "disconnected";

                Snackbar.make(findViewById(R.id.activity_main), "Network Status: " + networkStatus, Snackbar.LENGTH_LONG).show();
            }
        }, intentFilter);

        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        else
            connected = false;

        if(connected == false)
        {
            Intent intent = new Intent(SampleActivity.this,Internet_Connection.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

            finish();
        }else {
            // Session class instance
            session = new SessionManager(getApplicationContext());
            //Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();

            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            // get user data from session
            HashMap<String, String> user = session.getUserDetails();
            // name
            userName = user.get(SessionManager.KEY_NAME);

            slidingRootNav = new SlidingRootNavBuilder(this)
                    .withToolbarMenuToggle(toolbar)
                    .withMenuOpened(false)
                    .withContentClickableWhenMenuOpened(false)
                    .withSavedState(savedInstanceState)
                    .withMenuLayout(R.layout.menu_left_drawer)
                    .inject();

            screenIcons = loadScreenIcons();
            screenTitles = loadScreenTitles();

            DrawerAdapter adapter = new DrawerAdapter(Arrays.asList(
                    createItemFor(POS_DASHBOARD).setChecked(true),
                    createItemFor(POS_OUR_PROFILE),
                    createItemFor(POS_CO_PARTNERS),
                    createItemFor(POS_ADD_MEMBERS),
                    createItemFor(POS_PRINT),
                    createItemFor(POS_SEND_PUSH),
                    new SpaceItem(48),
                    createItemFor(POS_LOGOUT)));
            adapter.setListener(this);

            RecyclerView list = findViewById(R.id.list);
            list.setNestedScrollingEnabled(false);
            list.setLayoutManager(new LinearLayoutManager(this));
            list.setAdapter(adapter);

            adapter.setSelected(POS_DASHBOARD);

            tvUserName = findViewById(R.id.tvUserName);
            tvUserName.setText(userName);

            tvHome = findViewById(R.id.tvHome);
            tvOurProfile = findViewById(R.id.tvOurProfile);
            tvCoPartners = findViewById(R.id.tvCoPartners);
            tvAddMembers = findViewById(R.id.tvAddMembers);
            tvNotification = findViewById(R.id.tvNotification);
            tvPrint = findViewById(R.id.tvPrint);
            tvLogout = findViewById(R.id.tvLogout);
            tvMembersInfo = findViewById(R.id.tvMembersInfo);
            tvBlood = findViewById(R.id.tvBlood);
            tvTicketBooking = findViewById(R.id.tvTicketBooking);


            tvTicketBooking.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(SampleActivity.this,TicketBooking.class);
                    startActivity(i);
                }
            });
            tvHome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    slidingRootNav.closeMenu();
                }
            });
            tvMembersInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(SampleActivity.this,SearchMember.class);
                    startActivity(i);
                }
            });
            tvBlood.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(SampleActivity.this,BloodActivity.class);
                    startActivity(i);
                }
            });
            tvOurProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(SampleActivity.this,ProfileActivity.class);
                    startActivity(i);
                }
            });
            tvCoPartners.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(SampleActivity.this,AddUserActivity.class);
                    startActivity(i);
                }
            });
            tvAddMembers.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(SampleActivity.this,PersonalDetailsActivity.class);
                /*
                Intent intent = new Intent(SampleActivity.this,child1Activity.class);
                 Bundle extras = new Bundle();
                extras.putInt("noOfChildrenInt", 3);
                extras.putInt("countChild", 1);
                extras.putString("headName","Rajini" );
                intent.putExtras(extras);
                 */
                    startActivity(intent);

                }
            });
            tvNotification.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(SampleActivity.this,SendPushNotification.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            });
            tvPrint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //download_url = "http://southern-electric.co.in/FamilyApp/Jain.apk";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(download_url));
                    startActivity(i);

                }
            });
            tvLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    session.logoutUser();
                    BackgroundWorker backgroundWorker = new BackgroundWorker();
                    type = "status";
                    backgroundWorker.execute();
                }
            });
            Button l1= (Button) findViewById(R.id.l1);
            Button l2= (Button) findViewById(R.id.l2);
            Button l3= (Button) findViewById(R.id.l3);
            Button l4= (Button) findViewById(R.id.l4);
            Button l5= (Button) findViewById(R.id.l5);


            l1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(SampleActivity.this,ViewDetailsActivity.class);
                    Bundle extras = new Bundle();
                    extras.putString("group_id", "1");
                    intent.putExtras(extras);
                    startActivity(intent);
                }
            });
            l2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(SampleActivity.this,ViewDetailsActivity.class);
                    Bundle extras = new Bundle();
                    extras.putString("group_id", "2");
                    intent.putExtras(extras);
                    startActivity(intent);
                }
            });
            l3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(SampleActivity.this,ViewDetailsActivity.class);
                    Bundle extras = new Bundle();
                    extras.putString("group_id", "3");
                    intent.putExtras(extras);
                    startActivity(intent);
                }
            });
            l4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(SampleActivity.this,ViewDetailsActivity.class);
                    Bundle extras = new Bundle();
                    extras.putString("group_id", "4");
                    intent.putExtras(extras);
                    startActivity(intent);
                }
            });
            l5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(SampleActivity.this,ViewDetailsActivity.class);
                    Bundle extras = new Bundle();
                    extras.putString("group_id", "5");
                    intent.putExtras(extras);
                    startActivity(intent);
                }
            });

            loading = ProgressDialog.show(SampleActivity.this, "Fetching Data...","Please Wait...",true,true);
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    type = "getImagesForSlider";
                    BackgroundWorker backgroundWorker = new BackgroundWorker();
                    backgroundWorker.execute();
                }
            });
            thread.setPriority(Thread.MIN_PRIORITY);
            thread.start();
        }

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

            SampleActivity.this.runOnUiThread(new Runnable() {
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


    private Boolean exit = false;
    @Override
    public void onBackPressed() {

        if (exit) {
            finish(); // finish activity
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }
    }
    @Override
    public void onItemSelected(int position) {
        if (position == POS_LOGOUT) {
            session.logoutUser();
            BackgroundWorker backgroundWorker = new BackgroundWorker();
            type = "status";
            backgroundWorker.execute();
        }else if (position == POS_OUR_PROFILE) {
            Intent intent = new Intent(SampleActivity.this,ProfileActivity.class);
            //Intent intent = new Intent(SampleActivity.this,ViewDetailsActivity.class);
            startActivity(intent);
        }else if (position == POS_CO_PARTNERS) {
            Intent intent = new Intent(SampleActivity.this,AddUserActivity.class);
            //Intent intent = new Intent(SampleActivity.this,ViewDetailsActivity.class);
            startActivity(intent);
        }else if (position == POS_ADD_MEMBERS) {
            Intent intent = new Intent(SampleActivity.this,PersonalDetailsActivity.class);
            /*
            Intent intent = new Intent(SampleActivity.this,child1Activity.class);
            Bundle extras = new Bundle();
            extras.putInt("noOfChildrenInt", 3);
            extras.putInt("countChild", 1);
            intent.putExtras(extras);
            */
            startActivity(intent);
        }else if (position == POS_PRINT) {
            Intent intent = new Intent(SampleActivity.this,ProfileActivity.class);
            startActivity(intent);
        }else if (position == POS_SEND_PUSH) {
            //Intent intent = new Intent(SampleActivity.this,PersonalDetailsActivity.class);
            Intent intent = new Intent(SampleActivity.this,SendPushNotification.class);
            startActivity(intent);
        }
        slidingRootNav.closeMenu();
        Fragment selectedScreen = CenteredTextFragment.createFor(screenTitles[position]);
        showFragment(selectedScreen);
    }

    private void showFragment(Fragment fragment) {
        getFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    private DrawerItem createItemFor(int position) {
        return new SimpleItem(screenIcons[position], screenTitles[position])
                .withIconTint(color(R.color.textColorSecondary))
                .withTextTint(color(R.color.textColorPrimary))
                .withSelectedIconTint(color(R.color.colorAccent))
                .withSelectedTextTint(color(R.color.colorAccent));
    }

    private String[] loadScreenTitles() {
        return getResources().getStringArray(R.array.ld_activityScreenTitles);
    }

    private Drawable[] loadScreenIcons() {
        TypedArray ta = getResources().obtainTypedArray(R.array.ld_activityScreenIcons);
        Drawable[] icons = new Drawable[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            int id = ta.getResourceId(i, 0);
            if (id != 0) {
                icons[i] = ContextCompat.getDrawable(this, id);
            }
        }
        ta.recycle();
        return icons;
    }

    @ColorInt
    private int color(@ColorRes int res) {
        return ContextCompat.getColor(this, res);
    }

    public class BackgroundWorker extends AsyncTask<String,Void,String> {
        String json_string;
        JSONArray jsonArray;
        JSONObject jsonObject;

        android.app.AlertDialog alertDialog;

        @Override
        protected String doInBackground(String... params) {
            try {
                String post_data = null;
                String login_url = null;

                if(type.equals("status")) {
                    login_url = "http://southern-electric.co.in/FamilyApp/LoginSetStatus.php";
                    post_data = URLEncoder.encode("userName","UTF-8")+"="+ URLEncoder.encode(userName,"UTF-8");
                }
                if(type.equals("getImagesForSlider")){
                    login_url = "http://southern-electric.co.in/FamilyApp/getImagesForSlider.php";
                    Log.e(type,type );

                    post_data = URLEncoder.encode("adType", "UTF-8") + "=" + URLEncoder.encode("type2", "UTF-8")
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
            //Log.e("Json", result);
            if(type.equals("status")){
                Toast.makeText(SampleActivity.this,"You are logged out successfully" ,Toast.LENGTH_LONG ).show();
                finish();
            }
            if(type.equals("getImagesForSlider")){
                
                json_string = result;

                if(json_string != null)
                {
                    Log.e(type,result );
                    try {
                        jsonObject = new JSONObject(json_string);
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
                        loading.dismiss();
                        viewPagerAdapter = new ViewPagerAdapter(SampleActivity.this,images,title);

                        init();

                        String versionNamePlay,versionNameApp = null;
                        versionNamePlay = jsonObject.getString("versionName");
                        download_url = jsonObject.getString("url");
                        Log.e("versionNamePlay", versionNamePlay);

                        try {
                            PackageInfo pInfo = SampleActivity.this.getPackageManager().getPackageInfo(getPackageName(), 0);
                            versionNameApp= pInfo.versionName;
                            Log.e("versionNameApp", versionNameApp);
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }
                        if(!versionNameApp.equals(versionNamePlay)){
                            final android.support.v7.app.AlertDialog.Builder notifyNewVersionApp = new android.support.v7.app.AlertDialog.Builder(SampleActivity.this);
                            notifyNewVersionApp.setTitle("Product update");
                            notifyNewVersionApp.setMessage("A new version is available. Would you like to upgrade now?\n(Current: "+versionNameApp+" Latest: "+versionNamePlay);
                            notifyNewVersionApp.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String url = "https://play.google.com/store/apps/details?id=com.androfocus.investnshare";
                                    Intent i = new Intent(Intent.ACTION_VIEW);
                                    i.setData(Uri.parse(url));
                                    startActivity(i);
                                }
                            });
                            notifyNewVersionApp.setNegativeButton("Not Now", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    android.support.v7.app.AlertDialog alert1 = notifyNewVersionApp.create();
                                    alert1.cancel();
                                }
                            });
                            notifyNewVersionApp.show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else {
                    Log.e("JSON","null" );
                }
            }
        }


    }
}
