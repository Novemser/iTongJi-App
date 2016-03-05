package com.example.aitongji.Home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.aitongji.R;

import java.util.ArrayList;

/**
 * Created by Novemser on 2016/2/11.
 */
public class HomePageCards extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.home_fragment, container, false);

        RecyclerView mRecyclerView = (RecyclerView) root.findViewById(R.id.id_home_recyclerView);
        RecyclerView.Adapter mAdapter = new Home_Recycler_Adapter(getArguments());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
        mRecyclerView.setAdapter(mAdapter);

//        Bundle bundle = getArguments();
//        ArrayList<String> str = bundle.getStringArrayList("infoTitle");
//        System.out.println(str);

        return root;
    }
}
