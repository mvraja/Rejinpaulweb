package org.reginpaul.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
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
import org.reginpaul.Api;
import org.reginpaul.BuildConfig;

import org.reginpaul.R;
import org.reginpaul.RequestHandler;

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

public class AUimpquesFragment extends Fragment {
    String url;

    List<Impques> ImpquesList;
    ListView listView;
    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;
    private View listViewItem;
    ProgressBar p;
    private String fileName,folder;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_auimpques, container, false);
        String sDept1 = "%22au%22";
        listView = rootView.findViewById(R.id.listView);
        p = rootView.findViewById(R.id.pbar);
        ImpquesList = new ArrayList<>();
        PerformNetworkRequest request = new PerformNetworkRequest("https://rejinpaulnetwork.com/rejinpaulapp/v1/Api.php?apicall=getimpques&category=%22au%22", null, CODE_GET_REQUEST);
//        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_READ_TIMETABLE + sDept1, null, CODE_GET_REQUEST);
        request.execute();
        ImpquesAdapter impquesAdapter = new ImpquesAdapter(getActivity(), ImpquesList);
        listView.setAdapter(impquesAdapter);

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
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
                    Toast.makeText(getContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                    Log.d("timetable", object.toString());
                    refreshList(object.getJSONArray("pfiles"));
                } else
                    Log.d("timetable", object.toString());

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
                Log.d("timetable", url);
                String getstring = requestHandler.sendGetRequest(url);

                return requestHandler.sendGetRequest(url);
            }
            return null;
        }
    }

    private void refreshList(JSONArray pfiles) throws JSONException {
        ImpquesList.clear();
        for (int i = 0; i < pfiles.length(); i++) {
            JSONObject obj = pfiles.getJSONObject(i);
            ImpquesList.add(new Impques(obj.getInt("id"), obj.getString("name")));
            Log.d("impques", new Impques(obj.getInt("id"), obj.getString("name")).toString());
        }


        ImpquesAdapter timetableAdapter = new ImpquesAdapter(getActivity(), ImpquesList);
        listView.setAdapter(timetableAdapter);
    }

    class ImpquesAdapter extends ArrayAdapter<Impques> {

        List<Impques> impquesList;

        public ImpquesAdapter(Activity context, @NonNull List<Impques> impquesList) {
            super(context, R.layout.layout_list, impquesList);

            this.impquesList = impquesList;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            listViewItem = inflater.inflate(R.layout.layout_list, null, true);

            TextView textViewName = listViewItem.findViewById(R.id.textViewName);

//
            final Impques impques = impquesList.get(position);
//
//            textViewName.setText(impques.getName());
//            textViewName.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    Toast.makeText(getContext(),"Download in progress",Toast.LENGTH_SHORT).show();
//                    String url = "https://rejinpaulnetwork.com/rejinpaulapp/timetable/" + impques.getName() + ".pdf";
//                    Log.d("UGNewURL", url);
//                    new DownloadFile().execute(url);
//                }
//
//                class DownloadFile extends AsyncTask<String, String, String> {
//                    @Override
//                    protected void onPreExecute() {
//                        super.onPreExecute();
//                        p.setIndeterminate(false);
//                        p.setVisibility(View.VISIBLE);
//                    }
//
//                    @Override
//                    protected String doInBackground(String... f_url) {
//                        int count;
//                        try {
//                            URL url = new URL(f_url[0]);
//                            URLConnection connection = url.openConnection();
//                            connection.connect();
//                            int lengthOfFile = connection.getContentLength();
//
//
//                            InputStream input = connection.getInputStream();
//                            Log.d("dwnld url name", url.toString());
//
//                            String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
//
//                            fileName = f_url[0].substring(f_url[0].lastIndexOf('/') + 1, f_url[0].length());
//                            Log.d("dwnld url fname", fileName);
//                            folder = Environment.DIRECTORY_DOWNLOADS + File.separator;
//                            Log.d("UGnew folder", folder);
//                            File directory = new File(folder);
//
//                            if (!directory.exists()) {
//                                directory.mkdirs();
//                            }
//
//                            OutputStream output = new FileOutputStream(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + impques.getName() + ".pdf");
//                            Log.d("Check folder path", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + impques.getName() + ".pdf");
//                            byte data[] = new byte[1024];
//
//                            long total = 0;
//
//                            while ((count = input.read(data)) != -1) {
//                                total += count;
//                                Log.d("UGNew fragment", "Check");
//                                output.write(data, 0, count);
//                            }
//                            output.flush();
//
//                            output.close();
//                            input.close();
//                            Log.d("UGNew", "Downloaded at: " + folder + fileName);
//                            return "Downloaded at: " + folder + fileName;
//
//                        } catch (Exception e) {
//                            Log.e("Error: ", e.getMessage());
//                        }
//
//
//                        return "Something went wrong";
//                    }
//
//
//                    @Override
//                    protected void onPostExecute(String message) {
//                        super.onPostExecute(message);
//                        p.setVisibility(View.GONE);
//                        Toast.makeText(getContext(),"File downloaded in File Manager/"+folder+fileName,Toast.LENGTH_SHORT).show();
//                        File open = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + impques.getName() + ".pdf");
//                        Uri fileURI = FileProvider.getUriForFile(getContext(),
//                                BuildConfig.APPLICATION_ID + ".provider",
//                                open);
//                        Intent i = new Intent();
//                        i.setAction(android.content.Intent.ACTION_VIEW);
//                        i.setDataAndType(fileURI, "application/pdf");
//                        i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//
//                        startActivity(i);
//
//                    }
//                }
//            });
//
            ImageView fileShare = listViewItem.findViewById(R.id.imgShare);
            ImageView fileDownload = listViewItem.findViewById(R.id.imgDown);

//            final Material material = materialList.get(position);

            textViewName.setText(impques.getName());

            fileShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(getContext(),"Clicked download", Toast.LENGTH_LONG).show();


//                    String url = "http://mindvoice.info/rpweb/questionbank/" + material.getName() + ".pdf";
                    String url = "https://rejinpaulnetwork.com/rejinpaulapp/impquestion/" + impques.getName() + ".pdf";
                    Log.d("Questions fragment", url);
                    new DownloadFile().execute(url);


                }

                class DownloadFile extends AsyncTask<String, String, String> {
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
//                        p = new ProgressBar(QuestionsBankActivity.this);
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

                            OutputStream output = new FileOutputStream(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + impques.getName() + ".pdf");
                            Log.d("Check folder path", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + impques.getName() + ".pdf");
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
                                    (Environment.DIRECTORY_DOWNLOADS), impques.getName() + ".pdf");
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


                    String url = "https://rejinpaulnetwork.com/rejinpaulapp/impquestion/"+impques.getName()+".pdf";
//                    String url = "http://mindvoice.info/rpweb/questionbank/" + material.getName() + ".pdf";
                    Log.d("Materials fragment", url);
                    File outputFile = new File(Environment.getExternalStoragePublicDirectory
                            (Environment.DIRECTORY_DOWNLOADS), impques.getName() + ".pdf");

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
//                        p = new ProgressBar(QuestionsBankActivity.this);
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

                            OutputStream output = new FileOutputStream(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + impques.getName() + ".pdf");
                            Log.d("Check folder path", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + impques.getName() + ".pdf");
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
                        Toast.makeText(getContext(),"File downloaded in File Manager/"+folder+fileName,Toast.LENGTH_SHORT).show();
                        File open = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + impques.getName() + ".pdf");
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
            return listViewItem;
        }
    }

    private class Impques {

        private int id;
        private String name;

        public Impques(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

    }


}
