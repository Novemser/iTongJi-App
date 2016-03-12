package com.example.aitongji.Utils;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Novemser on 3/12/2016.
 */
public class DataBundle implements Serializable {

    public String username;
    public String password;
    public String timeToday;
    public String timeWeek;
    public String cardRest;
    public String course_table_str;
    public ArrayList<String> infoTitle;
    public ArrayList<String> infoTime;
    public ArrayList<String> info_id;
    public CourseTable courseTable;

    public DataBundle(String username, String password, String timeToday, String timeWeek, String cardRest, String course_table_str, ArrayList<String> infoTitle, ArrayList<String> infoTime, ArrayList<String> info_id) {
        this.username = username;
        this.password = password;
        this.timeToday = timeToday;

        this.timeWeek = timeWeek;
        this.cardRest = cardRest;
        this.course_table_str = course_table_str;
        this.infoTitle = infoTitle;
        this.infoTime = infoTime;
        this.info_id = info_id;
        this.courseTable = CourseTable.getInstance();
    }
}
