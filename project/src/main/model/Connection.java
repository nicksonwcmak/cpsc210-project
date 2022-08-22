package model;

import org.json.JSONObject;

// represents a connection between two people, with description
public class Connection {
    private Person person1;
    private Person person2;
    private String description;

    // EFFECTS: creates a connection between given people with given description
    public Connection(Person p1, Person p2, String description) {
        this.person1 = p1;
        this.person2 = p2;
        this.description = description;
    }

    //EFFECTS: gets first person in the connection
    public Person getPerson1() {
        return person1;
    }

    //EFFECTS: gets second person in the connection
    public Person getPerson2() {
        return person2;
    }

    //EFFECTS: gets description of connection
    public String getDescription() {
        return description;
    }

    // MODIFIES: this
    // EFFECTS: sets description to desc
    public void setDescription(String desc) {
        this.description = desc;
    }

    // EFFECTS: returns true if person1 or person2 is the same as p
    public boolean hasPerson(Person p) {
        return (person1.equals(p) || person2.equals(p));
    }

    // This method references code from this repo
    // Link: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
    // EFFECTS: represents this as a JSON object
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("person 1 name",person1.getName());
        json.put("person 2 name", person2.getName());
        json.put("description", description);
        return json;
    }
}
