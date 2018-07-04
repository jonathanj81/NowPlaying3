package com.example.jon.nowplaying3;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import com.example.jon.nowplaying3.DataHandling.Poster;
import com.example.jon.nowplaying3.DataHandling.PosterRepository;
import com.example.jon.nowplaying3.DataHandling.SinglePosterViewModel;
import com.example.jon.nowplaying3.DataHandling.SinglePosterViewModelFactory;
import com.example.jon.nowplaying3.Utils.DetailsPosterTask;
import com.example.jon.nowplaying3.Utils.DetailsPosterTaskListener;
import com.example.jon.nowplaying3.Utils.NetworkUtils;
import com.example.jon.nowplaying3.databinding.ActivityDetailBinding;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener, TrailerAdapter.TrailerAdapterOnClickHandler,
        DetailsPosterTaskListener<Poster> {

    private static int mMovieID;
    private int mPosterHeight = 210;

    private Poster mCurrentPoster;
    private PosterRepository mRepository;
    private TrailerAdapter mTrailerAdapter;
    private SinglePosterViewModel mViewModel;
    private ActivityDetailBinding binding;
    private boolean isFavorite = false;
    private boolean areDetailsIn = false;
    private static final String BASE_REVIEW_URL = "https://www.themoviedb.org/movie/";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.detailTrailerList.setHasFixedSize(true);
        binding.detailTrailerList.setLayoutManager(layoutManager);
        mTrailerAdapter = new TrailerAdapter(this);
        binding.detailTrailerList.setAdapter(mTrailerAdapter);

        mRepository = new PosterRepository(getApplication());

        Intent fromMain = getIntent();
        mMovieID = fromMain.getIntExtra("movieID", 0);
        mViewModel = ViewModelProviders.of(this,
                new SinglePosterViewModelFactory(getApplication(), mMovieID)).get(SinglePosterViewModel.class);

        binding.detailFavoriteStar.setOnClickListener(this);
        binding.detailReadReviewsButton.setOnClickListener(this);

        binding.detailTrailerList.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mPosterHeight = binding.detailIvPoster.getHeight();
                mViewModel.getSingleLivePoster().observe(DetailActivity.this, new Observer<Poster>() {
                    @Override
                    public void onChanged(@Nullable Poster poster) {
                        binding.setPoster(poster);
                        mCurrentPoster = poster;
                        setAdditionalDisplay();
                    }
                });
                binding.detailTrailerList.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    @Override
    public void onClick(String key) {
        String BASE_YOUTUBE_VIDEO_PATH = "https://www.youtube.com/watch?v=";
        Uri uri = Uri.parse(BASE_YOUTUBE_VIDEO_PATH + key);
        Intent toTrailer = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(toTrailer);
    }

    @Override
    public void onTaskComplete(final Poster result) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                areDetailsIn = true;
                mRepository.updatePoster(result);
            }
        }).start();
    }

    private void setAdditionalDisplay() {
        if (areDetailsIn || mCurrentPoster.getInFavorites() == 1) {
            Picasso.with(getParent()).load(NetworkUtils.buildImageUrl(mCurrentPoster.getImagePath()).toString())
                    .into(binding.detailIvPoster);
            setColors();
            if (mCurrentPoster.getInFavorites() == 1) {
                isFavorite = true;
                binding.detailFavoriteStar.setImageResource(R.drawable.baseline_favorite_black_18dp);
            }
            mTrailerAdapter.setTrailers(mCurrentPoster.getTrailerKeys(), mPosterHeight);
        } else {
            final ConnectivityManager cm =
                    (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();
            if (isConnected) {
                new DetailsPosterTask(this).execute(mCurrentPoster);
            } else {
                Toast.makeText(this, getString(R.string.details_incomplete), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void setColors() {
        BitmapDrawable d = (BitmapDrawable) binding.detailIvPoster.getDrawable();
        Bitmap bitmap = d.getBitmap();
        if (bitmap != null && !bitmap.isRecycled()) {
            Palette palette = Palette.from(bitmap).resizeBitmapArea(binding.detailIvPoster.getHeight())
                    .maximumColorCount(128).generate();

            Palette.Swatch mutedSwatch = palette.getMutedSwatch();
            Palette.Swatch mutedLightSwatch = palette.getLightMutedSwatch();
            Palette.Swatch mutedDarkSwatch = palette.getDarkMutedSwatch();

            if (mutedSwatch != null) {
                binding.detailRootLayout.setBackgroundColor(mutedSwatch.getRgb());
            }
            GradientDrawable description = (GradientDrawable) binding.detailDescriptionScrollview.getBackground();
            if (mutedDarkSwatch != null) {
                description.setColor(mutedDarkSwatch.getRgb());
            }
            GradientDrawable trailersView = (GradientDrawable) binding.detailTrailerList.getBackground();
            if (mutedLightSwatch != null) {
                trailersView.setColor(mutedLightSwatch.getRgb());
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.detail_favorite_star:
                addRemoveFavorite();
                break;
            case R.id.detail_read_reviews_button:
                Uri uri = Uri.parse(BASE_REVIEW_URL + String.valueOf(mMovieID) + "/reviews");
                Intent toReviews = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(toReviews);
        }
    }

    private void addRemoveFavorite() {

        if (isFavorite) {
            new AlertDialog.Builder(this)
                    .setTitle(getResources().getString(R.string.star_dialog_title))
                    .setMessage(getResources().getString(R.string.star_dialog_currently_on_question))
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            mCurrentPoster.setInFavorites(0);
                            binding.detailFavoriteStar.setImageResource(R.drawable.baseline_favorite_border_black_18dp);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    mRepository.updatePoster(mCurrentPoster);
                                }
                            }).start();
                        }
                    })
                    .setNegativeButton(android.R.string.no, null).show();
        } else {
            new AlertDialog.Builder(this)
                    .setTitle(getResources().getString(R.string.star_dialog_title))
                    .setMessage(getResources().getString(R.string.star_dialog_currently_off_question))
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            mCurrentPoster.setInFavorites(1);
                            binding.detailFavoriteStar.setImageResource(R.drawable.baseline_favorite_black_18dp);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    mRepository.updatePoster(mCurrentPoster);
                                }
                            }).start();
                        }
                    })
                    .setNegativeButton(android.R.string.no, null).show();
        }
    }
}
