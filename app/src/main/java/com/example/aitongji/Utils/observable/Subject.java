package com.example.aitongji.Utils.observable;

/**
 * Created by Novemser on 2016/11/22.
 */
public interface Subject {
    void registerObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObservers();
}
