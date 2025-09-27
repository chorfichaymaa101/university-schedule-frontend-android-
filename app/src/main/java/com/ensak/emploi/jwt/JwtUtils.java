package com.ensak.emploi.jwt;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import com.auth0.jwt.JWT;

public class JwtUtils {
    private static final String PREF_NAME = "AppPrefs";
    private static final String TOKEN_KEY = "authToken";

    private final SharedPreferences sharedPreferences;

    public JwtUtils(Context context) {
        try {
            // Create a MasterKey for encryption
            MasterKey masterKey = new MasterKey.Builder(context)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();

            // Create EncryptedSharedPreferences instance
            sharedPreferences = EncryptedSharedPreferences.create(
                    context,
                    PREF_NAME, // Name of the shared preferences file
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize EncryptedSharedPreferences", e);
        }
    }

    // Save the token securely
    public void saveToken(String token) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TOKEN_KEY, token);
        editor.apply();
    }

    // Retrieve the token
    public String getToken() {
        return sharedPreferences.getString(TOKEN_KEY, null);
    }

    public static String getClaimFromToken(String token, String claimName) {
        try {
            JWT jwt = new JWT(); // Parse the JWT
            return jwt.decodeJwt(token).getClaim(claimName).asString(); // Extract the specified claim as a String
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Return null if decoding fails
        }
    }
}
