package org.reginpaul.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
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
import org.reginpaul.Api;
import org.reginpaul.BuildConfig;
import org.reginpaul.MainActivity;
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

public class UgoldtimeFragment extends Fragment {
//    private WebView webView;
//    String sDept;
//    private TabLayout tabLayout;
//    private ViewPager viewPager;
//
//    public UgoldtimeFragment() {
//
//    }

    List<Timetable> timetableList;
    ListView listView;
    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;
    private View listViewItem;
    ProgressBar p;
    private String fileName,folder;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        View returnView = inflater.inflate(R.layout.fragment_ugnewtime, container, false);
//
//        MainActivity mainActivity = (MainActivity) getActivity();
//        sDept = mainActivity.getDept();
//
////        PerformNetworkRequest request = new PerformNetworkRequest("http://mindvoice.info/rpweb/v1/Api.php?apicall=gettimetable&category=" + sDept, null, CODE_GET_REQUEST);
////        request.execute();
//
//
//        webView = returnView.findViewById(R.id.webfile);
//        webView.setWebViewClient(new inlineBrowser());
//        webView.getSettings().setLoadWithOverviewMode(true);
//        webView.getSettings().setUseWideViewPort(true);
//        webView.getSettings().setLoadsImagesAutomatically(true);
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
//        webView.loadUrl("https://rejinpaulnetwork.com/rejinpaulapp/soon.pdf");
//
//     /*   if (sDept.equalsIgnoreCase("anna university")) {
//            webView.loadUrl("https://rejinpaulnetwork.com/rejinpaulapp/timetable/aucr2013.pdf");
//        }
//        if (sDept.equalsIgnoreCase("jntu")) {
//            webView.loadUrl("http://mindvoice.info/rpweb/soon.pdf");
//        }
//
//        if (sDept.equalsIgnoreCase("school board")) {
//            webView.loadUrl("http://mindvoice.info/rpweb/soon.pdf");
//        }
//
//        if (sDept.equalsIgnoreCase("competitive exams")) {
//            webView.loadUrl("http://mindvoice.info/rpweb/soon.pdf");
//        }*/
//     return returnView;
//    }
//    private class inlineBrowser extends WebViewClient {
//        @Override
//        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            view.loadUrl(url);
//            return true;
//        }
        View rootView = inflater.inflate(R.layout.fragment_ugnew, container, false);
        String sDept1 = "%22school%22";
        listView = rootView.findViewById(R.id.listView);
        p = rootView.findViewById(R.id.pbar);
        timetableList = new ArrayList<>();
        //PerformNetworkRequest request = new PerformNetworkRequest("https://rejinpaulnetwork.com/rejinpaulapp/v1/Api.php?apicall=gettimetable&category=%22au%22", null, CODE_GET_REQUEST);
        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_READ_TIMETABLE + sDept1, null, CODE_GET_REQUEST);
        request.execute();
        TimetableAdapter timetableAdapter = new TimetableAdapter(getActivity(), timetableList);
        listView.setAdapter(timetableAdapter);

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

    private class inlineBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
        /*public void onPageFinished(WebView view, String url){
            progressBar.setVisibility(View.GONE);
        }*/
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
        timetableList.clear();
        for (int i = 0; i < pfiles.length(); i++) {
            JSONObject obj = pfiles.getJSONObject(i);
            timetableList.add(new Timetable(obj.getInt("id"), obj.getString("name")));
            Log.d("timetable", new Timetable(obj.getInt("id"), obj.getString("name")).toString());
        }


        TimetableAdapter timetableAdapter = new TimetableAdapter(getActivity(), timetableList);
        listView.setAdapter(timetableAdapter);
    }

    class TimetableAdapter extends ArrayAdapter<Timetable> {

        List<Timetable> timetableList;

        public TimetableAdapter(Activity context, @NonNull List<Timetable> timetableList) {
            super(context, R.layout.layout_list_syllabus, timetableList);

            this.timetableList = timetableList;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            listViewItem = inflater.inflate(R.layout.layout_list_syllabus, null, true);

            TextView textViewName = listViewItem.findViewById(R.id.textViewName);


            final Timetable timetable = timetableList.get(position);

            textViewName.setText(timetable.getName());
            textViewName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Toast.makeText(getContext(),"Download in progress",Toast.LENGTH_SHORT).show();
                    String url = "https://rejinpaulnetwork.com/rejinpaulapp/timetable/" + timetable.getName() + ".pdf";
                    Log.d("UGNewURL", url);
                    new DownloadFile().execute(url);
                }

                class DownloadFile extends AsyncTask<String, String, String> {
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
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
                            Log.d("UGnew folder", folder);
                            File directory = new File(folder);

                            if (!directory.exists()) {
                                directory.mkdirs();
                            }

                            OutputStream output = new FileOutputStream(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + timetable.getName() + ".pdf");
                            Log.d("Check folder path", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + timetable.getName() + ".pdf");
                            byte data[] = new byte[1024];

                            long total = 0;

                            while ((count = input.read(data)) != -1) {
                                total += count;
                                Log.d("UGNew fragment", "Check");
                                output.write(data, 0, count);
                            }
                            output.flush();

                            output.close();
                            input.close();
                            Log.d("UGNew", "Downloaded at: " + folder + fileName);
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
                        File open = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + timetable.getName() + ".pdf");
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

    private class Timetable {

        private int id;
        private String name;

        public Timetable(int id, String name) {
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

