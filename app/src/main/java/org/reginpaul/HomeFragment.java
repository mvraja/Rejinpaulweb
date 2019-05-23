package org.reginpaul;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeFragment extends android.support.v4.app.Fragment implements AdapterView.OnItemClickListener{
    String[] title = { "Syllabus","Study materials","Question papers","Events","Notification","Results" };
    int[] icon={R.drawable.syllabus,R.drawable.study,R.drawable.ic_question,R.drawable.friends,R.drawable.messages,R.drawable.result};
    GridView grid;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        grid = (GridView) rootView.findViewById(R.id.grid);
        CustomGrid adapter=new CustomGrid();
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(this);
        return rootView;

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {

            case 0:
                getFragmentManager().beginTransaction().replace(R.id.main_container, new SyllabusFragment()).commit();
                break;

            case 1:
                getFragmentManager().beginTransaction().replace(R.id.main_container, new MaterialsFragment()).commit();
                break;

            case 2:
                getFragmentManager().beginTransaction().replace(R.id.main_container, new QuestionsFragment()).commit();
                break;

            case 3:
//                getFragmentManager().beginTransaction().replace(R.id.main_container, new SyllabusFragment()).commit();
                break;

            case 4:
                getFragmentManager().beginTransaction().replace(R.id.main_container, new NotificationFragment()).commit();
                break;
            case 5:
                getFragmentManager().beginTransaction().replace(R.id.main_container, new ResultFragment()).commit();
                break;

        }
    }
    class CustomGrid extends BaseAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return 6;
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
            Viewholder holder = null;
            if (convertView == null) {
                holder = new Viewholder();
                convertView = inflater.inflate(R.layout.custom_list, parent,
                        false);
                holder.name = (TextView) convertView.findViewById(R.id.title);


                holder.image = (ImageView) convertView.findViewById(R.id.image);
                convertView.setTag(holder);
                convertView.setBackgroundResource(R.drawable.grid_items_border);

            } else {
                holder = (Viewholder) convertView.getTag();
            }
            holder.name.setText(title[position]);
            holder.image.setImageResource(icon[position]);


            return convertView;

        }

        class Viewholder {
            ImageView image;
            TextView name;

        }
    }


}