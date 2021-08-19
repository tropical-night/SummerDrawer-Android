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
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
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

public class MainActivity extends AppCompatActivity {
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    ImageButton goProfile, goSearch;

    ViewPager2 viewPager2;
    ArrayList<Contents> sliderItems;

    ImageButton btn_goProfile;
    private DrawerLayout drawerLayout;
    private View drawerView;

    ConstraintLayout toLike, toScrap, toMovie, toBook, toWebtoon, toDrama, toMagazine;
    TextView userNameTxt, toLikeTxt, toScrapTxt,
            toMovieTxt, toBookTxt, toWebtoonTxt, toDramaTxt, toMagazineTxt;
    DotsIndicator dots_indicator;

    //인기 작품 서랍장의 뷰
    ConstraintLayout movieLayout, bookLayout, dramaLayout, webtoonLayout,
            movieContentLayout, bookContentLayout, dramaContentLayout, webtoonContentLayout;
    TextView movieTxt, bookTxt, dramaTxt, webtoonTxt;
    ImageView img_movie1, img_movie2, img_movie3, img_book1, img_book2, img_book3, img_webtoon1,
            img_webtoon2, img_webtoon3, img_drama1, img_drama2, img_drama3;
    TextView text_book_title1, text_book_title2, text_book_title3, text_movie_title1, text_movie_title2,
            text_movie_title3, text_webtoon_title1, text_webtoon_title2, text_webtoon_title3,
            text_drama_title1, text_drama_title2, text_drama_title3,
            text_book_desc1, text_book_desc2, text_book_desc3, text_movie_desc1, text_movie_desc2,
            text_movie_desc3, text_webtoon_desc1, text_webtoon_desc2, text_webtoon_desc3,
            text_drama_desc1, text_drama_desc2, text_drama_desc3;
    View view5, view6, view7, view8, view9, view10, view11, view12, view13, view14, view15, view16, view17, view18, view19, view20;
    boolean isMOpen, isBOpen, isDOpen, isWOpen = false;

    // 지금 뜨는 신작의 뷰
    ImageView img_latest1, img_latest2, img_latest3, img_latest4;
    TextView title_latest1, title_latest2, title_latest3, title_latest4,
            category_latest1, category_latest2, category_latest3, category_latest4,
            writer_latest1, writer_latest2, writer_latest3, writer_latest4,
            summary_latest1, summary_latest2, summary_latest3, summary_latest4;

    // 데이터 불러오기
    ArrayList<Contents> contentList = new ArrayList<>(); // 전체 작품 리스트
    ArrayList<Contents> movieList = new ArrayList<>();
    ArrayList<Contents> bookList = new ArrayList<>();
    ArrayList<Contents> webtoonList = new ArrayList<>();
    ArrayList<Contents> dramaList = new ArrayList<>();
    ArrayList<LikeScrap> likeScrapList = new ArrayList<>(); // 좋아요,스크랩 리스트

    private FirebaseAuth firebaseAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sliderItems = new ArrayList<>();

        contentList = (ArrayList<Contents>) getIntent().getSerializableExtra("allContents");
        movieList = (ArrayList<Contents>) getIntent().getSerializableExtra("movieList");
        bookList = (ArrayList<Contents>) getIntent().getSerializableExtra("bookList");
        webtoonList = (ArrayList<Contents>) getIntent().getSerializableExtra("webtoonList");
        dramaList = (ArrayList<Contents>) getIntent().getSerializableExtra("dramaList");
        likeScrapList = (ArrayList<LikeScrap>) getIntent().getSerializableExtra("likeScrapList");

        pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        editor = pref.edit();
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        String userName = pref.getString("userName", "null");
        String userID = pref.getString("userID", "null");
        String password = pref.getString("password", "null");

