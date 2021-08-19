package com.example.summerdrawer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.EditText;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    ArrayList<Contents> allContents, likeScrapList;

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;

    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        allContents = (ArrayList<Contents>)getIntent().getSerializableExtra("contentList");
        likeScrapList = (ArrayList<Contents>)getIntent().getSerializableExtra("likeScrap");

        recyclerView = findViewById(R.id.recyclerView);

        RVAdapter adapter = new RVAdapter(this, allContents, likeScrapList);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        editText = findViewById(R.id.editText);

    }
}