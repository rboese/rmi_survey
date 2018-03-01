package com.boese.views;

import com.boese.controller.SurveyRmiController;
import com.boese.rmi.RmiSurvey;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.rmi.RemoteException;

public class AddOptionView extends JPanel {
    private RmiSurvey survey;
    private SurveyRmiController controller;

    public AddOptionView(SurveyRmiController controller) {
        this.controller = controller;
        this.survey = controller.getSurvey();

        generateUI();
    }

    private void generateUI() {
        setBorder(BorderFactory.createTitledBorder("Neue Antwortmoeglichkeit hinzufuegen"));
        setLayout(new GridLayout(0, 2));
        add(new JLabel("Zusaetzliche Antwortmoeglichkeit: "));
        JTextField textField = new JTextField();
        textField.addActionListener(e -> new Thread(() -> {
            if(textField.getText().length() == 0) {
                return;
            }

            try {
                survey.addOption(textField.getText());
            } catch (RemoteException e1) {
                controller.raiseConnectionLost();
            }
            SwingUtilities.invokeLater(() -> textField.setText(""));
        }).start());

        add(textField);
    }
}
