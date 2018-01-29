package arthur.towerOfHanoi;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by User on 25.12.16.
 */
public class FrameHanoi extends JFrame {

    private JPanel controlPanel;
    private CanvasHanoi canvasHanoi;
    private JTextField textField;
    private int size;
    JButton loadBtn;
    JButton moveBtn;

    public FrameHanoi() {

        this.controlPanel = new JPanel();
        this.canvasHanoi = new CanvasHanoi();
        this.textField = new JTextField(5);

        this.loadBtn = new JButton("load");
        this.moveBtn = new JButton("Move");

        // moveBtn.setEnabled(false);
        moveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveActionPreformed();
                //   moveBtn.setEnabled(false);
            }
        });
        loadBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadActionPerformed();
            }
        });

        JButton stopBtn = new JButton("Stop");
        stopBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopActionPerformed();
            }
        });

        JButton pauseBtn = new JButton("Pause");
        pauseBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pauseActionPerformed();
            }
        });
        JButton resumeBtn = new JButton("Resume");
        resumeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resumeActionPerformed();
            }
        });

        JLabel label = new JLabel("Disc Count: ");
        JLabel label1 = new JLabel(" Maximum Disc Count: 10");
        label1.setForeground(Color.RED);

        controlPanel.add(loadBtn);
        controlPanel.add(moveBtn);
        controlPanel.add(stopBtn);
        controlPanel.add(pauseBtn);
        controlPanel.add(resumeBtn);
        controlPanel.add(label);
        controlPanel.add(textField);
        controlPanel.add(label1);

        textField.setEditable(true);

        add(canvasHanoi, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        setLocation(50, 50);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(750, 550);
        setVisible(true);
        setTitle("TowerOfHanoi");

        canvasHanoi.drawPegs();

    }

    private void stopActionPerformed() {
        canvasHanoi.stop();
    }

    private void pauseActionPerformed() {
        canvasHanoi.pause();
    }

    private void resumeActionPerformed() {
        canvasHanoi.resume();
    }


    private void moveActionPreformed() {
        try {
            canvasHanoi.moveDiscs((int) size);
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }

    }

    private void loadActionPerformed() {
        String textVal = textField.getText();
        if (validateText(textVal)) {
            size = Integer.parseInt(textVal);
            System.out.println(size);
            canvasHanoi.clearDiscs();
            canvasHanoi.addDiscs(size);
        }
    }

    private boolean validateText(String str) {
        try {
            int intValue = Integer.parseInt(str);
            int maxDiscsCount = 10;
            if (intValue > 0) {
                //  moveBtn.setEnabled(true);
                if (intValue <= maxDiscsCount) {
                    return true;
                } else {
                    //     moveBtn.setEnabled(false);
                    return false;
                }
            } else if (intValue == 0) {
                // moveBtn.setEnabled(false);
                String message = "The number of discs can't be zero :  " + str + " ";
                JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            } else {
                //     moveBtn.setEnabled(false);
                String message = "The number of discs can't be negative :  " + str + " ";
                JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            String message = e.getClass().getName() + " : " + e.getMessage();
            JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public static void main(String[] args) {
        FrameHanoi frameHanoi = new FrameHanoi();
        frameHanoi.setBackground(Color.white);
    }
}
