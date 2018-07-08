package com.example.jon.nowplaying3.Utils;

import android.net.Uri;
import android.os.Build;
import android.util.Log;

import com.example.jon.nowplaying3.BuildConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;


/**
 * Created by Jon on 7/18/2017.
 */

public final class NetworkUtils {

    public static final String LOG_TAG = NetworkUtils.class.getSimpleName();

    private static final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/";
    private static final String IMAGE_SIZE = "w185/";

    private static final String BASE_POSTER_LIST_URL = "http://api.themoviedb.org/3/movie/";
    private static final String MY_API_KEY = BuildConfig.API_KEY;
    private static final String CREDITS_SUFFIX = "/credits";
    private static final String TRAILERS_SUFFIX = "/videos";
    private static final String REVIEWS_SUFFIX = "/reviews";


    public static URL buildDataUrl(String... strings) {

        StringBuilder baseStringToParse = new StringBuilder(BASE_POSTER_LIST_URL);

        switch (strings[0]){
            case "id":
                switch (strings[1]){
                    case "details":
                        baseStringToParse.append(strings[2]);
                        break;
                    case "reviews":
                        baseStringToParse.append(strings[2]).append(REVIEWS_SUFFIX);
                        break;
                    case "videos":
                        baseStringToParse.append(strings[2]).append(TRAILERS_SUFFIX);
                        break;
                    case "credits":
                        baseStringToParse.append(strings[2]).append(CREDITS_SUFFIX);
                        break;
                }
                break;
            case "sort":
                baseStringToParse.append(strings[1]);
                break;
            default:
                throw new UnsupportedOperationException("Your inputs are invalid");
        }

        Uri builtUri = Uri.parse(baseStringToParse.toString()).buildUpon()
                .appendQueryParameter("api_key", MY_API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static URL buildImageUrl(String path){
        Uri uri = Uri.parse(BASE_IMAGE_URL + IMAGE_SIZE + path);

        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    private static String readFromStream (InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        InputStreamReader inputStreamReader;
        if (inputStream != null){
            inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null){
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    public static String getJSONResponse(URL url) throws IOException {

        String response = "";
        if (url == null){
            return response;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try{
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                response = readFromStream(inputStream);
            }else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        }catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the JSON results.", e);
        }finally {
            if (urlConnection != null){
                urlConnection.disconnect();
            }
            if (inputStream != null){
                inputStream.close();
            }
        }
        return response;
    }
}
