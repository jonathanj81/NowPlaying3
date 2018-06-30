package com.example.jon.nowplaying3.DataHandling;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "poster_table")
public class Poster {

    private double average;
    private String imagePath;
    private String description;
    private double popularity;
    private String title;
    private String releaseDate;
    private int runtime;
    private String genre;
    private String credits;

    @PrimaryKey
    private int movieId;

    private List<String> trailerKeys;
    private int inFavorites;
    private int inPopular;
    private int inRated;

    public Poster(String description, double average, String imagePath, double popularity, String title, String releaseDate,
                  int movieId, int inFavorites, int inPopular, int inRated) {

        this.imagePath = imagePath;
        this.average = average;
        this.description = description;
        this.popularity = popularity;
        this.title = title;
        this.releaseDate = releaseDate;
        this.movieId = movieId;
        this.inFavorites = inFavorites;
        this.inPopular = inPopular;
        this.inRated = inRated;
        this.trailerKeys = new ArrayList<>();
    }

    public String getAverage() {
        return String.valueOf(average);
    }

    public void setAverage(double average) {
        this.average = average;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getCredits() {
        return credits;
    }

    public void setCredits(List<String> credits) {
        StringBuilder temp = new StringBuilder();
        for (String item : credits){
            temp.append(item).append(" ");
        }
        this.credits = temp.toString();
    }

    public List<String> getTrailerKeys() {
        return trailerKeys;
    }

    public void setTrailerKeys(List<String> trailerKeys) {
        this.trailerKeys = trailerKeys;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public int getInFavorites() {
        return inFavorites;
    }

    public void setInFavorites(int inFavorites) {
        this.inFavorites = inFavorites;
    }

    public int getInPopular() {
        return inPopular;
    }

    public void setInPopular(int inPopular) {
        this.inPopular = inPopular;
    }

    public int getInRated() {
        return inRated;
    }

    public void setInRated(int inFavorites) {
        this.inRated = inRated;
    }
}
