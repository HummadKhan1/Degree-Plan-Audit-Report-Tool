
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.table.AbstractTableModel;

public class TableModel extends AbstractTableModel {

    private String degreePlan; // String contains the type of degree plans to create its default data tables
    private ArrayList<Course> courses; // ArrayList contains all the courses read-in from the transcript after some filtering
    private HashMap<String, ArrayList<Course>> defaultCourses; //Hashmap contains all the default course list for every degree plan
    private int numTables; // Integer flag used to pass the number of the tables created for the degree plan
    private int tableFlag; // Integer flag to differentiate between the different tables created
    private String[] columnNames = {"Course Title", "Course Number", "UTD Semester", "Transfer", "Grade"};
    private int dataRow = 0;
    ArrayList<ArrayList<String>> outerList; // ArrayList of ArrayList of String contains the full list of the default course of the selected degree plan
    ArrayList<String> innerList; // ArrayList of String used to contain the column values for each table
    ArrayList<ArrayList<String>> tempTable; // ArrayList of ArrayList of String used to create the different tables for the degree plans
    ArrayList<ArrayList<ArrayList<String>>> dataList = new ArrayList<ArrayList<ArrayList<String>>>(); // ArrayList of ArrayList of Arraylist of String contains
    // all the default courses in a table data format

    /**
     * Constructor.
     *
     * @param numOfTables
     * @param tableFlag
     * @param degreePlan
     * @param defaultCourses
     * @param courses
     */
    public TableModel(int numOfTables, int tableFlag, String degreePlan, HashMap<String, ArrayList<Course>> defaultCourses, ArrayList<Course> courses){
        this.degreePlan = degreePlan;
        this.defaultCourses = defaultCourses;
        this.courses = courses;
        this.tableFlag = tableFlag;
        this.numTables = numOfTables;

        addCoursesToData();
        splitData();
        printData();

    }

    /**
     * Method transfers the required degree plan's default courses to an
     * ArrayList of ArrayList of Strings
     *
     */
    public void addCoursesToData() {
        //Searches for the selected degree plan
        for (String key : defaultCourses.keySet()) {
            if (key.equals(degreePlan)) {
                outerList = new ArrayList<ArrayList<String>>();
                // Created ArrayList of the default courses from selected key
                ArrayList<Course> defaultCourseList = defaultCourses.get(key);
                // Iterates through the default course list
                for (Course course : defaultCourseList) {
                    // Creates an ArrayList of Strings to store each course values
                    innerList = new ArrayList<String>();
                    for (int col = 0; col < columnNames.length; col++) {
                        if (col == 0) {
                            innerList.add(course.getClassName());
                        } else if (col == 1) {
                            innerList.add(course.getDepartment() + " " + course.getCourseNumber());
                        } else {
                            innerList.add(null);
                        }
                    }
                    // Adds the ArrayList of Strings to an ArrayList of ArrayList of Strings
                    outerList.add(innerList);
                }
            }

        }
    }

