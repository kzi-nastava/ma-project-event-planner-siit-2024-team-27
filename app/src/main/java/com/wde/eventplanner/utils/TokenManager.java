package com.wde.eventplanner.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.security.crypto.EncryptedSharedPreferences.PrefValueEncryptionScheme;
import androidx.security.crypto.EncryptedSharedPreferences.PrefKeyEncryptionScheme;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import com.wde.eventplanner.models.user.UserRole;

import org.json.JSONObject;

import java.util.Base64;
import java.util.UUID;

public class TokenManager {
    private SharedPreferences sharedPreferences;
    private static TokenManager instance;
    private UUID profileId;
    private UserRole role;
    private String token;
    private UUID userId;

    private TokenManager(Context context) {
        try {
            sharedPreferences = EncryptedSharedPreferences.create("secure_prefs",
                    MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
                    context, PrefKeyEncryptionScheme.AES256_SIV, PrefValueEncryptionScheme.AES256_GCM);
        } catch (Exception e) {
            sharedPreferences = context.getSharedPreferences("fallback_prefs", Context.MODE_PRIVATE);
        }
        token = sharedPreferences.getString("token", null);
        deserializeToken();
    }

    private void deserializeToken() {
        try {
            String[] parts = this.token.split("\\.");
            if (parts.length == 3) {
                String payload = new String(Base64.getUrlDecoder().decode(parts[1]));
                String role = new JSONObject(payload).getJSONArray("roles").get(0).toString();
                String profileId = new JSONObject(payload).getString("profileId");
                String userId = new JSONObject(payload).getString("userId");
                this.role = UserRole.valueOf(role.toUpperCase());
                this.profileId = UUID.fromString(profileId);
                this.userId = UUID.fromString(userId);
                return;
            }
        } catch (Exception ignored) {
        }
        this.role = UserRole.ANONYMOUS;
        this.profileId = null;
        this.userId = null;
    }

    public static synchronized TokenManager getInstance(Context context) {
        if (instance == null) instance = new TokenManager(context);
        return instance;
    }

    public static void saveToken(String token, Context context) {
        TokenManager instance = getInstance(context);
        instance.token = token;
        instance.deserializeToken();
        instance.sharedPreferences.edit().putString("token", token).apply();
    }

    public static String getToken(Context context) {
        return getInstance(context).token;
    }

    public static UserRole getRole(Context context) {
        return getInstance(context).role;
    }

    public static UUID getProfileId(Context context) {
        return getInstance(context).profileId;
    }

    public static UUID getUserId(Context context) {
        return getInstance(context).userId;
    }

    public static void clearToken(Context context) {
        TokenManager instance = getInstance(context);
        instance.sharedPreferences.edit().remove("token").apply();
        instance.role = UserRole.ANONYMOUS;
        instance.profileId = null;
        instance.userId = null;
        instance.token = null;
    }
}
