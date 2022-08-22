package model;

import model.exceptions.NotConnectedException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;


// represents a person with name, list of connections to others
public class Person {
    private String name;
    private List<Connection> connections;

    // EFFECTS: creates a person with the given name
    public Person(String name) {
        this.name = name;
        connections = new ArrayList<>();
    }

    // EFFECTS: gets the person's name
    public String getName() {
        return this.name;
    }

    // EFFECTS: gets person's list of connections
    public List<Connection> getConnections() {
        return this.connections;
    }

    // MODIFIES: this
    // EFFECTS: if other person isn't already connected to this and c contains this, adds person to list of connections
    //          and returns true, otherwise returns false
    public boolean addConnection(Connection c) {
        if (c.getPerson1().equals(this) && noConnectionTo(c.getPerson2())) {
            return connections.add(c);
        } else if (c.getPerson2().equals(this) && noConnectionTo(c.getPerson1())) {
            return connections.add(c);
        }
        return false;
    }

    // EFFECTS: returns person's connection to person with name
    //          throws NotConnectedException if person is not connected to person with name
    public Connection getConnectionTo(String name) throws NotConnectedException {
        for (Connection next : connections) {
            Person p1 = next.getPerson1();
            Person p2 = next.getPerson2();
            if (p1.getName().equals(name) || p2.getName().equals(name)) {
                return next;
            }
        }
        throw new NotConnectedException();
    }

    // EFFECTS: returns true if this has a connection to person with name
    public boolean hasConnectionTo(String name) {
        for (Connection next : connections) {
            Person p1 = next.getPerson1();
            Person p2 = next.getPerson2();
            if (p1.getName().equals(name) || p2.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    // EFFECTS: returns true if this is not connected to person p
    private boolean noConnectionTo(Person p) {
        return !hasConnectionTo(p.getName());
    }

    // This method references code from this repo
    // Link: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
    // EFFECTS: represents this as a JSON object
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("connections", connectionsToJson());
        return json;
    }

    // This method references code from this repo
    // Link: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
    // EFFECTS: returns connections in this group as a JSON array
    public JSONArray connectionsToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Connection p : connections) {
            jsonArray.put(p.toJson());
        }
        return jsonArray;
    }
    //toJson and connectionsToJson are tested implicitly by persistence method tests
}