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

        webView = returnView.findViewById(R.id.webfile);
        webView.setWebViewClient(new inlineBrowser());
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        if (sDept.equalsIgnoreCase("anna university")) {
            webView.loadUrl("http://mindvoice.info/rpweb/timetable/AU.pdf");
        }
        if (sDept.equalsIgnoreCase("jntu")){
            webView.loadUrl("http://mindvoice.info/rpweb/timetable/JNTU.pdf");
        }

        if (sDept.equalsIgnoreCase("school board")){
            webView.loadUrl("http://mindvoice.info/rpweb/timetable/SCHOOL.pdf");
        }

        if (sDept.equalsIgnoreCase("competitive exams")){
            webView.loadUrl("http://mindvoice.info/rpweb/timetable/COMP.pdf");
        }

        return returnView;
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
