package com.example.mobile_shopping.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_shopping.R;
import com.example.mobile_shopping.Screens.MainScreenActivity;
import com.example.mobile_shopping.Users;
import com.example.mobile_shopping.UsersViewHolder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AllUsersFragment extends Fragment implements UsersViewHolder.ClickListener {


    public static RecyclerView _mRecycler;
    private DatabaseReference _mUsersDatabase;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View _view = inflater.inflate(R.layout.all_users_layout, null);
        _init(_view);

        return _view;
    }

    private void _init(View _view) {
        _mRecycler = _view.findViewById(R.id.allUsersRecyclerView);
        _mRecycler.setHasFixedSize(true);
        _mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        _mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("_users");
        _mUsersDatabase.keepSynced(true);

        _readUsers();

    }

    public void _readUsers () {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.keepSynced(true);
        DatabaseReference usersRef = rootRef.child("_users");
        usersRef.keepSynced(true);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Users> list = new ArrayList<>();
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String key = ds.getKey();
                    System.out.println("key : " + key);
                    String name = ds.child("_name").getValue(String.class);
                    String image = ds.child("_image").getValue(String.class);
                    String status = ds.child("_status").getValue(String.class);
                    String thumb = ds.child("_thumb_image").getValue(String.class);
                    list.add(new Users(key, name, image, status, thumb));
                    System.out.println("\n veri : " + name + " / " + image + " / " + status);
                }
                _mRecycler.setAdapter(new UsersViewHolder(getContext(), list, AllUsersFragment.this));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        usersRef.addListenerForSingleValueEvent(eventListener);
    }

    @Override
    public void _recyclerClickListener(int _position) {
        MainScreenActivity._clearAllFragments(MainScreenActivity._mFragmentManager);

        UserDetailFragment _udFragment = new UserDetailFragment();
        _udFragment._uKey = UsersViewHolder._users.get(_position).get_key();

        MainScreenActivity.changeFragment(new UserDetailFragment(), MainScreenActivity._mFragmentTransaction, MainScreenActivity._mFragmentManager);
    }
}
