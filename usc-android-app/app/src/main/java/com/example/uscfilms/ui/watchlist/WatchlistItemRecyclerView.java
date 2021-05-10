package com.example.uscfilms.ui.watchlist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.uscfilms.R;
import com.example.uscfilms.CardDataModel;
import com.example.uscfilms.detailsPage.DetailsPage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class WatchlistItemRecyclerView extends RecyclerView.Adapter<WatchlistItemRecyclerView.WatchlistItem>{
    private Context context;
    private ArrayList<CardDataModel> watchlistData = new ArrayList<>();
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor sharedPrefEditor;
    private View parentFragment;

    public WatchlistItemRecyclerView(View parentFragment, Context context, SharedPreferences sharedPref) {
        this.parentFragment = parentFragment;
        this.context = context;
        this.sharedPref = sharedPref;
        this.sharedPrefEditor = this.sharedPref.edit();
        this.setWatchlistData();
    }

    public void setWatchlistData() {
        String watchlistString = sharedPref.getString("watchlist", "{}");
        JSONObject watchlistJSONObj = new JSONObject();
        try {
             watchlistJSONObj = new JSONObject(watchlistString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        int index = 0;
        for (Iterator<String> it = watchlistJSONObj.keys(); it.hasNext(); ) {
            String itemID = it.next();
            try {
                JSONObject dataJSONObj = watchlistJSONObj.getJSONObject(itemID);
                watchlistData.add(new CardDataModel(dataJSONObj));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        notifyDataSetChanged();
    }

    public JSONObject createWatchlistJSONObject(){
        JSONObject watchlistJSONObject = new JSONObject();
        for(int i=0; i<watchlistData.size(); i++){
            try {
                watchlistJSONObject.put(watchlistData.get(i).getID(), watchlistData.get(i).getJSONObject());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return watchlistJSONObject;
    }

    public void swapItems(int fromPosition, int toPosition){
        if (fromPosition < toPosition) {
            watchlistData.add(toPosition + 1, watchlistData.get(fromPosition));
            watchlistData.remove(fromPosition);
        } else{
            watchlistData.add(toPosition, watchlistData.get(fromPosition));
            watchlistData.remove(fromPosition + 1);
        }

//        Collections.swap(watchlistData, fromPosition, toPosition);
        sharedPrefEditor.putString("watchlist", createWatchlistJSONObject().toString());
        sharedPrefEditor.apply();
    }

    @NonNull
    @Override
    public WatchlistItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WatchlistItem(LayoutInflater.from(parent.getContext()).inflate(R.layout.watchlist_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull WatchlistItem holder, int position) {
        updateEmptyWatchlistMessageDisplay();
        Glide.with(context)
                .asBitmap()
                .load(watchlistData.get(position).getPosterPath())
                .into(holder.watchlistItemImage);

        holder.watchlistItemParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detailsPageIntent = new Intent(context, DetailsPage.class);
                detailsPageIntent.putExtra("item_id", watchlistData.get(position).getID());
                detailsPageIntent.putExtra("media_type", watchlistData.get(position).getMediaType());
                context.startActivity(detailsPageIntent);
            }
        });


        holder.watchlistItemMediaType.setText(watchlistData.get(position).getMediaType());
        holder.watchlistItemRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String watchlistString = sharedPref.getString("watchlist", "{}");
                try {
                    Toast.makeText(context, "'" + watchlistData.get(position).getTitle() + "' was " + context.getString(R.string.Removed_from_watchlist), Toast.LENGTH_SHORT).show();
                    watchlistData.remove(position);
                    sharedPrefEditor.putString("watchlist", createWatchlistJSONObject().toString());
                    sharedPrefEditor.apply();
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position,getItemCount());
                    updateEmptyWatchlistMessageDisplay();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void updateEmptyWatchlistMessageDisplay(){
        if (getItemCount() == 0)
            parentFragment.findViewById(R.id.EmptyWatchlistMessage).setVisibility(View.VISIBLE);

        else
            parentFragment.findViewById(R.id.EmptyWatchlistMessage).setVisibility(View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return watchlistData.size();
    }

    public class WatchlistItem extends RecyclerView.ViewHolder{
        private CardView watchlistItemParent;
        private ImageView watchlistItemImage;
        private ImageView watchlistItemRemoveButton;
        private TextView watchlistItemMediaType;

        public WatchlistItem(@NonNull View itemView) {
            super(itemView);
            watchlistItemParent = itemView.findViewById(R.id.watchlist_item_card);
            watchlistItemImage = itemView.findViewById(R.id.watchlist_item_card_image);
            watchlistItemRemoveButton = itemView.findViewById(R.id.watchlist_page_remove_from_watchlist_button);
            watchlistItemMediaType = itemView.findViewById(R.id.watchlist_item_media_type);
        }
    }
}

