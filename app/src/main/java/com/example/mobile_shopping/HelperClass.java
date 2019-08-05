package com.example.mobile_shopping;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.mobile_shopping.Screens.LoginScreenActivity;
import com.example.mobile_shopping.Screens.MainScreenActivity;

public class HelperClass {

    public static void _afterSucceedLogin(Activity activity) {
        activity.startActivity(new Intent(activity, MainScreenActivity.class));
    }

    public static void _afterLogOut (Activity activity) {
        activity.startActivity(new Intent(activity, LoginScreenActivity.class));
    }

    public static boolean _isFieldsEmpty (String... _fields) {
        boolean _isEmpty = false;
        for (String _field : _fields) {
            if (_field.isEmpty()) {
                _isEmpty = true;
                break;
            }
        }
        return _isEmpty;
    }

    public static void _showToast (Context _context, String message) {
        Toast.makeText(_context, message, Toast.LENGTH_SHORT).show();
    }



}
