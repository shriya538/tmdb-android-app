package com.example.uscfilms.detailsPage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.uscfilms.R;

import java.util.ArrayList;

public class DetailsPageCastRecycleViewAdapter extends RecyclerView.Adapter<DetailsPageCastRecycleViewAdapter.DetailsPageCastCard>{
    private Context context;
    private ArrayList<DetailsPageDataModel.CastDataModel> castData;
    public DetailsPageCastRecycleViewAdapter(Context context) {
        this.context = context;
    }

    public void setCastData(ArrayList<DetailsPageDataModel.CastDataModel> castData) {
        this.castData = castData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DetailsPageCastCard onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DetailsPageCastCard(LayoutInflater.from(parent.getContext()).inflate(R.layout.details_page_cast_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DetailsPageCastCard holder, int position) {
        DetailsPageDataModel.CastDataModel currentCastItem = castData.get(position);
        Glide.with(context)
                .asBitmap()
                .load(currentCastItem.getProfileImage())
                .into(holder.castCardImage);

        holder.castCardName.setText(currentCastItem.getName());
    }

    @Override
    public int getItemCount() {
        return castData.size();
    }

    public class DetailsPageCastCard extends RecyclerView.ViewHolder{
        private CardView castCardParent;
        private ImageView castCardImage;
        private TextView castCardName;

        public DetailsPageCastCard(@NonNull View itemView) {
            super(itemView);
            castCardParent = itemView.findViewById(R.id.details_page_cast_card);
            castCardImage = itemView.findViewById(R.id.details_page_card_cast_image);
            castCardName = itemView.findViewById(R.id.details_page_card_cast_name);
        }
    }
}
