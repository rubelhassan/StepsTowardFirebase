package com.example.rubel.stepstowardfirebase;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

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

                // user is not authenticated or attempt to logout
                if(mCurrentAuthUser == null){
                    Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(loginIntent);
                    finish();
                }
            }
        };
    }

    /*
     *  initialize ui components
     *  and add listeners if needed
     */
    private void initUiComponents() {
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
        // user is not authenticated yet
        if(mCurrentAuthUser == null){
            Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
        }
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
            mAuth.signOut();
        }
        return super.onOptionsItemSelected(item);
    }
}
