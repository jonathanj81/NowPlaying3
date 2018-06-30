package com.example.jon.nowplaying3;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.jon.nowplaying3.DataHandling.Poster;
import com.example.jon.nowplaying3.DataHandling.PosterRepository;
import com.example.jon.nowplaying3.DataHandling.PosterViewModel;
import com.example.jon.nowplaying3.DataHandling.Trailer;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private static String mGenre;
    private static String mRuntime;
    private static int mMovieID;
    private TextView mCreditsView;
    private TextView mGenreView;
    private TextView mRuntimeView;
    private static List<String> mCredits;
    private static List<Trailer> mTrailerKeys;
    private RecyclerView mRecycler;
    private ImageView mPoster;
    private TextView mTitleView;
    private ConstraintLayout mRoot;
    private TextView mDescriptionView;
    private ScrollView mDescriptionScroll;
    private ImageView mFavoriteStar;
    private TextView mNoTrailers;
    private int isFromFavorites;

    private Poster mCurrentPoster;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecycler = findViewById(R.id.detail_trailer_list);
        mRecycler.setHasFixedSize(true);
        mRecycler.setLayoutManager(layoutManager);

        Intent fromMain = getIntent();
        int movieID = fromMain.getIntExtra("movieID", 0);
        if (movieID != 0){
            PosterViewModel viewModel = ViewModelProviders.of(this).get(PosterViewModel.class);
            viewModel.getSinglePoster(movieID).observe(this, new Observer<Poster>() {
                @Override
                public void onChanged(@Nullable Poster poster) {

                }
            });
        }
    }
}
