package com.example.summerdrawer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.util.ArrayList;

public class ContentsListActivity extends AppCompatActivity{
    public static Activity listActivity;
    String content;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    ArrayList<Contents> contentList = new ArrayList<>();
    ArrayList<Contents> movieList = new ArrayList<>();
    ArrayList<Contents> movieListLike = new ArrayList<>();
    ArrayList<Contents> bookList = new ArrayList<>();
    ArrayList<Contents> bookListLike = new ArrayList<>();
    ArrayList<Contents> webtoonList = new ArrayList<>();
    ArrayList<Contents> webtoonListLike = new ArrayList<>();
    ArrayList<Contents> dramaList = new ArrayList<>();
    ArrayList<Contents> dramaListLike = new ArrayList<>();
    ArrayList<LikeScrap> likeScrapList = new ArrayList<>(); // 좋아요,스크랩 리스트

    Button btn_logo, btn_arrange;
    ImageButton btn_goProfile, btn_goSearch;
    private DrawerLayout drawerLayout;
    private View drawerView;
    boolean arrangeState = false; // false면 최신순, true면 인기순

    ConstraintLayout toLike, toScrap, toMovie, toBook, toWebtoon, toDrama, toMagazine;
    TextView userNameTxt, toLikeTxt, toScrapTxt,
            toMovieTxt, toBookTxt, toWebtoonTxt, toDramaTxt, toMagazineTxt;

    RecyclerView contentListRV;
    RVAdapter adapter;

