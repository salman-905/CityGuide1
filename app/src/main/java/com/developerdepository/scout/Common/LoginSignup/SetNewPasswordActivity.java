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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shreyaspatil.MaterialDialog.MaterialDialog;
import com.shreyaspatil.MaterialDialog.interfaces.DialogInterface;
import com.tapadoo.alerter.Alerter;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;
import net.yslibrary.android.keyboardvisibilityevent.util.UIUtil;

import java.util.regex.Pattern;

import dmax.dialog.SpotsDialog;
import maes.tech.intentanim.CustomIntent;

public class SetNewPasswordActivity extends AppCompatActivity {

    private ImageButton backBtn;
    private TextInputLayout newPasswordInputLayout, confirmPasswordInputLayout;
    private Button updateBtn;

    AlertDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_new_password);

        //StatusBar Color
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(getResources().getColor(android.R.color.white));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        progressDialog = new SpotsDialog.Builder().setContext(SetNewPasswordActivity.this)
                .setMessage("Updating Password...")
                .setCancelable(false)
                .setTheme(R.style.SpotsDialog)
                .build();

        initViews();
        setActionOnViews();

        KeyboardVisibilityEvent.setEventListener(SetNewPasswordActivity.this, new KeyboardVisibilityEventListener(){
            @Override
            public void onVisibilityChanged(boolean isOpen) {
                if(!isOpen)
                {
                    newPasswordInputLayout.clearFocus();
                    confirmPasswordInputLayout.clearFocus();
                }
            }
        });
    }

    private void initViews() {
        backBtn = findViewById(R.id.back_arrow_btn);
        newPasswordInputLayout = findViewById(R.id.set_new_password_field);
        confirmPasswordInputLayout = findViewById(R.id.confirm_new_password_field);
        updateBtn = findViewById(R.id.set_new_password_update_btn);
    }

    private void setActionOnViews() {
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIUtil.hideKeyboard(SetNewPasswordActivity.this);

                if(!validatePasswords()) {
                    return;
                } else {
                    if(!isConnectedToInternet(SetNewPasswordActivity.this)) {
                        showConnectToInternetDialog();
                        return;
                    } else {
                        progressDialog.show();

                        String newPassword = newPasswordInputLayout.getEditText().getText().toString().trim();
                        String mobile = getIntent().getStringExtra("mobile");

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
                        reference.child(mobile).child("password").setValue(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()) {
                                    progressDialog.dismiss();
                                    startActivity(new Intent(SetNewPasswordActivity.this, SetPasswordSuccessMessageActivity.class));
                                    CustomIntent.customType(SetNewPasswordActivity.this, "left-to-right");
                                    finish();
                                } else {
                                    progressDialog.dismiss();
                                    Alerter.create(SetNewPasswordActivity.this)
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
                                    return;
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    private boolean isConnectedToInternet(SetNewPasswordActivity setNewPasswordActivity) {
        ConnectivityManager connectivityManager = (ConnectivityManager) setNewPasswordActivity.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if((wifiConn != null && wifiConn.isConnected()) || (mobileConn != null && mobileConn.isConnected())) {
            return true;
        } else {
            return false;
        }
    }

    private void showConnectToInternetDialog() {
        MaterialDialog materialDialog = new MaterialDialog.Builder(SetNewPasswordActivity.this)
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

    private boolean validatePasswords() {
        String newPassword = newPasswordInputLayout.getEditText().getText().toString().trim();
        String confirmPassword = confirmPasswordInputLayout.getEditText().getText().toString().trim();

        Pattern PASSWORD_PATTERN = Pattern.compile("^" +
                "(?=.*[0-9])" +                 //at least 1 digit
                "(?=.*[a-z])" +                 //at least 1 lowercase letter
                "(?=.*[A-Z])" +                 //at least 1 uppercase letter
                "(?=.*[!@#$%^&*+=_])" +         //at least 1 special character
                "(?=\\S+$)" +                   //no white spaces
                ".{6,}" +                       //at least 6-character long
                "$");

        if(newPassword.isEmpty()) {
            newPasswordInputLayout.setError("Field can't be empty.");
            return false;
        } else if(confirmPassword.isEmpty()) {
            confirmPasswordInputLayout.setError("Field can't be empty.");
            return false;
        } else if(!PASSWORD_PATTERN.matcher(newPassword).matches()) {
            newPasswordInputLayout.setError("Should contain at least 6 characters - 1 digit, 1 lowercase & 1 uppercase letter, 1 special character and with no white spaces.");
            return false;
        } else if(!confirmPassword.equals(newPassword)) {
            newPasswordInputLayout.setError("These passwords didn't match!");
            confirmPasswordInputLayout.setError("These passwords didn't match!");
            return false;
        } else {
            newPasswordInputLayout.setError(null);
            newPasswordInputLayout.setErrorEnabled(false);
            confirmPasswordInputLayout.setError(null);
            confirmPasswordInputLayout.setErrorEnabled(false);
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
        CustomIntent.customType(SetNewPasswordActivity.this, "right-to-left");
    }
}