package com.example.summerdrawer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.text.Html;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ViewPager viewPager;
    ArrayList<Integer> arrayList;
    LinearLayout layout_dot;
    TextView[] dot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        layout_dot = (LinearLayout) findViewById(R.id.layout_dot);
        arrayList = new ArrayList<>();

        arrayList.add(R.color.teal_200);
        arrayList.add(R.color.green);
        arrayList.add(R.color.colorPrimaryDark);
        arrayList.add(R.color.colorAccent);
        arrayList.add(R.color.purple_500);

        CustomPagerAdapter pagerAdapter = new CustomPagerAdapter(getApplicationContext(), arrayList);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setPageMargin(50);
        addDot(0);

        // whenever the page changes
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }
            @Override
            public void onPageSelected(int i) {
                addDot(i);
            }
            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    public void addDot(int page_position) {
        dot = new TextView[arrayList.size()];
        layout_dot.removeAllViews();

        for (int i = 0; i < dot.length; i++) {;
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
}