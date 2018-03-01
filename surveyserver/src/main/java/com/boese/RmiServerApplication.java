package com.boese;

import com.boese.models.Survey;
import com.boese.rmi.RmiOption;
import com.boese.rmi.RmiSurvey;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RmiServerApplication {
    public static void main(String... args) {
        try {
            Registry registry = LocateRegistry.createRegistry(50001);

            Survey survey = new Survey("Was war die Frage?", registry);

            RmiSurvey surveyStub = (RmiSurvey) UnicastRemoteObject.exportObject(survey, 0);
            registry.bind("survey", surveyStub);
        } catch (RemoteException | AlreadyBoundException ignored) {
            ignored.printStackTrace();
        }
    }
}
