package com.example.mobile_shopping.Screens;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.mobile_shopping.Fragments.UserProfileFragment;
import com.example.mobile_shopping.HelperClass;
import com.example.mobile_shopping.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;


public class MainScreenActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int GALLERY_IMAGE_REQUEST = 1;
    private DrawerLayout _drawerLayout;
    private FirebaseAuth _mAuth;
    private Toolbar _toolbar;
    private ActionBarDrawerToggle _toggle;
    private NavigationView _navigationView;
    public static Activity _mAct;
    private FragmentTransaction _mFragmentTransaction;
    private FragmentManager _mFragmentManager;
    private StorageReference _mImageStorage;
    private FirebaseUser _currentUser;

    private String _currentUserId;

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

        _mAct = MainScreenActivity.this;
        _mFragmentManager = getSupportFragmentManager();
        _mImageStorage = FirebaseStorage.getInstance().getReference();
        _currentUser = FirebaseAuth.getInstance().getCurrentUser();
        _currentUserId = _currentUser.getUid();

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == GALLERY_IMAGE_REQUEST && resultCode == RESULT_OK) {
            Uri _imageUri = data.getData();

            CropImage.activity(_imageUri)
                    .setAspectRatio(1,1)
                    .start(this);

        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            HelperClass._showProgressDialog(this, "image is saving", "your image is saving in out database, please wait a moment");
            CropImage.ActivityResult _result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                Uri _resultUri = _result.getUri();
                System.out.println("\n uri : " + _resultUri.toString());
                StorageReference _filePath = _mImageStorage.child("profile_images").child(_currentUserId + ".jpg");
                _filePath.putFile(_resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (!task.isSuccessful())
                            Toast.makeText(MainScreenActivity.this, "error occur while file saving in database, please try again", Toast.LENGTH_SHORT).show();
                        else if (task.isSuccessful())
                            Toast.makeText(MainScreenActivity.this, "saved", Toast.LENGTH_SHORT).show();
                        HelperClass._dismissDialog();
                    }
                });
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception _exception = _result.getError();
            }

        }

    }
}
