package com.example.aitongji.Utils.observable.basic;

/**
 * Created by Novemser on 2016/11/21.
 */
public interface Subject {
    void registerObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObservers();
}
