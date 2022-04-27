package com.example.cloudpush;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.Manifest;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private ImageView btn_back;
    private EditText number1,number2,number3,number4,number5,mobile_no;
    private AppCompatButton verify,submit;
    private LinearLayout verify_layer,password_layer;
    private String Num1,Num2,Num3,Num4,Num5,OTP;
    private String mPattern;
    private Pattern patternMobile;
    private Matcher matcherMobile;
    private String verificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        setUpUI();
        setBtn_Clicks();
    }

    private void setUpUI(){


        number1 = findViewById(R.id.number1);
        number2 = findViewById(R.id.number2);
        number3 = findViewById(R.id.number3);
        number4 = findViewById(R.id.number4);
        number5 = findViewById(R.id.number5);

        mobile_no = findViewById(R.id.mobile_no);

        verify = findViewById(R.id.verify);
        submit = findViewById(R.id.submit);

        verify_layer = findViewById(R.id.verify_layer);
        password_layer = findViewById(R.id.password_layer);

        setPatterns();
    }

    private void setPatterns(){

        mPattern = "^\\+(?:[0-9] ?){6,14}[0-9]$";
        patternMobile = Pattern.compile(mPattern);
    }

    private void setBtn_Clicks(){

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                resetPasswordRequest();
            }
        });


        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkEntered_OTP();

            }
        });
    }


    private void resetPasswordRequest(){

        String phone = mobile_no.getText().toString();

        matcherMobile = patternMobile.matcher(phone);

        if(phone.isEmpty()){

            toastMessageError("Please enter mobile number");
        }
        else if(!matcherMobile.matches()){

            toastMessageError("Please enter valid mobile number");
        }
        else{

            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    phone,
                    60,
                    TimeUnit.SECONDS,
                    MainActivity.this,
                    new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                        @Override
                        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                            password_layer.setVisibility(View.GONE);
                            verify_layer.setVisibility(View.VISIBLE);

                        }

                        @Override
                        public void onVerificationFailed(@NonNull FirebaseException e) {

                        }

                        @Override
                        public void onCodeSent(@NonNull String verificationID, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                            super.onCodeSent(verificationID, forceResendingToken);
                            otpEnter();
                            verificationId = verificationID;
                        }
                    }
            );


        }
    }


    private void toastMessageError(String text){
        Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
    }


    private void otpEnter(){

        number1.addTextChangedListener(new GenericTextWatcher(number1));
        number2.addTextChangedListener(new GenericTextWatcher(number2));
        number3.addTextChangedListener(new GenericTextWatcher(number3));
        number4.addTextChangedListener(new GenericTextWatcher(number4));
        number5.addTextChangedListener(new GenericTextWatcher(number5));


    }

    private void checkEntered_OTP(){
        Num1 = number1.getText().toString();
        Num2 = number2.getText().toString();
        Num3 = number3.getText().toString();
        Num4 = number4.getText().toString();
        Num5 = number5.getText().toString();

        if(Num1.isEmpty() || Num2.isEmpty() || Num3.isEmpty() ||Num4.isEmpty() || Num5.isEmpty()){

           toastMessageError("Please Enter all TextFields");
        }
        else{
            OTP = Num1 + Num2 + Num3 + Num4 +Num5;
            Log.e("CheckOTP: ",""+OTP);
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, OTP);

            FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    Log.e("CHECK SIGNIN", "onComplete: "+"Success");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Log.e("CHECK SIGNIN", "onFailure: "+"Failed");
                }
            });

        }

    }

    //for manage otp text
    private class GenericTextWatcher implements TextWatcher {
        private View view;
        private GenericTextWatcher(View view)
        {
            this.view = view;
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // TODO Auto-generated method stub
            String text = editable.toString();
            switch(view.getId())
            {

                case R.id.number1:
                    if(text.length()==1)
                        number2.requestFocus();
                    break;
                case R.id.number2:
                    if(text.length()==1)
                        number3.requestFocus();
                    else if(text.length()==0)
                        number2.requestFocus();
                    break;
                case R.id.number3:
                    if(text.length()==1)
                        number4.requestFocus();
                    break;
                case R.id.number4:
                    if(text.length()==1)
                        number5.requestFocus();
                    break;
                case R.id.number5:
                    if(text.length()==0)
                        number4.requestFocus();
                    else if(text.length() ==1)

                        break;
            }
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
        }
    }
}