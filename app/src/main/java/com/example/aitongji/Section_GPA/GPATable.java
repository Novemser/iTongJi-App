package com.example.aitongji.Section_GPA;

import android.app.ActionBar;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.CollapsibleActionView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.transition.Explode;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.aitongji.R;
import com.example.aitongji.Utils.GPA.CourseGPA;
import com.example.aitongji.Utils.GPA.StudentGPA;
import com.github.mikephil.charting.data.Entry;

import butterknife.Bind;
import butterknife.ButterKnife;

public class GPATable extends AppCompatActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.gpa_tableView)
    TableLayout tableLayout;
    @Bind(R.id.toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;

    private StudentGPA studentGPA;

    private static final String[][] DATA_TO_SHOW = {{"This", "is", "a", "test"},
            {"and", "a", "second", "test"}, {"This", "is", "3", "test"}};
    private static final String[] HEADER = {"课程", "GPA", "", ""};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
//            getWindow().setEnterTransition( new Explode() );
            getWindow().setExitTransition(new Explode());
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpatable);
        ButterKnife.bind(this);
        studentGPA = (StudentGPA) getIntent().getSerializableExtra("studentGPA");

        setSupportActionBar(toolbar);

        collapsingToolbarLayout.setTitle("我的成绩");

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        TableRow header = new TableRow(this);
        header.addView(new TextView(this));
        tableLayout.addView(header);
        tableLayout.setColumnStretchable(0, true);
        tableLayout.setColumnStretchable(1, true);
        tableLayout.setColumnStretchable(2, true);
        tableLayout.setColumnStretchable(3, true);

        for (int i = 0; i < studentGPA.semesters.size(); i++) {
            Log.d("GPATable", "Semaster " + i + " has " + studentGPA.semesters.get(i).courseGPAs.size() + " courses.");
            TableRow tableRow = new TableRow(this);
            tableRow.setGravity(Gravity.CENTER);
            tableRow.setPadding(0, 10, 0, 10);
            tableRow.setBackgroundResource(R.drawable.shape_top_corner_no_bottom_line);
            TextView semesterTitle = new TextView(this);
            semesterTitle.setText(studentGPA.semesters.get(i).semaster_info);
            semesterTitle.setGravity(Gravity.CENTER);
            semesterTitle.setTextSize(18);
            semesterTitle.setTextColor(Color.BLACK);
            tableRow.addView(semesterTitle);

            TextView c1 = new TextView(this);
            TextView c2 = new TextView(this);
            TextView c3 = new TextView(this);
            TextView c4 = new TextView(this);
            c1.setGravity(Gravity.CENTER);
            c2.setGravity(Gravity.CENTER);
            c3.setGravity(Gravity.CENTER);
            c4.setGravity(Gravity.CENTER);
            c1.setText("课名");
            c2.setText("成绩");
            c3.setText("学分");
            c4.setText("绩点");
            c1.setPadding(2, 4, 2, 4);
            c2.setPadding(2, 4, 2, 4);
            c3.setPadding(2, 4, 2, 4);
            c4.setPadding(2, 4, 2, 4);
            c1.setTextColor(Color.BLACK);
            c2.setTextColor(Color.BLACK);
            c3.setTextColor(Color.BLACK);
            c4.setTextColor(Color.BLACK);
            header = new TableRow(this);
            header.addView(c1);
            header.addView(c2);
            header.addView(c3);
            header.addView(c4);
            header.setPadding(0, 10, 0, 10);
            header.setBackgroundResource(R.drawable.shape_no_corner_without_bottom);

            tableLayout.addView(tableRow);
            tableLayout.addView(header);
            for (int j = 0; j < studentGPA.semesters.get(i).courseGPAs.size(); j++) {
                CourseGPA courseGPA = studentGPA.semesters.get(i).courseGPAs.get(j);
                c1 = new TextView(this);
                c2 = new TextView(this);
                c3 = new TextView(this);
                c4 = new TextView(this);
                c1.setGravity(Gravity.CENTER);
                c2.setGravity(Gravity.CENTER);
                c3.setGravity(Gravity.CENTER);
                c4.setGravity(Gravity.CENTER);
                c1.setText(courseGPA.course_name);
                c2.setText(courseGPA.evaluation);
                c3.setText(String.valueOf(courseGPA.grade));
                c4.setText(String.valueOf(courseGPA.gpa));
                c1.setPadding(2, 8, 2, 8);
                c2.setPadding(2, 8, 2, 8);
                c3.setPadding(2, 8, 2, 8);
                c4.setPadding(2, 8, 2, 8);
                c1.setTextColor(Color.BLACK);
                c2.setTextColor(Color.BLACK);
                c3.setTextColor(Color.BLACK);
                c4.setTextColor(Color.BLACK);

                header = new TableRow(this);
                header.addView(c1);
                header.addView(c2);
                header.addView(c3);
                header.addView(c4);
                header.setPadding(0, 10, 0, 10);
                header.setBackgroundResource(R.drawable.shape_no_corner_without_bottom);

                tableLayout.addView(header);
            }
            header = new TableRow(this);
            TextView footer = new TextView(this);
            footer.setGravity(Gravity.CENTER);
            String gpa = String.valueOf(studentGPA.semesters.get(i).semaster_gpa);
            SpannableStringBuilder stringBuilder = new SpannableStringBuilder(gpa);
            stringBuilder.setSpan(new ForegroundColorSpan(Color.RED), 0, gpa.length(), Spanned.SPAN_COMPOSING);
            footer.setText(new SpannableStringBuilder("本学期平均绩点：").append(stringBuilder));
            footer.setTextSize(16);
            footer.setPadding(5, 5, 5, 5);
            header.setBackgroundResource(R.drawable.shape_bottom_corner_no_top_line);
            header.setPadding(0, 5, 0, 5);

            header.addView(footer);
            tableLayout.addView(header);
            header = new TableRow(this);
            header.addView(new TextView(this));
            tableLayout.addView(header);
        }

        header = new TableRow(this);
        header.addView(new TextView(this));
        tableLayout.addView(header);
        TextView avgGPA = new TextView(this);

        // 总平均绩点
        avgGPA.setTextSize(20);
        avgGPA.setGravity(Gravity.CENTER);
        SpannableStringBuilder sb = new SpannableStringBuilder(String.valueOf(studentGPA.total_gpa));
        sb.setSpan(new ForegroundColorSpan(Color.RED), 0, String.valueOf(studentGPA.total_gpa).length(), Spanned.SPAN_COMPOSING);
        avgGPA.setText(new SpannableStringBuilder("总平均绩点：").append(sb));
        avgGPA.setPadding(0, 15, 0, 15);
        header = new TableRow(this);
        header.addView(avgGPA);
        header.setGravity(Gravity.CENTER);
        tableLayout.addView(header);

        // 所修学分
        TextView maj = new TextView(this);
        TextView get = new TextView(this);
        TextView fail = new TextView(this);
        sb = new SpannableStringBuilder(String.valueOf(studentGPA.grade_majored));
        sb.setSpan(new ForegroundColorSpan(Color.RED), 0, String.valueOf(studentGPA.grade_majored).length(), Spanned.SPAN_COMPOSING);
        maj.setText(new SpannableStringBuilder("选课学分：").append(sb));
        maj.setGravity(Gravity.CENTER);

        sb = new SpannableStringBuilder(String.valueOf(studentGPA.grade_get));
        sb.setSpan(new ForegroundColorSpan(Color.RED), 0, String.valueOf(studentGPA.grade_get).length(), Spanned.SPAN_COMPOSING);
        get.setText(new SpannableStringBuilder("实修学分：").append(sb));
        get.setGravity(Gravity.CENTER);

        sb = new SpannableStringBuilder(String.valueOf(studentGPA.failed_courses));
        sb.setSpan(new ForegroundColorSpan(Color.RED), 0, String.valueOf(studentGPA.failed_courses).length(), Spanned.SPAN_COMPOSING);
        fail.setText(new SpannableStringBuilder("不及格门数：").append(sb));
        fail.setGravity(Gravity.CENTER);

        header = new TableRow(this);
        header.setPadding(0, 15, 0, 15);
        header.setGravity(Gravity.CENTER);
        header.addView(maj);
        tableLayout.addView(header);

        header = new TableRow(this);
        header.setPadding(0, 15, 0, 15);
        header.setGravity(Gravity.CENTER);
        header.addView(get);
        tableLayout.addView(header);

        header = new TableRow(this);
        header.setPadding(0, 15, 0, 15);
        header.setGravity(Gravity.CENTER);
        header.addView(fail);
        tableLayout.addView(header);
    }
}
