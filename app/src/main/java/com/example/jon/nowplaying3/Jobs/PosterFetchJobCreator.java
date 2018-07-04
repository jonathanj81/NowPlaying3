package com.example.jon.nowplaying3.Jobs;

import android.app.Application;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;
import com.example.jon.nowplaying3.DataHandling.PosterDao;
import com.example.jon.nowplaying3.DataHandling.PosterDatabase;
import com.example.jon.nowplaying3.DataHandling.PosterRepository;

public class PosterFetchJobCreator implements JobCreator {

    private PosterDatabase mDB;

    public PosterFetchJobCreator(PosterDatabase db){
        mDB = db;
    }

    @Override
    @Nullable
    public Job create(@NonNull String tag) {
        switch (tag) {
            case PosterFetchJob.TAG:
                return new PosterFetchJob(mDB);
            default:
                return null;
        }
    }
}