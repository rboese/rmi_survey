package com.boese.views;

import com.boese.controller.SurveyRmiController;
import com.boese.rmi.RmiOption;
import com.boese.rmi.RmiSurvey;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;

public class LabelView extends JPanel {
    private SurveyRmiController controller;
    private RmiSurvey survey;

    public LabelView(SurveyRmiController controller) {
        this.controller = controller;
        this.survey = controller.getSurvey();
        generateUI();
        controller.registerListener(((sender, value) -> {
            if(sender instanceof RmiSurvey && value instanceof RmiOption) {
                LabelViewItem lvi = new LabelViewItem(controller, (RmiOption)value);
                add(lvi);
                revalidate();
                repaint();
            }
        }));
    }

    private synchronized void generateUI() {
        setBorder(BorderFactory.createTitledBorder("Erhoehen"));
        setLayout(new GridLayout(0, 1));
        try {
            if(survey != null) {
                //RmiOption[] option = survey.getOptions();
                for (RmiOption o : survey.getOptions()) {
                    LabelViewItem lvi = new LabelViewItem(controller, o);
                    add(lvi);
                }
            }
        } catch (RemoteException e) {
            controller.raiseConnectionLost();
        }
        revalidate();
        repaint();
    }
}
