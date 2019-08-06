package com.example.mobile_shopping.Screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.mobile_shopping.Fragments.UserProfileFragment;
import com.example.mobile_shopping.HelperClass;
import com.example.mobile_shopping.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainScreenActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout _drawerLayout;
    private FirebaseAuth _mAuth;
    private Toolbar _toolbar;
    private ActionBarDrawerToggle _toggle;
    private NavigationView _navigationView;
    private Activity _mAct;
    private FragmentTransaction _mFragmentTransaction;
    private FragmentManager _mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen_activity);
        _init();
    }

    private void _init() {
        _mAuth = FirebaseAuth.getInstance();
        _drawerLayout = findViewById(R.id.drawer_layout);
        _toolbar = findViewById(R.id.toolbar);
        _navigationView = findViewById(R.id.navView);

        _mAct = this;
        _mFragmentManager = getSupportFragmentManager();

        setSupportActionBar(_toolbar);
        _toggle = new ActionBarDrawerToggle(this, _drawerLayout, _toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        _drawerLayout.addDrawerListener(_toggle);
        _toggle.syncState();
        _navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        if (_drawerLayout.isDrawerOpen(GravityCompat.START))
            _drawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch(menuItem.getItemId()) {
            case R.id.logOut:
                _mAuth.signOut();
                HelperClass._afterLogOut(_mAct);
                break;
            case R.id.drawerMenuProfile:
                _mFragmentTransaction = _mFragmentManager.beginTransaction();
                UserProfileFragment _profileFragment = new UserProfileFragment();
                _mFragmentTransaction.add(R.id.fragment_container, _profileFragment, "_userFragment");
                _mFragmentTransaction.commit();
            break;

        }
        return true;
    }
}
