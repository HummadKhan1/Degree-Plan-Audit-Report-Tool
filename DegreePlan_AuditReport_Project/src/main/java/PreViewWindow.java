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
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JCheckBox;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

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
    private ArrayList<Course> convertedLeveling_Prereq = new ArrayList<>(); //ArrayList will store the leveling/pre-requisite courses chosen using Course objects  
    private ArrayList<Course> coursesArray = new ArrayList<>(); // coursesArray contains all the courses read-in from the transcript after some filtering
    private HashMap<String, ArrayList<Course>> defaultCoursesMap; // Hashmap stores the default courses found in the Default.txt file
                                                                  // with the key being the type of track the courses fall under 
    private ArrayList<ArrayList<ArrayList<String>>> finalDataList; // ArrayList stores data found in each row of each table of the pre-view degree plan 
    
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
        for (Course course : courses)
        {
            coursesArray.add(new Course(course));
        }
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
                handleChosenLeveling_Prereq();
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
     * Method is used to create check boxes that will store the users choice of either Fast Track or Thesis.
     * 
     * @return the check box panel  
     */
    public JPanel fastTrackORThesis(){
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Create new JPanel, will house the check boxes 
        
        // Create check boxes 
        JCheckBox checkBox1 = new JCheckBox("Fast Track");
        JCheckBox checkBox2 = new JCheckBox("Thesis");


        checkBox1.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                if (checkBox1.isSelected()) 
                {
                    setFastTrackORThesis(checkBox1.getText());
                    checkBox2.setSelected(false);
                }
                else 
                {
                    setFastTrackORThesis(null);
                }
            }
        });

        checkBox2.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                if (checkBox2.isSelected())
                {
                    setFastTrackORThesis(checkBox2.getText());
                    checkBox1.setSelected(false);
                } 
                else
                {
                    setFastTrackORThesis(null);
                }
            }
        });

        panel.add(checkBox1);
        panel.add(checkBox2);
        
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
        
        DefaultListModel<String> listModel = new DefaultListModel<>();  // Create a new DefaultListModel to store the items in the JList
        JList<String> trackList = new JList<>(listModel); // Create a new JList and initialize it with the DefaultListModel
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
            for (String course : getDefaultCSTracks()) // Add the CS tracks to the list model
            {
                listModel.addElement(course);
            }
        }
        else
        {
            for (String course : getDefaultSETracks()) // Add the SE tracks to the list model
            {
                listModel.addElement(course);
            }
        }
        
        JLabel selectedLabel = new JLabel("Selected track: None");

        // Selection listener for trackList
        trackList.addListSelectionListener(new ListSelectionListener(){
            @Override
            public void valueChanged(ListSelectionEvent e){
                selectButton.setEnabled(true);
                deleteButton.setEnabled(true);
                setSelectedTrack(trackList.getSelectedValue());
                selectedLabel.setText("Selected track: None");
            }
        });

        // Action listener to the selectButton
        selectButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                if (getSelectedTrack() != null) 
                {
                    setTypeOfDegreePlan(getSelectedTrack().trim());
                    selectedLabel.setText("Selected track: " + getSelectedTrack());
                }
            }
        });

        // Action listener to the deleteButton
        deleteButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                String selectedTrack = trackList.getSelectedValue();
                listModel.removeElement(selectedTrack);
                
                if (trackCategory == 1)
                {
                    getDefaultCSTracks().remove(selectedTrack);
                }
                else 
                {
                    getDefaultSETracks().remove(selectedTrack);
                }
            }
        });

        // Action listener to the addButton
        addButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                String newCourse = newTrackField.getText().trim();
                if (!newCourse.isEmpty() && !(defaultCSTracks.contains(newCourse)) && !(defaultSETracks.contains(newCourse))) 
                {
                    listModel.addElement(newCourse);
                    
                    if (trackCategory == 1) 
                    {
                        getDefaultCSTracks().add(newCourse);
                    } 
                    else 
                    {
                        getDefaultSETracks().add(newCourse);
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

        for (String course : getDefaultLeveling()) // Add the leveling/pre-requisites to the list model
        {
            listModel.addElement(course);
        }
        
        // Selection listener for myList
        myList.addListSelectionListener(new ListSelectionListener(){
            @Override
            public void valueChanged(ListSelectionEvent e){
                selectButton.setEnabled(true);
                deleteButton.setEnabled(true);
                selectedLeveling_Prereq = myList.getSelectedValue();
            }
        });

        // Action listener to the selectButton
        selectButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                if (getSelectedLeveling_Prereq() != null)
                {
                    if (!(chosenLeveling_Prereq.contains(selectedLeveling_Prereq))) 
                    {
                        getChosenLeveling_Prereq().add(getSelectedLeveling_Prereq().trim());
                        defaultListModel.addElement(getSelectedLeveling_Prereq().trim());
                    }
                }
            }
        });
        
        // Action listener to the deleteButton
        deleteButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                String selectedCourse = myList.getSelectedValue();
                listModel.removeElement(selectedCourse);
                getDefaultLeveling().remove(selectedCourse);
            }
        });

        // Action listener to the addButton
        addButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                String newCourse = newCourseField.getText().trim();
                if (!newCourse.isEmpty() && !(getDefaultLeveling().contains(newCourse))) 
                {
                    listModel.addElement(newCourse);
                    getDefaultLeveling().add(newCourse);
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
                getLatch().countDown(); // Remove latch
            }
        });
        
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
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
        TableModel model = new TableModel(numOfTables, tableFlag, getTypeOfDegreePlan(), getDefaultCoursesMap(), getCoursesArray(), getConvertedLeveling_Prereq()); 
        this.finalDataList = new ArrayList<ArrayList<ArrayList<String>>> (model.dataList);

        model.addTableModelListener(new TableModelListener(){
            @Override
            public void tableChanged(TableModelEvent e){
                model.dataList = new ArrayList<ArrayList<ArrayList<String>>> (getFinalDataList());
                finalDataList = new ArrayList<ArrayList<ArrayList<String>>> (model.dataList);
            }
        });

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
            @Override
            public void actionPerformed(ActionEvent e){
                model.addRow();
                model.updateNewRow(tableFlag);
                model.fireTableDataChanged(); // This method comes from AbstractTableModel Class and will trigger the TableModelListener's tableChanged() method, 
                                              // which will in turn update the JTable's view with the new data.
            }
        });
        
        JButton deleteButton = new JButton("Delete Row");
        deleteButton.addActionListener(new ActionListener(){
            @Override
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
     * Method parses a list of chosen leveling/pre-requisite courses, matches each course against a regular expression pattern, and updates the
     * corresponding courses in the coursesArray with a ">->" in the name to that indicates they are leveling/pre-requisite courses.
     */
    public void handleChosenLeveling_Prereq(){
        // Regular expressions to match patterns in chosenLeveling_Prereq 
        Pattern coursePattern = Pattern.compile("([A-Za-z]+)\\s+(\\d{4})\\s+(.+)");
            
        for (int ix = 0; ix < getChosenLeveling_Prereq().size(); ix++)
        {
            String line = getChosenLeveling_Prereq().get(ix).trim(); // Get the current course as a string and trim whitespace at the ends

            // Regular expression matchers to extract relevant information from chosenLeveling_Prereq
            Matcher coursePatternMatcher = coursePattern.matcher(line);

            // If the pattern matches the course string, extract the department, course number, and class name
            if (coursePatternMatcher.matches())
            {
                String department = coursePatternMatcher.group(1);
                String courseNumber = coursePatternMatcher.group(2);
                String className = ">->" + coursePatternMatcher.group(3);

                Course course = new Course(department, courseNumber, className, "", "", "", "Admission Prerequisites");
                getConvertedLeveling_Prereq().add(course);
            }
        }

        // Hashmap keeps track of courses by their Course key which is a concatenated String of department + course number
        HashMap<String, Course> coursesArrayMap = new HashMap<>();  // Key: Course key, Value: Course 

        // Populate the HashMap with the courses from coursesArray
        for (Course course : getCoursesArray())
        {
            String key = course.getDepartment() + course.getCourseNumber();
            coursesArrayMap.put(key, course);
        }

        // Use an iterator to iterate over the convertedLeveling_Prereq list and modify the corresponding courses in coursesArrayMap
        Iterator<Course> iterator = getConvertedLeveling_Prereq().iterator();
        while (iterator.hasNext()) 
        {
            Course leveling_PrereqCourse = iterator.next();
            String key = leveling_PrereqCourse.getDepartment() + leveling_PrereqCourse.getCourseNumber();
            Course courseFromList = coursesArrayMap.get(key);

            if (courseFromList != null)
            {
                courseFromList.setClassName(">->" + courseFromList.getClassName());
                coursesArrayMap.put(key, courseFromList);
                iterator.remove();
            }
        }
        
        coursesArray = new ArrayList<>(coursesArrayMap.values()); // Update the coursesArray with the modified courses from coursesArrayMap    
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

    /**
     * @return the selectedLeveling_Prereq
     */
    public String getSelectedLeveling_Prereq(){
        return selectedLeveling_Prereq;
    }

    /**
     * @param selectedLeveling_Prereq the selectedLeveling_Prereq to set
     */
    public void setSelectedLeveling_Prereq(String selectedLeveling_Prereq){
            this.selectedLeveling_Prereq = selectedLeveling_Prereq.trim();
    }

    /**
     * @return the defaultCSTracks
     */
    public ArrayList<String> getDefaultCSTracks(){
        return defaultCSTracks;
    }

    /**
     * @return the defaultSETracks
     */
    public ArrayList<String> getDefaultSETracks(){
        return defaultSETracks;
    }

    /**
     * @return the defaultLeveling
     */
    public ArrayList<String> getDefaultLeveling(){
        return defaultLeveling;
    }

    /**
     * @return the chosenLeveling_Prereq
     */
    public ArrayList<String> getChosenLeveling_Prereq(){
        return chosenLeveling_Prereq;
    }

    /**
     * @return the convertedLeveling_Prereq
     */
    public ArrayList<Course> getConvertedLeveling_Prereq(){
        return convertedLeveling_Prereq;
    }

    /**
     * @return the coursesArray
     */
    public ArrayList<Course> getCoursesArray(){
        return coursesArray;
    }

    /**
     * @return the defaultCoursesMap
     */
    public HashMap<String, ArrayList<Course>> getDefaultCoursesMap(){
        return defaultCoursesMap;
    }
    
    public void print(){
        System.out.println("Courses:");
        System.out.println();
        
        for (Course course : getCoursesArray()) 
        {
            System.out.println(course.getDepartment() + " " + course.getCourseNumber() + ": " + course.getClassName());
            System.out.println("Attempted Points: " + course.getAttemptedCredits());
            System.out.println("Earned Points: " + course.getEarnedCredits());
            System.out.println("Letter Grade: " + course.getLetterGrade());
            System.out.println("Class Points: " + course.getPoints());
            System.out.println("Semester: " + course.getSemester());
            System.out.println("Transfer Type: " + course.getTransferType());
            System.out.println(course.getRepeatCourse());
            System.out.println();
        }
        
        System.out.println("convertedLeveling_Prereq:");
        System.out.println();
        for (Course course : getConvertedLeveling_Prereq()) 
        {
            System.out.println(course.getDepartment() + " " + course.getCourseNumber() + ": " + course.getClassName());
            System.out.println("Attempted Points: " + course.getAttemptedCredits());
            System.out.println("Earned Points: " + course.getEarnedCredits());
            System.out.println("Letter Grade: " + course.getLetterGrade());
            System.out.println("Class Points: " + course.getPoints());
            System.out.println("Semester: " + course.getSemester());
            System.out.println("Transfer Type: " + course.getTransferType());
            System.out.println(course.getRepeatCourse());
            System.out.println();
        }
    }

    /**
     * @return the finalDataList
     */
    public ArrayList<ArrayList<ArrayList<String>>> getFinalDataList(){
        return finalDataList;
    }
}