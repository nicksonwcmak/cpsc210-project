package model;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// method tests for Connection
public class ConnectionTest {
    Connection testConnection;
    Person testP1;
    Person testP2;

    @BeforeEach
    public void setup() {
        testP1 = new Person("A");
        testP2 = new Person("B");
        testConnection = new Connection(testP1,testP2,"TEST");
    }

    @Test
    public void testGetters() {
        assertEquals(testP1, testConnection.getPerson1());
        assertEquals(testP2, testConnection.getPerson2());
        assertEquals("TEST", testConnection.getDescription());
    }

    @Test
    public void testSetDescription() {
        testConnection.setDescription("Testing");
        assertEquals("Testing", testConnection.getDescription());

        testConnection.setDescription("TEST");
        assertEquals("TEST", testConnection.getDescription());
    }

    @Test
    public void testHasPerson() {
        assertTrue(testConnection.hasPerson(testP1));
        assertTrue(testConnection.hasPerson(testP2));
        assertFalse(testConnection.hasPerson(new Person("C")));
    }

    @Test
    public void testToJson() {
        JSONObject testJsonObj = testConnection.toJson();
        assertEquals(testP1.getName(),testJsonObj.get("person 1 name"));
        assertEquals(testP2.getName(),testJsonObj.get("person 2 name"));
        assertEquals("TEST",testJsonObj.get("description"));
    }
}
