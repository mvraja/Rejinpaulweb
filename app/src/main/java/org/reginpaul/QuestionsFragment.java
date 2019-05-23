package org.reginpaul;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
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


public class QuestionsFragment extends Fragment implements AdapterView.OnItemClickListener{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;

    List<Material> materialList;
    ListView listView;
    private OnFragmentInteractionListener mListener;
    private View listViewItem;
    URL ImageUrl = null;
    InputStream is = null;
    Bitmap bmImg = null;
    ImageView imageView = null;
    ProgressDialog p;
    private String fileName;
    private String folder;
    private boolean isDownloaded;
    String sDept;
    String[] title = {"Electronics and communication Engineering", "Electrical and Electronics Engineering", "Computer Science and Engineering", "Information and Technology", "Electrical &Instrumentation Engineering", "CIVIL Engineering", "Mechanical Engineering", "BioTechnology"};
    //    int[] icon = {R.drawable.syllabus, R.drawable.study, R.drawable.ic_question, R.drawable.friends, R.drawable.messages, R.drawable.result, R.drawable.result, R.drawable.result};
    GridView grid;
    public QuestionsFragment() {

    }

    public static QuestionsFragment newInstance(String param1, String param2) {
        QuestionsFragment fragment = new QuestionsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View returnview = inflater.inflate(R.layout.fragment_questions, container, false);
        grid = (GridView) returnview.findViewById(R.id.grid);
        CustomGrid adapter = new CustomGrid();
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(this);

//        listView = returnview.findViewById(R.id.listView);
        materialList = new ArrayList<>();

        MainActivity mainActivity = (MainActivity)getActivity();
        sDept = mainActivity.getDept();
        String sDept1="\""+sDept+"\"";


        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_READ_QUESTIONS+sDept1, null, CODE_GET_REQUEST);
        request.execute();
        MaterialAdapter materialAdapter = new MaterialAdapter(materialList);
//        listView.setAdapter(materialAdapter);
        return returnview;
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    class CustomGrid extends BaseAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return 8;
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub

            LayoutInflater inflater = LayoutInflater
                    .from(getContext());
            CustomGrid.Viewholder holder = null;
            if (convertView == null) {
                holder = new CustomGrid.Viewholder();
                convertView = inflater.inflate(R.layout.custom_list_dept, parent,
                        false);
                holder.name = (TextView) convertView.findViewById(R.id.title);


//                holder.image = (ImageView) convertView.findViewById(R.id.image);
                convertView.setTag(holder);

            } else {
                holder = (CustomGrid.Viewholder) convertView.getTag();
            }
            holder.name.setText(title[position]);
//            holder.image.setImageResource(icon[position]);


            return convertView;

        }

        class Viewholder {
            //            ImageView image;
            TextView name;

        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MaterialAdapter materialAdapter = new MaterialAdapter(materialList);
//        listView.setAdapter(materialAdapter);
    }

    /*private void readMaterial(String categ) {
        SyllabusFragment.PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_READ_MSG, null, CODE_GET_REQUEST);
        request.execute();
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
                    Toast.makeText(getContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
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

        public MaterialAdapter(@NonNull List<Material> materialList) {
            super(QuestionsFragment.this.getContext(), R.layout.layout_list, materialList);
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


                    String url = "http://mindvoice.info/rpweb/question/" + material.getName() + ".pdf";
                    Log.d("Questions fragment", url);
                    new DownloadFile().execute(url);


                }

                class DownloadFile extends AsyncTask<String, String, String> {
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        p = new ProgressDialog(QuestionsFragment.this.getContext());
                        p.setMessage("Please wait...It is downloading");
                        p.setIndeterminate(false);
                        p.setCancelable(false);
                        p.show();
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
                                Log.d("Questions fragmnet", "Check");
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

                            QuestionsFragment.this.startActivity(share);

                            return "Downloaded at: " + folder + fileName;

                        } catch (Exception e) {
                            Log.e("Error: ", e.getMessage());
                        }


                        return "Something went wrong";
                    }


                    @Override
                    protected void onPostExecute(String message) {
                        super.onPostExecute(message);

                        p.hide();

                    }
                }
            });
            fileDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(getContext(),"Clicked download", Toast.LENGTH_LONG).show();


                    String url = "http://mindvoice.info/rpweb/question/" + material.getName() + ".pdf";
                    Log.d("Materials fragment", url);
                    new DownloadFile().execute(url);


                }

                class DownloadFile extends AsyncTask<String, String, String> {
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        p = new ProgressDialog(QuestionsFragment.this.getContext());
                        p.setMessage("Please wait...It is downloading");
                        p.setIndeterminate(false);
                        p.setCancelable(false);
                        p.show();
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

                        p.hide();

                    }
                }
            });

            return listViewItem;
        }
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

}
