package com.example.rubel.stepstowardfirebase;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseAuth.AuthStateListener;

public class MainActivity extends AppCompatActivity {

    // Firebase auth clients
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentAuthUser;
    private AuthStateListener mAuthStateListener;

    // UI components
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUiComponents();

        initFirebaseAuthClients();

        verifyUserLogin();
    }

    /*
     * initialize firebase auth related clients
     */
    private void initFirebaseAuthClients() {
        mAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mCurrentAuthUser = firebaseAuth.getCurrentUser();

                // user is attempted to logout, make anonymous
                if(mCurrentAuthUser == null){
                    loginUserAnonymously();
                }
            }
        };
    }

    /*
     *  initialize ui components
     *  and add listeners if needed
     */
    private void initUiComponents() {
        // Hello Rubel
        mToolbar = (Toolbar) findViewById(R.id.toolbar_main);
        mToolbar.setTitle("Android Firebase");
        setSupportActionBar(mToolbar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // add auth state listener
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // remove auth state listener
        mAuth.removeAuthStateListener(mAuthStateListener);
    }

    /*
     * check if the user is logged in or not
     */
    private void verifyUserLogin() {
        mCurrentAuthUser = mAuth.getCurrentUser();
        // user is not authenticated yet make him anonymous
        if(mCurrentAuthUser == null){
            loginUserAnonymously();
        }else if(mCurrentAuthUser.isAnonymous()){
            TextView tv = (TextView) findViewById(R.id.tv_welcome_message);
            tv.setText("Your are logged in as a guest");
        }
    }

    /*
     * log in user as guest
     */
    private void loginUserAnonymously() {
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            // TODO turn on features for anonymous login
                            // also inform user thant he/she is logged in anonymously
                            TextView tv = (TextView) findViewById(R.id.tv_welcome_message);
                            tv.setText("Your are logged in as a guest");
                        }else {
                            // there is something wrong show message and close the app
                            // finish();
                            Log.d("ERROR", task.getException().toString());
                        }
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_logout){
            // TODO free resource if allocated
            if(mAuth.getCurrentUser() != null && !mAuth.getCurrentUser().isAnonymous())
            mAuth.signOut();
        }
        return super.onOptionsItemSelected(item);
    }
}
