package com.boese.views;

import com.boese.controller.SurveyRmiController;
import com.boese.rmi.RmiOption;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;

public class BarChartViewItem extends JPanel {
    SurveyRmiController controller;
    RmiOption option;
    public BarChartViewItem(SurveyRmiController controller, RmiOption option) {
        this.controller = controller;
        this.option = option;

        generateUI();
    }

    private void generateUI() {
        setLayout(new GridLayout(0, 2));
        try {
            add(new JLabel(option.getText()));
        } catch (RemoteException e) {
            controller.raiseConnectionLost();
        }
        add(new Bar(controller, option));
    }
}
