package com.boese;

import com.boese.controller.SurveyRmiController;
import com.boese.views.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class SurveyClientApplication {
    public static void main(String... args) {
        enableHighResolutionHandling();

        SurveyRmiController controller = new SurveyRmiController("localhost", 50001);
        controller.connect();

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Survey");
            frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            frame.addWindowListener(new WindowListener() {
                @Override
                public void windowOpened(WindowEvent e) {

                }

                @Override
                public void windowClosing(WindowEvent e) {
                    controller.dispose();
                }

                @Override
                public void windowClosed(WindowEvent e) {

                }

                @Override
                public void windowIconified(WindowEvent e) {

                }

                @Override
                public void windowDeiconified(WindowEvent e) {

                }

                @Override
                public void windowActivated(WindowEvent e) {

                }

                @Override
                public void windowDeactivated(WindowEvent e) {

                }
            });
            frame.setLayout(new BorderLayout());
            frame.setSize(new Dimension(1600, 600));

            JPanel topPanel = new JPanel();
            topPanel.setLayout(new GridLayout(0, 2));
            topPanel.add(new QuestionView(controller));
            topPanel.add(new AddOptionView(controller));
            frame.add(topPanel, BorderLayout.NORTH);

            JPanel centerPanel = new JPanel();
            centerPanel.setLayout(new GridLayout(0, 2));
            centerPanel.add(new LabelView(controller));
            centerPanel.add(new TextView(controller));
            centerPanel.add(new PieChartView(controller));
            centerPanel.add(new BarChartView(controller));
            frame.add(centerPanel, BorderLayout.CENTER);

            frame.setVisible(true);
            frame.revalidate();
            frame.repaint();
        });
    }

    private static void enableHighResolutionHandling()
    {
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (InstantiationException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        catch (UnsupportedLookAndFeelException e)
        {
            e.printStackTrace();
        }
    }
}
