package com.example.summerdrawer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class ContentDetailActivity extends AppCompatActivity {

    Contents content;
    Button btn_logo;

    TextView contentTagTxt, contentTitleTxt, contentCategoryAuthorTxt, contentStoryTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_detail);
        //클릭한 컨텐츠 정보 받아오기
        content = (Contents)getIntent().getSerializableExtra("content");
        String category = content.getCategory();

        //화면에 가져온 컨텐츠 정보 뿌려주기
        btn_logo = findViewById(R.id.btn_logo);
        btn_logo.setText(content.getCategory()+"서랍");

        contentTagTxt = findViewById(R.id.contentTagTxt);
        contentTagTxt.setText(content.getTag());

        contentTitleTxt = findViewById(R.id.contentTitleTxt);
        contentTitleTxt.setText(content.getTitle());

        contentCategoryAuthorTxt = findViewById(R.id.contentCategoryAuthorTxt);
        contentCategoryAuthorTxt.setText(content.getCategoryAuthor());

        contentStoryTxt = findViewById(R.id.contentStoryTxt);
        contentStoryTxt.setText(content.getStory());

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

}