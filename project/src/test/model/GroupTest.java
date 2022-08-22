package model;

import model.exceptions.AlreadyInGroupException;
import model.exceptions.NotConnectedException;
import model.exceptions.NotInGroupException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// method tests for Group
class GroupTest {
    Group testGroup;
    Person testP1;
    Person testP2;
    Person testP3;
    Connection testConnect;

    @BeforeEach
    public void setup() {
        testGroup = new Group("TEST-NAME");
        testP1 = new Person("A");
        testP2 = new Person("B");
        testP3 = new Person("C");
        testConnect = new Connection(testP1,testP2,"TEST");
    }
    @Test
    public void testHasPersonWithName() {
        addTestPeople();
        assertTrue(testGroup.hasPersonWithName("A"));
        assertTrue(testGroup.hasPersonWithName("B"));
        assertTrue(testGroup.hasPersonWithName("B"));
        assertFalse(testGroup.hasPersonWithName("D"));
    }

    @Test
    public void testAddPersonSingleNoException() {
        try {
            assertFalse(testGroup.hasPersonWithName("A"));
            testGroup.addPerson(testP1);
            assertTrue(testGroup.hasPersonWithName("A"));
            assertEquals(1,testGroup.getNumberOfPeople());
        } catch (AlreadyInGroupException e) {
            fail("Caught AlreadyInGroupException when no exception expected");
        }
    }

    @Test
    public void testAddPersonSingleException() {
        testGroup.addPerson(testP1);

        try {
            testGroup.addPerson(testP1);
            fail("AlreadyInGroupException was not thrown");
        } catch (AlreadyInGroupException e) {
            // expected
        }
        assertEquals(1,testGroup.getNumberOfPeople());
    }

    @Test
    public void testAddPersonMultiple() {
        for (Integer i=0; i<20; i++) {
            Person nextPerson = new Person(i.toString());
            testGroup.addPerson(nextPerson);
        }
        for (Integer i=0; i<20; i++) {
            assertTrue(testGroup.hasPersonWithName(i.toString()));
        }
        assertEquals(20,testGroup.getNumberOfPeople());
    }

    @Test
    public void testAddConnectionSingleNoException() {
        try {
            testGroup.addConnection(testConnect);
            assertEquals(testConnect, testP1.getConnectionTo("B"));
        } catch (NotConnectedException e) {
            fail("Caught NotConnectionException when no exception expected");
        } catch (AlreadyInGroupException e) {
            fail("Caught AlreadyInGroupException when no exception expected");
        }
    }

    @Test
    public void testAddConnectionSingleException() {
        try {
            testGroup.addConnection(testConnect);
            testGroup.addConnection(testConnect);
            fail("AlreadyInGroupException was not thrown");
        } catch (AlreadyInGroupException e) {
            // expected
        }
    }

    @Test
    public void testAddConnectionMultiple() {
        testGroup.addPerson(testP1);
        for (Integer i=0; i<20; i++) {
            Person nextPerson = new Person(i.toString());
            Connection nextConnection = new Connection(testP1,nextPerson,i.toString());
            testGroup.addPerson(nextPerson);
            testGroup.addConnection(nextConnection);
        }
        for (Integer i=0; i<20; i++) {
            assertTrue(testP1.hasConnectionTo(i.toString()));
        }
        assertEquals(20,testGroup.getNumberOfConnections());
    }


    @Test
    public void testGetPersonWithNameNoException() {
        addTestPeople();
        try {
            assertEquals(testP1,testGroup.getPersonWithName("A"));
            assertEquals(testP2,testGroup.getPersonWithName("B"));
            assertEquals(testP3,testGroup.getPersonWithName("C"));
        } catch (NotInGroupException e) {
            System.out.println("Caught NotInGroupException when no exception expected");
        }
    }

    @Test
    public void testGetPersonWithNameException() {
        try {
            testGroup.getPersonWithName("1");
            fail("NotInGroupException was not thrown");
        } catch (NotInGroupException e) {
            // expected
        }
    }

    @Test
    public void testGetConnectionBetweenNoException() {
        addTestPeople();
        Connection testConnection12 = new Connection(testP1,testP2,"TEST");
        Connection testConnection13 = new Connection(testP1,testP3,"TEST");
        Connection testConnection23 = new Connection(testP2,testP3,"TEST");
        testGroup.addConnection(testConnection12);
        testGroup.addConnection(testConnection13);
        testP3.addConnection(testConnection23);

        try {
            assertEquals(testConnection12,testGroup.getConnectionBetween(testP1,testP2));
            assertEquals(testConnection13,testGroup.getConnectionBetween(testP3,testP1));
            assertEquals(testConnection23,testGroup.getConnectionBetween(testP2,testP3));
        } catch (NotConnectedException e) {
            fail("Caught NotConnectedException when no exception expected");
        }
    }

    @Test
    public void testGetConnectionBetweenException() {
        Person testP4 = new Person("D");
        try {
            testGroup.getConnectionBetween(testP1,testP4);
            fail("NotConnectedException was not thrown");
        } catch (NotConnectedException e) {
            // expected
        }
    }

    @Test
    public void testGetNumberOfPeople() {
        assertEquals(0,testGroup.getNumberOfPeople());
        addTestPeople();
        assertEquals(3,testGroup.getNumberOfPeople());
    }

    @Test
    public void testGetNumberOfConnections() {
        assertEquals(0,testGroup.getNumberOfConnections());
        addTestPeople();
        Connection testConnection12 = new Connection(testP1,testP2,"TEST");
        Connection testConnection13 = new Connection(testP1,testP3,"TEST");
        Connection testConnection23 = new Connection(testP2,testP3,"TEST");
        testGroup.addConnection(testConnection12);
        testGroup.addConnection(testConnection13);
        testGroup.addConnection(testConnection23);
        assertEquals(3,testGroup.getNumberOfConnections());
    }

    @Test
    public void testGetName() {
        assertEquals("TEST-NAME",testGroup.getName());
    }

    @Test
    public void testGetPeople() {
        List<Person> testPeople1 = testGroup.getPeople();
        assertEquals(0,testPeople1.size());

        addTestPeople();
        List<Person> testPeople2 = testGroup.getPeople();
        assertTrue(testPeople2.contains(testP1));
        assertTrue(testPeople2.contains(testP2));
        assertTrue(testPeople2.contains(testP3));
        assertEquals(3,testPeople1.size());
    }

    @Test
    public void testHasConnectionBetween() {
        addTestPeople();
        assertFalse(testGroup.hasConnectionBetween(testP1,testP2));
        Connection testConnect = new Connection(testP1,testP2,"TEST");
        testGroup.addConnection(testConnect);
        assertTrue(testGroup.hasConnectionBetween(testP1,testP2));
        assertTrue(testGroup.hasConnectionBetween(testP2,testP1));
        assertFalse(testGroup.hasConnectionBetween(testP1,testP3));
    }

    private void addTestPeople() {
        testGroup.addPerson(testP1);
        testGroup.addPerson(testP2);
        testGroup.addPerson(testP3);
    }

    //toJson and peopleToJson are tested implicitly by persistence method tests
}