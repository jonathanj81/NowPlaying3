package com.example.jon.nowplaying3.Utils;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.example.jon.nowplaying3.DataHandling.Poster;
import com.example.jon.nowplaying3.DataHandling.Trailer;

import java.util.ArrayList;
import java.util.List;

public class DetailsPosterTask extends AsyncTask<Poster,Void,Poster> {

    private DetailsPosterTaskListener<Poster> mListener;

    public DetailsPosterTask (DetailsPosterTaskListener<Poster> listener){
        mListener = listener;
    }

    @Override
    protected Poster doInBackground(Poster... posters) {
        Poster tempPoster = posters[0];
        String movieID = String.valueOf(tempPoster.getMovieId());

        List<String> runGenResults = JSONParsingUtils.getRuntimeAndGenre
                (new String[]{"id", "details", String.valueOf(movieID)});
        String mRuntime = runGenResults.get(0);
        String mGenre = runGenResults.get(1);

        List<String> mCredits = JSONParsingUtils.getCredits(new String[]{"id", "credits", String.valueOf(movieID)});
        String credits = TextUtils.join(",",mCredits);
        List<Trailer> trailers = JSONParsingUtils.getTrailerKeys(new String[]{"id", "videos", String.valueOf(movieID)});
        List<String> trailerKeyStrings = new ArrayList<>();
        for (Trailer trailer : trailers){
            trailerKeyStrings.add(trailer.getKey());
        }
        tempPoster.setCredits(credits);
        tempPoster.setGenre(mGenre);
        tempPoster.setRuntime(mRuntime);
        tempPoster.setTrailerKeys(trailerKeyStrings);

        return tempPoster;
    }

    @Override
    protected void onPostExecute(Poster poster) {
        super.onPostExecute(poster);
        mListener.onTaskComplete(poster);
    }
}
