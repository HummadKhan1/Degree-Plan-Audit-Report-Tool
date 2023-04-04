import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.JTextField;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * This class is primarily used to display the pre-view of the selected Degree Plan using a JFrame, 
 * but it is also used to get some user input through a separate JFrame. 
 * 
 */
public class PreViewWindow{
    private CountDownLatch latch; // Used to make the program wait until user has finished all interaction with GUIs
    private String typeOfDegreePlan = ""; // Variable will contain the type of degree plan selected by the user 
    private String fastTrackORThesis = ""; // Variable stores the users input for either Fast Track or Thesis
    final private String CORE_COURSES_LABEL = "CORE COURSES    (15 Credit Hours)   3.19 Grade Point Average Required";
    final private String APPROVED_6000_LEVEL_ELECTIVES_LABEL = "FIVE APPROVED 6000 LEVEL ELECTIVES    (15 * Credit Hours)    3.0 Grade Point Average";
    final private String ADDITIONAL_ELECTIVES_LABEL = "Additional Electives (3 Credit Hours Minimum)";
    final private String OTHER_REQUIREMENTS = "Other Requirements";
    final private String CYBER_SECURITY = "Cyber Security";
    final private String DATA_SCIENCE = "Data Science";
    final private String INTELLIGENT_SYSTEMS = "Intelligent Systems";
    final private String INTERACTIVE_COMPUTING = "Interactive Computing";
    final private String NETWORKS_AND_TELECOMMUNICATIONS = "Networks and Telecommunications";
    final private String SYSTEMS = "Systems";
    final private String TRADITIONAL_COMPUTER_SCIENCE = "Traditional Computer Science";
    final private String SOFTWARE_ENGINEERING = "Software Engineering";
    private String selectedTrack = null; // Variable will store the specific Masters track 
    private String selectedLeveling_Prereq = null; // Variable will store the currently selected leveling/pre-requisite course 
    private ArrayList<String> defaultCSTracks = new ArrayList<>(); // ArrayList will store the CS tracks available 
    private ArrayList<String> defaultSETracks = new ArrayList<>(); // ArrayList will store the SE tracks available 
    private ArrayList<String> defaultLeveling = new ArrayList<>(); // ArrayList will store the leveling/pre-requisite courses that are possible 
    private ArrayList<String> chosenLeveling_Prereq = new ArrayList<>(); // ArrayList will store the leveling/pre-requisite courses chosen by the user 
    private ArrayList<Course> coursesList; // ArrayList contains all the courses read-in from the transcript after some filtering
    private HashMap<String, ArrayList<Course>> defaultCoursesMap; // Hashmap stores the default courses found in the Default.txt file
                                                                  // with the key being the type of track the courses fall under 
    
    /**
     * Constructor.
     * 
     * @param courses
     * @param defaultCSTracks
     * @param defaultSETracks
     * @param defaultLeveling
     * @param hashMap 
     */
    public PreViewWindow(ArrayList<Course> courses, ArrayList<String> defaultCSTracks, ArrayList<String> defaultSETracks, ArrayList<String> defaultLeveling, 
            HashMap<String, ArrayList<Course>> hashMap){
        setLatch(new CountDownLatch(1));
        this.coursesList = new ArrayList<>(courses); 
        this.defaultCSTracks = new ArrayList<String>(defaultCSTracks);
        this.defaultSETracks = new ArrayList<String>(defaultSETracks);
        this.defaultLeveling = new ArrayList<String>(defaultLeveling);    
        this.defaultCoursesMap = new HashMap<String, ArrayList<Course>>(hashMap); 
        
        SwingUtilities.invokeLater(() -> {
            createUserInputFrame();
        });
    }

