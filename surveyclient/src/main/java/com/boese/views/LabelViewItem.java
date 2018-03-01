package com.boese.views;

import com.boese.controller.SurveyRmiController;
import com.boese.rmi.RmiOption;
import com.boese.rmi.RmiSurvey;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;

public class LabelViewItem extends JPanel {
    private RmiOption option;
    private SurveyRmiController controller;
    private RmiSurvey survey;
    private JLabel label = new JLabel();

    public LabelViewItem(SurveyRmiController controller, RmiOption option) {
        this.controller = controller;
        this.option = option;
        this.survey = controller.getSurvey();
        generateUI();
        controller.registerListener((sender, value) -> {
            if(sender instanceof RmiSurvey) {
                return;
            }
            SwingUtilities.invokeLater(() -> label.setText(generateLabelText()));
        });
    }

    private void generateUI() {
        JButton button = new JButton("Erhoehen");
        button.addActionListener((e) -> new Thread(() -> {
            try {
                option.increaseCount();
            } catch (RemoteException e1) {
                controller.raiseConnectionLost();
            }
        }).start());
        label.setText(generateLabelText());
        label.setHorizontalAlignment(JLabel.RIGHT);
        add(label);
        add(button);
        setLayout(new GridLayout(0, 2));
        repaint();
    }

    private synchronized String generateLabelText() {
        try {
            int count = option.getCount();
            int total = survey.getTotalVotes();
            return String.format("%s: %d von %d (%d%%)", option.getText(),
                    count,
                    total,
                    (total == 0) ? 0 : (count * 100 / total));
        } catch (RemoteException e) {
            controller.raiseConnectionLost();
        }
        return "";
    }
}
