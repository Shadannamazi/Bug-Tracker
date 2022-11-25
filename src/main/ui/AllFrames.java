package ui;


import model.AllProjects;
import model.Event;
import model.EventLog;
import model.Project;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
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

// Constructs AllFrames based on JFrame
public abstract class AllFrames extends JFrame implements ActionListener, WindowListener {

    public static final int WIDTH = 600;
    public static final int HEIGHT = 600;
    public static final Color COLOR = new Color(40, 40, 40);

    protected static final String JSON_STORE = "./data/allProjects.json";

    //create all projects
    protected AllProjects allProjects = new AllProjects();
    protected JsonWriter jsonWriter;
    protected JsonReader jsonReader;

    ArrayList<Object[]> wasteButtonsCreate = new ArrayList<>();
    ArrayList<JButton> wasteButtonsRemove = new ArrayList<>();
    protected JComponent viewAllPanel = new JPanel();
    protected JComponent removePanel = new JPanel();
    protected JComponent createPanelTab1 = new JPanel();


    //create frame
    protected JFrame frame = new JFrame();
    protected JPanel mainPanel;

    protected JButton createButtonTab1;
    protected JButton removeButtonTab2;
    protected JTextField fieldRemoveTab2 = new JTextField();

    protected JButton loadButton = new JButton("Load Activity");
    protected JButton saveActivityButton = new JButton("Save Activity");
    protected JButton refreshButton = new JButton("Refresh");

    protected JLabel projectName = new JLabel("Name");
    protected JLabel projectCreator = new JLabel("Creator");
    protected JLabel removeLabel;



    //MODIFIES: this
    //EFFECTS: constructs the allFrames with given type(bug or project) title and tab1, 2,3
    protected AllFrames(String title, String type,String tab1, String tab2, String tab3) {

        createButtonTab1 = new JButton("Create New " + type);
        removeButtonTab2 = new JButton("Remove " + type);
        removeLabel = new JLabel("Select " + type + ": ");

        frame.setTitle(title);
        frame.setSize(WIDTH,HEIGHT);
        frame.setResizable(false);
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(COLOR);

        createButtonsForFrame(tab1,tab2,tab3);
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);

        this.revalidate();
        this.repaint();

        viewAllPanel.setLayout(null);
        removePanel.setLayout(null);

        frame.setVisible(true);
    }


    //MODIFIES: this
    //EFFECTS: creates buttons for the frame with tab names
    protected void createButtonsForFrame(String tab1, String tab2, String tab3) {
        //create new main panel
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBounds(100,0,500,575);
        mainPanel.setBackground(COLOR);
        frame.add(mainPanel);

        JLabel selectProject = new JLabel();
        //sets bug image
        ImageIcon bugLogo = new ImageIcon(new ImageIcon("data/bugLogo.png").getImage().getScaledInstance(
                100, 50, Image.SCALE_DEFAULT));

        selectProject.setVerticalAlignment(JLabel.TOP);
        selectProject.setIcon(bugLogo);
        selectProject.setBounds(0,0,500,50);

        setSaveActivityButton();
        setLoadButton();
        setRefreshButton();

        frame.add(selectProject);

        createTabs(tab1,tab2,tab3);
        this.revalidate();
        this.repaint();


    }

    //MODIFIES: this
    //EFFECTS: constructs save button
    protected void setSaveActivityButton() {
        saveActivityButton.setBounds(0, 50, 100, 50);
        saveActivityButton.addActionListener(this);
        frame.add(saveActivityButton, BorderLayout.CENTER);
    }

    //MODIFIES: this
    //EFFECTS: constructs load button
    protected void setLoadButton() {
        loadButton.setBounds(0, 100, 100, 50);
        loadButton.addActionListener(this);
        frame.add(loadButton, BorderLayout.CENTER);
    }

    //MODIFIES: this
    //EFFECTS: constructs refresh button
    protected void setRefreshButton() {
        refreshButton.setBounds(0, 150, 100, 50);
        refreshButton.addActionListener(this);
        frame.add(refreshButton, BorderLayout.CENTER);
    }

    //MODIFIES: this
    //EFFECTS: constructs tabs with title tab1,2,3
    protected void createTabs(String tab1, String tab2, String tab3) {

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(Color.DARK_GRAY);

        JComponent createForTab = createTab1();
        createForTab.setBackground(new Color(90, 90, 90));
        tabbedPane.addTab(tab1,createForTab);

        JComponent removeForTab = createTab2();
        removeForTab.setBackground(new Color(90, 90, 90));
        tabbedPane.addTab(tab2,removeForTab);

        this.revalidate();
        this.repaint();
        JComponent viewForTab = createTab3();
        viewForTab.setBackground(new Color(90, 90, 90));
        tabbedPane.addTab(tab3,viewForTab);

        mainPanel.add(tabbedPane);

    }

    //MODIFIES: this
    //EFFECTS: constructs tab1
    protected JComponent createTab1() {

        createPanelTab1.setLayout(null);
        createButtonTab1.setBounds(0, 250, 475, 25);
        createButtonTab1.addActionListener(this);
        createPanelTab1.add(createButtonTab1, BorderLayout.CENTER);

        this.revalidate();
        this.repaint();

        return createPanelTab1;
    }



    //MODIFIES: this
    //EFFECTS: constructs tab2
    protected JComponent createTab2() {

        removeButtonTab2.setBounds(0, 500, 475, 25);
        removeButtonTab2.addActionListener(this);
        removePanel.add(removeButtonTab2, BorderLayout.CENTER);

        fieldRemoveTab2.setBounds(115, 475, 355, 25);

        removePanel.add(fieldRemoveTab2,BorderLayout.CENTER);

        showUpdated();
        this.revalidate();
        this.repaint();

        return removePanel;
    }

    //MODIFIES: this
    //EFFECTS: constructs tab3
    protected JComponent createTab3() {

        showUpdated();
        this.revalidate();
        this.repaint();

        return viewAllPanel;
    }

    //MODIFIES: this
    //EFFECTS: shows updated panels
    public abstract void showUpdated();


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

    // MODIFIES: this
    // EFFECTS: loads workroom from file
    protected void loadAllProjects() {
        try {
            allProjects = jsonReader.read();
            System.out.println("Loaded all projects from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
        showUpdated();
    }

    //MODIFIES: this
    //EFFECTS: sets the action listeners to save, load and refresh the projects
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == saveActivityButton) {
            saveAllProjects();

        } else if (e.getSource() == loadButton) {
            loadAllProjects();

        } else if (e.getSource() == refreshButton) {
            showUpdated();

        }

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


}

