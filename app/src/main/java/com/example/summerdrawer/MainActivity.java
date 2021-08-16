package com.example.summerdrawer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ViewPager2 viewPager2;
    ArrayList<SliderItems> sliderItems;
    ArrayList<RankItems> rankItems;
    WormDotsIndicator dots_indicator;
    ViewPager2 viewPager2_recycler;
    TabLayout tabLayout;

    ImageButton btn_goProfile;
    private DrawerLayout drawerLayout;
    private View drawerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        // 상단바 검색 페이지로 이동
        goSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 클릭시 이벤트
            }
        });

        // slider item 추가
        sliderItems = new ArrayList<>();
        sliderItems.add(new SliderItems(R.color.teal_200));
        sliderItems.add(new SliderItems(R.color.green));
        sliderItems.add(new SliderItems(R.color.red));
        sliderItems.add(new SliderItems(R.color.colorAccent));
        sliderItems.add(new SliderItems(R.color.purple_500));

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
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);
            }
        });

        viewPager2.setPageTransformer(compositePageTransformer);

        rankItems = new ArrayList<>();
        //rankItems.add(new RankItems(R.color.teal_200, "유미와세포들", "영화", "작가1", "세포세포 유미유미"));
        //rankItems.add(new RankItems(R.color.colorAccent, "소녀의세계", "웹툰", "작가3", "소녀의 세계 이야기"));
        //rankItems.add(new RankItems(R.color.colorAccent, "웹툰웹툰", "웹툰", "작가4", "소녀의 세계 이야기"));

        List<String> titles = Arrays.asList("영화", "책", "웹툰", "인스타툰");

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
}