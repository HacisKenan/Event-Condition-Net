package gui;

import javax.swing.*;

public class test {
    public static void main(String[] args) {
        //MyFrame x = new MyFrame();
        //x.setBounds(10,10,420,180);
        //x.show();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new GUI();
            }
        });
    }
}
