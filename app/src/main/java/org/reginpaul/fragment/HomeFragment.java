package org.reginpaul.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.reginpaul.NotificationActivity;
import org.reginpaul.R;

public class HomeFragment extends Fragment implements AdapterView.OnItemClickListener{

    String[] title = { "Syllabus","Study materials", "Profile", "Events","Notification","Results" };
    int[] icon={R.drawable.syllabus,R.drawable.study, R.drawable.profile_home, R.drawable.friends,R.drawable.messages,R.drawable.result};
    GridView grid;
    AppCompatActivity activity;
    ActionBar actionBar;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        grid = rootView.findViewById(R.id.grid_home);
        progressBar = rootView.findViewById(R.id.progress_bar);
        CustomGrid adapter=new CustomGrid();
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(this);
        ApiLoad apiLoad = new ApiLoad();
        apiLoad.execute();
        return rootView;

    }

    @Override
    public void onResume() {
        super.onResume();
        activity = (AppCompatActivity) getActivity();
        actionBar = activity.getSupportActionBar();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {

            case 0:
                getFragmentManager().beginTransaction().replace(R.id.main_container, new SyllabusFragment()).commit();
                actionBar.setTitle("Syllabus");
                break;

            case 1:
                getFragmentManager().beginTransaction().replace(R.id.main_container, new MaterialsFragment()).commit();
                actionBar.setTitle("Study Materials");
                break;

            case 2:
                getFragmentManager().beginTransaction().replace(R.id.main_container, new ProfileFragment()).commit();
                actionBar.setTitle("Profile");
                break;


            case 3:
                getFragmentManager().beginTransaction().replace(R.id.main_container, new EventsRegFragment()).commit();
                actionBar.setTitle("Events");
                break;

            case 4:
                Intent intent = new Intent(getActivity(),NotificationActivity.class);
                startActivity(intent);
                break;

            case 5:
                getFragmentManager().beginTransaction().replace(R.id.main_container, new ResultFragment()).commit();
                actionBar.setTitle("Results");
                break;

        }
    }

    class CustomGrid extends BaseAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return title.length;
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            TextView name;
            ImageView image;

            LayoutInflater inflater = LayoutInflater.from(getContext());
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.custom_list, parent, false);

                name = convertView.findViewById(R.id.title);
                image = convertView.findViewById(R.id.image);

//                convertView.setBackgroundResource(R.drawable.grid_items_wborder);
                name.setText(title[position]);
                image.setImageResource(icon[position]);
            }
            return convertView;
        }
    }

    public class ApiLoad extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String[] params) {
            return null;
        }

        @Override
        protected void onPostExecute(String data) {
            super.onPostExecute(data);
            progressBar.setVisibility(View.GONE);
        }
    }


}