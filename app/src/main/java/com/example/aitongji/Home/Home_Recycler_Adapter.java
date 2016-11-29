package com.example.aitongji.Home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.aitongji.Model.StudentGPASubject;
import com.example.aitongji.R;
import com.example.aitongji.Section_Course.CoursePageActivity;
import com.example.aitongji.Section_Elect.ElectricityQuery;
import com.example.aitongji.Section_GPA.GPATable;
import com.example.aitongji.Section_Information.Card_Information;
import com.example.aitongji.Utils.Course.Course;
import com.example.aitongji.Utils.Course.CourseTable;
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
@Deprecated
@SuppressWarnings("all")
public class Home_Recycler_Adapter extends RecyclerView.Adapter<Home_Recycler_Adapter.ViewHolder> {
    private String time_today;
    private String time_week;
    private String card_rest;
    private String username;
    private String password;
    private String TAG = "Home Recycler view";
    private ArrayList<String> info_id = new ArrayList<>();
    private ArrayList<String> info_title = new ArrayList<>();
    private ArrayList<String> info_time = new ArrayList<>();

    private int week;

    private StudentGPASubject studentGPASubject;
    private Activity activity;

    public Home_Recycler_Adapter(Bundle infoBundle, Activity activity) {
        time_today = infoBundle.getString("timeToday");
        time_week = infoBundle.getString("timeWeek");
        info_title = infoBundle.getStringArrayList("infoTitle");
        info_time = infoBundle.getStringArrayList("infoTime");
        card_rest = infoBundle.getString("cardRest");
        username = infoBundle.getString("username");
        password = infoBundle.getString("password");
        info_id = infoBundle.getStringArrayList("info_id");
        try {
            week = Integer.parseInt(time_week);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            week = 0;
        }
        this.activity = activity;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.gpa_chart)
        LineChart chart;
        @Bind(R.id.gpa_avg)
        TextView textGpaAvg;
        @Bind(R.id.id_card_week_time)
        CardView cardWeekTime;
        @Bind(R.id.id_card_information)
        CardView cardInformation;
        @Bind(R.id.id_card_course)
        CardView cardCourse;
        @Bind(R.id.id_card_gpa)
        CardView cardGPA;
        @Bind(R.id.id_card_card)
        CardView cardCard;
        @Bind(R.id.id_card_elect)
        CardView cardElect;
        int cardCardCnt = 0;
        int cardWeekCnt = 0;

        private void setChartView() {
            chart.setNoDataText("绩点信息");
            chart.setDescription("");
            ArrayList<Entry> vals = new ArrayList<>();
            ArrayList<String> xVals = new ArrayList<>();
            for (int i = 0; i < studentGPASubject.semesters.size(); i++) {
                vals.add(new Entry(studentGPASubject.semesters.get(i).semaster_gpa, i));
                xVals.add("Sem." + (i + 1));
            }
            textGpaAvg.setText("总平均绩点:" + studentGPASubject.total_gpa);

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

        public ViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
//            studentGPASubject = AndroidResource.studentGPASubject;
            setChartView();

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

            cardWeekTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    YoYo.with(Techniques.Bounce).duration(700).playOn(cardWeekTime);
                    cardWeekCnt++;
                    if (cardWeekCnt == 5) {
                        Snackbar.make(itemView, "据说连续点击可以让假期更快到来(大雾", Snackbar.LENGTH_SHORT).show();
                        cardWeekCnt = 0;
                    }
                }
            });

            cardCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    YoYo.with(Techniques.Shake).duration(1000).playOn(cardCard);
                    cardCardCnt++;
                    if (cardCardCnt == 5) {
                        Snackbar.make(itemView, "再怎么点余额也不会变多嘛>.<", Snackbar.LENGTH_LONG).setAction("知道啦", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        }).show();
                        cardCardCnt = 0;
                    }
                }
            });

            cardCourse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(), CoursePageActivity.class);
                    intent.putExtra("time_week", time_week);

                    ActivityOptionsCompat optionsCompat =
                            ActivityOptionsCompat.makeSceneTransitionAnimation(activity, itemView.findViewById(R.id.ic_alarm), "alarm");
                    ActivityCompat.startActivity(activity, intent, optionsCompat.toBundle());
//                    itemView.getContext().startActivity(intent);
                }
            });


            cardGPA.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, GPATable.class);
                    intent.putExtra("studentGPASubject", studentGPASubject);

                    ActivityOptionsCompat optionsCompat =
                            ActivityOptionsCompat.makeSceneTransitionAnimation(activity, itemView.findViewById(R.id.ic_school), "school");
                    ActivityCompat.startActivity(activity, intent, optionsCompat.toBundle());
                }
            });

            cardElect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new MaterialDialog.Builder(MainActivity.getContext())
                            .title("小提示")
                            .content("电费只能在连接校园内网时查询哦")
                            .positiveText("好的")
                            .negativeText("取消")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(MaterialDialog dialog, DialogAction which) {
                                    itemView.getContext().startActivity(new Intent(itemView.getContext(), ElectricityQuery.class));
                                }
                            })
                            .show();

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
        CharSequence day = DateFormat.format("dd", nowDate.getTime());

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
        int nowTime = Integer.parseInt(new SimpleDateFormat("HHmm").format(nowDate));
//        System.out.println(nowTime);
        boolean flag = false;
//        System.out.println(courses_of_today.size());
        for (Course cour : courses_of_today) {
            System.out.println(cour.course_name);
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

        // 如果下节课程不在当日
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

        // Time
        TextView textView = (TextView) view.findViewById(R.id.id_text_week);
        textView.setText(context.getString(R.string.week_num, time_week));
        textView = (TextView) view.findViewById(R.id.id_text_date_and_week);
        textView.setText(context.getString(R.string.date_and_week, year, month, day, getWeekOfDate(nowDate)));

        // Course
        textView = (TextView) view.findViewById(R.id.id_text_course_time_and_name);
        textView.setText(context.getString(R.string.course_time_and_name, getStartTime(course.start_time), course.course_name));
        textView = (TextView) view.findViewById(R.id.id_text_course_place);
        textView.setText(context.getString(R.string.course_place, course.classroom));
        textView = (TextView) view.findViewById(R.id.id_text_course_day);
        textView.setText("周" + getWeekOfDate(course.week_num));

        // Information
//        if (info_time.size() >= 4 && info_title.size() >= 4) {
//            textView = (TextView) view.findViewById(R.id.id_home_text_information_info_1);
//            textView.setText(info_time.get(0) + " " + info_title.get(0));
//            textView = (TextView) view.findViewById(R.id.id_home_text_information_info_2);
//            textView.setText(info_time.get(1) + " " + info_title.get(1));
//            textView = (TextView) view.findViewById(R.id.id_home_text_information_info_3);
//            textView.setText(info_time.get(2) + " " + info_title.get(2));
//            textView = (TextView) view.findViewById(R.id.id_home_text_information_info_4);
//            textView.setText(info_time.get(3) + " " + info_title.get(3));
//        }
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

    public static String getWeekOfDate(int dt) {
        String[] weekDays = {"一", "二", "三", "四", "五", "六", "日"};
        if (dt < 0 || dt > 6)
            return "";
        return weekDays[dt];
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
