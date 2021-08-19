package com.example.summerdrawer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    ArrayList<Contents> allContents, likeScrapList, filteredList;

    RecyclerView recyclerView;
    RVAdapter adapter;
    LinearLayoutManager linearLayoutManager;

    EditText editText;
    ConstraintLayout buttonLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        allContents = (ArrayList<Contents>)getIntent().getSerializableExtra("contentList");
        likeScrapList = (ArrayList<Contents>)getIntent().getSerializableExtra("likeScrap");
        filteredList = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerView);

        adapter = new RVAdapter(this, allContents, likeScrapList);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        editText = findViewById(R.id.editText);
        buttonLayout = findViewById(R.id.buttonLayout);

        editText.addTextChangedListener(new TextWatcher() {
            TextView textView10 = findViewById(R.id.textView10);
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String searchTxt = editText.getText().toString();
                if(searchTxt.length()!=0){
                    buttonLayout.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    textView10.setText("'"+searchTxt+"' 검색 결과입니다.");

                    searchFilter(searchTxt);
                }else{
                    buttonLayout.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    textView10.setText("인기 검색어");
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void searchFilter(String searchText) {
        filteredList.clear();

        for (int i = 0; i < allContents.size(); i++) {
            if (allContents.get(i).getTitle().toLowerCase().contains(searchText.toLowerCase()) ||
            allContents.get(i).getAuthor().toLowerCase().contains(searchText.toLowerCase()) ||
            allContents.get(i).getTag().toLowerCase().contains(searchText.toLowerCase())) {
                filteredList.add(allContents.get(i));
            }
        }

        adapter.filterList(filteredList);
    }

    public void btnOnclick(View view) {
        Button btn;
        switch (view.getId()){
            case R.id.button2:
                btn = findViewById(R.id.button2);
                editText.setText(btn.getText().toString().replace("#", ""));
                break;
            case R.id.button3:
                btn = findViewById(R.id.button3);
                editText.setText(btn.getText().toString().replace("#", ""));
                break;
            case R.id.button4:
                btn = findViewById(R.id.button4);
                editText.setText(btn.getText().toString().replace("#", ""));
                break;
            case R.id.button5:
                btn = findViewById(R.id.button5);
                editText.setText(btn.getText().toString().replace("#", ""));
                break;
            case R.id.button6:
                btn = findViewById(R.id.button6);
                editText.setText(btn.getText().toString().replace("#", ""));
                break;
            case R.id.textView8:
                finish();
                break;

        }
    }

}