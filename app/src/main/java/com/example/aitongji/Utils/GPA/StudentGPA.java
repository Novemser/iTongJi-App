package com.example.aitongji.Utils.GPA;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Novemser on 3/10/2016.
 */
public class StudentGPA implements Serializable {
    public float total_gpa;
    public float grade_majored;
    public float grade_get;
    public int failed_courses;
    public String student_id;
    public String student_name;
    public String acadmy;
    public String major;
    public ArrayList<Semester> semesters;

    public StudentGPA() {
        semesters = new ArrayList<>();
    }
}
