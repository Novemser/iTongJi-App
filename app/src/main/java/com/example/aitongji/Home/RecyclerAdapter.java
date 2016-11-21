package com.example.aitongji.Home;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.aitongji.R;
import com.example.aitongji.Utils.Commons;
import com.example.aitongji.Utils.Course.Course;
import com.example.aitongji.Utils.Course.CourseTable;
import com.example.aitongji.Utils.Managers.ResourceManager;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

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
        switch (holder.getItemViewType()) {
            case 0:
                ViewHolder0 viewHolder0 = (ViewHolder0) holder;
                List<String> info_time = ResourceManager.getInstance().getInfoTimes();
                List<String> info_title = ResourceManager.getInstance().getInfoTitles();
                // Information
                if (info_time.size() >= 4 && info_title.size() >= 4) {
                    viewHolder0.textView1.setText(info_time.get(0) + " " + info_title.get(0));
                    viewHolder0.textView2.setText(info_time.get(1) + " " + info_title.get(1));
                    viewHolder0.textView3.setText(info_time.get(2) + " " + info_title.get(2));
                    viewHolder0.textView4.setText(info_time.get(3) + " " + info_title.get(3));
                }
                break;
            case 1:
                ViewHolder1 viewHolder1 = (ViewHolder1) holder;
                // 周数
                if (ResourceManager.getInstance().getWeekStr() != null) {
                    viewHolder1.textWeek.setText(ResourceManager.getInstance().getApplicationContext().getString(R.string.week_num, ResourceManager.getInstance().getWeekStr()));
                    int week = Integer.parseInt(ResourceManager.getInstance().getWeekStr());
                    if (week <= 18) {
                        viewHolder1.progressBar.setProgress((int) (week / 18.0 * 100));
                    } else {
                        viewHolder1.progressBar.setProgress(100);
                    }
                }
                // 余额
                if (ResourceManager.getInstance().getCardRest() != null)
                    viewHolder1.textCardRest.setText(ResourceManager.getInstance().getApplicationContext().getString(R.string.card_rest, ResourceManager.getInstance().getCardRest()));
                // 课表
                if (ResourceManager.getInstance().getCourseTable() != null && ResourceManager.getInstance().getWeekStr() != null) {
                    CourseTable courseTable = ResourceManager.getInstance().getCourseTable();
                    // 无课表返回
                    if (courseTable.course_table.size() <= 0)
                        return;

                    int week = Integer.parseInt(ResourceManager.getInstance().getWeekStr());
                    Date nowDate = new Date();
                    ArrayList<Course> courses_of_today = courseTable.course_table.get(Commons.getWeekOfDateDecimal(nowDate));
                    Collections.sort(courses_of_today, new Comparator<Course>() {
                        @Override
                        public int compare(Course lhs, Course rhs) {
                            return lhs.start_time > rhs.start_time ? 1 : (lhs.start_time == rhs.start_time ? 0 : -1);
                        }
                    });

                    // 获得下一节课程 通过当前时间判断
                    Course course = null;
                    int nowTime = Integer.parseInt(new SimpleDateFormat("HHmm").format(nowDate));
//        System.out.println(nowTime);
                    boolean flag = false;
//        System.out.println(courses_of_today.size());
                    for (Course cour : courses_of_today) {
                        System.out.println(cour.course_name);
                        int startTime = Integer.parseInt(Commons.getStartTime(cour.start_time).replace(":", ""));
                        if (startTime > nowTime) {
                            if (cour.is_single_week == 0 || (cour.is_single_week == 1 && week % 2 == 1) || (cour.is_single_week == 2 && week % 2 == 0)) {
                                course = cour;
                                flag = true;
                                break;
                            }
                        }
//            System.out.println(cour.course_name + " \t" + cour.week_num + cour.classroom + " \t" + cour.teacher_name + " \t" + cour.start_time + " \t" + cour.end_time);
                    }

                    // 如果下节课程不在当日
                    if (!flag) {
                        int i = 1;
                        while (CourseTable.getInstance().course_table.get((Commons.getWeekOfDateDecimal(nowDate) + i) % 7).size() == 0) {
                            i++;
                        }
                        courses_of_today = CourseTable.getInstance().course_table.get((Commons.getWeekOfDateDecimal(nowDate) + i) % 7);
                        Collections.sort(courses_of_today, new Comparator<Course>() {
                            @Override
                            public int compare(Course lhs, Course rhs) {
                                return lhs.start_time > rhs.start_time ? 1 : (lhs.start_time == rhs.start_time ? 0 : -1);
                            }
                        });
                        // 找到当日最早课程，注意处理单双周逻辑
                        for (Course cour : courses_of_today) {
//                Log.d(TAG, cour.course_name + " " + cour.is_single_week + " " + week % 2);
                            if (cour.is_single_week == 0 || (cour.is_single_week == 1 && week % 2 == 1) || (cour.is_single_week == 2 && week % 2 == 0)) {
                                course = cour;
                                flag = true;
                                break;
                            }
                        }
                        if (!flag)
                            course = courses_of_today.get(0);
                    }

                    // Course
                    viewHolder1.textCourseTimeAndName.setText(ResourceManager.getInstance().getApplicationContext().getString(R.string.course_time_and_name, Commons.getStartTime(course.start_time), course.course_name));
                    viewHolder1.textCoursePlace.setText(ResourceManager.getInstance().getApplicationContext().getString(R.string.course_place, course.classroom));
                    viewHolder1.textCourseDay.setText("周" + Commons.getWeekOfDate(course.week_num));

                }
                break;
            case 2:
                ViewHolder2 viewHolder2 = (ViewHolder2) holder;
                if (ResourceManager.getInstance().getGPATable() != null)
                    viewHolder2.setChartView();
                break;
            default:
                break;
        }
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
        @Bind(R.id.progressbar_week)
        MaterialProgressBar progressBar;
        @Bind(R.id.id_text_week)
        TextView textWeek;
        @Bind(R.id.id_text_date_and_week)
        TextView textDateAndWeek;
        @Bind(R.id.id_text_card_rest)
        TextView textCardRest;
        @Bind(R.id.id_text_course_time_and_name)
        TextView textCourseTimeAndName;
        @Bind(R.id.id_text_course_place)
        TextView textCoursePlace;
        @Bind(R.id.id_text_course_day)
        TextView textCourseDay;

        public ViewHolder1(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            Date nowDate = new Date();
            CharSequence year = DateFormat.format("yyyy", nowDate.getTime());
            CharSequence month = DateFormat.format("MM", nowDate.getTime());
            CharSequence day = DateFormat.format("dd", nowDate.getTime());

            textDateAndWeek.setText(ResourceManager.getInstance().getApplicationContext().getString(R.string.date_and_week, year, month, day, Commons.getWeekOfDate(nowDate)));
        }
    }

    public static class ViewHolder2 extends RecyclerView.ViewHolder {
        @Bind(R.id.gpa_avg)
        TextView textGpaAvg;
        @Bind(R.id.id_card_gpa)
        CardView cardGPA;
        @Bind(R.id.gpa_chart)
        LineChart chart;

        public ViewHolder2(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        private void setChartView() {
            chart.setNoDataText("绩点信息");
            chart.setDescription("");
            ArrayList<Entry> vals = new ArrayList<>();
            ArrayList<String> xVals = new ArrayList<>();
            for (int i = 0; i < ResourceManager.getInstance().getGPATable().semesters.size(); i++) {
                vals.add(new Entry(ResourceManager.getInstance().getGPATable().semesters.get(i).semaster_gpa, i));
                xVals.add("Sem." + (i + 1));
            }
            textGpaAvg.setText("总平均绩点:" + ResourceManager.getInstance().getGPATable().total_gpa);

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
            dataSet.setLineWidth(1.5f);

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

    }
}
