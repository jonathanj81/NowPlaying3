package com.example.jon.nowplaying3.DataHandling;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface PosterDao {

    @Query("SELECT * FROM poster_table ORDER BY movieId")
    LiveData<List<Poster>> loadAllPosters();

    @Query("SELECT * FROM poster_table WHERE inPopular = 1")
    LiveData<List<Poster>> loadAllPopular();

    @Query("SELECT * FROM poster_table WHERE inRated = 1")
    LiveData<List<Poster>> loadAllRated();

    @Query("SELECT * FROM poster_table WHERE inFavorites = 1")
    LiveData<List<Poster>> loadAllFavorites();

    @Query("SELECT * FROM poster_table WHERE movieId = :movieId LIMIT 1")
    Poster getSinglePoster(int movieId);

    @Query("SELECT * FROM poster_table WHERE movieId = :movieId LIMIT 1")
    LiveData<Poster> getSingleLivePoster(int movieId);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertPoster(Poster poster);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updatePoster(Poster poster);

    @Query("DELETE FROM poster_table WHERE inFavorites != 1")
    void deletePosters();

}
