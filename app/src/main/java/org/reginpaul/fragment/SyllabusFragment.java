package org.reginpaul.fragment;

import android.content.Context;
import android.net.Uri;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.reginpaul.MainActivity;
import org.reginpaul.R;

import java.util.ArrayList;
import java.util.List;


public class SyllabusFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    ListView listView;
    private OnFragmentInteractionListener mListener;
    String sDept;

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

        MainActivity mainActivity = (MainActivity) getActivity();
        sDept = mainActivity.getDept();

        viewPager = returnview.findViewById(R.id.viewpager);
        tabLayout = returnview.findViewById(R.id.tabs);

        if (sDept.equalsIgnoreCase("anna university")||sDept.equalsIgnoreCase("jntu")) {
            setupViewPagerAU(viewPager);
        }

        if (sDept.equalsIgnoreCase("school board")){
            setupViewPagerSC(viewPager);
        }
        if (sDept.equalsIgnoreCase("competitive exams")){
            setupViewPagerCE(viewPager);
        }

        tabLayout.setupWithViewPager(viewPager);

        return returnview;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void setupViewPagerAU(ViewPager viewPager) {
        Log.d("Syllabus setup","EntertedAU");
        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());

        adapter.addFragment(new UgnewFragment(), "UG 2017");
        adapter.addFragment(new PgnewFragment(), "PG 2017");
        adapter.addFragment(new UgoldFragment(), "UG 2013");
        adapter.addFragment(new PgoldFragment(), "PG 2013");

        viewPager.setAdapter(adapter);



    }


    private void setupViewPagerAU1(ViewPager viewPager) {
        Log.d("Syllabus setup","EntertedAU1");
        ViewPagerAdapter adapter1 = new ViewPagerAdapter(getFragmentManager());
        viewPager.getAdapter().notifyDataSetChanged();
        adapter1.addFragment(new UgoldFragment(), "UG");
        adapter1.addFragment(new PgoldFragment(), "PG");
        viewPager.setAdapter(adapter1);

    }


    private void setupViewPagerSC(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());
        adapter.addFragment(new XFragment(), "Class 10");
        adapter.addFragment(new XIFragment(), "Class 11");
        adapter.addFragment(new XIIFragment(), "Class 12");
        viewPager.setAdapter(adapter);
    }

    private void setupViewPagerCE(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());
        adapter.addFragment(new TnFragment(), "TNPSC");
        adapter.addFragment(new RrbFragment(), "RRB");
        adapter.addFragment(new BankFragment(), "BANK");
        adapter.addFragment(new UpscFragment(), "UPSC");
        viewPager.setAdapter(adapter);
    }


    public class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }


        public void addFragment(Fragment fragment, String title) {

            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
        private String getFragmentTag(int pos){
            return "android:switcher:"+R.id.viewpager+":"+pos;
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
