package com.example.mobile_shopping.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_shopping.Fragments.UsersFriendsFragment;
import com.example.mobile_shopping.Friends;
import com.example.mobile_shopping.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends FirebaseRecyclerAdapter<Friends, UserAdapter.UserHolder> {


    public UserAdapter(@NonNull FirebaseRecyclerOptions<Friends> options) {
        super(options);
    }


    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View _view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_users_fragment_item, parent, false);
        return new UserHolder(_view);
    }

    @Override
    protected void onBindViewHolder(@NonNull UserHolder userHolder, int i, @NonNull Friends users) {
        userHolder._userDisplayName.setText(users.get_name());
        userHolder._userStatus.setText(users.get_status());
    }

    class UserHolder extends RecyclerView.ViewHolder {

        View _mView;
        TextView _userDisplayName, _userStatus;
        CircleImageView _userImage;

        public UserHolder(@NonNull View itemView) {
            super(itemView);


            _userDisplayName = _mView.findViewById(R.id.allUsersDisplayName);
            _userStatus = _mView.findViewById(R.id.allUsersStatus);
            _userImage = _mView.findViewById(R.id.allUserPhoto);
        }
    }

}
