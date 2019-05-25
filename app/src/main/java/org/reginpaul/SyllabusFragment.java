package org.reginpaul;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Arrays;


public class SyllabusFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    ListView listView;
    private OnFragmentInteractionListener mListener;
    String sDept;
    String[] au_title = {"ECE", "EEE", "CSE", "IT", "EIE", "CIVIL", "MECH", "BIOTECH"};
    String[] jntu_title = {"ECE", "EEE", "CSE", "IT", "EIE", "CIVIL", "MECH", "BIOTECH"};
    String[] tn_title = {"HSC", "SSLC"};
    String[] ce_title = {"TNPSC", "UPSC", "Bank Exam", "Railways Exam", "Police Exam"};

    GridView grid;
    ArrayList department = new ArrayList<>();

    public SyllabusFragment() {

    }

    public static SyllabusFragment newInstance(String param1, String param2) {
        SyllabusFragment fragment = new SyllabusFragment();
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
        View returnview = inflater.inflate(R.layout.fragment_syllabus, container, false);
        grid = returnview.findViewById(R.id.grid_home);

        MainActivity mainActivity = (MainActivity) getActivity();
        sDept = mainActivity.getDept();

        final CustomGrid adapter;

        if (sDept.equalsIgnoreCase("anna university")) {
            department = new ArrayList<>(Arrays.asList(au_title));
        }
        if (sDept.equalsIgnoreCase("jntu")){
            department = new ArrayList<>(Arrays.asList(jntu_title));
        }
        if (sDept.equalsIgnoreCase("school board")){
            department = new ArrayList<>(Arrays.asList(tn_title));
        }
        if (sDept.equalsIgnoreCase("competitive exams")){
            department = new ArrayList<>(Arrays.asList(ce_title));
        }

        adapter = new CustomGrid(getActivity().getApplicationContext(),R.layout.custom_list_dept,department);
        grid.setAdapter(adapter);


        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                Intent intent = new Intent(getActivity(),SyllabusActivity.class);
                intent.putExtra("strtext",selectedItem);
                startActivity(intent);

            }
        });

        return returnview;
    }


    public class CustomGrid extends ArrayAdapter {

    ArrayList deptList;

    public CustomGrid(Context context, int textViewResourceId, ArrayList objects) {
        super(context, textViewResourceId, objects);
        deptList = objects;
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
                convertView.setBackgroundResource(R.drawable.grid_items_border);
                TextView name = convertView.findViewById(R.id.title);
                name.setText(deptList.get(position).toString());
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
