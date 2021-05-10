package com.example.uscfilms;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class CardDataModel {
    private String posterPath;
    private String title;
    private String ID;
    private String mediaType;
    private String videoKey;

    public CardDataModel(JSONObject itemData) {
        try {
            this.posterPath = (String) itemData.get("poster_path");
            this.title = (String) itemData.get("title");
            this.ID = String.valueOf(itemData.get("id"));
            this.mediaType = (String) itemData.get("media_type");
            this.videoKey = "";
            this.setVideoKey();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getVideoKey() {
        return videoKey;
    }

    public void setVideoKey() {
        String getVideoURL = "https://android-backend-dot-movie-show-time.uc.r.appspot.com/get_youtube_video_id/";
        getVideoURL = getVideoURL + this.mediaType + "/" + this.ID;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                getVideoURL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject data = response.getJSONObject("data");
                            videoKey = (String) data.get("key");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }
        );
        MySingleton.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    public void setVideoKey(String videoKey) {
        this.videoKey = videoKey;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public JSONObject getJSONObject(){
        JSONObject dataJSON = new JSONObject();
        try {
            dataJSON.put("poster_path", getPosterPath());
            dataJSON.put("title", getTitle());
            dataJSON.put("id", getID());
            dataJSON.put("media_type", getMediaType());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dataJSON;
    }
}
