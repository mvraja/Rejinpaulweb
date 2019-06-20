package org.reginpaul;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
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
import org.reginpaul.fragment.EventsFragment;
import org.reginpaul.fragment.EventsRegFragment;
import org.reginpaul.fragment.HomeFragment;
import org.reginpaul.fragment.MaterialsFragment;
import org.reginpaul.fragment.ProfileFragment;
import org.reginpaul.fragment.ResultFragment;
import org.reginpaul.fragment.SyllabusFragment;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Scanner;


public class MainActivity extends AppCompatActivity implements
        SyllabusFragment.OnFragmentInteractionListener, EventsRegFragment.OnFragmentInteractionListener, MaterialsFragment.OnFragmentInteractionListener{
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

    private FirebaseAuth mAuth;
    private DatabaseReference UsersRef;

    String currentUserID, department, firebaseToken, userGender;

        private static final String AUTH_KEY = "key=AAAA5oDKL-4:APA91bEVqeEYD7lkC0EnNmFyV3qHSZcNcoFGCNSZvB0QmeO-rJJDzfzuAyRhf7tmJkYzHN7lDxma8ploBc9B8k2d8kGf85hhsu3PbpipemeCXl43yG7bEKvA-5r9DdHlbok47q7lwybR";
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private String Notify_title;
    private String Notify_msg;
    private Handler updateBarHandler;
//    ProgressDialog barProgressDialog;
private  ProgressDialog progressBar;
    private int progressBarStatus = 0;
    private Handler progressBarbHandler = new Handler();

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
        getSupportActionBar().setTitle("  "+"Home");
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
                    /*if (dataSnapshot.hasChild("profileimage")) {
                        String image = dataSnapshot.child("profileimage").getValue().toString();
                        //String image = "https://firebasestorage.googleapis.com/v0/b/rejinpaul-c8196.appspot.com/o/Profile%20Images%2FJEtueCXwe6XHlZKv01YwVhBX23s1.jpg";
                        //String image = "https://firebasestorage.googleapis.com/v0/b/rejinpaul-c8196.appspot.com/o/Profile%20Images%2FJEtueCXwe6XHlZKv01YwVhBX23s1.jpg?alt=media&token=4ebbc727-cd92-4700-afe7-c90305735559";

                        Picasso.with(MainActivity.this).load(image).placeholder(R.drawable.profile).into(NavProfileImage);
                    }*/

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
                //Log.e("Updated Token",updatedToken);
                Log.d("Firebase reg id", firebaseToken);
            }
        });
        sendWithOtherThread("tokens");

        HashMap<String, String> params = new HashMap<>();
        if (getIntent().getExtras() != null) {
            Log.d("Bg code", "Entering bg code");
            for (String key : getIntent().getExtras().keySet())
                if (key.equals("title")) {
//            title_txt.setText(getIntent().getExtras().getString(key));
                    params.put("title", getIntent().getExtras().getString(key));
                    Log.d("Bg title", getIntent().getExtras().getString(key));
                } else if (key.equals("message"))
//        msg_txt.setText(getIntent().getExtras().getString(key));
                    params.put("message", getIntent().getExtras().getString(key));

        }
//        params.put("msgtype", Notify_title);
//        params.put("msg", Notify_msg);
        Log.d("Bg code", "outside bg code");
//        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_CREATE_MSG, params, CODE_POST_REQUEST);
//        request.execute();
        /*if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(Constants.CHANNEL_ID, Constants.CHANNEL_NAME, importance);
            mChannel.setDescription(Constants.CHANNEL_DESCRIPTION);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            //mChannel.setSound();
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mNotificationManager.createNotificationChannel(mChannel);
        }*/

        //MyNotificationManager.getInstance(this).displayNotification("Greetings", "Hello how are you?");
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mRegistrationBroadcastReceiver,
                        new IntentFilter(Config.REGISTRATION_COMPLETE));
                LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mRegistrationBroadcastReceiver,
                        new IntentFilter(Config.PUSH_NOTIFICATION));

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
                    SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
                    String regId = pref.getString("regId", null);

                    Log.d("Firebase reg id noti: ", regId);
