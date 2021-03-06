package com.example.mobile_shopping.Screens;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mobile_shopping.HelperClass;
import com.example.mobile_shopping.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;

public class LoginScreenActivity extends AppCompatActivity {

    private TextInputLayout _textInputEmail, _textInputPassword;
    private EditText _edtUserEmail, _edtUserPassword;
    private FirebaseAuth _mAuth;
    private Activity _mAct;
    private FirebaseUser _currentUser;
    private DatabaseReference _mDatabase;
    private FirebaseDatabase _mfirebaseDatabase;
    private DatabaseReference _mUserDatabase;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen_activity);
        _init();
    }

    private void _init() {
        _textInputEmail = findViewById(R.id.textInputEmail);
        _textInputPassword = findViewById(R.id.textInputPassword);

        _edtUserEmail = _textInputEmail.getEditText();
        _edtUserPassword = _textInputPassword.getEditText();

        _mAct = this;
        _mAuth = FirebaseAuth.getInstance();
        _mfirebaseDatabase = FirebaseDatabase.getInstance();
        _mUserDatabase = FirebaseDatabase.getInstance().getReference().child("_users");

        if (_checkCurrentUser())
            HelperClass._afterSucceedLogin(_mAct);
    }

    private boolean _checkCurrentUser() {
        return _mAuth.getCurrentUser() == null ? false : true;
    }

    private void _printErrorMessageWithException(String message, Exception _exception) {
        Toast.makeText(LoginScreenActivity.this, message + " " + _exception.getMessage(), Toast.LENGTH_SHORT).show();
    }


    public void _loginScreenCreateAccount(View view) {
        String _userEmail = _edtUserEmail.getText().toString();
        String _userPassword = _edtUserPassword.getText().toString();

        if (!HelperClass._isFieldsEmpty(_textInputEmail, _textInputPassword)) {
            _mAuth.createUserWithEmailAndPassword(_userEmail, _userPassword)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                _setUsersValueOnDatabase();
                                String _mDeviceToken = FirebaseInstanceId.getInstance().getToken();
                                String _mUserId = _mAuth.getCurrentUser().getUid();
                                _mUserDatabase.child(_mUserId).child("device_token").setValue(_mDeviceToken)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    HelperClass._afterSucceedLogin(_mAct);
                                                } else {
                                                    _printErrorMessageWithException("failed to sign in :", task.getException());
                                                }
                                            }
                                        });
                                HelperClass._afterSucceedLogin(_mAct);
                            } else {
                                _printErrorMessageWithException("failed to sign up :", task.getException());
                            }
                        }
                    });
        }
    }

    public void _loginScreenSignIn(View view) {
        String _userEmail = _edtUserEmail.getText().toString();
        String _userPassword = _edtUserPassword.getText().toString();

        if (!HelperClass._isFieldsEmpty(_textInputEmail, _textInputPassword)) {
            _mAuth.signInWithEmailAndPassword(_userEmail, _userPassword)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                String _mDeviceToken = FirebaseInstanceId.getInstance().getToken();
                                String _mUserId = _mAuth.getCurrentUser().getUid();

                                _mUserDatabase.child(_mUserId).child("device_token").setValue(_mDeviceToken)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    HelperClass._afterSucceedLogin(_mAct);
                                                } else {
                                                    _printErrorMessageWithException("failed to sign in :", task.getException());
                                                }
                                            }
                                        });

                            } else {
                                _printErrorMessageWithException("failed to sign in :", task.getException());
                            }
                        }
                    });
        }
    }

    private void _setUsersValueOnDatabase() {
        _currentUser = _mAuth.getCurrentUser();
        String _userId = _currentUser.getUid();
        _mDatabase = _mfirebaseDatabase.getReference().child("_users").child(_userId);

        HashMap<String, String> _userMap = new HashMap<>();
        _userMap.put("_name", _edtUserEmail.getText().toString());
        _userMap.put("_status", getString(R.string._created_user_status));
        _userMap.put("_image", "default");
        _userMap.put("_thumb_image", "default");

        _mDatabase.setValue(_userMap);
    }
}
