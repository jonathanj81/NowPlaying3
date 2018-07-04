package com.example.jon.nowplaying3.DataHandling;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.support.annotation.NonNull;

import com.example.jon.nowplaying3.App;
import com.example.jon.nowplaying3.Utils.FetchPosterTask;

import java.util.concurrent.Executors;

@Database(entities = {Poster.class}, version = 4, exportSchema = false)
@TypeConverters(ListConverter.class)
public abstract class PosterDatabase extends RoomDatabase {
    public abstract PosterDao mDao();

    private static PosterDatabase mDB;

    public static PosterDatabase getDatabase(final Context context) {
        if (mDB == null) {
            synchronized (PosterDatabase.class) {
                if (mDB == null) {
                    mDB = Room.databaseBuilder(context.getApplicationContext(),
                            PosterDatabase.class,"posters")
                            .fallbackToDestructiveMigrationFrom(3)
                            .addCallback(new Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            new FetchPosterTask(new PosterRepository((App)context.getApplicationContext()))
                                                    .execute("sort","popular");
                                            new FetchPosterTask(new PosterRepository((App)context.getApplicationContext()))
                                                    .execute("sort","top_rated");
                                        }
                                    });
                                }
                            })
                            .build();
                }
            }
        }
        return mDB;
    }
}
