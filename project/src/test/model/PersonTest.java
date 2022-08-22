package model;

import model.exceptions.NotConnectedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

// method tests for Person
public class PersonTest {
    Person testP1;
    Person testP2;
    Connection testConnection;

    @BeforeEach
    public void setup() {
        testP1 = new Person("A");
        testP2 = new Person("B");
        testConnection = new Connection(testP1,testP2,"TEST");
    }

    @Test
    public void testGetName() {
        assertEquals("A", testP1.getName());
        assertEquals("B", testP2.getName());
    }

    @Test
    public void testAddConnectionSingle() {
        assertFalse(testP1.hasConnectionTo("B"));
        testP1.addConnection(testConnection);
        assertTrue(testP1.hasConnectionTo("B"));
        assertFalse(testP1.addConnection(testConnection));

        assertFalse(testP2.hasConnectionTo("B"));
        testP2.addConnection(testConnection);
        assertTrue(testP2.hasConnectionTo("B"));
        assertFalse(testP2.addConnection(testConnection));
    }

    @Test
    public void testAddConnectionMultiple() {
        for (Integer i=0; i<20; i++) {
            Person newTestPerson = new Person(i.toString());
            testP1.addConnection(new Connection(testP1,newTestPerson,""));
        }

        for (Integer i=0; i<20; i++) {
            assertTrue(testP1.hasConnectionTo(i.toString()));
        }
        assertEquals(20,testP1.getConnections().size());
    }

    @Test
    public void testGetConnectionToNoException() {
        testP1.addConnection(testConnection);
        testP2.addConnection(testConnection);
        Connection testConnection2 = new Connection(testP1,new Person("C"),"TEST");
        testP1.addConnection(testConnection2);

        try {
            assertEquals(testConnection,testP1.getConnectionTo("B"));
            assertEquals(testConnection,testP2.getConnectionTo("A"));
            assertEquals(testConnection2,testP1.getConnectionTo("C"));
        } catch (NotConnectedException e) {
            fail("Caught NotConnectedException when no exception expected");
        }
    }
    @Test
    public void testGetConnectionException() {
        try {
            testP1.getConnectionTo("1");
            fail("NotConnnectedException was not thrown");
        } catch (NotConnectedException e) {
            // expected
        }
    }

    @Test
    public void testHasConnectionTo() {
        assertFalse(testP1.hasConnectionTo("B"));
        testP1.addConnection(testConnection);
        testP2.addConnection(testConnection);
        assertTrue(testP1.hasConnectionTo("B"));
        assertTrue(testP2.hasConnectionTo("A"));
    }

    @Test
    public void testGetConnectionsSingle() {
        testP1.addConnection(testConnection);
        Collection<Connection> testConnectionList = testP1.getConnections();
        assertTrue(testConnectionList.contains(testConnection));
        assertEquals(1, testConnectionList.size());
    }

    @Test
    public void testGetConnectionsMultiple() {
        List<String> testNos = new ArrayList<>();
        for (Integer i=0; i<20; i++) {
            Person newTestPerson = new Person(i.toString());
            testP1.addConnection(new Connection(testP1,newTestPerson,""));
            testNos.add(i.toString());
        }

        Collection<Connection> testConnections = testP1.getConnections();
        for (Connection next : testConnections) {
            assertEquals(testP1,next.getPerson1());
            String p2Name = next.getPerson2().getName();
            assertTrue(testNos.contains(p2Name));
            testNos.remove(p2Name);
        }
        assertEquals(20,testConnections.size());
    }

    //toJson and connectionsToJson are tested implicitly by persistence method tests

}
