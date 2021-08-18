package com.example.summerdrawer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder>{
    private ArrayList<Contents> contentListData = null;

    //아이템 뷰를 저장하는 뷰홀더 클래스
    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView contentImg;
        TextView contentTitle, contentCategoryAuthor, contentSummary,
                likeNumTxt, scrapNumTxt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            contentImg = itemView.findViewById(R.id.contentImg);
            contentTitle = itemView.findViewById(R.id.contentTitle);
            contentCategoryAuthor = itemView.findViewById(R.id.contentCategoryAuthor);
            contentSummary = itemView.findViewById(R.id.contentSummary);
            likeNumTxt = itemView.findViewById(R.id.likeNumTxt);
            scrapNumTxt = itemView.findViewById(R.id.scrapNumTxt);
        }
    }

    RVAdapter(ArrayList list){
        contentListData = list;
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
        //holder.contentImg
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
