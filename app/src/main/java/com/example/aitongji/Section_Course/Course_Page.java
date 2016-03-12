package com.example.aitongji.Section_Course;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.example.aitongji.Base.BaseActivity;
import com.example.aitongji.Base.ToolbarActivity;
import com.example.aitongji.R;
import com.example.aitongji.Utils.Course;
import com.example.aitongji.Utils.CourseTable;
import com.example.aitongji.Utils.DensityUtil;

import java.util.ArrayList;

public class Course_Page extends AppCompatActivity implements ScrollViewListener {

    private int[] bg = {
            R.drawable.kb1,
            R.drawable.kb2,
            R.drawable.kb3,
            R.drawable.kb4,
            R.drawable.kb5,
            R.drawable.kb6,
            R.drawable.kb7};
    private CourseLayout layout;
    private TextView time_week_tv;
    private ObservableScrollView sv1, sv2;
    private int height;//布局高度
    private int week;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            getWindow().setEnterTransition(new Explode());
            getWindow().setExitTransition(new Explode());
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_table_layout);

        time_week_tv = (TextView) findViewById(R.id.course_week);
        time_week_tv.setText("第" + getIntent().getStringExtra("time_week") + "周");
        height = DensityUtil.dip2px(getApplicationContext(), 600);//默认高度600dp
        week = Integer.parseInt(getIntent().getStringExtra("time_week"));

        layout = (CourseLayout) findViewById(R.id.courses);
        ArrayList<ArrayList<Course>> courseTable = CourseTable.getInstance().course_table;
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

        sv1 = (ObservableScrollView) findViewById(R.id.scrollView_1);
        sv2 = (ObservableScrollView) findViewById(R.id.scrollView_2);
        sv1.setOnScrollViewListener(this);
        sv2.setOnScrollViewListener(this);
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
