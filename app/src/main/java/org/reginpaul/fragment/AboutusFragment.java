package org.reginpaul.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ViewFlipper;
import org.reginpaul.R;

public class AboutusFragment extends Fragment {

    private ViewFlipper v_flipper;
    private ImageView Mail,Phone,Rate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_about_us, container, false);

        int image[]={R.drawable.ad1,R.drawable.ad2,R.drawable.ad3,R.drawable.ad4,R.drawable.ad5};
        v_flipper=rootView.findViewById(R.id.v_flipper);
        Mail=(ImageView) rootView.findViewById(R.id.mail);
        Phone=(ImageView) rootView.findViewById(R.id.ph);
        Rate=(ImageView) rootView.findViewById(R.id.rate);
        for(int images : image){
            flipperImages(images);
        }
        Mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (Intent.ACTION_VIEW , Uri.parse("mailto:" + "rejinpaul@rejinpaul.com"));
                intent.putExtra(Intent.EXTRA_SUBJECT, "Queries to Rejinpaul");
//                intent.putExtra(Intent.EXTRA_TEXT, "your_text");
                startActivity(intent);
            }
        });
        Phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:1234567890"));
                startActivity(callIntent);
            }
        });
Rate.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        final String appPackageName = getActivity().getPackageName(); // getPackageName() from Context or Activity object
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }
});
        return rootView;
    }

    public void flipperImages(int image){
        ImageView imageView=new ImageView(getContext());
        imageView.setBackgroundResource(image);
        v_flipper.addView(imageView);
        v_flipper.setFlipInterval(5000);
        v_flipper.setAutoStart(true);
        v_flipper.setInAnimation(getContext(),android.R.anim.slide_in_left);
        v_flipper.setOutAnimation(getContext(),android.R.anim.slide_out_right);
    }
}