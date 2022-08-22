package persistence;

import model.Connection;
import model.Group;
import model.Person;
import model.exceptions.NotInGroupException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

// This class references code from this repo
// Link: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
// unit tests for JsonWriter
public class JsonWriterTest extends JsonTest {
    Group g;
    Person personA;
    Person personB;
    Person personC;

    @BeforeEach
    public void setup() {
        g = new Group("TEST-GROUP");
        personA = new Person("A");
        personB = new Person("B");
        personC = new Person("C");
    }

    @Test
    public void testWriterInvalidFile() {
        try {
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was not thrown");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    void testWriterEmptyGroup() {
        try {
            JsonWriter writer = new JsonWriter("./data/testJsonWriterEmptyGroup.json");
            writer.open();
            writer.write(g);
            writer.close();

            JsonReader reader = new JsonReader("./data/testJsonWriterEmptyGroup.json");
            g = reader.read();
            assertEquals("TEST-GROUP", g.getName());
            assertEquals(0, g.getNumberOfPeople());
            assertEquals(0,g.getNumberOfConnections());
        } catch (IOException e) {
            fail("Caught IOException when no exception expected");
        } catch (NotInGroupException e) {
            fail("Caught NotInGroupException when no exception expected");
        }
    }

    @Test
    public void testWriterGeneralWorkroom() {
        try {
            addPeopleToGroup();
            JsonWriter writer = new JsonWriter("./data/testJsonWriterGeneralGroup.json");
            writer.open();
            writer.write(g);
            writer.close();

            JsonReader reader = new JsonReader("./data/testJsonWriterGeneralGroup.json");
            g = reader.read();
            assertEquals("TEST-GROUP", g.getName());
            List<Person> people = g.getPeople();
            assertEquals(3, g.getNumberOfPeople());
            assertEquals(2,g.getNumberOfConnections());
            checkPersonSingleConnection(g,people.get(0),"A",people.get(1));
            checkPersonSingleConnection(g,people.get(2),"C",people.get(1));
            checkPersonTwoConnections(g,people.get(1),"B",people.get(0),people.get(2));
        } catch (IOException e) {
            fail("Caught IOException when no exception expected");
        } catch (NotInGroupException e) {
            fail("Caught NotInGroupException when no exception expected");
        }
    }

    protected void addPeopleToGroup() {
        g.addPerson(personA);
        g.addPerson(personB);
        g.addPerson(personC);
        g.addConnection(new Connection(personA,personB,"TEST-1"));
        g.addConnection(new Connection(personC,personB,"TEST-2"));
    }
}
