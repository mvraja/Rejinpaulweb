package org.reginpaul;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class NotificationFragment extends Fragment {

String messagetext;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_notification, container, false);
//        TextView notify_txt=rootView.findViewById(R.id.notification_txt);
//        Intent intent = getActivity().getIntent();
//
//        Bundle b = getActivity().getIntent().getExtras();

//         messagetext = b.getString("FCM_PARM", "");
//         notify_txt.setText(messagetext);
        return rootView;

    }


}
