package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BugTest {

    private Bug bug1;
    private Bug bug2;
    private Bug bug3;
    private Bug bug4;

    @BeforeEach
    public void runBefore() {
        bug1 = new Bug("bug1", "assignee1", "pub1", BugSeverityLevel.LOW);
        bug2 = new Bug("bug2", "assignee2", "pub2", BugSeverityLevel.MEDIUM);
        bug3 = new Bug("bug3", "assignee3", "pub3", BugSeverityLevel.HIGH);
        bug4 = new Bug("bug4", "assignee4", "pub4", BugSeverityLevel.LOW);

    }

    @Test
    public void testConstructor() {
        assertEquals("bug1", bug1.getTitle());
        assertEquals("assignee1", bug1.getAssignee());
        assertEquals("pub1", bug1.getPublisher());
        assertEquals(BugSeverityLevel.LOW, bug1.getSeverityLevel());
        assertFalse(bug1.isFixed());

        assertEquals("bug2", bug2.getTitle());
        assertEquals("assignee2", bug2.getAssignee());
        assertEquals("pub2", bug2.getPublisher());
        assertEquals(BugSeverityLevel.MEDIUM, bug2.getSeverityLevel());
        assertFalse(bug2.isFixed());

        assertEquals("bug3", bug3.getTitle());
        assertEquals("assignee3", bug3.getAssignee());
        assertEquals("pub3", bug3.getPublisher());
        assertEquals(BugSeverityLevel.HIGH, bug3.getSeverityLevel());
        assertFalse(bug3.isFixed());

        assertEquals("bug4", bug4.getTitle());
        assertEquals("assignee4", bug4.getAssignee());
        assertEquals("pub4", bug4.getPublisher());
        assertEquals(BugSeverityLevel.LOW, bug4.getSeverityLevel());
        assertFalse(bug4.isFixed());

    }

    @Test
    public void testGetFixed() {
        bug1.fixBug();
        assertTrue(bug1.isFixed());

        bug2.fixBug();
        assertTrue(bug2.isFixed());

        bug3.fixBug();
        assertTrue(bug3.isFixed());

        bug4.fixBug();
        assertTrue(bug4.isFixed());

    }

}
