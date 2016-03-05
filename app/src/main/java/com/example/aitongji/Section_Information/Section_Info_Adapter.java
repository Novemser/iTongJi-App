package com.example.aitongji.Section_Information;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.aitongji.R;

import java.util.ArrayList;

/**
 * Created by Novemser on 2015/11/27.
 */
public class Section_Info_Adapter extends RecyclerView.Adapter<Section_Info_Adapter.ViewHolder> {

    private ArrayList<String[]> mDataset;

    public Section_Info_Adapter(ArrayList<String[]> mDataset) {
        this.mDataset = mDataset;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.section_info_item, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.title.setText(mDataset.get(i)[0]);
        viewHolder.url.setText(mDataset.get(i)[1]);
        viewHolder.detail.setText(mDataset.get(i)[2]);
        viewHolder.circleWithColorText.setText(mDataset.get(i)[3]);
        viewHolder.circleWithColorText.setShapeColor(Color.parseColor(mDataset.get(i)[4]));
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView url;
        public TextView detail;
        public Circle_with_color_text circleWithColorText;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.id_section1_info_item_title);
            url = (TextView) itemView.findViewById(R.id.id_section1_info_item_url );
            detail = (TextView) itemView.findViewById(R.id.id_section1_info_item_detail);
            circleWithColorText = (Circle_with_color_text) itemView.findViewById(R.id.id_section1_circle_with_color_text);
        }
    }
}
