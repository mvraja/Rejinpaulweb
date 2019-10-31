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
import android.webkit.WebView;

import org.reginpaul.MainActivity;
import org.reginpaul.R;

import java.util.ArrayList;
import java.util.List;

public class ImpquesFragment extends Fragment {
    private WebView webView;
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
        /*if (sDept.equalsIgnoreCase("anna university")) {
            setupViewPagerAU(viewPager);
        }

        if (sDept.equalsIgnoreCase("jntu")){
            setupViewPagerJNTU(viewPager);
        }
        if (sDept.equalsIgnoreCase("school board")){
            setupViewPagerSC(viewPager);
        }
        if (sDept.equalsIgnoreCase("competitive exams")){
            setupViewPagerCE(viewPager);
        }*/


        tabLayout.setupWithViewPager(viewPager);
        return returnView;
    }

    /*private void setupViewPagerJNTU(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());

        adapter.addFragment(new UgJntuFragment(), "JNTU UG");
        adapter.addFragment(new PgJntuFragment(), "JNTU PG");
        viewPager.setAdapter(adapter);

    }*/

    private void setupViewPagerall(ViewPager viewPager) {
        Log.d("Timetable setup","EntertedAU");
        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());

        adapter.addFragment(new AUimpquesFragment(), "ANNA UNIV");
        adapter.addFragment(new PgnewtimeFragment(), "JNTU");
        adapter.addFragment(new UgoldtimeFragment(), "SCHOOL");
        adapter.addFragment(new PgoldtimeFragment(), "TNPSC");

        viewPager.setAdapter(adapter);

    }
    /*private void setupViewPagerSC(ViewPager viewPager) {
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
    }*/

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


/*
    private class inlineBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }*/
}


