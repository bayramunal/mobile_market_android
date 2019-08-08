package com.example.mobile_shopping.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mobile_shopping.HelperClass;
import com.example.mobile_shopping.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserDetailFragment extends Fragment {

    public static String _uKey;
    private DatabaseReference _mReference;
    TextView _uDisplayName, _uStatus;
    ImageView _uImage;

    private String _userName, _userStatus, _userImage;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View _view = inflater.inflate(R.layout.uset_detail_fragment_layout, null);

        _init(_view);

        return _view;
    }

    private void _init (View _view) {
        _uDisplayName = _view.findViewById(R.id.userDetailDisplayName);
        _uStatus = _view.findViewById(R.id.userDetailStatus);
        _uImage = _view.findViewById(R.id.userDetailPhoto);

        _mReference = FirebaseDatabase.getInstance().getReference().child("_users").child(_uKey);
        _getUserInformations(_mReference);

    }

    private void _getUserInformations (DatabaseReference _mReference) {
        _mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                _userName = dataSnapshot.child("_name").getValue().toString();
                _userImage = dataSnapshot.child("_image").getValue().toString();
                _userStatus = dataSnapshot.child("_status").getValue().toString();

                _uDisplayName.setText(_userName);
                _uStatus.setText(_userStatus);
                HelperClass._loadImageFromUrl(getContext(), _userImage, _uImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
