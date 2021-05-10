package com.example.uscfilms.detailsPage;

import com.example.uscfilms.CardDataModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DetailsPageDataModel {
    public class CastDataModel {
        private String profileImage;
        private String name;

        public CastDataModel(JSONObject data) throws JSONException {
            this.profileImage = data.getString("profile_path");
            this.name = data.getString("name");
        }

        public String getProfileImage() {
            return profileImage;
        }

        public String getName() {
            return name;
        }
    }

    public class ReviewsDataModel {
        private String author;
        private String dateCreated;
        private String rating;
        private String reviewText;

        public ReviewsDataModel(JSONObject data) throws JSONException {
            this.author = data.getString("author");
            setDateCreated(data.getString("created_at"));
            this.rating = data.getString("rating");
            this.reviewText = data.getString("content");
        }

        public String getAuthor() {
            return author;
        }

        public void setDateCreated(String dateCreated) {
            try {
                SimpleDateFormat fromFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                Date date = fromFormat.parse(dateCreated);

                SimpleDateFormat outFormat= new SimpleDateFormat("EEE, MMM d, yyyy");
                this.dateCreated=outFormat.format(date);


            } catch (ParseException e) {
                this.dateCreated="";
                e.printStackTrace();
            }
        }

        public String getDateCreated() {

            return dateCreated;
        }

        public String getRating() {
            return rating;
        }

        public String getReviewText() {
            return reviewText;
        }
    }

    private String title;
    private String overview;
    private String mediaType;
    private String videoId;
    private String genres;
    private String releaseYear;
    private String videoIdType;
    private String posterPath;
    private String backdropPath;
    private ArrayList<CardDataModel> recommendedPicks;
    private ArrayList<ReviewsDataModel> reviews;
    private ArrayList<CastDataModel> cast;
    private JSONObject data;
    private JSONObject detailsJSON;

    public DetailsPageDataModel(JSONObject data, String mediaType) {
        try {
            this.data = data;
            this.detailsJSON = (JSONObject) this.data.get("DETAILS");
            this.title = this.detailsJSON.getString("title");
            this.overview = this.detailsJSON.getString("overview");
            this.posterPath = this.detailsJSON.getString("poster_path");
            this.backdropPath = this.detailsJSON.getString("backdrop_path");
            this.mediaType = mediaType;
            this.setVideoId();
            this.setGenres();
            this.setReleaseYear();
            this.setRecommendedPicks();
            this.setCast();
            this.setReviews();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setVideoId() {
        String dataKey = "VIDEO";
        try {
            JSONObject videoDetails = this.data.getJSONObject(dataKey);
            this.videoId = videoDetails.getString("key");
            this.videoIdType = videoDetails.getString("type");
        } catch (Exception e) {
            this.videoId = "tzkWB85ULJY";
            this.videoIdType = "default";
            e.printStackTrace();
        }
    }

    public void setGenres() {
        try {
            JSONArray genresArray = this.detailsJSON.getJSONArray("genres");
            if (genresArray.length() > 0) {
                String[] genreNames = new String[genresArray.length()];
                for (int i = 0; i < genresArray.length(); i++) {
                    genreNames[i] = genresArray.getString(i);
                }
                this.genres = String.join(", ", genreNames);
            } else {
                this.genres = "";
            }

        } catch (JSONException e) {
            this.genres = "";
            e.printStackTrace();
        }

        this.genres = genres;
    }

    public void setReleaseYear() {
        try {
            this.releaseYear = this.detailsJSON.getString((mediaType.equals("Movie")?"release_date":"first_air_date"));
            this.releaseYear = this.releaseYear.substring(0,4);
        } catch (JSONException e) {
            this.releaseYear = "";
            e.printStackTrace();
        }
    }

    public void setRecommendedPicks() {
        String dataKey = "RECOMMENDED";
        this.recommendedPicks = new ArrayList<>();
        try {
            JSONArray recommendedPicksJSON = this.data.getJSONArray(dataKey);
            for (int i=0; i< Math.min(10, recommendedPicksJSON.length()); i++){
                JSONObject currentPick = recommendedPicksJSON.getJSONObject(i);
                currentPick.put("media_type", this.mediaType);
                this.recommendedPicks.add(new CardDataModel(currentPick));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setReviews() {
        this.reviews = new ArrayList<>();
        try {
            JSONArray reviewsJSON = this.data.getJSONArray("REVIEWS");
            for (int i=0; i< Math.min(3, reviewsJSON.length()); i++){
                this.reviews.add(new ReviewsDataModel(reviewsJSON.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setCast() {
        this.cast = new ArrayList<>();
        try {
            JSONArray castJSON = this.data.getJSONArray("CAST");
            for (int i=0; i< Math.min(6, castJSON.length()); i++){
                this.cast.add(new CastDataModel(castJSON.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getVideoId() {
        return videoId;
    }

    public String getGenres() {
        return genres;
    }

    public String getReleaseYear() {
        return releaseYear;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public ArrayList<CardDataModel> getRecommendedPicks() {
        return recommendedPicks;
    }

    public ArrayList<ReviewsDataModel> getReviews() {
        return reviews;
    }

    public ArrayList<CastDataModel> getCast() {
        return cast;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public String getVideoIdType() {
        return videoIdType;
    }
}
