package com.example.summerdrawer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ViewPager2 viewPager2;
    ArrayList<SliderItems> sliderItems;
    DotsIndicator dots_indicator;
    ViewPager2 viewPager2_recycler;
    Button btn_movie, btn_book, btn_webtoon, btn_instaToon, btn_magazine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton goProfile = (ImageButton)findViewById(R.id.btn_goProfile);
        ImageButton goSearch = (ImageButton)findViewById(R.id.btn_goSearch);
        viewPager2 = findViewById(R.id.viewpager);
        dots_indicator = findViewById(R.id.dots_indicator);
        viewPager2_recycler = findViewById(R.id.viewpager_recycler);

        // 상단바 프로필 이동
        goProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 클릭시 이벤트
            }
        });

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
                //TextView t = page.findViewById(R.id.textView);
                //if(position != 0) t.setVisibility(View.INVISIBLE);
                //else t.setVisibility(View.VISIBLE);
            }
        });

        viewPager2.setPageTransformer(compositePageTransformer);

        btn_movie = findViewById(R.id.btn_movie);
        //영화 버튼 클릭 시 화면 전환
        btn_movie.setOnClickListener(view->{
            toContentsList("영화");
        });

        btn_book = findViewById(R.id.btn_book);
        //책 버튼 클릭 시 화면 전환환
       btn_book.setOnClickListener(view->{
            toContentsList("책");
        });

        btn_webtoon = findViewById(R.id.btn_webtoon);
        //웹툰 버튼 클릭 시 화면 전환환
        btn_webtoon.setOnClickListener(view->{
            toContentsList("웹툰");
        });

        btn_instaToon = findViewById(R.id.btn_instaToon);
        //인스타툰 버튼 클릭 시 화면 전환환
        btn_instaToon.setOnClickListener(view->{
            toContentsList("인스타툰");
        });
    }

    //클릭한 버튼에 따라 카테고리를 지정하여 contentList에 넘겨주는 함수
    void toContentsList(String category){
        Intent toList = new Intent(this, ContentsListActivity.class);
        toList.putExtra("content", category);
        startActivity(toList);
    }
}