package com.example.aitongji.Home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aitongji.R;
import com.example.aitongji.Section_Course.Course_Page;
import com.example.aitongji.Section_Information.Card_Information;
import com.example.aitongji.Utils.Course;
import com.example.aitongji.Utils.CourseTable;
import com.example.aitongji.Utils.DataHandler;
import com.example.aitongji.Utils.GPA.GetGPA;
import com.example.aitongji.Utils.GPA.StudentGPA;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

/**
 * Created by Novemser on 2016/2/1.
 */
public class Home_Recycler_Adapter extends RecyclerView.Adapter<Home_Recycler_Adapter.ViewHolder> {
    private String time_today = null;
    private String time_week = null;
    private String card_rest = null;
    private String username;
    private String password;
    private String course_table_str;
    private String TAG = "Home Recycler view";
    private ArrayList<String> info_id = new ArrayList<>();
    private ArrayList<String> info_title = new ArrayList<>();
    private ArrayList<String> info_time = new ArrayList<>();

    private int week;

    private StudentGPA studentGPA;

    public Home_Recycler_Adapter(Bundle infoBundle) {
        time_today = infoBundle.getString("timeToday");
        time_week = infoBundle.getString("timeWeek");
        info_title = infoBundle.getStringArrayList("infoTitle");
        info_time = infoBundle.getStringArrayList("infoTime");
        card_rest = infoBundle.getString("cardRest");
        username = infoBundle.getString("username");
        password = infoBundle.getString("password");
        info_id = infoBundle.getStringArrayList("info_id");
        course_table_str = infoBundle.getString("course_table_str");
        week = Integer.parseInt(time_week);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.gpa_chart)
        LineChart chart;
        @Bind(R.id.gpa_avg)
        TextView textGpaAvg;

        private void setChartView() {
            ArrayList<Entry> vals = new ArrayList<>();
            ArrayList<String> xVals = new ArrayList<>();
            for (int i = 0; i < studentGPA.semesters.size(); i++) {
                Log.d(TAG, "Semaster " + i + " has " + studentGPA.semesters.get(i).courseGPAs.size() + " courses.");
                vals.add(new Entry(studentGPA.semesters.get(i).semaster_gpa, i));
                xVals.add("Sem." + (i + 1));
            }
            textGpaAvg.setText("总平均绩点:" + studentGPA.total_gpa);

            LineDataSet dataSet = new LineDataSet(vals, "GPA");
            dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(dataSet);

            LineData data = new LineData(xVals, dataSets);
            data.setValueTextColor(Color.WHITE);
            data.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                    return String.valueOf(value);
                }
            });
            data.setValueTextSize(12);
            dataSet.setDrawCircleHole(true);
            dataSet.setCircleColorHole(ContextCompat.getColor(MainActivity.getContext(), R.color.home_gpa_table_background));
            dataSet.setDrawCubic(true);
            dataSet.setDrawFilled(true);
            dataSet.setFillDrawable(ContextCompat.getDrawable(MainActivity.getContext(), R.drawable.gpa_table_bg));

            XAxis xAxis = chart.getXAxis();
            YAxis left = chart.getAxisLeft();
            left.setDrawLabels(false); // no axis labels
            left.setDrawAxisLine(false); // no axis line
            left.setDrawGridLines(false); // no grid lines
            left.setDrawZeroLine(true); // draw a zero line
            left.setGridColor(Color.LTGRAY);
            chart.getAxisRight().setEnabled(false); // no right axis
            Legend legend = chart.getLegend();
            legend.setEnabled(false);

            chart.setData(data);
            chart.setTouchEnabled(false);
            chart.setDrawGridBackground(false);
            chart.setBackgroundColor(Color.TRANSPARENT);
            chart.setDrawBorders(false);
            chart.animateY(1000, Easing.EasingOption.EaseOutBack);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setAvoidFirstLastClipping(false);
            xAxis.setTextColor(Color.WHITE);
            xAxis.setGridColor(Color.WHITE);
            xAxis.setDrawGridLines(false);
            xAxis.setAxisLineColor(Color.TRANSPARENT);
            chart.invalidate();
        }

        public ViewHolder(final View itemView) {
            super(itemView);
            final SharedPreferences preferences = MainActivity.getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
            ButterKnife.bind(this, itemView);
            studentGPA = (StudentGPA) DataHandler.getObject(MainActivity.getContext(), "studentGPA.dat");

            CardView cardInformation = (CardView) itemView.findViewById(R.id.id_card_information);
            cardInformation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(), Card_Information.class);
                    intent.putStringArrayListExtra("info_title", info_title);
                    intent.putStringArrayListExtra("info_time", info_time);
                    intent.putStringArrayListExtra("info_id", info_id);
                    intent.putExtra("username", username);
                    intent.putExtra("password", password);
                    itemView.getContext().startActivity(intent);
                }
            });

            CardView cardWeekTime = (CardView) itemView.findViewById(R.id.id_card_week_time);
            cardWeekTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(itemView.getContext(), "WeekTime Clicked", Toast.LENGTH_SHORT).show();
                }
            });

            final CardView cardCourse = (CardView) itemView.findViewById(R.id.id_card_course);
            cardCourse.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(), Course_Page.class);
                    intent.putExtra("time_week", time_week);
                    itemView.getContext().startActivity(intent);
                }
            });

            CardView cardGPA = (CardView) itemView.findViewById(R.id.id_card_gpa);
            chart.setNoDataText("点击抓取绩点信息……");
            chart.setDescription("");
            setChartView();
            cardGPA.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

    }

    @Override
    public Home_Recycler_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.home_recyclerview_items, parent, false);

        Date nowDate = new Date();
        CharSequence year = DateFormat.format("yyyy", nowDate.getTime());
        CharSequence month = DateFormat.format("MM", nowDate.getTime());
        CharSequence day = DateFormat.format("d", nowDate.getTime());
        CharSequence hour = DateFormat.format("hh", nowDate.getTime());
        CharSequence minutes = DateFormat.format("mm", nowDate.getTime());

