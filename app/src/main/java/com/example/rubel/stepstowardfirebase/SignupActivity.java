package com.example.rubel.stepstowardfirebase;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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

public class SignupActivity extends AppCompatActivity implements View.OnClickListener{

    // Firebase auth clients
    private FirebaseAuth mAuth;

    // UI elements
    private EditText mEditTextEmail;
    private EditText mEditTextPassword;
    private EditText mEditTextConfirmPassword;
    private Button mBtnSignup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        initUiComponents();

        mAuth = FirebaseAuth.getInstance();
    }

    /*
     *  initialize all ui components
     *  and add listeners if needed
     */
    private void initUiComponents() {
        mEditTextEmail = (EditText) findViewById(R.id.edit_text_email_act_signup);
        mEditTextPassword = (EditText) findViewById(R.id.edit_text_password_act_signup);
        mEditTextConfirmPassword = (EditText) findViewById(R.id.edit_text_confirm_password_act_signup);
        mBtnSignup = (Button) findViewById(R.id.btn_signup_act_signup);

        // attach listener
        mBtnSignup.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_signup_act_signup){
            String email = mEditTextEmail.getText().toString().trim();
            String password = mEditTextPassword.getText().toString().trim();
            String confirmPassword  = mEditTextConfirmPassword.getText().toString().trim();

            if(validateUserInputs(email, password, confirmPassword))
                trySignUpUser(email, password);
        }
    }

    /*
     *  this method will create a firebase user
     */
    private void trySignUpUser(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // user creation success
                        if(task.isSuccessful()){
                            navigateToMainActivity();
                        }
                        // user not created
                        else {
                            Snackbar.make(mBtnSignup, "Something went wrong", Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
    }

    /*
     * validate user email and password
     * and show errors accordingly if found
     */
    private boolean validateUserInputs(String email, String password, String confirmPassword) {

        if(TextUtils.isEmpty(email)){
            mEditTextEmail.setError("Enter email");
            return false;
        }

        if(!AppUtils.validateEmail(email)) {
            mEditTextEmail.setError("Enter a valid email");
            return false;
        }

        if(password.length() < 6){
            mEditTextPassword.setError("Password should be minimum 6 characters long.");
            return false;
        }

        if(!password.equals(confirmPassword)){
            mEditTextConfirmPassword.setError("Password mismatch");
            return false;
        }

        return true;
    }

    /*
     * this method will navigate user to main activity
     */
    private void navigateToMainActivity() {
        Intent intent = new Intent(SignupActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
