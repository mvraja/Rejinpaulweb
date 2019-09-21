package org.reginpaul;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.InputStream;
import java.util.List;

import uk.co.senab.photoview.PhotoViewAttacher;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder>  {


    private Context mCtx;
    private List<RegEvent> regEventList;
    ScaleGestureDetector SGD;
    private float scale = 1f;
    private ImageView image;


    public EventAdapter(Context mCtx, List<RegEvent> regEventList) {
        this.mCtx = mCtx;
        this.regEventList = regEventList;
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.custom_list_event, null);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        RegEvent product = regEventList.get(position);


        byte [] encodeByte=Base64.decode(product.getImage(), Base64.DEFAULT);
        Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);

        holder.imageView.setImageBitmap(bitmap);
        holder.textViewTitle.setText(product.getTitle());
        holder.textViewShortDesc.setText(product.getType());
        holder.textViewRating.setText(product.getDate());
        holder.textViewPrice.setText(product.getLoc());

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadImage(v);
            }
        });
    }

    private void loadImage(View v) {
        ImageView tempImageView = (ImageView)v;

        View layout = LayoutInflater.from(mCtx).inflate(R.layout.custom_image,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(mCtx,android.R.style.Theme_Light);
        builder.setView(layout);

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();


         image = layout.findViewById(R.id.fullimage);
        image.setImageDrawable(tempImageView.getDrawable());
//        ImageButton btnzIn = (ImageButton) layout.findViewById(R.id.btnZoomIn);
//        ImageButton btnzOut = (ImageButton) layout.findViewById(R.id.btnZoomOut);
//
//        btnzIn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Animation animZoomIn = AnimationUtils.loadAnimation(mCtx,R.anim.zoomin);
//                image.startAnimation(animZoomIn);
//            }
//        });
//        btnzOut.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Animation animZoomOut = AnimationUtils.loadAnimation(mCtx,R.anim.zoomout);
//                image.startAnimation(animZoomOut);
//            }
//        });
        PhotoViewAttacher pAttacher;
        pAttacher = new PhotoViewAttacher(image);
        pAttacher.update();

        Button ok = layout.findViewById(R.id.btnOk);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }



    @Override
    public int getItemCount() {
        return regEventList.size();
    }


    class EventViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle, textViewShortDesc, textViewRating, textViewPrice;
        ImageView imageView;

        public EventViewHolder(View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewShortDesc = itemView.findViewById(R.id.textViewType);
            textViewRating = itemView.findViewById(R.id.textViewDate);
            textViewPrice = itemView.findViewById(R.id.textViewLoc);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }

}