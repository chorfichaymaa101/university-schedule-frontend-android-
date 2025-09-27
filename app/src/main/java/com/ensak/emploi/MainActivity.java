package com.ensak.emploi;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import com.ensak.emploi.jwt.JwtUtils;
import com.ensak.emploi.model.AuthResponse;
import com.ensak.emploi.model.IdTokenRequest;
import com.ensak.emploi.retrofit.RetrofitService;
import com.ensak.emploi.service.AuthService;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "GoogleSignIn";
    private GoogleSignInClient googleSignInClient;
    private ActivityResultLauncher<Intent> googleSignInLauncher;
    public static final String CLIENT_ID = "868806523294-jngbc1tel77o9352h52og9c14u8hrt5j.apps.googleusercontent.com";

    @Override
    protected void onStart() {
        super.onStart();

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            // User is already signed in
            handleSignInResult(account);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Configure Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail() // Request email
                .requestIdToken(CLIENT_ID)
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        // Register the ActivityResultLauncher
        googleSignInLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        Log.e(TAG, "result: " + result);

                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            try {
                                // Retrieve the sign-in result
                                GoogleSignInAccount account = GoogleSignIn.getSignedInAccountFromIntent(result.getData())
                                        .getResult(ApiException.class);
                                // Successfully signed in
                                handleSignInResult(account);
                            } catch (ApiException e) {
                                // Handle sign-in errors
                                Toast.makeText(MainActivity.this, "Sign-in failed", Toast.LENGTH_SHORT).show();
                                Log.e(TAG, "Sign-in failed: " + e.getStatusCode(), e);
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Sign-in failed", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "Sign-in canceled or failed.");
                        }
                    }
                }
        );

        // Start Google Sign-In process
        findViewById(R.id.loginWithGmailButton).setOnClickListener(v -> signInWithGoogle());
    }

    private void signInWithGoogle() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        googleSignInLauncher.launch(signInIntent);
    }

    private void handleSignInResult(GoogleSignInAccount account) {
        if (account != null) {
            String idToken = account.getIdToken();
            String email = account.getEmail();
            String displayName = account.getDisplayName();

            // Send the ID token or user data to your backend
            Log.i(TAG, "ID Token: " + idToken);
            Log.i(TAG, "Email: " + email);
            Log.i(TAG, "Name: " + displayName);

            sendIdTokenToBackend(idToken);
        }
    }

    private void sendIdTokenToBackend(String idToken) {
        RetrofitService retrofit = new RetrofitService();

        AuthService authService = retrofit.getRetrofit().create(AuthService.class);
        System.out.println(authService.toString());
        Call<AuthResponse> call = authService.authenticateWithGoogle(new IdTokenRequest(idToken));
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful()) {
                    // Store JWT or handle successful login
                    String jwt = response.body().getJwt();
                    saveToken(jwt);
                    Log.d("Auth", "JWT: " + jwt);
                    decodeJwt(jwt);
                } else {
                    Log.e("Auth", "Authentication failed: " + response.message());
                    Toast.makeText(MainActivity.this, "Sign-in failed", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Log.e("Auth", "Error: " + t.getMessage());
                Toast.makeText(MainActivity.this, "Sign-in failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveToken(String token) {
        JwtUtils jwtUtils = new JwtUtils(getApplicationContext());
        jwtUtils.saveToken(token);
        Log.e("JWT12", "token saved");
    }

    //TODO: put this in another folder
    public void decodeJwt(String jwt) {
        try {
            // Split the JWT into its three parts
            String[] jwtParts = jwt.split("\\.");
            if (jwtParts.length < 2) {
                Log.e("JWT12", "Invalid JWT format");
                Toast.makeText(MainActivity.this, "Sign-in failed", Toast.LENGTH_SHORT).show();
                return;
            }

            // Decode the payload part (2nd part of the JWT)
            String payload = new String(Base64.decode(jwtParts[1], Base64.URL_SAFE));
            Log.d("JWT12", "Payload: " + payload);

            // Parse the payload as JSON
            JSONObject payloadJson = new JSONObject(payload);

            // Access specific claims (e.g., "sub", "name", "email")
            String subject = payloadJson.optString("sub");
            String role = payloadJson.optString("role");
            String email = payloadJson.optString("email");

            Log.d("JWT12", "Subject: " + subject);
            Log.d("JWT12", "role: " + role);
            Log.d("JWT12", "Email: " + email);

            Intent intent;
            if ("ADMIN".equalsIgnoreCase(role)) {
                intent = new Intent(this, Login.class);
                // Optionally pass data to the activity
                intent.putExtra("email", email);
                intent.putExtra("subject", subject);

                // Start the activity
                startActivity(intent);
            } else if ("STUDENT".equalsIgnoreCase(role)) {
                intent = new Intent(this, Login.class);
                // Optionally pass data to the activity
                intent.putExtra("email", email);
                intent.putExtra("subject", subject);

                // Start the activity
                startActivity(intent);
            } else if ("PROF".equalsIgnoreCase(role)) {
                intent = new Intent(this, Login.class);
                // Optionally pass data to the activity
                intent.putExtra("email", email);
                intent.putExtra("subject", subject);

                // Start the activity
                startActivity(intent);
            }else{
                Toast.makeText(MainActivity.this, "Sign-in failed", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Log.e("JWT12", "Error decoding JWT", e);
        }
    }


}
