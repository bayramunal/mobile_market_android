package com.example.mobile_shopping.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_shopping.R;

public class ItemListAdapter extends RecyclerView.Adapter<ItemListViewHolder> {



    @NonNull
    @Override
    public ItemListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View _itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_layout, parent);
        ItemListViewHolder _viewHolder = new ItemListViewHolder(_itemView);

        return _viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemListViewHolder holder, int position) {
        // String _item = list.get(position);
        // holder.item.setText(_item); // _viewHolder's item
    }

    @Override
    public int getItemCount() {
        //return  list.size();
        return 0;
    }
}
