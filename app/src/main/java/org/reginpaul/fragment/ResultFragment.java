package org.reginpaul.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import org.reginpaul.R;

public class ResultFragment extends Fragment implements AdapterView.OnItemClickListener {

    String[] title = {"Anna University", "JNTU", "TN SSLC", "TN HSC"};
    int[] icon = {R.drawable.au, R.drawable.jntu, R.drawable.tn, R.drawable.tn};
    GridView grid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_result, container, false);
        grid = rootView.findViewById(R.id.grid_home);
        CustomGrid adapter = new CustomGrid();
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(this);

        return rootView;

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
       /* switch (position) {
            case 0:
                getFragmentManager().beginTransaction().replace(R.id.main_container, new ShowResultFragment()).commit();
                break;
        }*/
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
                convertView.setBackgroundResource(R.drawable.grid_items_border);
                name.setText(title[position]);
                image.setImageResource(icon[position]);
            }

            return convertView;

        }

    }


}