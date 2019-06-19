package org.reginpaul.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import org.reginpaul.R;
import org.reginpaul.ResultActivity;

import java.util.ArrayList;
import java.util.Arrays;

public class ResultFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    AppCompatActivity activity;
    ActionBar actionBar;

    String[] title = {"Anna University", "JNTU", "TN SSLC", "TN HSC"};
    Integer[] icon = {R.drawable.ic_au, R.drawable.ic_jntu, R.drawable.ic_tn, R.drawable.ic_tn};
    GridView grid;
    String selectedItem, sItem;
    Intent intent;

    ArrayList clg = new ArrayList<>();
    ArrayList images = new ArrayList<Integer>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        activity = (AppCompatActivity) getActivity();
        actionBar = activity.getSupportActionBar();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_result, container, false);
        grid = rootView.findViewById(R.id.grid_home);
        final CustomGrid adapter;

        clg = new ArrayList<>(Arrays.asList(title));
        images = new ArrayList<>(Arrays.asList(icon));

        adapter = new CustomGrid(getActivity().getApplicationContext(), R.layout.custom_list_dept, clg, images);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = parent.getItemAtPosition(position).toString();
                if (selectedItem.equalsIgnoreCase("anna university")){
                    sItem = "au";
                }
                if (selectedItem.equalsIgnoreCase("jntu")){
                    sItem = "jntu";
                }
                if (selectedItem.equalsIgnoreCase("tn hsc")){
                    sItem = "hsc";
                }
                if (selectedItem.equalsIgnoreCase("tn sslc")){
                    sItem = "sslc";
                }
                intent = new Intent(getActivity(), ResultActivity.class);
                intent.putExtra("stRes",sItem);
                startActivity(intent);
            }
        });

        return rootView;

    }

       /* switch (position) {
            case 0:
                getFragmentManager().beginTransaction().replace(R.id.main_container, new ShowResultFragment()).commit();
                break;
        }*/

    class CustomGrid extends ArrayAdapter {

        ArrayList clgList;
        ArrayList<Integer> imgList;

        private CustomGrid(Context context, int textViewResourceId, ArrayList objects, ArrayList<Integer> img) {
            super(context, textViewResourceId, objects);
            clgList = objects;
            imgList = img;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return title.length;
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
                convertView.setBackgroundResource(R.drawable.grid_items_border);
                name.setText(title[position]);
                image.setImageResource(icon[position]);
            }

            return convertView;

        }

    }


}