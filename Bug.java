package model;

import java.util.Objects;
import model.BugSeverityLevel;
import org.json.JSONObject;
import persistence.Writable;

//Represents a bug having a title, an assignee, a publisher, a boolean to check if its fixed and
//a severity level
public class Bug implements Writable {
    private String title;
    private String assignee;
    private String publisher;
    private boolean isFixed;
    private BugSeverityLevel severityLevel;

    //REQUIRES: title, assignee, publisher, severityLevel has a non-zero length
    //EFFECTS: title of bug is set to title; assignee of bug is set to assignee;
    //publisher of bug is set to publisher; severityLevel of bug to severityLevel
    //sets the boolean fixed to false
    public Bug(String title, String assignee, String publisher, BugSeverityLevel severityLevel) {
        this.title = title;
        this.assignee = assignee;
        this.publisher = publisher;
        this.isFixed = false;
        this.severityLevel = severityLevel;
    }

    public String getTitle() {
        return this.title;
    }

    public String getAssignee() {
        return this.assignee;
    }

    public String getPublisher() {
        return this.publisher;
    }

    public BugSeverityLevel getSeverityLevel() {
        return this.severityLevel;
    }

    //EFFECTS: return whether the bug is fixed or not fixed
    public boolean isFixed() {
        return this.isFixed;
    }

    //EFFECTS: sets the fixed boolean to true
    //MODIFIES: this
    public void fixBug() {
        this.isFixed = true;
        EventLog.getInstance().logEvent(new Event("Fixed bug: " + this.getTitle()));
    }

    //EFFECTS: converts bug to Json
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("title", title);
        json.put("assignee", assignee);
        json.put("publisher", publisher);
        json.put("fixed", isFixed);
        json.put("severityLevel", severityLevel);
        return json;
    }
}


