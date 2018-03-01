package com.boese.views;

import com.boese.controller.SurveyRmiController;
import com.boese.rmi.RmiOption;
import com.boese.rmi.RmiSurvey;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;

public class BarChartView extends JPanel {
    RmiSurvey survey;
    SurveyRmiController controller;

    public BarChartView(SurveyRmiController controller) {
        this.controller = controller;
        this.survey = controller.getSurvey();
        generateUI();

        controller.registerListener((sender, value) -> {
            if(sender instanceof RmiSurvey && value instanceof RmiOption) {
                add(new BarChartViewItem(controller, (RmiOption) value));
                revalidate();
            }
        });
    }

    private void generateUI() {
        setBorder(BorderFactory.createTitledBorder("Balkendiagramm"));
        setLayout(new GridLayout(0, 1));
        try {
            survey.getOptions().forEach(o -> add(new BarChartViewItem(controller, o)));
        } catch (RemoteException e) {
            controller.raiseConnectionLost();
        }
        revalidate();
        repaint();
    }
}
