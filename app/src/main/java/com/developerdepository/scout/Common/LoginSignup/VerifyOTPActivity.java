package com.developerdepository.scout.Common.LoginSignup;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.chaos.view.PinView;
import com.developerdepository.scout.Databases.UserHelperClass;
import com.developerdepository.scout.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shreyaspatil.MaterialDialog.MaterialDialog;
import com.shreyaspatil.MaterialDialog.interfaces.DialogInterface;
import com.tapadoo.alerter.Alerter;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;
import net.yslibrary.android.keyboardvisibilityevent.util.UIUtil;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import dmax.dialog.SpotsDialog;
import maes.tech.intentanim.CustomIntent;

public class VerifyOTPActivity extends AppCompatActivity {

    private ImageButton closeBtn;
    private PinView pinView;
    private Button verifyBtn;
    private TextView verifyOtpMsg, resendOtp;

    Timer timer;
    int count = 60;

    PhoneAuthProvider.ForceResendingToken resendingToken;

    String name, username, email, password, gender, dateOfBirth, mobile, whatToDo;
    String codeBySystem;

    AlertDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        getWindow().setEnterTransition(null);
        getWindow().setExitTransition(null);

        //Transparent StatusBar
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        progressDialog = new SpotsDialog.Builder().setContext(VerifyOTPActivity.this)
                .setMessage("Verifying Mobile...")
                .setCancelable(false)
                .setTheme(R.style.SpotsDialog)
                .build();

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        username = intent.getStringExtra("username");
        email = intent.getStringExtra("email");
        password = intent.getStringExtra("password");
        gender = intent.getStringExtra("gender");
        dateOfBirth = intent.getStringExtra("dateOfBirth");
        mobile = intent.getStringExtra("mobile");
        whatToDo = intent.getStringExtra("whatToDo");

        initViews();
        setActionOnViews();

        KeyboardVisibilityEvent.setEventListener(VerifyOTPActivity.this, new KeyboardVisibilityEventListener(){
            @Override
            public void onVisibilityChanged(boolean isOpen) {
                if(!isOpen)
                {
                    pinView.clearFocus();
                }
            }
        });

        sendVerificationCodeToUser(mobile);
    }

    private void initViews() {
        closeBtn = findViewById(R.id.close_btn);
        verifyOtpMsg = findViewById(R.id.verify_otp_message);
        resendOtp = findViewById(R.id.resend_otp);
        pinView = findViewById(R.id.verify_otp_pinview);
        verifyBtn = findViewById(R.id.verify_otp_btn);
    }

    private void setActionOnViews() {
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        verifyOtpMsg.setText(String.format("Enter the verification code sent to %s", mobile));

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                VerifyOTPActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(count == 0)
                        {
                            resendOtp.setText("Resend OTP");
                            resendOtp.setAlpha(1.0f);
                            resendOtp.setEnabled(true);
                            resendOtp.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    resendOTP();
                                    resendOtp.setEnabled(false);
                                    resendOtp.setAlpha(0.5f);
                                    count = 60;
                                }
                            });
                        }
                        else
                        {
                            resendOtp.setText(String.format("Resend OTP in %d", count));
                            resendOtp.setAlpha(0.5f);
                            resendOtp.setEnabled(false);
                            count--;
                        }
                    }
                });
            }
        },0,1000);

        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIUtil.hideKeyboard(VerifyOTPActivity.this);

                String codeEnteredByUser = pinView.getText().toString();
                if(!codeEnteredByUser.isEmpty()) {
                    verifyCode(codeEnteredByUser);
                } else {
                    return;
                }
            }
        });
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);
                    resendingToken = forceResendingToken;
                    codeBySystem = s;
                }

                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                    String code = phoneAuthCredential.getSmsCode();
                    if(code!=null) {
                        pinView.setText(code);
                        verifyCode(code);
                    }
                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                    if (e instanceof FirebaseAuthInvalidCredentialsException) {
                        progressDialog.dismiss();
                        Alerter.create(VerifyOTPActivity.this)
                                .setText("Whoa! Seems like you've got an invalid code!")
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
                    } else if (e instanceof FirebaseTooManyRequestsException) {
                        progressDialog.dismiss();
                        Alerter.create(VerifyOTPActivity.this)
                                .setText("Too many requests at the moment. Try again after some time!")
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
                }
            };

    private void sendVerificationCodeToUser(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                mobile,
                60,
                TimeUnit.SECONDS,
                VerifyOTPActivity.this,
                mCallbacks
        );
    }

    private void resendOTP()
    {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                mobile,
                60,
                TimeUnit.SECONDS,
                VerifyOTPActivity.this,
                mCallbacks, resendingToken);
    }

    private void verifyCode(String code) {
        if(!isConnectedToInternet(VerifyOTPActivity.this)) {
            showConnectToInternetDialog();
            return;
        } else {
            progressDialog.show();
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeBySystem, code);
            signInUsingCredential(credential);
        }
    }

    private boolean isConnectedToInternet(VerifyOTPActivity verifyOTPActivity) {
        ConnectivityManager connectivityManager = (ConnectivityManager) verifyOTPActivity.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if((wifiConn != null && wifiConn.isConnected()) || (mobileConn != null && mobileConn.isConnected())) {
            return true;
        } else {
            return false;
        }
    }

    private void showConnectToInternetDialog() {
        MaterialDialog materialDialog = new MaterialDialog.Builder(VerifyOTPActivity.this)
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

    private void signInUsingCredential(PhoneAuthCredential credential) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            if(whatToDo.equals("updateData")) {
                                updateOldUserData();
                            } else if(whatToDo.equals("storeData")) {
                                storeNewUserData();
                            }
                        } else {
                            if(task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                progressDialog.dismiss();
                                Alerter.create(VerifyOTPActivity.this)
                                        .setText("Whoa! Verification Failed. Try Again!")
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
                    }
                });
    }

    private void updateOldUserData() {
        progressDialog.dismiss();
        Intent intent = new Intent(VerifyOTPActivity.this, SetNewPasswordActivity.class);
        intent.putExtra("mobile", mobile);
        startActivity(intent);
        CustomIntent.customType(VerifyOTPActivity.this, "left-to-right");
        finish();
    }

    private void storeNewUserData() {
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("Users");

        UserHelperClass newUser = new UserHelperClass(name, username, email, password, gender, dateOfBirth, mobile);

        reference.child(mobile).setValue(newUser).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    progressDialog.dismiss();
                    verifyBtn.setEnabled(false);
                    timer.cancel();
                    resendOtp.setText("Verification Succeeded!");
                    Alerter.create(VerifyOTPActivity.this)
                            .setText("Verification Succeeded!")
                            .setTextAppearance(R.style.SuccessAlert)
                            .setBackgroundColorRes(R.color.successColor)
                            .setIcon(R.drawable.ic_success)
                            .setDuration(3000)
                            .enableSwipeToDismiss()
                            .enableIconPulse(true)
                            .enableVibration(true)
                            .disableOutsideTouch()
                            .enableProgress(true)
                            .setProgressColorInt(getResources().getColor(android.R.color.white))
                            .show();
                    return;
                } else {
                    progressDialog.dismiss();
                    Alerter.create(VerifyOTPActivity.this)
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

    public static void setWindowFlag(VerifyOTPActivity verifyOTPActivity, final int bits, boolean on) {
        Window window = verifyOTPActivity.getWindow();
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

    @Override
    protected void onStop() {
        super.onStop();
        timer.cancel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }
}