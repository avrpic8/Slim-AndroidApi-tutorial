package com.smartElectronics.slim.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.smartElectronics.slim.R;
import com.smartElectronics.slim.fragments.HomeFragment;
import com.smartElectronics.slim.fragments.SettingsFragment;
import com.smartElectronics.slim.fragments.UsersFragment;
import com.smartElectronics.slim.storage.SharedPref;

public class ProfileActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    private Toolbar toolbar;
    private BottomNavigationView navigationView;

    @Override
    protected void onStart() {
        super.onStart();

        if(!SharedPref.getInstance(getBaseContext()).isLoggedIn()) {
            Intent intent = new Intent(getBaseContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initViews();
        setFragment(new HomeFragment());
    }


    private void initViews(){

        toolbar = findViewById(R.id.toolbar_layout);
        toolbar.setTitle("Slim");
        toolbar.setTitleTextColor(getResources().getColor(R.color.black));
        setSupportActionBar(toolbar);

        navigationView = findViewById(R.id.bottom_nav);
        navigationView.setOnNavigationItemSelectedListener(this);
    }

    private void setFragment(Fragment fragment){

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Fragment fragment = null;

        switch (item.getItemId()){

            case R.id.home:
                fragment = new HomeFragment();
                item.setChecked(true);
                break;
            case R.id.users:
                fragment = new UsersFragment();
                item.setChecked(true);
                break;
            case R.id.settings:
                fragment = new SettingsFragment();
                item.setChecked(true);
                break;
        }

        if(fragment != null)
            setFragment(fragment);
        return false;
    }
}