package com.example.summerdrawer;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;

public class RankPagerAdapter extends PagerAdapter {
    private Context mContext = null;

    public RankPagerAdapter() {}
    public RankPagerAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        return super.instantiateItem(container, position);

       //rankItems = new ArrayList<>();
        //RankAdapter rankAdapter = new RankAdapter(rankItems, viewPager2_recycler);
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return false;
    }
}