//                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    //Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();
                    Log.d("Notification text", message);
//                    txtMessage.setText(message);
                }
            }
        };
//        // Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713
        MobileAds.initialize(this, "ca-app-pub-5307742290353150~2901344057");
//
//        // Load an ad into the AdMob banner view.
        AdView adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().setRequestAgent("android_studio:ad_template").build();
        adView.loadAd(adRequest);

//        displayFirebaseRegId();
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
//            jNotification.put("title", "Rejinpaul");
//            jNotification.put("body", "Testing App");
//            jNotification.put("sound", "default");
//            jNotification.put("badge", "1");
//            jNotification.put("click_action", "OPEN_ACTIVITY_1");
//            jNotification.put("icon", "ic_notification");
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

//            Notify_title=jData.getString("title");
//            Notify_msg=jData.getString("body");
//            HashMap<String, String> params = new HashMap<>();
//            params.put("msgtype", Notify_title);
//            params.put("msg", Notify_msg);
//
//            PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_CREATE_MSG , params, CODE_POST_REQUEST);
//            request.execute();
////        intent.putExtras(bundle);
            
            
            URL url = new URL("https://fcm.googleapis.com/fcm/send");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", AUTH_KEY);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // Send FCM message content.
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(jPayload.toString().getBytes());

            // Read FCM response.
            InputStream inputStream = conn.getInputStream();
            final String resp = convertStreamToString(inputStream);

            Log.d("Notification test", jData.getString("title"));
            Handler h = new Handler(Looper.getMainLooper());
            h.post(new Runnable() {
                @Override
                public void run() {
                    String mTextView = resp;

                }
            });
            
//            HashMap<String, String> params = new HashMap<>();
//            params.put("msgtype", jPayload.getString("title"));
//            params.put("msg", jPayload.getString("message"));
//Log.d("Parms background",jPayload.getString("message"));
//            PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_CREATE_MSG , params, CODE_POST_REQUEST);
//            request.execute();
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

            /*case R.id.nav_questions:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new QuestionsFragment()).commit();
                getSupportActionBar().setTitle("Question Papers");
                break;*/

            case R.id.nav_events:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new EventsRegFragment()).commit();
                getSupportActionBar().setTitle("Events");
                break;

            case R.id.nav_notification:
                Intent intent = new Intent(getApplicationContext(),NotificationActivity.class);
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
//            title_txt.setText(getIntent().getExtras().getString(key));
//                    params.put("title", getIntent().getExtras().getString(key));
                    Log.d("Bg title", getIntent().getExtras().getString(key));
                } else if (key.equals("message"))
//        msg_txt.setText(getIntent().getExtras().getString(key));
//                    params.put("message",getIntent().getExtras().getString(key));
                    Log.d("Bg msg", getIntent().getExtras().getString(key));
        }
        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
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

    private class PerformNetworkRequest extends AsyncTask<Void, Void, String> {
        String url;
        HashMap<String, String> params;
        int requestCode;

        PerformNetworkRequest(String url, HashMap<String, String> params, int requestCode) {
            this.url = url;
            this.params = params;
            this.requestCode = requestCode;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject object = new JSONObject(s);
                if (!object.getBoolean("error")) {
                    Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                    Log.d("Syllabus", object.toString());
//                    refreshList(object.getJSONArray("pfiles"));
//                    object.put("msgtype",Notify_title);
//                    object.put("msg",Notify_msg);
                } else
                    Log.d("Syllabus", object.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            if (requestCode == CODE_POST_REQUEST)
                return requestHandler.sendPostRequest(url, params);


            if (requestCode == CODE_GET_REQUEST) {
                Log.d("Syllabus", url);
                String getstring = requestHandler.sendGetRequest(url);

                return requestHandler.sendGetRequest(url);
            }
            return null;
        }
    }
}
