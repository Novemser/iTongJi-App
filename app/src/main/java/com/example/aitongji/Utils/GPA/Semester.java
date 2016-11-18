package com.example.aitongji.Utils.GPA;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * Created by Novemser on 3/10/2016.
 */

public class Semester implements Serializable {
    public String semaster_info;
    public float semaster_gpa;
    public ArrayList<CourseGPA> courseGPAs;

    public Semester() {
        courseGPAs = new ArrayList<>();
    }
}
