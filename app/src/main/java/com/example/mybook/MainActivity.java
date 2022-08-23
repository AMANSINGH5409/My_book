package com.example.mybook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.mybook.Models.Notifications;
import com.example.mybook.fragments.BooksFragment;
import com.example.mybook.fragments.HomeFragment;
import com.example.mybook.fragments.ProfileFragment;
import com.example.mybook.fragments.UploadFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    BottomNavigationView bottomNavigationView;
    FirebaseAuth auth;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.main_bottom_nav);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        toolbar = findViewById(R.id.MainToolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String token) {
                Notifications notificationsToken = new Notifications(token);
                database.getReference().child("Notification").child(auth.getCurrentUser().getUid()).setValue(notificationsToken);
            }
        });

        FragmentTransaction Transaction = getSupportFragmentManager().beginTransaction();
        Transaction.replace(R.id.container,new HomeFragment());
        Transaction.commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction Transaction = getSupportFragmentManager().beginTransaction();
                switch(item.getItemId()){
                    case R.id.home:
                        Transaction.setCustomAnimations(R.anim.enter_from_bottom,R.anim.exit_from_top);
                        Transaction.replace(R.id.container,new HomeFragment());
                        break;
                    case R.id.upload:
                        Transaction.setCustomAnimations(R.anim.enter_from_right,R.anim.exit_from_left);
                        Transaction.replace(R.id.container,new UploadFragment());
                        break;
                    case R.id.books:
                        Transaction.setCustomAnimations(R.anim.enter_from_left,R.anim.exit_from_right);
                        Transaction.replace(R.id.container,new BooksFragment());
                        break;
                    case R.id.profile:
                        Transaction.setCustomAnimations(R.anim.enter_from_top,R.anim.exit_from_bottom);
                        Transaction.replace(R.id.container,new ProfileFragment());
                        break;
                }
                Transaction.commit();
                return true;
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.signout:
                auth.signOut();
                Toast.makeText(MainActivity.this, "User logged Out !", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this,SignInActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        return true;
    }


}