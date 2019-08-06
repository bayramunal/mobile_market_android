package com.example.mobile_shopping;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.example.mobile_shopping.Screens.LoginScreenActivity;
import com.example.mobile_shopping.Screens.MainScreenActivity;
import com.google.android.material.textfield.TextInputLayout;

public class HelperClass {

    public static void _afterSucceedLogin(Activity activity) {
        activity.startActivity(new Intent(activity, MainScreenActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
    }

    public static void _afterLogOut (Activity activity) {
        activity.startActivity(new Intent(activity, LoginScreenActivity.class));
    }

    public static boolean _isFieldsEmpty (TextInputLayout... _fields) {
        boolean _isEmpty = false;
        for (TextInputLayout _field : _fields) {
            if (_field.getEditText().getText().toString().isEmpty())
                _isEmpty = true;

            _validateEditText(_field);
        }
        return _isEmpty;
    }

    public static void _showToast (Context _context, String message) {
        Toast.makeText(_context, message, Toast.LENGTH_SHORT).show();
    }

    private static boolean _validateEditText (TextInputLayout _textInput) {
        String _userEmail = _textInput.getEditText().getText().toString().trim();
        if (_userEmail.isEmpty()) {
            _textInput.setError("this field cannot be empty");
            return false;
        } else {
            _textInput.setError(null);
            return true;
        }
    }

    public static void _showDialog (Context _context, View _alertView) {
        //View _alertView = _activity.getLayoutInflater().inflate(_layoutResId, null);

        AlertDialog.Builder _alertBuilder = new AlertDialog.Builder(_context);
        _alertBuilder.setView(_alertView);

        // get alert items here

        AlertDialog _alertdialog = _alertBuilder.create();
        _alertdialog.show();

    }



}
