package org.reginpaul;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class NotificationActivity extends AppCompatActivity{

    TextView titleView;
    TextView messageView;

    String title;
    String message, fileName, folder;
    List<Notify> notifylist;
    ListView listView;

    Notify notify;

    private View listViewItem;
    ProgressBar p;

    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);


//        titleView = (TextView) findViewById(R.id.title);
//        messageView = (TextView) findViewById(R.id.message);
//
//        Intent intent = getIntent();
//        title = intent.getStringExtra("title");
//        message = intent.getStringExtra("message");
//        Log.d("Notifactivity_title",title);
//        titleView.setText(title);
//        messageView.setText(message);
        listView = findViewById(R.id.listView);
        toolbar = findViewById(R.id.noti_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow =  ContextCompat.getDrawable(getApplicationContext(), R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorBrown), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Notifications");


        notifylist = new ArrayList<Notify>();
        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_READ_MSG , null, CODE_GET_REQUEST);
        request.execute();
        NotifyAdapter notifyAdapter = new NotifyAdapter(notifylist);
        listView.setAdapter(notifyAdapter);

//        listView.setOnItemClickListener(this);

    }

  /*  @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        AlertDialog.Builder adb = new AlertDialog.Builder(NotificationActivity.this);
        adb.setTitle(notify.getMsgtype());
        adb.setMessage(notify.getMsg());
        adb.setPositiveButton("Ok", null);
        adb.show();

    }*/


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
//                    Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                    Log.d("Notify", object.toString());
//                    JSONArray jsonarray=object.getJSONArray("message");
//                    Log.d("notify json",jsonarray.toString());
                    refreshList(object.getJSONArray("notes"));

                } else
                    Log.d("Notify", object.toString());

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
                Log.d("Notify", url);
                String getstring = requestHandler.sendGetRequest(url);

                return requestHandler.sendGetRequest(url);
            }
            return null;
        }
    }

    private void refreshList(JSONArray notes) throws JSONException {
        notifylist.clear();
        for (int i = 0; i < notes.length(); i++) {
            JSONObject obj = notes.getJSONObject(i);
//            String title=obj.getString("msgtype");
//            String msg=obj.getString("msg");
//            Log.d("notify msg",msg);
//            HashMap<String,String> message=new HashMap<>();
//            message.put("msgtype",title);
//            message.put("msg",msg);
//            notifylist.add(new Notify(title,msg));
            notifylist.add(new Notify( obj.getString("msgtype"),obj.getString("msg")));
            Log.d("Notify display", new Notify(obj.getString("msgtype"),obj.getString("msg")).toString());
        }


        NotifyAdapter notifyAdapter = new NotifyAdapter(notifylist);
        listView.setAdapter(notifyAdapter);
    }

    class NotifyAdapter extends ArrayAdapter<Notify> {

        List<Notify> notifyList;
        ViewGroup viewGroup;

        private NotifyAdapter(@NonNull List<Notify> notifyList) {
            super(NotificationActivity.this, R.layout.layout_notify, notifylist);
            this.notifyList = notifyList;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            listViewItem = inflater.inflate(R.layout.layout_notify, null, true);
            viewGroup = parent;
            TextView title_txt= listViewItem.findViewById(R.id.title);
            TextView msg_txt= listViewItem.findViewById(R.id.message);
            //TextView msg_link= listViewItem.findViewById(R.id.link);


//TextView show_title=listViewItem.findViewById(R.id.)
//            TextView textViewName = listViewItem.findViewById(R.id.ti);

//            TextView title = listViewItem.findViewById(R.id.title);
//            ImageView fileDownload = listViewItem.findViewById(R.id.imgDown);

            final Notify notify = notifyList.get(position);

            title_txt.setText(notify.getMsgtype());
            msg_txt.setText(notify.getMsg());
            //msg_link.setText(notify.getLink());
//if(getIntent().getExtras()!=null) {
//    for(String key:getIntent().getExtras().keySet())
//        if(key.equals("title"))
//            title_txt.setText(getIntent().getExtras().getString(key));
//
//    else if(key.equals("message"))
//        msg_txt.setText(getIntent().getExtras().getString(key));
//
//
//}


            listViewItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    View dialogView = LayoutInflater.from(NotificationActivity.this).inflate(R.layout.custom_alert, viewGroup, false);

                    AlertDialog.Builder builder = new AlertDialog.Builder(NotificationActivity.this,android.R.style.Theme_Light);
                    builder.setView(dialogView);

                    final AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                    TextView tit = alertDialog.findViewById(R.id.txt20);
                    tit.setText(notify.getMsgtype());
                    TextView tmsg = alertDialog.findViewById(R.id.txt21);
                    tmsg.setText(notify.getMsg());
                    tmsg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String getMsg = notify.getMsg();
//                            String getMsg = tmsg.getText().toString();
                            String[] part = getMsg.split("-");
                            String newUrl = part[1];
                            Log.d("NewnotifyURL",newUrl);
                            new DownloadFile().execute(newUrl);

                        }
                    });

                    Button ok = alertDialog.findViewById(R.id.buttonOk);
                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });
                }
            });
            return listViewItem;
        }
    }

    class DownloadFile extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = new ProgressBar(NotificationActivity.this);
            p.setIndeterminate(false);
            p.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection connection = url.openConnection();
                connection.connect();
                int lengthOfFile = connection.getContentLength();

                InputStream input = connection.getInputStream();
                Log.d("dwnld url name", url.toString());

                String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());

                fileName = f_url[0].substring(f_url[0].lastIndexOf('/') + 1, f_url[0].length());
                Log.d("new url fname", fileName);
                folder = Environment.DIRECTORY_DOWNLOADS + File.separator;
                Log.d("Newurl foldername", folder);
                File directory = new File(folder);

                if (!directory.exists()) {
                    directory.mkdirs();
                }

                OutputStream output = new FileOutputStream(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + fileName);
                Log.d("Check folder path", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + fileName + ".pdf");
                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
//                    Log.d("Materials fragment", "Check");
                    output.write(data, 0, count);
                }

                output.flush();

                output.close();
                input.close();
                Log.d("Materials fragment", "Downloaded at: " + folder + fileName);
//                File outputFile = new File(Environment.getExternalStoragePublicDirectory
//                        (Environment.DIRECTORY_DOWNLOADS), material.getName() + ".pdf");
//                Uri uri = Uri.parse(String.valueOf(outputFile));
//
//                Intent share = new Intent();
//                share.setAction(Intent.ACTION_SEND);
//                share.setType("application/pdf");
//                share.putExtra(Intent.EXTRA_STREAM, uri);
//                share.setPackage("com.whatsapp");
//
//                startActivity(share);

                return "Downloaded at: " + folder + fileName;

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }


            return "Something went wrong";
        }


        @Override
        protected void onPostExecute(String message) {
            super.onPostExecute(message);
            p.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        title = intent.getStringExtra("title");
        message = intent.getStringExtra("message");

        titleView.setText("Refreshed Notification: \n"+title);
        messageView.setText(message);

    }
}