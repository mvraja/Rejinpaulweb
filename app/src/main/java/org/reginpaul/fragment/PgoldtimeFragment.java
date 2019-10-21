package org.reginpaul.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.reginpaul.MainActivity;
import org.reginpaul.R;

public class PgoldtimeFragment extends Fragment {
    private WebView webView;
    String sDept;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    public PgoldtimeFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View returnView = inflater.inflate(R.layout.fragment_ugnewtime, container, false);

        MainActivity mainActivity = (MainActivity) getActivity();
        sDept = mainActivity.getDept();

//        PerformNetworkRequest request = new PerformNetworkRequest("http://mindvoice.info/rpweb/v1/Api.php?apicall=gettimetable&category=" + sDept, null, CODE_GET_REQUEST);
//        request.execute();

        webView = returnView.findViewById(R.id.webfile);
        webView.setWebViewClient(new inlineBrowser());
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        if (sDept.equalsIgnoreCase("anna university")) {
            webView.loadUrl("https://rejinpaul.com/rejinpaulapp/pdf/12.pdf");
        }
//        if (sDept.equalsIgnoreCase("jntu")) {
//            webView.loadUrl("http://mindvoice.info/rpweb/timetable/JNTU.pdf");
//        }
//
//        if (sDept.equalsIgnoreCase("school board")) {
//            webView.loadUrl("http://mindvoice.info/rpweb/timetable/SCHOOL.pdf");
//        }
//
//        if (sDept.equalsIgnoreCase("competitive exams")) {
//            webView.loadUrl("http://mindvoice.info/rpweb/timetable/COMP.pdf");
//        }
        return returnView;
    }
    private class inlineBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

}

