package persistence;

import model.Group;
import model.Person;
import model.exceptions.NotInGroupException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


// This class references code from this repo
// Link: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
// unit tests for JsonReader
public class JsonReaderTest extends JsonTest {

    @Test
    public void testReaderNonExistentFile() {
        JsonReader jsonReader = new JsonReader("./data/nonExistentFile");
        try {
            Group group = jsonReader.read();
            fail("IOException was not thrown");
        } catch (IOException e) {
            // expected
        } catch (NotInGroupException e) {
            fail("Caught NotInGroupException - expected IOException");
        }
    }

    @Test
    public void testReaderEmptyGroup() {
        JsonReader reader = new JsonReader("./data/testJsonReaderEmptyGroup.json");
        try {
            Group g = reader.read();
            assertEquals("TEST-NAME", g.getName());
            assertEquals(0, g.getNumberOfPeople());
            assertEquals(0,g.getNumberOfConnections());
        } catch (IOException e) {
            fail("Caught IOException: couldn't read from file");
        } catch (NotInGroupException e) {
            fail("Caught NotInGroupException: data had invalid state");
        }
    }

    @Test
    public void testReaderGeneralGroup() {
       JsonReader reader = new JsonReader("./data/testJsonReaderGeneralGroup.json");
        try {
            Group g = reader.read();
            assertEquals("TEST-NAME", g.getName());
            List<Person> people = g.getPeople();
            assertEquals(3, people.size());
            assertEquals(2,g.getNumberOfConnections());
            checkPersonSingleConnection(g,people.get(0),"A",people.get(1));
            checkPersonSingleConnection(g,people.get(2),"C",people.get(1));
            checkPersonTwoConnections(g,people.get(1),"B",people.get(0),people.get(2));
        } catch (IOException e) {
            fail("Couldn't read from file");
        } catch (NotInGroupException e) {
            fail("Caught NotInGroupException: data had invalid state");
        }
    }

}