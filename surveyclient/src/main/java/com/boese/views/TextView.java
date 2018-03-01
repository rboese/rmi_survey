package com.boese.views;

import com.boese.controller.SurveyRmiController;
import com.boese.rmi.RmiOption;
import com.boese.rmi.RmiSurvey;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;

public class TextView extends JPanel {
    private RmiSurvey survey;
    private SurveyRmiController controller;

    public TextView(SurveyRmiController controller) {
        this.controller = controller;
        this.survey = controller.getSurvey();
        generateUI();
        controller.registerListener(((sender, value) -> SwingUtilities.invokeLater(() ->{
            if(sender instanceof RmiSurvey && value instanceof RmiOption) {
                TextViewItem tvi = new TextViewItem(controller, (RmiOption)value);
                add(tvi);
                revalidate();
                repaint();
            }
        })));
    }

    private void generateUI() {
        setBorder(BorderFactory.createTitledBorder("Setzen"));
        setLayout(new GridLayout(0, 1));
        try {
            for(RmiOption o : survey.getOptions()) {
                TextViewItem tvi = new TextViewItem(controller, o);
                add(tvi);
            }
        } catch (RemoteException e) {
            controller.raiseConnectionLost();
        }
        setVisible(true);
        revalidate();
        repaint();
    }
}
