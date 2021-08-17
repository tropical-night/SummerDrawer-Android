package com.example.summerdrawer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class ContentsListActivity extends AppCompatActivity {

    Button btn_logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contents_list);

        btn_logo = findViewById(R.id.btn_logo);
        btn_logo.setText(getIntent().getStringExtra("content")+"서랍");


    }
}