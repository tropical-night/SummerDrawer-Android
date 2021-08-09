package com.example.summerdrawer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import java.util.List;

public class RankAdapter extends RecyclerView.Adapter <RankAdapter.RankViewHolder> {

    private List<RankItems> rankItems;
    private ViewPager2 viewPager2;

    RankAdapter(List<RankItems> rankItems, ViewPager2 viewPager2) {
        this.rankItems = rankItems;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public RankAdapter.RankViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RankAdapter.RankViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.rank_list_item, parent, false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull RankAdapter.RankViewHolder holder, int position) {
        holder.title.setText(rankItems.get(position).getTitle());
        holder.category.setText(rankItems.get(position).getCategory());
        holder.author.setText(rankItems.get(position).getAuthor());
        holder.desc.setText(rankItems.get(position).getDesc());
        holder.setImage(rankItems.get(position));
    }

    @Override
    public int getItemCount() {
        return rankItems.size();
    }

    class RankViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView category;
        public TextView author;
        public TextView desc;
        private ImageView image;

        public RankViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image_rank);
            title = itemView.findViewById(R.id.text_rank_title);
            category = itemView.findViewById(R.id.text_rank_category);
            author = itemView.findViewById(R.id.text_rank_author);
            desc = itemView.findViewById(R.id.text_rank_desc);
        }

        // 이미지 가져올 경우
        void setImage(RankItems rankItems) {
            image.setImageResource(rankItems.getImage());
        }
    }
}
