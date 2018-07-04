package com.example.jon.nowplaying3.DataHandling;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

public class SinglePosterViewModel extends ViewModel {

    private LiveData<Poster> mSinglePoster;
    private PosterRepository mRepository;

    public SinglePosterViewModel(Application application, int id) {
        mRepository = new PosterRepository(application);
        mSinglePoster = mRepository.getSingleLivePoster(id);
    }

    public LiveData<Poster> getSingleLivePoster() {
        return mSinglePoster;
    }
}
