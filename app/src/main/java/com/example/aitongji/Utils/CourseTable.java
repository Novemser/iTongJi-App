package com.example.aitongji.Utils;

import java.util.ArrayList;

/**
 * Created by Novemser on 2/23/2016.
 */
public class CourseTable {
    private static CourseTable instance = null;
    public ArrayList<ArrayList<Course>> course_table = new ArrayList<>();

    public static CourseTable newInstance() {
        synchronized (CourseTable.class) {
            if (instance == null) {
                instance = new CourseTable();
            }
        }
        return instance;
    }

    public static CourseTable getInstance() {
        if (instance == null) {
            synchronized (CourseTable.class) {
                if (instance == null) {
                    instance = new CourseTable();
                }
            }
        }
        return instance;
    }

    private CourseTable() {
        for (int i = 0; i < 7; i++) {
            course_table.add(new ArrayList<Course>());
        }
    }

    public void addCourse(String courseName, String teacher, String week, String startToEnd, String startEndWeek, String place) {
        int index;

        switch (week) {
            case "星期一":
                index = 0;
                break;
            case "星期二":
                index = 1;
                break;
            case "星期三":
                index = 2;
                break;
            case "星期四":
                index = 3;
                break;
            case "星期五":
                index = 4;
                break;
            case "星期六":
                index = 5;
                break;
            case "星期日":
                index = 6;
                break;
            default:
                index = 0;
        }

        ArrayList<Course> temp = course_table.get(index);
        Course course = new Course();
        course.classroom = place;
        course.week_num = index;
        course.course_name = courseName;
        course.teacher_name = teacher;

        startToEnd = startToEnd.replace("[", "");
        startToEnd = startToEnd.replace("]", "");
        String tmpStr[] = startToEnd.split("-");
        course.start_time = Integer.parseInt(tmpStr[0]);
        course.end_time = Integer.parseInt(tmpStr[1]);

        tmpStr = startEndWeek.split("\\[");
//        System.out.println("Left:" + tmpStr[0].length() + "Right:" + tmpStr[1]);
        if (tmpStr[0].length() == 0) {
            course.is_single_week = 0;
        } else {
            switch (tmpStr[0]) {
                case "单":
                    course.is_single_week = 1;
                    break;
                case "双":
                    course.is_single_week = 2;
                    break;
            }
        }
        startEndWeek = tmpStr[1].replace("]", "");
        tmpStr = startEndWeek.split("-");
        course.start_week = Integer.parseInt(tmpStr[0]);
        course.end_week = Integer.parseInt(tmpStr[1]);
//        System.out.println("startWeek:" + tmpStr[0] + " endWeek:" + tmpStr[1] + " Single:" + course.is_single_week);

        temp.add(course);
    }
}
