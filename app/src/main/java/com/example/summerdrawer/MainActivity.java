package com.example.summerdrawer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.util.ArrayList;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        //db에서 메인 화면에 보여줄 데이터 받아오기
        db = FirebaseFirestore.getInstance();
        sliderItems = new ArrayList<>();

        db.collection("webtoon").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    //document.getData() or document.getId()
                    String title = (String) document.getData().get("title");
                    String category = (String) document.getData().get("category");
                    String tag = setTagListToString((String) document.getData().get("tag"));
                    String author = setAuthorToString((String) document.getData().get("author"));

                    String desc = (String) document.getData().get("summary");
                    sliderItems.add(new SliderItems(R.color.darker_gray, title, category, author, desc, tag));
                }
                setContentView(R.layout.activity_main);

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
            Log.d("test", "버튼 누름");
            drawerLayout.openDrawer(drawerView);
        });

        ImageButton goSearch = findViewById(R.id.btn_goSearch);
        viewPager2 = findViewById(R.id.viewpager);
        dots_indicator = findViewById(R.id.dots_indicator);
        goProfile = findViewById(R.id.btn_goProfile);
        goSearch = findViewById(R.id.btn_goSearch);
        viewPager2 = findViewById(R.id.viewpager);
        dots_indicator = findViewById(R.id.dots_indicator);
        // 어댑터 설정
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
//        //읽을거리 리스트 액티비티로 이동
//        toDrama.setOnClickListener(view->{
//        });
//        toDramaTxt.setOnClickListener(view->{
//        });
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
}