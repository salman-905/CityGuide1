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
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.developerdepository.scout.R;
import com.google.android.material.textfield.TextInputLayout;
import com.shreyaspatil.MaterialDialog.MaterialDialog;
import com.shreyaspatil.MaterialDialog.interfaces.DialogInterface;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;
import net.yslibrary.android.keyboardvisibilityevent.util.UIUtil;

import java.util.regex.Pattern;

import maes.tech.intentanim.CustomIntent;

public class RetailerSignUp1Activity extends AppCompatActivity {

    private ImageButton backButton;
    private View view1, view2, view3;
    private TextView title, message, loginMsg, loginBtn;
    private TextInputLayout nameInputLayout, usernameInputLayout, emailInputLayout, createPasswordInputLayout, confirmPasswordInputLayout;
    private ImageView illustration;
    private Button nextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailer_sign_up1);

        getWindow().setEnterTransition(null);
        getWindow().setExitTransition(null);

        //Transparent StatusBar
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        initViews();
        setActionOnViews();

        KeyboardVisibilityEvent.setEventListener(RetailerSignUp1Activity.this, new KeyboardVisibilityEventListener(){
            @Override
            public void onVisibilityChanged(boolean isOpen) {
                if(!isOpen)
                {
                    nameInputLayout.clearFocus();
                    emailInputLayout.clearFocus();
                    usernameInputLayout.clearFocus();
                    createPasswordInputLayout.clearFocus();
                    confirmPasswordInputLayout.clearFocus();
                }
            }
        });
    }

    private void initViews() {
        backButton = findViewById(R.id.back_arrow_btn);
        view1 = findViewById(R.id.view1);
        view2 = findViewById(R.id.view2);
        view3 = findViewById(R.id.view3);
        title = findViewById(R.id.retailer_signup_title);
        message = findViewById(R.id.retailer_signup_message);
        illustration = findViewById(R.id.retailer_signup_illustration);
        nameInputLayout = findViewById(R.id.retailer_signup_name_field);
        usernameInputLayout = findViewById(R.id.retailer_signup_username_field);
        emailInputLayout = findViewById(R.id.retailer_signup_email_field);
        createPasswordInputLayout = findViewById(R.id.retailer_signup_create_password_field);
        confirmPasswordInputLayout = findViewById(R.id.retailer_signup_confirm_password_field);
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
                UIUtil.hideKeyboard(RetailerSignUp1Activity.this);

                String name = nameInputLayout.getEditText().getText().toString().trim();
                String username = usernameInputLayout.getEditText().getText().toString().toLowerCase().trim();
                String email = emailInputLayout.getEditText().getText().toString().toLowerCase().trim();
                String password = createPasswordInputLayout.getEditText().getText().toString().trim();

                if(!validateName() | !validateUsername() | !validateEmail() | !validatePasswords()) {
                    return;
                }

                Intent signupIntent = new Intent(RetailerSignUp1Activity.this, RetailerSignUp2Activity.class);
                signupIntent.putExtra("name", name);
                signupIntent.putExtra("username", username);
                signupIntent.putExtra("email", email);
                signupIntent.putExtra("password", password);

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

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(RetailerSignUp1Activity.this, pairs);
                startActivity(signupIntent, options.toBundle());
                finish();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RetailerSignUp1Activity.this, RetailerLoginActivity.class));
                CustomIntent.customType(RetailerSignUp1Activity.this, "fadein-to-fadeout");
                finish();
            }
        });
    }

    private boolean validateName() {
        String name = nameInputLayout.getEditText().getText().toString().trim();

        if(name.isEmpty()) {
            nameInputLayout.setError("Field can't be empty.");
            return false;
        } else {
            nameInputLayout.setError(null);
            nameInputLayout.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateUsername() {
        String username = usernameInputLayout.getEditText().getText().toString().toLowerCase().trim();
        String USERNAME_PATTERN = "^(?=\\S+$)[a-z0-9_.]{6,20}$";

        if(username.isEmpty()) {
            usernameInputLayout.setError("Field can't be empty.");
            return false;
        } else if(!username.matches(USERNAME_PATTERN)) {
            usernameInputLayout.setError("Should've at least 6 characters - letters, digits, [_] or [.]. White space isn't allowed.");
            return false;
        } else {
            usernameInputLayout.setError(null);
            usernameInputLayout.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateEmail() {
        String email = emailInputLayout.getEditText().getText().toString().toLowerCase().trim();

        if(email.isEmpty()) {
            emailInputLayout.setError("Field can't be empty.");
            return false;
        } else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInputLayout.setError("Invalid Email. Try again.");
            return false;
        } else {
            emailInputLayout.setError(null);
            emailInputLayout.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePasswords() {
        String createPassword = createPasswordInputLayout.getEditText().getText().toString().trim();
        String confirmPassword = confirmPasswordInputLayout.getEditText().getText().toString().trim();

        Pattern PASSWORD_PATTERN = Pattern.compile("^" +
                        "(?=.*[0-9])" +                 //at least 1 digit
                        "(?=.*[a-z])" +                 //at least 1 lowercase letter
                        "(?=.*[A-Z])" +                 //at least 1 uppercase letter
                        "(?=.*[!@#$%^&*+=_])" +         //at least 1 special character
                        "(?=\\S+$)" +                   //no white spaces
                        ".{6,}" +                       //at least 6-character long
                        "$");

        if(createPassword.isEmpty()) {
            createPasswordInputLayout.setError("Field can't be empty.");
            return false;
        } else if(confirmPassword.isEmpty()) {
            confirmPasswordInputLayout.setError("Field can't be empty.");
            return false;
        } else if(!PASSWORD_PATTERN.matcher(createPassword).matches()) {
            createPasswordInputLayout.setError("Should contain at least 6 characters - 1 digit, 1 lowercase & 1 uppercase letter, 1 special character and with no white spaces.");
            return false;
        } else if(!confirmPassword.equals(createPassword)) {
            createPasswordInputLayout.setError("These passwords didn't match!");
            confirmPasswordInputLayout.setError("These passwords didn't match!");
            return false;
        } else {
            createPasswordInputLayout.setError(null);
            createPasswordInputLayout.setErrorEnabled(false);
            confirmPasswordInputLayout.setError(null);
            confirmPasswordInputLayout.setErrorEnabled(false);
            return true;
        }
    }

    public static void setWindowFlag(RetailerSignUp1Activity retailerSignUp1Activity, final int bits, boolean on) {
        Window window = retailerSignUp1Activity.getWindow();
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