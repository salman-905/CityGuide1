package com.developerdepository.scout.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.developerdepository.scout.Common.LoginSignup.RetailerStartUpScreenActivity;
import com.developerdepository.scout.HelperClasses.DashboardHelperClasses.CategoriesAdapter;
import com.developerdepository.scout.HelperClasses.DashboardHelperClasses.CategoriesModel;
import com.developerdepository.scout.HelperClasses.DashboardHelperClasses.FeaturedModel;
import com.developerdepository.scout.HelperClasses.DashboardHelperClasses.FeaturedLocationsAdapter;
import com.developerdepository.scout.HelperClasses.DashboardHelperClasses.MostViewedLocationsAdapter;
import com.developerdepository.scout.HelperClasses.DashboardHelperClasses.MostViewedModel;
import com.developerdepository.scout.R;
import com.google.android.material.navigation.NavigationView;
import com.shreyaspatil.MaterialDialog.MaterialDialog;
import com.shreyaspatil.MaterialDialog.interfaces.DialogInterface;

import java.util.ArrayList;

import maes.tech.intentanim.CustomIntent;

public class UserDashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //Drawer Menu Variables
    private DrawerLayout dashboardDrawerLayout;
    private NavigationView dashboardNavigationMenu;

    //Dashboard View Variables
    private ConstraintLayout contentView, addPlacesBtn;
    private ImageButton dashboardMenu;
    private RecyclerView featuredRecycler, mostViewedRecycler, categoriesRecycler;
    private TextView categoriesViewAll;
    private ImageButton imgBtnRe, imgBtnHo, imgBtnEd, imgBtnSh;

    //Other Variables
    private RecyclerView.Adapter featuredAdapter, mostViewedAdapter, categoriesAdapter;
    private static final float END_SCALE = 0.8f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

        //StatusBar Color
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(getResources().getColor(R.color.dashboard_background));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        initViews();

        setNavigationMenu();
        setFeaturedRecycler();
        setMostViewedRecycler();
        setCategoriesRecycler();
        setActionOnViews();

        imgBtnRe = findViewById(R.id.imgBtnRe);
        imgBtnHo = findViewById(R.id.imgBtnHo);
        imgBtnEd = findViewById(R.id.imgBtnEd);
        imgBtnSh = findViewById(R.id.imgBtnSh);

        imgBtnRe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserDashboardActivity.this, RestaurantsActivity.class));
            }
        });

        imgBtnHo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserDashboardActivity.this, HotelsActivity.class));
            }
        });

        imgBtnEd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserDashboardActivity.this, EducationActivity.class));
            }
        });

        imgBtnSh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserDashboardActivity.this, ShopsActivity.class));
            }
        });

    }

    private void initViews() {
        //Initialize Views

        //Drawer Menu
        dashboardDrawerLayout = findViewById(R.id.dashboard_drawer_layout);
        dashboardNavigationMenu = findViewById(R.id.dashboard_navigation_menu);

        //Dashboard Views
        contentView = findViewById(R.id.content_view);
        dashboardMenu = findViewById(R.id.dashboard_menu);
        addPlacesBtn = findViewById(R.id.add_places_btn);
        featuredRecycler = findViewById(R.id.featured_locations_recycler);
        mostViewedRecycler = findViewById(R.id.most_viewed_locations_recycler);
        categoriesRecycler = findViewById(R.id.categories_recycler);
        categoriesViewAll = findViewById(R.id.categories_view_all);


    }

    private void setNavigationMenu() {
        dashboardNavigationMenu.bringToFront();
        dashboardNavigationMenu.setNavigationItemSelectedListener(UserDashboardActivity.this);
        dashboardNavigationMenu.setCheckedItem(R.id.nav_home);

        dashboardMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dashboardDrawerLayout.isDrawerVisible(GravityCompat.START))
                    dashboardDrawerLayout.closeDrawer(GravityCompat.START);
                else dashboardDrawerLayout.openDrawer(GravityCompat.START);
            }
        });

        animateNavigationDrawer();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_all_categories :
                startActivity(new Intent(UserDashboardActivity.this, AllCategoriesActivity.class));
                CustomIntent.customType(UserDashboardActivity.this, "left-to-right");
                break;
            case R.id.nav_add_missing_place :
                startActivity(new Intent(UserDashboardActivity.this, RetailerStartUpScreenActivity.class));
                CustomIntent.customType(UserDashboardActivity.this, "bottom-to-up");
                break;
            case R.id.nav_currency :
                startActivity(new Intent(UserDashboardActivity.this, CurrencyActivity.class));
                CustomIntent.customType(UserDashboardActivity.this, "bottom-to-up");
                break;

        }
        return false;
    }

    private void animateNavigationDrawer() {
        dashboardDrawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaledOffset;
                contentView.setScaleX(offsetScale);
                contentView.setScaleY(offsetScale);

                final float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = contentView.getWidth() * diffScaledOffset / 2;
                final float xTranslation = xOffset - xOffsetDiff;
                contentView.setTranslationX(xTranslation);
            }
        });
    }

    private void setFeaturedRecycler() {
        //Setting Featured Locations Recycler
        featuredRecycler.setHasFixedSize(true);
        featuredRecycler.setLayoutManager(new LinearLayoutManager(UserDashboardActivity.this, LinearLayoutManager.HORIZONTAL, false));

        ArrayList<FeaturedModel> featuredLocations = new ArrayList<>();

        featuredLocations.add(new FeaturedModel(R.drawable.img_placeholder1, "ScrapYard", "ScrapYard is the best rated restaurant in the location at the moment."));
        featuredLocations.add(new FeaturedModel(R.drawable.img_placeholder2, "Radisson Blu", "Radisson Blu is the best rated hotel in the location at the moment."));
        featuredLocations.add(new FeaturedModel(R.drawable.img_placeholder3, "Chapter7", "Chapter7 is the best rated club in the location at the moment."));
        featuredLocations.add(new FeaturedModel(R.drawable.img_placeholder4, "Squared", "Squared is the best rated multi-purpose store in the location at the moment."));

        featuredAdapter = new FeaturedLocationsAdapter(featuredLocations);

        featuredRecycler.setAdapter(featuredAdapter);
    }

    private void setMostViewedRecycler() {
        //Setting Most Viewed Locations Recycler
        mostViewedRecycler.setHasFixedSize(true);
        mostViewedRecycler.setLayoutManager(new LinearLayoutManager(UserDashboardActivity.this, LinearLayoutManager.HORIZONTAL, false));

        ArrayList<MostViewedModel> mostViewedLocations = new ArrayList<>();

        mostViewedLocations.add(new MostViewedModel(R.drawable.img_placeholder3, "Chapter7", "Chapter7 is the best rated club in the location at the moment."));
        mostViewedLocations.add(new MostViewedModel(R.drawable.img_placeholder4, "Squared", "Squared is the best rated multi-purpose store in the location at the moment."));
        mostViewedLocations.add(new MostViewedModel(R.drawable.img_placeholder1, "ScrapYard", "ScrapYard is the best rated restaurant in the location at the moment."));
        mostViewedLocations.add(new MostViewedModel(R.drawable.img_placeholder2, "Radisson Blu", "Radisson Blu is the best rated hotel in the location at the moment."));

        mostViewedAdapter = new MostViewedLocationsAdapter(mostViewedLocations);

        mostViewedRecycler.setAdapter(mostViewedAdapter);
    }

    private void setCategoriesRecycler() {
        //Setting Categories Recycler
        categoriesRecycler.setHasFixedSize(true);
        categoriesRecycler.setLayoutManager(new LinearLayoutManager(UserDashboardActivity.this, LinearLayoutManager.HORIZONTAL, false));

        ArrayList<CategoriesModel> categories = new ArrayList<>();

        categories.add(new CategoriesModel(R.color.card2, R.drawable.illustration_shopping, "Shopping"));
        categories.add(new CategoriesModel(R.color.card5, R.drawable.illustration_restaurant, "Restaurants"));
        categories.add(new CategoriesModel(R.color.card4, R.drawable.illustration_hospital, "Hospitals"));
        categories.add(new CategoriesModel(R.color.card1, R.drawable.illustration_education, "Education"));
        categories.add(new CategoriesModel(R.color.card3, R.drawable.illustration_travel, "Travel"));

        categoriesAdapter = new CategoriesAdapter(categories);

        categoriesRecycler.setAdapter(categoriesAdapter);
    }

    private void setActionOnViews() {
        categoriesViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserDashboardActivity.this, AllCategoriesActivity.class));
                CustomIntent.customType(UserDashboardActivity.this, "left-to-right");
            }
        });

        addPlacesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserDashboardActivity.this, RetailerStartUpScreenActivity.class));
                CustomIntent.customType(UserDashboardActivity.this, "bottom-to-up");
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(dashboardDrawerLayout.isDrawerVisible(GravityCompat.START)) {
            dashboardDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            MaterialDialog materialDialog = new MaterialDialog.Builder(UserDashboardActivity.this)
                    .setTitle(getResources().getString(R.string.exitDialogTitle))
                    .setMessage(getResources().getString(R.string.exitDialogMessage))
                    .setCancelable(false)
                    .setPositiveButton(getResources().getString(R.string.exitDialogConfirm), R.drawable.ic_material_dialog_confirm, new MaterialDialog.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            dialogInterface.dismiss();
                            finishAffinity();
                        }
                    })
                    .setNegativeButton(getResources().getString(R.string.exitDialogCancel), R.drawable.ic_material_dialog_cancel, new MaterialDialog.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            dialogInterface.dismiss();
                        }
                    }).build();
            materialDialog.show();
        }
    }
}