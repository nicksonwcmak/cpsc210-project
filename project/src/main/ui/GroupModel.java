package ui;

import model.Connection;
import model.Group;
import model.Person;
import model.exceptions.AlreadyInGroupException;
import model.exceptions.NotConnectedException;
import model.exceptions.NotInGroupException;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.Scanner;
import java.util.Set;

// provides console UI for a model for a group of people

public class GroupModel {
    private static final String JSON_STORE = "./data/groupmodel.json";
    private Group group;
    private Scanner input;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;


    // EFFECTS: starts the group model
    public GroupModel() {
        runModel();
    }

    // This method references code from this repo
    // Link: https://github.students.cs.ubc.ca/CPSC210/TellerApp
    // EFFECTS: runs the model
    public void runModel() {
        boolean keepRunning = true;
        String command;

        init();

        while (keepRunning) {
            displayMenu();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("q")) {
                keepRunning = false;
            } else {
                processCommand(command);
            }
        }

    }

    // This method references code from this repo
    // Link: https://github.students.cs.ubc.ca/CPSC210/TellerApp
    // MODIFIES: this
    // EFFECTS: initializes the model
    private void init() {
        group = new Group("My group model");
        input = new Scanner(System.in);
        input.useDelimiter("\n");
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
    }

    // This method references code from this repo
    // Link: https://github.students.cs.ubc.ca/CPSC210/TellerApp
    // EFFECTS: displays the menu
    private void displayMenu() {
        System.out.println("\nSelect one:");
        System.out.println("\ta -> add a person");
        System.out.println("\tc -> add a connection");
        System.out.println("\tp -> view a person's connections");
        System.out.println("\te -> edit a connection");
        System.out.println("\tl -> load data from file");
        System.out.println("\ts -> save data to file");
        System.out.println("\tq -> quit");
    }


    // This method references code from this repo
    // Link: https://github.students.cs.ubc.ca/CPSC210/TellerApp
    // EFFECTS: processes commands
    private void processCommand(String command) {
        if (command.equals("a")) {
            addPersonToModel();
        } else if (command.equals("c")) {
            addConnectionToModel();
        } else if (command.equals("p")) {
            getPersonsConnections();
        } else if (command.equals("e")) {
            editConnection();
        } else if (command.equals("l")) {
            loadGroup();
        } else if (command.equals("s")) {
            saveGroup();
        } else {
            System.out.println("Input is not a valid command...");
        }
    }

    // MODIFIES: this
    // EFFECTS: adds a new person to the model, if a person with the input name is not already in the model
    private void addPersonToModel() {
        System.out.println("Enter name of person to add: ");
        String newPersonName = input.next();

        try {
            Person newPerson = new Person(newPersonName);
            group.addPerson(newPerson);
            System.out.println("Person " + newPersonName + " added");
        } catch (AlreadyInGroupException e) {
            System.out.println("A person with this name is already in the group...");
        }
    }

    // MODIFIES: this
    // EFFECTS: adds a connection between two people already in the model, if they are not already connected
    private void addConnectionToModel() {
        System.out.println("Enter name of first person:");
        String person1Name = input.next();
        try {
            Person person1 = group.getPersonWithName(person1Name);
            System.out.println("Enter name of second person:");
            String person2Name = input.next();
            Person person2 = group.getPersonWithName(person2Name);

            System.out.println("Input description:");
            String desc = input.next();
            Connection newConnection = new Connection(person1, person2, desc);
            group.addConnection(newConnection);
            System.out.println("Connection added: " + desc + ", between " + person1Name + " and " + person2Name);

        } catch (NotInGroupException e) {
            System.out.println("Person not in group...");
        } catch (AlreadyInGroupException e) {
            System.out.println("These two people already have a connection...");
        }

    }

    // EFFECTS: displays the connections a person has
    private void getPersonsConnections() {
        System.out.println("Enter name of person: ");
        String personName = input.next();

        try {
            Person searchedPerson = group.getPersonWithName(personName);
            printConnectionsOf(searchedPerson);
        } catch (NotInGroupException e) {
            System.out.println("Person not in group...");
        }
    }

    // EFFECTS: prints p's connections to console
    private void printConnectionsOf(Person p) {
        Collection<Connection> connections = p.getConnections();
        if (connections.isEmpty()) {
            System.out.println("This person has no connections");
        }
        for (Connection next : connections) {
            System.out.println("Person 1: " + next.getPerson1().getName() + ";  Person 2: "
                    + next.getPerson2().getName() + ";  Description: " + next.getDescription());
        }
    }

    // MODIFIES: this
    // EFFECTS: edits description of a connection in the model
    private void editConnection() {
        System.out.println("Enter name of first person:");
        String person1Name = input.next();

        try {
            Person person1 = group.getPersonWithName(person1Name);
            System.out.println(person1Name + " has connections:");
            printConnectionsOf(person1);

            System.out.println("Input name of other person in the connection to be edited:");
            String person2Name = input.next();

            Connection editingConnection = person1.getConnectionTo(person2Name);
            System.out.println("Input new connection description:");
            String newDesc = input.next();
            editingConnection.setDescription(newDesc);
            System.out.println("Connection description has been updated");
        } catch (NotInGroupException e) {
            System.out.println("Person not in group...");
        } catch (NotConnectedException e) {
            System.out.println("Person " + person1Name + " has no connection to the second person");
        }
    }

    // This method references code from this repo
    // Link: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
    // EFFECTS: saves data to file
    private void saveGroup() {
        try {
            jsonWriter.open();
            jsonWriter.write(group);
            jsonWriter.close();
            System.out.println("Saved to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // This method references code from this repo
    // Link: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
    // MODIFIES: this
    // EFFECTS: loads group from file
    private void loadGroup() {
        try {
            group = jsonReader.read();
            System.out.println("Loaded group from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        } catch (NotInGroupException e) {
            System.out.println("File " + JSON_STORE + " had an invalid state");
        }
    }

}