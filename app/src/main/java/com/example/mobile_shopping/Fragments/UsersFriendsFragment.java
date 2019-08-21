package com.example.mobile_shopping.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_shopping.Adapter.UserAdapter;
import com.example.mobile_shopping.R;
import com.example.mobile_shopping.Screens.MainScreenActivity;
import com.example.mobile_shopping.UsersViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class UsersFriendsFragment extends Fragment implements UsersViewHolder.ClickListener {


    private String mCurrentUserId;
    private View mView;
    private RecyclerView mRecyclerView;
    private FirebaseAuth mAuth;
    private DatabaseReference mFriendsDatabase;
    private DatabaseReference mUsersDatabase;
    private FirebaseRecyclerOptions<com.example.mobile_shopping.Friends> mFirebaseOptions;
    private FirebaseRecyclerAdapter<com.example.mobile_shopping.Friends, FriendsViewHolder> friendsRecyclerAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.layout_friends_fragment, null);


        mAuth = FirebaseAuth.getInstance();
        mCurrentUserId = mAuth.getUid();

        mFriendsDatabase = FirebaseDatabase.getInstance().getReference().child("friends");
        mFriendsDatabase.keepSynced(true);
        Toast.makeText(getContext(), "mCurrentUserId : " + mCurrentUserId, Toast.LENGTH_SHORT).show();
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("_users");
        mUsersDatabase.keepSynced(true);


        adapter();

        //_readUsers();

/*

        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("friends");

        mFirebaseOptions = new
                FirebaseRecyclerOptions.Builder<Friends>()
                .setQuery(query, Friends.class).build();

        friendsRecyclerAdapter = new
                FirebaseRecyclerAdapter<Friends,FriendsViewHolder>(mFirebaseOptions) {
                    @Override
                    protected void onBindViewHolder(@NonNull FriendsViewHolder holder,
                                                    int position, @NonNull Friends model) {
                    }


                    @Override
                    public FriendsViewHolder onCreateViewHolder(ViewGroup parent, int
                            viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_friends_item,parent,false);

                        return new FriendsViewHolder(view);
                    }
                };
        friendsRecyclerAdapter.startListening();
        friendsRecyclerAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(friendsRecyclerAdapter);  */


        return mView;
    }


    public void adapter () {

        mRecyclerView = mView.findViewById(R.id.usersFriendsRecyclerView);
        //mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Query query = FirebaseDatabase.getInstance().getReference().child("friends");

        mFirebaseOptions = new FirebaseRecyclerOptions.Builder<com.example.mobile_shopping.Friends>()
                .setQuery(query, com.example.mobile_shopping.Friends.class).build();

        UserAdapter mAdapter = new UserAdapter(mFirebaseOptions);
        mRecyclerView.setAdapter(mAdapter);
    }


   /* public void _readUsers() {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.keepSynced(true);
        DatabaseReference usersRef = rootRef.child("friends");
        usersRef.keepSynced(true);

        final DatabaseReference userDbRef = rootRef.child("_users");
        userDbRef.keepSynced(true);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    final String key = ds.getKey();
                    if (!ds.getKey().equals(mCurrentUserId)) {
                        System.out.println("key : " + key);
                        ValueEventListener singleUserListener = new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                ArrayList<com.example.mobile_shopping.Friends> list = new ArrayList<>();
                                for(DataSnapshot innerDs : dataSnapshot.getChildren()) {
                                    String innerKey = innerDs.getKey();
                                    if (innerKey.equals(key)) {
                                        String name = innerDs.child("_name").getValue(String.class);
                                        String image = innerDs.child("_image").getValue(String.class);
                                        String status = innerDs.child("_status").getValue(String.class);
                                        String thumb = innerDs.child("_thumb_image").getValue(String.class);
                                        System.out.println("\n veri : " + name + " / " + image + " / " + status);
                                        list.add(new com.example.mobile_shopping.Friends(key, name, image, status, thumb));
                                    }
                                }
                                UserAdapter mHolder = new UserAdapter(getContext(), list, new UsersFriendsFragment());
                                mRecyclerView.setAdapter(mHolder);
                                mHolder.notifyDataSetChanged();*/
                          /*  }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        };
                        userDbRef.addListenerForSingleValueEvent(singleUserListener);
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        usersRef.addListenerForSingleValueEvent(eventListener);
    }*/


    @Override
    public void onStart() {
        super.onStart();

        /* */

        Toast.makeText(getContext(), "Friends friends fragment has been created" + mFriendsDatabase.child(mAuth.getCurrentUser().getUid()).getKey(), Toast.LENGTH_SHORT).show();

        mFirebaseOptions = new FirebaseRecyclerOptions.Builder<com.example.mobile_shopping.Friends>()
                .setQuery(mFriendsDatabase,com.example.mobile_shopping.Friends.class).build();
        friendsRecyclerAdapter = new FirebaseRecyclerAdapter<com.example.mobile_shopping.Friends, FriendsViewHolder>
                (
                        mFirebaseOptions
                ) {


            @Override
            protected void onBindViewHolder(@NonNull final FriendsViewHolder friendsViewHolder, int i, @NonNull final com.example.mobile_shopping.Friends friends) {
                friendsViewHolder.setDate("a");
                String list_user_id = getRef(i).getKey();

                mUsersDatabase.child(list_user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String userName = dataSnapshot.child("_name").getValue().toString();
                        //String userThumb = dataSnapshot.child("thumb_image").getValue().toString();
                        String userOnline = dataSnapshot.child("online").getValue().toString();

                        Toast.makeText(getContext(), "userName : " + userName, Toast.LENGTH_SHORT).show();

                        friendsViewHolder.setName(userName);
                        friendsViewHolder.setUserOnline(userOnline);

                        Toast.makeText(getContext(), "userName : " + userName, Toast.LENGTH_SHORT).show();


                        if (!dataSnapshot.hasChild("online")) {
                            boolean userStatus = (boolean) dataSnapshot.child("online").getValue();
                            friendsViewHolder.setUserOnline(String.valueOf(userStatus));
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @NonNull
            @Override
            public FriendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View mView = LayoutInflater.from(getContext()).inflate(R.layout.layout_friends_item, parent, false);
                return new FriendsViewHolder(mView);
            }
        };


        friendsRecyclerAdapter.startListening();


//        Toast.makeText(getContext(), "friendsRecyclerAdapter : " + friendsRecyclerAdapter.getItem(0).getDate(), Toast.LENGTH_SHORT).show();

        mRecyclerView.setAdapter(friendsRecyclerAdapter);


    }


    @Override
    public void onResume() {
        super.onResume();
        Toast.makeText(getContext(), "Friends friends fragment has been created", Toast.LENGTH_SHORT).show();
        if (friendsRecyclerAdapter != null)
            friendsRecyclerAdapter.startListening();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (friendsRecyclerAdapter != null)
            friendsRecyclerAdapter.stopListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (friendsRecyclerAdapter != null) {
            friendsRecyclerAdapter.stopListening();
        }
    }

    @Override
    public void _recyclerClickListener(int _position) {
        MainScreenActivity._clearAllFragments(MainScreenActivity._mFragmentManager);

        UserDetailFragment _udFragment = new UserDetailFragment();
        _udFragment._uKey = UsersViewHolder._users.get(_position).get_key();

        MainScreenActivity.changeFragment(new UserDetailFragment(), MainScreenActivity._mFragmentTransaction, MainScreenActivity._mFragmentManager);
    }
}




















