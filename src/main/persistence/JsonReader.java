package persistence;

// This class has been created with help of JsonReader Class in JsonSerializationDemo
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo/blob/master/src/main/persistence/JsonReader.java

import model.AllProjects;
import model.Bug;
import model.BugSeverityLevel;
import model.Project;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

// Represents a reader that reads AllProjects from JSON data stored in file
public class JsonReader {
    private String source;


    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads AllProjects from file and returns it;
    // throws IOException if an error occurs reading data from file
    public AllProjects read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseAllProjects(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses AllProjects from JSON object and returns it and calls parseProject
    // MODIFIES: allProjects
    private AllProjects parseAllProjects(JSONObject jsonObject) {
        AllProjects allProjects = new AllProjects();

        JSONArray allProjectsArray = jsonObject.getJSONArray("All Projects");

        for (int i = 0; i < allProjectsArray.length(); i++) {
            Project oneProject = parseProject(allProjectsArray.getJSONObject(i));
            allProjects.addProject(oneProject);
        }
        return allProjects;

    }

    // EFFECTS: parses project from JSON object and returns it and calls parseBug
    // MODIFIES: project
    private Project parseProject(JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        String creator = jsonObject.getString("creator");

        Project project = new Project(name, creator);

        JSONArray allBugsArray = jsonObject.getJSONArray("bugArrayList");

        for (int i = 0; i < allBugsArray.length(); i++) {
            Bug oneBug = parseBug(allBugsArray.getJSONObject(i));
            project.addBug(oneBug);
        }
        return project;

    }

    // EFFECTS: parses bug from JSON object and returns it
    private Bug parseBug(JSONObject jsonObject) {
        String title = jsonObject.getString("title");
        String assignee = jsonObject.getString("assignee");
        String publisher = jsonObject.getString("publisher");
        Boolean fixed = jsonObject.getBoolean("fixed");

        BugSeverityLevel severityLevel = jsonObject.getEnum(BugSeverityLevel.class, "severityLevel");

        Bug bug = new Bug(title,assignee,publisher,severityLevel);
        if (fixed.equals(true)) {
            bug.fixBug();
        }
        return bug;
    }


}
