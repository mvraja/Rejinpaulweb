package org.reginpaul;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ViewFlipper;

public class AboutusActivity extends AppCompatActivity {

    private ViewFlipper v_flipper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);
        int image[]={R.drawable.ads1,R.drawable.ads2,R.drawable.ads3,R.drawable.ads4,R.drawable.ads5};
        v_flipper=findViewById(R.id.v_flipper);
        for(int images : image){
            FlipperImages(images);
        }

    }
    public void FlipperImages(int image){
        ImageView imageView=new ImageView(this);
        imageView.setBackgroundResource(image);
        v_flipper.addView(imageView);
        v_flipper.setFlipInterval(5000);
        v_flipper.setAutoStart(true);
        v_flipper.setInAnimation(this,android.R.anim.slide_in_left);
        v_flipper.setOutAnimation(this,android.R.anim.slide_out_right);



    }
}
