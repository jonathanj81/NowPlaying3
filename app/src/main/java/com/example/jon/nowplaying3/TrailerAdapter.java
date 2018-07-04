package com.example.jon.nowplaying3;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerAdapterViewHolder> {

    private final TrailerAdapterOnClickHandler mClickHandler;
    private Context mContext;
    private List<String> mTrailerKeys;
    private int mHeight;
    private final String BASE_YOUTUBE_FIRST_THUMB_PATH = "https://img.youtube.com/vi/";
    private final String BASE_YOUTUBE_FIRST_THUMB_SUFFIX = "/0.jpg";


    public interface TrailerAdapterOnClickHandler {
        void onClick(String trailerKey);
    }

    public TrailerAdapter(TrailerAdapterOnClickHandler handler){
        mClickHandler = handler;
    }

    @NonNull
    @Override
    public TrailerAdapter.TrailerAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        mContext = parent.getContext();
        View layoutView = LayoutInflater.from(mContext).inflate(R.layout.trailer_list_view, parent,false);
        layoutView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,mHeight/3));
        return new TrailerAdapterViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerAdapter.TrailerAdapterViewHolder holder, int position) {

        holder.mNumberView.setText(String.valueOf(position + 1));
        Picasso.with(mContext).load(Uri.parse(BASE_YOUTUBE_FIRST_THUMB_PATH +
                mTrailerKeys.get(position) + BASE_YOUTUBE_FIRST_THUMB_SUFFIX))
                .into(holder.mTrailerThumb);
    }

    @Override
    public int getItemCount() {
        if (null == mTrailerKeys) return 0;
        return mTrailerKeys.size();
    }

    public void setTrailers(List<String> trailerKeys, int height) {
        mTrailerKeys = trailerKeys;
        mHeight = height;
        notifyDataSetChanged();
    }

    public class TrailerAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        final TextView mNumberView;
        final ImageView mTrailerThumb;

        public TrailerAdapterViewHolder(View view) {
            super(view);
            mNumberView = view.findViewById(R.id.list_trailer_number);
            mTrailerThumb = view.findViewById(R.id.list_trailer_thumb);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mClickHandler.onClick(mTrailerKeys.get(getAdapterPosition()));
        }
    }
}
