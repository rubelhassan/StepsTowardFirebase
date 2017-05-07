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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Copyright 2016 Rubel Hassan. All Rights Reserved.
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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    // Firebase auth clients
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentAuthUser;

    // UI elements
    private EditText mEditTextEmail;
    private EditText mEditTextPassword;
    private Button mBtnLogin;
    private Button mBtnSignup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        initUiComponents();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_login_act_login){
            String email = mEditTextEmail.getText().toString().trim();
            String password = mEditTextPassword.getText().toString().trim();

            // TODO validate user inputs before trying to login

            tryLoginUser(email, password);
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

        // attach listener
        mBtnLogin.setOnClickListener(this);
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
     * this method will navigate user to main activity
     */
    private void navigateToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
