package org.reginpaul.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.reginpaul.MainActivity;
import org.reginpaul.R;

import java.util.ArrayList;
import java.util.List;

public class ImpquesFragment extends Fragment {
    String sDept;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    public ImpquesFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View returnView = inflater.inflate(R.layout.fragment_impques, container, false);

        MainActivity mainActivity = (MainActivity) getActivity();
        sDept = mainActivity.getDept();

        viewPager = returnView.findViewById(R.id.viewpager);
        tabLayout = returnView.findViewById(R.id.tabs);

        setupViewPagerall(viewPager);


        tabLayout.setupWithViewPager(viewPager);
        return returnView;
    }


    private void setupViewPagerall(ViewPager viewPager) {
        Log.d("Timetable setup","EntertedAU");
        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());

        adapter.addFragment(new AUimpquesFragment(), "ANNA UNIV");
        adapter.addFragment(new JNTUimpquesFragment(), "JNTU");
        adapter.addFragment(new SCHOOLimpquesFragment(), "SCHOOL");
        adapter.addFragment(new TNPSCimpquesFragment(), "TNPSC");

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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
    }


}


