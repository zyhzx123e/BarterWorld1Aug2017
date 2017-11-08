package com.example.jason.barterworld.fcm;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by jason on 12/3/2017.
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static String TAG = "Registration";
    public static String fcm_token = FirebaseInstanceId.getInstance().getToken();

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // TODO: Implement this method to send any registration to your app's servers.
        Log.d(TAG, "Registration.onTokenRefresh TOKEN: " + refreshedToken );
    }
}
