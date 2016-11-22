package com.example.aitongji.Model;

import com.example.aitongji.Utils.GPA.Semester;
import com.example.aitongji.Utils.observable.Observer;
import com.example.aitongji.Utils.observable.Subject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Novemser on 3/10/2016.
 */
public class StudentGPASubject implements Subject, Serializable {
    public float total_gpa;
    public float grade_majored;
    public float grade_get;
    public int failed_courses;
    public String student_id;
    public String student_name;
    public String acadmy;
    public String major;
    public ArrayList<Semester> semesters;
    private List<Observer> observerList;

    public StudentGPASubject() {
        semesters = new ArrayList<>();
        observerList = new ArrayList<>();
    }

    @Override
    public void registerObserver(Observer observer) {
        if (!observerList.contains(observer))
            observerList.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        if (observerList.contains(observer))
            observerList.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer ob :
                observerList) {
            ob.update(2);
        }
    }
}
