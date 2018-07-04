package com.example.jon.nowplaying3.DataHandling;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

public class SinglePosterViewModelFactory implements ViewModelProvider.Factory {

    private final Application mApp;
    private final int mID;

    public SinglePosterViewModelFactory(Application application, int id) {
        mApp = application;
        mID = id;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new SinglePosterViewModel(mApp,mID);
    }
}
