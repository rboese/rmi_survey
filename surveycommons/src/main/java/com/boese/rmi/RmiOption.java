package com.boese.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RmiOption extends Remote, ValueChangeable {
    void increaseCount() throws RemoteException;
    int getCount() throws RemoteException;
    void setCount(int count) throws RemoteException;
    String getText() throws RemoteException;
}
