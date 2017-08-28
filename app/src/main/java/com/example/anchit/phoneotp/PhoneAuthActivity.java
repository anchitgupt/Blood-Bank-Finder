package com.example.anchit.phoneotp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneAuthActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "Hello";
    EditText e1, e2;
    String mVerificationId;
    Button b1,b2;
    FirebaseAuth auth;
    PhoneAuthProvider.ForceResendingToken mResendToken;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    PhoneAuthCredential credential;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        //FirebaseAuth.getInstance().getCurrentUser();
            user = FirebaseAuth.getInstance().getCurrentUser();
            if(user!=null){
                startActivity(new Intent(this,MainActivity.class));
            }
        e1 = (EditText) findViewById(R.id.textView);
        e2 = (EditText) findViewById(R.id.textView2);

        b1 = (Button) findViewById(R.id.send);
        b2 = (Button) findViewById(R.id.submit);
        e2.setVisibility(View.GONE);
        b2.setVisibility(View.GONE);

        auth = FirebaseAuth.getInstance();

        b1.setOnClickListener(this);
        b2.setOnClickListener(this);

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verificaiton without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:" +
                        "" + credential);

                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // ...
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // ...
                }

                // Show a message and update the UI
                // ...
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                 mVerificationId = verificationId;
                  mResendToken = token;
                 e1.setVisibility(View.GONE);
                b1.setVisibility(View.GONE);
                e2.setVisibility(View.VISIBLE);
                b2.setVisibility(View.VISIBLE);
                // ...
            }
        };

    }

    @Override
    public void onClick(View view) {
        if (view == b1) {

            String phonenumber = "+91" + e1.getText().toString();
            if(e1.getText().length() == 10) {
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        phonenumber,
                        60,
                        TimeUnit.SECONDS,
                        this,
                        mCallbacks
                );

            }
            else {
                Toast.makeText(this, "Invalid NUmber", Toast.LENGTH_LONG).show();
            }
        }
        else{
            credential = PhoneAuthProvider.getCredential(mVerificationId,e2.getText().toString());
            signInWithPhoneAuthCredential(credential);

        }
    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                                        startActivity(new Intent(PhoneAuthActivity.this,MainActivity.class));
                            FirebaseUser user = task.getResult().getUser();
                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }

}
