package com.example.uscfilms.detailsPage;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.uscfilms.CardDataModel;
import com.example.uscfilms.R;

import java.util.ArrayList;

public class DetailsCarouselRecycleView extends RecyclerView.Adapter<DetailsCarouselRecycleView.DetailsCarouselCard>{
    private ArrayList<CardDataModel> data;
    private Context context;

    public DetailsCarouselRecycleView(Context context) {
        this.context=context;
    }

    public void setDetailsCarouselData(ArrayList<CardDataModel> data){
        this.data=data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DetailsCarouselCard onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DetailsCarouselCard(LayoutInflater.from(parent.getContext()).inflate(R.layout.details_page_recommended_carousel_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DetailsCarouselCard holder, int position) {
        CardDataModel currentDetailsCardData = data.get(position);
        holder.detailsCarouselCardParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newDetailsPageIntent = new Intent(context, DetailsPage.class);
                newDetailsPageIntent.putExtra("item_id", currentDetailsCardData.getID());
                newDetailsPageIntent.putExtra("media_type",currentDetailsCardData.getMediaType());
                context.startActivity(newDetailsPageIntent);

            }
        });

        Glide.with(context).
                asBitmap()
                .load(currentDetailsCardData.getPosterPath())
                .into(holder.detailsCarouselCardImage);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class DetailsCarouselCard extends RecyclerView.ViewHolder{
        private ImageView detailsCarouselCardImage;
        private CardView detailsCarouselCardParent;

        public DetailsCarouselCard(@NonNull View itemView) {
            super(itemView);
            detailsCarouselCardImage = itemView.findViewById(R.id.details_page_recommended_carousel_card_image);
            detailsCarouselCardParent = itemView.findViewById(R.id.details_page_recommended_carousel_card);
        }
    }
}
