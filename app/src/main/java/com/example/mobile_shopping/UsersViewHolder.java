package com.example.mobile_shopping;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersViewHolder extends RecyclerView.Adapter<UsersViewHolder.MyViewHolder> {

    public TextView _userDisplayName, _userStatus;
    public CircleImageView _userImage;

    LayoutInflater inflater;
    Context context;

    ArrayList<Users> _users;

    public UsersViewHolder(Context context, ArrayList<Users> users) {
        inflater = LayoutInflater.from(context);
        this._users = users;
        this.context = context;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.all_users_fragment_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Users _user = _users.get(position);
        holder.setData(_user, position);
    }

    @Override
    public int getItemCount() {
        return _users.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView _displayName, _status;

        public MyViewHolder(View itemView) {
            super(itemView);
            _displayName = itemView.findViewById(R.id.allUsersDisplayName);
            _status = itemView.findViewById(R.id.allUsersStatus);
        }

        public void setData(Users user, int position) {
            this._displayName.setText(user.get_name());
            this._status.setText(user.get_status());
        }
    }

    private void loadImageFromUrl(String url, ImageView img)
    {
        Picasso.with(context).load(url).placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
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
}
