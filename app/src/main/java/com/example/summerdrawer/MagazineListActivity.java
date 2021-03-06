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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.util.ArrayList;

public class MagazineListActivity extends AppCompatActivity {
    String content;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    ArrayList<Contents> contentList = new ArrayList<>();
    ArrayList<Contents> movieList = new ArrayList<>();
    ArrayList<Contents> bookList = new ArrayList<>();
    ArrayList<Contents> webtoonList = new ArrayList<>();
    ArrayList<Contents> dramaList = new ArrayList<>();
    ArrayList<LikeScrap> likeScrapList = new ArrayList<>(); // 좋아요,스크랩 리스트

    ImageButton btn_goProfile, btn_goSearch;

    TextView userNameTxt, toLikeTxt, toScrapTxt,
            toMovieTxt, toBookTxt, toWebtoonTxt, toDramaTxt, toMagazineTxt;
    ConstraintLayout toLike, toScrap, toMovie, toBook, toWebtoon, toDrama, toMagazine;

    // 이미지 슬라이더
    ViewPager2 viewPager2;
    DotsIndicator dots_indicator;
    ImageView mostPopularImg;
    ArrayList<Contents> sliderItems;

    RecyclerView contentListRVM, contentListRVB, contentListRVW, contentListRVD;
    RVAdapter2 adapterM, adapterB, adapterW, adapterD;

    private DrawerLayout drawerLayout;
    private View drawerView;

    TextView textView34;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magazine_list);

        content = getIntent().getStringExtra("content");

        contentList = (ArrayList<Contents>)getIntent().getSerializableExtra("allContents");
        movieList = (ArrayList<Contents>) getIntent().getSerializableExtra("movieList");
        bookList = (ArrayList<Contents>) getIntent().getSerializableExtra("bookList");
        webtoonList = (ArrayList<Contents>) getIntent().getSerializableExtra("webtoonList");
        dramaList = (ArrayList<Contents>) getIntent().getSerializableExtra("dramaList");
        likeScrapList = (ArrayList<LikeScrap>) getIntent().getSerializableExtra("likeScrapList");

        textView34 = findViewById(R.id.textView34);
        textView34.setOnClickListener(view->{
            Intent m = new Intent(this, MagazineActivity.class);
            startActivity(m);
        });
        // 슬라이더
        sliderItems = new ArrayList<>();
        viewPager2 = findViewById(R.id.viewpager2);
        dots_indicator = findViewById(R.id.dots_indicator1);
        setAdapter();

        // 어댑터 생성
        contentListRVM = findViewById(R.id.contentListRVM);
        ArrayList<String> magazineM = new ArrayList<>();
        magazineM.add("'걸캅스', 개인‘들’이 연대하면 세상은 바뀐다");
        magazineM.add("'걸캅스', 개인‘들’이 연대하면 세상은 바뀐다");
        magazineM.add("'걸캅스', 개인‘들’이 연대하면 세상은 바뀐다");
        adapterM = new RVAdapter2(this, magazineM, R.drawable.girlcaps);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        contentListRVM.setLayoutManager(linearLayoutManager);
        contentListRVM.setAdapter(adapterM);

        // 어댑터 생성
        contentListRVB = findViewById(R.id.contentListRVB);
        ArrayList<String> magazineB = new ArrayList<>();
        magazineB.add("밝은 밤, 영원히 잊히지 않을 이야기 \n" +
                "다른 이의 후기 ");
        magazineB.add("밝은 밤, 영원히 잊히지 않을 이야기 \n" +
                "다른 이의 후기 ");
        magazineB.add("밝은 밤, 영원히 잊히지 않을 이야기 \n" +
                "다른 이의 후기 ");
        adapterB = new RVAdapter2(this, magazineB, R.drawable.brightnight);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this);
        linearLayoutManager2.setOrientation(RecyclerView.HORIZONTAL);
        contentListRVB.setLayoutManager(linearLayoutManager2);
        contentListRVB.setAdapter(adapterB);

        // 어댑터 생성
        contentListRVW = findViewById(R.id.contentListRVW);
        ArrayList<String> magazineW = new ArrayList<>();
        magazineW.add("오빠가 사라졌다, 경선 작가 인터뷰 \n" +
                "제작 인터뷰");
        magazineW.add("오빠가 사라졌다, 경선 작가 인터뷰 \n" +
                "제작 인터뷰");
        magazineW.add("오빠가 사라졌다, 경선 작가 인터뷰 \n" +
                "제작 인터뷰");
        adapterW = new RVAdapter2(this, magazineW, R.drawable.lostbrother);
        LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(this);
        linearLayoutManager3.setOrientation(RecyclerView.HORIZONTAL);
        contentListRVW.setLayoutManager(linearLayoutManager3);
        contentListRVW.setAdapter(adapterW);

        // 어댑터 생성
        contentListRVD = findViewById(R.id.contentListRVD);
        ArrayList<String> magazineD = new ArrayList<>();
        magazineD.add("검블유의 시작, 여자 셋이 일로 싸우는 내용 \n" +
                "제작 인터뷰");
        magazineD.add("검블유의 시작, 여자 셋이 일로 싸우는 내용 \n" +
                "제작 인터뷰");
        magazineD.add("검블유의 시작, 여자 셋이 일로 싸우는 내용 \n" +
                "제작 인터뷰");
        adapterD = new RVAdapter2(this, magazineD, R.drawable.www);
        LinearLayoutManager linearLayoutManager4 = new LinearLayoutManager(this);
        linearLayoutManager4.setOrientation(RecyclerView.HORIZONTAL);
        contentListRVD.setLayoutManager(linearLayoutManager4);
        contentListRVD.setAdapter(adapterD);

        //사용자 이름 받아와서 설정해주기
        pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        userNameTxt = findViewById(R.id.userNameTxt);
        userNameTxt.setText(pref.getString("userName", "null"));

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


        toMagazine.setBackgroundResource(R.drawable.drawer_pressed);
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

    void setAdapter() {
        ArrayList<String> name = new ArrayList<>();
        name.add("lost_brother");
        name.add("innocuousperson");
        name.add("wannabeyou");

        viewPager2.setAdapter(new MagazineSlider(this, name, viewPager2));

        viewPager2.setClipChildren(false);
        //viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        // indicator 설정
        dots_indicator.setViewPager2(viewPager2);
    }

}