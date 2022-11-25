package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;

// Represents a project having a name,a creator, and an arraylist of bugs
public class Project implements Writable {

    private String name;                  //project name
    private String creator;               //project creator
    private ArrayList<Bug> bugArrayList;  //arraylist of bugs for project


    //REQUIRES: projectName and projectCreator has a non-zero length
    //EFFECTS: name on project is set to projectName; creator on project is set to projectCreator;
    // name on project is set to projectName; Creates an arraylist to store the bugs
    public Project(String projectName, String projectCreator) {

        this.name = projectName;
        this.creator = projectCreator;
        this.bugArrayList = new ArrayList<>();
    }

    public String getName() {
        return this.name;
    }

    public String getCreator() {
        return this.creator;
    }

    public ArrayList<Bug> getBugArrayList() {
        return bugArrayList;
    }

    //EFFECTS: adds bug to arraylist of bugs
    //MODIFIES: this
    public void addBug(Bug bug) {
        bugArrayList.add(bug);
        EventLog.getInstance().logEvent(new Event("Added bug: " + bug.getTitle()
                + " to all bugs"));
    }

    //EFFECTS: adds bug to arraylist of bugs
    //MODIFIES: this
    public void removeBug(Bug bug) {
        bugArrayList.remove(bug);
        EventLog.getInstance().logEvent(new Event("Removed bug: " + bug.getTitle()
                + " from all bugs"));
    }

    //EFFECTS: returns number of bugs in project
    public int getSizeBugList() {
        return bugArrayList.size();
    }


    //EFFECTS: converts project to Json
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("creator", creator);
        json.put("bugArrayList", bugArrayListToJson());
        return json;
    }

    //EFFECTS: converts each bug in project to Json
    private JSONArray bugArrayListToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Bug b : bugArrayList) {
            jsonArray.put(b.toJson());
        }
        return jsonArray;
    }

}

