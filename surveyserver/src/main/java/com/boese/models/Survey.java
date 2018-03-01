package com.boese.models;

import com.boese.rmi.*;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class Survey implements RmiSurvey, Serializable {
    private List<RmiOption> options = new ArrayList<>();
    private String question;
    private List<Listener> listeners = new ArrayList<>();
    private Registry registry;

    public Survey(String question, Registry registry){
        this.registry = registry;
        this.question = question;
    }

    public final String getQuestion() {
        return question;
    }

    public synchronized List<RmiOption> getOptions() {
        return options;
    }

    public synchronized void addOption(String text) {
        RmiOption o = new Option(text);
        try {
            RmiOption optionStub = (RmiOption) UnicastRemoteObject.exportObject(o, 0);
            registry.rebind("option." + text, optionStub);
        } catch (RemoteException e) {

        }
        options.add(o);

        try {
            o.registerListener(this::raiseEvent);
        } catch (RemoteException ignored) {

        }
        raiseEvent(o);
        raiseEvent(o, 0);
    }

    public synchronized int getTotalVotes() {
        int sum = 0;
        for (RmiOption option : options) {
            int count = 0;
            try {
                count = option.getCount();
            } catch (RemoteException ignored) {

            }
            sum += count;
        }
        return sum;
    }


    @Override
    public synchronized void registerListener(Listener listener) {
        if(!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    @Override
    public synchronized void deregisterListener(Listener listener) {
        listeners.remove(listener);
    }

    private synchronized void raiseEvent(ValueChangeable sender, Object value) {
        List<Listener> obsoleteListeners = new ArrayList<>();
        for (Listener e : listeners) {
            try {
                e.raiseEvent(sender, value);
            } catch (RemoteException ignored) {
                obsoleteListeners.add(e);
            }
        }

        listeners.removeAll(obsoleteListeners);
    }

    private synchronized void raiseEvent(Object value) {
        int numListener = listeners.size();
        for(int i = 0; i < numListener; i++) {
            try {
                listeners.get(i).raiseEvent(this, value);
            } catch (RemoteException ignored) {

            }
        }
    }
}
