package com.example.uscfilms.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.uscfilms.CardDataModel;
import com.example.uscfilms.detailsPage.DetailsPage;
import com.example.uscfilms.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CarouselRecycleView extends RecyclerView.Adapter<CarouselRecycleView.CarouselCard> {
    private Context context;
    private ArrayList<CardDataModel> carouselData = new ArrayList<>();
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor sharedPrefEditor;
    private JSONObject watchlist;

    public CarouselRecycleView(Context context, SharedPreferences sharedPref) {
//        super();
        this.context = context;
        this.sharedPref = sharedPref;
        this.sharedPrefEditor = this.sharedPref.edit();
//        this.carouselData=carouselData;

    }

    public void setCarouselData(ArrayList<CardDataModel> data) {
        this.carouselData = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CarouselCard onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CarouselCard(LayoutInflater.from(parent.getContext()).inflate(R.layout.carousel_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CarouselCard holder, int position) {
        CardDataModel currentCardData = carouselData.get(position);
        holder.carouselCardParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detailsPageIntent = new Intent(context, DetailsPage.class);
                detailsPageIntent.putExtra("item_id", currentCardData.getID());
                detailsPageIntent.putExtra("media_type", currentCardData.getMediaType());
                context.startActivity(detailsPageIntent);
            }
        });

        Glide.with(context)
                .asBitmap()
                .load(currentCardData.getPosterPath())
                .into(holder.carouselCardImage);

        holder.carouselCardDot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String watchlistString = sharedPref.getString("watchlist", "{}");
                try {
                    watchlist = new JSONObject(watchlistString);
                } catch (JSONException e) {
                    e.printStackTrace();
                    watchlist = new JSONObject();
                }
                holder.carouselCardMenu.show();
                holder.carouselCardMenu
                        .getMenu()
                        .findItem(R.id.home_page_carousel_card_menu_add_to_watchlist)
                        .setTitle((watchlist.has(currentCardData.getID()))?context.getString(R.string.Remove_from_watchlist):context.getString(R.string.Add_to_watchlist));
                holder.carouselCardMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.home_page_carousel_card_menu_open_in_TMDB:
                                Toast.makeText(context, "Opening in Browser", Toast.LENGTH_SHORT).show();
                                String tmdbURL = (currentCardData.getMediaType().equals("Movie"))?context.getString(R.string.tmdb_movie_page):context.getString(R.string.tmdb_tv_page);
                                tmdbURL = tmdbURL + currentCardData.getID();
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(tmdbURL));
                                context.startActivity(intent);
                                break;

                            case R.id.home_page_carousel_card_menu_share_on_fb:
                                Toast.makeText(context, "Sharing to Facebook", Toast.LENGTH_SHORT).show();
                                String fbURL = context.getString(R.string.share_on_facebook_url)+context.getString(R.string.youtube_video_url)+currentCardData.getVideoKey() + "&amp;src=sdkpreparse";
                                Intent fb_intent = new Intent(Intent.ACTION_VIEW, Uri.parse(fbURL));
                                context.startActivity(fb_intent);
                                break;

                            case R.id.home_page_carousel_card_menu_share_on_twitter:
                                Toast.makeText(context, "Sharing to Twitter", Toast.LENGTH_SHORT).show();
                                String twitterShareURL = context.getString(R.string.twitter_page)+"Check this out!\n"+context.getString(R.string.youtube_video_url)+currentCardData.getVideoKey();
                                Intent twitter_intent = new Intent(Intent.ACTION_VIEW, Uri.parse(twitterShareURL));
                                context.startActivity(twitter_intent);
                                break;


                            case R.id.home_page_carousel_card_menu_add_to_watchlist:
                                try {
                                    if (watchlist.has(currentCardData.getID())){
                                        watchlist.remove(currentCardData.getID());
                                        sharedPrefEditor.putString("watchlist", watchlist.toString());
                                        sharedPrefEditor.apply();
                                        Toast.makeText(context, "'" + currentCardData.getTitle() + "' was " + context.getString(R.string.Removed_from_watchlist), Toast.LENGTH_SHORT).show();
                                    } else {
                                        watchlist.put(currentCardData.getID(), currentCardData.getJSONObject());
                                        sharedPrefEditor.putString("watchlist", watchlist.toString());
                                        sharedPrefEditor.apply();
                                        Toast.makeText(context, "'" + currentCardData.getTitle() + "' was " + context.getString(R.string.Added_to_watchlist), Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                break;
                        }
                        return false;
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return carouselData.size();
    }

    public class CarouselCard extends RecyclerView.ViewHolder {

        private ImageView carouselCardImage;
        private CardView carouselCardParent;
        private TextView carouselCardDot;
        private PopupMenu carouselCardMenu;


        public CarouselCard(@NonNull View itemView) {
            super(itemView);
            carouselCardImage = itemView.findViewById(R.id.carousel_item_image);
            carouselCardParent = itemView.findViewById(R.id.carousel_card);
            carouselCardDot = itemView.findViewById(R.id.carousel_card_menu_button);
            carouselCardMenu = new PopupMenu(context, carouselCardDot);
            carouselCardMenu.inflate(R.menu.home_page_carousel_card_menu);
        }
    }
}
