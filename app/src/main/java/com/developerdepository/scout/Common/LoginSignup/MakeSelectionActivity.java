package com.developerdepository.scout.Common.LoginSignup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.developerdepository.scout.R;

import maes.tech.intentanim.CustomIntent;

public class MakeSelectionActivity extends AppCompatActivity {

    private ImageButton backBtn;
    private ConstraintLayout viaSmsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_selection);

        //StatusBar Color
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(getResources().getColor(android.R.color.white));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        initViews();
        setActionOnViews();
    }

    private void initViews() {
        backBtn = findViewById(R.id.back_arrow_btn);
        viaSmsBtn = findViewById(R.id.make_selection_via_sms);
    }

    private void setActionOnViews() {
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        viaSmsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MakeSelectionActivity.this, VerifyOTPActivity.class));
                CustomIntent.customType(MakeSelectionActivity.this, "left-to-right");
                finish();
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
        CustomIntent.customType(MakeSelectionActivity.this, "right-to-left");
    }
}