package persistence;

// This class has been created with help of JsonReader Class in JsonSerializationDemo
//https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo/blob/master/src/test/persistence/JsonTest.java

import model.Bug;
import model.BugSeverityLevel;
import model.Project;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonTest {

    protected void checkProject(String name, String creator, Project project) {
        assertEquals(name,project.getName());
        assertEquals(creator,project.getCreator());

    }

    protected void checkBug(String title, String publisher, String assignee, Boolean fixed,
                            BugSeverityLevel severityLevel, Bug bug) {
        assertEquals(title, bug.getTitle());
        assertEquals(publisher, bug.getPublisher());
        assertEquals(assignee, bug.getAssignee());
        assertEquals(fixed, bug.isFixed());
        assertEquals(severityLevel, bug.getSeverityLevel());
    }
}
