package com.boese.controller;

import com.boese.rmi.Listener;
import com.boese.rmi.RmiSurvey;
import com.boese.rmi.ValueChangeable;

import javax.swing.*;
import java.io.Serializable;
import java.rmi.AlreadyBoundException;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SurveyRmiController implements Listener, Serializable {
    private int port;
    private String host;
    private Registry registry;
    private RmiSurvey survey;
    private List<Listener> listeners = new ArrayList<>();
    private boolean connected = false;
    private String name;

    public SurveyRmiController(String host, int port) {
        this.host = host;
        this.port = port;
        Random random = new Random();
        long l = random.nextLong();
        name = "surveyController." + l;
    }

    public synchronized void connect() {
        if (connected) {
            return;
        }

        try {
            registry = LocateRegistry.getRegistry(host, port);
            connected = true;

            Listener controllerStub = (Listener) UnicastRemoteObject.exportObject(this, 0);

            registry.bind(name, controllerStub);

            survey = (RmiSurvey) registry.lookup("survey");
            survey.registerListener(this);
        } catch (AlreadyBoundException | RemoteException | NotBoundException e) {
            connected = false;
        }
    }

    public synchronized RmiSurvey getSurvey() {
        return survey;
    }

    public synchronized void raiseConnectionLost() {
        connected = false;
    }

    public void registerListener(Listener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    @Override
    public void raiseEvent(ValueChangeable sender, Object value) throws RemoteException {
        for (Listener l : listeners) {
            SwingUtilities.invokeLater(() -> {
                try {
                    l.raiseEvent(sender, value);
                } catch (RemoteException e) {
                    connected = false;
                }
            });
        }
    }

    public void dispose() {
        connected = false;
        try {
            UnicastRemoteObject.unexportObject(this, true);
        } catch (NoSuchObjectException e) {

        }

    }
}
