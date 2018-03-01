package com.boese.views;

import com.boese.controller.SurveyRmiController;
import com.boese.rmi.RmiOption;
import com.boese.rmi.RmiSurvey;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;

public class Bar extends JPanel {
    RmiOption option;
    RmiSurvey survey;
    SurveyRmiController controller;

    public Bar(SurveyRmiController controller, RmiOption option) {
        this.controller = controller;
        this.survey = controller.getSurvey();
        this.option = option;

        controller.registerListener((sender, value) -> repaint());
    }

    @Override
    protected synchronized void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLUE);

        g.drawRect(10, 0, getWidth() - 20,getHeight() - 20);
        try {
            if(survey.getTotalVotes() > 0) {
                g.fillRect(10, 0, option.getCount() * (getWidth() - 20) / survey.getTotalVotes(), getHeight() - 20);
            }
        } catch (RemoteException e) {
            controller.raiseConnectionLost();
        }
    }
}
