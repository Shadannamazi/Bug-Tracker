package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class AllProjectsTest {

    private AllProjects allProjectsTest;
    private Project project1;
    private Project project2;
    private Project project3;
    private Project project4;

    @BeforeEach
    public void runBefore() {
        allProjectsTest = new AllProjects();
        project1 = new Project("pro1", "creator1");
        project2 = new Project("pro2", "creator2");
        project3 = new Project("pro3", "creator3");
        project4 = new Project("pro4", "creator4");
    }

    @Test
    public void testConstructor() {
        assertEquals(0,allProjectsTest.getNumberAllProjects());
    }

    @Test
    public void testAddProject() {
        allProjectsTest.addProject(project1);
        assertEquals(1,allProjectsTest.getNumberAllProjects());
        assertEquals(project1,allProjectsTest.getProjectArrayList().get(0));
    }

    @Test
    public void testAddMultipleProjects() {
        allProjectsTest.addProject(project1);
        allProjectsTest.addProject(project2);
        assertEquals(2,allProjectsTest.getNumberAllProjects());
        allProjectsTest.addProject(project3);
        allProjectsTest.addProject(project4);
        assertEquals(4,allProjectsTest.getNumberAllProjects());
        assertEquals(project1,allProjectsTest.getProjectArrayList().get(0));
        assertEquals(project2,allProjectsTest.getProjectArrayList().get(1));
        assertEquals(project3,allProjectsTest.getProjectArrayList().get(2));
        assertEquals(project4,allProjectsTest.getProjectArrayList().get(3));

    }

    @Test
    public void testAddSameProject() {
        allProjectsTest.addProject(project1);
        assertEquals(1,allProjectsTest.getNumberAllProjects());
        assertEquals(project1,allProjectsTest.getProjectArrayList().get(0));

        allProjectsTest.addProject(project1);
        assertEquals(2,allProjectsTest.getNumberAllProjects());
        assertEquals(project1,allProjectsTest.getProjectArrayList().get(1));

        allProjectsTest.addProject(project1);
        assertEquals(3,allProjectsTest.getNumberAllProjects());
        assertEquals(project1,allProjectsTest.getProjectArrayList().get(2));

    }

    @Test
    public void testRemoveProject() {
        allProjectsTest.addProject(project1);
        allProjectsTest.addProject(project2);
        allProjectsTest.addProject(project3);
        allProjectsTest.addProject(project4);

        allProjectsTest.removeProject(project1);
        assertEquals(3,allProjectsTest.getNumberAllProjects());
        assertFalse(allProjectsTest.getProjectArrayList().contains(project1));

    }

    @Test
    public void testRemoveMultipleProjects() {
        allProjectsTest.addProject(project1);
        allProjectsTest.addProject(project2);
        allProjectsTest.addProject(project3);
        allProjectsTest.addProject(project4);

        allProjectsTest.removeProject(project1);
        assertEquals(3,allProjectsTest.getNumberAllProjects());
        assertFalse(allProjectsTest.getProjectArrayList().contains(project1));

        allProjectsTest.removeProject(project2);
        assertEquals(2,allProjectsTest.getNumberAllProjects());
        assertFalse(allProjectsTest.getProjectArrayList().contains(project2));

        allProjectsTest.removeProject(project3);
        assertEquals(1,allProjectsTest.getNumberAllProjects());
        assertFalse(allProjectsTest.getProjectArrayList().contains(project3));

        allProjectsTest.removeProject(project4);
        assertEquals(0,allProjectsTest.getNumberAllProjects());
        assertFalse(allProjectsTest.getProjectArrayList().contains(project4));
    }

    @Test
    public void testRemoveNonExistent() {
        allProjectsTest.addProject(project1);
        allProjectsTest.addProject(project2);
        allProjectsTest.removeProject(project3);
        allProjectsTest.removeProject(project4);
        assertEquals(2,allProjectsTest.getProjectArrayList().size());

    }
}