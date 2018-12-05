package xyz.brenden.firebase101;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.FirebaseApp;

import java.util.Arrays;
import java.util.List;

public class LoginActivity extends Activity {

    // Properties
    public static SharedPreferences sharedPref;
    private SharedPreferences.Editor edit;
    private static final int RC_SIGN_IN = 123;
    private static final String TAG = "LOGIN SCREEN";

    // Setup Auth Providers
    List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.GoogleBuilder().build());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.sharedPref = this.getSharedPreferences("xyz.brenden.firebase101", Context.MODE_PRIVATE);

        // Start Firebase and Launch Auth UI
        FirebaseApp.initializeApp(getApplicationContext());
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setIsSmartLockEnabled(!BuildConfig.DEBUG)
                        .build(),
                RC_SIGN_IN);
    }


    // Listen for Activity Results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check Request Code for Firebase Login
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data); // Use this to find out error detais

            // Check for Proper Login
            if (resultCode == RESULT_OK) {
                LaunchApp();
            } else {
                // Sign in failed, check response for error code
                Snackbar.make(getCurrentFocus(), "ERROR SIGNING IN (CODE " + response.getError().getErrorCode() + ")", Snackbar.LENGTH_SHORT);
                return;
            }
        }
    }

    // Start Application
    private void LaunchApp(){
        startActivity(new Intent(this, MainActivity.class));
        Toast.makeText(this, "Welcome to the demo. Enjoy!!!", Toast.LENGTH_SHORT).show();
        finish();
    }

}
