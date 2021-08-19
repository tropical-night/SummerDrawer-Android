package com.example.summerdrawer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder>{
    private ArrayList<Contents> contentListData = null;
    private ArrayList<LikeScrap> likeScrapList = null;
    private Context context;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    boolean mLike, mScrap = false;
    String id = "";

    //아이템 뷰를 저장하는 뷰홀더 클래스
    public class ViewHolder extends RecyclerView.ViewHolder{
        ConstraintLayout contentLayout;
        ImageView contentImg;
        TextView contentTitle, contentCategoryAuthor, contentSummary,
                likeNumTxt, scrapNumTxt;
        ImageView likeImgRV, scrapImgRV;
        // 데이터베이스

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            contentLayout = itemView.findViewById(R.id.contentLayout);
            contentImg = itemView.findViewById(R.id.contentImg);
            contentTitle = itemView.findViewById(R.id.contentTitleRV);
            contentCategoryAuthor = itemView.findViewById(R.id.contentCategoryAuthorRV);
            contentSummary = itemView.findViewById(R.id.contentSummaryRV);
            likeNumTxt = itemView.findViewById(R.id.likeNumTxtRV);
            scrapNumTxt = itemView.findViewById(R.id.scrapNumTxtRV);
            likeImgRV = itemView.findViewById(R.id.likeImgRV);
            scrapImgRV = itemView.findViewById(R.id.scrapImgRV);

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

    RVAdapter(Context context, ArrayList list, ArrayList likeScrap){
        likeScrapList = likeScrap;
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
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        Glide.with(holder.itemView).load(contentListData.get(position).getImg1()).into(holder.contentImg);
        holder.contentTitle.setText(contentListData.get(position).getTitle());
        holder.contentSummary.setText(contentListData.get(position).getSummary());
        holder.contentCategoryAuthor.setText(contentListData.get(position).getCategoryAuthor());

        // 현재 타이틀과 같은 작품 id 찾기
        for(Contents list: contentListData) {
            if(String.valueOf(contentListData.get(position).getTitle()).equals(list.getTitle())){
                id = list.getId();
            }
        }
        // 찾은 아이디로 좋아요/스크랩수 설정해주기
        db.collection("contents").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                if(document.exists()) {
                    holder.likeNumTxt.setText(String.valueOf(document.getData().get("like")));
                    holder.scrapNumTxt.setText(String.valueOf(document.getData().get("scrap")));
                }
            }
        });
        checkLikeScrap(id, holder.likeImgRV, holder.scrapImgRV);
    }

    @Override
    public int getItemCount() {
        return contentListData.size();
    }

    public void fixItem( ArrayList<Contents> contents) {
        contentListData = contents;
        notifyDataSetChanged();
    }

    // 이미 좋아요, 스크랩이 되어있는지 확인하는 함수
    void checkLikeScrap(String id, ImageView like, ImageView scrap) {
        db.collection("users").document(mAuth.getCurrentUser().getUid())
                .collection("myLike").whereEqualTo("id", id)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    if (id.equals(document.getData().get("id"))) {
                        mLike = true;
                    } else mLike = false;
                }
                if (!mLike) like.setImageResource(R.drawable.unlike);
                else like.setImageResource(R.drawable.like);
                mLike = false;
            }
        });

        db.collection("users").document(mAuth.getCurrentUser().getUid())
                .collection("myScrap").whereEqualTo("id", id)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    if (id.equals(document.getData().get("id"))) {
                        mScrap = true;
                    } else mScrap = false;
                }
                if (!mScrap) scrap.setImageResource(R.drawable.unscrap);
                else scrap.setImageResource(R.drawable.scrap);
                mScrap = false;
            }
        });
    }
}
