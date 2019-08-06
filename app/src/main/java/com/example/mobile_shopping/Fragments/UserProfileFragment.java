package com.example.mobile_shopping.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mobile_shopping.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileFragment extends Fragment {

    private DatabaseReference _mReference;
    private FirebaseUser _mUser;

    private String _userImage, _userName, _userStatus, _userThumImage;

    private CircleImageView _layoutUserImage;
    private TextView _layoutUserDisplayName, _layoutUserStatus;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View _view = inflater.inflate(R.layout.user_profile_fragment, null);

        _init(_view);
        _getUserInformations();

        return _view;
    }

    private void _init(View _view) {
        _mUser = FirebaseAuth.getInstance().getCurrentUser();

        String _currentUserId = _mUser.getUid();
        _mReference = FirebaseDatabase.getInstance().getReference().child("_users").child(_currentUserId);

        _layoutUserImage = _view.findViewById(R.id.userProfilePhoto);
        _layoutUserDisplayName = _view.findViewById(R.id.userProfileDisplayName);
        _layoutUserStatus = _view.findViewById(R.id.userProfileStatus);
    }

    private void _getUserInformations () {
        _mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                _userName = dataSnapshot.child("_name").getValue().toString();
                _userImage = dataSnapshot.child("_image").getValue().toString();
                _userStatus = dataSnapshot.child("_status").getValue().toString();
                _userThumImage = dataSnapshot.child("_thumb_image").getValue().toString();

                _layoutUserDisplayName.setText(_userName);
                _layoutUserStatus.setText(_userStatus);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
