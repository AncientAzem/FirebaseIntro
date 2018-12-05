package xyz.brenden.firebase101;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class FMCTokenService extends FirebaseInstanceIdService {

    private static final String TAG = "FMCTokenService";

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        LoginActivity.sharedPref = this.getSharedPreferences("xyz.brenden.firebase101", Context.MODE_PRIVATE);
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        String stored = LoginActivity.sharedPref.getString("notif_token", "");

        if(!FirebaseAuth.getInstance().getUid().equals(null)){
            if(stored.equals("") || (!stored.equals(refreshedToken))){
                addDeviceToken(refreshedToken);
            }
        }
    }

    private void addDeviceToken(final String token) {
        //Process Updated Token
    }
}
