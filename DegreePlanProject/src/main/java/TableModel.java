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
    ArrayList<ArrayList<String>> defaultCourseAL; // ArrayList of ArrayList of String contains the full list of the default course of the selected degree plan
    ArrayList<ArrayList<String>> studentCourseAL = new ArrayList<ArrayList<String>>();
    ArrayList<ArrayList<String>> unchangedStudentCourseAL = new ArrayList<ArrayList<String>>();
    ArrayList<String> innerList; // ArrayList of String used to contain the column values for each table
    ArrayList<ArrayList<String>> tempTable; // ArrayList of ArrayList of String used to create the different tables for the degree plans
    ArrayList<ArrayList<ArrayList<String>>> dataList = new ArrayList<ArrayList<ArrayList<String>>>(); // ArrayList of ArrayList of Arraylist of String contains
                                                                                                      // all the default courses in a table data format
    private ArrayList<Course> convertedLeveling_Prereq = new ArrayList<>(); //ArrayList will store the leveling/pre-requisite courses chosen using Course objects  

    /**
     * Constructor.
     *
     * @param numOfTables
     * @param tableFlag
     * @param degreePlan
     * @param defaultCourses
     * @param courses
     */
    public TableModel(int numOfTables, int tableFlag, String degreePlan, HashMap<String, ArrayList<Course>> defaultCourses, ArrayList<Course> courses, 
            ArrayList<Course> convertedLeveling_Prereq){
        this.degreePlan = degreePlan;
        this.defaultCourses = defaultCourses;
        this.courses = courses;
        this.tableFlag = tableFlag;
        this.numTables = numOfTables;
        this.convertedLeveling_Prereq = convertedLeveling_Prereq;
        
        addCoursesToData();
        addStudentCoursesToArrayList();
        splitData();
        popullateTableColumns();
        //printData();

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
                defaultCourseAL = new ArrayList<ArrayList<String>>();
                // Created ArrayList of the default courses from selected key
                ArrayList<Course> defaultCourseList = defaultCourses.get(key);
                // Iterates through the default course list
                for (Course course : defaultCourseList) {
                	String courseNumber = course.getDepartment() + " " + course.getCourseNumber();
                    // Creates an ArrayList of Strings to store each course values
                    innerList = new ArrayList<String>();
                    for (int col = 0; col < columnNames.length; col++) {
                        if (col == 0) {
                            innerList.add(course.getClassName());
                        } 
                        else if (col == 1) {
                            innerList.add(courseNumber);
                        }
                        else {
                            innerList.add("");
                        }
                    }
                    // Adds the ArrayList of Strings to an ArrayList of ArrayList of Strings
                    defaultCourseAL.add(innerList);
                }
            }

        }
    }
    
    public void addStudentCoursesToArrayList() {
    	for(Course course: courses) {
    		String courseNumber = course.getDepartment() + " " + course.getCourseNumber();
    		// Creates an ArrayList of Strings to store each course values
            innerList = new ArrayList<String>();
            for (int col = 0; col < columnNames.length; col++) {
            	switch(col) {
            		case 0:
            			innerList.add(course.getClassName());
            			break;
            		case 1:
            			innerList.add(courseNumber);
            			break;
            		case 2:
            			innerList.add(course.getSemester());
            			break;
            		case 3:
            			innerList.add(course.getTransferType());
            			break;
            		case 4:
            			innerList.add(course.getLetterGrade());
            			break;
            	}
            }
            studentCourseAL.add(innerList);
            unchangedStudentCourseAL.add(innerList);
    	}
    }
    
    public void updateNewRow(Integer tableFlag) {
    	if(tableFlag == 0 || tableFlag == 1 || tableFlag == 2 || tableFlag == 3 || tableFlag == 4 || tableFlag == 5) {
    		for(int i = 0; i < dataList.size(); i++) {
        		for(int j = 0; j < dataList.get(i).size(); j++) {
        			String col1 = dataList.get(i).get(j).get(0).toUpperCase();
        			String col2 = dataList.get(i).get(j).get(1).toUpperCase();
        			for(int k = 0; k < unchangedStudentCourseAL.size(); k++) {
        				String studentCol1 = unchangedStudentCourseAL.get(k).get(0);
        				String studentCol2 = unchangedStudentCourseAL.get(k).get(1);
        				
        				if(col1.equals(studentCol1) && col2.equals(studentCol2) || col2.equals(studentCol2)) {
        					dataList.get(i).get(j).set(0, unchangedStudentCourseAL.get(k).get(0));
        					dataList.get(i).get(j).set(2, unchangedStudentCourseAL.get(k).get(2));
        					dataList.get(i).get(j).set(3, unchangedStudentCourseAL.get(k).get(3));
        					dataList.get(i).get(j).set(4, unchangedStudentCourseAL.get(k).get(4));
        				}
        			}
        		}
        	}
    	}  	
    }

    public void popullateTableColumns() {
    	for(int i = 0; i < dataList.size(); i++) {
    		for(int j = 0; j < dataList.get(i).size(); j++) {
    			String col1 = dataList.get(i).get(j).get(0).toUpperCase();
    			String col2 = dataList.get(i).get(j).get(1).toUpperCase();
    			for(int k = 0; k < studentCourseAL.size(); k++) {
    				String studentCol1 = studentCourseAL.get(k).get(0);
    				String studentCol2 = studentCourseAL.get(k).get(1);
    				
    				if(col1.equals(studentCol1) && col2.equals(studentCol2) || col2.equals(studentCol2)) {
    					dataList.get(i).get(j).set(2, studentCourseAL.get(k).get(2));
    					dataList.get(i).get(j).set(3, studentCourseAL.get(k).get(3));
    					dataList.get(i).get(j).set(4, studentCourseAL.get(k).get(4));
    					studentCourseAL.remove(k);
    				}
    			}
    		}
    	}
    	
    	if (degreePlan.equals("Intelligent Systems")) {
    		Integer count = 0;
    		tempTable = new ArrayList<ArrayList<String>>();
    		
    		while(!studentCourseAL.isEmpty() && count < 5) {
    			for(int j = 0; j < studentCourseAL.size(); j++) {
        			String number = studentCourseAL.get(j).get(1).substring(3);
        			Integer courseNum = Integer.parseInt(number);
        			if(courseNum > 6000) {
        				studentCourseAL.get(j).add("6000 Electives");
        				tempTable.add(studentCourseAL.get(j));
        				studentCourseAL.remove(studentCourseAL.get(j));
        				count++;
        			}
        		}
    		}
    		dataList.set(2, tempTable);
    		
    		tempTable = new ArrayList<ArrayList<String>>();
    		
    		while(!studentCourseAL.isEmpty()) {
    			for(int j = 0; j < studentCourseAL.size(); j++) {
        			String number = studentCourseAL.get(j).get(1).substring(3);
        			Integer courseNum = Integer.parseInt(number);
        			if(courseNum > 5000) {
        				studentCourseAL.get(j).add("Additional Electives");
        				tempTable.add(studentCourseAL.get(j));
        				studentCourseAL.remove(j);
        			}
        		}
    		}
    		dataList.set(3, tempTable);
    	}
    	else if (degreePlan.equals("Systems")) {
    		Integer count = 0;
    		tempTable = new ArrayList<ArrayList<String>>();
    		while(!studentCourseAL.isEmpty() && count < 5) {
    			for(int j = 0; j < studentCourseAL.size(); j++) {
        			String number = studentCourseAL.get(j).get(1).substring(3);
        			Integer courseNum = Integer.parseInt(number);
        			if(courseNum > 6000) {
        				studentCourseAL.get(j).add("6000 Electives");
        				tempTable.add(studentCourseAL.get(j));
        				studentCourseAL.remove(studentCourseAL.get(j));
        				count++;
        			}
        		}
    		}
    		dataList.set(2, tempTable);
    		
    		tempTable = new ArrayList<ArrayList<String>>();
    		
    		while(!studentCourseAL.isEmpty()) {
    			for(int j = 0; j < studentCourseAL.size(); j++) {
        			String number = studentCourseAL.get(j).get(1).substring(3);
        			Integer courseNum = Integer.parseInt(number);
        			if(courseNum > 5000) {
        				studentCourseAL.get(j).add("Additional Electives");
        				tempTable.add(studentCourseAL.get(j));
        				studentCourseAL.remove(j);
        			}
        		}
    		}
    		dataList.set(3, tempTable);
    	}
    	else if (degreePlan.equals("Interactive Computing")) {
    		Integer count = 0;
    		tempTable = new ArrayList<ArrayList<String>>();
    		
    		while(!studentCourseAL.isEmpty() && count < 5) {
    			for(int j = 0; j < studentCourseAL.size(); j++) {
        			String[] number = studentCourseAL.get(j).get(1).split(" ");
        			Integer courseNum = Integer.parseInt(number[1]);
        			if(courseNum > 6000 && (number[0].equals("CS") || number[0].equals("SE"))) {
        				studentCourseAL.get(j).add("6000 Electives");
        				tempTable.add(studentCourseAL.get(j));
        				studentCourseAL.remove(studentCourseAL.get(j));
        				count++;
        			}
        		}
    		}
    		dataList.set(2, tempTable);
    		
    		tempTable = new ArrayList<ArrayList<String>>();
    		
    		while(!studentCourseAL.isEmpty()) {
    			for(int j = 0; j < studentCourseAL.size(); j++) {
        			String[] number = studentCourseAL.get(j).get(1).split(" ");
        			Integer courseNum = Integer.parseInt(number[1]);
        			if(courseNum > 5000) {
        				studentCourseAL.get(j).add("Additional Electives");
        				tempTable.add(studentCourseAL.get(j));
        				studentCourseAL.remove(j);
        			}
        		}
    		}
    		dataList.set(3, tempTable);
    	}
    	else if (degreePlan.equals("Data Science")) {
    		Integer count = 0;
    		tempTable = new ArrayList<ArrayList<String>>();
    		
    		while(!studentCourseAL.isEmpty() && count < 5) {
    			for(int j = 0; j < studentCourseAL.size(); j++) {
        			String[] number = studentCourseAL.get(j).get(1).split(" ");
        			Integer courseNum = Integer.parseInt(number[1]);
        			if(courseNum > 6000) {
        				studentCourseAL.get(j).add("6000 Electives");
        				tempTable.add(studentCourseAL.get(j));
        				studentCourseAL.remove(studentCourseAL.get(j));
        				count++;
        			}
        		}
    		}
    		dataList.set(2, tempTable);
    		
    		tempTable = new ArrayList<ArrayList<String>>();
    		
    		while(!studentCourseAL.isEmpty()) {
    			for(int j = 0; j < studentCourseAL.size(); j++) {
        			String[] number = studentCourseAL.get(j).get(1).split(" ");
        			Integer courseNum = Integer.parseInt(number[1]);
        			if(courseNum > 5000) {
        				studentCourseAL.get(j).add("Additional Electives");
        				tempTable.add(studentCourseAL.get(j));
        				studentCourseAL.remove(j);
        			}
        		}
    		}
    		dataList.set(3, tempTable);
    	}
    	else if (degreePlan.equals("Traditional Computer Science")) {
    		Integer count = 0;
    		tempTable = new ArrayList<ArrayList<String>>();
    		
    		while(!studentCourseAL.isEmpty() && count < 5) {
    			for(int j = 0; j < studentCourseAL.size(); j++) {
        			String[] number = studentCourseAL.get(j).get(1).split(" ");
        			Integer courseNum = Integer.parseInt(number[1]);
        			if(courseNum > 6000) {
        				studentCourseAL.get(j).add("6000 Electives");
        				tempTable.add(studentCourseAL.get(j));
        				studentCourseAL.remove(studentCourseAL.get(j));
        				count++;
        			}
        		}
    		}
    		dataList.set(2, tempTable);
    		
    		tempTable = new ArrayList<ArrayList<String>>();
    		
    		while(!studentCourseAL.isEmpty()) {
    			for(int j = 0; j < studentCourseAL.size(); j++) {
        			String[] number = studentCourseAL.get(j).get(1).split(" ");
        			Integer courseNum = Integer.parseInt(number[1]);
        			if(courseNum > 5000) {
        				studentCourseAL.get(j).add("Additional Electives");
        				tempTable.add(studentCourseAL.get(j));
        				studentCourseAL.remove(j);
        			}
        		}
    		}
    		dataList.set(3, tempTable);
    	}
    	else if (degreePlan.equals("Networks and Telecommunications")) {
    		Integer count = 0;
    		tempTable = new ArrayList<ArrayList<String>>();
    		
    		while(!studentCourseAL.isEmpty() && count < 5) {
    			for(int j = 0; j < studentCourseAL.size(); j++) {
        			String[] number = studentCourseAL.get(j).get(1).split(" ");
        			Integer courseNum = Integer.parseInt(number[1]);
        			if(courseNum > 6000) {
        				studentCourseAL.get(j).add("6000 Electives");
        				tempTable.add(studentCourseAL.get(j));
        				studentCourseAL.remove(studentCourseAL.get(j));
        				count++;
        			}
        		}
    		}
    		dataList.set(2, tempTable);
    		
    		tempTable = new ArrayList<ArrayList<String>>();
    		
    		while(!studentCourseAL.isEmpty()) {
    			for(int j = 0; j < studentCourseAL.size(); j++) {
        			String[] number = studentCourseAL.get(j).get(1).split(" ");
        			Integer courseNum = Integer.parseInt(number[1]);
        			if(courseNum > 5000) {
        				studentCourseAL.get(j).add("Additional Electives");
        				tempTable.add(studentCourseAL.get(j));
        				studentCourseAL.remove(j);
        			}
        		}
    		}
    		dataList.set(3, tempTable);
    	}
    	else if (degreePlan.equals("Software Engineering")) {
    		Integer count = 0;
    		tempTable = new ArrayList<ArrayList<String>>();
    		
    		while(!studentCourseAL.isEmpty() && count < 5) {
    			for(int j = 0; j < studentCourseAL.size(); j++) {
        			String[] number = studentCourseAL.get(j).get(1).split(" ");
        			Integer courseNum = Integer.parseInt(number[1]);
        			if(courseNum > 6000) {
        				studentCourseAL.get(j).add("6000 Electives");
        				tempTable.add(studentCourseAL.get(j));
        				studentCourseAL.remove(studentCourseAL.get(j));
        				count++;
        			}
        		}
    		}
    		dataList.set(2, tempTable);
    		
    		tempTable = new ArrayList<ArrayList<String>>();
    		
    		while(!studentCourseAL.isEmpty()) {
    			for(int j = 0; j < studentCourseAL.size(); j++) {
        			String[] number = studentCourseAL.get(j).get(1).split(" ");
        			Integer courseNum = Integer.parseInt(number[1]);
        			if(courseNum > 5000) {
        				studentCourseAL.get(j).add("Additional Electives");
        				tempTable.add(studentCourseAL.get(j));
        				studentCourseAL.remove(j);
        			}
        		}
    		}
    		dataList.set(3, tempTable);
    	}
    	else if (degreePlan.equals("Cyber Security")) {
    		Integer count = 0;
    		tempTable = new ArrayList<ArrayList<String>>();
    		
    		while(!studentCourseAL.isEmpty() && count < 5) {
    			for(int j = 0; j < studentCourseAL.size(); j++) {
        			String[] number = studentCourseAL.get(j).get(1).split(" ");
        			Integer courseNum = Integer.parseInt(number[1]);
        			if(courseNum > 6000) {
        				studentCourseAL.get(j).add("6000 Electives");
        				tempTable.add(studentCourseAL.get(j));
        				studentCourseAL.remove(studentCourseAL.get(j));
        				count++;
        			}
        		}
    		}
    		dataList.set(2, tempTable);
    		
    		tempTable = new ArrayList<ArrayList<String>>();
    		
    		while(!studentCourseAL.isEmpty()) {
    			for(int j = 0; j < studentCourseAL.size(); j++) {
        			String[] number = studentCourseAL.get(j).get(1).split(" ");
        			Integer courseNum = Integer.parseInt(number[1]);
        			if(courseNum > 5000) {
        				studentCourseAL.get(j).add("Additional Electives");
        				tempTable.add(studentCourseAL.get(j));
        				studentCourseAL.remove(j);
        			}
        		}
    		}
    		dataList.set(3, tempTable);
    	}
    }
    
    /**
     * Method splits the ArrayList of ArrayList of Strings created from the
     * transfered data of the hash map into tables and stores that table into an
     * ArrayList of ArrayList of ArrayList of Strings
     *
     */
    public void splitData() {
    	if(numTables == 5) {
    		// Loops through to create tables
            for (int i = 0; i < numTables; i++) {
                // Pollulates data for table 1
                if (i == 0) {
                	if (degreePlan.equals("Networks and Telecommunications")) {
                        tempTable = new ArrayList<ArrayList<String>>();
                        for (int row = 0; row < 5; row++) {
                        	defaultCourseAL.get(dataRow).add("Core Courses");
                            tempTable.add(defaultCourseAL.get(dataRow));
                            dataRow++;
                        }
                        dataList.add(tempTable);
                    } else if (degreePlan.equals("Software Engineering")) {
                        tempTable = new ArrayList<ArrayList<String>>();
                        for (int row = 0; row < 5; row++) {
                        	defaultCourseAL.get(dataRow).add("Core Courses");
                            tempTable.add(defaultCourseAL.get(dataRow));
                            dataRow++;
                        }
                        dataList.add(tempTable);
                    }
                }
                // Pollulates the data for table 5
                else if (i == 4) {
                	if (degreePlan.equals("Networks and Telecommunications")) {
                        dataRow = 5;
                        tempTable = new ArrayList<ArrayList<String>>();
                        for (int row = 0; row < 7; row++) {
                        	defaultCourseAL.get(dataRow).add("Admission Prerequisites");
                            tempTable.add(defaultCourseAL.get(dataRow));
                            dataRow++;
                        }
                        dataList.add(tempTable);
                    } else if (degreePlan.equals("Software Engineering")) {
                        dataRow = 5;
                        tempTable = new ArrayList<ArrayList<String>>();
                        for (int row = 0; row < 6; row++) {
                        	defaultCourseAL.get(dataRow).add("Admission Prerequisites");
                            tempTable.add(defaultCourseAL.get(dataRow));
                            dataRow++;
                        }
                        dataList.add(tempTable);
                    }
                }
                // Pollulates the data for the other tables
	            else {
	                tempTable = new ArrayList<ArrayList<String>>();
	                innerList = new ArrayList<String>();
	                for (int col = 0; col < columnNames.length; col++) {
	                    innerList.add("");
	                }
	                tempTable.add(innerList);
	                dataList.add(tempTable);
	            }
            }
            
    	}
    	else {
    		// Loops through to create tables
	        for (int i = 0; i < numTables; i++) {
	            // Pollulates data for table 1
	            if (i == 0) {
	                // Check the type of degree plan 
	                if (degreePlan.equals("Intelligent Systems")) {
	                    tempTable = new ArrayList<ArrayList<String>>();
	                    for (int row = 0; row < 4; row++) {
	                    	defaultCourseAL.get(dataRow).add("Core Courses");
	                        tempTable.add(defaultCourseAL.get(dataRow));
	                        dataRow++;
	                    }
	                    dataList.add(tempTable);
	                } else if (degreePlan.equals("Interactive Computing")) {
	                    tempTable = new ArrayList<ArrayList<String>>();
	                    for (int row = 0; row < 2; row++) {
	                    	defaultCourseAL.get(dataRow).add("Core Courses");
	                        tempTable.add(defaultCourseAL.get(dataRow));
	                        dataRow++;
	                    }
	                    dataList.add(tempTable);
	                } else if (degreePlan.equals("Traditional Computer Science")) {
	                    tempTable = new ArrayList<ArrayList<String>>();
	                    for (int row = 0; row < 3; row++) {
	                    	defaultCourseAL.get(dataRow).add("Core Courses");
	                        tempTable.add(defaultCourseAL.get(dataRow));
	                        dataRow++;
	                    }
	                    dataList.add(tempTable);
	                } else if (degreePlan.equals("Data Science")) {
	                    tempTable = new ArrayList<ArrayList<String>>();
	                    for (int row = 0; row < 4; row++) {
	                    	defaultCourseAL.get(dataRow).add("Core Courses");
	                        tempTable.add(defaultCourseAL.get(dataRow));
	                        dataRow++;
	                    }
	                    dataList.add(tempTable);
	                } else if (degreePlan.equals("Systems")) {
	                    tempTable = new ArrayList<ArrayList<String>>();
	                    for (int row = 0; row < 4; row++) {
	                    	defaultCourseAL.get(dataRow).add("Core Courses");
	                        tempTable.add(defaultCourseAL.get(dataRow));
	                        dataRow++;
	                    }
	                    dataList.add(tempTable);
	                } else if (degreePlan.equals("Cyber Security")) {
	                    tempTable = new ArrayList<ArrayList<String>>();
	                    for (int row = 0; row < 3; row++) {
	                    	defaultCourseAL.get(dataRow).add("Core Courses");
	                        tempTable.add(defaultCourseAL.get(dataRow));
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
	                    	defaultCourseAL.get(dataRow).add("X of the Following Courses");
	                        tempTable.add(defaultCourseAL.get(dataRow));
	                        dataRow++;
	                    }
	                    dataList.add(tempTable);
	                } else if (degreePlan.equals("Interactive Computing")) {
	                    dataRow = 2;
	                    tempTable = new ArrayList<ArrayList<String>>();
	                    for (int row = 0; row < 5; row++) {
	                    	defaultCourseAL.get(dataRow).add("X of the Following Courses");
	                        tempTable.add(defaultCourseAL.get(dataRow));
	                        dataRow++;
	                    }
	                    dataList.add(tempTable);
	                } else if (degreePlan.equals("Traditional Computer Science")) {
	                    dataRow = 3;
	                    tempTable = new ArrayList<ArrayList<String>>();
	                    for (int row = 0; row < 3; row++) {
	                    	defaultCourseAL.get(dataRow).add("X of the Following Courses");
	                        tempTable.add(defaultCourseAL.get(dataRow));
	                        dataRow++;
	                    }
	                    dataList.add(tempTable);
	                } else if (degreePlan.equals("Data Science")) {
	                    dataRow = 4;
	                    tempTable = new ArrayList<ArrayList<String>>();
	                    for (int row = 0; row < 5; row++) {
	                    	defaultCourseAL.get(dataRow).add("X of the Following Courses");
	                        tempTable.add(defaultCourseAL.get(dataRow));
	                        dataRow++;
	                    }
	                    dataList.add(tempTable);
	                } else if (degreePlan.equals("Systems")) {
	                    dataRow = 4;
	                    tempTable = new ArrayList<ArrayList<String>>();
	                    for (int row = 0; row < 5; row++) {
	                    	defaultCourseAL.get(dataRow).add("X of the Following Courses");
	                        tempTable.add(defaultCourseAL.get(dataRow));
	                        dataRow++;
	                    }
	                    dataList.add(tempTable);
	                } else if (degreePlan.equals("Cyber Security")) {
	                    dataRow = 3;
	                    tempTable = new ArrayList<ArrayList<String>>();
	                    for (int row = 0; row < 4; row++) {
	                    	defaultCourseAL.get(dataRow).add("X of the Following Courses");
	                        tempTable.add(defaultCourseAL.get(dataRow));
	                        dataRow++;
	                    }
	                    dataList.add(tempTable);
	                }
	            }
	            // Pollulates the data for table 6
	            else if (i == 5) {
	                // Check the type of degree plan 
	                if (degreePlan.equals("Intelligent Systems")) {
	                    dataRow = 6;
	                    tempTable = new ArrayList<ArrayList<String>>();
	                    for (int row = 0; row < 5; row++) {
	                    	defaultCourseAL.get(dataRow).add("Admission Prerequisites");
	                        tempTable.add(defaultCourseAL.get(dataRow));
	                        dataRow++;
	                    }
	                    dataList.add(tempTable);
	                } else if (degreePlan.equals("Interactive Computing")) {
	                    dataRow = 7;
	                    tempTable = new ArrayList<ArrayList<String>>();
	                    for (int row = 0; row < 5; row++) {
	                    	defaultCourseAL.get(dataRow).add("Admission Prerequisites");
	                        tempTable.add(defaultCourseAL.get(dataRow));
	                        dataRow++;
	                    }
	                    dataList.add(tempTable);
	                } else if (degreePlan.equals("Traditional Computer Science")) {
	                    dataRow = 6;
	                    tempTable = new ArrayList<ArrayList<String>>();
	                    for (int row = 0; row < 7; row++) {
	                    	defaultCourseAL.get(dataRow).add("Admission Prerequisites");
	                        tempTable.add(defaultCourseAL.get(dataRow));
	                        dataRow++;
	                    }
	                    dataList.add(tempTable);
	                } else if (degreePlan.equals("Data Science")) {
	                    dataRow = 9;
	                    tempTable = new ArrayList<ArrayList<String>>();
	                    for (int row = 0; row < 6; row++) {
	                    	defaultCourseAL.get(dataRow).add("Admission Prerequisites");
	                        tempTable.add(defaultCourseAL.get(dataRow));
	                        dataRow++;
	                    }
	                    dataList.add(tempTable);
	                } else if (degreePlan.equals("Systems")) {
	                    dataRow = 9;
	                    tempTable = new ArrayList<ArrayList<String>>();
	                    for (int row = 0; row < 6; row++) {
	                    	defaultCourseAL.get(dataRow).add("Admission Prerequisites");
	                        tempTable.add(defaultCourseAL.get(dataRow));
	                        dataRow++;
	                    }
	                    dataList.add(tempTable);
	                } else if (degreePlan.equals("Cyber Security")) {
	                    dataRow = 7;
	                    tempTable = new ArrayList<ArrayList<String>>();
	                    for (int row = 0; row < 6; row++) {
	                    	defaultCourseAL.get(dataRow).add("Admission Prerequisites");
	                        tempTable.add(defaultCourseAL.get(dataRow));
	                        dataRow++;
	                    }
	                    dataList.add(tempTable);
	                }
	            } // Pollulates the data for the other tables
	            else {
	                tempTable = new ArrayList<ArrayList<String>>();
//	                innerList = new ArrayList<String>();
//	                for (int col = 0; col < columnNames.length; col++) {
//	                    innerList.add("");
//	                }
//	                tempTable.add(innerList);
	                dataList.add(tempTable);
	            }
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
            innerList.add("");
        }

        if (tableFlag == 0) {
        	innerList.add("Core Courses");
            dataList.get(0).add(innerList);
        } else if (tableFlag == 1) {
        	if(numTables == 6) {
        		innerList.add("X of the Following Courses");
        		dataList.get(1).add(innerList);
        	}
        	else {
        		innerList.add("6000 Electives");
        		dataList.get(1).add(innerList);
        	}
        } else if (tableFlag == 2) {
        	if(numTables == 6) {
        		innerList.add("6000 Electives");
        		dataList.get(2).add(innerList);
        	}
        	else {
        		innerList.add("Additional Electives");
        		dataList.get(2).add(innerList);
        	}
        } else if (tableFlag == 3) {
        	if(numTables == 6) {
        		innerList.add("Additional Electives");
        		dataList.get(3).add(innerList);
        	}
        	else {
        		innerList.add("Other Requirements");
        		dataList.get(3).add(innerList);
        	}
        } else if (tableFlag == 4) {
        	if(numTables == 6) {
        		innerList.add("Other Requirements");
                dataList.get(4).add(innerList);
        	}
        	else {
        		innerList.add("Addmission Prerequisites");
        		dataList.get(4).add(innerList);
        	}
        } else if (tableFlag == 5) {
        	innerList.add("Addmission Prerequisites");
    		dataList.get(4).add(innerList);
        }
        
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
    }

    public void printData() {
//        for (int i = 0; i < dataList.size(); i++) {
//            for (int j = 0; j < dataList.get(i).size(); j++) {
//                System.out.print(dataList.get(i).get(j) + "\n");
//            }
//            System.out.println();
//        }
        
//        for (Course course : courses) {
//    		System.out.println(course.getClassName() + " " + course.getDepartment() + " " + course.getCourseNumber() + " " +
//    							course.getSemester() + " " + course.getTransferType() + " " + course.getLetterGrade());
//    	}
//        for (int i = 0; i < studentCourseAL.size(); i++) {
//        	System.out.print(studentCourseAL.get(i) + "\n");
//        }
//        System.out.println();
    }

}