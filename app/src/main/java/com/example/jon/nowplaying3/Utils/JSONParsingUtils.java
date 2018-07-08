package com.example.jon.nowplaying3.Utils;

import android.util.Log;

import com.example.jon.nowplaying3.DataHandling.Poster;
import com.example.jon.nowplaying3.DataHandling.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.min;

/**
 * Created by Jon on 7/18/2017.
 */

public final class JSONParsingUtils {

    private static final String KEY_RESULTS = "results";
    private static final String KEY_KEY = "key";
    private static final String KEY_CAST = "cast";
    private static final String KEY_NAME = "name";
    private static final String KEY_GENRES = "genres";
    private static final String KEY_RUNTIME = "runtime";
    private static final String KEY_TITLE = "title";
    private static final String KEY_VOTE_AVERAGE = "vote_average";
    private static final String KEY_POPULARITY = "popularity";
    private static final String KEY_POSTER_PATH = "poster_path";
    private static final String KEY_OVERVIEW = "overview";
    private static final String KEY_RELEASE_DATE = "release_date";
    private static final String KEY_ID = "id";

    public static List<Poster> getPosterArray(String jsonResponse, String type) throws JSONException {

        double mAverage = 0.0;
        String mImagePath = "";
        String mDescription = "";
        double mPopularity = 0.0;
        String mTitle = "";
        String mReleaseDate = "";
        int mID = 0;
        int inPop = 0;
        int inRated = 0;

        if (type.equals("popular")){
            inPop = 1;
        } else {
            inRated = 1;
        }

        List<Poster> posters = new ArrayList<>();

        try {
            JSONObject root = new JSONObject(jsonResponse);
            if (root.has(KEY_RESULTS)) {
                JSONArray movieList = root.getJSONArray(KEY_RESULTS);

                for (int i = 0; i < movieList.length(); i++) {

                    try {
                        JSONObject movie = movieList.getJSONObject(i);

                        if (movie.has(KEY_VOTE_AVERAGE)) {
                            mAverage = movie.getDouble(KEY_VOTE_AVERAGE);
                        }
                        if (movie.has(KEY_TITLE)) {
                            mTitle = movie.getString(KEY_TITLE);
                        }
                        if (movie.has(KEY_POPULARITY)) {
                            mPopularity = movie.getDouble(KEY_POPULARITY);
                        }
                        if (movie.has(KEY_POSTER_PATH)) {
                            mImagePath = movie.getString(KEY_POSTER_PATH);
                        }
                        if (movie.has(KEY_OVERVIEW)) {
                            mDescription = movie.getString(KEY_OVERVIEW);
                        }
                        if (movie.has(KEY_RELEASE_DATE)) {
                            mReleaseDate = movie.getString(KEY_RELEASE_DATE);
                        }
                        if (movie.has(KEY_ID)) {
                            mID = movie.getInt(KEY_ID);
                        }
                    } catch (JSONException e2) {
                        e2.printStackTrace();
                    }
                    Poster poster = new Poster(mDescription, String.valueOf(mAverage), mImagePath, mPopularity,
                            mTitle, mReleaseDate, mID, 0, inPop, inRated);
                    posters.add(poster);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return posters;
    }

    public static List<String> getRuntimeAndGenre(String[] urlSuffixes){

        List<String> runGen = new ArrayList<>();

        URL url = NetworkUtils.buildDataUrl(urlSuffixes);
        String jSON = null;
        try {
            jSON = NetworkUtils.getJSONResponse(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!(jSON == null || jSON.isEmpty())){
            try {
                JSONObject root = new JSONObject(jSON);
                if (root.has(KEY_RUNTIME)){
                    int time = root.getInt(KEY_RUNTIME);
                    int hours = time/60;
                    int minutes = time%60;
                    runGen.add(hours + ":" + minutes);
                }
                if (root.has(KEY_GENRES)){
                    JSONArray genreList = root.getJSONArray(KEY_GENRES);
                    JSONObject firstGenre = genreList.getJSONObject(0);
                    if (firstGenre.has(KEY_NAME)){
                        runGen.add(firstGenre.getString(KEY_NAME));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return runGen;
    }

    public static List<String> getCredits(String[] urlSuffixes){

        List<String> topCredits = new ArrayList<>();

        URL url = NetworkUtils.buildDataUrl(urlSuffixes);
        String jSON = null;
        try {
            jSON = NetworkUtils.getJSONResponse(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!(jSON == null || jSON.isEmpty())){
            try {
                JSONObject root = new JSONObject(jSON);
                if (root.has(KEY_CAST)){
                    JSONArray castList = root.getJSONArray(KEY_CAST);
                    for (int i = 0; i < min(castList.length(),3); i++){
                        JSONObject castMember = castList.getJSONObject(i);
                        if (castMember.has(KEY_NAME)){
                            topCredits.add(" " + castMember.getString(KEY_NAME));
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (topCredits.size() == 0){
            topCredits.add("none");
        }
        return topCredits;
    }

    public static List<Trailer> getTrailerKeys(String[] urlSuffixes){

        List<Trailer> trailers = new ArrayList<>();

        URL url = NetworkUtils.buildDataUrl(urlSuffixes);
        String jSON = null;
        try {
            jSON = NetworkUtils.getJSONResponse(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!(jSON == null || jSON.isEmpty())){
            try {
                JSONObject root = new JSONObject(jSON);
                if (root.has(KEY_RESULTS)){
                    JSONArray results = root.getJSONArray(KEY_RESULTS);
                    for (int i = 0; i < results.length(); i++){
                        JSONObject movie = results.getJSONObject(i);
                        if (movie.has(KEY_KEY)){
                            Trailer trailer = new Trailer(movie.getString(KEY_KEY));
                            trailers.add(trailer);
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return trailers;
    }
}
