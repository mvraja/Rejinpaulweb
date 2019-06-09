package org.reginpaul;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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

public class ResultActivity extends AppCompatActivity {

    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;

    List<Result> materialList;
    ListView listView;

    private View listViewItem;
    ProgressBar p;

    private String clgName;
    private Toolbar toolbar;
    private WebView webView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        toolbar = findViewById(R.id.res_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow = ContextCompat.getDrawable(getApplicationContext(), R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorBrown), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Result Display Page");


        savedInstanceState = getIntent().getExtras();
        clgName = savedInstanceState.getString("stRes");

        listView = findViewById(R.id.listView);
        webView = findViewById(R.id.webView);

        //String sRes = "\"" + clgName + "\"";

        materialList = new ArrayList<>();
        PerformNetworkRequest request = new PerformNetworkRequest("http://mindvoice.info/rpweb/v1/Api.php?apicall=getresult&name="+clgName, null, CODE_GET_REQUEST);
        request.execute();
        MaterialAdapter materialAdapter = new MaterialAdapter(materialList);
        listView.setAdapter(materialAdapter);

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
                    Log.d("Results", object.toString());
                    refreshList(object.getJSONArray("results"));
                } else

                    Log.d("Results", object.toString());

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
                Log.d("Results", url);
                String getstring = requestHandler.sendGetRequest(url);

                return requestHandler.sendGetRequest(url);
            }
            return null;
        }
    }

    private void refreshList(JSONArray pfiles) throws JSONException {
        materialList.clear();
        for (int i = 0; i < pfiles.length(); i++) {
            JSONObject obj = pfiles.getJSONObject(i);
            materialList.add(new Result(obj.getString("name"), obj.getString("linkname")));
            Log.d("Results", new Result(obj.getString("name"), obj.getString("linkname")).toString());
        }


        MaterialAdapter materialAdapter = new MaterialAdapter(materialList);
        listView.setAdapter(materialAdapter);
    }

    class MaterialAdapter extends ArrayAdapter<Result> {

        List<Result> materialList;

        public MaterialAdapter(@NonNull List<Result> materialList) {
            super(ResultActivity.this, R.layout.layout_result, materialList);
            this.materialList = materialList;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            listViewItem = inflater.inflate(R.layout.layout_result, null, true);

            final TextView textViewName = listViewItem.findViewById(R.id.textViewName);

            final Result material = materialList.get(position);

            textViewName.setText(material.getLinkName());


            textViewName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String selectedItem = (String) textViewName.getText();
                    //Toast.makeText(getApplicationContext(),selectedItem,Toast.LENGTH_SHORT).show();
                    listView.setVisibility(View.GONE);
                    webView.setVisibility(View.VISIBLE);
                    webView.setWebViewClient(new inlineBrowser());
                    webView.getSettings().setLoadWithOverviewMode(true);
                    webView.getSettings().setUseWideViewPort(true);
                    webView.getSettings().setLoadsImagesAutomatically(true);
                    webView.getSettings().setJavaScriptEnabled(true);
                    webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
                    webView.loadUrl(selectedItem);
                    /*webView.getSettings().setJavaScriptEnabled(true);
                    webView.setWebViewClient(new WebViewClient(){

                        @Override
                        public boolean shouldOverrideUrlLoading(WebView view, String url){
                            view.loadUrl(selectedItem);
                            return true;
                        }
                    });*/

                }
            });

            return listViewItem;

        }
    }

    private class inlineBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}