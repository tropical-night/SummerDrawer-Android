package com.example.summerdrawer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SplashActivity extends AppCompatActivity {
    FirebaseFirestore db;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    ArrayList<Contents> contentList = new ArrayList<>(); // 전체 작품 리스트
    ArrayList<Contents> movieList = new ArrayList<>();
    ArrayList<Contents> bookList = new ArrayList<>();
    ArrayList<Contents> webtoonList = new ArrayList<>();
    ArrayList<Contents> dramaList = new ArrayList<>();
    ArrayList<LikeScrap> likeScrapList = new ArrayList<>(); // 좋아요,스크랩 리스트
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //db에서 메인 화면에 보여줄 데이터 받아오기
        db = FirebaseFirestore.getInstance();


        // 날짜순으로 데이터베이스 불러오기(카테고리별로 분류하여 객체에 저장)
        db.collection("contents").orderBy("date", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String id = document.getId();
                    String title = (String) document.getData().get("title");
                    String category = (String) document.getData().get("category");
                    String tag = setTagListToString((String) document.getData().get("tag"));
                    String author = setAuthorToString((String) document.getData().get("author"));
                    String date = setTimestampToString((Timestamp) document.getData().get("date"));
                    String summary = (String) document.getData().get("summary");
                    String story = (String) document.getData().get("story");
                    String introduction = (String) document.getData().get("introduction");
                    String img1 = (String) document.getData().get("img_thumbnail");

                    // 전체 리스트에 저장
                    contentList.add(new Contents(id, title, category, author, date, summary, introduction, story, tag, img1));

                    // 카테고리별 저장
                    if(category.equals("영화")){
                        movieList.add(new Contents(id, title, "영화", author, date, summary, introduction, story, tag, img1));
                    }
                    else if (category.equals("도서")){
                        bookList.add(new Contents(id, title, "도서", author, date, summary, introduction, story, tag, img1));
                    }
                    else if (category.equals("웹툰")){
                        webtoonList.add(new Contents(id, title, "웹툰", author, date, summary, introduction, story, tag, img1));
                    }
                    else if (category.equals("드라마")){
                        dramaList.add(new Contents(id, title, "드라마", author, date, summary, introduction, story, tag, img1));
                    }
                }

                // 좋아요 순으로 데이터베이스 불러오기
                db.collection("contents").orderBy("like", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String id = document.getId();
                            String category = (String) document.getData().get("category");
                            int like = (int) Integer.parseInt(String.valueOf(document.getData().get("like")));
                            int scrap = (int) Integer.parseInt(String.valueOf(document.getData().get("scrap")));
                            likeScrapList.add(new LikeScrap(id, category, like, scrap));
                        }

                        pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
                        editor = pref.edit();

                        boolean isLogin = pref.getBoolean("isLogin", false);

                        if(!isLogin){
                            toLogin();
                        }else{
                            toMain();
                        }
                    }
                });

            }

        });

    }
    //로그인 액티비티로 이동
    void toLogin(){
        Intent toLoginI = new Intent(this, LoginActivity.class);
        toLoginI.putExtra("allContents", contentList);
        toLoginI.putExtra("movieList", movieList);
        toLoginI.putExtra("bookList", bookList);
        toLoginI.putExtra("webtoonList", webtoonList);
        toLoginI.putExtra("dramaList", dramaList);
        toLoginI.putExtra("likeScrapList", likeScrapList);
        startActivity(toLoginI);
    }

    //메인 액티비티로 이동
    void toMain(){
        Intent toMainI = new Intent(this, MainActivity.class);
        toMainI.putExtra("allContents", contentList);
        toMainI.putExtra("movieList", movieList);
        toMainI.putExtra("bookList", bookList);
        toMainI.putExtra("webtoonList", webtoonList);
        toMainI.putExtra("dramaList", dramaList);
        toMainI.putExtra("likeScrapList", likeScrapList);
        startActivity(toMainI);
        finish();
    }

    String setTagListToString(String tagDB){
        String[] tagList = tagDB.split("_");
        StringBuilder tag = new StringBuilder();
        for(int i=0; i<tagList.length; i++ )
        {
            tag.append("#").append(tagList[i]);
            if(i != tagList.length-1) tag.append("   ");
        }
        return tag.toString();
    }

    // 작가리스트를 하나의 string으로 바꿔주는 함수
    String setAuthorToString(String authorDB){
        String[] authorList = authorDB.split("_");
        StringBuilder author = new StringBuilder();

        if(authorList.length > 3) {
            author.append(authorList[0]).append(" 외 " + (authorList.length - 1)).append("명");
        }
        else if(authorList.length == 2){
            author.append(authorList[0]).append(", ").append(authorList[1]);
        }
        else author.append(authorList[0]);
        return author.toString();
    }

    // Timestamp를 String으로 변경해주는 함수
    String setTimestampToString(Timestamp timestamp) {
        Date date = timestamp.toDate();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY년 MM월 dd일");
        String dateString = simpleDateFormat.format(date);

        return dateString;
    }

}