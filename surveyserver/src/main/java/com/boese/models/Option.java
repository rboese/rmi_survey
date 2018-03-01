package com.boese.models;

import com.boese.rmi.Listener;
import com.boese.rmi.RmiOption;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class Option implements RmiOption, Serializable {
    private String text;
    private int count;

    private List<Listener> listeners = new ArrayList<Listener>();

    public Option(String text) {
        this.text = text;
    }

    public synchronized void increaseCount() {
        count++;
        raiseEvent(count);
    }

    public synchronized int getCount() {
        return count;
    }

    public synchronized void setCount(int count) {
        this.count = count;
        raiseEvent(count);
    }

    public String getText() {
        return text;
    }

    public synchronized void registerListener(Listener listener) {
        if(!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public synchronized void deregisterListener(Listener listener) {
        listeners.remove(listener);
    }

    private synchronized void raiseEvent(int value) {
        List<Listener> obsoleteListener = new ArrayList<>();
        listeners.forEach((e) -> {
            try {
                e.raiseEvent(this, value);
            } catch (RemoteException ignored) {
                obsoleteListener.add(e);
            }
        });

        listeners.removeAll(obsoleteListener);
    }
}
