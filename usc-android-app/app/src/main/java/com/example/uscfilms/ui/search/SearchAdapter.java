package com.example.uscfilms.ui.search;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.uscfilms.CardDataModel;
import com.example.uscfilms.R;
import com.example.uscfilms.detailsPage.DetailsPage;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchItem>{
    private Context context;
    private ArrayList<SearchDataModel> searchData= new ArrayList<>();

    public SearchAdapter(Context context) {
        this.context = context;
    }


    public void setSearchData(ArrayList<SearchDataModel> data){
        this.searchData = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SearchItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SearchItem(LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SearchItem holder, int position) {
        Glide.with(context)
                .asBitmap()
                .load(searchData.get(position).getBackdropPath())
                .into(holder.searchItemImage);

        holder.searchItemMediaType.setText(searchData.get(position).getMediaType());
        holder.searchItemRating.setText(searchData.get(position).getRating());
        holder.searchItemTitle.setText(searchData.get(position).getTitle());
        holder.searchItemYear.setText(searchData.get(position).getYear());
        holder.searchItemParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detailsPageIntent = new Intent(context, DetailsPage.class);
                detailsPageIntent.putExtra("item_id", searchData.get(position).getID());
                detailsPageIntent.putExtra("media_type", searchData.get(position).getMediaType());
                context.startActivity(detailsPageIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return searchData.size();
    }


    class SearchItem extends RecyclerView.ViewHolder{
        private ImageView searchItemImage;
        private TextView searchItemYear;
        private TextView searchItemTitle;
        private TextView searchItemRating;
        private TextView searchItemMediaType;
        private CardView searchItemParent;

        public SearchItem(@NonNull View itemView) {
            super(itemView);
            searchItemImage = itemView.findViewById(R.id.search_item_backdrop);
            searchItemYear = itemView.findViewById(R.id.search_item_release_date);
            searchItemTitle = itemView.findViewById(R.id.search_item_title);
            searchItemMediaType=itemView.findViewById(R.id.search_item_media_type);
            searchItemRating=itemView.findViewById(R.id.search_item_rating_value);
            searchItemParent=itemView.findViewById(R.id.search_item_card);
        }
    }
}
