package ui;

import model.*;
import model.Event;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

// citation: https://docs.oracle.com/javase/tutorial/uiswing/
// https://www.youtube.com/watch?v=Kmgo00avvEw&t=2811s

// Constructs ProjectFrame based on JFrame
public class ProjectFrame extends AllFrames implements ActionListener, WindowListener {

    private static final String JSON_STORE = "./data/allProjects.json";

    protected JLabel bugName = new JLabel("Name");
    protected JLabel bugPublisher = new JLabel("Publisher");
    protected JLabel bugAssignee = new JLabel("Assignee");
    //protected JLabel bugIsFixed = new JLabel("Fixed");
    protected JLabel bugSeverityLevel = new JLabel("Severity Level");

    protected JLabel removeBug = new JLabel("Select Bug: ");
    protected JButton createBugButton;
    protected JButton removeBugButton;

    protected JButton fixBugButton;
    protected JLabel fixBug;
    protected JTextField fieldFixBug;

    protected JTextField fieldBugName;
    protected JTextField fieldBugPublisher;
    protected JTextField fieldBugAssignee;

    protected JComboBox bugSeverityLevelList;


    private Project project;
    private AllProjects allProjects;

    protected DefaultTableModel tableModel;
    protected JTable table;

    // Constructs each project frame based on JFrame by calling its super with bug type and tab names
    public ProjectFrame(AllProjects allProjects,Project project) {

        super(project.getName(),"Bug", "Create New Bug", "Remove Bug", "View All Bugs");

        this.allProjects = allProjects;

        this.project = project;
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        bugSeverityLevelList.addActionListener(this);

    }

    //MODIFIES: this
    //EFFECTS: shows updated bug panels
    @Override
    public void showUpdated() {
        resetViewAllBugs();
        resetRemoveBugs();
        showUpdatedAllBugsPanel();
        showUpdatedRemoveBugPanel();
        frame.revalidate();
        frame.repaint();

    }


    //MODIFIES: this
    //EFFECTS: constructs new tab1
    @Override
    public JComponent createTab1() {
        createPanelTab1 = super.createTab1();

        fieldBugName = new JTextField();
        fieldBugPublisher = new JTextField();
        fieldBugAssignee = new JTextField();
        //checkBoxBugIsFixed = new JCheckBox();
        bugSeverityLevelList = new JComboBox(BugSeverityLevel.values());

        bugName = new JLabel("Name");
        bugPublisher = new JLabel("Publisher");
        bugAssignee = new JLabel("Assignee");
        //bugIsFixed = new JLabel("Fixed");
        bugSeverityLevel = new JLabel("Severity Level");
        bugName.setForeground(Color.white);
        bugPublisher.setForeground(Color.white);
        bugAssignee.setForeground(Color.white);
        //bugIsFixed.setForeground(Color.white);
        bugSeverityLevel.setForeground(Color.white);

        setBoundsButtonsFields();

        createPanelTab1.add(fieldBugName);
        createPanelTab1.add(fieldBugPublisher);
        createPanelTab1.add(fieldBugAssignee);
        //createPanelTab1.add(checkBoxBugIsFixed);
        createPanelTab1.add(bugSeverityLevelList);

        createPanelTab1.add(bugName);
        createPanelTab1.add(bugPublisher);
        createPanelTab1.add(bugAssignee);
        //createPanelTab1.add(bugIsFixed);
        createPanelTab1.add(bugSeverityLevel);

        return createPanelTab1;

    }

    //MODIFIES: this
    //EFFECTS: sets the bounds of the buttons in the frame
    public void setBoundsButtonsFields() {
        fieldBugName.setBounds(100,50,200,25);
        fieldBugPublisher.setBounds(100,100,200,25);
        fieldBugAssignee.setBounds(100,150,200,25);
        //checkBoxBugIsFixed.setBounds(100,150,200,25);
        bugSeverityLevelList.setBounds(100,200,200,25);
        bugName.setBounds(15,50,100,25);
        bugPublisher.setBounds(15,100,100,25);
        bugAssignee.setBounds(15,150,100,25);
        //bugIsFixed.setBounds(15,150,100,25);
        bugSeverityLevel.setBounds(15,200,100,25);
    }

    //MODIFIES: this
    //EFFECTS: constructs new tab2
    @Override
    public JComponent createTab2() {
        removePanel = super.createTab2();
        removeBug = new JLabel("Select Bug: ");
        removeBug.setBounds(15, 475, 125, 25);
        removeBug.setForeground(Color.white);
        removePanel.add(removeBug);
        showUpdated();

        return removePanel;
    }

    //MODIFIES: this
    //EFFECTS: constructs new tab3
    @Override
    public JComponent createTab3() {
        viewAllPanel = super.createTab3();
        fixBugButton = new JButton("Fix Bug");
        fixBug = new JLabel("Select Bug: ");
        fieldFixBug = new JTextField();

        fixBugButton.setBounds(0, 500, 475, 25);
        fixBugButton.addActionListener(this);
        viewAllPanel.add(fixBugButton, BorderLayout.CENTER);

        fieldFixBug.setBounds(115, 475, 355, 25);

        viewAllPanel.add(fieldFixBug,BorderLayout.CENTER);
        fixBug.setBounds(15, 475, 125, 25);
        fixBug.setForeground(Color.white);

        viewAllPanel.add(fixBug);
        return viewAllPanel;
    }


