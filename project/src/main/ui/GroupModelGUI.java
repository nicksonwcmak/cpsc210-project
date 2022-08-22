package ui;

import model.Connection;
import model.Event;
import model.EventLog;
import model.Group;
import model.Person;
import model.exceptions.AlreadyInGroupException;
import model.exceptions.NotInGroupException;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;


// provides graphical UI for a model of a group of people
public class GroupModelGUI extends JFrame {
    private static final String JSON_STORE = "./data/GroupModel.json";
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private Group group;
    private JDesktopPane desktop;
    private JInternalFrame optionPanel;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // This method references code from this repo
    // Link: https://github.students.cs.ubc.ca/CPSC210/AlarmSystem.git
    // EFFECTS: starts the group model
    public GroupModelGUI() {
        desktop = new JDesktopPane();
        optionPanel = new JInternalFrame("Option Panel", false, false, false,
                false);

        setContentPane(desktop);
        setTitle("Group Model");
        setSize(WIDTH, HEIGHT);

        initializeFields();
        addButtons();
        optionPanel.pack();
        optionPanel.setVisible(true);
        desktop.add(optionPanel);
        addWindowListener(new CloseWindowAdapter());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: initializes fields
    private void initializeFields() {
        group = new Group("My group");
        jsonReader = new JsonReader(JSON_STORE);
        jsonWriter = new JsonWriter(JSON_STORE);
    }

    // This method references code from this repo
    // Link: https://github.students.cs.ubc.ca/CPSC210/AlarmSystem.git
    // MODIFIES: this
    // EFFECTS: adds buttons to the screen
    private void addButtons() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5,1));
        buttonPanel.add(new JButton(new AddPersonAction()));
        buttonPanel.add(new JButton(new AddConnectionAction()));
        buttonPanel.add(new JButton(new DisplayConnectionsAction()));
        buttonPanel.add(new JButton(new LoadDataAction()));
        buttonPanel.add(new JButton(new SaveDataAction()));

        optionPanel.add(buttonPanel, BorderLayout.WEST);
    }

    // This class references code from this repo
    // Link: https://github.students.cs.ubc.ca/CPSC210/AlarmSystem.git
    // represents action taken when user wants to add a person to the system
    private class AddPersonAction extends AbstractAction {

        // EFFECTS: creates an AddPersonAction with appropriate name
        AddPersonAction() {
            super("Add a person");
        }

        // MODIFIES: GroupModelGUI.this
        // EFFECTS: prompts user for new person's name. If group already has a person with that name, shows an error
        //          message; otherwise adds the person to the group
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = JOptionPane.showInputDialog(null, "Person name?",
                    "Enter name for new person", JOptionPane.QUESTION_MESSAGE);
            Person person = new Person(name);
            try {
                group.addPerson(person);
            } catch (AlreadyInGroupException exception) {
                JOptionPane.showMessageDialog(null, exception.getMessage(), "System Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // represents action taken when user wants to add a connection to the system
    private class AddConnectionAction extends AbstractAction {

        // EFFECTS: creates an AddConnectionAction with appropriate name
        AddConnectionAction() {
            super("Add a connection");
        }

        // MODIFIES: GroupModelGUI.this
        // EFFECTS: prompts user for names of people to connect and a description. If no exception occurs, adds the
        //          connection to the group and displays a visual representation of it.
        @Override
        public void actionPerformed(ActionEvent e) {
            String person1Name = JOptionPane.showInputDialog(null, "First person's name?",
                    "Enter name of a person in the group", JOptionPane.QUESTION_MESSAGE);
            try {
                Person person1 = group.getPersonWithName(person1Name);
                String person2Name = JOptionPane.showInputDialog(null, "Second person's name?",
                        "Enter name of a different person in the group", JOptionPane.QUESTION_MESSAGE);
                Person person2 = group.getPersonWithName(person2Name);
                String description = JOptionPane.showInputDialog(null,
                        "Connection description?", "Enter a short description for the connection",
                        JOptionPane.QUESTION_MESSAGE);
                Connection connection = new Connection(person1, person2, description);
                group.addConnection(connection);
                desktop.add(new ConnectionImage(connection, GroupModelGUI.this));
            } catch (AlreadyInGroupException | NotInGroupException exception) {
                JOptionPane.showMessageDialog(null, exception.getMessage(), "System Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // represents action to be taken when user wants to load data from file
    private class LoadDataAction extends AbstractAction {

        LoadDataAction() {
            super("Load group data from file");
        }

        // MODIFIES: GroupModelGUI.this
        // EFFECTS: loads data from JSON_STORE. If unsuccessful, produces and appropriate error message.
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                group = jsonReader.read();
                JOptionPane.showMessageDialog(null,
                        "Loaded data from file " + JSON_STORE, "Action Successful",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException exception) {
                JOptionPane.showMessageDialog(null,
                        "Unable to read from file: " + JSON_STORE, "System Error",
                        JOptionPane.ERROR_MESSAGE);
            } catch (NotInGroupException exception) {
                JOptionPane.showMessageDialog(null,
                        "File " + JSON_STORE + " had an invalid state", "System Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // represents action to be taken when user wants to save data to file
    private class SaveDataAction extends AbstractAction {

        // EFFECTS: creates a SaveDataAction with appropriate name
        SaveDataAction() {
            super("Save group data to file");
        }

        // MODIFIES: GroupModelGUI.this
        // EFFECTS: saves group data to JSON_STORE. If unsuccessful, produces an appropriate error message.
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                jsonWriter.open();
                jsonWriter.write(group);
                jsonWriter.close();
                JOptionPane.showMessageDialog(null,
                        "Saved to " + JSON_STORE, "Action Successful",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (FileNotFoundException exception) {
                JOptionPane.showMessageDialog(null,
                        "Unable to write to file: " + JSON_STORE, "System Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // represents action to be taken when user wants to save data to file
    private class DisplayConnectionsAction extends AbstractAction {

        // EFFECTS: creates a DisplayConnectionsAction with appropriate name
        DisplayConnectionsAction() {
            super("Display a person's connections");
        }

        // MODIFIES: GroupModelGUI.this
        // EFFECTS: displays a person's connections as a table
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = JOptionPane.showInputDialog(null, "Person's name?",
                    "Enter name of a person in the group", JOptionPane.QUESTION_MESSAGE);
            try {
                Person person = group.getPersonWithName(name);
                if (person.getConnections().size() == 0) {
                    JOptionPane.showMessageDialog(null,
                            "This person has no connections to display", "System Error",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    ConnectionsTable connectionsTable = new ConnectionsTable(person, GroupModelGUI.this);
                    connectionsTable.setOpaque(true);
                    desktop.add(connectionsTable);
                }
            } catch (NotInGroupException exception) {
                JOptionPane.showMessageDialog(null, exception.getMessage(), "System Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // represents a window listener that activates when the main window is closing
    private class CloseWindowAdapter extends WindowAdapter {

        // This method references code from this repo
        // Link: https://github.students.cs.ubc.ca/CPSC210/AlarmSystem.git
        // EFFECTS: prints event log to console when window is closed
        @Override
        public void windowClosing(WindowEvent e) {
            for (Event next : EventLog.getInstance()) {
                System.out.println(next);
            }
        }

    }

}