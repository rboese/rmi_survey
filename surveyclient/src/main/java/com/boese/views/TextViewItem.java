package com.boese.views;

import com.boese.controller.SurveyRmiController;
import com.boese.rmi.RmiOption;
import com.boese.rmi.RmiSurvey;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;

public class TextViewItem extends JPanel {
    private RmiSurvey survey;
    private RmiOption option;
    private SurveyRmiController controller;
    private JTextField textField;

    public TextViewItem(SurveyRmiController controller, RmiOption option) {
        this.controller = controller;
        this.survey = controller.getSurvey();
        this.option = option;

        try {
            generateUI();
        } catch (RemoteException e) {
            controller.raiseConnectionLost();
        }

        controller.registerListener(((sender, value) -> SwingUtilities.invokeLater(() -> {
            if(!(sender instanceof RmiOption)) {
                return;
            }
            RmiOption o = (RmiOption) sender;
            if(o.equals(option)){
                textField.setText(String.format("%d", value));
            }
        })));
    }

    private void generateUI() throws RemoteException {
        JLabel label = new JLabel(option.getText());
        label.setHorizontalAlignment(JLabel.RIGHT);
        add(label);

        textField  = new JTextField();
        textField.setText(String.format("%d", option.getCount()));
        textField.addActionListener(e -> new Thread(() -> {
            int number = 0;
            try {
                number = Integer.parseInt(textField.getText());
                if (number >= 0) {
                    option.setCount(number);
                } else {
                    number = option.getCount();
                }
            } catch (NumberFormatException exeption) {
                try {
                    number = option.getCount();
                } catch (RemoteException e1) {
                    controller.raiseConnectionLost();
                }
            } catch (RemoteException e1) {
                controller.raiseConnectionLost();
            }

            final int finalNumber = number;
            SwingUtilities.invokeLater(() ->textField.setText(String.format("%d", finalNumber)));
        }).start());
        add(textField);

        setLayout(new GridLayout(0, 2));
        setVisible(true);
    }
}