        //메뉴 탭의 사용자 이름 수정해주기
        userNameTxt = findViewById(R.id.userNameTxt);
        db.collection("users").document(firebaseAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(userName.equals("null")){
                    DocumentSnapshot document = task.getResult();
                    editor.putString("userName", document.getData().get("name").toString());
                    editor.apply();
                    userNameTxt.setText(pref.getString("userName", "null"));
                }else {
                    userNameTxt.setText(userName);
                }
            }
        });

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

        view9 = findViewById(R.id.view9);
        view10 = findViewById(R.id.view10);
        view11 = findViewById(R.id.view11);
        view12 = findViewById(R.id.view12);
        view13 = findViewById(R.id.view13);
        view14 = findViewById(R.id.view14);
        view15 = findViewById(R.id.view15);
        view16 = findViewById(R.id.view16);
        view17 = findViewById(R.id.view17);
        view18 = findViewById(R.id.view18);
        view19 = findViewById(R.id.view19);
        view20 = findViewById(R.id.view20);

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

            goSearch = findViewById(R.id.btn_goSearch);
            viewPager2 = findViewById(R.id.viewpager);
            dots_indicator = findViewById(R.id.dots_indicator);
            goProfile = findViewById(R.id.btn_goProfile);
            viewPager2 = findViewById(R.id.viewpager);
            dots_indicator = findViewById(R.id.dots_indicator);

            // 좋아요 순으로 데이터베이스 불러오기
            setAdapter();

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
            //userNameTxt.setText("김뫄뫄");

            //좋아하는 버튼 클릭 시
            toLike = findViewById(R.id.toLike);
            toLikeTxt = findViewById(R.id.toLikeTxt);
            //좋아하는 작품으로 액티비티 이동
            toLike.setOnClickListener(view->{
            });
            toLikeTxt.setOnClickListener(view->{
            });

            //저장해둔 버튼 클릭 시
            toScrap = findViewById(R.id.toScrap);
            toScrapTxt = findViewById(R.id.toScrapTxt);
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
                toContentsList("영화", movieList);
            });
            toMovieTxt.setOnClickListener(view->{
                toContentsList("영화", movieList);
            });

            //도서 버튼 클릭시
            toBook = findViewById(R.id.toBook);
            toBookTxt = findViewById(R.id.toBookTxt);
            //도서 추천 리스트 액티비티로 이동
            toBook.setOnClickListener(view->{
                toContentsList("도서", bookList);
            });
            toBookTxt.setOnClickListener(view->{
                toContentsList("도서", bookList);
            });

            //웹툰 버튼 클릭시
            toWebtoon = findViewById(R.id.toWebtoon);
            toWebtoonTxt = findViewById(R.id.toWebtoonTxt);
            //도서 추천 리스트 액티비티로 이동
            toWebtoon.setOnClickListener(view->{
                toContentsList("웹툰", webtoonList);
            });
            toWebtoonTxt.setOnClickListener(view->{
                toContentsList("웹툰", webtoonList);
            });

            //드라마 버튼 클릭시
            toDrama = findViewById(R.id.toDrama);
            toDramaTxt = findViewById(R.id.toDramaTxt);
            //드라마 추천 리스트 액티비티로 이동
            toDrama.setOnClickListener(view->{
                toContentsList("드라마", dramaList);
            });
            toDramaTxt.setOnClickListener(view->{
                toContentsList("드라마", dramaList);
            });

            //읽을거리 버튼 클릭시
            toMagazine = findViewById(R.id.toMagazine);
            toMagazineTxt = findViewById(R.id.toMagazineTxt);
