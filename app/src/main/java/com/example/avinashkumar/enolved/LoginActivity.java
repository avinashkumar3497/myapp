package com.example.avinashkumar.enolved;

import android.content.Intent;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.common.api.ApiException;
import android.net.ConnectivityManager;
import android.content.Context;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    boolean isConnected;        //internet conn.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        final GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        setContentView(R.layout.activity_login);

        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                check();
                if (isConnected) {
                    // Code here executes on main thread after user presses button
                    Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                    startActivityForResult(signInIntent, 2);    //requestCode can have any value
                } else logfailed();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check for existing Google Sign In account, if the user is already signed in
// the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == 2) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("NOTSIGNED", "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    public void updateUI(GoogleSignInAccount account) {
        if (account != null) {
            // Log.d("SIGNED", "Already Signed In");
            //Log.d("NAME", account.getDisplayName());
            Intent intent = new Intent(this, DataEntry.class);
            startActivity(intent);
        }
    }

    public void logfailed() {                                                                       //To create toast, because toast can't be created inside onCreate-onClick
        Toast toast = Toast.makeText(this, "No connection", Toast.LENGTH_LONG);
        toast.show();
    }

    void check() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);       //to check internet connectivity
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();                                                 //to check internet connectivity
        isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();                 //to check internet connectivity
    }

}
