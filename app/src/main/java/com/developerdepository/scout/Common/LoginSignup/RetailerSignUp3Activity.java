package com.developerdepository.scout.Common.LoginSignup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.developerdepository.scout.R;
import com.google.android.material.textfield.TextInputLayout;
import com.hbb20.CountryCodePicker;
import com.shreyaspatil.MaterialDialog.MaterialDialog;
import com.shreyaspatil.MaterialDialog.interfaces.DialogInterface;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;
import net.yslibrary.android.keyboardvisibilityevent.util.UIUtil;

import maes.tech.intentanim.CustomIntent;

public class RetailerSignUp3Activity extends AppCompatActivity {

    private ConstraintLayout parentLayout;
    private ImageButton backButton;
    private View view1, view2, view3;
    private TextView title, message, loginMsg, loginBtn;
    private ImageView illustration;
    private CountryCodePicker signUpCCP;
    private TextInputLayout mobileInputLayout;
    private Button nextBtn;

    String name, username, email, password, gender, dateOfBirth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailer_sign_up3);

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
        gender = intent.getStringExtra("gender");
        dateOfBirth = intent.getStringExtra("dateOfBirth");

        initViews();
        setActionOnViews();

        KeyboardVisibilityEvent.setEventListener(RetailerSignUp3Activity.this, new KeyboardVisibilityEventListener(){
            @Override
            public void onVisibilityChanged(boolean isOpen) {
                if(!isOpen)
                {
                    mobileInputLayout.clearFocus();
                }
            }
        });
    }

    private void initViews() {
        parentLayout = findViewById(R.id.parent_layout);
        backButton = findViewById(R.id.back_arrow_btn);
        view1 = findViewById(R.id.view1);
        view2 = findViewById(R.id.view2);
        view3 = findViewById(R.id.view3);
        title = findViewById(R.id.retailer_signup_title);
        message = findViewById(R.id.retailer_signup_message);
        illustration = findViewById(R.id.retailer_signup_illustration);
        signUpCCP = findViewById(R.id.retailer_signup_ccp);
        mobileInputLayout = findViewById(R.id.retailer_signup_mobile_field);
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
                UIUtil.hideKeyboard(RetailerSignUp3Activity.this);

                if(!validateMobile()) {
                    return;
                } else {
                    if(!isConnectedToInternet(RetailerSignUp3Activity.this)) {
                        showConnectToInternetDialog();
                        return;
                    } else {
                        String mobile = "+" + signUpCCP.getSelectedCountryCode() + mobileInputLayout.getEditText().getText().toString().trim();

                        Intent otpIntent = new Intent(RetailerSignUp3Activity.this, VerifyOTPActivity.class);
                        otpIntent.putExtra("name", name);
                        otpIntent.putExtra("username", username);
                        otpIntent.putExtra("email", email);
                        otpIntent.putExtra("password", password);
                        otpIntent.putExtra("gender", gender);
                        otpIntent.putExtra("dateOfBirth", dateOfBirth);
                        otpIntent.putExtra("mobile", mobile);
                        otpIntent.putExtra("whatToDo", "storeData");

                        Pair[] pairs = new Pair[1];
                        pairs[0] = new Pair<View, String>(parentLayout, "transition_otp");

                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(RetailerSignUp3Activity.this, pairs);
                        startActivity(otpIntent, options.toBundle());
                        finish();
                    }
                }
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RetailerSignUp3Activity.this, RetailerLoginActivity.class));
                CustomIntent.customType(RetailerSignUp3Activity.this, "fadein-to-fadeout");
                finish();
            }
        });
    }

    private boolean isConnectedToInternet(RetailerSignUp3Activity signUp3Activity) {
        ConnectivityManager connectivityManager = (ConnectivityManager) signUp3Activity.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if((wifiConn != null && wifiConn.isConnected()) || (mobileConn != null && mobileConn.isConnected())) {
            return true;
        } else {
            return false;
        }
    }

    private void showConnectToInternetDialog() {
        MaterialDialog materialDialog = new MaterialDialog.Builder(RetailerSignUp3Activity.this)
                .setTitle("No Internet Connection!")
                .setMessage("Please connect to a network first to proceed further!")
                .setCancelable(false)
                .setAnimation(R.raw.no_internet_connection)
                .setPositiveButton("Connect", R.drawable.ic_material_dialog_connect, new MaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton("Cancel", R.drawable.ic_material_dialog_cancel, new MaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                    }
                }).build();
        materialDialog.show();
    }

    private boolean validateMobile() {
        String mobile = mobileInputLayout.getEditText().getText().toString().trim();

        if(mobile.isEmpty()) {
            mobileInputLayout.setError("Field can't be empty.");
            return false;
        } else if(mobile.length() != 10) {
            mobileInputLayout.setError("Invalid Mobile. Try Again.");
            return false;
        } else {
            mobileInputLayout.setError(null);
            mobileInputLayout.setErrorEnabled(false);
            return true;
        }
    }

    public static void setWindowFlag(RetailerSignUp3Activity retailerSignUp3Activity, final int bits, boolean on) {
        Window window = retailerSignUp3Activity.getWindow();
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