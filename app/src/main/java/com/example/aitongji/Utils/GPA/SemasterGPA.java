package com.example.aitongji.Utils.GPA;

import java.util.ArrayList;

/**
 * Created by Novemser on 3/10/2016.
 */
public class SemasterGPA {
    public String semaster_info;
    public float semaster_gpa;
    public ArrayList<CourseGPA> courseGPAs;

    public SemasterGPA() {
        courseGPAs = new ArrayList<>();
    }
}
