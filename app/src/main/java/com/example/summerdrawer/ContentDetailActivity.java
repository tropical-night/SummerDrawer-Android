package com.example.summerdrawer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class ContentDetailActivity extends AppCompatActivity {

    Contents content;
    Button btn_logo;

    TextView contentTagTxt, contentTitleTxt, contentCategoryAuthorTxt, contentStoryTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_detail);
        content = (Contents)getIntent().getSerializableExtra("content");
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
}