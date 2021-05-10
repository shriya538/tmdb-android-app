package com.example.uscfilms.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.uscfilms.CardDataModel;
import com.example.uscfilms.Constants;
import com.example.uscfilms.MySingleton;
import com.example.uscfilms.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.ArrayList;

import com.smarteist.autoimageslider.SliderView;

public class HomeFragment extends Fragment {

//    private HomeViewModel homeViewModel;
    private ArrayList<CardDataModel> bannerDataSV;
    private ArrayList<CardDataModel> topRatedRVData;
    private ArrayList<CardDataModel> popularRVData;
    private static String homePageMode = "movie";
    private RecyclerView popularCarouselRV;
    private RecyclerView topRatedCarouselRV;
    private View root;
    private SharedPreferences sharedPref;
    private boolean tvMode = false;
    private TextView movieModeButton;
    private TextView tvModeButton;
//    private int retryLoadingCount = 0;
    private ProgressBar progressBar;
    private TextView progressBarText;
//    private SharedPreferences.Editor sharedPrefEditor;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        root = inflater.inflate(R.layout.fragment_home, container, false);
        popularCarouselRV = root.findViewById(R.id.home_page_popular_recycler_view);
        topRatedCarouselRV = root.findViewById(R.id.home_page_top_rated_recycler_view);
        sharedPref = getActivity().getSharedPreferences("watchlist_shared_pref", Context.MODE_PRIVATE);
//        sharedPrefEditor = sharedPref.edit();
        movieModeButton = root.findViewById(R.id.home_page_movie_mode_button);
        progressBar = root.findViewById(R.id.pBar);
        progressBarText = root.findViewById(R.id.pBarText);
        movieModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                renderMovieMode();
            }
        });

        tvModeButton = root.findViewById(R.id.home_page_tv_mode_button);
        tvModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                renderTVMode();
            }
        });

        renderMovieMode();
        
        return root;
    }

    public void makeLoadingSpinnerVisible(boolean loading){
        if (loading) {
            root.findViewById(R.id.home_page_action_bar).setVisibility(View.INVISIBLE);
            root.findViewById(R.id.home_page_main_scroll_view_container).setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            progressBarText.setVisibility(View.VISIBLE);
        }
        else {
            root.findViewById(R.id.home_page_action_bar).setVisibility(View.VISIBLE);
            root.findViewById(R.id.home_page_main_scroll_view_container).setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            progressBarText.setVisibility(View.GONE);
        }
    }

    public void renderMovieMode(){
        makeLoadingSpinnerVisible(true);
        tvModeButton.setTextColor(getResources().getColor(R.color.details_page_blue));
        movieModeButton.setTextColor(getResources().getColor(R.color.white));
        tvMode = false;
        processPage();
    }

    public void renderTVMode(){
        makeLoadingSpinnerVisible(true);
        movieModeButton.setTextColor(getResources().getColor(R.color.details_page_blue));
        tvModeButton.setTextColor(getResources().getColor(R.color.white));
        tvMode = true;
        processPage();
    }

    public void processPage(){

        TextView homePageFooterTMDB =  root.findViewById(R.id.home_page_footer_powered_by_tmbd);
        homePageFooterTMDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tbdbHomePageIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.tmdb_homepage)));
                startActivity(tbdbHomePageIntent);
            }
        });

//        if (this.retryLoadingCount >= 5){
//            return;
//        }
        JsonObjectRequest homePageData = new JsonObjectRequest
                (Request.Method.GET, getString(R.string.network_host) + "/home_page", null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Toast.makeText(getActivity(), response.get("success").toString(), Toast.LENGTH_SHORT).show();
                            // parsing json data
                            JSONObject data = response.getJSONObject("data");

                            // parsing data for home fragment banners
                            createHomeFragmentData(data);

                            // render banner
                            createHomePageBanner();

                            //render top rated carousel
                            createHomePageCarousels(topRatedRVData, topRatedCarouselRV);
                            createHomePageCarousels(popularRVData, popularCarouselRV);
//                            retryLoadingCount = 0;
                            makeLoadingSpinnerVisible(false);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
//                        retryLoadingCount++;
//                        processPage();
                    }
                });
        homePageData.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                5,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance().addToRequestQueue(homePageData);
    }

    public void createHomeFragmentData(JSONObject data) throws JSONException {
        if (!tvMode){
            JSONArray trendingData = data.getJSONArray("CURRENTLY_PLAYING_MOVIES");
            bannerDataSV = new ArrayList<>();
            for(int i = 0; i < Math.min(trendingData.length(), Constants.BANNER_ITEMS_COUNT); i++){
                bannerDataSV.add(new CardDataModel(trendingData.getJSONObject(i)));
            }

            JSONArray topRatedData = data.getJSONArray("TOP_RATED_MOVIES");
            topRatedRVData = new ArrayList<>();
            for(int i = 0; i < topRatedData.length(); i++){
                topRatedRVData.add(new CardDataModel(topRatedData.getJSONObject(i)));
            }

            JSONArray popularData = data.getJSONArray("POPULAR_MOVIES");
            popularRVData = new ArrayList<>();
            for(int i = 0; i < popularData.length(); i++){
                popularRVData.add(new CardDataModel(popularData.getJSONObject(i)));
            }
        }
        else {
            JSONArray trendingData = data.getJSONArray("TRENDING_TV");
            bannerDataSV = new ArrayList<>();
            for(int i = 0; i < Math.min(trendingData.length(), Constants.BANNER_ITEMS_COUNT); i++){
                bannerDataSV.add(new CardDataModel(trendingData.getJSONObject(i)));
            }

            JSONArray topRatedData = data.getJSONArray("TOP_RATED_TV");
            topRatedRVData = new ArrayList<>();
            for(int i = 0; i < topRatedData.length(); i++){
                topRatedRVData.add(new CardDataModel(topRatedData.getJSONObject(i)));
            }

            JSONArray popularData = data.getJSONArray("POPULAR_TV");
            popularRVData = new ArrayList<>();
            for(int i = 0; i < popularData.length(); i++){
                popularRVData.add(new CardDataModel(popularData.getJSONObject(i)));
            }
        }

    }

    public void createHomePageBanner(){
        SliderAdapter homePageBannerAdapter = new SliderAdapter(getContext(), bannerDataSV);

        // rendering banner data in the slider view adapter

        SliderView bannerSlider = root.findViewById(R.id.home_page_banner_slider_view);
        // below method is used to set auto cycle direction in left to
        // right direction you can change according to requirement.
        bannerSlider.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);

        // below method is used to
        // setadapter to sliderview.
        bannerSlider.setSliderAdapter(homePageBannerAdapter);

        // below method is use to set
        // scroll time in seconds.
        bannerSlider.setScrollTimeInSec(3);

        // to set it scrollable automatically
        // we use below method.
        bannerSlider.setAutoCycle(true);

        // to start autocycle below method is used.
        bannerSlider.startAutoCycle();
    }

    public void createHomePageCarousels(ArrayList<CardDataModel> carouselData, RecyclerView carouselRV){
        CarouselRecycleView carouselAdapter = new CarouselRecycleView(root.getContext(), sharedPref);
        carouselAdapter.setCarouselData(carouselData);
        carouselRV.setAdapter(carouselAdapter);
        carouselRV.setLayoutManager(new LinearLayoutManager(root.getContext(), RecyclerView.HORIZONTAL, false));

    }
}