package persistence;

import model.Connection;
import model.Group;
import model.Person;
import model.exceptions.NotInGroupException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

// This class references code from this repo
// Link: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git

// represents a reader which reads a group from JSON data stored in file
public class JsonReader {
    private String source;

    // EFFECTS: creates a JsonReader to read a file from source
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads Group data from file and returns it
    //          throws IOException if error occurs while reading data
    public Group read() throws IOException, NotInGroupException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseGroup(jsonObject);
    }

    // EFFECTS: reads source file as a string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }
        return contentBuilder.toString();
    }

    // EFFECTS: parses Group from JSON object and returns it
    private Group parseGroup(JSONObject jsonObject) throws NotInGroupException {
        String name = jsonObject.getString("name");
        Group g = new Group(name);
        addPersons(g,jsonObject);
        return g;
    }

    // MODIFIES: g
    // EFFECTS: parses Persons from JSON object and returns it
    private void addPersons(Group g, JSONObject jsonObject) throws NotInGroupException {
        JSONArray jsonArray = jsonObject.getJSONArray("people");
        for (Object json : jsonArray) {
            JSONObject nextPerson = (JSONObject) json;
            addPerson(g,nextPerson);
        }
    }

    // MODIFIES: g
    // EFFECTS: parses Person from JSON object and returns it
    private void addPerson(Group g, JSONObject jsonObject) throws NotInGroupException {
        String name = jsonObject.getString("name");
        if (!g.hasPersonWithName(name)) {
            Person person = new Person(name);
            g.addPerson(person);
        }
        addConnections(g,jsonObject);
    }

    // MODIFIES: g
    // EFFECTS: parses Connections from JSON object and returns it
    private void addConnections(Group g, JSONObject jsonObject) throws NotInGroupException {
        JSONArray jsonArray = jsonObject.getJSONArray("connections");
        for (Object json : jsonArray) {
            JSONObject nextConnection = (JSONObject) json;
            addConnection(g,nextConnection);
        }
    }

    // MODIFIES: g
    // EFFECTS: parses Connection from JSON object and returns it
    private void addConnection(Group g, JSONObject jsonObject) throws NotInGroupException {
        String p1Name = jsonObject.getString("person 1 name");
        String p2Name = jsonObject.getString("person 2 name");
        String desc = jsonObject.getString("description");

        Person p1 = addConnectedPerson(g,p1Name);
        Person p2 = addConnectedPerson(g,p2Name);
        if (!g.hasConnectionBetween(p1,p2)) {
            g.addConnection(new Connection(p1,p2,desc));
        }
    }

    // MODIFIES: g
    // EFFECTS: if g has a person with name, returns it; otherwise returns a new person with name
    private Person addConnectedPerson(Group g, String name) throws NotInGroupException {
        if (g.hasPersonWithName(name)) {
            return g.getPersonWithName(name);
        } else {
            Person p = new Person(name);
            g.addPerson(p);
            return p;
        }
    }
}
