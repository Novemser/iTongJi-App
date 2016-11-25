package com.example.aitongji.Model;

import com.example.aitongji.Utils.observable.Observer;
import com.example.aitongji.Utils.observable.Subject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Novemser on 2016/11/25.
 */
public class RestMoneySubject implements Subject, Serializable {
    private List<Observer> observerList;

    public String getCardRest() {
        return cardRest;
    }

    public void setCardRest(String cardRest) {
        this.cardRest = cardRest;
        notifyObservers();
    }

    private String cardRest;

    public RestMoneySubject() {
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
            ob.update(1);
        }
    }
}
