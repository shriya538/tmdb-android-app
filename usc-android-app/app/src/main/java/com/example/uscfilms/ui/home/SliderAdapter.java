package com.example.uscfilms.ui.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.example.uscfilms.CardDataModel;
import com.example.uscfilms.detailsPage.DetailsPage;
import com.example.uscfilms.R;
import com.smarteist.autoimageslider.SliderViewAdapter;
import jp.wasabeef.glide.transformations.BlurTransformation;

import java.util.ArrayList;
import java.util.List;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderAdapterViewHolder> {

    // list for storing urls of images.
    private final List<CardDataModel> mSliderItems;
    private Context context;

    // Constructor
    public SliderAdapter(Context context, ArrayList<CardDataModel> sliderDataArrayList) {
        this.context = context;
        this.mSliderItems = sliderDataArrayList;
    }

    // We are inflating the slider_layout
    // inside on Create View Holder method.
    @Override
    public SliderAdapterViewHolder onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_page_banner, null);
        return new SliderAdapterViewHolder(inflate);
    }

    // Inside on bind view holder we will
    // set data to item of Slider View.
    @Override
    public void onBindViewHolder(SliderAdapterViewHolder viewHolder, final int position) {

        final CardDataModel sliderItem = mSliderItems.get(position);

        // Glide is use to load image
        // from url in your imageview.
        Glide.with(viewHolder.itemView)
                .load(sliderItem.getPosterPath())
                .fitCenter()
                .into(viewHolder.bannerImage);

        Glide.with(viewHolder.itemView)
                .load(sliderItem.getPosterPath())
                .apply(bitmapTransform(new MultiTransformation<Bitmap>(new BlurTransformation(25), new CenterCrop())))
                .into(viewHolder.bannerBackground);

        viewHolder.bannerParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detailsPageIntent = new Intent(context, DetailsPage.class);
                detailsPageIntent.putExtra("item_id", sliderItem.getID());
                detailsPageIntent.putExtra("media_type", sliderItem.getMediaType());
                context.startActivity(detailsPageIntent);
            }
        });
    }

    // this method will return
    // the count of our list.
    @Override
    public int getCount() {
        return mSliderItems.size();
    }

    static class SliderAdapterViewHolder extends SliderViewAdapter.ViewHolder {
        // Adapter class for initializing
        // the views of our slider view.
        View itemView;
        ImageView bannerImage;
        ImageView bannerBackground;
        View bannerParent;


        public SliderAdapterViewHolder(View itemView) {
            super(itemView);
            bannerImage = itemView.findViewById(R.id.banner_image);
            this.itemView = itemView;
            bannerBackground = itemView.findViewById(R.id.banner_background);
            bannerParent = itemView.findViewById(R.id.home_page_banner_card);
        }
    }
}