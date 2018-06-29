package com.example.jon.nowplaying3;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.jon.nowplaying3.DataHandling.Poster;
import com.example.jon.nowplaying3.DataHandling.PosterDatabase;
import com.example.jon.nowplaying3.DataHandling.PosterViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity implements PosterAdapter.PosterAdapterOnClickHandler{

    private String sortStyle = "popular";
    private RecyclerView mRecycler;
    private PosterAdapter mPosterAdapter;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;

    private PosterDatabase mDB;

    private PosterViewModel mViewModel;
    private boolean isConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mErrorMessageDisplay = findViewById(R.id.tv_error_message);
        mLoadingIndicator = findViewById(R.id.pb_loading_posters);
        mDB = PosterDatabase.getDatabase(this);

        int spans = getAppropriateSpanCount(this);

        GridLayoutManager gLayout = new GridLayoutManager(this,spans);

        mRecycler = findViewById(R.id.recyclerview_grid);
        mRecycler.setHasFixedSize(true);
        mRecycler.setLayoutManager(gLayout);

        mPosterAdapter = new PosterAdapter(this, spans);
        mRecycler.setAdapter(mPosterAdapter);

        setTitle(getResources().getString(R.string.app_name) + " (" + sortStyle + ")");
    }

    @Override
    protected void onResume() {
        super.onResume();
        handleConnectionAndLoad();
    }

    private void handleConnectionAndLoad() {
        final ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if (!isConnected) {
            mErrorMessageDisplay.setText(getString(R.string.no_connection));
            mErrorMessageDisplay.setVisibility(View.VISIBLE);
            mLoadingIndicator.setVisibility(View.GONE);
            sortStyle = "favorites";
        }
        mViewModel = ViewModelProviders.of(this).get(PosterViewModel.class);
        switch (sortStyle){
            case "popular":
                mViewModel.getPopular().observe(this, new Observer<List<Poster>>() {
                    @Override
                    public void onChanged(@Nullable List<Poster> posters) {
                        mRecycler.setVisibility(View.VISIBLE);
                        mPosterAdapter.setSort(sortStyle);
                        mPosterAdapter.setPosters(posters);
                    }
                });
            case "top_rated":
                mViewModel.getRated().observe(this, new Observer<List<Poster>>() {
                    @Override
                    public void onChanged(@Nullable List<Poster> posters) {
                        mRecycler.setVisibility(View.VISIBLE);
                        mPosterAdapter.setSort(sortStyle);
                        mPosterAdapter.setPosters(posters);
                    }
                });
            case "favorites":
                mViewModel.getFavorites().observe(this, new Observer<List<Poster>>() {
                    @Override
                    public void onChanged(@Nullable List<Poster> posters) {
                        mRecycler.setVisibility(View.VISIBLE);
                        mPosterAdapter.setSort(sortStyle);
                        mPosterAdapter.setPosters(posters);
                    }
                });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sort,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_sort_popular:
                if (!(sortStyle.equals("popular"))){
                    sortStyle = "popular";
                    updateDisplay(true);
                }
                return true;
            case R.id.action_sort_rating:
                if (!(sortStyle.equals("top_rated"))){
                    sortStyle = "top_rated";
                    updateDisplay(true);
                }
                return true;
            case R.id.action_show_favorites:
                if (!(sortStyle.equals("favorites"))){
                    sortStyle = "favorites";
                    updateDisplay(true);
                }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(Poster poster) {
        return;
        //Intent toDetail = new Intent(this, DetailActivity.class);
        //toDetail.putExtra("movieID",poster.getMovieId());
        //startActivity(toDetail);
    }

    private int getAppropriateSpanCount(Context context){
        int land = Configuration.ORIENTATION_LANDSCAPE;
        if(context.getResources().getConfiguration().orientation == land){
            return 5;
        }
        return 3;
    }

    private void updateDisplay(boolean connected){
        handleConnectionAndLoad();
        setTitle(getResources().getString(R.string.app_name) + " (" + sortStyle + ")");
        mLoadingIndicator.setVisibility(View.GONE);
        if (connected) {
            mErrorMessageDisplay.setVisibility(View.GONE);
        }
        mRecycler.setVisibility(View.VISIBLE);
    }
}
