package org.reginpaul;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 4000;
    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
        HashMap<String, String> params = new HashMap<>();
        if (getIntent().getExtras() != null) {
            Log.d("Bg code", "Entering bg code in splash");
            for (String key : getIntent().getExtras().keySet())
                if (key.equals("title")) {
//            title_txt.setText(getIntent().getExtras().getString(key));
                    params.put("msgtype", getIntent().getExtras().getString(key));
                    Log.d("Bg title", getIntent().getExtras().getString(key));
                } else if (key.equals("message")) {
//        msg_txt.setText(getIntent().getExtras().getString(key));
                    params.put("msg", getIntent().getExtras().getString(key));
                    Log.d("Bg message", getIntent().getExtras().getString(key));
                }
        }
//        params.put("msgtype", Notify_title);
//        params.put("msg", Notify_msg);
        Log.d("Bg code", "outside bg code in splash");

        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_CREATE_MSG , params, CODE_POST_REQUEST);
        request.execute();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);
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
            if (requestCode == CODE_POST_REQUEST) {
                Log.d("Notification url", url);
                return requestHandler.sendPostRequest(url, params);
            }

            if (requestCode == CODE_GET_REQUEST) {
                Log.d("Syllabus", url);
                String getstring = requestHandler.sendGetRequest(url);

                return requestHandler.sendGetRequest(url);
            }
            return null;
        }
    }

}
