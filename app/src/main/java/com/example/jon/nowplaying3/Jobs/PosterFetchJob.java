package com.example.jon.nowplaying3.Jobs;

import android.support.annotation.NonNull;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobRequest;
import com.example.jon.nowplaying3.DataHandling.Poster;
import com.example.jon.nowplaying3.DataHandling.PosterDao;
import com.example.jon.nowplaying3.DataHandling.PosterDatabase;
import com.example.jon.nowplaying3.Utils.JSONParsingUtils;
import com.example.jon.nowplaying3.Utils.NetworkUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;


public class PosterFetchJob extends Job {

    public static final String TAG = "poster_job_tag";
    private PosterDatabase mDB;
    private PosterDao mDao;

    public PosterFetchJob(PosterDatabase db){
        mDB = db;
        mDao = mDB.mDao();
    }

    @Override
    @NonNull
    protected Result onRunJob(Params params) {

        mDao.deletePosters();
        List<Poster> posters;
        String[] types = {"popular","top_rated"};

        for (String type : types) {
            try {
                String jsonResponse = NetworkUtils
                        .getJSONResponse(NetworkUtils.buildDataUrl("sort",type));
                posters = JSONParsingUtils.getPosterArray(jsonResponse, type);
            } catch (Exception e) {
                e.printStackTrace();
                return Result.FAILURE;
            }

            for (Poster poster : posters) {
                int insertResult = (int) mDao.insertPoster(poster);
                if (insertResult == -1) {
                    Poster original = mDao.getSinglePoster(poster.getMovieId());
                    int isFromPopular = poster.getInPopular();
                    if (isFromPopular == 1) {
                        original.setInPopular(1);
                    } else {
                        original.setInRated(1);
                    }
                    mDao.updatePoster(original);
                }
            }
        }
        return Result.SUCCESS;
    }

    public void scheduleJob() {
        new JobRequest.Builder(PosterFetchJob.TAG)
                .setPeriodic(TimeUnit.HOURS.toMillis(8))
                .setRequiredNetworkType(JobRequest.NetworkType.CONNECTED)
                .setRequiresDeviceIdle(false)
                .setRequiresBatteryNotLow(false)
                .setRequiresStorageNotLow(false)
                .setUpdateCurrent(true)
                .build()
                .schedule();
    }
}
