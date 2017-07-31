package com.test.upcoming.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Aks4125 on 7/26/2017.
 */

public class MovieImages {


    private int id;

    private ArrayList<Backdrop> backdrops = null;

    private ArrayList<Poster> posters = null;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Backdrop> getBackdrops() {
        return backdrops;
    }

    public void setBackdrops(ArrayList<Backdrop> backdrops) {
        this.backdrops = backdrops;
    }

    public ArrayList<Poster> getPosters() {
        return posters;
    }

    public void setPosters(ArrayList<Poster> posters) {
        this.posters = posters;
    }
    public class Poster {

        @SerializedName("aspect_ratio")
        private Double aspectRatio;
        @SerializedName("file_path")
        private String filePath;
        @SerializedName("height")
        private int height;
        @SerializedName("iso_639_1")
        private String iso6391;
        @SerializedName("vote_average")
        private double voteAverage;
        @SerializedName("vote_count")
        private int voteCount;

        private int width;

        public Double getAspectRatio() {
            return aspectRatio;
        }

        public void setAspectRatio(Double aspectRatio) {
            this.aspectRatio = aspectRatio;
        }

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public String getIso6391() {
            return iso6391;
        }

        public void setIso6391(String iso6391) {
            this.iso6391 = iso6391;
        }

        public double getVoteAverage() {
            return voteAverage;
        }

        public void setVoteAverage(double voteAverage) {
            this.voteAverage = voteAverage;
        }

        public int getVoteCount() {
            return voteCount;
        }

        public void setVoteCount(int voteCount) {
            this.voteCount = voteCount;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

    }
    public class Backdrop {

        @SerializedName("aspect_ratio")
        private Double aspectRatio;
        @SerializedName("file_path")
        private String filePath;

        private int height;
        @SerializedName("iso_639_1")
        private String iso6391;
        @SerializedName("vote_average")
        private double voteAverage;
        @SerializedName("vote_count")
        private int voteCount;

        private int width;

        public Double getAspectRatio() {
            return aspectRatio;
        }

        public void setAspectRatio(Double aspectRatio) {
            this.aspectRatio = aspectRatio;
        }

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public String getIso6391() {
            return iso6391;
        }

        public void setIso6391(String iso6391) {
            this.iso6391 = iso6391;
        }

        public double getVoteAverage() {
            return voteAverage;
        }

        public void setVoteAverage(double voteAverage) {
            this.voteAverage = voteAverage;
        }

        public int getVoteCount() {
            return voteCount;
        }

        public void setVoteCount(int voteCount) {
            this.voteCount = voteCount;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

    }
}
