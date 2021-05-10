package com.example.uscfilms.ui.search;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class SearchDataModel {
    private String backdropPath;
    private String mediaType;
    private String year;
    private String title;
    private String rating;
    private String ID;

    public SearchDataModel(JSONObject itemData) {
        try {
            this.ID = String.valueOf(itemData.get("id"));
            this.backdropPath= (String) itemData.get("backdrop_path");
            this.setMediaType(itemData);
            this.rating = (String) itemData.get("vote_average");
            this.setTitle(itemData);
            this.setYear(itemData);


        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public String getID() {
        return ID;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public void setMediaType(JSONObject data) {
        try {
            this.mediaType = (data.get("media_type").equals("movie"))?"Movie":"TV";
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public String getYear() {
        return year;
    }

    public void setYear(JSONObject data) {
        try {
            this.year = (String) ((this.mediaType.equals("Movie"))?data.get("release_date"):data.get("first_air_date"));
            this.year = "("+this.year.split("-")[0]+")";
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(JSONObject data) {
        try {
            this.title = (String) ((this.mediaType.equals("Movie"))?data.get("original_title"):data.get("original_name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

}
