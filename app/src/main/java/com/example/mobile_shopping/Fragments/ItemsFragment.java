package com.example.mobile_shopping.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_shopping.R;

public class ItemsFragment extends Fragment {

    RecyclerView _recyclerView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View _view = inflater.inflate(R.layout.items_fragment, null);

        _init(_view);


        return _view;
    }

    private void _init(View _view) {
        _recyclerView = _view.findViewById(R.id.itemsRecyclerView);
    }
}
