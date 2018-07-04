package com.example.jon.nowplaying3.Utils;

import android.util.Log;

import com.example.jon.nowplaying3.DataHandling.Poster;
import com.example.jon.nowplaying3.DataHandling.Trailer;
import com.example.jon.nowplaying3.R;

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
            if (root.has("results")) {
                JSONArray movieList = root.getJSONArray("results");

                for (int i = 0; i < movieList.length(); i++) {

                    try {
                        JSONObject movie = movieList.getJSONObject(i);

                        if (movie.has("vote_average")) {
                            mAverage = movie.getDouble("vote_average");
                        }
                        if (movie.has("title")) {
                            mTitle = movie.getString("title");
                        }
                        if (movie.has("popularity")) {
                            mPopularity = movie.getDouble("popularity");
                        }
                        if (movie.has("poster_path")) {
                            mImagePath = movie.getString("poster_path");
                        }
                        if (movie.has("overview")) {
                            mDescription = movie.getString("overview");
                        }
                        if (movie.has("release_date")) {
                            mReleaseDate = movie.getString("release_date");
                        }
                        if (movie.has("id")) {
                            mID = movie.getInt("id");
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
                if (root.has("runtime")){
                    int time = root.getInt("runtime");
                    int hours = time/60;
                    int minutes = time%60;
                    runGen.add(hours + ":" + minutes);
                }
                if (root.has("genres")){
                    JSONArray genreList = root.getJSONArray("genres");
                    JSONObject firstGenre = genreList.getJSONObject(0);
                    if (firstGenre.has("name")){
                        runGen.add(firstGenre.getString("name"));
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
                if (root.has("cast")){
                    JSONArray castList = root.getJSONArray("cast");
                    for (int i = 0; i < min(castList.length(),3); i++){
                        JSONObject castMember = castList.getJSONObject(i);
                        if (castMember.has("name")){
                            topCredits.add(" " + castMember.getString("name"));
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
                if (root.has("results")){
                    JSONArray results = root.getJSONArray("results");
                    for (int i = 0; i < results.length(); i++){
                        JSONObject movie = results.getJSONObject(i);
                        if (movie.has("key")){
                            Trailer trailer = new Trailer(movie.getString("key"));
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
