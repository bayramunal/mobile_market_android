package com.example.mobile_shopping.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mobile_shopping.HelperClass;
import com.example.mobile_shopping.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileFragment extends Fragment {

    //firebase
    private DatabaseReference _mReference;
    private FirebaseUser _mUser;

    //progress
    private ProgressDialog _mProgressDialog;

    private String _userImage, _userName, _userStatus, _userThumImage;
    private CircleImageView _layoutUserImage;
    private TextView _layoutUserDisplayName, _layoutUserStatus;
    private Button _btnChangeStatus;
    private TextInputLayout _textInputStatus;
    private EditText _textInputEdt;

    private View _alertView;

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
        _mProgressDialog = new ProgressDialog(getContext());

        _layoutUserImage = _view.findViewById(R.id.userProfilePhoto);
        _layoutUserDisplayName = _view.findViewById(R.id.userProfileDisplayName);
        _layoutUserStatus = _view.findViewById(R.id.userProfileStatus);

        _btnChangeStatus = _view.findViewById(R.id.userProfileChangeStatusBtn);
        _btnChangeStatusClick(_btnChangeStatus);

        _alertView = getLayoutInflater().inflate(R.layout.status_alert_dialog_layout, null);
        _textInputStatus = _alertView.findViewById(R.id.changeStatusText);
    }

    public void _userProfileChangeStatusButton () {
        HelperClass._showDialog(getContext(), _alertView);

        Button _alertViewSaveStatus = _alertView.findViewById(R.id.statusSaveBtn);
        _alertViewButtonClick (_alertViewSaveStatus); // alertview button click event
    }

    private void _btnChangeStatusClick(Button _btn) {
        _btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _userProfileChangeStatusButton();
            }
        });
    }

    private void _alertViewButtonClick(Button _btn) {
        _btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //progress
                _mProgressDialog.setTitle("saving status");
                _mProgressDialog.setMessage("please wait, your status is updating...");
                _mProgressDialog.show();

                _textInputEdt = _textInputStatus.getEditText();
                String _newStatusText = _textInputEdt.getText().toString();

                _mReference.child("_status").setValue(_newStatusText).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(getContext(), "your status cant update right now, please try again later", Toast.LENGTH_SHORT).show();
                        }
                        _mProgressDialog.dismiss();
                    }
                });
            }
        });
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
