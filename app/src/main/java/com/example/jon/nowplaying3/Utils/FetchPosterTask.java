package com.example.jon.nowplaying3.Utils;

import android.os.AsyncTask;

import com.example.jon.nowplaying3.DataHandling.Poster;
import com.example.jon.nowplaying3.DataHandling.PosterDatabase;
import com.example.jon.nowplaying3.DataHandling.PosterRepository;

import java.util.List;

/**
 * Created by Jon on 7/22/2017.
 */

public class FetchPosterTask extends AsyncTask<String, Void, List<Poster>> {

    private PosterRepository mRepository;

    public FetchPosterTask(PosterRepository repository){
        mRepository = repository;
    }

    @Override
    protected List<Poster> doInBackground(String... strings) {

        try {
            String jsonResponse = NetworkUtils
                    .getJSONResponse(NetworkUtils.buildDataUrl(strings));

            return JSONParsingUtils.getPosterArray(jsonResponse,strings[1]);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(List<Poster> posters) {
        super.onPostExecute(posters);
        mRepository.upSert(posters);
    }
}
