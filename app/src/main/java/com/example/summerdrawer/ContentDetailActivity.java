package com.example.summerdrawer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class ContentDetailActivity extends AppCompatActivity {

    Contents content;
    Button btn_logo;
    ImageButton btn_goProfile;
    private DrawerLayout drawerLayout;
    private View drawerView;

    TextView contentTagTxt, contentTitleTxt, contentCategoryAuthorTxt, contentStoryTxt;

    ConstraintLayout toLike, toScrap, toMovie, toBook, toWebtoon, toDrama, toMagazine;
    TextView userNameTxt, toLikeTxt, toScrapTxt,
            toMovieTxt, toBookTxt, toWebtoonTxt, toDramaTxt, toMagazineTxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_detail);

        //사용자 이름 받아와서 설정해주기
        userNameTxt = findViewById(R.id.userNameTxt);
        userNameTxt.setText("김뫄뫄");

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
            //메뉴 탭 등장
            drawerLayout.openDrawer(drawerView);
        });


        //*******************************아래는 메뉴 탭 기능입니다*******************************
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

        //메뉴 탭 버튼 테두리 넣어주기
        switch (category){
            case "영화":
                toMovie.setBackgroundResource(R.drawable.drawer_pressed);
                break;
            case "도서":
                toBook.setBackgroundResource(R.drawable.drawer_pressed);
                break;
            case "웹툰":
                toWebtoon.setBackgroundResource(R.drawable.drawer_pressed);
                break;
            case "드라마":
                toDrama.setBackgroundResource(R.drawable.drawer_pressed);
                break;
        }

    }
    void toContentsList(String category){
        Intent toList = new Intent(this, ContentsListActivity.class);
        toList.putExtra("content", category);
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

}