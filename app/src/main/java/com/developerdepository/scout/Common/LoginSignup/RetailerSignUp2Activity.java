package com.developerdepository.scout.Common.LoginSignup;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Pair;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.developerdepository.scout.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.shreyaspatil.MaterialDialog.MaterialDialog;
import com.shreyaspatil.MaterialDialog.interfaces.DialogInterface;
import com.tapadoo.alerter.Alerter;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.util.Calendar;

import maes.tech.intentanim.CustomIntent;

public class RetailerSignUp2Activity extends AppCompatActivity {

    private ImageButton backButton;
    private View view1, view2, view3;
    private TextView title, message, loginMsg, loginBtn;
    private ImageView illustration;
    private ChipGroup genderChipGroup;
    private DatePicker dobPicker;
    private Button nextBtn;

    String name, username, email, password, gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailer_sign_up2);

        getWindow().setEnterTransition(null);
        getWindow().setExitTransition(null);

        //Transparent StatusBar
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        username = intent.getStringExtra("username");
        email = intent.getStringExtra("email");
        password = intent.getStringExtra("password");

        initViews();
        setActionOnViews();
    }

    private void initViews() {
        backButton = findViewById(R.id.back_arrow_btn);
        view1 = findViewById(R.id.view1);
        view2 = findViewById(R.id.view2);
        view3 = findViewById(R.id.view3);
        title = findViewById(R.id.retailer_signup_title);
        message = findViewById(R.id.retailer_signup_message);
        illustration = findViewById(R.id.retailer_signup_illustration);
        genderChipGroup = findViewById(R.id.retailer_signup_gender_chipgroup);
        dobPicker = findViewById(R.id.retailer_signup_dob_picker);
        nextBtn = findViewById(R.id.retailer_signup_next_btn);
        loginMsg = findViewById(R.id.retailer_signup_login_msg);
        loginBtn = findViewById(R.id.retailer_signup_login_btn);
    }

    private void setActionOnViews() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validateAge()) {
                    return;
                }

                if(genderChipGroup.getCheckedChipId() == R.id.retailer_signup_male_chip) {
                    gender = "Male";
                } else if(genderChipGroup.getCheckedChipId() == R.id.retailer_signup_female_chip) {
                    gender = "Female";
                } else if(genderChipGroup.getCheckedChipId() == R.id.retailer_signup_other_chip) {
                    gender = "Other";
                } else if(!genderChipGroup.isSelected()) {
                    Alerter.create(RetailerSignUp2Activity.this)
                            .setText("Please select your gender.")
                            .setTextAppearance(R.style.InfoAlert)
                            .setBackgroundColorRes(R.color.infoColor)
                            .setIcon(R.drawable.ic_info)
                            .setDuration(3000)
                            .enableSwipeToDismiss()
                            .enableIconPulse(true)
                            .enableVibration(true)
                            .disableOutsideTouch()
                            .enableProgress(true)
                            .setProgressColorInt(getResources().getColor(android.R.color.white))
                            .show();
                    return;
                }

                int day = dobPicker.getDayOfMonth();
                int month = dobPicker.getMonth() + 1;
                int year = dobPicker.getYear();

                String dateOfBirth = day + "/" + month + "/" + year;

                Intent signupIntent = new Intent(RetailerSignUp2Activity.this, RetailerSignUp3Activity.class);
                signupIntent.putExtra("name", name);
                signupIntent.putExtra("username", username);
                signupIntent.putExtra("email", email);
                signupIntent.putExtra("password", password);
                signupIntent.putExtra("gender", gender);
                signupIntent.putExtra("dateOfBirth", dateOfBirth);

                Pair[] pairs = new Pair[10];
                pairs[0] = new Pair<View, String>(backButton, "transition_signup_back_btn");
                pairs[1] = new Pair<View, String>(view1, "transition_signup_view1");
                pairs[2] = new Pair<View, String>(view2, "transition_signup_view2");
                pairs[3] = new Pair<View, String>(view3, "transition_signup_view3");
                pairs[4] = new Pair<View, String>(title, "transition_signup_title");
                pairs[5] = new Pair<View, String>(message, "transition_signup_msg");
                pairs[6] = new Pair<View, String>(illustration, "transition_signup_illustration");
                pairs[7] = new Pair<View, String>(nextBtn, "transition_signup_next_btn");
                pairs[8] = new Pair<View, String>(loginMsg, "transition_signup_login_msg");
                pairs[9] = new Pair<View, String>(loginBtn, "transition_signup_login_btn");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(RetailerSignUp2Activity.this, pairs);
                startActivity(signupIntent, options.toBundle());
                finish();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RetailerSignUp2Activity.this, RetailerLoginActivity.class));
                CustomIntent.customType(RetailerSignUp2Activity.this, "fadein-to-fadeout");
                finish();
            }
        });
    }

    private boolean validateAge() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int userAge = dobPicker.getYear();
        int isAgeValid = currentYear - userAge;

        if(isAgeValid < 13) {
            Alerter.create(RetailerSignUp2Activity.this)
                    .setText("You must be at least 13 years old.")
                    .setTextAppearance(R.style.InfoAlert)
                    .setBackgroundColorRes(R.color.infoColor)
                    .setIcon(R.drawable.ic_info)
                    .setDuration(3000)
                    .enableSwipeToDismiss()
                    .enableIconPulse(true)
                    .enableVibration(true)
                    .disableOutsideTouch()
                    .enableProgress(true)
                    .setProgressColorInt(getResources().getColor(android.R.color.white))
                    .show();
            return false;
        } else {
            return true;
        }
    }

    public static void setWindowFlag(RetailerSignUp2Activity retailerSignUp2Activity, final int bits, boolean on) {
        Window window = retailerSignUp2Activity.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();

        if(on){
            layoutParams.flags |= bits;
        } else {
            layoutParams.flags &= ~bits;
        }
        window.setAttributes(layoutParams);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}