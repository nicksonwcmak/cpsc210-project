package ui;

import model.Connection;
import model.Person;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.List;

// this class references TableDemo in the Java "How to Use Tables" tutorial
// Link: https://docs.oracle.com/javase/tutorial/uiswing/components/table.html

// represents a table displaying a person's connections
public class ConnectionsTable extends JInternalFrame {
    private static final int WINDOW_WIDTH = 400;
    private static final int WINDOW_HEIGHT = 200;
    private Person person;

    // EFFECTS: creates a popup window containing a table of p's connections
    public ConnectionsTable(Person p, Component parent) {
        super("Connections of " + p.getName(), true,true,true,false);
        person = p;

        JTable table = new JTable(new ConnectionsTableModel());
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane);

        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setLocation(parent.getWidth() / 2, parent.getHeight() / 2);
        setBackground(Color.white);
        setVisible(true);
    }

    // models a table of connections
    private class ConnectionsTableModel extends AbstractTableModel {
        private String[] columnNames = {"First person's name", "Second person's name", "Description"};

        // EFFECTS: returns the name of each column
        @Override
        public String getColumnName(int column) {
            return columnNames[column];
        }

        // EFFECTS: returns the number of rows
        @Override
        public int getRowCount() {
            return person.getConnections().size();
        }

        // EFFECTS: returns the number of columns
        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        // EFFECTS: generates an appropriate value for each entry
        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            List<Connection> connections = person.getConnections();
            Connection connection = connections.get(rowIndex);
            if (columnIndex == 0) {
                return connection.getPerson1().getName();
            } else if (columnIndex == 1) {
                return connection.getPerson2().getName();
            } else if (columnIndex == 2) {
                return connection.getDescription();
            } else {
                return null;
            }
        }
    }
}
