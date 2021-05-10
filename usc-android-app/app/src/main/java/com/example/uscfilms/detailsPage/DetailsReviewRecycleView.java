package com.example.uscfilms.detailsPage;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uscfilms.R;

import org.json.JSONObject;

import java.util.ArrayList;

public class DetailsReviewRecycleView extends RecyclerView.Adapter<DetailsReviewRecycleView.DetailsReviewCard>{
    private ArrayList<DetailsPageDataModel.ReviewsDataModel> data;
    private Context context;

    public DetailsReviewRecycleView(Context context){this.context=context;}

    public void setDetailsReviewRecycle(ArrayList<DetailsPageDataModel.ReviewsDataModel> data){
        this.data =data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DetailsReviewCard onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DetailsReviewCard(LayoutInflater.from(parent.getContext()).inflate(R.layout.details_page_reviews_card_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DetailsReviewCard holder, int position) {
        DetailsPageDataModel.ReviewsDataModel currentReviewsCard = data.get(position);
        holder.reviewTitle.setText("by "+ currentReviewsCard.getAuthor() + " on "+currentReviewsCard.getDateCreated());
        holder.reviewStar.setText(currentReviewsCard.getRating()+"/5");
        holder.reviewText.setText(currentReviewsCard.getReviewText());
        holder.reviewDetailsCardParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reviewsPageIntent = new Intent(context, ReviewsPageActivity.class);
                reviewsPageIntent.putExtra("rating", currentReviewsCard.getRating());
                reviewsPageIntent.putExtra("author", currentReviewsCard.getAuthor());
                reviewsPageIntent.putExtra("dateCreated", currentReviewsCard.getDateCreated());
                reviewsPageIntent.putExtra("reviewText", currentReviewsCard.getReviewText());
                context.startActivity(reviewsPageIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class DetailsReviewCard extends RecyclerView.ViewHolder{
        private TextView reviewTitle;
        private TextView reviewStar;
        private TextView reviewText;
        private CardView reviewDetailsCardParent;

        public DetailsReviewCard(@NonNull View itemView) {
            super(itemView);
            reviewTitle = itemView.findViewById(R.id.detailsReviewTitle);
            reviewStar = itemView.findViewById(R.id.detailsReviewStarText);
            reviewText = itemView.findViewById(R.id.detailsReviewText);
            reviewDetailsCardParent = itemView.findViewById(R.id.details_page_review_card);
        }
    }


}
