package org.reginpaul.fragment;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_about_us, container, false);

        int image[]={R.drawable.ad1,R.drawable.ad2,R.drawable.ad3,R.drawable.ad4,R.drawable.ad5};
        v_flipper=rootView.findViewById(R.id.v_flipper);
        for(int images : image){
            flipperImages(images);
        }

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