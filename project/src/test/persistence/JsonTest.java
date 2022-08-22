package persistence;

import model.Group;
import model.Person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class JsonTest {
    protected void checkPersonSingleConnection(Group g, Person p, String expectedName, Person expectedConnected) {
        assertTrue(g.hasPersonWithName(p.getName()));
        assertEquals(expectedName, p.getName());
        assertEquals(1, p.getConnections().size());
        assertTrue(p.hasConnectionTo(expectedConnected.getName()));
    }

    protected void checkPersonTwoConnections
            (Group g, Person p, String expectedName, Person expectedConnected1, Person expectedConnected2) {
        assertTrue(g.hasPersonWithName(p.getName()));
        assertEquals(expectedName,p.getName());
        assertEquals(2,p.getConnections().size());
        assertTrue(p.hasConnectionTo(expectedConnected1.getName()));
        assertTrue(p.hasConnectionTo(expectedConnected2.getName()));
    }
}
