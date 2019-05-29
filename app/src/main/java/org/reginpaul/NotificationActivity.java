package org.reginpaul;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NotificationActivity extends AppCompatActivity{

    TextView titleView;
    TextView messageView;

    String title;
    String message;
    List<Notify> notifylist;
    ListView listView;

    private View listViewItem;

    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;
    private ProgressBar p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

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

        notifylist = new ArrayList<Notify>();
        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_READ_MSG , null, CODE_GET_REQUEST);
        request.execute();
        NotifyAdapter notifyAdapter = new NotifyAdapter(notifylist);
        listView.setAdapter(notifyAdapter);

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

        private NotifyAdapter(@NonNull List<Notify> notifyList) {
            super(NotificationActivity.this, R.layout.layout_notify, notifylist);
            this.notifyList = notifyList;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            listViewItem = inflater.inflate(R.layout.layout_notify, null, true);
            TextView title_txt=(TextView) listViewItem.findViewById(R.id.title);
            TextView msg_txt=(TextView) listViewItem.findViewById(R.id.message);
//TextView show_title=listViewItem.findViewById(R.id.)
//            TextView textViewName = listViewItem.findViewById(R.id.ti);

//            TextView title = listViewItem.findViewById(R.id.title);
//            ImageView fileDownload = listViewItem.findViewById(R.id.imgDown);

            final Notify notify = notifyList.get(position);

            title_txt.setText(notify.getMsgtype());
            msg_txt.setText(notify.getMsg());
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
            return listViewItem;
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