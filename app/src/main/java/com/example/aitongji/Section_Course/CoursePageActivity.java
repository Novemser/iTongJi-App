package com.example.aitongji.Section_Course;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.aitongji.R;
import com.example.aitongji.Utils.Course.Course;
import com.example.aitongji.Utils.Managers.ResourceManager;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CoursePageActivity extends AppCompatActivity implements ScrollViewListener {

    @Bind(R.id.toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @Bind(R.id.month)
    TextView tvMonth;
    @Bind(R.id.course_week)
    TextView time_week_tv;
    @Bind(R.id.left_week)
    TextView lfWeek;
    @Bind(R.id.right_week)
    TextView rgWeek;
    @Bind(R.id.courses)
    CourseLayout layout;
    @Bind(R.id.scrollView_1)
    ObservableScrollView sv1;
    @Bind(R.id.scrollView_2)
    ObservableScrollView sv2;

    private int[] bg = {
            R.drawable.kb1,
            R.drawable.kb2,
            R.drawable.kb3,
            R.drawable.kb4,
            R.drawable.kb5,
            R.drawable.kb6,
            R.drawable.kb7};

    private int week;
    private int nowWeek;
    Calendar calendar = Calendar.getInstance();

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_page);
        ButterKnife.bind(this);
        // 切换周数
        lfWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (week == 1)
                    return;
                calendar.add(Calendar.DATE, -7);
                week -= 1;
                setContent();
                if (nowWeek != week)
                    time_week_tv.setText("第 " + String.valueOf(week) + " 周(非本周)");
            }
        });

        rgWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.DATE, 7);
                week += 1;
                setContent();
                if (nowWeek != week)
                    time_week_tv.setText("第 " + String.valueOf(week) + " 周(非本周)");
            }
        });

        collapsingToolbarLayout.setTitle("我的课表");

        // 设置周数
        if (ResourceManager.getInstance().getWeekStr() != null)
            nowWeek = week = Integer.parseInt(ResourceManager.getInstance().getWeekStr());
        else
            nowWeek = week = 1;
        setContent();

        sv1.setOnScrollViewListener(this);
        sv2.setOnScrollViewListener(this);
    }

    private void setContent() {
        layout.removeAllViews();

        Date nowDate = calendar.getTime();
        CharSequence month = DateFormat.format("MM", nowDate.getTime());
        tvMonth.setText(month + "月");
        time_week_tv.setText("第 " + String.valueOf(week) + " 周");

        ArrayList<ArrayList<Course>> courseTable = ResourceManager.getInstance().getCourseTableSubject().course_table;
        for (int i = 0; i < courseTable.size(); i++) {
            for (int j = 0; j < courseTable.get(i).size(); j++) {
                Course course = courseTable.get(i).get(j);
                if (course.is_single_week == 0 || (course.is_single_week == 1 && week % 2 == 1) || (course.is_single_week == 2 && week % 2 == 0)) {
                    CourseView view = new CourseView(getApplicationContext());
                    view.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    view.setCourseId(10);
                    view.setStartSection(course.start_time);
                    view.setEndSection(course.end_time);
                    view.setWeek(course.week_num + 1);
                    view.setIs_single_week(course.is_single_week);
                    int bgRes = bg[getRandom(bg.length - 1)];//随机获取背景色
                    view.setBackgroundResource(bgRes);
                    view.setText(course.course_name + "\n@" + course.classroom);
                    view.setTextColor(Color.WHITE);
                    view.setTextSize(12);
                    view.setGravity(Gravity.CENTER);
                    layout.addView(view);
                }
            }
        }
    }

    public static int getRandom(int max) {
        return (int) (Math.random() * max);
    }

    @Override
    public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
        if (scrollView == sv1) {
            sv2.scrollTo(x, y);
        } else if (scrollView == sv2) {
            sv1.scrollTo(x, y);
        }
    }
}
