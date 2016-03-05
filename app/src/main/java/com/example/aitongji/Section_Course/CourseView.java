package com.example.aitongji.Section_Course;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.Button;

import com.example.aitongji.R;

/**
 * Created by Novemser on 2/25/2016.
 */
public class CourseView extends Button {
    private int courseId;
    private int startSection;
    private int endSection;
    private int weekDay;
    private int is_single_week;

    public CourseView(Context context) {
        this(context, null);
    }

    public CourseView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CourseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CourseView);
        courseId = array.getInt(R.styleable.CourseView_courseId, 0);
        startSection = array.getInt(R.styleable.CourseView_startSection, 0);
        endSection = array.getInt(R.styleable.CourseView_endSection, 0);
        weekDay = array.getInt(R.styleable.CourseView_weekDay, 0);
        is_single_week = array.getInt(R.styleable.CourseView_isSingleWeek, 0);
        array.recycle();
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getStartSection() {
        return startSection;
    }

    public void setStartSection(int startSection) {
        this.startSection = startSection;
    }

    public int getEndSection() {
        return endSection;
    }

    public void setEndSection(int endSection) {
        this.endSection = endSection;
    }

    public int getWeek() {
        return weekDay;
    }

    public void setWeek(int week) {
        this.weekDay = week;
    }

    public int getIs_single_week() {
        return is_single_week;
    }

    public void setIs_single_week(int is_single_week) {
        this.is_single_week = is_single_week;
    }
}
