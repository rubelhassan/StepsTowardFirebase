package com.example.rubel.stepstowardfirebase;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

/**
 * Copyright 2017 Rubel Hassan. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public class LoginActivity extends AppCompatActivity implements
        View.OnClickListener, GoogleApiClient.OnConnectionFailedListener{

    private static final int RC_GOOGLE_SIGN_IN = 101;

    // Firebase auth clients
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentAuthUser;

    // UI elements
    private EditText mEditTextEmail;
    private EditText mEditTextPassword;
    private Button mBtnLogin;
    private Button mBtnSignup;
    private SignInButton mBtnGoogleSignIn;

    // Google API client
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initUiComponents();

        configureGoogleSignIn();

        mAuth = FirebaseAuth.getInstance();
    }

    /*
     * configure google sign-in options
     * and build google api client
     */
    private void configureGoogleSignIn() {

        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_login_act_login){
            String email = mEditTextEmail.getText().toString().trim();
            String password = mEditTextPassword.getText().toString().trim();

            // TODO validate user inputs before trying to login

            tryLoginUser(email, password);
        }else if(v.getId() == R.id.btn_signup_act_login){
            Intent signUpIntent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(signUpIntent);
        }else if(v.getId() == R.id.btn_google_signin){
            tryGoogleSignIn();
        }
    }

    /*
     *  initialize all ui components
     *  and add listeners if needed
     */
    private void initUiComponents() {
        mEditTextEmail = (EditText) findViewById(R.id.edit_text_email_act_login);
        mEditTextPassword = (EditText) findViewById(R.id.edit_text_password_act_login);
        mBtnLogin = (Button) findViewById(R.id.btn_login_act_login);
        mBtnSignup = (Button) findViewById(R.id.btn_signup_act_login);
        mBtnGoogleSignIn = (SignInButton) findViewById(R.id.btn_google_signin);

        // attach listener
        mBtnLogin.setOnClickListener(this);
        mBtnSignup.setOnClickListener(this);
        mBtnGoogleSignIn.setOnClickListener(this);
    }


    /*
     * trigger an intent using GoogleApiClient
     */
    private void tryGoogleSignIn(){
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN);
    }

    /*
     * Login user using email and password
     */
    private void tryLoginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // login successful
                        if(task.isSuccessful()){
                            navigateToMainActivity();
                        }
                        // login failed
                        else{
                            Snackbar.make(mEditTextEmail, "Something went wrong",
                                    Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
    }

    /*
     * Login user using google account
     */
    private void tryLoginWithWithGoogle(GoogleSignInAccount account){
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // sign-in success
                        if(task.isSuccessful()){
                            navigateToMainActivity();
                        }
                        // sign-in failed
                        else{
                            Snackbar.make(mEditTextEmail, "Google account authentication failed",
                                    Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
    }

    /*
     * this method will navigate user to main activity
     */
    private void navigateToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Snackbar.make(mEditTextEmail, "Google service error",
                Snackbar.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_GOOGLE_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    /*
     * retrieve google account if google sign-in is successful
     */
    private void handleSignInResult(GoogleSignInResult result) {
        // sign-in successful
        if(result.isSuccess()){
            GoogleSignInAccount accGoogle = result.getSignInAccount();
            // now login to firebase using this account
            tryLoginWithWithGoogle(accGoogle);
        }
        // sign-in failed
        else{
            Snackbar.make(mEditTextEmail, "Google sign-in failed",
                    Snackbar.LENGTH_LONG).show();
        }
    }

}
