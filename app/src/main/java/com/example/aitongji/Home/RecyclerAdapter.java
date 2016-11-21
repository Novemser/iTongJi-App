package com.example.aitongji.Home;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.aitongji.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Novemser on 2016/11/21.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView;
        switch (viewType) {
            case 0:
                contactView = inflater.inflate(R.layout.home_information_row_1, parent, false);
                return new ViewHolder0(contactView);

            case 1:
                contactView = inflater.inflate(R.layout.home_information_row_2, parent, false);
                return new ViewHolder1(contactView);

            case 2:
                contactView = inflater.inflate(R.layout.home_information_row_3, parent, false);
                return new ViewHolder2(contactView);

            default:
                contactView = inflater.inflate(R.layout.home_information_row_1, parent, false);
                return new ViewHolder0(contactView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public static class ViewHolder0 extends RecyclerView.ViewHolder {
        @Bind(R.id.id_card_information)
        CardView cardInformation;
        @Bind(R.id.id_home_text_information_info_1)
        TextView textView1;
        @Bind(R.id.id_home_text_information_info_2)
        TextView textView2;
        @Bind(R.id.id_home_text_information_info_3)
        TextView textView3;
        @Bind(R.id.id_home_text_information_info_4)
        TextView textView4;
        public ViewHolder0(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

    public static class ViewHolder1 extends RecyclerView.ViewHolder {
        @Bind(R.id.id_card_week_time)
        CardView cardWeekTime;
        @Bind(R.id.id_card_course)
        CardView cardCourse;
        @Bind(R.id.id_card_card)
        CardView cardCard;
        @Bind(R.id.id_card_elect)
        CardView cardElect;
        public ViewHolder1(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

    public static class ViewHolder2 extends RecyclerView.ViewHolder {
        @Bind(R.id.gpa_avg)
        TextView textGpaAvg;
        @Bind(R.id.id_card_gpa)
        CardView cardGPA;
        public ViewHolder2(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
