package com.example.summerdrawer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;

import java.util.List;

public class TopSliderAdapter extends RecyclerView.Adapter <TopSliderAdapter.TopSliderViewHolder>{
    private List<Contents> sliderItems;
    private ViewPager2 viewPager2;

    private Context context;

    TopSliderAdapter(Context context, List <Contents>sliderItems, ViewPager2 viewPager2) {
        this.context = context;
        this.sliderItems = sliderItems;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public TopSliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TopSliderAdapter.TopSliderViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.pager_item2, parent, false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull TopSliderAdapter.TopSliderViewHolder holder, int position) {
        Glide.with(holder.itemView).load(sliderItems.get(position).getImg2()).into(holder.img_top);
    }

    @Override
    public int getItemCount() {
        return sliderItems.size();
    }

    public class TopSliderViewHolder extends RecyclerView.ViewHolder{
        ImageView img_top;
        public TopSliderViewHolder(@NonNull View itemView) {
            super(itemView);
           img_top = itemView.findViewById(R.id.mostPopularImg);
        }
    }
}
