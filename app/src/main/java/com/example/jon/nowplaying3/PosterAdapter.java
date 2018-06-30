package com.example.jon.nowplaying3;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.jon.nowplaying3.DataHandling.Poster;
import com.example.jon.nowplaying3.Utils.NetworkUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Jon on 7/18/2017.
 */

public class PosterAdapter extends RecyclerView.Adapter<PosterAdapter.PosterAdapterViewHolder> {

    private List<Poster> mPosters;

    private final PosterAdapterOnClickHandler mClickHandler;
    private String mSortType;
    private Context mContext;
    private int mSpans;

    public interface PosterAdapterOnClickHandler {
        void onClick(Poster poster);
    }

    public PosterAdapter(PosterAdapterOnClickHandler clickHandler, int spans) {
        mClickHandler = clickHandler;
        mSpans = spans;
    }

    public class PosterAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView mPositionView;
        final TextView mRelativeView;
        final ImageView mPosterThumb;
        final ProgressBar mProgressBar;
        final ImageView mLittleStar;

        public PosterAdapterViewHolder(View view) {
            super(view);
            mPositionView = view.findViewById(R.id.tv_position_display);
            mRelativeView = view.findViewById(R.id.tv_relative_display);
            mPosterThumb = view.findViewById(R.id.iv_poster_thumb);
            mProgressBar = view.findViewById(R.id.pb_loading_individual_posters);
            mLittleStar = view.findViewById(R.id.iv_main_favorite_dot);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mClickHandler.onClick(mPosters.get(getAdapterPosition()));
        }
    }

    @Override
    public PosterAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        mContext = viewGroup.getContext();

        View layoutView = LayoutInflater.from(mContext).inflate(R.layout.card_view_poster, viewGroup,false);
        layoutView.setLayoutParams(new RecyclerView.LayoutParams(calculateWidth(mSpans),calculateWidth(mSpans)*3/2));
        return new PosterAdapterViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(PosterAdapterViewHolder holder, int position) {
        holder.mPositionView.setText(String.valueOf(position + 1));
        holder.mProgressBar.setVisibility(View.VISIBLE);
        Picasso.with(mContext).load(NetworkUtils.buildImageUrl(mPosters.get(position).getImagePath()).toString())
                .into(holder.mPosterThumb, new ImageLoadedCallback(holder.mProgressBar) {
                    @Override
                    public void onSuccess() {
                        if (this.progressBar != null) {
                            this.progressBar.setVisibility(View.GONE);
                        }
                    }});

        if (mSortType.equals("popular")){
            double index = mPosters.get(position).getPopularity() / mPosters.get(0).getPopularity() * 100;
            holder.mRelativeView.setText(String.format("%.2f",index));
        } else {
            holder.mRelativeView.setText(mPosters.get(position).getAverage());
        }
        if (mPosters.get(position).getInFavorites() == 1){
            holder.mLittleStar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        if (null == mPosters) return 0;
        return mPosters.size();
    }

    public void setPosters(List<Poster> posters) {
        mPosters = posters;
        notifyDataSetChanged();
    }

    public void setSort(String sortType){
        mSortType = sortType;
    }

    private class ImageLoadedCallback implements Callback {
        ProgressBar progressBar;

        public  ImageLoadedCallback(ProgressBar progBar){
            progressBar = progBar;
        }

        @Override
        public void onSuccess() {

        }

        @Override
        public void onError() {

        }
    }

    private int calculateWidth(int spans){

        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        return (width-(spans*8)-4)/spans;
    }
}