    /**
     * Method creates one frame and said frame creates the capability to get user input.
     * 
     */
    public void createUserInputFrame(){
        int choice = CS_or_SE();
        JFrame frame = new JFrame("User Input Frame"); // Creates a new JFrame. This will act as the sole frame to display the 
                                                       // frame that will accept user input  
                                                       
        frame.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e){
                //SwingUtilities.invokeLater(PreViewWindow.this::createFrameAndTables);
                SwingUtilities.invokeLater(() -> {
                    createFrameAndTables();
                });
            }
        });

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));  // Create a new JPanel to act as my main panel in the frame. 20 pixel vertical gap between panels
        mainPanel.add(fastTrackORThesis(), BorderLayout.NORTH);
        mainPanel.add(specificTrack(choice), BorderLayout.CENTER); 
        mainPanel.add(leveling_preReq_Courses(), BorderLayout.SOUTH); 
        
        // Create a new JScrollPane and add the mainPanel 
        JScrollPane scrollPane = new JScrollPane(mainPanel); // The new JScrollPane object is used to make the frame scrollable using scroll bar 
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); // Vertical scroll bar policy is set to always show the scroll bar

        frame.getContentPane().add(scrollPane); // Add the scroll pane to the content pane of the JFrame
        frame.pack(); // Pack the frame, which sets its size to the preferred size of its components
        frame.setVisible(true); // Make the frame visible 
    }
    
    /**
     * Method is used to display a dialogue box where the user can select what track category to use. 
     * 
     * @return an int representing the chosen option: [1] Computer Science Track, [2] Software Engineering Track
     */
    public int CS_or_SE(){
        String[] options = {"[1] Computer Science Track", "[2] Software Engineering Track"};
        
        // Create dialog box 
        int choice = JOptionPane.showOptionDialog(null, "Please choose an option:",
                "Options", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options, options[0]);

        // Executes if the user closes the dialog box, result is exit program
        if (choice == JOptionPane.CLOSED_OPTION) 
        {
            System.exit(0);
        }
        
        return choice + 1; 
    }
    
    /**
     * Method is used to create radio buttons that will store the users choice of either Fast Track or Thesis.
     * 
     * @return the radio button panel  
     */
    public JPanel fastTrackORThesis(){
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Create new JPanel, will house the radio buttons 
        
        // Create radio buttons 
        JRadioButton radioButton1 = new JRadioButton("Fast Track");
        JRadioButton radioButton2 = new JRadioButton("Thesis");

        radioButton1.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                setFastTrackORThesis(radioButton1.getText());
            }
        });

        radioButton2.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                setFastTrackORThesis(radioButton2.getText());
            }
        });
        
        // Add the radio buttons to a ButtonGroup so only one radio button can be selected
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(radioButton1);
        buttonGroup.add(radioButton2);

        panel.add(radioButton1);
        panel.add(radioButton2);
        
        return panel; 
    }
    
    /**
     * Method creates a GUI panel for displaying and manipulating a list of possible tracks.
     * Note that the list of possible tracks to display and manipulate depends on the trackCategory value being passed in.
     * 
     * @param trackCategory
     * @return the GUI panel for displaying and manipulating the list of possible tracks
     */
    public JPanel specificTrack(int trackCategory){
        JPanel panel = new JPanel(); // Create new JPanel
        
        DefaultListModel<String> listModel = new DefaultListModel<String>();  // Create a new DefaultListModel to store the items in the JList
        JList<String> trackList = new JList<String>(listModel); // Create a new JList and initialize it with the DefaultListModel
        trackList.setPreferredSize(new Dimension(300, 200)); // Set the size of the JList to 300 x 200 pixels
        
        JTextField newTrackField = new JTextField(30); // Create a new JTextField for entering new tracks to be added to the list.
                                                       // Text field will be able to display up to 30 characters of text at a time
        
        // Create necessary buttons 
        JButton selectButton = new JButton("Select");
        JButton deleteButton = new JButton("Delete");
        JButton addButton = new JButton("Add");
        
        // List of possible tracks to display and manipulate depends on the trackCategory value
        if (trackCategory == 1)
        {
            for (String course : defaultCSTracks) // Add the CS tracks to the list model
            {
                listModel.addElement(course);
            }
        }
        else
        {
            for (String course : defaultSETracks) // Add the SE tracks to the list model
            {
                listModel.addElement(course);
            }
        }
        
        JLabel selectedLabel = new JLabel("Selected track: None");

        // Selection listener for trackList
        trackList.addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent e){
                selectButton.setEnabled(true);
                deleteButton.setEnabled(true);
                setSelectedTrack(trackList.getSelectedValue());
                selectedLabel.setText("Selected track: None");
            }
        });

        // Action listener to the selectButton
        selectButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if (getSelectedTrack() != null) 
                {
                    setTypeOfDegreePlan(getSelectedTrack());
                    selectedLabel.setText("Selected track: " + getSelectedTrack());
                }
            }
        });

        // Action listener to the deleteButton
        deleteButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                String selectedTrack = trackList.getSelectedValue();
                listModel.removeElement(selectedTrack);
                
                if (trackCategory == 1)
                {
                    defaultCSTracks.remove(selectedTrack);
                }
                else 
                {
                    defaultSETracks.remove(selectedTrack);
                }
            }
        });

        // Action listener to the addButton
        addButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                String newCourse = newTrackField.getText();
                if (!newCourse.isEmpty()) 
                {
                    listModel.addElement(newCourse);
                    
                    if (trackCategory == 1) 
                    {
                        defaultCSTracks.add(newCourse);
                    } 
                    else 
                    {
                        defaultSETracks.add(newCourse);
                    }
                    
                    newTrackField.setText("");
                }
            }
        });

        // Create a scroll pane that contains the trackList
        JScrollPane scrollPane = new JScrollPane(trackList);
        scrollPane.setPreferredSize(new Dimension(300, 200)); // Set the size of the scroll pane to 300 x 200 pixels
        
        // Add the GUI components to the panel
        panel.setLayout(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(selectButton);
        buttonPanel.add(deleteButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Add the text field and add button to the GUI
        JPanel addPanel = new JPanel();
        addPanel.add(newTrackField);
        addPanel.add(addButton);
        panel.add(addPanel, BorderLayout.NORTH);

        // Add the selectedLabel to the GUI
        panel.add(selectedLabel, BorderLayout.WEST);
   
        return panel; 
    }
    /**
     * Method creates a GUI panel for displaying and manipulating a list of leveling/pre-requisite courses
     * 
     * @return the GUI panel for displaying and manipulating a list of leveling/pre-requisite courses
     */
    public JPanel leveling_preReq_Courses(){
        JPanel panel = new JPanel(); // Create new JPanel
        
        DefaultListModel<String> listModel = new DefaultListModel<String>();  // Create a new DefaultListModel to store the items in the JList, myList
        JList<String> myList = new JList<String>(listModel); // Create a new JList and initialize it with the DefaultListModel
        myList.setPreferredSize(new Dimension(300, 200)); // Set the size of the JList to 300 x 200 pixels
        
        DefaultListModel<String> defaultListModel = new DefaultListModel<String>();  // Create a new DefaultListModel to store the items in the JList, chosenList
        JList<String> chosenList = new JList<String>(defaultListModel); // Create a new JList and initialize it with the DefaultListModel
        chosenList.setPreferredSize(new Dimension(300, 200)); // Set the size of the JList to 300 x 200 pixels
        
        JTextField newCourseField = new JTextField(30); // Create a new JTextField for entering new leveling/pre-requisite courses to be added to the list.
                                                       // Text field will be able to display up to 30 characters of text at a time
        
        // Create necessary buttons 
        JButton selectButton = new JButton("Select");
        JButton deleteButton = new JButton("Delete");
        JButton addButton = new JButton("Add");

        for (String course : defaultLeveling) // Add the leveling/pre-requisites to the list model
        {
            listModel.addElement(course);
        }
        
        // Selection listener for myList
        myList.addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent e){
                selectButton.setEnabled(true);
                deleteButton.setEnabled(true);
                selectedLeveling_Prereq = myList.getSelectedValue();
            }
        });

        // Action listener to the selectButton
        selectButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if (selectedLeveling_Prereq != null)
                {
                    if (!(chosenLeveling_Prereq.contains(selectedLeveling_Prereq))) 
                    {
                        chosenLeveling_Prereq.add(selectedLeveling_Prereq);
                        defaultListModel.addElement(selectedLeveling_Prereq);
                    }
                }
            }
        });
        
        // Action listener to the deleteButton
        deleteButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                String selectedCourse = myList.getSelectedValue();
                listModel.removeElement(selectedCourse);
                defaultLeveling.remove(selectedCourse);
            }
        });

        // Action listener to the addButton
        addButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                String newCourse = newCourseField.getText();
                if (!newCourse.isEmpty()) 
                {
                    listModel.addElement(newCourse);
                    defaultLeveling.add(newCourse);
                    newCourseField.setText("");
                }
            }
        });

        // Create a scroll pane that contains the myList
        JScrollPane scrollPane1 = new JScrollPane(myList);
        scrollPane1.setPreferredSize(new Dimension(300, 200)); // Set the size of the scroll pane to 300 x 200 pixels
        
        // Create a scroll pane that contains the myList
        JScrollPane scrollPane2 = new JScrollPane(chosenList);
        scrollPane2.setPreferredSize(new Dimension(300, 200)); // Set the size of the scroll pane to 300 x 200 pixels
        
        // Add the GUI components to the panel
        panel.setLayout(new BorderLayout());
        panel.add(scrollPane1, BorderLayout.CENTER);
        panel.add(scrollPane2, BorderLayout.WEST);
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(selectButton);
        buttonPanel.add(deleteButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Add the text field and add button to the GUI
        JPanel addPanel = new JPanel();
        addPanel.add(newCourseField);
        addPanel.add(addButton);
        panel.add(addPanel, BorderLayout.NORTH);
        
        return panel; 
    }
    
    /**
     * Method creates one frame and within that frame it creates multiple tables. 
     * 
     */
    public void createFrameAndTables(){
        JFrame frame = new JFrame("Pre-view of Degree Plan"); // Creates a new JFrame. This will act as the sole frame to display the 
                                                              // pre-view of the needed Degree Plan  
        int intFlag; // Variable is a integer flag used to distinguish between groups of degree plan types  
        int tableFlag; // Variable is a integer flag to differentiate between the different tables created

        JPanel mainPanel = new JPanel(new BorderLayout());  // Create a new JPanel to act as my main panel in the frame
        JPanel tableContainer = new JPanel(new GridLayout(0, 1)); // Creates a new JPanel with a GridLayout of one column and any number of rows.
                                                                  // This is needed to display all the tables appropriately 
        
        // Check the type of degree plan 
        if (getTypeOfDegreePlan().equals(DATA_SCIENCE) || getTypeOfDegreePlan().equals(INTELLIGENT_SYSTEMS) || getTypeOfDegreePlan().equals(INTERACTIVE_COMPUTING)
                || getTypeOfDegreePlan().equals(SYSTEMS) || getTypeOfDegreePlan().equals(TRADITIONAL_COMPUTER_SCIENCE)) 
        {
            final int NUM_OF_TABLES = 6;
            JPanel[] tablePanels = new JPanel[NUM_OF_TABLES]; // Array used to hold 6 table panels 
            intFlag = 1;
            tableFlag = 0;


            // Create table panels and add them to the array
            for (int ix = 0; ix < tablePanels.length; ix++)
            {
                tablePanels[ix] = createTablePanel(NUM_OF_TABLES, tableFlag, intFlag, ix); // Method call 
                tableFlag++;
            }
            
            // Add the table panels to the table container
            for (int ix = 0; ix < tablePanels.length; ix++) 
            {
                tableContainer.add(tablePanels[ix]);
            }

            // Add the table container to the main panel
            mainPanel.add(tableContainer, BorderLayout.CENTER);
        } 
        else if (getTypeOfDegreePlan().equals(CYBER_SECURITY)) // Check the type of degree plan 
        {
            final int NUM_OF_TABLES = 6;
            JPanel[] tablePanels = new JPanel[NUM_OF_TABLES]; // Array used to hold 6 table panels
            intFlag = 2;
            tableFlag = 0; 

            // Create table panels and add them to the array
            for (int ix = 0; ix < tablePanels.length; ix++)
            {
                tablePanels[ix] = createTablePanel(NUM_OF_TABLES, tableFlag, intFlag, ix); // Method call
                tableFlag++;
            }

            // Add the table panels to the table container
            for (int ix = 0; ix < tablePanels.length; ix++)
            {
                tableContainer.add(tablePanels[ix]);
            }

            // Add the table container to the main panel
            mainPanel.add(tableContainer, BorderLayout.CENTER);
        } 
        else if (getTypeOfDegreePlan().equals(NETWORKS_AND_TELECOMMUNICATIONS) || getTypeOfDegreePlan().equals(SOFTWARE_ENGINEERING)) // Check the type of degree plan 
        {
            final int NUM_OF_TABLES = 5;
            JPanel[] tablePanels = new JPanel[NUM_OF_TABLES]; // Array used to hold 5 table panels
            intFlag = 3;
            tableFlag = 0; 

            // Create table panels and add them to the array
            for (int ix = 0; ix < tablePanels.length; ix++)
            {
                tablePanels[ix] = createTablePanel(NUM_OF_TABLES, tableFlag, intFlag, ix); // Method call
                tableFlag++;
            }

            // Add the table panels to the table container
            for (int ix = 0; ix < tablePanels.length; ix++)
            {
                tableContainer.add(tablePanels[ix]);
            }
            // Add the table container to the main panel
            mainPanel.add(tableContainer, BorderLayout.CENTER);
        }
        else // Create generic table for degree plans not given during the time of @rkg190000 coding this Class 
        {
            final int NUM_OF_TABLES = 6;
            JPanel[] tablePanels = new JPanel[NUM_OF_TABLES]; // Array used to hold 6 table panels
            intFlag = 4;
            tableFlag = 0; 

            // Create table panels and add them to the array
            for (int ix = 0; ix < tablePanels.length; ix++)
            {
                tablePanels[ix] = createTablePanel(NUM_OF_TABLES, tableFlag, intFlag, ix); // Method call
                tableFlag++;
            }

            // Add the table panels to the table container
            for (int ix = 0; ix < tablePanels.length; ix++)
            {
                tableContainer.add(tablePanels[ix]);
            }

            // Add the table container to the main panel
            mainPanel.add(tableContainer, BorderLayout.CENTER);
        }
        
        frame.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e){
                latch.countDown(); // Remove latch
            }
        });
        
        // Create a new JScrollPane and add the mainPanel (which contains the tableContainer) to it
        JScrollPane scrollPane = new JScrollPane(mainPanel); // The new JScrollPane object is used to make the frame scrollable using scroll bar 
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); // Vertical scroll bar policy is set to always show the scroll bar

        frame.getContentPane().add(scrollPane); // Add the scroll pane to the content pane of the JFrame
        frame.pack(); // Pack the frame, which sets its size to the preferred size of its components
        frame.setVisible(true); // Make the frame visible 
    }
    
    /**
     * Method creates the table panel needed. 
     * Method is called multiple times by the createFrameAndTables() method to create the necessary amount of table panels. Each call is one table panel. 
     * 
     * @param numOfTables a integer flag used to pass the number of the tables created for the degree plan
     * @param tableFlag a integer flag to differentiate between the different tables created
     * @param intFlag a integer flag used to distinguish between groups of degree plan types 
     * @param degreePlanSection a integer flag used to distinguish the different sections of the degree plans. Variable correlates to the table as well
     * @return the table panel
     */
    public JPanel createTablePanel(int numOfTables, int tableFlag, int intFlag, int degreePlanSection){
        JPanel panel = new JPanel(new BorderLayout()); // Create new JPanel, will house the label(s) and table
        JPanel labelPanel = new JPanel(new BorderLayout()); // Create new JPanel, will house the label(s) needed 
        
        labelsForTables(intFlag, degreePlanSection, labelPanel); // Method call 

        // Create instance of TableModel. TableModel is used to define the structure and data of a table
        TableModel model = new TableModel(numOfTables, tableFlag, typeOfDegreePlan, defaultCoursesMap, coursesList); 

        JTable table = new JTable(model); // Create new JTable and pass instance of TableModel. JTable sets up the table to display the data in the desired format 
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        
        // Create a new JScrollPane and add the table (which contains the data fields) to it
        JScrollPane scrollPane = new JScrollPane(table);// The new JScrollPane object is used to make the table scrollable using scroll bar.
                                                        // However, the scroll bar will only appear when the contents of the table are so many 
                                                        // that some become unviewable through the view port 
                                                        
        table.setPreferredScrollableViewportSize(new Dimension(400, 200)); // Sets view port size 
        
        // Wrap the scroll pane (which contains the JTable) in a Box layout
        Box tableBox = Box.createHorizontalBox();
        tableBox.add(scrollPane); 

        JPanel buttonPanel = new JPanel(); // New JPanel is created for buttons 
        
        JButton addButton = new JButton("Add Row");
        addButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                model.addRow();
                model.fireTableDataChanged(); // This method comes from AbstractTableModel Class and will trigger the TableModelListener's tableChanged() method, 
                                              // which will in turn update the JTable's view with the new data.
            }
        });
        
        JButton deleteButton = new JButton("Delete Row");
        deleteButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) 
                {
                    model.deleteRow(selectedRow);
                    model.fireTableDataChanged(); // This method comes from AbstractTableModel Class and will trigger the TableModelListener's tableChanged()
                                                  // method, which will in turn update the JTable's view with the new data.
                }
            }
        });
        
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);

        // Wrap the button panel in a Box layout
        Box buttonBox = Box.createHorizontalBox();
        buttonBox.add(buttonPanel);

        // Wrap the table and button Box layouts in a vertical Box layout. Components will also be properly spaced and aligned vertically
        Box mainBox = Box.createVerticalBox();
        mainBox.add(Box.createVerticalGlue());
        mainBox.add(tableBox);
        mainBox.add(buttonBox);
        mainBox.add(Box.createVerticalGlue());

        // Add the mainBox layout to the panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(mainBox, BorderLayout.CENTER);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Adds padding 
        panel.add(labelPanel, BorderLayout.NORTH);
        panel.add(tablePanel, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Method is used to create the appropriate labels for each table that is created. 
     * Label is displayed above respective table. 
     * 
     * @param intFlag a integer flag used to distinguish between groups of degree plan types 
     * @param degreePlanSection a integer flag used to distinguish the different sections of the degree plans. Variable correlates to the table as well
     * @param labelPanel the JPanel used to display the labels above there respective table within the mainPanel 
     */
    public void labelsForTables(int intFlag, int degreePlanSection, JPanel labelPanel){
        JLabel label = new JLabel(); // Create JLabel object.  
        
        if (intFlag == 1) // Via a integer flag, a check is done to see if the degree plan is one of the following: 
                          // Data Science, Intelligent Systems, Interactive Computing, Systems, or Traditional Computer Science
        {
            if (degreePlanSection == 0) // Check if the section/table needing a label is the first one 
            {
                label = new JLabel(CORE_COURSES_LABEL); // Create label 
                labelPanel.add(label, BorderLayout.NORTH); // Add the label to the label panel 
                labelPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Adds padding 
            } 
            else if (degreePlanSection == 1) // Check if the section/table needing a label is the second one
            {
                // Different labels are needed for this section/table dependent on the type of degree plan 
                if (getTypeOfDegreePlan().equals(DATA_SCIENCE) || getTypeOfDegreePlan().equals(INTELLIGENT_SYSTEMS) || getTypeOfDegreePlan().equals(SYSTEMS))
                {
                    label = new JLabel("One of the Following Courses");
                } 
                else if (getTypeOfDegreePlan().equals(INTERACTIVE_COMPUTING))
                {
                    label = new JLabel("Three of the Following Courses");
                }
                else if (getTypeOfDegreePlan().equals(TRADITIONAL_COMPUTER_SCIENCE)) 
                {
                    label = new JLabel("Two of the Following Courses");
                }

                labelPanel.add(label, BorderLayout.NORTH);
                labelPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Adds padding 
            } 
            else if (degreePlanSection == 2) // Check if the section/table needing a label is the third one
            {
                label = new JLabel(APPROVED_6000_LEVEL_ELECTIVES_LABEL);
                labelPanel.add(label, BorderLayout.NORTH);
                labelPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Adds padding 
            } 
            else if (degreePlanSection == 3) // Check if the section/table needing a label is the fourth one
            { 
                label = new JLabel(ADDITIONAL_ELECTIVES_LABEL);
                labelPanel.add(label, BorderLayout.NORTH);
                labelPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Adds padding
            } 
            else if (degreePlanSection == 4) // Check if the section/table needing a label is the fifth one
            {
                label = new JLabel(OTHER_REQUIREMENTS);
                labelPanel.add(label, BorderLayout.NORTH);
                labelPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Adds padding
            }
        } 
        else if (intFlag == 2) // Via a integer flag, a check is done to see if the degree plan is one of the following: 
                               // Cyber Security 
        {
            if (degreePlanSection == 0) // Check if the section/table needing a label is the first one
            {
                label = new JLabel(CORE_COURSES_LABEL);
                labelPanel.add(label, BorderLayout.NORTH);
                labelPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Adds padding 
            }
            else if (degreePlanSection == 1) // Check if the section/table needing a label is the second one
            {
                label = new JLabel("Two of the Following Courses");
                labelPanel.add(label, BorderLayout.NORTH);
                labelPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Adds padding
            } 
            else if (degreePlanSection == 2) // Check if the section/table needing a label is the third one
            {
                label = new JLabel("TWO IA* APPROVED 6000 LEVEL ELECTIVES   (6 Credit Hours)   3.0 Grade Point Average");
                labelPanel.add(label, BorderLayout.NORTH);
                labelPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Adds padding
            }
            else if (degreePlanSection == 3) // Check if the section/table needing a label is the fourth one
            {
                label = new JLabel("CS APPROVED 6000 LEVEL ELECTIVES   (12 Credit Hours)   3.0 Grade Point Average");
                labelPanel.add(label, BorderLayout.NORTH);
                labelPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Adds padding
            }
            else if (degreePlanSection == 4) // Check if the section/table needing a label is the fifth one
            {
                label = new JLabel(OTHER_REQUIREMENTS);
                labelPanel.add(label, BorderLayout.NORTH);
                labelPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Adds padding
            } 
            else if (degreePlanSection == 5) // Check if the section/table needing a label is the sixth one
            {
                label = new JLabel("No 5XXX courses can be applied to this degree plan");
                labelPanel.add(label, BorderLayout.NORTH);
                labelPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Adds padding
            }
        }
        else if (intFlag == 3) // Via a integer flag, a check is done to see if the degree plan is one of the following: 
                               // Networks and Telecommunications, or Software Engineering
        {
            if (degreePlanSection == 0) // Check if the section/table needing a label is the first one
            {
                label = new JLabel(CORE_COURSES_LABEL);
                labelPanel.add(label, BorderLayout.NORTH);
                labelPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Adds padding

            } 
            else if (degreePlanSection == 1) // Check if the section/table needing a label is the second one
            {
                label = new JLabel(APPROVED_6000_LEVEL_ELECTIVES_LABEL);
                labelPanel.add(label, BorderLayout.NORTH);
                labelPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Adds padding
                
                if (getTypeOfDegreePlan().equals(SOFTWARE_ENGINEERING)) // An extra label is added if condition is true
                {
                    label = new JLabel("CS 6359 cannot be used on this degree plan");
                    labelPanel.add(label, BorderLayout.CENTER);
                }
            } 
            else if (degreePlanSection == 2) // Check if the section/table needing a label is the third one
            {
                label = new JLabel(ADDITIONAL_ELECTIVES_LABEL);
                labelPanel.add(label, BorderLayout.NORTH);
                labelPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Adds padding
            } 
            else if (degreePlanSection == 3) // Check if the section/table needing a label is the fourth one
            {
                label = new JLabel(OTHER_REQUIREMENTS);
                labelPanel.add(label, BorderLayout.NORTH);
                labelPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Adds padding
            }
        }
        else if (intFlag == 4) // Via a integer flag, a check is done to see if the degree plan is one that was not present when 
                               // @rkg190000 coded this Class
        {
            if (degreePlanSection == 0) // Check if the section/table needing a label is the first one
            {
                label = new JLabel("CORE COURSES");
                labelPanel.add(label, BorderLayout.NORTH);
                labelPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Adds padding 
            }
            else if (degreePlanSection == 1) // Check if the section/table needing a label is the second one
            {
                label = new JLabel("X Number of the Following Courses");
                labelPanel.add(label, BorderLayout.NORTH);
                labelPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Adds padding
            } 
            else if (degreePlanSection == 2) // Check if the section/table needing a label is the third one
            {
                label = new JLabel("APPROVED X000 LEVEL ELECTIVES");
                labelPanel.add(label, BorderLayout.NORTH);
                labelPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Adds padding
            }
            else if (degreePlanSection == 3) // Check if the section/table needing a label is the fourth one
            {
                label = new JLabel("ADDITIONAL ELECTIVES");
                labelPanel.add(label, BorderLayout.NORTH);
                labelPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Adds padding
            }
            else if (degreePlanSection == 4) // Check if the section/table needing a label is the fifth one
            {
                label = new JLabel(OTHER_REQUIREMENTS);
                labelPanel.add(label, BorderLayout.NORTH);
                labelPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Adds padding
            } 
        }
    }

    /**
     * @return the typeOfDegreePlan
     */
    public String getTypeOfDegreePlan(){
        return typeOfDegreePlan;
    }

    /**
     * @param typeOfDegreePlan the typeOfDegreePlan to set
     */
    public void setTypeOfDegreePlan(String typeOfDegreePlan){
        this.typeOfDegreePlan = typeOfDegreePlan;
    }

    /**
     * @return the fastTrackORThesis
     */
    public String getFastTrackORThesis(){
        return fastTrackORThesis;
    }

    /**
     * @param fastTrackORThesis the fastTrackORThesis to set
     */
    public void setFastTrackORThesis(String fastTrackORThesis){
        this.fastTrackORThesis = fastTrackORThesis;
    }

    /**
     * @return the selectedTrack
     */
    public String getSelectedTrack(){
        return selectedTrack;
    }

    /**
     * @param selectedTrack the selectedTrack to set
     */
    public void setSelectedTrack(String selectedTrack){
        this.selectedTrack = selectedTrack;
    }
    
    /**
     * @return the latch
     */
    public CountDownLatch getLatch(){
        return latch;
    }

    /**
     * @param latch the latch to set
     */
    public void setLatch(CountDownLatch latch){
        this.latch = latch;
    }
}
