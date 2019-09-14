package org.reginpaul;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;


import de.hdodenhof.circleimageview.CircleImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.reginpaul.fragment.AboutusFragment;
import org.reginpaul.fragment.EventsFragment;
import org.reginpaul.fragment.EventsRegFragment;
import org.reginpaul.fragment.HomeFragment;
import org.reginpaul.fragment.MaterialsFragment;
import org.reginpaul.fragment.ProfileFragment;
import org.reginpaul.fragment.ResultFragment;
import org.reginpaul.fragment.SearchFragment;
import org.reginpaul.fragment.SyllabusFragment;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;


public class MainActivity extends AppCompatActivity implements
        SyllabusFragment.OnFragmentInteractionListener, EventsRegFragment.OnFragmentInteractionListener, MaterialsFragment.OnFragmentInteractionListener {
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar mToolbar;
    private FrameLayout fragmentPage;
    private android.support.v4.app.Fragment fragment = null;

    private CircleImageView NavProfileImage;
    private TextView NavProfileUserName;
    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;
    private static final int SYSTEM_ALERT_WINDOW_PERMISSION = 2084;
    private FirebaseAuth mAuth;
    private DatabaseReference UsersRef;

    String currentUserID, department, firebaseToken, userGender;

    private static final String AUTH_KEY = "key=AAAAofV9caU:APA91bFEwCBQ5GNRJugmOoTgUkUC4TSW872j8qVPpN47dQoRHg_Ej4nWPm81HLu3VEhFewUi1_s6czLh-A-9hlnC_KA-CfmHvfZ-JjBsifcZ9hXZOovS7Ry5VO0eM9OdFNUhCLDQRN0m";
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private Handler updateBarHandler;
    private ProgressDialog progressBar;
    private FloatingActionButton fab, fab1,fab2,fab3,fab4;
    private boolean isFABOpen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        updateBarHandler = new Handler();
        progressBar = new ProgressDialog(MainActivity.this);
        progressBar.setCancelable(true);
        progressBar.setMessage("Please wait for a while..");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        progressBar.show();

        updateBarHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.dismiss();
            }
        }, 5000);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            askPermission();
        }

        fab = findViewById(R.id.fab);
        fab1 = findViewById(R.id.fab1);
        fab2 = findViewById(R.id.fab2);
        fab3 = findViewById(R.id.fab3);
        fab4 = findViewById(R.id.fab4);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isFABOpen){
                    showFABMenu();
                }else{
                    closeFABMenu();
                }
            }
        });

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent share = new Intent();
                share.setAction(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_TEXT, "Share this education app and help your friends to enjoy benefits. https://play.google.com/store/apps/details?id=org.reginpaul&hl=en");
                share.setPackage("com.whatsapp");

                startActivity(share);

            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent share = new Intent();
                String urltoshare="https://play.google.com/store/apps/details?id=org.reginpaul&hl=en";
                share.setAction(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_TEXT, urltoshare);
                boolean facebookAppFound = false;
                List<ResolveInfo> matches = getPackageManager().queryIntentActivities(share, 0);
                for (ResolveInfo info : matches) {
                    if (info.activityInfo.packageName.toLowerCase().startsWith("com.facebook.katana")) {
                        share.setPackage(info.activityInfo.packageName);
                        facebookAppFound = true;
                        break;
                    }
                }

                if (!facebookAppFound) {
                    String sharerUrl = "https://www.facebook.com/sharer/sharer.php?u=" + urltoshare;
                    share = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
                }

                startActivity(share);
            }
        });

        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent share = new Intent();
                share.setAction(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_TEXT, "Share this education app and help your friends to enjoy benefits.https://play.google.com/store/apps/details?id=org.reginpaul&hl=en");
                share.setPackage("com.instagram.android");

                startActivity(share);

            }
        });

        fab4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tweetIntent = new Intent(Intent.ACTION_SEND);
                tweetIntent.putExtra(Intent.EXTRA_TEXT, "Share this education app and help your friends to enjoy benefits.https://play.google.com/store/apps/details?id=org.reginpaul&hl=en");
                tweetIntent.setType("text/plain");

                PackageManager packManager = getPackageManager();
                List<ResolveInfo> resolvedInfoList = packManager.queryIntentActivities(tweetIntent,  PackageManager.MATCH_DEFAULT_ONLY);

                boolean resolved = false;
                for(ResolveInfo resolveInfo: resolvedInfoList){
                    if(resolveInfo.activityInfo.packageName.startsWith("com.twitter.android")){
                        tweetIntent.setClassName(
                                resolveInfo.activityInfo.packageName,
                                resolveInfo.activityInfo.name );
                        resolved = true;
                        break;
                    }
                }
                if(resolved){
                    startActivity(tweetIntent);
                }else{
                    Toast.makeText(MainActivity.this, "Twitter app not found", Toast.LENGTH_LONG).show();
                }
            }
        });

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");

        fragmentPage = findViewById(R.id.main_container);
        mToolbar = findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        //getSupportActionBar().setTitle("Home action");

        drawerLayout = findViewById(R.id.drawable_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setIcon(R.drawable.logo_small);
        getSupportActionBar().setTitle("  " + "Home");
        navigationView = findViewById(R.id.navigation_view);


        View navView = navigationView.inflateHeaderView(R.layout.navigation_header);
        NavProfileImage = navView.findViewById(R.id.nav_profile_image);
        NavProfileUserName = navView.findViewById(R.id.nav_user_full_name);


        UsersRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (dataSnapshot.hasChild("username")) {
                        String fullname = dataSnapshot.child("username").getValue().toString();
                        NavProfileUserName.setText(fullname);
                    }

                    if (dataSnapshot.hasChild("gender")) {
                        userGender = dataSnapshot.child("gender").getValue().toString();
                        if (userGender.equalsIgnoreCase("male")) {
                            NavProfileImage.setImageResource(R.drawable.male);
                        } else {
                            NavProfileImage.setImageResource(R.drawable.female);
                        }
                    }
                    if (dataSnapshot.hasChild("dept")) {
                        department = dataSnapshot.child("dept").getValue().toString();
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new HomeFragment()).commit();
            getSupportActionBar().setTitle("Home");
            navigationView.setCheckedItem(R.id.nav_home);
        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                UserMenuSelector(item);
                drawerLayout.closeDrawer(GravityCompat.START);
                return false;
            }
        });

        //notification code

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(MainActivity.this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                firebaseToken = instanceIdResult.getToken();
                Log.d("Firebase reg id", firebaseToken);
            }
        });
        sendWithOtherThread("tokens");

        HashMap<String, String> params = new HashMap<>();
        if (getIntent().getExtras() != null) {

            for (String key : getIntent().getExtras().keySet())
                if (key.equals("title")) {

                    params.put("title", getIntent().getExtras().getString(key));
                } else if (key.equals("message"))
                    params.put("message", getIntent().getExtras().getString(key));

        }
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mRegistrationBroadcastReceiver,
                        new IntentFilter(Config.REGISTRATION_COMPLETE));
                LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mRegistrationBroadcastReceiver,
                        new IntentFilter(Config.PUSH_NOTIFICATION));

                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
                    SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
                    String regId = pref.getString("regId", null);


                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {

                    String message = intent.getStringExtra("message");

                }
            }
        };

        MobileAds.initialize(this, getString(R.string.admob_app_id));
        AdView adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().setRequestAgent("android_studio:ad_template").build();
        adView.loadAd(adRequest);
