package model;

import model.exceptions.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

// represents a group of people with connections between them
public class Group {
    private String name;
    private List<Person> people;
    private List<Connection> connections;

    // EFFECTS: creates an empty group
    public Group(String name) {
        this.name = name;
        people = new ArrayList<>();
        connections = new ArrayList<>();
    }

    // MODIFIES: this
    // EFFECTS: adds a person to the group
    //          throws DuplicatePersonException if a person with the same name is already in the group
    public void addPerson(Person p) throws AlreadyInGroupException {
        if (hasPersonWithName(p.getName())) {
            throw new DuplicatePersonException();
        }
        people.add(p);
        EventLog.getInstance().logEvent(new Event("Person " + p.getName() + " added to the model"));
    }

    // MODIFIES: this
    // EFFECTS: adds the connection to the model
    //          throws DuplicateConnectionException if group already contains connection between these people
    public void addConnection(Connection c) {
        for (Connection next : connections) {
            if (next.hasPerson(c.getPerson1()) && next.hasPerson(c.getPerson2())) {
                throw new DuplicateConnectionException();
            }
        }
        connections.add(c);
        c.getPerson1().addConnection(c);
        c.getPerson2().addConnection(c);
        EventLog.getInstance().logEvent(new Event("Connection " + c.getDescription() + " between "
                + c.getPerson1().getName() + " and " + c.getPerson2().getName() + " added to the model"));
    }

    // EFFECTS: returns the person with the given name
    //          throws NotInGroupException if the person is not in the group
    public Person getPersonWithName(String name) throws NotInGroupException {
        for (Person next : people) {
            if (next.getName().equals(name)) {
                return next;
            }
        }
        throw new NotInGroupException();
    }

    // EFFECTS: returns true if group contains person with name
    public boolean hasPersonWithName(String name) {
        for (Person next : people) {
            if (next.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    // EFFECTS: returns connection between p1 and p2
    public Connection getConnectionBetween(Person p1, Person p2) throws NotConnectedException {
        if (hasConnectionBetween(p1, p2)) {
            return p1.getConnectionTo(p2.getName());
        } else {
            return p2.getConnectionTo(p1.getName());
        }
    }

    // EFFECTS: returns true if there is a connection between p1 and p2
    public boolean hasConnectionBetween(Person p1, Person p2) {
        for (Connection connection : connections) {
            if ((connection.hasPerson(p1)) && (connection.hasPerson(p2))) {
                return true;
            }
        }
        return false;
    }

    // EFFECTS: returns the total number of people
    public int getNumberOfPeople() {
        return people.size();
    }

    // EFFECTS: returns the total number of connections
    public int getNumberOfConnections() {
        return connections.size();
    }

    // EFFECTS: returns name of the group
    public String getName() {
        return name;
    }

    // EFFECTS: returns list of people
    public List<Person> getPeople() {
        return people;
    }

    // This method references code from this repo
    // Link: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
    // EFFECTS: represents this as a JSON object
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("people", peopleToJson());
        EventLog.getInstance().logEvent(new Event("Converted group to JSON for saving"));
        return json;
    }
    //toJson and peopleToJson are tested implicitly by persistence method tests

    // This method references code from this repo
    // Link: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
    // EFFECTS: returns people in this group as a JSON array
    private JSONArray peopleToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Person p : people) {
            jsonArray.put(p.toJson());
        }
        return jsonArray;
    }
    //toJson and peopleToJson are tested implicitly by persistence method tests
}
