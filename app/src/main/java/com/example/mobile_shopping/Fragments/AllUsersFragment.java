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
import com.example.mobile_shopping.Users;
import com.example.mobile_shopping.UsersViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

public class AllUsersFragment extends Fragment {


    private RecyclerView _mRecycler;
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
                    String name = ds.child("_name").getValue(String.class);
                    String image = ds.child("_image").getValue(String.class);
                    String status = ds.child("_status").getValue(String.class);
                    list.add(new Users(name, "", status, ""));
                    System.out.println("\n veri : " + name + " / " + image + " / " + status);
                }
                _mRecycler.setAdapter(new UsersViewHolder(getContext(), list));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        usersRef.addListenerForSingleValueEvent(eventListener);
    }
}