//        CharSequence week = getWeekOfDate(nowDate);
//        System.out.println("Hours:" + hour + " Minutes" + minutes);
//        System.out.println("Week:" + week);
//        System.out.println("Week Num:" + getWeekOfDateDecimal(nowDate));
        ArrayList<Course> courses_of_today = CourseTable.getInstance().course_table.get(getWeekOfDateDecimal(nowDate));
        Collections.sort(courses_of_today, new Comparator<Course>() {
            @Override
            public int compare(Course lhs, Course rhs) {
                return lhs.start_time > rhs.start_time ? 1 : (lhs.start_time == rhs.start_time ? 0 : -1);
            }
        });

        // 获得下一节课程 通过当前时间判断
        Course course = null;
        int nowTime = Integer.parseInt(DateFormat.format("HHmm", nowDate.getTime()).toString());
//        System.out.println(nowTime);
        boolean flag = false;
//        System.out.println(courses_of_today.size());
        for (Course cour : courses_of_today) {
            int startTime = Integer.parseInt(getStartTime(cour.start_time).replace(":", ""));
            if (startTime > nowTime) {
                if (cour.is_single_week == 0 || (cour.is_single_week == 1 && week % 2 == 1) || (cour.is_single_week == 2 && week % 2 == 0)) {
                    course = cour;
                    flag = true;
                    break;
                }
            }
//            System.out.println(cour.course_name + " \t" + cour.week_num + cour.classroom + " \t" + cour.teacher_name + " \t" + cour.start_time + " \t" + cour.end_time);
        }

        if (!flag) {
            int i = 1;
            while (CourseTable.getInstance().course_table.get((getWeekOfDateDecimal(nowDate) + i) % 7).size() == 0) {
                i++;
            }
            courses_of_today = CourseTable.getInstance().course_table.get((getWeekOfDateDecimal(nowDate) + i) % 7);
            Collections.sort(courses_of_today, new Comparator<Course>() {
                @Override
                public int compare(Course lhs, Course rhs) {
                    return lhs.start_time > rhs.start_time ? 1 : (lhs.start_time == rhs.start_time ? 0 : -1);
                }
            });
            course = courses_of_today.get(0);
        }

        TextView textView = (TextView) view.findViewById(R.id.id_text_week);
        textView.setText(context.getString(R.string.week_num, time_week));
        textView = (TextView) view.findViewById(R.id.id_text_date_and_week);
        textView.setText(context.getString(R.string.date_and_week, year, month, day, getWeekOfDate(nowDate)));
        textView = (TextView) view.findViewById(R.id.id_text_course_time_and_name);
        textView.setText(context.getString(R.string.course_time_and_name, getStartTime(course.start_time), course.course_name));
        textView = (TextView) view.findViewById(R.id.id_text_course_place);
        textView.setText(context.getString(R.string.course_place, course.classroom));

        // Information
        textView = (TextView) view.findViewById(R.id.id_home_text_information_info_1);
        textView.setText(info_time.get(0) + " " + info_title.get(0));
        textView = (TextView) view.findViewById(R.id.id_home_text_information_info_2);
        textView.setText(info_time.get(1) + " " + info_title.get(1));
        textView = (TextView) view.findViewById(R.id.id_home_text_information_info_3);
        textView.setText(info_time.get(2) + " " + info_title.get(2));
        textView = (TextView) view.findViewById(R.id.id_home_text_information_info_4);
        textView.setText(info_time.get(3) + " " + info_title.get(3));

        // Card Information
        textView = (TextView) view.findViewById(R.id.id_text_card_rest);
        textView.setText(context.getString(R.string.card_rest, card_rest));

        // ProgressBar
        MaterialProgressBar progressBar = (MaterialProgressBar) view.findViewById(R.id.progressbar_week);
        if (week <= 18) {
            progressBar.setProgress((int) (week / 18.0 * 100));
        } else {
            progressBar.setProgress(100);
        }

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Home_Recycler_Adapter.ViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public static String getWeekOfDate(Date dt) {
        String[] weekDays = {"日", "一", "二", "三", "四", "五", "六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    public static int getWeekOfDateDecimal(Date dt) {
        int[] weekDays = {6, 0, 1, 2, 3, 4, 5};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    public String getStartTime(int num) {
        switch (num) {
            case 1:
                return "08:00";
            case 2:
                return "08:55";
            case 3:
                return "10:00";
            case 4:
                return "10:55";
            case 5:
                return "13:30";
            case 6:
                return "14:20";
            case 7:
                return "15:25";
            case 8:
                return "16:15";
            case 9:
                return "18:30";
            case 10:
                return "19:25";
            default:
                return "00:00";
        }
    }
}
