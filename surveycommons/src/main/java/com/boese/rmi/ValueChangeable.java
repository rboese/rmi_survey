package com.boese.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ValueChangeable extends Remote {
    void registerListener(Listener listener) throws RemoteException;
    void deregisterListener(Listener listener) throws RemoteException;
}
