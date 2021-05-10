package com.example.uscfilms.detailsPage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.example.uscfilms.MySingleton;
import com.example.uscfilms.R;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.FloatBuffer;

public class DetailsPage extends AppCompatActivity {
    private String mediaType;
    private String itemId;
    private DetailsPageDataModel detailsPageData;
    private RecyclerView detailsRecommendedRV;
    private DetailsCarouselRecycleView detailsCarouselAdapter;
    private RecyclerView detailsCastRV;
    private DetailsPageCastRecycleViewAdapter detailsPageCastRVAdapter;
    private RecyclerView detailsReviewRV;
    private DetailsReviewRecycleView detailsReviewAdapter;
    private ImageView watchListToggleButton;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor sharedPrefEditor;
    private JSONObject watchlist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_page);
        try {
            this.getSupportActionBar().hide();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        Intent detailsActivityIntent = getIntent();
        this.mediaType= detailsActivityIntent.getStringExtra("media_type");
        this.itemId = detailsActivityIntent.getStringExtra("item_id");
        this.detailsReviewRV=findViewById(R.id.detailsReviewRVContainer);
        this.detailsRecommendedRV = findViewById(R.id.detailsRecommendedRecycleView);
        this.detailsCastRV = findViewById(R.id.detailsCastRecycleView);

        // show loading spinner
        findViewById(R.id.details_page_main_scroll_container).setVisibility(View.INVISIBLE);
        findViewById(R.id.detailsPageProgressBar).setVisibility(View.VISIBLE);
        findViewById(R.id.detailsPageProgressBarText).setVisibility(View.VISIBLE);

        this.sharedPref = getSharedPreferences("watchlist_shared_pref", (MODE_PRIVATE));
        this.sharedPrefEditor = sharedPref.edit();
        String watchlistString = sharedPref.getString("watchlist", "{}");
        watchlist = new JSONObject();
        try {
            watchlist = new JSONObject(watchlistString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest detailsPageData = new JsonObjectRequest
                (Request.Method.GET,
                        getString(R.string.network_host) + "/" + this.mediaType + "_page/"+this.itemId,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONObject data = (JSONObject) response.getJSONObject("data");
                                    createDetailsPageData(data);
                                    findViewById(R.id.details_page_main_scroll_container).setVisibility(View.VISIBLE);
                                    findViewById(R.id.detailsPageProgressBar).setVisibility(View.GONE);
                                    findViewById(R.id.detailsPageProgressBarText).setVisibility(View.GONE);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();
                            }
                        });
        MySingleton.getInstance().addToRequestQueue(detailsPageData);
    }

    public void createDetailsPageData(JSONObject data) throws JSONException{

        detailsPageData = new DetailsPageDataModel(data,this.mediaType);
        if(detailsPageData.getVideoIdType().equals("Trailer") && detailsPageData.getVideoId().length()!=0){
            YouTubePlayerView youTubePlayerView = findViewById(R.id.detailsYoutubePlayer);
            youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady(@NotNull YouTubePlayer youTubePlayer) {
                    youTubePlayer.cueVideo(detailsPageData.getVideoId(), 0);
                }
            });

            youTubePlayerView.setVisibility(View.VISIBLE);
            findViewById(R.id.detailsYoutubeBackdrop).setVisibility(View.INVISIBLE);
        }
        else{
            ImageView detailsYoutubeifBackdrop=findViewById(R.id.detailsYoutubeBackdrop);
            Glide.with(this).asBitmap().load(detailsPageData.getBackdropPath()).into(detailsYoutubeifBackdrop);
            findViewById(R.id.detailsYoutubePlayer).setVisibility(View.INVISIBLE);
            detailsYoutubeifBackdrop.setVisibility(View.VISIBLE);


        }





        TextView titleView = findViewById(R.id.detailsTitle);
        titleView.setText(detailsPageData.getTitle());

        TextView overviewView = findViewById(R.id.detailsOverviewText);
        overviewView.setText(detailsPageData.getOverview());

        TextView genresView = findViewById(R.id.detailsGenresText);
        genresView.setText(detailsPageData.getGenres());

        TextView yearView = findViewById(R.id.detailsYearText);
        yearView.setText(detailsPageData.getReleaseYear());

        watchListToggleButton = findViewById(R.id.detailsWatchlistAddWidget);
        if (watchlist.has(itemId)){
            watchListToggleButton.setImageResource(R.drawable.ic_remove_circle);
        }
        watchListToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleWatchlistAdditionDeletion();
            }
        });

        ImageView facebookDetailsView= findViewById(R.id.detailsFacebookLogo);
        facebookDetailsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fbDetailsURL = getString(R.string.share_on_facebook_url)+getString(R.string.youtube_video_url)+detailsPageData.getVideoId() + "&amp;src=sdkpreparse";
                Intent fbDetailsIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(fbDetailsURL));
                startActivity(fbDetailsIntent);
            }
        });

        ImageView twitterDetailsView = findViewById(R.id.detailsTwitterlogo);
        twitterDetailsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String twitterDetailsURL = getString(R.string.twitter_page)+"Check this out!\n"+getString(R.string.youtube_video_url)+detailsPageData.getVideoId();
                Intent twitterDetailsIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(twitterDetailsURL));
                startActivity(twitterDetailsIntent);

            }
        });

        // cast section
        if (detailsPageData.getCast().size() > 0) {
            detailsPageCastRVAdapter = new DetailsPageCastRecycleViewAdapter(this);
            detailsPageCastRVAdapter.setCastData(detailsPageData.getCast());
            detailsCastRV.setAdapter(detailsPageCastRVAdapter);
            detailsCastRV.setLayoutManager(new GridLayoutManager(this, 3));
        } else {
            findViewById(R.id.details_page_cast_container).setVisibility(View.INVISIBLE);
        }


        // review section
        if (detailsPageData.getReviews().size() > 0) {
            detailsReviewAdapter = new DetailsReviewRecycleView(this);
            detailsReviewAdapter.setDetailsReviewRecycle(detailsPageData.getReviews());
            detailsReviewRV.setAdapter(detailsReviewAdapter);
            detailsReviewRV.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        } else {
            findViewById(R.id.details_page_reviews_container).setVisibility(View.INVISIBLE);
        }

        // recommended picks
        if (detailsPageData.getRecommendedPicks().size() > 0) {
            detailsCarouselAdapter = new DetailsCarouselRecycleView(this);
            detailsCarouselAdapter.setDetailsCarouselData(detailsPageData.getRecommendedPicks());
            detailsRecommendedRV.setAdapter(detailsCarouselAdapter);
            detailsRecommendedRV.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        } else {
            findViewById(R.id.details_page_recommended_container).setVisibility(View.INVISIBLE);
        }
    }

    public void handleWatchlistAdditionDeletion(){
        if (watchlist.has(itemId)){
            watchlist.remove(itemId);
            sharedPrefEditor.putString("watchlist", watchlist.toString());
            sharedPrefEditor.apply();
//            Toast.makeText(this, "Removed from Watchlist", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "'" + detailsPageData.getTitle() + "' was " + getString(R.string.Removed_from_watchlist), Toast.LENGTH_SHORT).show();
            watchListToggleButton.setImageResource(R.drawable.ic_watchlist_add_details);
        } else {
            JSONObject watchlistItemJSON = new JSONObject();
            try {
                watchlistItemJSON.put("id", itemId);
                watchlistItemJSON.put("title", detailsPageData.getTitle());
                watchlistItemJSON.put("media_type", mediaType);
                watchlistItemJSON.put("poster_path", detailsPageData.getPosterPath());
                watchlist.put(itemId, watchlistItemJSON);
                sharedPrefEditor.putString("watchlist", watchlist.toString());
                sharedPrefEditor.apply();
//                Toast.makeText(this, "Added to Watchlist", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "'" + detailsPageData.getTitle() + "' was " + getString(R.string.Added_to_watchlist), Toast.LENGTH_SHORT).show();
                watchListToggleButton.setImageResource(R.drawable.ic_remove_circle);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}