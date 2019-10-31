package org.reginpaul;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.twitter.sdk.android.core.IntentUtils;

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


public class QuestionsBankActivity extends AppCompatActivity{


    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;

    List<Material> materialList;
    ListView listView;

    private View listViewItem;

    ProgressBar p;

    private String fileName, folder, category, temp_ctg, stSemester, stCourse, type;

    private Toolbar toolbar;
    private NotificationChannel mChannel;
    private NotificationManager mNotifyManager;
    private NotificationCompat.Builder mBuilder;
    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_bank);

        toolbar = findViewById(R.id.quesbank_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow =  ContextCompat.getDrawable(getApplicationContext(), R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorBrown), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("  "+"Question Bank");


        savedInstanceState = getIntent().getExtras();
        category = savedInstanceState.getString("strtext");

        category = savedInstanceState.getString("strtext");
        stSemester = savedInstanceState.getString("strSem");
        stCourse = savedInstanceState.getString("strCour");
        type = savedInstanceState.getString("type");

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
        else if (category.equalsIgnoreCase("auto")){
            temp_ctg = "AUTO";
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

        else if (category.equalsIgnoreCase("mca")){
            temp_ctg = "MCA";
        }

        else if (category.equalsIgnoreCase("mba")){
            temp_ctg = "MBA";
        }
        else {
            temp_ctg = "CSE";
        }
        String sDept1 = "\"" + temp_ctg + "\"";
        String sSemester = "\"" + stSemester + "\"";
        String sCourse = "\"" + stCourse + "\"";
        listView = findViewById(R.id.listView);

        materialList = new ArrayList<>();
//        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_READ_QUESTIONS + sDept1, null, CODE_GET_REQUEST);
        PerformNetworkRequest request = new PerformNetworkRequest("https://rejinpaulnetwork.com/rejinpaulapp/v1/Api.php?apicall=getquestionbank&category="+sDept1+"&semester="+sSemester+"&course="+sCourse, null, CODE_GET_REQUEST);
//        PerformNetworkRequest request = new PerformNetworkRequest("http://mindvoice.info/rpweb/v1/Api.php?apicall=getquestionbank&category="+sDept1+"&semester="+sSemester+"&course="+sCourse, null, CODE_GET_REQUEST);
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
//                s = EntityUtils.toString(response.getEntity());
                JSONObject object = new JSONObject(s);
                Log.d("Questions", object.toString());
                if (!object.getBoolean("error")) {
//                    Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                    Log.d("Questions", object.toString());
                    refreshList(object.getJSONArray("pfiles"));
                } else
                    Log.d("Questions", object.toString());

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
                Log.d("Questions", url);
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
            Log.d("Questions", new Material(obj.getInt("id"), obj.getString("name")).toString());
        }


        MaterialAdapter materialAdapter = new MaterialAdapter(materialList);
        listView.setAdapter(materialAdapter);
    }

    class MaterialAdapter extends ArrayAdapter<Material> {

        List<Material> materialList;
        int id = 1;


        public MaterialAdapter(@NonNull List<Material> materialList) {
            super(QuestionsBankActivity.this, R.layout.layout_list, materialList);
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
                    //Toast.makeText(getContext(),"Clicked download", Toast.LENGTH_LONG).show();


//                    String url = "http://mindvoice.info/rpweb/questionbank/" + material.getName() + ".pdf";
                    String url = "https://rejinpaulnetwork.com/rejinpaulapp/questionbank/" + material.getName() + ".pdf";
                    Log.d("Questions fragment", url);
                    new DownloadFile().execute(url);


                }

                class DownloadFile extends AsyncTask<String, String, String> {
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        p = new ProgressBar(QuestionsBankActivity.this);
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
                            Log.d("Questions fragment", folder);
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
                                Log.d("Questions fragment", "Check");
                                output.write(data, 0, count);
                            }

                            output.flush();

                            output.close();
                            input.close();
                            Log.d("Questions fragment", "Downloaded at: " + folder + fileName);
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


                    String url = "https://rejinpaulnetwork.com/rejinpaulapp/questionbank/"+material.getName()+".pdf";
//                    String url = "http://mindvoice.info/rpweb/questionbank/" + material.getName() + ".pdf";
                    Log.d("Materials fragment", url);
                    File outputFile = new File(Environment.getExternalStoragePublicDirectory
                            (Environment.DIRECTORY_DOWNLOADS), material.getName() + ".pdf");

//                    Uri fileURI = FileProvider.getUriForFile(getContext(),
//                            BuildConfig.APPLICATION_ID + ".provider",
//                            outputFile);
//
//                    Intent i = new Intent();
//                    i.setAction(android.content.Intent.ACTION_VIEW);
//                    i.setDataAndType(fileURI, "application/pdf");
//                    i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                    i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//
//                    Log.d("CHECKING intent1", String.valueOf(fileURI));
//                    pendingIntent = PendingIntent.getActivity(QuestionsBankActivity.this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

                    // Start a the operation in a background thread
//                    new Thread(
//                            new Runnable() {
//                                @Override
//                                public void run() {
//                                    int incr;
//                                    // Do the "lengthy" operation 20 times
//                                    for (incr = 0; incr <= 100; incr+=15) {
//                                        // Sets the progress indicator to a max value, the current completion percentage and "determinate" state
//                                        mBuilder.setProgress(100, incr, false);
//                                        // Displays the progress bar for the first time.
//                                        mNotifyManager.notify(id, mBuilder.build());
//                                        // Sleeps the thread, simulating an operation
//                                        try {
//                                            // Sleep for 1 second
//                                            Thread.sleep(1*1000);
//                                        } catch (InterruptedException e) {
//                                            Log.d("TAG", "sleep failure");
//                                        }
//                                    }
//                                    // When the loop is finished, updates the notification
//                                    mBuilder.setContentText("Download completed")
//                                            // Removes the progress bar
//                                            .setProgress(0,0,false)
//                                    // Creating a pending intent and wrapping our intent
//                                            .setContentIntent(pendingIntent);
//                                    mNotifyManager.notify(id, mBuilder.build());
//
//                                }
//                            }
//                            // Starts the thread by calling the run() method in its Runnable
//                    ).start();

//                    mBuilder.setContentIntent(pendingIntent);
                    Toast.makeText(getContext(),"File Download in Progress",Toast.LENGTH_LONG).show();
                    new DownloadFile().execute(url);

                }


                class DownloadFile extends AsyncTask<String, String, String> {
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        p = new ProgressBar(QuestionsBankActivity.this);
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
                            Log.d("Questions fragment", folder);
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
                                Log.d("Questions fragment", "Check");
                                output.write(data, 0, count);
                            }
                            output.flush();

                            output.close();
                            input.close();
                            Log.d("Questions fragment", "Downloaded at: " + folder + fileName);
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
//                        fileName = f_url[0].substring(f_url[0].lastIndexOf('/') + 1, f_url[0].length());
//                        Log.d("dwnld url fname", fileName);
                        folder = Environment.DIRECTORY_DOWNLOADS + File.separator;
                        Toast.makeText(getContext(),"File downloaded in File Manager/"+folder,Toast.LENGTH_SHORT).show();
                        File open = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + material.getName() + ".pdf");
                        Uri fileURI = FileProvider.getUriForFile(getContext(),
                                BuildConfig.APPLICATION_ID + ".provider",
                                open);
                        Intent i = new Intent();
                        i.setAction(android.content.Intent.ACTION_VIEW);
                        i.setDataAndType(fileURI, "application/pdf");
                        i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

                        startActivity(i);

                    }
                }
            });
//            mBuilder.setContentIntent(pendingIntent);
            return listViewItem;
        }
        public  boolean isStoragePermissionGranted() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    Log.v("Question","Permission is granted");
                    return true;
                } else {

                    Log.v("Question","Permission is revoked");
                    ActivityCompat.requestPermissions(QuestionsBankActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    return false;
                }
            }
            else { //permission is automatically granted on sdk<23 upon installation
                Log.v("Question","Permission is granted");
                return true;
            }
        }
    }


}
