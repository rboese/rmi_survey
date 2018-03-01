package com.boese.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RmiSurvey extends Remote, ValueChangeable {
    String getQuestion() throws RemoteException;
    List<RmiOption> getOptions() throws RemoteException;
    void addOption(String text) throws RemoteException;
    int getTotalVotes() throws RemoteException;
}
