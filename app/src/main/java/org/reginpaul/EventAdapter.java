package org.reginpaul;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.InputStream;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {


    private Context mCtx;
    private List<RegEvent> regEventList;

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

        //loading the image Glide.with(mCtx).load(product.getImage()).into(holder.imageView);
        byte [] encodeByte=Base64.decode(product.getImage(), Base64.DEFAULT);
        Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);

        holder.imageView.setImageBitmap(bitmap);
        holder.textViewTitle.setText(product.getTitle());
        holder.textViewShortDesc.setText(product.getType());
        holder.textViewRating.setText(product.getDate());
        holder.textViewPrice.setText(product.getLoc());
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