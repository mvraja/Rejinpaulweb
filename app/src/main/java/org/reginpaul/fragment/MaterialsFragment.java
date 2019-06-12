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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.reginpaul.MainActivity;

import org.reginpaul.R;

import java.util.ArrayList;
import java.util.Arrays;


public class MaterialsFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    ListView listView;
    private OnFragmentInteractionListener mListener;
    String sDept, tempSem, tempCourse, sem[], selectedItem, course[];
    String[] au_title = {"ECE", "EEE", "CSE", "IT", "AUTO", "CIVIL", "MECH", "BIOTECH", "MCA", "MBA"};

    String[] tn_title = {"Class X", "Class XI", "Class XII"};
    String[] ce_title = {"TNPSC", "BANK", "RRB", "POLICE"};

    Integer[] au_icon = {R.drawable.ece, R.drawable.eee, R.drawable.cse, R.drawable.it, R.drawable.automobile, R.drawable.civil, R.drawable.mechanical, R.drawable.biotech, R.drawable.mca, R.drawable.mba};
    Integer[] tn_icon = {R.drawable.rx, R.drawable.rxi, R.drawable.rxii};
    Integer[] ce_icon = {R.drawable.tnp, R.drawable.bank, R.drawable.rail, R.drawable.rail};

    GridView grid;
    ArrayList department = new ArrayList<>();
    ArrayList images = new ArrayList<Integer>();

    private Spinner semSpinner, courseSpinner;
    ArrayAdapter courseArray, semArray;

    AppCompatActivity activity;
    ActionBar actionBar;

    public MaterialsFragment() {
    }

    public static MaterialsFragment newInstance(String param1, String param2) {
        MaterialsFragment fragment = new MaterialsFragment();
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
    public void onResume() {
        super.onResume();
        activity = (AppCompatActivity) getActivity();
        actionBar = activity.getSupportActionBar();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View returnview = inflater.inflate(R.layout.fragment_materials, container, false);
        TextView t1 = returnview.findViewById(R.id.chooseCourse);
        TextView t2 = returnview.findViewById(R.id.chooseSem);
        grid = returnview.findViewById(R.id.grid_home);
        semSpinner = returnview.findViewById(R.id.setup_sem);
        courseSpinner = returnview.findViewById(R.id.setup_course);

        MainActivity mainActivity = (MainActivity) getActivity();
        sDept = mainActivity.getDept();

        final CustomGrid adapter;


        if (sDept.equalsIgnoreCase("anna university") || sDept.equalsIgnoreCase("jntu")) {
            department = new ArrayList<>(Arrays.asList(au_title));
            images = new ArrayList<>(Arrays.asList(au_icon));
        }
        if (sDept.equalsIgnoreCase("school board")) {
            t1.setVisibility(View.GONE);
            t2.setVisibility(View.GONE);
            semSpinner.setVisibility(View.GONE);
            courseSpinner.setVisibility(View.GONE);
            department = new ArrayList<>(Arrays.asList(tn_title));
            images = new ArrayList<>(Arrays.asList(tn_icon));
        }
        if (sDept.equalsIgnoreCase("competitive exams")) {
            t1.setVisibility(View.GONE);
            t2.setVisibility(View.GONE);
            semSpinner.setVisibility(View.GONE);
            courseSpinner.setVisibility(View.GONE);
            department = new ArrayList<>(Arrays.asList(ce_title));
            images = new ArrayList<>(Arrays.asList(ce_icon));
        }

        adapter = new CustomGrid(getActivity().getApplicationContext(), R.layout.custom_list_dept, department, images);
        grid.setAdapter(adapter);

        course = getActivity().getApplicationContext().getResources().getStringArray(R.array.temp_array1);
        courseArray = new ArrayAdapter(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, course);
        courseArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courseSpinner.setAdapter(courseArray);

        sem = getActivity().getApplicationContext().getResources().getStringArray(R.array.temp_array);
        semArray = new ArrayAdapter(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, sem);
        semArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        semSpinner.setAdapter(semArray);


        courseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tempCourse = course[position];//courseSpinner.getSelectedItem().toString();
                if (tempCourse.equalsIgnoreCase("ug")) {
                    sem = getActivity().getApplicationContext().getResources().getStringArray(R.array.ug);
                }
                if (tempCourse.equalsIgnoreCase("pg")) {
                    sem = getActivity().getApplicationContext().getResources().getStringArray(R.array.pg);
                }
                semArray = new ArrayAdapter(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, sem);
                semArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                semSpinner.setAdapter(semArray);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        semSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tempSem = sem[position];//semSpinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = parent.getItemAtPosition(position).toString();
                if (tempSem == null)
                    tempSem = "z";

                MaterialsTempFragment mf = new MaterialsTempFragment();
                Bundle bundle = new Bundle();
                bundle.putString("ctg",selectedItem);
                bundle.putString("sem",tempSem);
                bundle.putString("cour",tempCourse);
                mf.setArguments(bundle);

                getFragmentManager().beginTransaction().replace(R.id.main_container, mf).commit();
                actionBar.setTitle("Study Materials");
            }
        });

        /*grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                Intent intent = new Intent(getActivity(),MaterialsActivity.class);
                intent.putExtra("strtext",selectedItem);
                intent.putExtra("strSem", tempSem);
                startActivity(intent);

            }
        });*/

        return returnview;
    }



    public class CustomGrid extends ArrayAdapter {

        ArrayList deptList;
        ArrayList<Integer> imgList;

        private CustomGrid(Context context, int textViewResourceId, ArrayList objects, ArrayList<Integer> img) {
            super(context, textViewResourceId, objects);
            deptList = objects;
            imgList = img;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return super.getCount();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub

            LayoutInflater inflater = LayoutInflater.from(getContext());
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.custom_list_dept, parent, false);
//                convertView.setBackgroundResource(R.drawable.grid_items_wborder);
                TextView name = convertView.findViewById(R.id.title);
                ImageView image = convertView.findViewById(R.id.image);
                name.setText(deptList.get(position).toString());
                image.setImageResource(imgList.get(position));
            }
            return convertView;
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
