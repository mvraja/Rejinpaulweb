package org.reginpaul;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

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

public class SyllabusActivity extends AppCompatActivity {

    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;

    List<Material> materialList;
    ListView listView;

    private View listViewItem;

    ProgressBar p;

    private String fileName, folder, category, temp_ctg;

    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syllabus);

        toolbar = findViewById(R.id.syll_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Syllabus Files");


        savedInstanceState = getIntent().getExtras();
        category = savedInstanceState.getString("strtext");
        if (category.equalsIgnoreCase("ece")){
            temp_ctg = "ECE";
        }
        else if (category.equalsIgnoreCase("eee")){
            temp_ctg = "EEE";
        }
        else if (category.equalsIgnoreCase("cse")){
            temp_ctg = "CSE";
        }
        else if (category.equalsIgnoreCase("it")){
            temp_ctg = "IT";
        }
        else if (category.equalsIgnoreCase("eie")){
            temp_ctg = "EIE";
        }
        else if (category.equalsIgnoreCase("civil")){
            temp_ctg = "CIV";
        }
        else if (category.equalsIgnoreCase("mech")){
            temp_ctg = "MECH";
        }
        else if (category.equalsIgnoreCase("biotech")){
            temp_ctg = "BIOTECH";
        }
        else {
            temp_ctg = "CSE";
        }
        String sDept1 = "\"" + temp_ctg + "\"";
        listView = findViewById(R.id.listView);

        materialList = new ArrayList<>();
        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_READ_MATERIALS + sDept1, null, CODE_GET_REQUEST);
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
                    Log.d("Syllabus", object.toString());
                    refreshList(object.getJSONArray("pfiles"));
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

    private void refreshList(JSONArray pfiles) throws JSONException {
        materialList.clear();
        for (int i = 0; i < pfiles.length(); i++) {
            JSONObject obj = pfiles.getJSONObject(i);
            materialList.add(new Material(obj.getInt("id"), obj.getString("name")));
            Log.d("Syllabus material", new Material(obj.getInt("id"), obj.getString("name")).toString());
        }


        MaterialAdapter materialAdapter = new MaterialAdapter(materialList);
        listView.setAdapter(materialAdapter);
    }

    class MaterialAdapter extends ArrayAdapter<Material> {

        List<Material> materialList;

        private MaterialAdapter(@NonNull List<Material> materialList) {
            super(SyllabusActivity.this, R.layout.layout_list, materialList);
            this.materialList = materialList;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            listViewItem = inflater.inflate(R.layout.layout_list, null, true);

            TextView textViewName = listViewItem.findViewById(R.id.textViewName);

            ImageView fileShare = listViewItem.findViewById(R.id.imgShare);
            ImageView fileDownload = listViewItem.findViewById(R.id.imgDown);

            final Material material = materialList.get(position);

            textViewName.setText(material.getName());

            fileShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String url = "https://rejinpaulnetwork.com/rejinpaulapp/pdf/" + material.getName() + ".pdf";
                    Log.d("Syllabus fragment", url);
                    new DownloadFile().execute(url);

                }

                class DownloadFile extends AsyncTask<String, String, String> {
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        p = new ProgressBar(SyllabusActivity.this);
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
                            Log.d("dwnld url fname", fileName);
                            folder = Environment.DIRECTORY_DOWNLOADS + File.separator;
                            Log.d("Syllabus fragment", folder);
                            File directory = new File(folder);

                            if (!directory.exists()) {
                                directory.mkdirs();
                            }

                            OutputStream output = new FileOutputStream(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + material.getName() + ".pdf");
                            Log.d("Check folder path", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + material.getName() + ".pdf");
                            byte data[] = new byte[1024];

                            long total = 0;

                            while ((count = input.read(data)) != -1) {
                                total += count;
                                Log.d("Syllabus fragment", "Check");
                                output.write(data, 0, count);
                            }

                            output.flush();

                            output.close();
                            input.close();
                            Log.d("Syllabus fragment", "Downloaded at: " + folder + fileName);
                            File outputFile = new File(Environment.getExternalStoragePublicDirectory
                                    (Environment.DIRECTORY_DOWNLOADS), material.getName() + ".pdf");
                            Uri uri = Uri.parse(String.valueOf(outputFile));

                            Intent share = new Intent();
                            share.setAction(Intent.ACTION_SEND);
                            share.setType("application/pdf");
                            share.putExtra(Intent.EXTRA_STREAM, uri);
                            share.setPackage("com.whatsapp");

                            startActivity(share);

                            return "Downloaded at: " + folder + fileName;

                        } catch (Exception e) {
                            Log.e("Error: ", e.getMessage());
                        }


                        return "Something went wrong";
                    }


                    @Override
                    protected void onPostExecute(String message) {
                        super.onPostExecute(message);

                        p.setVisibility(View.GONE);

                    }
                }
            });
            fileDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(getContext(),"Clicked download", Toast.LENGTH_LONG).show();


                    String url = "https://rejinpaulnetwork.com/rejinpaulapp/pdf/" + material.getName() + ".pdf";
                    Log.d("Syllabus fragment", url);
                    new DownloadFile().execute(url);


                }

                class DownloadFile extends AsyncTask<String, String, String> {
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        p = new ProgressBar(SyllabusActivity.this);
                        //p.setMessage("Please wait...It is downloading");
                        p.setIndeterminate(false);
                        //p.setCancelable(false);
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
                            Log.d("dwnld url fname", fileName);
                            folder = Environment.DIRECTORY_DOWNLOADS + File.separator;
                            Log.d("Syllabus fragment", folder);
                            File directory = new File(folder);

                            if (!directory.exists()) {
                                directory.mkdirs();
                            }

                            OutputStream output = new FileOutputStream(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + material.getName() + ".pdf");
                            Log.d("Check folder path", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + material.getName() + ".pdf");
                            byte data[] = new byte[1024];

                            long total = 0;

                            while ((count = input.read(data)) != -1) {
                                total += count;
                                Log.d("Syllabus fragmnet", "Check");
                                output.write(data, 0, count);
                            }
                            output.flush();

                            output.close();
                            input.close();
                            Log.d("Syllabus fragment", "Downloaded at: " + folder + fileName);
                            return "Downloaded at: " + folder + fileName;

                        } catch (Exception e) {
                            Log.e("Error: ", e.getMessage());
                        }


                        return "Something went wrong";
                    }


                    @Override
                    protected void onPostExecute(String message) {
                        super.onPostExecute(message);

                        p.setVisibility(View.GONE);
                        Toast.makeText(getContext(),"File downloaded in File Manager/"+folder+fileName,Toast.LENGTH_SHORT).show();

                    }
                }
            });

            return listViewItem;
        }
    }
}