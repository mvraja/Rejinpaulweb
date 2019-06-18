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

public class PgoldFragment extends Fragment {

    WebView webView;
    String url;
    List<Syllabus> Syllabuslist;
    ListView listView;
    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;
    private View listViewItem;
    ProgressBar p;
    private String fileName,folder;

    public PgoldFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pgold, container, false);
//        webView = rootView.findViewById(R.id.pgweb1);
//
//        url = "http://mindvoice.info/rpweb/regulations/PG2013.pdf";
//        webView.setWebViewClient(new inlineBrowser());
//        webView.getSettings().setLoadWithOverviewMode(true);
//        webView.getSettings().setUseWideViewPort(true);
//        webView.getSettings().setLoadsImagesAutomatically(true);
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
//        webView.loadUrl("https://docs.google.com/gview?embedded=true&url="+url);
        listView = rootView.findViewById(R.id.listView);
        Syllabuslist = new ArrayList<>();
        PerformNetworkRequest request = new PerformNetworkRequest("http://mindvoice.info/rpweb/v1/Api.php?apicall=getsyllabus&year=%222013%22&course=%22PG%22", null, CODE_GET_REQUEST);
        request.execute();
        SyllabusAdapter syllabusAdapter = new SyllabusAdapter(getActivity(),Syllabuslist);
        listView.setAdapter(syllabusAdapter);

        return rootView;
    }

    private class inlineBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
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
        Syllabuslist.clear();
        for (int i = 0; i < pfiles.length(); i++) {
            JSONObject obj = pfiles.getJSONObject(i);
            Syllabuslist.add(new Syllabus(obj.getInt("id"), obj.getString("name")));
            Log.d("Syllabus", new Syllabus(obj.getInt("id"), obj.getString("name")).toString());
        }


        SyllabusAdapter syllabusAdapter = new SyllabusAdapter(getActivity(),Syllabuslist);
        listView.setAdapter(syllabusAdapter);
    }

    class SyllabusAdapter extends ArrayAdapter<Syllabus> {

        List<Syllabus> Syllabuslist;

        public SyllabusAdapter(Activity context, @NonNull List<Syllabus> Syllabuslist) {
            super(context, R.layout.layout_list_syllabus, Syllabuslist);

            this.Syllabuslist = Syllabuslist;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            listViewItem = inflater.inflate(R.layout.layout_list_syllabus, null, true);

            TextView textViewName = listViewItem.findViewById(R.id.textViewName);


            final Syllabus syllabus = Syllabuslist.get(position);

            textViewName.setText(syllabus.getName());
            textViewName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String url = "http://mindvoice.info/rpweb/regulations/PG2013/" + syllabus.getName() + ".pdf";
                    Log.d("UGNewURL", url);
                    new DownloadFile().execute(url);
                }

                class DownloadFile extends AsyncTask<String, String, String> {
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        p = new ProgressBar(getContext());
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

                            OutputStream output = new FileOutputStream(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + syllabus.getName() + ".pdf");
                            Log.d("Check folder path", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + syllabus.getName() + ".pdf");
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
                        File open = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + syllabus.getName() + ".pdf");
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

    private class Syllabus {

        private int id;
        private String name;

        public Syllabus(int id, String name) {
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

