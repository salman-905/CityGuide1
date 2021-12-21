package com.developerdepository.scout.LocationOwner;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.developerdepository.scout.R;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class RetailerDashboardActivity extends AppCompatActivity {

    ImageButton dashBoardBtn, profileBtn;
    ImageView dashboardDot, profileDot;
    BottomAppBar bottomAppBar;
    FloatingActionButton fabManage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailer_dashboard);

        //Transparent StatusBar
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        initViews();
        setBottomNavigation();

        dashBoardBtn.performClick();
    }

    private void initViews() {
        dashBoardBtn = findViewById(R.id.bottom_nav_dashboard);
        dashboardDot = findViewById(R.id.bottom_nav_dashboard_dot);
        profileBtn = findViewById(R.id.bottom_nav_profile);
        profileDot = findViewById(R.id.bottom_nav_profile_dot);
        bottomAppBar = findViewById(R.id.bottom_app_bar);
        fabManage = findViewById(R.id.fab_btn_add);
    }

    private void setBottomNavigation() {
        dashBoardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dashBoardBtn.setImageTintList(ColorStateList.valueOf(getColor(R.color.colorPrimary)));
                profileBtn.setImageTintList(ColorStateList.valueOf(getColor(R.color.inactiveBottomNavColor)));
                dashboardDot.setVisibility(View.VISIBLE);
                dashboardDot.setImageTintList(ColorStateList.valueOf(getColor(R.color.colorPrimary)));
                profileDot.setVisibility(View.GONE);
                fabManage.setBackgroundTintList(ColorStateList.valueOf(getColor(android.R.color.black)));
                fabManage.setImageTintList(ColorStateList.valueOf(getColor(R.color.inactiveBottomNavColor)));
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new RetailerDashboardFragment()).commit();
            }
        });

        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileBtn.setImageTintList(ColorStateList.valueOf(getColor(R.color.colorPrimary)));
                dashBoardBtn.setImageTintList(ColorStateList.valueOf(getColor(R.color.inactiveBottomNavColor)));
                profileDot.setVisibility(View.VISIBLE);
                profileDot.setImageTintList(ColorStateList.valueOf(getColor(R.color.colorPrimary)));
                dashboardDot.setVisibility(View.GONE);
                fabManage.setBackgroundTintList(ColorStateList.valueOf(getColor(android.R.color.black)));
                fabManage.setImageTintList(ColorStateList.valueOf(getColor(R.color.inactiveBottomNavColor)));
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new RetailerProfileFragment()).commit();
            }
        });

        fabManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabManage.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.colorPrimary)));
                fabManage.setImageTintList(ColorStateList.valueOf(getColor(android.R.color.black)));
                dashBoardBtn.setImageTintList(ColorStateList.valueOf(getColor(R.color.inactiveBottomNavColor)));
                profileBtn.setImageTintList(ColorStateList.valueOf(getColor(R.color.inactiveBottomNavColor)));
                dashboardDot.setVisibility(View.GONE);
                profileDot.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new RetailerManageFragment()).commit();
            }
        });
    }

    public static void setWindowFlag(RetailerDashboardActivity retailerDashboardActivity, final int bits, boolean on) {
        Window window = retailerDashboardActivity.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();

        if (on) {
            layoutParams.flags |= bits;
        } else {
            layoutParams.flags &= ~bits;
        }
        window.setAttributes(layoutParams);
    }
}