    // MODIFIES: this
    // EFFECTS: resets view projects
    public void resetViewAllBugs() {
        if (table != null) {
            viewAllPanel.remove(table);
        }

    }


    // MODIFIES: this
    // EFFECTS: resets remove project
    public void resetRemoveBugs() {
        for (JButton button : wasteButtonsRemove) {
            removePanel.remove(button);
        }
    }



    //https://stackoverflow.com/questions/20526917/load-arraylist-data-into-jtable
    ////MODIFIES: this
    // EFFECTS: draws a table to store the bugs of the project
    public void drawBugTable(JComponent panel) {
        ArrayList<Bug> bugList = project.getBugArrayList();

        String[] columnNames = {"#", "Title", "Publisher", "Assignee", "Severity Level", "Fixed"};
        tableModel = new DefaultTableModel(columnNames, 0) {

            boolean[] canEdit = new boolean[]{
                    false, false, false, false, false,true
            };
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {

                return columnIndex == 5;
            }
        };
        table = new JTable(tableModel);
        table.setBounds(0, 0, 480,(bugList.size() + 1) * 16);
        table.setShowHorizontalLines(true);
        table.setGridColor(new Color(40,40,40));


        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.getColumnModel().getColumn(0).setPreferredWidth(2);
        table.getColumnModel().getColumn(1).setPreferredWidth(200);

        tableModel.addRow(columnNames);

        panel.add(table);

    }

    //MODIFIES: this
    //EFFECTS: shows updated remove bugs panel
    public void showUpdatedRemoveBugPanel() {

        if (project != null) {
            if (project.getSizeBugList() > 0) {

                ArrayList<Bug> bugList = project.getBugArrayList();
                for (int i = 0; i < bugList.size(); i++) {
                    Bug bug = bugList.get(i);
                    JButton projectButton = new JButton((i + 1) + ": Title: " + bug.getTitle() + " -"
                            + " Publisher: " + bug.getPublisher());

                    projectButton.setBounds(0, i * 25 + 25, 475, 25);
                    projectButton.setHorizontalAlignment(SwingConstants.LEFT);
                    projectButton.addActionListener(this);

                    this.revalidate();

                    removePanel.add(projectButton, BorderLayout.CENTER);
                    wasteButtonsRemove.add(projectButton);


                    this.revalidate();
                }

            }

        }

    }


    //MODIFIES: this
    //EFFECTS: shows updated all bugs panel
    public void showUpdatedAllBugsPanel() {


        if (project != null) {
            if (project.getSizeBugList() > 0) {

                ArrayList<Bug> bugList = project.getBugArrayList();

                drawBugTable(viewAllPanel);

                for (int i = 0; i < bugList.size(); i++) {

                    Bug bug = bugList.get(i);

                    Object[] bugRow = {i + 1, bug.getTitle(),bug.getPublisher(),bug.getAssignee(),
                            bug.getSeverityLevel(), bug.isFixed()};

                    tableModel.addRow(bugRow);

                    this.revalidate();
                    this.repaint();

                    wasteButtonsCreate.add(bugRow);
                    this.revalidate();
                }

            }
        }

    }


    //MODIFIES: this
    //EFFECTS: sets the action listeners for create button in tab1, remove button in tab2, fix button in tab3
    // save, load and refresh the bugs
    @Override
    public void actionPerformed(ActionEvent e) {
        //project = new Project(projectName,projectCreator);
        if (e.getSource() == createButtonTab1) {
            BugSeverityLevel severityLevel;


            severityLevel = (BugSeverityLevel) bugSeverityLevelList.getSelectedItem();


            Bug bug = new Bug(fieldBugName.getText(), fieldBugAssignee.getText(), fieldBugPublisher.getText(),
                    severityLevel);
            project.addBug(bug);
            showUpdated();

        } else if (e.getSource() == removeButtonTab2) {

            Bug removeBug = project.getBugArrayList().get(Integer.parseInt(fieldRemoveTab2.getText()) - 1);

            project.removeBug(removeBug);
            showUpdated();

        } else if (e.getSource() == fixBugButton) {


            project.getBugArrayList().get(Integer.parseInt(fieldFixBug.getText()) - 1).fixBug();
            showUpdated();

        } else if (e.getSource() == refreshButton) {
            showUpdated();

        }  else if (e.getSource() == loadButton) {
            loadAllProjects();

        } else if (e.getSource() == saveActivityButton) {
            saveAllProjects();

        }


    }

    //MODIFIES: this
    // EFFECTS: saves the workroom to file
    protected void saveAllProjects() {
        try {
            jsonWriter.open();
            jsonWriter.writeAllProjects(allProjects);
            jsonWriter.close();
            System.out.println("Saved all projects to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
        showUpdated();
    }


    @Override
    public void windowOpened(WindowEvent e) {

    }

    //EFFECTS: Action when window is closing
    @Override
    public void windowClosing(WindowEvent e) {
        for (Event ev: EventLog.getInstance()) {
            System.out.println(ev.toString());
        }
    }

    //EFFECTS: Action when window closed
    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}
