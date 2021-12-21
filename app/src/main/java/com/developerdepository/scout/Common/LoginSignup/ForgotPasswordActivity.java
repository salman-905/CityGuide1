package com.developerdepository.scout.Common.LoginSignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import com.developerdepository.scout.R;
import com.developerdepository.scout.User.AllCategoriesActivity;
import com.developerdepository.scout.User.UserDashboardActivity;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;
import com.shreyaspatil.MaterialDialog.MaterialDialog;
import com.shreyaspatil.MaterialDialog.interfaces.DialogInterface;
import com.tapadoo.alerter.Alerter;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;
import net.yslibrary.android.keyboardvisibilityevent.util.UIUtil;

import dmax.dialog.SpotsDialog;
import maes.tech.intentanim.CustomIntent;

public class ForgotPasswordActivity extends AppCompatActivity {

    private ImageButton backBtn;
    private CountryCodePicker forgotPWCCP;
    private TextInputLayout mobileInputLayout;
    private Button nextBtn;

    AlertDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        //StatusBar Color
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(getResources().getColor(android.R.color.white));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        progressDialog = new SpotsDialog.Builder().setContext(ForgotPasswordActivity.this)
                .setMessage("Checking for account...")
                .setCancelable(false)
                .setTheme(R.style.SpotsDialog)
                .build();

        initViews();
        setActionOnViews();

        KeyboardVisibilityEvent.setEventListener(ForgotPasswordActivity.this, new KeyboardVisibilityEventListener(){
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
        backBtn = findViewById(R.id.back_arrow_btn);
        forgotPWCCP = findViewById(R.id.forgot_password_ccp);
        mobileInputLayout = findViewById(R.id.forgot_password_mobile_field);
        nextBtn = findViewById(R.id.forgot_password_next_btn);
    }

    private void setActionOnViews() {
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIUtil.hideKeyboard(ForgotPasswordActivity.this);

                if(!validateMobile()) {
                    return;
                } else {
                    if(!isConnectedToInternet(ForgotPasswordActivity.this)) {
                        showConnectToInternetDialog();
                        return;
                    } else {
                        progressDialog.show();

                        final String mobile = "+" + forgotPWCCP.getSelectedCountryCode() + mobileInputLayout.getEditText().getText().toString().trim();

                        Query checkUser = FirebaseDatabase.getInstance().getReference("Users").orderByChild("mobile").equalTo(mobile);

                        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()) {
                                    mobileInputLayout.setError(null);
                                    mobileInputLayout.setErrorEnabled(false);

                                    progressDialog.dismiss();

                                    Intent intent = new Intent(ForgotPasswordActivity.this, VerifyOTPActivity.class);
                                    intent.putExtra("mobile", mobile);
                                    intent.putExtra("whatToDo", "updateData");
                                    startActivity(intent);
                                    CustomIntent.customType(ForgotPasswordActivity.this, "left-to-right");
                                    finish();
                                } else {
                                    progressDialog.dismiss();
                                    Alerter.create(ForgotPasswordActivity.this)
                                            .setText("Whoa! There's no account with that mobile!")
                                            .setTextAppearance(R.style.ErrorAlert)
                                            .setBackgroundColorRes(R.color.errorColor)
                                            .setIcon(R.drawable.ic_error)
                                            .setDuration(3000)
                                            .enableSwipeToDismiss()
                                            .enableIconPulse(true)
                                            .enableVibration(true)
                                            .disableOutsideTouch()
                                            .enableProgress(true)
                                            .setProgressColorInt(getResources().getColor(android.R.color.white))
                                            .show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                progressDialog.dismiss();
                                Alerter.create(ForgotPasswordActivity.this)
                                        .setText("Whoa! There was some error!")
                                        .setTextAppearance(R.style.ErrorAlert)
                                        .setBackgroundColorRes(R.color.errorColor)
                                        .setIcon(R.drawable.ic_error)
                                        .setDuration(3000)
                                        .enableSwipeToDismiss()
                                        .enableIconPulse(true)
                                        .enableVibration(true)
                                        .disableOutsideTouch()
                                        .enableProgress(true)
                                        .setProgressColorInt(getResources().getColor(android.R.color.white))
                                        .show();
                            }
                        });
                    }
                }
            }
        });
    }

    private boolean isConnectedToInternet(ForgotPasswordActivity forgotPasswordActivity) {
        ConnectivityManager connectivityManager = (ConnectivityManager) forgotPasswordActivity.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if((wifiConn != null && wifiConn.isConnected()) || (mobileConn != null && mobileConn.isConnected())) {
            return true;
        } else {
            return false;
        }
    }

    private void showConnectToInternetDialog() {
        MaterialDialog materialDialog = new MaterialDialog.Builder(ForgotPasswordActivity.this)
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
            mobileInputLayout.setError("Invalid Mobile. Try Again!");
            return false;
        } else {
            mobileInputLayout.setError(null);
            mobileInputLayout.setErrorEnabled(false);
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void finish() {
        super.finish();
        CustomIntent.customType(ForgotPasswordActivity.this, "right-to-left");
    }
}