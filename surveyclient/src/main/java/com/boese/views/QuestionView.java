package com.boese.views;

import com.boese.controller.SurveyRmiController;

import javax.swing.*;
import java.rmi.RemoteException;

public class QuestionView extends JPanel {

    public QuestionView(SurveyRmiController controller) {

        SwingUtilities.invokeLater(() ->{
            try {
                String question = controller.getSurvey().getQuestion();
                generateUI(question);
            } catch (RemoteException e) {
                controller.raiseConnectionLost();
            }

        });
    }

    private void generateUI(String question) {
        setBorder(BorderFactory.createTitledBorder("Frage"));
        JLabel label = new JLabel(question);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
        add(label);
        setVisible(true);
    }
}
