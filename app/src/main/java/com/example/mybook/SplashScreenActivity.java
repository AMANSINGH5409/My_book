package com.example.mybook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

public class SplashScreenActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings remoteConfigSettings = new FirebaseRemoteConfigSettings.Builder()
                .setFetchTimeoutInSeconds(0)
                .build();
        remoteConfig.setConfigSettingsAsync(remoteConfigSettings);

        remoteConfig.fetchAndActivate().addOnSuccessListener(new OnSuccessListener<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
            }
        });

        Thread thread = new Thread(){
            public void run(){
                try {
                    sleep(1000);
                }catch (Exception e){
                    Toast.makeText(SplashScreenActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }finally {
                    Intent intent = new Intent(SplashScreenActivity.this,SignInActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };thread.start();

    }
}