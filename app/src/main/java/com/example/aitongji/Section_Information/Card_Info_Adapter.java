package com.example.aitongji.Section_Information;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.aitongji.R;
import com.example.aitongji.Utils.Managers.ResourceManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Novemser on 2/22/2016.
 */
public class Card_Info_Adapter extends RecyclerView.Adapter<Card_Info_Adapter.ViewHolder> {

    private List<String> info_title = new ArrayList<>();
    private List<String> info_time = new ArrayList<>();
    private List<String> info_id = new ArrayList<>();
    private String username;
    private String password;
    private static final int DELAY = 138;
    private int mLastPosition = -1;

    public Card_Info_Adapter() {
        this.info_title = ResourceManager.getInstance().getNewsTitleSubject().getInfoTitles();
        this.info_time = ResourceManager.getInstance().getNewsTitleSubject().getInfoTimes();
        this.username = ResourceManager.getInstance().getUserName();
        this.password = ResourceManager.getInstance().getUserPwd();
        this.info_id = ResourceManager.getInstance().getNewsTitleSubject().getInfoIds();
    }

    @Override
    public Card_Info_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.card_info, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (position == 0) {
            holder.titleView.setVisibility(View.VISIBLE);
        } else {
            holder.titleView.setVisibility(View.GONE);
        }

        SpannableStringBuilder stringBuilder = new SpannableStringBuilder(info_time.get(position));
        stringBuilder.setSpan(new ForegroundColorSpan(Color.parseColor("#bdbdbd")), 0, 5, Spanned.SPAN_COMPOSING);
        holder.contentView.setText(new SpannableStringBuilder(info_title.get(position)).append(" ").append(stringBuilder));
        showItemAnim(holder.contentView, position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), WebActivity.class);
                intent.putExtra("position", holder.getAdapterPosition());
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return info_title.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.id_text_infoPage_title) TextView titleView;
        @Bind(R.id.id_text_infoPage_content) TextView contentView;

        public ViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            titleView.setText("选课网通知");
        }
    }

    public void showItemAnim(final View view, final int position) {
        final Context context = view.getContext();
        if (position > mLastPosition) {
            view.setAlpha(0);
            view.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Animation animation = AnimationUtils.loadAnimation(context,
                            R.anim.slide_in_right);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            view.setAlpha(1);
                        }


                        @Override
                        public void onAnimationEnd(Animation animation) {
                        }


                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }
                    });
                    view.startAnimation(animation);
                }
            }, DELAY * position);
            mLastPosition = position;
        }
    }
}
