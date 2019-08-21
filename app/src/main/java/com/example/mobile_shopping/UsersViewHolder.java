package com.example.mobile_shopping;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersViewHolder extends RecyclerView.Adapter<UsersViewHolder.MyViewHolder> {


    LayoutInflater inflater;
    Context context;

    public static ArrayList<Friends> _users;
    private ClickListener _mClickListener;

    public UsersViewHolder(Context context, ArrayList<Friends> users, ClickListener _mClickListener) {
        inflater = LayoutInflater.from(context);
        this._users = users;
        this.context = context;
        this._mClickListener = _mClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.all_users_fragment_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view, _mClickListener);
        return holder;
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Friends _user = _users.get(position);
        holder.setData(_user, position);

    }

    @Override
    public int getItemCount() {
        return _users.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView _displayName, _status;
        CircleImageView _userImg;
        ClickListener _mClickListener;

        public MyViewHolder(View itemView, ClickListener _mClickListener) {
            super(itemView);
            _displayName = itemView.findViewById(R.id.allUsersDisplayName);
            _status = itemView.findViewById(R.id.allUsersStatus);
            _userImg = itemView.findViewById(R.id.allUserPhoto);
            this._mClickListener = _mClickListener;

            itemView.setOnClickListener(this);

        }

        public void setData(Friends user, int position) {
            this._displayName.setText(user.get_name());
            this._status.setText(user.get_status());
            loadImageFromUrl(user.get_image(), _userImg);
        }

        @Override
        public void onClick(View view) {
            _mClickListener._recyclerClickListener(getAdapterPosition());
        }
    }

    private void loadImageFromUrl(String url, ImageView img)
    {
        Picasso.with(context).load(url).placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(img, new com.squareup.picasso.Callback()
                {

                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {

                    }
                });
    }

    // creating interface for click listener

    public interface ClickListener {
        void _recyclerClickListener(int _position);
    }

}
