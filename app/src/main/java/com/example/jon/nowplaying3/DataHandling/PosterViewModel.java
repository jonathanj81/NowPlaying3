package com.example.jon.nowplaying3.DataHandling;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;

import java.util.List;

public class PosterViewModel extends AndroidViewModel {

    private PosterRepository mRepository;

    private LiveData<List<Poster>> mAllPosters;
    private LiveData<List<Poster>> mPopular;
    private LiveData<List<Poster>> mRated;
    private LiveData<List<Poster>> mFavorites;

    public PosterViewModel (Application application) {
        super(application);
        mRepository = new PosterRepository(application);
        mAllPosters = mRepository.getAllPosters();
        mPopular = mRepository.getPopular();
        mRated = mRepository.getRated();
        mFavorites = mRepository.getFavorites();
    }

    public LiveData<List<Poster>> getAllPosters() {
        return mAllPosters;
    }
    public LiveData<List<Poster>> getPopular() {
        return mPopular;
    }
    public LiveData<List<Poster>> getRated() {
        return mRated;
    }
    public LiveData<List<Poster>> getFavorites() {
        return mFavorites;
    }

    public void upSert(List<Poster> posters) { mRepository.upSert(posters); }
}
