package com.boese.rmi;

import com.boese.rmi.ValueChangeable;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Listener extends Remote {
    void raiseEvent(ValueChangeable sender, Object value) throws RemoteException;
}
