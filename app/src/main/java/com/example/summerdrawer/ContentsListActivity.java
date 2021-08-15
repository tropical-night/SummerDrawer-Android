package com.example.summerdrawer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class ContentsListActivity extends AppCompatActivity {

    ImageButton btn_goProfile_list, btn_goSearch_lst;
    Button btn_logo_lst;
    TextView categoryNameTxt, sortByTxt;
    TextView issueContentsTitleTxt, issueContentsDescTxt;
    ImageView issueContentsImg, setSortByImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contents_list);

        //좌측 상단의 프로필 버튼을 누르면 왼쪽에 네비게이션 드로어 그려주기
        btn_goProfile_list = findViewById(R.id.btn_goProfile_list);
        btn_goProfile_list.setOnClickListener(view->{

        });

        //우측 상단의 돋보기 버튼을 누르면 검색 액티비티로 이동
        btn_goSearch_lst = findViewById(R.id.btn_goSearch_lst);
        btn_goSearch_lst.setOnClickListener(view->{

        });

        categoryNameTxt = findViewById(R.id.categoryNameTxt);
        categoryNameTxt.setText(getIntent().getStringExtra("content"));

    }
}