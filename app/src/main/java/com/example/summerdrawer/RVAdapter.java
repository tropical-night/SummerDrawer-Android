package com.example.summerdrawer;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder>{
    private ArrayList<Contents> contentListData = null;
    private Context context;

    //아이템 뷰를 저장하는 뷰홀더 클래스
    public class ViewHolder extends RecyclerView.ViewHolder{
        ConstraintLayout contentLayout;
        ImageView contentImg;
        TextView contentTitle, contentCategoryAuthor, contentSummary,
                likeNumTxt, scrapNumTxt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            contentLayout = itemView.findViewById(R.id.contentLayout);
            contentImg = itemView.findViewById(R.id.contentImg);
            contentTitle = itemView.findViewById(R.id.contentTitleRV);
            contentCategoryAuthor = itemView.findViewById(R.id.contentCategoryAuthorRV);
            contentSummary = itemView.findViewById(R.id.contentSummaryRV);
            likeNumTxt = itemView.findViewById(R.id.likeNumTxtRV);
            scrapNumTxt = itemView.findViewById(R.id.scrapNumTxtRV);

            itemView.setOnClickListener(view->{
                int position = getAdapterPosition();
                if(position!=RecyclerView.NO_POSITION){
                    Intent contentDetailI = new Intent(context, ContentDetailActivity.class);
                    contentDetailI.putExtra("content", contentListData.get(position));
                    context.startActivity(contentDetailI);
                }
            });
        }
    }

    RVAdapter(Context context, ArrayList list){
        contentListData = list;
        this.context = context;
    }

    @NonNull
    @Override
    public RVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.rv_content_list, parent, false) ;
        RVAdapter.ViewHolder vh = new RVAdapter.ViewHolder(view) ;

        return vh ;
    }

    @Override
    public void onBindViewHolder(@NonNull RVAdapter.ViewHolder holder, int position) {
        //이미지 받아오는 방식에 따라 변경
        Glide.with(holder.itemView).load(contentListData.get(position).getImg1()).into(holder.contentImg);
        holder.contentTitle.setText(contentListData.get(position).getTitle());
        holder.contentSummary.setText(contentListData.get(position).getSummary());
        holder.contentCategoryAuthor.setText(contentListData.get(position).getCategoryAuthor());
        //holder.likeNumTxt.setText(contentListData.get(position).getLike());
        //holder.scrapNumTxt.setText(contentListData.get(position).getScrap());
    }

    @Override
    public int getItemCount() {
        return contentListData.size();
    }
}