//        ImageView fb=(ImageView)findViewById(R.id.img_fb);
//        ImageView insta=(ImageView)findViewById(R.id.img_insta);
//        ImageView youtube=(ImageView)findViewById(R.id.img_youtube);
//        ImageView web=(ImageView)findViewById(R.id.img_website);
//        fb.setOnClickListener(this);
//        insta.setOnClickListener(this);
//        youtube.setOnClickListener(this);
//        web.setOnClickListener(this);


    }

    private void askPermission() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, SYSTEM_ALERT_WINDOW_PERMISSION);
    }
    private void showFABMenu(){
        isFABOpen=true;
        fab1.animate().translationY(getResources().getDimension(R.dimen.standard_55));
        fab2.animate().translationY(getResources().getDimension(R.dimen.standard_105));
        fab3.animate().translationY(getResources().getDimension(R.dimen.standard_155));
        fab4.animate().translationY(getResources().getDimension(R.dimen.standard_205));
    }

    private void closeFABMenu(){
        isFABOpen=false;
        fab1.animate().translationY(0);
        fab2.animate().translationY(0);
        fab3.animate().translationY(0);
        fab4.animate().translationY(0);
    }
    private void sendWithOtherThread(final String type) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                pushNotification(type);
            }
        }).start();
    }

    private void pushNotification(String type) {
        JSONObject jPayload = new JSONObject();
        JSONObject jNotification = new JSONObject();
        JSONObject jData = new JSONObject();
        try {
            jData.put("title", "Rejinpaul");
            jData.put("body", "Testing App");
            jData.put("sound", "default");
            jData.put("badge", "1");
            jData.put("click_action", "OPEN_ACTIVITY_1");
            jData.put("icon", "ic_notification");
            jData.put("picture", "http://opsbug.com/static/google-io.jpg");

            switch (type) {
                case "tokens":
                    JSONArray ja = new JSONArray();
                    ja.put(AUTH_KEY);
                    ja.put(firebaseToken);
                    jPayload.put("registration_ids", ja);
                    break;
                default:
                    jPayload.put("to", firebaseToken);
            }

            jPayload.put("priority", "high");
            jPayload.put("notification", jNotification);
            jPayload.put("data", jData);

            URL url = new URL("https://fcm.googleapis.com/fcm/send");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", AUTH_KEY);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(jPayload.toString().getBytes());

            InputStream inputStream = conn.getInputStream();
            final String resp = convertStreamToString(inputStream);

            Handler h = new Handler(Looper.getMainLooper());
            h.post(new Runnable() {
                @Override
                public void run() {
                    String mTextView = resp;

                }
            });

        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    private String convertStreamToString(InputStream is) {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next().replace(",", ",\n") : "";
    }


    public String getDept() {
        return department;
    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            SendUserToLoginActivity();
        } else {
            CheckUserExistence();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private void CheckUserExistence() {
        final String current_user_id = mAuth.getCurrentUser().getUid();

        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChild(current_user_id)) {
                    SendUserToSetupActivity();
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void SendUserToSetupActivity() {
        Intent setupIntent = new Intent(MainActivity.this, SetupActivity.class);
        setupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(setupIntent);
        finish();
    }


    private void SendUserToLoginActivity() {
        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void UserMenuSelector(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new HomeFragment()).commit();
                getSupportActionBar().setTitle("Home");
                break;

            case R.id.nav_syllabus:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new SyllabusFragment()).commit();
                getSupportActionBar().setTitle("Syllabus");
                break;

            case R.id.nav_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new ProfileFragment()).commit();
                getSupportActionBar().setTitle("Profile");
                break;

            case R.id.nav_materials:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new MaterialsFragment()).commit();
                getSupportActionBar().setTitle("Study Materials");
                break;


            case R.id.nav_events:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new EventsRegFragment()).commit();
                getSupportActionBar().setTitle("Events");
                break;

            case R.id.nav_notification:
                Intent intent = new Intent(getApplicationContext(), NotificationActivity.class);
                startActivity(intent);
                break;

            case R.id.nav_result:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new ResultFragment()).commit();
                getSupportActionBar().setTitle("Results");
                break;

            case R.id.nav_logout:
                mAuth.signOut();
                SendUserToLoginActivity();
                break;

            case  R.id.nav_about:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new AboutusFragment()).commit();
                getSupportActionBar().setTitle("About us");
                break;

            case R.id.nav_search:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new SearchFragment()).commit();
                getSupportActionBar().setTitle("Search");
                break;
        }
    }

    @Override
    public void onBackPressed() {
        fragment = getSupportFragmentManager().findFragmentById(R.id.main_container);
        DrawerLayout drawerLayout = findViewById(R.id.drawable_layout);
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (fragment instanceof HomeFragment) {
                super.onBackPressed();
            } else {
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new HomeFragment()).commit();
                getSupportActionBar().setTitle("Home");
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Bg code", "Entering onresume");
        if (getIntent().getExtras() != null) {
            Log.d("Bg code", "Entering bg code");
            for (String key : getIntent().getExtras().keySet())
                if (key.equals("title")) {
                } else if (key.equals("message"))
                    Log.d("Bg msg", getIntent().getExtras().getString(key));
        }
        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.img_fb:
//                Uri urifb = Uri.parse("https://www.facebook.com/macroidapps/");
//                Intent fbintent = new Intent(Intent.ACTION_VIEW, urifb);
//                startActivity(fbintent);
//                break;
//            case R.id.img_insta:
//                Uri uriinsta = Uri.parse("https://www.facebook.com/macroidapps/");
//                Intent instaintent = new Intent(Intent.ACTION_VIEW, uriinsta);
//                startActivity(instaintent);
//                break;
//            case R.id.img_youtube:
//                Uri yturi = Uri.parse("https://www.youtube.com/channel/UCbeg_YK3R1mKo7xhM5-2Yeg");
//                Intent ytintent = new Intent(Intent.ACTION_VIEW, yturi);
//                startActivity(ytintent);
//                break;
//            case R.id.img_website:
//                Uri weburi = Uri.parse("http://Rejinpaul.com/");
//                Intent webintent = new Intent(Intent.ACTION_VIEW, weburi);
//                startActivity(webintent);
//                break;
//        }
//    }
}
