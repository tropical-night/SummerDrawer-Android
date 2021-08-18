package com.example.summerdrawer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ImageButton goProfile, goSearch;

    ViewPager2 viewPager2;
    ArrayList<SliderItems> sliderItems;

    ImageButton btn_goProfile;
    private DrawerLayout drawerLayout;
    private View drawerView;

    ConstraintLayout toLike, toScrap, toMovie, toBook, toWebtoon, toDrama, toMagazine;
    TextView userNameTxt, toLikeTxt, toScrapTxt,
            toMovieTxt, toBookTxt, toWebtoonTxt, toDramaTxt, toMagazineTxt;
    DotsIndicator dots_indicator;
    FirebaseFirestore db;

    //인기 작품 서랍장의 뷰
    ConstraintLayout movieLayout, bookLayout, dramaLayout, webtoonLayout,
            movieContentLayout, bookContentLayout, dramaContentLayout, webtoonContentLayout;
    TextView movieTxt, bookTxt, dramaTxt, webtoonTxt;
    boolean isMOpen, isBOpen, isDOpen, isWOpen = false;

    // 지금 뜨는 신작의 뷰
    ImageView img_latest1, img_latest2, img_latest3, img_latest4;
    TextView title_latest1, title_latest2, title_latest3, title_latest4,
            category_latest1, category_latest2, category_latest3, category_latest4,
            writer_latest1, writer_latest2, writer_latest3, writer_latest4,
            summary_latest1, summary_latest2, summary_latest3, summary_latest4;

    // 데이터 불러오기
    ArrayList<Contents> contentList; // 전체 작품 리스트
    ArrayList<Contents> movieList;
    ArrayList<Contents> bookList;
    ArrayList<Contents> webtoonList;
    ArrayList<Contents> dramaList;
    ArrayList<LikeScrap> likeScrapList; // 좋아요,스크랩 리스트

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        //db에서 메인 화면에 보여줄 데이터 받아오기
        db = FirebaseFirestore.getInstance();
        sliderItems = new ArrayList<>();

        contentList = new ArrayList<>();
        movieList = new ArrayList<>();
        bookList = new ArrayList<>();
        webtoonList = new ArrayList<>();
        dramaList = new ArrayList<>();
        likeScrapList = new ArrayList<>();


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
                    double rating = (double) document.getData().get("rating");
                    String img1 = (String) document.getData().get("img_thumbnail");

                    // 전체 리스트에 저장
                    contentList.add(new Contents(id, title, category, author, date, summary, introduction, story, tag, rating,img1));

                    // 카테고리별 저장
                    if(category.equals("영화")){
                        movieList.add(new Contents(id, title, "영화", author, date, summary, introduction, story, tag, rating,img1));
                    }
                    else if (category.equals("도서")){
                        bookList.add(new Contents(id, title, "도서", author, date, summary, introduction, story, tag, rating,img1));
                    }
                    else if (category.equals("웹툰")){
                        bookList.add(new Contents(id, title, "웹툰", author, date, summary, introduction, story, tag, rating,img1));
                    }
                    else if (category.equals("드라마")){
                        bookList.add(new Contents(id, title, "드라마", author, date, summary, introduction, story, tag, rating,img1));
                    }
                }

                setContentView(R.layout.activity_main);

                //인기 작품 서랍장의 뷰 초기화
                movieLayout = findViewById(R.id.movieLayout);
                movieTxt = findViewById(R.id.movieTxt);
                movieContentLayout = findViewById(R.id.movieContentLayout);

                bookLayout = findViewById(R.id.bookLayout);
                bookTxt = findViewById(R.id.bookTxt);
                bookContentLayout = findViewById(R.id.bookContentLayout);

                webtoonLayout = findViewById(R.id.webtoonLayout);
                webtoonTxt = findViewById(R.id.webtoonTxt);
                webtoonContentLayout = findViewById(R.id.webtoonContentLayout);

                dramaLayout = findViewById(R.id.dramaLayout);
                dramaTxt = findViewById(R.id.dramaTxt);
                dramaContentLayout = findViewById(R.id.dramaContentLayout);

                //인기 작품 서랍장의 뷰 클릭 시 열림/닫힘 이벤트 추가
                movieLayout.setOnClickListener(view->{
                    popularDrawer("영화");
                });
                movieTxt.setOnClickListener(view->{
                    popularDrawer("영화");

                });

                bookLayout.setOnClickListener(view->{
                    popularDrawer("도서");

                });
                bookTxt.setOnClickListener(view->{
                    popularDrawer("도서");

                });

                webtoonLayout.setOnClickListener(view->{
                    popularDrawer("웹툰");
                });
                webtoonTxt.setOnClickListener(view->{
                    popularDrawer("웹툰");
                });

                dramaLayout.setOnClickListener(view->{
                    popularDrawer("드라마");
                });
                dramaTxt.setOnClickListener(view->{
                    popularDrawer("드라마");
                });

                drawerLayout = findViewById(R.id.drawer_layout);
                drawerView = findViewById(R.id.navbar);

                drawerLayout.addDrawerListener(listener);
                drawerView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        return true;
                    }
                });

                //왼쪽 상단의 프로필 이미지 클릭 시
                btn_goProfile = findViewById(R.id.btn_goProfile);
                btn_goProfile.setOnClickListener(view->{
                    Log.d("test", "버튼 누름");
                //네비게이션 드로어 추가
                    drawerLayout.openDrawer(drawerView);
                });

                ImageButton goSearch = findViewById(R.id.btn_goSearch);
                viewPager2 = findViewById(R.id.viewpager);
                dots_indicator = findViewById(R.id.dots_indicator);
                goProfile = findViewById(R.id.btn_goProfile);
                goSearch = findViewById(R.id.btn_goSearch);
                viewPager2 = findViewById(R.id.viewpager);
                dots_indicator = findViewById(R.id.dots_indicator);

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
                        setAdapter();
                    }
                });
                // 어댑터 설정


                // 상단바 프로필 이동
                goProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // 클릭시 이벤트
                        btnOnclick(view);
                    }
                });

                // 상단바 검색 페이지로 이동
                goSearch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // 클릭시 이벤트
                    }
                });

                //사용자 이름 받아와서 설정해주기
                userNameTxt = findViewById(R.id.userNameTxt);
                userNameTxt.setText("김뫄뫄");

                //좋아하는 버튼 클릭 시
                toLike = findViewById(R.id.toLike);
                toLikeTxt = findViewById(R.id.toLikeTxt);
                //좋아하는 작품으로 액티비티 이동
                toLike.setOnClickListener(view->{
                });
                toLikeTxt.setOnClickListener(view->{
                });

                //저장해둔 버튼 클릭 시
                toScrap = findViewById(R.id.toLike);
                toScrapTxt = findViewById(R.id.toLikeTxt);
                //저장해둔 작품으로 액티비티 이동
                toLike.setOnClickListener(view->{
                });
                toLikeTxt.setOnClickListener(view->{
                });

                //영화 버튼 클릭시
                toMovie = findViewById(R.id.toMovie);
                toMovieTxt = findViewById(R.id.toMovieTxt);
                //영화 추천 리스트 액티비티로 이동
                toMovie.setOnClickListener(view->{
                    toContentsList("영화");
                });
                toMovieTxt.setOnClickListener(view->{
                    toContentsList("영화");
                });

                //도서 버튼 클릭시
                toBook = findViewById(R.id.toBook);
                toBookTxt = findViewById(R.id.toBookTxt);
                //도서 추천 리스트 액티비티로 이동
                toBook.setOnClickListener(view->{
                    toContentsList("도서");
                });
                toBookTxt.setOnClickListener(view->{
                    toContentsList("도서");
                });

                //웹툰 버튼 클릭시
                toWebtoon = findViewById(R.id.toWebtoon);
                toWebtoonTxt = findViewById(R.id.toWebtoonTxt);
                //도서 추천 리스트 액티비티로 이동
                toWebtoon.setOnClickListener(view->{
                    toContentsList("웹툰");
                });
                toWebtoonTxt.setOnClickListener(view->{
                    toContentsList("웹툰");
                });

                //드라마 버튼 클릭시
                toDrama = findViewById(R.id.toDrama);
                toDramaTxt = findViewById(R.id.toDramaTxt);
                //드라마 추천 리스트 액티비티로 이동
                toDrama.setOnClickListener(view->{
                    toContentsList("드라마");
                });
                toDramaTxt.setOnClickListener(view->{
                    toContentsList("드라마");
                });

                //읽을거리 버튼 클릭시
                toMagazine = findViewById(R.id.toMagazine);
                toMagazineTxt = findViewById(R.id.toMagazineTxt);
