package com.example.summerdrawer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class JoinActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    EditText userNameInput, userEmailInput, passwordInput, passwordCheckInput;
    Button joinBtn;
    ImageView mailCheckImg, passwordCheckImg;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        mailCheckImg = findViewById(R.id.mailCheckImg);
        passwordCheckImg = findViewById(R.id.passwordCheckImg);

        //파이어베이스 접근 설정
        // user = firebaseAuth.getCurrentUser();
        firebaseAuth =  FirebaseAuth.getInstance();
        //firebaseDatabase = FirebaseDatabase.getInstance().getReference();

        userNameInput = findViewById(R.id.userNameInput);
        userEmailInput = findViewById(R.id.userEmailInput);
        passwordInput = findViewById(R.id.passwordInput);
        passwordCheckInput = findViewById(R.id.passwordCheckInput);
        //파이어베이스 user 로 접글

        //가입버튼 클릭리스너   -->  firebase에 데이터를 저장한다.
        joinBtn = findViewById(R.id.joinBtn);
        joinBtn.setOnClickListener(view->{
            //가입 정보 가져오기
            String userName = userNameInput.getText().toString();
            final String userEmail = userEmailInput.getText().toString();
            String pwd = passwordInput.getText().toString();
            String pwdcheck = passwordCheckInput.getText().toString();

            //모든 정보를 입력했는지 확인
            if(userName.length()==0){
                Toast.makeText(JoinActivity.this, "이름을 입력해 주세요", Toast.LENGTH_SHORT).show();
            }else if(userEmail.length()==0){
                mailCheckImg.setImageResource(R.drawable.bad);
                Toast.makeText(JoinActivity.this, "이메일을 입력해 주세요", Toast.LENGTH_SHORT).show();
            }else if(passwordInput.length()<8){
                mailCheckImg.setImageResource(R.drawable.good);
                Toast.makeText(JoinActivity.this, "비밀번호를 8자리 이상 입력해 주세요.", Toast.LENGTH_SHORT).show();
            }else if(pwd.equals(pwdcheck)) {
                passwordCheckImg.setImageResource(R.drawable.good);
                Log.d(TAG, "등록 버튼 " + userEmail + " , " + pwd);
                final ProgressDialog mDialog = new ProgressDialog(JoinActivity.this);
                mDialog.setMessage("가입중입니다...");
                mDialog.show();

                //파이어베이스에 신규계정 등록하기
                firebaseAuth.createUserWithEmailAndPassword(userEmail, pwd).addOnCompleteListener(task->{
                    //가입 성공시
                    if (task.isSuccessful()) {
                        mailCheckImg.setImageResource(R.drawable.good);
                        mDialog.dismiss();

                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        String email = user.getEmail();
                        String uid = user.getUid();
                        String name = userNameInput.getText().toString().trim();

                        //해쉬맵 테이블을 파이어베이스 데이터베이스에 저장
                        HashMap<Object,String> hashMap = new HashMap<>();

                        hashMap.put("uid",uid);
                        hashMap.put("email",email);
                        hashMap.put("name",name);

                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference reference = database.getReference("Users");
                        reference.child(uid).setValue(hashMap);


                        //가입이 이루어져을시 가입 화면을 빠져나감.
                        finish();
                        Toast.makeText(JoinActivity.this, "회원가입에 성공하셨습니다.", Toast.LENGTH_SHORT).show();

                    } else {
                        mDialog.dismiss();
                        mailCheckImg.setImageResource(R.drawable.bad);
                        Toast.makeText(JoinActivity.this, "이미 존재하는 아이디 입니다.", Toast.LENGTH_SHORT).show();
                        return;  //해당 메소드 진행을 멈추고 빠져나감.

                    }

                });

                //비밀번호 오류시
            }else{
                passwordCheckImg.setImageResource(R.drawable.bad);
                Toast.makeText(JoinActivity.this, "비밀번호가 틀렸습니다. 다시 입력해 주세요.", Toast.LENGTH_SHORT).show();
                return;
            }
        });

    }

    public boolean onSupportNavigateUp(){
        onBackPressed();; // 뒤로가기 버튼이 눌렸을시
        return super.onSupportNavigateUp(); // 뒤로가기 버튼
    }
}