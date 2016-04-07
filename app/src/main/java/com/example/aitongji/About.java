package com.example.aitongji;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * Created by Novemser on 4/2/2016.
 */
public class About extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.about, container, false);
        RelativeLayout layout = (RelativeLayout) root.findViewById(R.id.share_app);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "查绩点再也不用输验证码，课表、一卡通、电费想查就查~快来试试iTongJi吧！客户端下载地址：http://zhushou.360.cn/detail/index/soft_id/3252384");
                startActivity(Intent.createChooser(shareIntent, "Share link using"));
            }
        });
        return root;
    }
}