//                //읽을거리 리스트 액티비티로 이동
//                toDrama.setOnClickListener(view->{
//                });
//                toDramaTxt.setOnClickListener(view->{
//                });

                // 최신 작품 4개 불러오기
                img_latest1 = findViewById(R.id.img_latest1);
                img_latest2 = findViewById(R.id.img_latest2);
                img_latest3 = findViewById(R.id.img_latest3);
                img_latest4 = findViewById(R.id.img_latest4);
                title_latest1 = findViewById(R.id.title_latest1);
                title_latest2 = findViewById(R.id.title_latest2);
                title_latest3 = findViewById(R.id.title_latest3);
                title_latest4 = findViewById(R.id.title_latest4);
                category_latest1 = findViewById(R.id.category_latest1);
                category_latest2 = findViewById(R.id.category_latest2);
                category_latest3 = findViewById(R.id.category_latest3);
                category_latest4 = findViewById(R.id.category_latest4);
                writer_latest1 = findViewById(R.id.writer_latest1);
                writer_latest2 = findViewById(R.id.writer_latest2);
                writer_latest3 = findViewById(R.id.writer_latest3);
                writer_latest4 = findViewById(R.id.writer_latest4);
                summary_latest1 = findViewById(R.id.summary_latest1);
                summary_latest2 = findViewById(R.id.summary_latest2);
                summary_latest3 = findViewById(R.id.summary_latest3);
                summary_latest4 = findViewById(R.id.summary_latest4);
                loadLatest();
            }

        });

    }

    //클릭한 버튼에 따라 카테고리를 지정하여 contentList에 넘겨주는 함수
    void toContentsList(String category){
        Intent toList = new Intent(this, ContentsListActivity.class);
        toList.putExtra("content", category);
        startActivity(toList);
    }

    DrawerLayout.DrawerListener listener = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
            //슬라이드 했을 때
        }

        @Override
        public void onDrawerOpened(@NonNull View drawerView) {
            //Drawer가 오픈된 상황일 때 호출
        }

        @Override
        public void onDrawerClosed(@NonNull View drawerView) {
            //닫힌 상황일 때 호출
        }

        @Override
        public void onDrawerStateChanged(int newState) {
            //특정 상태가 변경되었을 때 호출
        }
    };

    public void btnOnclick(View view) {
        switch (view.getId()){
            case R.id.btn_goProfile:
                drawerLayout.openDrawer(drawerView);
                break;
            case R.id.btn_goSearch:
                //Intent searchI = new Intent(this, SearchActivity);
                break;
        }
    }

    // 태그리스트를 하나의 string으로 바꿔주는 함수
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

    // 어댑터 설정
    void setAdapter() {
        viewPager2.setAdapter(new SliderAdapter(sliderItems, viewPager2));

        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        // indicator 설정
        dots_indicator.setViewPager2(viewPager2);

        // 가장자리 아이템들은 크기 작게
        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(10));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                TextView title = page.findViewById(R.id.text_title_today);
                TextView category = page.findViewById(R.id.text_category_today);
                TextView author = page.findViewById(R.id.text_author_today);
                TextView bar = page.findViewById(R.id.text_bar_today);
                TextView desc = page.findViewById(R.id.text_desc_today);
                TextView tag = page.findViewById(R.id.text_tag_today);

                // 현재 페이지의 뷰만 나타나게 함
                if(position == 1 || position == -1) {
                    title.setVisibility(View.INVISIBLE);
                    category.setVisibility(View.INVISIBLE);
                    author.setVisibility(View.INVISIBLE);
                    bar.setVisibility(View.INVISIBLE);
                    desc.setVisibility(View.INVISIBLE);
                    tag.setVisibility(View.INVISIBLE);
                }
                else {
                    title.setVisibility(View.VISIBLE);
                    category.setVisibility(View.VISIBLE);
                    author.setVisibility(View.VISIBLE);
                    bar.setVisibility(View.VISIBLE);
                    desc.setVisibility(View.VISIBLE);
                    tag.setVisibility(View.VISIBLE);
                }
            }
        });

        viewPager2.setPageTransformer(compositePageTransformer);
    }

    //인기 작품서랍장 열기/닫기
    void popularDrawer(String category){
        Animation ani = new AlphaAnimation(0, 1);
        ani.setDuration(1000);
        switch (category){
            case "영화":
                if(isMOpen){
                    //영화 서랍 닫기
                    movieContentLayout.setVisibility(View.GONE);
                    isMOpen=false;
                }else{
                    //영화 서랍은 열고
                    movieContentLayout.setVisibility(View.VISIBLE);
                    movieContentLayout.setAnimation(ani);
                    isMOpen=true;

                    //나머지는 닫기
                    bookContentLayout.setVisibility(View.GONE);
                    isBOpen = false;
                    webtoonContentLayout.setVisibility(View.GONE);
                    isWOpen = false;
                    dramaContentLayout.setVisibility(View.GONE);
                    isDOpen = false;
                }
                break;
            case "도서":
                if(isBOpen){
                    //도서 서랍 닫기
                    bookContentLayout.setVisibility(View.GONE);
                    isBOpen=false;
                }else{
                    //도서 서랍은 열고
                    bookContentLayout.setVisibility(View.VISIBLE);
                    bookContentLayout.setAnimation(ani);
                    isBOpen=true;
                    //나머지는 닫기
                    movieContentLayout.setVisibility(View.GONE);
                    isMOpen = false;
                    webtoonContentLayout.setVisibility(View.GONE);
                    isWOpen = false;
                    dramaContentLayout.setVisibility(View.GONE);
                    isDOpen = false;
                }
                break;
            case "웹툰":
                if(isWOpen){
                    //웹툰 서랍 닫기
                    webtoonContentLayout.setVisibility(View.GONE);
                    isWOpen=false;
                }else{
                    //웹툰 서랍은 열고
                    webtoonContentLayout.setVisibility(View.VISIBLE);
                    isWOpen=true;
                    webtoonContentLayout.setAnimation(ani);
                    //나머지는 닫기
                    movieContentLayout.setVisibility(View.GONE);
                    isMOpen = false;
                    bookContentLayout.setVisibility(View.GONE);
                    isBOpen = false;
                    dramaContentLayout.setVisibility(View.GONE);
                    isDOpen = false;
                }
                break;
            case "드라마":
                if(isDOpen){
                    //드라마 서랍 닫기
                    dramaContentLayout.setVisibility(View.GONE);
                    isDOpen=false;
                    dramaContentLayout.setAnimation(ani);
                }else{
                    //도서 서랍은 열고
                    dramaContentLayout.setVisibility(View.VISIBLE);
                    isDOpen=true;
                    //나머지는 닫기
                    movieContentLayout.setVisibility(View.GONE);
                    isMOpen = false;
                    bookContentLayout.setVisibility(View.GONE);
                    isBOpen = false;
                    webtoonContentLayout.setVisibility(View.GONE);
                    isWOpen = false;

                }
                break;
        }

    }

    // 최신 작품 4개 불러오기
    @SuppressLint("SetTextI18n")
    void loadLatest(){
        Glide.with(MainActivity.this).load(contentList.get(0).getImg1()).into(img_latest1);
        title_latest1.setText(contentList.get(0).getTitle());
        category_latest1.setText(contentList.get(0).getCategory() + " | ");
        writer_latest1.setText(contentList.get(0).getAuthor());
        summary_latest1.setText(contentList.get(0).getSummary());

        Glide.with(MainActivity.this).load(contentList.get(1).getImg1()).into(img_latest2);
        title_latest2.setText(contentList.get(1).getTitle());
        category_latest2.setText(contentList.get(1).getCategory() + " | ");
        writer_latest2.setText(contentList.get(1).getAuthor());
        summary_latest2.setText(contentList.get(1).getSummary());

        Glide.with(MainActivity.this).load(contentList.get(2).getImg1()).into(img_latest3);
        title_latest3.setText(contentList.get(2).getTitle());
        category_latest3.setText(contentList.get(2).getCategory() + " | ");
        writer_latest3.setText(contentList.get(2).getAuthor());
        summary_latest3.setText(contentList.get(2).getSummary());

        Glide.with(MainActivity.this).load(contentList.get(3).getImg1()).into(img_latest4);
        title_latest4.setText(contentList.get(3).getTitle());
        category_latest4.setText(contentList.get(3).getCategory() + " | ");
        writer_latest4.setText(contentList.get(3).getAuthor());
        summary_latest4.setText(contentList.get(3).getSummary());
    }
}