package com.developerdepository.scout.Common.LoginSignup;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import com.developerdepository.scout.R;
import com.developerdepository.scout.User.AllCategoriesActivity;

import maes.tech.intentanim.CustomIntent;

public class RetailerStartUpScreenActivity extends AppCompatActivity {

    private ImageButton closeBtn;
    private Button loginBtn, signupBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailer_start_up_screen);

        getWindow().setEnterTransition(null);
        getWindow().setExitTransition(null);

        //StatusBar Color
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(getResources().getColor(android.R.color.white));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        initViews();
        setActionOnViews();
    }

    private void initViews() {
        closeBtn = findViewById(R.id.close_btn);
        loginBtn = findViewById(R.id.retailer_startup_login_btn);
        signupBtn = findViewById(R.id.retailer_startup_signup_btn);
    }

    private void setActionOnViews() {
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(RetailerStartUpScreenActivity.this, RetailerLoginActivity.class);

                Pair[] pairs = new Pair[1];
                pairs[0] = new Pair<View, String>(loginBtn, "transition_login");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(RetailerStartUpScreenActivity.this, pairs);
                startActivity(loginIntent, options.toBundle());
            }
        });

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signupIntent = new Intent(RetailerStartUpScreenActivity.this, RetailerSignUp1Activity.class);

                Pair[] pairs = new Pair[1];
                pairs[0] = new Pair<View, String>(signupBtn, "transition_signup");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(RetailerStartUpScreenActivity.this, pairs);
                startActivity(signupIntent, options.toBundle());
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
        CustomIntent.customType(RetailerStartUpScreenActivity.this, "up-to-bottom");
    }
}