package persistence;

//// This class has been created with help of JsonReader Class in JsonSerializationDemo
//https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo/blob/master/src/test/persistence/JsonReaderTest.java

import model.AllProjects;
import model.BugSeverityLevel;
import model.Project;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonReaderTest extends JsonTest{

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            AllProjects allProjects = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyAllProjects() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyAllProjects.json");
        try {
            AllProjects allProjects = reader.read();
            assertEquals(0, allProjects.getNumberAllProjects());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralAllProjects() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralAllProjects.json");
        try {
            AllProjects allProjects = reader.read();

            List<Project> projects = allProjects.getProjectArrayList();
            assertEquals(2, projects.size());
            checkProject("proj1","creator 1",projects.get(0));
            assertEquals(3, projects.get(0).getBugArrayList().size());
            checkBug("bug1", "bruno","kelly", false, BugSeverityLevel.LOW,
                    projects.get(0).getBugArrayList().get(0));
            checkBug("bug2", "larry","jake", true, BugSeverityLevel.HIGH,
                    projects.get(0).getBugArrayList().get(1));
            checkBug("bug3", "billy","mark", false, BugSeverityLevel.MEDIUM,
                    projects.get(0).getBugArrayList().get(2));

            checkProject("proj2","creator 2",projects.get(1));
            assertEquals(0, projects.get(1).getBugArrayList().size());

        } catch (IOException e) {
            fail("Couldn't read from file");
        }

    }

}
