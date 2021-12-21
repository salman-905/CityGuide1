package com.developerdepository.scout.Common.LoginSignup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import com.developerdepository.scout.R;

import maes.tech.intentanim.CustomIntent;

public class SetPasswordSuccessMessageActivity extends AppCompatActivity {

    private ImageButton backBtn;
    private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_password_success_message);

        //StatusBar Color
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(getResources().getColor(android.R.color.white));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        initViews();
        setActionOnViews();
    }

    private void initViews() {
        backBtn = findViewById(R.id.back_arrow_btn);
        loginBtn = findViewById(R.id.set_password_success_login_btn);
    }

    private void setActionOnViews() {
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void finish() {
        super.finish();
        CustomIntent.customType(SetPasswordSuccessMessageActivity.this, "right-to-left");
    }
}