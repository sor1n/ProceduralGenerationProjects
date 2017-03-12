package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class Game extends JFrame
{
    public static JTextField name = new JTextField("Image", 10);
    public Game()
    {
        super("Random Image Generator");
        View view = new View();
        getContentPane().add(BorderLayout.CENTER, view);
        JPanel subPanel = new JPanel();
        JButton recreate = new JButton("Recreate");
        recreate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.createNewImage();
                view.repaint();
            }
        });
        JButton save = new JButton("Save");
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    view.save();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        JButton incRes = new JButton("Increase Resolution (+16)");
        incRes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.resolution += 16;
                view.createNewImage();
                view.repaint();
            }
        });
        JButton decRes = new JButton("Decrease Resolution (-16)");
        decRes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(view.resolution > 16) {
                    view.resolution -= 16;
                    view.createNewImage();
                    view.repaint();
                }
            }
        });
        subPanel.add(recreate);
        subPanel.add(save);
        subPanel.add(incRes);
        subPanel.add(decRes);
        subPanel.add(name);
        getContentPane().add(BorderLayout.SOUTH, subPanel);
        setExtendedState(Frame.MAXIMIZED_BOTH);
        this.setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        repaint();
    }

    public static void main(String[] args) throws Exception {
        new Game();
    }
}
