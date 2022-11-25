package persistence;

//// This class has been created with help of JsonReader Class in JsonSerializationDemo
//https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo/blob/master/src/test/persistence/JsonWriterTest.java

import model.AllProjects;
import model.Bug;
import model.BugSeverityLevel;
import model.Project;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonWriterTest extends JsonTest{
    //write data to a file and then use the reader to read it back in and check that we
    //read in a copy of what was written out

    @Test
    void testWriterInvalidFile() {
        try {
            AllProjects allProjects = new AllProjects();
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyWorkroom() {
        try {
            AllProjects allProjects = new AllProjects();
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyWorkroom.json");
            writer.open();
            writer.writeAllProjects(allProjects);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyWorkroom.json");
            allProjects = reader.read();
            assertEquals(0, allProjects.getNumberAllProjects());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralAllProjects() {
        try {
            AllProjects allProjects = new AllProjects();
            Project project1 = new Project("myProject", "writerCreator1");
            Project project2 = new Project("myProject2", "writerCreator2");

            Bug bug1 = new Bug("myBug1","sam", "writer1", BugSeverityLevel.LOW);
            Bug bug2 = new Bug("myBug2","sarah", "writer2", BugSeverityLevel.HIGH);
            Bug bug3 = new Bug("myBug3","smith", "writer3", BugSeverityLevel.MEDIUM);

            allProjects.addProject(project1);
            allProjects.addProject(project2);
            project1.addBug(bug1);
            project1.addBug(bug2);
            project2.addBug(bug3);

            JsonWriter writer = new JsonWriter("./data/testWriterGeneralWorkroom.json");
            writer.open();
            writer.writeAllProjects(allProjects);
            writer.close();

            //now read from file
            JsonReader reader = new JsonReader("./data/testWriterGeneralWorkroom.json");
            allProjects = reader.read();

            List<Project> projects = allProjects.getProjectArrayList();
            assertEquals(2, projects.size());
            checkProject("myProject","writerCreator1",projects.get(0));
            checkProject("myProject2","writerCreator2",projects.get(1));

            assertEquals(2, projects.get(0).getBugArrayList().size());
            checkBug("myBug1", "writer1","sam", false, BugSeverityLevel.LOW,
                    projects.get(0).getBugArrayList().get(0));
            checkBug("myBug2", "writer2","sarah", false, BugSeverityLevel.HIGH,
                    projects.get(0).getBugArrayList().get(1));

            assertEquals(1, projects.get(1).getBugArrayList().size());
            checkBug("myBug3", "writer3","smith", false, BugSeverityLevel.MEDIUM,
                    projects.get(1).getBugArrayList().get(0));

            assertEquals(1, projects.get(1).getBugArrayList().size());

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }

    }

}
