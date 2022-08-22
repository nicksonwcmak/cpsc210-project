package ui;

import model.Connection;

import javax.swing.*;
import java.awt.*;

// creates a graphical representation of a given connection
public class ConnectionImage extends JInternalFrame  {
    private static final int WIDTH = 400;
    private static final int HEIGHT = 200;
    private Connection connection;

    // This method references code from this repo
    // Link: https://github.students.cs.ubc.ca/CPSC210/AlarmSystem.git
    // EFFECTS: creates a panel containing a visual representation of c
    public ConnectionImage(Connection c, Component parent) {
        super("Connection Image",false,true,false,false);
        connection = c;
        setSize(WIDTH, HEIGHT);
        setLocation(parent.getWidth() / 2, parent.getHeight() / 2);
        setBackground(Color.white);
        setVisible(true);
    }

    // This method references code from this repo
    // Link: https://github.students.cs.ubc.ca/CPSC210/SimpleDrawingPlayer-Complete.git
    // MODIFIES: g, this
    // EFFECTS: paints a visual representation of c
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        FontMetrics fm = g.getFontMetrics();
        drawNameBox(g,WIDTH / 8, connection.getPerson1().getName(), fm);
        drawNameBox(g,5 * WIDTH / 8, connection.getPerson2().getName(), fm);

        g.drawLine(3 * WIDTH / 8, HEIGHT / 2, 5 * WIDTH / 8, HEIGHT / 2);
        int descLength = fm.stringWidth(connection.getDescription());
        g.drawString(connection.getDescription(),WIDTH / 2 - descLength / 2, HEIGHT / 2 - fm.getHeight() / 2);
    }

    // MODIFIES: g, this
    // EFFECTS: draws a box labelled with name
    private void drawNameBox(Graphics g, int x, String name, FontMetrics fm) {
        g.setColor(Color.white);
        g.fillRect(x, HEIGHT / 3, WIDTH / 4, HEIGHT / 3);
        g.setColor(Color.black);
        g.drawRect(x, HEIGHT / 3, WIDTH / 4, HEIGHT / 3);

        int nameLength = fm.stringWidth(name);
        g.drawString(name, x + (WIDTH / 8 - nameLength / 2), HEIGHT / 2 + fm.getHeight() / 3);
    }

}