    /**
     * Method splits the ArrayList of ArrayList of Strings created from the
     * transfered data of the hash map into tables and stores that table into an
     * ArrayList of ArrayList of ArrayList of Strings
     *
     */
    public void splitData() {
        // Loops through to create tables
        for (int i = 0; i < numTables; i++) {
            // Pollulates data for table 1
            if (i == 0) {
                // Check the type of degree plan 
                if (degreePlan.equals("Intelligent Systems")) {
                    tempTable = new ArrayList<ArrayList<String>>();
                    for (int row = 0; row < 4; row++) {
                        tempTable.add(outerList.get(dataRow));
                        dataRow++;
                    }
                    dataList.add(tempTable);
                } else if (degreePlan.equals("Interactive Computing")) {
                    tempTable = new ArrayList<ArrayList<String>>();
                    for (int row = 0; row < 2; row++) {
                        tempTable.add(outerList.get(dataRow));
                        dataRow++;
                    }
                    dataList.add(tempTable);
                } else if (degreePlan.equals("Traditional Computer Science")) {
                    tempTable = new ArrayList<ArrayList<String>>();
                    for (int row = 0; row < 3; row++) {
                        tempTable.add(outerList.get(dataRow));
                        dataRow++;
                    }
                    dataList.add(tempTable);
                } else if (degreePlan.equals("Data Science")) {
                    tempTable = new ArrayList<ArrayList<String>>();
                    for (int row = 0; row < 4; row++) {
                        tempTable.add(outerList.get(dataRow));
                        dataRow++;
                    }
                    dataList.add(tempTable);
                } else if (degreePlan.equals("Systems")) {
                    tempTable = new ArrayList<ArrayList<String>>();
                    for (int row = 0; row < 4; row++) {
                        tempTable.add(outerList.get(dataRow));
                        dataRow++;
                    }
                    dataList.add(tempTable);
                } else if (degreePlan.equals("Cyber Security")) {
                    tempTable = new ArrayList<ArrayList<String>>();
                    for (int row = 0; row < 3; row++) {
                        tempTable.add(outerList.get(dataRow));
                        dataRow++;
                    }
                    dataList.add(tempTable);
                } else if (degreePlan.equals("Networks and Telecommunications")) {
                    tempTable = new ArrayList<ArrayList<String>>();
                    for (int row = 0; row < 5; row++) {
                        tempTable.add(outerList.get(dataRow));
                        dataRow++;
                    }
                    dataList.add(tempTable);
                } else if (degreePlan.equals("Software Engineering")) {
                    tempTable = new ArrayList<ArrayList<String>>();
                    for (int row = 0; row < 5; row++) {
                        tempTable.add(outerList.get(dataRow));
                        dataRow++;
                    }
                    dataList.add(tempTable);
                }
            } // Pollulates the data for table 2
            else if (i == 1) {
                // Check the type of degree plan 
                if (degreePlan.equals("Intelligent Systems")) {
                    dataRow = 4;
                    tempTable = new ArrayList<ArrayList<String>>();
                    for (int row = 0; row < 2; row++) {
                        tempTable.add(outerList.get(dataRow));
                        dataRow++;
                    }
                    dataList.add(tempTable);
                } else if (degreePlan.equals("Interactive Computing")) {
                    dataRow = 2;
                    tempTable = new ArrayList<ArrayList<String>>();
                    for (int row = 0; row < 5; row++) {
                        tempTable.add(outerList.get(dataRow));
                        dataRow++;
                    }
                    dataList.add(tempTable);
                } else if (degreePlan.equals("Traditional Computer Science")) {
                    dataRow = 3;
                    tempTable = new ArrayList<ArrayList<String>>();
                    for (int row = 0; row < 3; row++) {
                        tempTable.add(outerList.get(dataRow));
                        dataRow++;
                    }
                    dataList.add(tempTable);
                } else if (degreePlan.equals("Data Science")) {
                    dataRow = 4;
                    tempTable = new ArrayList<ArrayList<String>>();
                    for (int row = 0; row < 5; row++) {
                        tempTable.add(outerList.get(dataRow));
                        dataRow++;
                    }
                    dataList.add(tempTable);
                } else if (degreePlan.equals("Systems")) {
                    dataRow = 4;
                    tempTable = new ArrayList<ArrayList<String>>();
                    for (int row = 0; row < 5; row++) {
                        tempTable.add(outerList.get(dataRow));
                        dataRow++;
                    }
                    dataList.add(tempTable);
                } else if (degreePlan.equals("Cyber Security")) {
                    dataRow = 3;
                    tempTable = new ArrayList<ArrayList<String>>();
                    for (int row = 0; row < 4; row++) {
                        tempTable.add(outerList.get(dataRow));
                        dataRow++;
                    }
                    dataList.add(tempTable);
                }
            } // Pollulates the data for table 6
            else if (i == 5) {
                // Check the type of degree plan 
                if (degreePlan.equals("Intelligent Systems")) {
                    dataRow = 6;
                    tempTable = new ArrayList<ArrayList<String>>();
                    for (int row = 0; row < 5; row++) {
                        tempTable.add(outerList.get(dataRow));
                        dataRow++;
                    }
                    dataList.add(tempTable);
                } else if (degreePlan.equals("Interactive Computing")) {
                    dataRow = 7;
                    tempTable = new ArrayList<ArrayList<String>>();
                    for (int row = 0; row < 5; row++) {
                        tempTable.add(outerList.get(dataRow));
                        dataRow++;
                    }
                    dataList.add(tempTable);
                } else if (degreePlan.equals("Traditional Computer Science")) {
                    dataRow = 6;
                    tempTable = new ArrayList<ArrayList<String>>();
                    for (int row = 0; row < 7; row++) {
                        tempTable.add(outerList.get(dataRow));
                        dataRow++;
                    }
                    dataList.add(tempTable);
                } else if (degreePlan.equals("Data Science")) {
                    dataRow = 9;
                    tempTable = new ArrayList<ArrayList<String>>();
                    for (int row = 0; row < 6; row++) {
                        tempTable.add(outerList.get(dataRow));
                        dataRow++;
                    }
                    dataList.add(tempTable);
                } else if (degreePlan.equals("Systems")) {
                    dataRow = 9;
                    tempTable = new ArrayList<ArrayList<String>>();
                    for (int row = 0; row < 6; row++) {
                        tempTable.add(outerList.get(dataRow));
                        dataRow++;
                    }
                    dataList.add(tempTable);
                } else if (degreePlan.equals("Cyber Security")) {
                    dataRow = 7;
                    tempTable = new ArrayList<ArrayList<String>>();
                    for (int row = 0; row < 6; row++) {
                        tempTable.add(outerList.get(dataRow));
                        dataRow++;
                    }
                    dataList.add(tempTable);
                } else if (degreePlan.equals("Networks and Telecommunications")) {
                    dataRow = 5;
                    tempTable = new ArrayList<ArrayList<String>>();
                    for (int row = 0; row < 7; row++) {
                        tempTable.add(outerList.get(dataRow));
                        dataRow++;
                    }
                    dataList.add(tempTable);
                } else if (degreePlan.equals("Software Engineering")) {
                    dataRow = 5;
                    tempTable = new ArrayList<ArrayList<String>>();
                    for (int row = 0; row < 6; row++) {
                        tempTable.add(outerList.get(dataRow));
                        dataRow++;
                    }
                    dataList.add(tempTable);
                }
            } // Pollulates the data for the other tables
            else {
                tempTable = new ArrayList<ArrayList<String>>();
                innerList = new ArrayList<String>();
                for (int col = 0; col < columnNames.length; col++) {
                    innerList.add(null);
                }
                tempTable.add(innerList);
                dataList.add(tempTable);
            }
        }

    }

