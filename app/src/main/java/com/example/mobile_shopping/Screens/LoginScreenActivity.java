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

public class LoginScreenActivity extends AppCompatActivity {

    private TextInputLayout _textInputEmail, _textInputPassword;
    private EditText _edtUserEmail, _edtUserPassword;
    private FirebaseAuth _mAuth;
    private Activity _mAct;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen_activity);
        _init();
    }

    private void _init () {
        _textInputEmail = findViewById(R.id.textInputEmail);
        _textInputPassword = findViewById(R.id.textInputPassword);

        _edtUserEmail = _textInputEmail.getEditText();
        _edtUserPassword = _textInputPassword.getEditText();

        _mAct = this;
        _mAuth = FirebaseAuth.getInstance();

        if (_checkCurrentUser())
            HelperClass._afterSucceedLogin(_mAct);
    }

    private boolean _checkCurrentUser () {
        return _mAuth.getCurrentUser() == null ? false : true;
    }

    private void _printErrorMessage (String message, Exception _exception) {
        Toast.makeText(LoginScreenActivity.this, message + " " + _exception.getMessage(), Toast.LENGTH_SHORT).show();
    }

    private boolean _validateUserEmail () {
        String _userEmail = _edtUserEmail.getText().toString().trim();
        if (_userEmail.isEmpty()) {
            _textInputEmail.setError("this field cannot be empty");
            return false;
        } else {
            _textInputEmail.setError(null);
            return true;
        }
    }

    private boolean _validateUserPassword () {
        String _userPassword = _edtUserPassword.getText().toString().trim();
        if (_userPassword.isEmpty()) {
            _textInputPassword.setError("this field cannot be empty");
            return false;
        } else {
            _textInputPassword.setError(null);
            return true;
        }
    }

    public void btnSignUp (View view) {
        String _userEmail = _edtUserEmail.getText().toString();
        String _userPassword = _edtUserPassword.getText().toString();

        if (!HelperClass._isFieldsEmpty(_userEmail, _userPassword)) {
            _mAuth.createUserWithEmailAndPassword(_userEmail, _userPassword)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                HelperClass._afterSucceedLogin(_mAct);
                            } else {
                                _printErrorMessage("failed to sign up :", task.getException());
                            }
                        }
                    });
        } else {
            HelperClass._showToast(LoginScreenActivity.this, "please fill the blanks");
        }

    }

    public void btnSignIn (View view) {
        String _userEmail = _edtUserEmail.getText().toString();
        String _userPassword = _edtUserPassword.getText().toString();

        if (!HelperClass._isFieldsEmpty(_userEmail, _userPassword)) {
            _mAuth.signInWithEmailAndPassword(_userEmail, _userPassword)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                HelperClass._afterSucceedLogin(_mAct);
                            } else {
                                _printErrorMessage("failed to sign in :", task.getException());
                            }
                        }
                    });
        } else {
            HelperClass._showToast(LoginScreenActivity.this, "please fill the blanks");
        }
    }
}
