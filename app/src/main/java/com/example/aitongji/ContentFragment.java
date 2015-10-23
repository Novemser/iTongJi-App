package com.example.aitongji;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.aitongji.R;

/**
 * Created by Novemser on 2015/9/26.
 */
public class ContentFragment extends Fragment {

    private TextView tv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_content, container, false);
        tv = (TextView) root.findViewById(R.id.tv);
        String text = getArguments().getString("text");
        tv.setText(text);
        return root;
    }
}
