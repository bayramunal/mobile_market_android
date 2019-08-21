package com.example.mobile_shopping.Fragments;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_shopping.R;

class FriendsViewHolder extends RecyclerView.ViewHolder {

    View mView;
    TextView txtName;

    public FriendsViewHolder (View mView) {
        super(mView);
        this.mView = mView;
        txtName = mView.findViewById(R.id.usersFriendsTextName);
    }

    public void setDate(String date) {
        TextView userNameView = mView.findViewById(R.id.usersFriendsTextName);

    }

    public void setName (String name) {
        this.txtName.setText(name);
    }

    public void setUserOnline (String status) {
        ImageView mUserOnlineIcon = mView.findViewById(R.id.userActiveStatus);


        if (status.equals("true")) {
            mUserOnlineIcon.setVisibility(View.VISIBLE);
        } else {
            mUserOnlineIcon.setVisibility(View.INVISIBLE);
        }

    }

}
