package com.example.mobile_shopping.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mobile_shopping.HelperClass;
import com.example.mobile_shopping.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;

public class UserDetailFragment extends Fragment {

    public static String _uKey;
    private FirebaseUser _mCurrentUser;
    private TextView _uDisplayName, _uStatus;
    private ImageView _uImage;
    private Button _btnSendReq, _btnCancelReq, _btnAcceptReq, _btnDeclineReq, _btnRemoveFriends;

    private DatabaseReference _mFriendReqDb;
    private DatabaseReference _mFriendDb;
    private DatabaseReference _mReference;

    private String _mCurrentState;


    private String _userName, _userStatus, _userImage;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View _view = inflater.inflate(R.layout.user_detail_fragment_layout, null);

        _init(_view);

        return _view;
    }

    private void _init (View _view) {

        _mCurrentState = "not_friends";
        _mFriendReqDb = FirebaseDatabase.getInstance().getReference().child("friend_req");
        _mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        _uDisplayName = _view.findViewById(R.id.userDetailDisplayName);
        _uStatus = _view.findViewById(R.id.userDetailStatus);
        _uImage = _view.findViewById(R.id.userDetailPhoto);
        _btnSendReq = _view.findViewById(R.id.userDeatilSendRequest);
        _btnCancelReq = _view.findViewById(R.id.userDetailCancelRequest);
        _btnAcceptReq = _view.findViewById(R.id.userDetailAcceptRequest);
        _btnDeclineReq = _view.findViewById(R.id.userDetailDeclineRequest);
        _btnRemoveFriends = _view.findViewById(R.id.userDetailUnfriend);

        setButtonsVisibilities();

        _btnRemoveFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _btnRemoveFriendsClickListener();
            }
        });

        _btnSendReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("state : " + _mCurrentState);
                _btnSendReqClickListener();
            }
        });

        _btnCancelReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("state : " + _mCurrentState);
                _btnCancelReqClickListener();
            }
        });

        _btnAcceptReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("state : " + _mCurrentState);
                _btnAccepReqClickListener();
            }
        });

        _btnDeclineReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("state : " + _mCurrentState);
                _btnDeclineClickListener();
            }
        });

        _mReference = FirebaseDatabase.getInstance().getReference().child("_users").child(_uKey);
        _mFriendDb = FirebaseDatabase.getInstance().getReference().child("friends");
        _getUserInformations(_mReference);

    }

    private void setButtonsVisibilities() {
        if (_mCurrentState.equals("not_friends")) {
            _btnVisibilities(_btnSendReq, _btnAcceptReq, _btnDeclineReq, _btnCancelReq, _btnRemoveFriends);
        } else if (_mCurrentState.equals("req_sent")) {
            _btnVisibilities(_btnCancelReq, _btnSendReq, _btnDeclineReq, _btnAcceptReq, _btnRemoveFriends);
        } else if (_mCurrentState.equals("req_received")) {
            _btnVisibilities(_btnAcceptReq, _btnSendReq, _btnCancelReq, _btnRemoveFriends);
            _btnVisibilities(_btnDeclineReq, _btnSendReq, _btnCancelReq, _btnRemoveFriends);
        } else if (_mCurrentState.equals("friends")) {
            _btnVisibilities(_btnRemoveFriends, _btnSendReq, _btnCancelReq, _btnDeclineReq, _btnAcceptReq);
        }
    }


    private void _getUserInformations (DatabaseReference _mReference) {
        _mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                _userName = dataSnapshot.child("_name").getValue().toString();
                _userImage = dataSnapshot.child("_image").getValue().toString();
                _userStatus = dataSnapshot.child("_status").getValue().toString();

                _uDisplayName.setText(_userName);
                _uStatus.setText(_userStatus);
                HelperClass._loadImageFromUrl(getContext(), _userImage, _uImage);


                // get friend requests

                _mFriendReqDb.child(_mCurrentUser.getUid())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChild(_uKey)) {
                                    String req_type = dataSnapshot.child(_uKey).child("req_type").getValue().toString();
                                    if (req_type.equals("received")) {
                                        _mCurrentState = "req_received";

                                    } else if (req_type.equals("sent")) {
                                        _mCurrentState = "req_sent";
                                    }
                                }
                                setButtonsVisibilities();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void _btnVisibilities (Button... btn) {
        btn[0].setVisibility(View.VISIBLE);
        System.out.println("buton : " + btn[0].getText().toString());
        for (int i = 1; i < btn.length; i++) {
            btn[i].setVisibility(View.GONE);
        }
    }

    private void _btnSendReqClickListener () {
                if (_mCurrentState.equals("not_friends")) { // not belong friend yet
                    sendFriendReq();
                }
    }

    private void sendFriendReq () {
        _mFriendReqDb.child(_mCurrentUser.getUid()).child(_uKey)
                .child("req_type").setValue("sent")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            _mFriendReqDb.child(_uKey).child(_mCurrentUser.getUid()).child("req_type").setValue("received")
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            _mCurrentState = "req_sent";
                                            Toast.makeText(getContext(), "your friend request sent succesfully", Toast.LENGTH_SHORT).show();

                                            setButtonsVisibilities();
                                        }
                                    });
                        } else {
                            Toast.makeText(getContext(), "failed to send friend request", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void _btnCancelReqClickListener() {
                if (_mCurrentState.equals("req_sent")) {
                    _mFriendReqDb.child(_mCurrentUser.getUid()).child(_uKey).removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        _mFriendReqDb.child(_uKey).child(_mCurrentUser.getUid()).removeValue()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            _mCurrentState = "not_friends";

                                                            Toast.makeText(getContext(), "your friend request cancelled successfully", Toast.LENGTH_SHORT).show();

                                                        } else {
                                                            Toast.makeText(getContext(), "while cancelling the friend request some error occured", Toast.LENGTH_SHORT).show();
                                                        }

                                                        setButtonsVisibilities();
                                                    }
                                                });
                                    } else {
                                        Toast.makeText(getContext(), "while cancelling the friend request some error occured", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
    }

    private void _btnDeclineClickListener() {
        if (_mCurrentState.equals("req_received")) {
            _mFriendReqDb.child(_mCurrentUser.getUid()).child(_uKey).removeValue()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                _mFriendReqDb.child(_uKey).child(_mCurrentUser.getUid()).removeValue()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    _mCurrentState = "not_friends";

                                                    Toast.makeText(getContext(), "your friend request cancelled successfully", Toast.LENGTH_SHORT).show();

                                                } else {
                                                    Toast.makeText(getContext(), "while cancelling the friend request some error occured", Toast.LENGTH_SHORT).show();
                                                }

                                                setButtonsVisibilities();
                                            }
                                        });
                            } else {
                                Toast.makeText(getContext(), "while cancelling the friend request some error occured", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

    }

    private void _btnRemoveFriendsClickListener() {
        if (_mCurrentState.equals("friends")) {
            _mFriendDb.child(_mCurrentUser.getUid()).child(_uKey).removeValue()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                _mFriendDb.child(_uKey).child(_mCurrentUser.getUid()).removeValue()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    _mCurrentState = "not_friends";

                                                    Toast.makeText(getContext(), "your friend request cancelled successfully", Toast.LENGTH_SHORT).show();

                                                } else {
                                                    Toast.makeText(getContext(), "while cancelling the friend request some error occured", Toast.LENGTH_SHORT).show();
                                                }

                                                setButtonsVisibilities();
                                            }
                                        });
                            } else {
                                Toast.makeText(getContext(), "while cancelling the friend request some error occured", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

    }

    private void _btnAccepReqClickListener() {

        if (_mCurrentState.equals("req_received")) {
            final String _mcurrentDate = DateFormat.getDateTimeInstance().format(new Date());
            _mFriendDb.child(_mCurrentUser.getUid()).child(_uKey).setValue(_mcurrentDate)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                _mFriendDb.child(_uKey).child(_mCurrentUser.getUid()).setValue(_mcurrentDate)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    _mFriendReqDb.child(_uKey).child(_mCurrentUser.getUid())
                                                            .removeValue()
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    _mFriendReqDb.child(_mCurrentUser.getUid()).child(_uKey)
                                                                            .removeValue()
                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                    if (task.isSuccessful()) {
                                                                                        _mCurrentState = "friends";
                                                                                    } else {

                                                                                    }

                                                                                    setButtonsVisibilities();
                                                                                }
                                                                            });
                                                                }
                                                            });
                                                } else {

                                                }
                                            }
                                        });
                            } else {

                            }
                        }
                    });
        }

    }


}
