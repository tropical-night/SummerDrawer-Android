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

    //?????? ?????? ???????????? ???
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

    // ?????? ?????? ????????? ???
    ImageView img_latest1, img_latest2, img_latest3, img_latest4;
    TextView title_latest1, title_latest2, title_latest3, title_latest4,
            category_latest1, category_latest2, category_latest3, category_latest4,
            writer_latest1, writer_latest2, writer_latest3, writer_latest4,
            summary_latest1, summary_latest2, summary_latest3, summary_latest4;

    // ????????? ????????????
    ArrayList<Contents> contentList = new ArrayList<>(); // ?????? ?????? ?????????
    ArrayList<Contents> movieList = new ArrayList<>();
    ArrayList<Contents> bookList = new ArrayList<>();
    ArrayList<Contents> webtoonList = new ArrayList<>();
    ArrayList<Contents> dramaList = new ArrayList<>();
    ArrayList<LikeScrap> likeScrapList = new ArrayList<>(); // ?????????,????????? ?????????

    Contents[] mPopular = new Contents[3];
    Contents[] bPopular = new Contents[3];
    Contents[] wPopular = new Contents[3];
    Contents[] dPopular = new Contents[3];

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

        //?????? ?????? ????????? ?????? ???????????????
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

        //?????? ?????? ???????????? ??? ?????????
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

        //?????? ?????? ???????????? ??? ?????? ??? ??????/?????? ????????? ??????
        movieLayout.setOnClickListener(view->{
            popularDrawer("??????");
        });
        movieTxt.setOnClickListener(view->{
            popularDrawer("??????");

        });

        bookLayout.setOnClickListener(view->{
            popularDrawer("??????");

        });
        bookTxt.setOnClickListener(view->{
            popularDrawer("??????");

        });

        webtoonLayout.setOnClickListener(view->{
            popularDrawer("??????");
        });
        webtoonTxt.setOnClickListener(view->{
            popularDrawer("??????");
        });

        dramaLayout.setOnClickListener(view->{
            popularDrawer("?????????");
        });
        dramaTxt.setOnClickListener(view->{
            popularDrawer("?????????");
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

        //?????? ????????? ????????? ????????? ?????? ???
        btn_goProfile = findViewById(R.id.btn_goProfile);
        btn_goProfile.setOnClickListener(view->{
            Log.d("test", "?????? ??????");
            //??????????????? ????????? ??????
                drawerLayout.openDrawer(drawerView);
            btnOnclick(view);
        });

        viewPager2 = findViewById(R.id.viewpager);
        dots_indicator = findViewById(R.id.dots_indicator);
        viewPager2 = findViewById(R.id.viewpager);
        dots_indicator = findViewById(R.id.dots_indicator);

        // ????????? ????????? ?????????????????? ????????????
        setAdapter();

        //????????? ?????? ???????????? ???????????????
        userNameTxt = findViewById(R.id.userNameTxt);
        //userNameTxt.setText("?????????");

        //???????????? ?????? ?????? ???
        toLike = findViewById(R.id.toLike);
        toLikeTxt = findViewById(R.id.toLikeTxt);
        //???????????? ???????????? ???????????? ??????
        toLike.setOnClickListener(view->{
            toLikeScrapList("like");
        });
        toLikeTxt.setOnClickListener(view->{
            toLikeScrapList("like");
        });

        //???????????? ?????? ?????? ???
        toScrap = findViewById(R.id.toScrap);
        toScrapTxt = findViewById(R.id.toScrapTxt);
        //???????????? ???????????? ???????????? ??????
        toScrap.setOnClickListener(view->{
            toLikeScrapList("scrap");
        });
        toScrapTxt.setOnClickListener(view->{
            toLikeScrapList("scrap");
        });

        //?????? ?????? ?????????
        toMovie = findViewById(R.id.toMovie);
        toMovieTxt = findViewById(R.id.toMovieTxt);
        //?????? ?????? ????????? ??????????????? ??????
        toMovie.setOnClickListener(view->{
            toContentsList("??????", movieList);
        });
        toMovieTxt.setOnClickListener(view->{
            toContentsList("??????", movieList);
        });

        //?????? ?????? ?????????
        toBook = findViewById(R.id.toBook);
        toBookTxt = findViewById(R.id.toBookTxt);
        //?????? ?????? ????????? ??????????????? ??????
        toBook.setOnClickListener(view->{
            toContentsList("??????", bookList);
        });
        toBookTxt.setOnClickListener(view->{
            toContentsList("??????", bookList);
        });

        //?????? ?????? ?????????
        toWebtoon = findViewById(R.id.toWebtoon);
        toWebtoonTxt = findViewById(R.id.toWebtoonTxt);
        //?????? ?????? ????????? ??????????????? ??????
        toWebtoon.setOnClickListener(view->{
            toContentsList("??????", webtoonList);
        });
        toWebtoonTxt.setOnClickListener(view->{
            toContentsList("??????", webtoonList);
        });

        //????????? ?????? ?????????
        toDrama = findViewById(R.id.toDrama);
        toDramaTxt = findViewById(R.id.toDramaTxt);
        //????????? ?????? ????????? ??????????????? ??????
        toDrama.setOnClickListener(view->{
            toContentsList("?????????", dramaList);
        });
        toDramaTxt.setOnClickListener(view->{
            toContentsList("?????????", dramaList);
        });

        //???????????? ?????? ?????????
        toMagazine = findViewById(R.id.toMagazine);
        toMagazineTxt = findViewById(R.id.toMagazineTxt);
        //???????????? ????????? ??????????????? ??????
        toMagazineTxt.setOnClickListener(view->{
            Intent toMagazineI = new Intent(this, MagazineListActivity.class);

            toMagazineI.putExtra("content", "????????????");
            toMagazineI.putExtra("allContents", contentList);
            toMagazineI.putExtra("movieList", movieList);
            toMagazineI.putExtra("bookList", bookList);
            toMagazineI.putExtra("webtoonList", webtoonList);
            toMagazineI.putExtra("dramaList", dramaList);
            toMagazineI.putExtra("likeScrapList", likeScrapList);
            startActivity(toMagazineI);
        });
        toMagazine.setOnClickListener(view->{
            Intent toMagazineI = new Intent(this, MagazineListActivity.class);

            toMagazineI.putExtra("content", "????????????");
            toMagazineI.putExtra("allContents", contentList);
            toMagazineI.putExtra("movieList", movieList);
            toMagazineI.putExtra("bookList", bookList);
            toMagazineI.putExtra("webtoonList", webtoonList);
            toMagazineI.putExtra("dramaList", dramaList);
            toMagazineI.putExtra("likeScrapList", likeScrapList);
            startActivity(toMagazineI);
        });

        // ?????? ?????? 4??? ????????????
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

        // ??????????????? ???????????? 3??? ????????????
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

        // ??????????????? ?????? ?????? 3??? ????????????
        loadLike();
        }


    //????????? ????????? ?????? ??????????????? ???????????? contentList??? ???????????? ??????
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

    // ???????????? ??????/???????????? ???????????? ???????????? ??????
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
    }

    DrawerLayout.DrawerListener listener = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
            //???????????? ?????? ???
        }

        @Override
        public void onDrawerOpened(@NonNull View drawerView) {
            //Drawer??? ????????? ????????? ??? ??????
        }

        @Override
        public void onDrawerClosed(@NonNull View drawerView) {
            //?????? ????????? ??? ??????
        }

        @Override
        public void onDrawerStateChanged(int newState) {
            //?????? ????????? ??????????????? ??? ??????
        }
    };

    public void btnOnclick(View view) {
        Intent detailI = new Intent(this, ContentDetailActivity.class);
        switch (view.getId()){
            case R.id.btn_goProfile:
                drawerLayout.openDrawer(drawerView);
                break;
            case R.id.btn_goSearch:
                Intent searchI = new Intent(this, SearchActivity.class);
                searchI.putExtra("contentList", contentList);
                searchI.putExtra("likeScrap", likeScrapList);
                startActivity(searchI);
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
            case R.id.view9:
                detailI.putExtra("content", mPopular[0]);
                startActivity(detailI);
                break;
            case R.id.view10:
                detailI.putExtra("content", mPopular[1]);
                startActivity(detailI);
                break;
            case R.id.view11:
                detailI.putExtra("content", mPopular[2]);
                startActivity(detailI);
                break;
            case R.id.view12:
                detailI.putExtra("content", bPopular[0]);
                startActivity(detailI);
                break;
            case R.id.view13:
                detailI.putExtra("content", bPopular[1]);
                startActivity(detailI);
                break;
            case R.id.view14:
                detailI.putExtra("content", bPopular[2]);
                startActivity(detailI);
                break;
            case R.id.view15:
                detailI.putExtra("content", wPopular[0]);
                startActivity(detailI);
                break;
            case R.id.view16:
                detailI.putExtra("content", wPopular[1]);
                startActivity(detailI);
                break;
            case R.id.view17:
                detailI.putExtra("content", wPopular[2]);
                startActivity(detailI);
                break;
            case R.id.view18:
                detailI.putExtra("content", dPopular[0]);
                startActivity(detailI);
                break;
            case R.id.view19:
                detailI.putExtra("content", dPopular[1]);
                startActivity(detailI);
                break;
            case R.id.view20:
                detailI.putExtra("content", dPopular[2]);
                startActivity(detailI);
                break;

        }
    }

    // ????????? ??????(???????????? 5???)
    void setAdapter() {
        // ?????? ?????? ??? ????????? ?????? ?????? 5??? ????????????
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
        // indicator ??????
        dots_indicator.setViewPager2(viewPager2);

        // ???????????? ??????????????? ?????? ??????
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

                // ?????? ???????????? ?????? ???????????? ???
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

    //?????? ??????????????? ??????/??????
    void popularDrawer(String category){
        Animation ani = new AlphaAnimation(0, 1);
        ani.setDuration(1000);
        switch (category){
            case "??????":
                if(isMOpen){
                    //?????? ?????? ??????
                    movieContentLayout.setVisibility(View.GONE);
                    isMOpen=false;
                }else{
                    //?????? ????????? ??????
                    movieContentLayout.setVisibility(View.VISIBLE);
                    movieContentLayout.setAnimation(ani);
                    isMOpen=true;

                    //???????????? ??????
                    bookContentLayout.setVisibility(View.GONE);
                    isBOpen = false;
                    webtoonContentLayout.setVisibility(View.GONE);
                    isWOpen = false;
                    dramaContentLayout.setVisibility(View.GONE);
                    isDOpen = false;
                }
                break;
            case "??????":
                if(isBOpen){
                    //?????? ?????? ??????
                    bookContentLayout.setVisibility(View.GONE);
                    isBOpen=false;
                }else{
                    //?????? ????????? ??????
                    bookContentLayout.setVisibility(View.VISIBLE);
                    bookContentLayout.setAnimation(ani);
                    isBOpen=true;
                    //???????????? ??????
                    movieContentLayout.setVisibility(View.GONE);
                    isMOpen = false;
                    webtoonContentLayout.setVisibility(View.GONE);
                    isWOpen = false;
                    dramaContentLayout.setVisibility(View.GONE);
                    isDOpen = false;
                }
                break;
            case "??????":
                if(isWOpen){
                    //?????? ?????? ??????
                    webtoonContentLayout.setVisibility(View.GONE);
                    isWOpen=false;
                }else{
                    //?????? ????????? ??????
                    webtoonContentLayout.setVisibility(View.VISIBLE);
                    isWOpen=true;
                    webtoonContentLayout.setAnimation(ani);
                    //???????????? ??????
                    movieContentLayout.setVisibility(View.GONE);
                    isMOpen = false;
                    bookContentLayout.setVisibility(View.GONE);
                    isBOpen = false;
                    dramaContentLayout.setVisibility(View.GONE);
                    isDOpen = false;
                }
                break;
            case "?????????":
                if(isDOpen){
                    //????????? ?????? ??????
                    dramaContentLayout.setVisibility(View.GONE);
                    isDOpen=false;
                    dramaContentLayout.setAnimation(ani);
                }else{
                    //?????? ????????? ??????
                    dramaContentLayout.setVisibility(View.VISIBLE);
                    isDOpen=true;
                    //???????????? ??????
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

    // ?????? ?????? 4??? ????????????
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

    // ??????????????? ?????? ?????? ????????????
    void loadLike(){
        int b = 0;
        int m = 0;
        int w = 0;
        int d = 0;
        for(LikeScrap contents: likeScrapList) {
            // ?????? ????????? ?????? ?????? 3??? ????????????
            for(Contents bookList: bookList) {
                if(contents.getId().equals(bookList.getId())){
                    if(b == 0) {
                        bPopular[b] = bookList;
                        setData(img_book1, text_book_title1, text_book_desc1, bookList);
                    }
                    else if(b == 1) {
                        bPopular[b] = bookList;
                        setData(img_book2, text_book_title2, text_book_desc2, bookList);
                    }
                    else if(b == 2) {
                        bPopular[b] = bookList;
                        setData(img_book3, text_book_title3, text_book_desc3, bookList);
                    }
                    else break;
                    b++;
                }
            }

            // ?????? ????????? ?????? ?????? 3??? ????????????
            for(Contents movieList: movieList) {
                if(contents.getId().equals(movieList.getId())){
                    if(m == 0) {
                        mPopular[m] = movieList;
                        setData(img_movie1, text_movie_title1, text_movie_desc1, movieList);
                    }
                    else if(m == 1) {
                        mPopular[m] = movieList;
                        setData(img_movie2, text_movie_title2, text_movie_desc2, movieList);
                    }
                    else if(m == 2) {
                        mPopular[m] = movieList;
                        setData(img_movie3, text_movie_title3, text_movie_desc3, movieList);
                    }
                    else break;
                    m++;
                }
            }

            // ?????? ????????? ?????? ?????? 3??? ????????????
            for(Contents webtoonList: webtoonList) {
                if(contents.getId().equals(webtoonList.getId())){
                    if(w == 0) {
                        wPopular[w] = webtoonList;
                        setData(img_webtoon1, text_webtoon_title1, text_webtoon_desc1, webtoonList);
                    }
                    else if(w == 1) {
                        wPopular[w] = webtoonList;
                        setData(img_webtoon2, text_webtoon_title2, text_webtoon_desc2, webtoonList);
                    }
                    else if(w == 2) {
                        wPopular[w] = webtoonList;
                        setData(img_webtoon3, text_webtoon_title3, text_webtoon_desc3, webtoonList);
                    }
                    else break;
                    w++;
                }
            }

            // ????????? ????????? ?????? ?????? 3??? ????????????
            for(Contents dramaList: dramaList) {
                if(contents.getId().equals(dramaList.getId())){
                    if(d == 0) {
                        dPopular[d] = dramaList;
                        setData(img_drama1, text_drama_title1, text_drama_desc1, dramaList);
                    }
                    else if(d == 1) {
                        dPopular[d] = dramaList;
                        setData(img_drama2, text_drama_title2, text_drama_desc2, dramaList);
                    }
                    else if(d == 2) {
                        dPopular[d] = dramaList;
                        setData(img_drama3, text_drama_title3, text_drama_desc3, dramaList);
                    }
                    else break;
                    d++;
                }
            }
        }

    }

    // ?????? ?????? ????????? ?????? ???????????? ??????
    @SuppressLint("SetTextI18n")
    void setData(ImageView img, TextView title, TextView desc, Contents contents) {
        Glide.with(MainActivity.this).load(contents.getImg1()).into(img);
        title.setText(contents.getTitle());
        desc.setText(contents.getCategory() + " | " + contents.getAuthor());
    }
}