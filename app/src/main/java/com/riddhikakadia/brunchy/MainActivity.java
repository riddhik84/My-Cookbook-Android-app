package com.riddhikakadia.brunchy;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    public static final String ANONYMOUS = "anonymous";
    public static final int RC_SIGN_IN = 111;

    private final String LOG_TAG = MainActivity.class.getSimpleName();

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    View navigationHeader;
    ImageView user_account_photo;
    TextView user_account_name;
    TextView user_account_email;

    String mUsername;
    String mEmail;
    Uri mPhotoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationHeader = navigationView.getHeaderView(0);
        user_account_photo = (ImageView) navigationHeader.findViewById(R.id.user_account_photo);
        user_account_name = (TextView) navigationHeader.findViewById(R.id.user_account_name);
        user_account_email = (TextView) navigationHeader.findViewById(R.id.user_account_email);

        mUsername = ANONYMOUS;
        mEmail = "";
        mPhotoUrl = null;

        mFirebaseAuth = FirebaseAuth.getInstance();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                String fbUser = getString(R.string.new_user);
                String fbEmail = "";
                Uri fbUserPhoto = null;
                FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    //User logged in
                    Log.d(LOG_TAG, "RK Firebase User: " + mFirebaseAuth.getCurrentUser().toString());
                    Log.d(LOG_TAG, "RK firebase User id " + firebaseUser.getUid().toString());

                    Toast.makeText(getApplicationContext(), getString(R.string.welcome_toast), Toast.LENGTH_SHORT).show();
                    //Update drawer header with signed in user
                    if (firebaseUser.getDisplayName() != null) {
                        fbUser = firebaseUser.getDisplayName().toString();
                    }
                    if (firebaseUser.getEmail() != null) {
                        fbEmail = firebaseUser.getEmail().toString();
                    }
                    if (firebaseUser.getPhotoUrl() != null) {
                        fbUserPhoto = firebaseUser.getPhotoUrl();
                    }
                    onSignedInInitialize(fbUserPhoto, fbUser, fbEmail);

                } else {
                    //User not logged in - go to login screen
                    onSignedOutCleanup();

                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    //TODO change with app logo
                                    .setLogo(R.drawable.chef128)
                                    .setIsSmartLockEnabled(false)
                                    .setProviders(Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                            new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Log.d(LOG_TAG, "RK requestCode RC_SIGN_IN");
            if (resultCode == RESULT_OK) {
                Log.d(LOG_TAG, "RK resultCode RESULT_OK sign in");
            } else if (resultCode == RESULT_CANCELED) {
                Log.d(LOG_TAG, "RK resultCode RESULT_CANCELED sign in");
                Toast.makeText(this, "Sign In Canceled!", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    private void onSignedInInitialize(Uri photoUrl, String username, String useremail) {
        mPhotoUrl = photoUrl;
        mUsername = username;
        mEmail = useremail;

        Log.d(LOG_TAG, "RK onSignedInInitialize() mUsername " + mUsername + " mEmail " + mEmail + " mURL " + mPhotoUrl);

        //Update navigation drawer header
        if (mPhotoUrl != null) {
            Picasso.with(getApplicationContext())
                    .load(mPhotoUrl)
                    .resize(110, 110)
                    .centerCrop()
                    .into(user_account_photo);
        }
        user_account_name.setText(mUsername);
        user_account_email.setText(mEmail);

    }

    private void onSignedOutCleanup() {

    }
}
