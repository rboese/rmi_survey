package com.boese.views;

import com.boese.controller.SurveyRmiController;
import com.boese.rmi.RmiOption;
import com.boese.rmi.RmiSurvey;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;

public class PieChartView extends JPanel {
    SurveyRmiController controller;
    RmiSurvey survey;

    private static final Color[] colorArray = new Color[]{
            Color.BLACK,
            Color.BLUE,
            Color.GREEN,
            Color.RED,
            Color.ORANGE,
            Color.CYAN
    };

    public PieChartView(SurveyRmiController controller) {
        this.controller = controller;
        this.survey = controller.getSurvey();

        controller.registerListener(((sender, value) -> repaint()));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        final int minOffsetX = 30;
        final int minOffsetY = 30;

        int startAngle = 0;

        int colorIndex = 0;

        int radius = Math.min((getWidth() - 2 * minOffsetX) / 2, (getHeight() - 2 * minOffsetY) / 2);

        int offsetX = getWidth() / 2 - radius;
        int offsetY = getHeight() / 2 - radius;

        try {
            for (RmiOption o : survey.getOptions()) {
                g.setColor(colorArray[colorIndex]);
                g.fillArc(offsetX,
                        offsetY,
                        2 * radius,
                        2 * radius,
                        startAngle,
                        calculateAngle(o)
                );
                colorIndex++;
                if (colorIndex == colorArray.length) {
                    colorIndex = 0;
                }

                startAngle += calculateAngle(o);
            }
        } catch (RemoteException e) {
            controller.raiseConnectionLost();
        }
    }

    private int calculateAngle(RmiOption o) throws RemoteException {
        int total = survey.getTotalVotes();

        if(total == 0) {
            return 360;
        }

        return 360 * o.getCount() / total;
    }
}