    // 이미지 슬라이더
    ViewPager2 viewPager2;
    DotsIndicator dots_indicator;
    ImageView mostPopularImg;
    ArrayList<Contents> sliderItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contents_list);
        listActivity = ContentsListActivity.this;

        content = getIntent().getStringExtra("content");

        contentList = (ArrayList<Contents>)getIntent().getSerializableExtra("allContents");
        movieList = (ArrayList<Contents>) getIntent().getSerializableExtra("movieList");
        bookList = (ArrayList<Contents>) getIntent().getSerializableExtra("bookList");
        webtoonList = (ArrayList<Contents>) getIntent().getSerializableExtra("webtoonList");
        dramaList = (ArrayList<Contents>) getIntent().getSerializableExtra("dramaList");
        likeScrapList = (ArrayList<LikeScrap>) getIntent().getSerializableExtra("likeScrapList");

        // 슬라이더
        sliderItems = new ArrayList<>();
        viewPager2 = findViewById(R.id.viewpager2);
        dots_indicator = findViewById(R.id.dots_indicator1);

        // 인기순으로 정렬
        setLikeList();

        switch(content) {
            case "영화":
                adapter = new RVAdapter(this, movieList, likeScrapList);
                setAdapter(movieList);
                break;
            case "도서":
                adapter = new RVAdapter(this, bookList, likeScrapList);
                setAdapter(bookList);
                break;
            case "웹툰":
                adapter = new RVAdapter(this, webtoonList, likeScrapList);
                setAdapter(webtoonList);
                break;
            case "드라마":
                adapter = new RVAdapter(this, dramaList, likeScrapList);
                setAdapter(dramaList);
        }

        // 최신순/인기순 버튼 눌렀을 때
        btn_arrange = findViewById(R.id.btn_arrange);
        btn_arrange.setOnClickListener(view -> {
            if(!arrangeState) {
                btn_arrange.setText("인기순");
                switch(content) {
                    case "영화":
                        adapter.fixItem(movieListLike);
                        break;
                    case "도서":
                        adapter.fixItem(bookListLike);
                        break;
                    case "웹툰":
                        adapter.fixItem(webtoonListLike);
                        break;
                    case "드라마":
                        adapter.fixItem(dramaListLike);
                }
                arrangeState = true;
            }
            else {
                btn_arrange.setText("최신순");
                switch(content) {
                    case "영화":
                        adapter.fixItem(movieList);
                        break;
                    case "도서":
                        adapter.fixItem(bookList);
                        break;
                    case "웹툰":
                        adapter.fixItem(webtoonList);
                        break;
                    case "드라마":
                        adapter.fixItem(dramaList);
                }
                arrangeState = false;
            }
        });

        //상단의 페이지 이름 수정
        btn_logo = findViewById(R.id.btn_logo);
        btn_logo.setText(content+"서랍");

        //사용자 이름 받아와서 설정해주기
        pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        userNameTxt = findViewById(R.id.userNameTxt);
        userNameTxt.setText(pref.getString("userName", "null"));

        //좋아하는 버튼 클릭 시
        toLike = findViewById(R.id.toLike);
        toLikeTxt = findViewById(R.id.toLikeTxt);
        //좋아하는 작품으로 액티비티 이동
        toLike.setOnClickListener(view->{
            toLikeScrapList("like");
        });
        toLikeTxt.setOnClickListener(view->{
            toLikeScrapList("like");
        });

        //저장해둔 버튼 클릭 시
        toScrap = findViewById(R.id.toScrap);
        toScrapTxt = findViewById(R.id.toScrapTxt);
        //저장해둔 작품으로 액티비티 이동
        toScrap.setOnClickListener(view->{
            toLikeScrapList("scrap");
        });
        toScrapTxt.setOnClickListener(view->{
            toLikeScrapList("scrap");
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
        //읽을거리 리스트 액티비티로 이동
        toMagazine.setOnClickListener(view->{
            Intent toMagazineI = new Intent(this, MagazineListActivity.class);
            toMagazineI.putExtra("allContents", contentList);
            toMagazineI.putExtra("movieList", movieList);
            toMagazineI.putExtra("bookList", bookList);
            toMagazineI.putExtra("webtoonList", webtoonList);
            toMagazineI.putExtra("dramaList", dramaList);
            toMagazineI.putExtra("likeScrapList", likeScrapList);
            startActivity(toMagazineI);
            finish();
        });
        toMagazineTxt.setOnClickListener(view->{
            Intent toMagazineI = new Intent(this, MagazineListActivity.class);
            toMagazineI.putExtra("content", "읽을거리");
            toMagazineI.putExtra("allContents", contentList);
            toMagazineI.putExtra("movieList", movieList);
            toMagazineI.putExtra("bookList", bookList);
            toMagazineI.putExtra("webtoonList", webtoonList);
            toMagazineI.putExtra("dramaList", dramaList);
            toMagazineI.putExtra("likeScrapList", likeScrapList);
            startActivity(toMagazineI);
            finish();
        });



        switch (content){
            case "영화":
                toMovie.setBackgroundResource(R.drawable.drawer_pressed);
                break;
            case "도서":
                toBook.setBackgroundResource(R.drawable.drawer_pressed);
                break;
            case "웹툰":
                toWebtoon.setBackgroundResource(R.drawable.drawer_pressed);
                break;
            case "드라마":
                toDrama.setBackgroundResource(R.drawable.drawer_pressed);
                break;
        }

        contentListRV = findViewById(R.id.contentListRVB);
        contentListRV.setLayoutManager(new LinearLayoutManager(this));

        contentListRV.setAdapter(adapter);

        //네비게이션 드로어 추가
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
            drawerLayout.openDrawer(drawerView);
        });

        //오른쪽 상단의 검색 이미지 클릭 시
        btn_goSearch = findViewById(R.id.btn_goSearch);
        btn_goSearch.setOnClickListener(view->{
            Intent searchI = new Intent(this, SearchActivity.class);
            searchI.putExtra("contentList", contentList);
            startActivity(searchI);
        });

    }

    void toLikeScrapList(String category){
        Intent toList = new Intent(this, LikeScrapActivity.class);
        toList.putExtra("category", category);
        toList.putExtra("allContents", contentList);
        toList.putExtra("movieList", movieList);
        toList.putExtra("bookList", bookList);
        toList.putExtra("webtoonList", webtoonList);
        toList.putExtra("dramaList", dramaList);
        toList.putExtra("likeScrapList", likeScrapList);
        startActivity(toList);
        finish();
    }

    void toContentsList(String category){
        Intent toList = new Intent(this, ContentsListActivity.class);
        toList.putExtra("content", category);
        toList.putExtra("allContents", contentList);
        toList.putExtra("movieList", movieList);
        toList.putExtra("bookList", bookList);
        toList.putExtra("webtoonList", webtoonList);
        toList.putExtra("dramaList", dramaList);
        toList.putExtra("likeScrapList", likeScrapList);
        startActivity(toList);
        finish();
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

    // 좋아요 순으로 정렬하는 함수
    void setLikeList(){
        for(LikeScrap contents: likeScrapList) {
            // 도서 좋아요 순서
            for (Contents bookList : bookList) {
                if (contents.getId().equals(bookList.getId())) {
                    bookListLike.add(bookList);
                }
            }
            // 영화 좋아요 순서
            for (Contents movieList : movieList) {
                if (contents.getId().equals(movieList.getId())) {
                    movieListLike.add(movieList);
                }
            }
            // 웹툰 좋아요 순서
            for (Contents webtoonList : webtoonList) {
                if (contents.getId().equals(webtoonList.getId())) {
                    webtoonListLike.add(webtoonList);
                }
            }
            // 드라마 좋아요 순서
            for (Contents dramaList : dramaList) {
                if (contents.getId().equals(dramaList.getId())) {
                    dramaListLike.add(dramaList);
                }
            }
        }
    }

    void setAdapter(ArrayList<Contents> list) {
        int i = 0;
        for(LikeScrap likeScrap: likeScrapList) {
            // 좋아요 수가 많은 5개 불러오기
            for (Contents contents : list) {
                if (likeScrap.getId().equals(contents.getId())) {
                    if (i == 0) sliderItems.add(0, contents);
                    else if (i == 1) sliderItems.add(1, contents);
                    else if (i == 2) sliderItems.add(2, contents);
                    else if (i == 3) sliderItems.add(3, contents);
                    else if (i == 4) sliderItems.add(4, contents);
                    else break;
                    i++;
                }
            }
        }

        viewPager2.setAdapter(new TopSliderAdapter(this, sliderItems, viewPager2));

        viewPager2.setClipChildren(false);
        //viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        // indicator 설정
        dots_indicator.setViewPager2(viewPager2);
    }

}