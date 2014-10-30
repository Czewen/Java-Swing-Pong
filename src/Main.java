import java.awt.BorderLayout;
import java.awt.Graphics;

import javax.swing.JFrame;

public class Main{

    public static void main(String[] args) {

        JFrame frame = new JFrame("Pong");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        PongPanel pongPanel = new PongPanel();
        frame.add(pongPanel, BorderLayout.CENTER);
        frame.setSize(600, 600);
        frame.setVisible(true);

       
    }
}