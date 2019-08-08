package com.example.mobile_shopping.Screens;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.mobile_shopping.Fragments.AllUsersFragment;
import com.example.mobile_shopping.Fragments.UserProfileFragment;
import com.example.mobile_shopping.HelperClass;
import com.example.mobile_shopping.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import id.zelory.compressor.Compressor;


public class MainScreenActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int GALLERY_IMAGE_REQUEST = 1;
    private DrawerLayout _drawerLayout;
    private FirebaseAuth _mAuth;
    private Toolbar _toolbar;
    private ActionBarDrawerToggle _toggle;
    private NavigationView _navigationView;
    public static Activity _mAct;
    public static FragmentTransaction _mFragmentTransaction;
    public static  FragmentManager _mFragmentManager;
    private StorageReference _mImageStorage;
    private FirebaseUser _currentUser;
    private DatabaseReference _mDatabase;

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
        _mDatabase = FirebaseDatabase.getInstance().getReference().child("_users").child(_currentUserId);

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

        switch (menuItem.getItemId()) {
            case R.id.logOut:
                _mAuth.signOut();
                HelperClass._afterLogOut(_mAct);
                break;
            case R.id.drawerMenuProfile:
                _clearAllFragments(_mFragmentManager);
                changeFragment(new UserProfileFragment(), _mFragmentTransaction, _mFragmentManager);
                break;
            case R.id.allUsers:
                _clearAllFragments(_mFragmentManager);
                changeFragment(new AllUsersFragment(), _mFragmentTransaction, _mFragmentManager);
                break;

        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == GALLERY_IMAGE_REQUEST && resultCode == RESULT_OK) {
            Uri _imageUri = data.getData();

            CropImage.activity(_imageUri)
                    .setAspectRatio(1, 1)
                    .start(this);

        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult _result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                HelperClass._showProgressDialog(this, "image is saving", "your image is saving in out database, please wait a moment");
                Uri _resultUri = _result.getUri();
                System.out.println("\n uri : " + _resultUri.toString());

                final StorageReference _filePath = _mImageStorage.child("profile_images").child(_currentUserId + ".jpg");


                _filePath.putFile(_resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        _filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                _mDatabase.child("_image").setValue(uri.toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(MainScreenActivity.this, "your image was successfully added to our storage", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        });
                        HelperClass._dismissDialog();
                    }

                });
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception _exception = _result.getError();
            }
        }
    }

    public static void changeFragment (Fragment _mFragment, FragmentTransaction _mFragmentTransaction, FragmentManager _mFragmentManager) {
        _mFragmentTransaction = _mFragmentManager.beginTransaction();
        _mFragmentTransaction.add(R.id.fragment_container, _mFragment);
        _mFragmentTransaction.commit();
    }

    public static void _clearAllFragments(FragmentManager _mFragmentManager) {
        for (Fragment fragment : _mFragmentManager.getFragments()) {
            _mFragmentManager.beginTransaction().remove(fragment).commit();
        }
    }
}
