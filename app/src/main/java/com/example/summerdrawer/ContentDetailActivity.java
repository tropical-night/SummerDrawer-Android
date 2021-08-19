package com.example.summerdrawer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ContentDetailActivity extends AppCompatActivity {

    Contents content;
    Button btn_logo;

    ImageView imageView;
    TextView contentTagTxt, contentTitleTxt, contentCategoryAuthorTxt, contentStoryTxt;
    ImageView img_mLike, img_mScrap;
    TextView text_detail_like, text_detail_scrap;

    // db 연결
    FirebaseAuth mAuth;
    FirebaseFirestore db;

    boolean mLike, mScrap = false;
    String mLikeId, mScrapId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_detail);
        //클릭한 컨텐츠 정보 받아오기
        content = (Contents)getIntent().getSerializableExtra("content");
        String category = content.getCategory();

        //화면에 가져온 컨텐츠 정보 뿌려주기
        imageView = findViewById(R.id.imageView);
        Glide.with(ContentDetailActivity.this).load(content.getImg1()).into(imageView);

        btn_logo = findViewById(R.id.btn_logo);
        btn_logo.setText(content.getCategory()+"서랍");

        contentTagTxt = findViewById(R.id.contentTagTxt);
        contentTagTxt.setText(content.getTag());

        contentTitleTxt = findViewById(R.id.contentTitleTxt);
        contentTitleTxt.setText(content.getTitle());

        contentCategoryAuthorTxt = findViewById(R.id.contentCategoryAuthorTxt);
        contentCategoryAuthorTxt.setText(content.getCategoryAuthor());

        contentStoryTxt = findViewById(R.id.contentStoryTxt);
        contentStoryTxt.setText(content.getStory());

        text_detail_like = findViewById(R.id.text_detail_like);
        text_detail_scrap = findViewById(R.id.text_detail_scrap);
        img_mLike = findViewById(R.id.img_mLike);
        img_mScrap = findViewById(R.id.img_mScrap);

        // 데이터베이스
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // 이미 좋아요, 스크랩이 되어있는지 확인
        checkLikeScrap();

        // 좋아요수, 스크랩수 할당
        db.collection("contents").document(content.getId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()) {
                        text_detail_like.setText(document.getData().get("like").toString());
                        text_detail_scrap.setText(document.getData().get("scrap").toString());
                    }
                }
            }
        });

    }

    // 이미 좋아요, 스크랩이 되어있는지 확인하는 함수
    void checkLikeScrap() {
        db.collection("users").document(mAuth.getCurrentUser().getUid())
                .collection("myLike").whereEqualTo("id", content.getId())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    if (content.getId().equals(document.getData().get("id"))) {
                        mLike = true;
                    } else mLike = false;
                }
                if (!mLike) img_mLike.setImageResource(R.drawable.unlike);
                else img_mLike.setImageResource(R.drawable.like);
            }
        });

        db.collection("users").document(mAuth.getCurrentUser().getUid())
                .collection("myScrap").whereEqualTo("id", content.getId())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    if (content.getId().equals(document.getData().get("id"))) {
                        mScrap = true;
                    } else mScrap = false;
                }
                if (!mScrap) img_mScrap.setImageResource(R.drawable.unscrap);
                else img_mScrap.setImageResource(R.drawable.scrap);
            }
        });
    }

    // 좋아요를 클릭했을 때 실행되는 함수
    public void setLike(View view){
        // 이미 좋아요가 눌러진 상태인지 확인
        db.collection("users").document(mAuth.getCurrentUser().getUid())
                .collection("myLike").whereEqualTo("id", content.getId())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    if(content.getId().equals(document.getData().get("id"))){
                        mLike = true;
                    }
                    else mLike = false;
                }
                Log.d("like", String.valueOf(mLike));
                if(!mLike) img_mLike.setImageResource(R.drawable.unlike);
                else img_mLike.setImageResource(R.drawable.like);

                if(!mLike) {
                    img_mLike.setImageResource(R.drawable.like);
                    HashMap<Object,String> hashMap = new HashMap<>();
                    hashMap.put("id", content.getId());

                    db.collection("users").document(mAuth.getCurrentUser().getUid()).collection("myLike").document().set(hashMap);
                    db.collection("contents").document(content.getId()).update("like", Integer.parseInt(text_detail_like.getText().toString()) + 1);
                    text_detail_like.setText(String.valueOf(Integer.parseInt(text_detail_like.getText().toString()) + 1));

                    mLike = true;
                }
                else {
                    img_mLike.setImageResource(R.drawable.unlike);
                    db.collection("users").document(mAuth.getCurrentUser().getUid()).collection("myLike").whereEqualTo("id", content.getId()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                mLikeId = document.getId();
                            }
                            db.collection("users").document(mAuth.getCurrentUser().getUid()).collection("myLike").document(mLikeId).delete();
                        }
                    });
                    db.collection("contents").document(content.getId()).update("like", Integer.parseInt(text_detail_like.getText().toString()) - 1);
                    text_detail_like.setText(String.valueOf(Integer.parseInt(text_detail_like.getText().toString()) - 1));
                    mLike = false;
                }
            }
        });

    }

    // 스크랩을 클랙했을 때 실행되는 함수
    public void setScrap(View view){
        // 이미 좋아요가 눌러진 상태인지 확인
        db.collection("users").document(mAuth.getCurrentUser().getUid())
                .collection("myScrap").whereEqualTo("id", content.getId())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    if(content.getId().equals(document.getData().get("id"))){
                        mScrap = true;
                    }
                    else mScrap = false;
                }
                if(!mScrap) img_mScrap.setImageResource(R.drawable.unscrap);
                else img_mScrap.setImageResource(R.drawable.scrap);

                if(!mScrap) {
                    img_mScrap.setImageResource(R.drawable.scrap);
                    HashMap<Object,String> hashMap = new HashMap<>();
                    hashMap.put("id", content.getId());

                    db.collection("users").document(mAuth.getCurrentUser().getUid()).collection("myScrap").document().set(hashMap);
                    db.collection("contents").document(content.getId()).update("scrap", Integer.parseInt(text_detail_scrap.getText().toString()) + 1);
                    text_detail_scrap.setText(String.valueOf(Integer.parseInt(text_detail_scrap.getText().toString()) + 1));

                    mScrap = true;
                }
                else {
                    img_mScrap.setImageResource(R.drawable.unscrap);
                    db.collection("users").document(mAuth.getCurrentUser().getUid()).collection("myScrap").whereEqualTo("id", content.getId()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                mScrapId = document.getId();
                            }
                            db.collection("users").document(mAuth.getCurrentUser().getUid()).collection("myScrap").document(mScrapId).delete();
                        }
                    });
                    db.collection("contents").document(content.getId()).update("scrap", Integer.parseInt(text_detail_scrap.getText().toString()) - 1);
                    text_detail_scrap.setText(String.valueOf(Integer.parseInt(text_detail_scrap.getText().toString()) - 1));
                    mScrap = false;
                }
            }
        });

    }

}