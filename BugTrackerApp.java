package ui;

// This class has been created with help of WorkroomApp Class in JsonSerializationDemo
//https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo/blob/master/src/main/persistence/JsonReader.java


import model.AllProjects;
import model.Bug;
import model.BugSeverityLevel;
import model.Project;
import persistence.JsonReader;
import persistence.JsonWriter;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;



// Bug Tracker Application
public class BugTrackerApp {
    private Scanner input;
    private static final String JSON_STORE = "./data/allProjects.json";

    private AllProjects allProjects;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    //AllProjects allProjects = new AllProjects();
    Bug bug;

    // EFFECTS: runs the Bug Tracker application
    public BugTrackerApp() throws FileNotFoundException {
        allProjects = new AllProjects();
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        runBugTracker();
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    public void runBugTracker() {
        boolean keepGoing = true;
        String command = null;

        input = new Scanner(System.in).useDelimiter("\n");


        while (keepGoing) {

            displayMenu();

            command = input.next();
            command = command.toLowerCase();

            if (command.equals("q")) {
                keepGoing = false;
            } else {
                processCommand(command);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: processes user command
    public void processCommand(String command) {

        if (command.equals("c")) {
            Project myProject = createNewProject();
            allProjects.addProject(myProject);

        } else if (command.equals("r")) {
            if (allProjects.getProjectArrayList().size() > 0) {
                Project removeThisProject = removeProject(allProjects.getProjectArrayList());
                allProjects.removeProject(removeThisProject);
            } else {
                System.out.println("No projects available");
            }

        } else if (command.equals("v")) {
            if (allProjects.getProjectArrayList().size() > 0) {
                selectProject(command);
            } else {
                System.out.println("No projects available");
            }

        } else if (command.equals("s")) {
            saveAllProjects();

        } else if (command.equals("l")) {
            loadAllProjects();

        } else {
            System.out.println("Selection not valid...");
        }
    }

    // MODIFIES: this
    // EFFECTS: processes user command for a selected project
    public void selectProject(String command) {
        Project selectedProject = processSelectProject();

        boolean keepGoingBug = true;
        while (keepGoingBug) {
            displayBugMenu();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("q")) {
                keepGoingBug = false;

            } else if (command.equals("a")) {
                Bug newBug = createNewBug();
                selectedProject.addBug(newBug);

            } else if (command.equals("r")) {
                processRemoveCommand(selectedProject);

            } else if (command.equals("h")) {
                viewHistory(selectedProject);

            } else if (command.equals("m")) {
                processMarkBugCommand(selectedProject);

            } else if (command.equals("b")) {
                keepGoingBug = false;
            }
        }
    }

    // EFFECTS: creates a new project with the name and creator
    public Project createNewProject() {
        Project project = new Project(addProjectName(), addProjectCreator());
        return project;
    }

    // EFFECTS: shows a menu of all projects and processes the project the user selects
    public Project processSelectProject() {
        viewListOfProjects(allProjects);
        System.out.println("Enter the number of the project you want to select: ");
        int numberSelectedProject = input.nextInt();
        Project selectedProject = findProject(allProjects.getProjectArrayList(), numberSelectedProject);
        return selectedProject;
    }

    // EFFECTS: shows a history of all bugs and removes the selected bug from the user
    public void processRemoveCommand(Project selectedProject) {
        viewHistory(selectedProject);
        if (selectedProject.getBugArrayList().size() > 0) {
            Bug removeThisBug = removeBug(selectedProject.getBugArrayList());
            selectedProject.removeBug(removeThisBug);
        }
    }

    // EFFECTS: shows a history of all bugs and marks the selected bug from the user to fixed
    public void processMarkBugCommand(Project selectedProject) {
        viewHistory(selectedProject);
        if (selectedProject.getBugArrayList().size() > 0) {
            Bug markThisBug = markFixedBug(selectedProject.getBugArrayList());
            markThisBug.fixBug();
        }
    }

    // MODIFIES: this
    // EFFECTS: removes the selected project from list of all projects
    public Project removeProject(ArrayList<Project> projectArrayList) {
        viewListOfProjects(allProjects);
        System.out.println("Enter the Project number that you want to remove: ");
        int projectNumberRemove = input.nextInt();
        //find the bug in the list then remove it
        return findProject(projectArrayList,projectNumberRemove);
    }

    // EFFECTS: finds the project selected by the user and returns it
    public Project findProject(ArrayList<Project> projectList, int number) {
        return projectList.get((number - 1));
    }

    // MODIFIES: this
    // EFFECTS: takes a string input from the user and sets it to the name of the project
    public String addProjectName() {
        System.out.println("Enter Project Name: ");
        String name = input.next();
        return name;
    }

    // MODIFIES: this
    // EFFECTS: takes a string input from the user and sets it to the creator of the project
    public String addProjectCreator() {
        System.out.println("Enter Project Creator: ");
        String creator = input.next();
        return creator;
    }

    // EFFECTS: prints all projects in the all projects
    public void viewListOfProjects(AllProjects server) {
        ArrayList<Project> projectList = server.getProjectArrayList();
        for (int i = 0; i < projectList.size(); i++) {
            Project project = projectList.get(i);
            System.out.println((i + 1) + ": Title: " + project.getName() + "      Creator: " + project.getCreator());
        }

    }

    // MODIFIES: this
    // EFFECTS: creates a new bug and sets the title, assignee, publisher and severity level
    public Bug createNewBug() {
        String title = addBugTitle();
        String assignee = addBugAssignee();
        String publisher = addBugPublisher();
        BugSeverityLevel severityLevel = addBugSeverity();
        bug = new Bug(title, assignee, publisher, severityLevel);
        return bug;
    }

    // MODIFIES: this
    // EFFECTS: removes the selected bug from list of all bugs
    public Bug removeBug(ArrayList<Bug> bugArrayList) {
        System.out.println("Enter Bug number that you want to remove: ");
        int bugNumberRemove = input.nextInt();
        //find the bug in the list then remove it
        return findBug(bugArrayList,bugNumberRemove);
    }

    // EFFECTS: finds the bug selected by the user and returns it
    public Bug findBug(ArrayList<Bug> bugsList, int number) {
        //loop through array to find the bug
        return bugsList.get(number - 1);
    }

    // EFFECTS: prints all bugs in the all bugs
    public void viewHistory(Project project) {
        if (project.getBugArrayList().size() > 0) {
            ArrayList<Bug> bugsList = project.getBugArrayList();
            for (int i = 0; i < bugsList.size();i++) {
                Bug bug = bugsList.get(i);
                System.out.println((i + 1) + ": Title: " + bug.getTitle());
                System.out.println("   Publisher: " + bug.getPublisher());
                System.out.println("   Assignee: " + bug.getAssignee());
                System.out.println("   Severity Level: " + bug.getSeverityLevel());
                System.out.println("   Fixed: " + bug.isFixed());
            }
        } else {
            System.out.println("No Bugs available");
        }
    }

    // MODIFIES: this
    // EFFECTS: marks the bug selected by the user to fixed
    public Bug markFixedBug(ArrayList<Bug> bugArrayList) {
        System.out.println("Enter Bug number that you want to mark as fixed: ");
        int bugNumberFix = input.nextInt();
        //find the bug in the list then fix it
        return findBug(bugArrayList,bugNumberFix);
    }



    // EFFECTS: sets the input string from the user as the title of the bug
    public String addBugTitle() {
        System.out.println("Enter Bug Title: ");
        String title = input.next();
        return title;
    }

    // EFFECTS: sets the input string from the user as the assignee of the bug
    public String addBugAssignee() {
        System.out.println("Enter Bug Assignee: ");
        String assignee = input.next();
        return assignee;
    }

    // EFFECTS: sets the input string from the user as the severity level of the bug
    public BugSeverityLevel addBugSeverity() {
        System.out.println("Enter Bug Severity Level: (LOW, MEDIUM, HIGH) ");
        BugSeverityLevel severity = BugSeverityLevel.valueOf(input.next());
        return severity;
    }

    // EFFECTS: sets the input string from the user as the publisher of the bug
    public String addBugPublisher() {
        System.out.println("Enter Bug Publisher: ");
        String publisher = input.next();
        return publisher;
    }

    // EFFECTS: displays the Bug menu of options to user
    public void displayBugMenu() {
        System.out.println("Select from");
        System.out.println("\ta -> Add new Bug");
        System.out.println("\tr -> Remove Bug");
        System.out.println("\th -> View history of Bugs");
        System.out.println("\tm -> Mark fixed Bug");
        System.out.println("\tb -> Go Back");
    }

    // EFFECTS: displays the main menu of options to user
    public void displayMenu() {
        System.out.println("Select from");
        System.out.println("\tc -> Create New Project");
        System.out.println("\tr -> Remove Project");
        System.out.println("\tv -> View and Select Projects");
        System.out.println("\ts -> Save project to file");
        System.out.println("\tl -> Load project from file");
        System.out.println("\tq -> Quit");
    }



    // EFFECTS: saves the workroom to file
    private void saveAllProjects() {
        try {
            jsonWriter.open();
            jsonWriter.writeAllProjects(allProjects);
            jsonWriter.close();
            System.out.println("Saved all projects to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads workroom from file
    private void loadAllProjects() {
        try {
            allProjects = jsonReader.read();
            System.out.println("Loaded all projects from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }


}
