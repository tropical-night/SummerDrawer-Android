package com.example.summerdrawer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

//    ViewPager viewPager;
//    ArrayList<Integer> arrayList;
//    LinearLayout layout_dot;
//    TextView[] dot;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        viewPager = (ViewPager) findViewById(R.id.viewpager);
//        layout_dot = (LinearLayout) findViewById(R.id.layout_dot);
//        arrayList = new ArrayList<>();
//
//        arrayList.add(R.color.teal_200);
//        arrayList.add(R.color.green);
//        arrayList.add(R.color.colorPrimaryDark);
//        arrayList.add(R.color.colorAccent);
//        arrayList.add(R.color.purple_500);
//
//        CustomPagerAdapter pagerAdapter = new CustomPagerAdapter(getApplicationContext(), arrayList);
//        viewPager.setAdapter(pagerAdapter);
//        viewPager.setPageMargin(50);
//        addDot(0);
//
//        // whenever the page changes
//        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int i, float v, int i1) {
//
//            }
//            @Override
//            public void onPageSelected(int i) {
//                addDot(i);
//            }
//            @Override
//            public void onPageScrollStateChanged(int i) {
//
//            }
//        });
//    }
//
//    public void addDot(int page_position) {
//        dot = new TextView[arrayList.size()];
//        layout_dot.removeAllViews();
//
//        for (int i = 0; i < dot.length; i++) {;
//            dot[i] = new TextView(this);
//            dot[i].setText(Html.fromHtml("&#9679;"));
//            dot[i].setPadding(10,0,10,0);
//            dot[i].setTextSize(10);
//            dot[i].setTextColor(getResources().getColor(R.color.darker_gray));
//            layout_dot.addView(dot[i]);
//        }
//        //active dot
//        dot[page_position].setTextColor(getResources().getColor(R.color.red));
//    }

    ViewPager2 viewPager2;
    ArrayList<SliderItems> sliderItems;
    LinearLayout layout_dot;
    TextView[] dot;
    Handler sliderHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton goProfile = (ImageButton)findViewById(R.id.btn_goProfile);
        ImageButton goSearch = (ImageButton)findViewById(R.id.btn_goSearch);

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

        viewPager2 = findViewById(R.id.viewpager);
        layout_dot = (LinearLayout)findViewById(R.id.layout_dot);

        sliderItems = new ArrayList<>();
        sliderItems.add(new SliderItems(R.color.teal_200));
        sliderItems.add(new SliderItems(R.color.green));
        sliderItems.add(new SliderItems(R.color.red));
        sliderItems.add(new SliderItems(R.color.colorAccent));
        sliderItems.add(new SliderItems(R.color.purple_500));

        viewPager2.setAdapter(new SliderAdapter(sliderItems, viewPager2));
        addDot(0);

        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);


        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);
            }
        });

        viewPager2.setPageTransformer(compositePageTransformer);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnable);
                Log.e("test", String.valueOf(position));
                int p = position > 4 ? position % 5 : position;
                addDot(p);
            }
        });
    }

    public void addDot(int page_position) {
        dot = new TextView[5];
        layout_dot.removeAllViews();

        for (int i = 0; i < dot.length; i++) {
            dot[i] = new TextView(this);
            dot[i].setText(Html.fromHtml("&#9679;"));
            dot[i].setPadding(10,0,10,0);
            dot[i].setTextSize(10);
            dot[i].setTextColor(getResources().getColor(R.color.darker_gray));
            layout_dot.addView(dot[i]);
        }
        //active dot
        dot[page_position].setTextColor(getResources().getColor(R.color.red));
    }

    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
        }
    };
}