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

public class MagazineSlider extends RecyclerView.Adapter <MagazineSlider.MagazineViewHolder>{
    private List<String> imgItems;
    private ViewPager2 viewPager2;

    private Context context;

    MagazineSlider(Context context, List<String>imgItems, ViewPager2 viewPager2) {
        this.context = context;
        this.imgItems = imgItems;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public MagazineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MagazineSlider.MagazineViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.pager_item2, parent, false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull MagazineSlider.MagazineViewHolder holder, int position) {
        if(imgItems.get(position).equals("lost_brother")) {
            holder.img_top.setImageResource(R.drawable.lostbrother_top);
        }
        else if(imgItems.get(position).equals("innocuousperson")){
            holder.img_top.setImageResource(R.drawable.innocuousperson_top);
        }
        else if(imgItems.get(position).equals("wannabeyou")){
            holder.img_top.setImageResource(R.drawable.wannabeyou_top);
        }
    }

    @Override
    public int getItemCount() {
        return imgItems.size();
    }

    public class MagazineViewHolder extends RecyclerView.ViewHolder{
        ImageView img_top;
        public MagazineViewHolder(@NonNull View itemView) {
            super(itemView);
            img_top = itemView.findViewById(R.id.mostPopularImg);
        }
    }
}