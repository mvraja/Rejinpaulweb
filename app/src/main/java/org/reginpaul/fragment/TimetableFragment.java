package org.reginpaul.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.webkit.WebViewClient;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.reginpaul.MainActivity;
import org.reginpaul.R;
import org.reginpaul.RequestHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class TimetableFragment extends Fragment {

    private WebView webView;
    String sDept;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    public TimetableFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View returnView = inflater.inflate(R.layout.fragment_timetable, container, false);

        MainActivity mainActivity = (MainActivity) getActivity();
        sDept = mainActivity.getDept();

        //    PerformNetworkRequest request = new PerformNetworkRequest("http://mindvoice.info/rpweb/v1/Api.php?apicall=gettimetable&category="+sDept, null, CODE_GET_REQUEST);
        //  request.execute();

//        webView = returnView.findViewById(R.id.webfile);
//        webView.setWebViewClient(new inlineBrowser());
//        webView.getSettings().setLoadWithOverviewMode(true);
//        webView.getSettings().setUseWideViewPort(true);
//        webView.getSettings().setLoadsImagesAutomatically(true);
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
//
//        if (sDept.equalsIgnoreCase("anna university")) {
//            webView.loadUrl("http://mindvoice.info/rpweb/timetable/AU.pdf");
//        }
//        if (sDept.equalsIgnoreCase("jntu")){
//            webView.loadUrl("http://mindvoice.info/rpweb/timetable/JNTU.pdf");
//        }
//
//        if (sDept.equalsIgnoreCase("school board")){
//            webView.loadUrl("http://mindvoice.info/rpweb/timetable/SCHOOL.pdf");
//        }
//
//        if (sDept.equalsIgnoreCase("competitive exams")){
//            webView.loadUrl("http://mindvoice.info/rpweb/timetable/COMP.pdf");
//        }

        viewPager = returnView.findViewById(R.id.viewpager);
        tabLayout = returnView.findViewById(R.id.tabs);

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
        return returnView;
    }
    private void setupViewPagerAU(ViewPager viewPager) {
        Log.d("Syllabus setup","EntertedAU");
        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());

        adapter.addFragment(new UgnewtimeFragment(), "UG 2017");
        adapter.addFragment(new PgnewtimeFragment(), "PG 2017");
        adapter.addFragment(new UgoldtimeFragment(), "UG 2013");
        adapter.addFragment(new PgoldtimeFragment(), "PG 2013");

        viewPager.setAdapter(adapter);

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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
    }



    private class inlineBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