    @Override
    public int getRowCount() {
        if (tableFlag == 0) {
            return dataList.get(0).size();
        } else if (tableFlag == 1) {
            return dataList.get(1).size();
        } else if (tableFlag == 2) {
            return dataList.get(2).size();
        } else if (tableFlag == 3) {
            return dataList.get(3).size();
        } else if (tableFlag == 4) {
            return dataList.get(4).size();
        } else if (tableFlag == 5) {
            return dataList.get(5).size();
        } else {
            return -1;
        }
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int row, int column) {
        if (tableFlag == 0) {
            return dataList.get(0).get(row).get(column);
        } else if (tableFlag == 1) {
            return dataList.get(1).get(row).get(column);
        } else if (tableFlag == 2) {
            return dataList.get(2).get(row).get(column);
        } else if (tableFlag == 3) {
            return dataList.get(3).get(row).get(column);
        } else if (tableFlag == 4) {
            return dataList.get(4).get(row).get(column);
        } else if (tableFlag == 5) {
            return dataList.get(5).get(row).get(column);
        } else {
            return -1;
        }
    }

    @Override
    public void setValueAt(Object value, int row, int column) {
        if (tableFlag == 0) {
            dataList.get(0).get(row).set(column, (String) value);
            fireTableCellUpdated(row, column);
        } else if (tableFlag == 1) {
            dataList.get(1).get(row).set(column, (String) value);;
            fireTableCellUpdated(row, column);
        } else if (tableFlag == 2) {
            dataList.get(2).get(row).set(column, (String) value);
            fireTableCellUpdated(row, column);
        } else if (tableFlag == 3) {
            dataList.get(3).get(row).set(column, (String) value);
            fireTableCellUpdated(row, column);
        } else if (tableFlag == 4) {
            dataList.get(4).get(row).set(column, (String) value);
            fireTableCellUpdated(row, column);
        } else if (tableFlag == 5) {
            dataList.get(5).get(row).set(column, (String) value);
            fireTableCellUpdated(row, column);
        }
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return true;
    }

    public void addRow() {
        innerList = new ArrayList<String>();
        for (int col = 0; col < columnNames.length; col++) {
            innerList.add(null);
        }

        if (tableFlag == 0) {
            dataList.get(0).add(innerList);
        } else if (tableFlag == 1) {
            dataList.get(1).add(innerList);
        } else if (tableFlag == 2) {
            dataList.get(2).add(innerList);
        } else if (tableFlag == 3) {
            dataList.get(3).add(innerList);
        } else if (tableFlag == 4) {
            dataList.get(4).add(innerList);
        } else if (tableFlag == 5) {
            dataList.get(5).add(innerList);
        }

        fireTableDataChanged();

        printData();
    }

    public void deleteRow(int row) {
        if (tableFlag == 0) {
            dataList.get(0).remove(row);
        } else if (tableFlag == 1) {
            dataList.get(1).remove(row);
        } else if (tableFlag == 2) {
            dataList.get(2).remove(row);
        } else if (tableFlag == 3) {
            dataList.get(3).remove(row);
        } else if (tableFlag == 4) {
            dataList.get(4).remove(row);
        } else if (tableFlag == 5) {
            dataList.get(5).remove(row);
        }

        printData();
    }

    public void printData() {
        for (int i = 0; i < dataList.size(); i++) {
            for (int j = 0; j < dataList.get(i).size(); j++) {
                System.out.print(dataList.get(i).get(j) + "\n");
            }
            System.out.println();
        }
//        for (int i = 0; i < outerList.size(); i++) {
//        	System.out.print(outerList.get(i) + "\n");
//        }
    }

}
