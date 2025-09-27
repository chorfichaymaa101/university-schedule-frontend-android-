package com.ensak.emploi;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static com.ensak.emploi.MainActivity.CLIENT_ID;
import static com.ensak.emploi.jwt.JwtUtils.getClaimFromToken;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import com.ensak.emploi.jwt.JwtUtils;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class Login extends AppCompatActivity {
    private GoogleSignInClient googleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        JwtUtils jwtUtils = new JwtUtils(getApplicationContext());
        String token = jwtUtils.getToken();
        TextView name = (TextView)findViewById(R.id.name);
        name.setText(getClaimFromToken(token, "role"));
        findViewById(R.id.logout).setOnClickListener(v -> logout());
    }
    
    public  void logout(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail() // Request email
                .requestIdToken(CLIENT_ID)
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        Log.e(TAG, "Sign-in failed: " + googleSignInClient);
        googleSignInClient.signOut()
                .addOnCompleteListener(this, task -> {
                    // Clear local user session or token
                    // Redirect user to the login screen or perform other actions
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();
                });
    }

   /*
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Uri data = intent.getData();
        if (data != null && "myapp".equals(data.getScheme())) {
            String token = data.getQueryParameter("token"); // e.g., ?token=jwt_token
            if (token != null) {
                saveToken(token);
                Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    

    private void saveToken(String token) {
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("authToken", token);
        editor.apply();
    }*/
   
}
