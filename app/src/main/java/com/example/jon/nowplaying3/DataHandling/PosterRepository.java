package com.example.jon.nowplaying3.DataHandling;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PosterRepository {

    private PosterDao mDao;
    private LiveData<List<Poster>> mAllPosters;
    private LiveData<List<Poster>> mPopular;
    private LiveData<List<Poster>> mRated;
    private LiveData<List<Poster>> mFavorites;

    public PosterRepository(Application application) {
        PosterDatabase db = PosterDatabase.getDatabase(application);
        mDao = db.mDao();
        mAllPosters = mDao.loadAllPosters();
        mPopular = mDao.loadAllPopular();
        mRated = mDao.loadAllRated();
        mFavorites = mDao.loadAllFavorites();

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
    public LiveData<Poster> getSinglePoster(int id){
        return mDao.getSinglePoster(id);
    }

    public void deleteNonFaves(){
        new deleteAsyncTask(mDao).execute();
    }

    public static class deleteAsyncTask extends AsyncTask<Void,Void,Void>{

        private PosterDao mDeleteDao;

        deleteAsyncTask(PosterDao dao){
            mDeleteDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            mDeleteDao.deletePosters();
            return null;
        }
    }

    public void upSert (List<Poster> posters) {
        new upSertAsyncTask(mDao,posters).execute();
    }

    private static class upSertAsyncTask extends AsyncTask<Void, Void, Void> {

        private PosterDao mAsyncTaskDao;
        private List<Poster> mPosters;

        upSertAsyncTask(PosterDao dao, List<Poster> posters) {
            mAsyncTaskDao = dao;
            mPosters = posters;
        }

        @Override
        protected Void doInBackground(Void...voids) {

            for (Poster poster : mPosters) {
                int insertResult = (int) mAsyncTaskDao.insertPoster(poster);
                if (insertResult == -1) {
                    Poster original = mAsyncTaskDao.getSinglePoster(poster.getMovieId()).getValue();
                    int isFromPopular = poster.getInPopular();
                    if (isFromPopular == 1) {
                        original.setInPopular(1);
                    } else {
                        original.setInRated(1);
                    }
                    mAsyncTaskDao.updatePoster(original);
                }
            }
            return null;
        }
    }
}