//            //읽을거리 리스트 액티비티로 이동
//            toDrama.setOnClickListener(view->{
//            });
//            toDramaTxt.setOnClickListener(view->{
//            });

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

            // 카테고리별 인기작품 3개 불러오기
            img_book1 = findViewById(R.id.img_book1);
            img_book2 = findViewById(R.id.img_book2);
            img_book3 = findViewById(R.id.img_book3);
            img_movie1 = findViewById(R.id.img_movie1);
            img_movie2 = findViewById(R.id.img_movie2);
            img_movie3 = findViewById(R.id.img_movie3);
            img_webtoon1 = findViewById(R.id.img_webtoon1);
            img_webtoon2 = findViewById(R.id.img_webtoon2);
            img_webtoon3 = findViewById(R.id.img_webtoon3);
            img_drama1 = findViewById(R.id.img_drama1);
            img_drama2 = findViewById(R.id.img_drama2);
            img_drama3 = findViewById(R.id.img_drama3);
            text_book_title1 = findViewById(R.id.text_book_title1);
            text_book_title2 = findViewById(R.id.text_book_title2);
            text_book_title3 = findViewById(R.id.text_book_title3);
            text_movie_title1 = findViewById(R.id.text_movie_title1);
            text_movie_title2 = findViewById(R.id.text_movie_title2);
            text_movie_title3 = findViewById(R.id.text_movie_title3);
            text_webtoon_title1 = findViewById(R.id.text_webtoon_title1);
            text_webtoon_title2 = findViewById(R.id.text_webtoon_title2);
            text_webtoon_title3 = findViewById(R.id.text_webtoon_title3);
            text_drama_title1 = findViewById(R.id.text_drama_title1);
            text_drama_title2 = findViewById(R.id.text_drama_title2);
            text_drama_title3 = findViewById(R.id.text_drama_title3);
            text_book_desc1 = findViewById(R.id.text_book_desc1);
            text_book_desc2 = findViewById(R.id.text_book_desc2);
            text_book_desc3 = findViewById(R.id.text_book_desc3);
            text_movie_desc1 = findViewById(R.id.text_movie_desc1);
            text_movie_desc2 = findViewById(R.id.text_movie_desc2);
            text_movie_desc3 = findViewById(R.id.text_movie_desc3);
            text_webtoon_desc1 = findViewById(R.id.text_webtoon_desc1);
            text_webtoon_desc2 = findViewById(R.id.text_webtoon_desc2);
            text_webtoon_desc3 = findViewById(R.id.text_webtoon_desc3);
            text_drama_desc1 = findViewById(R.id.text_drama_desc1);
            text_drama_desc2 = findViewById(R.id.text_drama_desc2);
            text_drama_desc3 = findViewById(R.id.text_drama_desc3);

        // 카데고리별 인기 작품 3개 불러오기
        loadLike();
        }


    //클릭한 버튼에 따라 카테고리를 지정하여 contentList에 넘겨주는 함수
    void toContentsList(String category, ArrayList<Contents> list){
        Intent toList = new Intent(this, ContentsListActivity.class);
        toList.putExtra("content", category);
        toList.putExtra("allContents", contentList);
        toList.putExtra("movieList", movieList);
        toList.putExtra("bookList", bookList);
        toList.putExtra("webtoonList", webtoonList);
        toList.putExtra("dramaList", dramaList);
        toList.putExtra("likeScrapList", likeScrapList);
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
        Intent detailI = new Intent(this, ContentDetailActivity.class);
        Contents c = null;
        switch (view.getId()){
            case R.id.btn_goProfile:
                drawerLayout.openDrawer(drawerView);
                break;
            case R.id.btn_goSearch:
                //Intent searchI = new Intent(this, SearchActivity);
                break;
            case R.id.view5:
                detailI.putExtra("content", contentList.get(0));
                startActivity(detailI);
                break;
            case R.id.view6:
                detailI.putExtra("content", contentList.get(1));
                startActivity(detailI);
                break;
            case R.id.view7:
                detailI.putExtra("content", contentList.get(2));
                startActivity(detailI);
                break;
            case R.id.view8:
                detailI.putExtra("content", contentList.get(3));
                startActivity(detailI);
                break;
        }
    }

    // 어댑터 설정(인기작품 5개)
    void setAdapter() {
        // 전체 작품 중 좋아요 수가 많은 5개 불러오기
        for(int i=0; i<5; i++) {
            for(Contents contents: contentList) {
                if(contents.getId().equals(likeScrapList.get(i).getId())){
                    sliderItems.add(contents);
                }
            }
        }

        viewPager2.setAdapter(new SliderAdapter(this, sliderItems, viewPager2));

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

    // 카테고리별 인기 작품 불러오기
    void loadLike(){
        int b = 0;
        int m = 0;
        int w = 0;
        int d = 0;
        for(LikeScrap contents: likeScrapList) {
            // 도서 좋아요 수가 많은 3개 불러오기
            for(Contents bookList: bookList) {
                if(contents.getId().equals(bookList.getId())){
                    if(b == 0) {
                        setData(img_book1, text_book_title1, text_book_desc1, bookList);
                    }
                    else if(b == 1) {
                        setData(img_book2, text_book_title2, text_book_desc2, bookList);
                    }
                    else if(b == 2) {
                        setData(img_book3, text_book_title3, text_book_desc3, bookList);
                    }
                    else break;
                    b++;
                }
            }

            // 영화 좋아요 수가 많은 3개 불러오기
            for(Contents movieList: movieList) {
                if(contents.getId().equals(movieList.getId())){
                    if(m == 0) setData(img_movie1, text_movie_title1, text_movie_desc1, movieList);
                    else if(m == 1) setData(img_movie2, text_movie_title2, text_movie_desc2, movieList);
                    else if(m == 2) setData(img_movie3, text_movie_title3, text_movie_desc3, movieList);
                    else break;
                    m++;
                }
            }

            // 웹툰 좋아요 수가 많은 3개 불러오기
            for(Contents webtoonList: webtoonList) {
                if(contents.getId().equals(webtoonList.getId())){
                    if(w == 0) setData(img_webtoon1, text_webtoon_title1, text_webtoon_desc1, webtoonList);
                    else if(w == 1) setData(img_webtoon2, text_webtoon_title2, text_webtoon_desc2, webtoonList);
                    else if(w == 2) setData(img_webtoon3, text_webtoon_title3, text_webtoon_desc3, webtoonList);
                    else break;
                    w++;
                }
            }

            // 드라마 좋아요 수가 많은 3개 불러오기
            for(Contents dramaList: dramaList) {
                if(contents.getId().equals(dramaList.getId())){
                    if(d == 0) setData(img_drama1, text_drama_title1, text_drama_desc1, dramaList);
                    else if(d == 1) setData(img_drama2, text_drama_title2, text_drama_desc2, dramaList);
                    else if(d == 2) setData(img_drama3, text_drama_title3, text_drama_desc3, dramaList);
                    else break;
                    d++;
                }
            }
        }

    }

    // 인기 작품 서랍장 뷰와 연결하는 함수
    @SuppressLint("SetTextI18n")
    void setData(ImageView img, TextView title, TextView desc, Contents contents) {
        Glide.with(MainActivity.this).load(contents.getImg1()).into(img);
        title.setText(contents.getTitle());
        desc.setText(contents.getCategory() + " | " + contents.getAuthor());
    }
}