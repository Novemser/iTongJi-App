package com.example.aitongji.Model;

import com.example.aitongji.Utils.observable.Observer;
import com.example.aitongji.Utils.observable.Subject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Novemser on 2016/11/22.
 */
public class NewsTitleSubject implements Subject, Serializable {
    private List<Observer> observerList;

    public List<String> getInfoIds() {
        return infoIds;
    }

    private List<String> infoIds;

    public List<String> getInfoTitles() {
        return infoTitles;
    }

    private List<String> infoTitles;

    public List<String> getInfoTimes() {
        return infoTimes;
    }

    private List<String> infoTimes;

    public NewsTitleSubject() {
        observerList = new ArrayList<>();
        infoIds = new ArrayList<>();
        infoTimes = new ArrayList<>();
        infoTitles = new ArrayList<>();
    }

    public void setNewsInfo(List<String> infoIds,
                            List<String> infoTimes,
                            List<String> infoTitles) {
        this.infoIds = infoIds;
        this.infoTimes = infoTimes;
        this.infoTitles = infoTitles;
        notifyObservers();
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
            ob.update(0);
        }
    }
}
