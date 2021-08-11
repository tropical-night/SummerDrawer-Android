package com.example.summerdrawer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

public class RankAdapter extends RecyclerView.Adapter <RankAdapter.RankViewHolder> implements Filterable {

    private List<RankItems> rankItems;
    private List<RankItems> filterList;
    private List<RankItems> unFilterList;
    private ViewPager2 viewPager2;
    Filter listFilter;

    RankAdapter(List<RankItems> rankItems, ViewPager2 viewPager2) {
        this.rankItems = rankItems;
        this.viewPager2 = viewPager2;
        filterList = rankItems;
        unFilterList = rankItems;
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
        holder.title.setText(filterList.get(position).getTitle());
        holder.category.setText(filterList.get(position).getCategory());
        holder.author.setText(filterList.get(position).getAuthor());
        holder.desc.setText(filterList.get(position).getDesc());
        holder.setImage(filterList.get(position));
    }


    @Override
    public int getItemCount() {
        return filterList.size();
    }

    @Override
    public Filter getFilter() {
        if (listFilter == null) {
            listFilter = new ListFilter() ;
        }

        return listFilter ;
    }

    public class RankViewHolder extends RecyclerView.ViewHolder {
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

    private class ListFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            String charString = charSequence.toString();

            if(charString.isEmpty()) {filterList = unFilterList;}
            else {
                ArrayList<RankItems> filteringList = new ArrayList<>();
                for(RankItems item : rankItems) {
                    if (item.getCategory() == charString) filteringList.add(item);
                }
                filterList = filteringList;
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filterResults;
            return filterResults;
//            FilterResults results = new FilterResults();

//            if(charSequence == null || charSequence.length() == 0) {
//                results.values = rankItems;
//                results.count = rankItems.size();
//            } else {
//                ArrayList<RankItems> itemList = new ArrayList<RankItems>() ;
//
//                for(RankItems item : rankItems) {
//                    if (item.getCategory() == "웹툰") itemList.add(item);
//                }
//                results.values = itemList;
//                results.count = itemList.size();
//            }
//            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            filterList = (ArrayList<RankItems>)filterResults.values;

            if(filterResults.count > 0){
                notifyDataSetChanged();
            }
        }
    }
}
