
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
    ArrayList<ArrayList<String>> specialCourses = new ArrayList<ArrayList<String>>();
    ArrayList<Course> convertedLeveling_Prereq; //ArrayList of the special courses selected by the user
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
     * @param convertedLeveling_Prereq
     */
    public TableModel(int numOfTables, int tableFlag, String degreePlan, HashMap<String, ArrayList<Course>> defaultCourses, ArrayList<Course> courses, ArrayList<Course> convertedLeveling_Prereq){
        this.degreePlan = degreePlan;
        this.defaultCourses = defaultCourses;
        this.courses = courses;
        this.tableFlag = tableFlag;
        this.numTables = numOfTables;
        this.convertedLeveling_Prereq = convertedLeveling_Prereq;

        
        addCoursesToData();
        addStudentCoursesToArrayList();
        splitData();
        addSpecialPrereq();
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
    
    /**
     * Method transfers the read-in courses from the transcript into
     * an ArrayList
     *
     */
    public void addStudentCoursesToArrayList() {
    	//Iterates through the transcript course arraylist
    	for(Course course: courses) {
    		//Combines the course department and number into one string
    		String courseNumber = course.getDepartment() + " " + course.getCourseNumber();
    		
    		//Checks if the course is one of the special courses
    		if(course.getClassName().substring(0, 3).equals(">->") || course.getCourseNumber().equals("5343") || course.getCourseNumber().equals("5333") || course.getCourseNumber().equals("5348")) {
    			// Creates an ArrayList of Strings to store each course values
    			innerList = new ArrayList<String>();
    			//Adds the course info into a arraylist
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
    			//Add the arraylist of course info into another arraylist
    			specialCourses.add(innerList);
    			unchangedStudentCourseAL.add(innerList);
    			//courses.remove(course);
    		}
    		else {
    			// Creates an ArrayList of Strings to store each course values
    			innerList = new ArrayList<String>();
    			//Adds the course info into a arraylist
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
    			//Add the arraylist of course info into another arraylist
    			studentCourseAL.add(innerList);
    			unchangedStudentCourseAL.add(innerList);
    		}
    	}
    }
    
    /**
     * Method populates the modified row with the course info if the course is contained
     * in the course ArrayList read-in from the transcript
     * 
     * @param tableFlag
     *
     */
    public void updateNewRow(Integer tableFlag) {
    	//Checks for the table flag 
    	if(tableFlag == 0 || tableFlag == 1 || tableFlag == 2 || tableFlag == 3 || tableFlag == 4 || tableFlag == 5) {
    		//Loops through the dataList Arraylist
    		for(int i = 0; i < dataList.size(); i++) {
        		for(int j = 0; j < dataList.get(i).size(); j++) {
        			//Stores the first and second column of the dataList course into Strings
        			String col1 = dataList.get(i).get(j).get(0).toUpperCase();
        			String col2 = dataList.get(i).get(j).get(1).toUpperCase();
        			for(int k = 0; k < unchangedStudentCourseAL.size(); k++) {
        				//Stores the first and second column of the unchangedStudentCourseAL course into Strings
        				String studentCol1 = unchangedStudentCourseAL.get(k).get(0);
        				String studentCol2 = unchangedStudentCourseAL.get(k).get(1);
        				
        				//Checks if the modified row's course matches with the read-in course ArrayList
        				if(col1.equals(studentCol1) && col2.equals(studentCol2) || col2.equals(studentCol2)) {
        					//Populates the other columns if the course matches
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
    
    /**
     * Method compares and adds the special courses to the specified table
     *
     */
    public void addSpecialPrereq() {
    	//Empty variables for the special courses grades
    	String grade5343 = "";
    	String grade5333 = "";
    	String grade5348 = "";

    	//Iterates through the course that were selected by user and were not in the read-in courses ArrayList
    	for(Course course: convertedLeveling_Prereq) {
    		//Combines the course department and number into one string
    		String courseNumber = course.getDepartment() + " " + course.getCourseNumber();
    		// Creates an ArrayList of Strings to store each course values
    		innerList = new ArrayList<String>();
    		//Adds the course info into a arraylist
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
            
    		//Checks if the course is one of the special courses and add it to the specialCourses ArrayList
            if(course.getCourseNumber().equals("5343")) {
            	//Sets the grade
    			grade5343 = course.getLetterGrade();
    			//System.out.println(grade5343 + "*\n" + grade5333 + "*\n" + grade5348 + "*\n");
    			specialCourses.add(innerList);
    		}
    		else if(course.getCourseNumber().equals("5333")) {
    			//Sets the grade
    			grade5333 = course.getLetterGrade();
    			//System.out.println(grade5343 + "*\n" + grade5333 + "*\n" + grade5348 + "*\n");
    			specialCourses.add(innerList);
    		}
    		else if(course.getCourseNumber().equals("5348")) {
    			//Sets the grade
    			grade5348 = course.getLetterGrade();
    			//System.out.println(grade5343 + "*\n" + grade5333 + "*\n" + grade5348 + "*\n");
    			specialCourses.add(innerList);
    		}
            
            //Add a flag to the end 
            innerList.add("Admission Prerequisites");
            
            //Check the number of tables of the degree plan
            if(numTables == 6) {
            	//Loops through the Prereq table and replaces the default course with the special course
            	for(int i = 0; i < dataList.get(5).size(); i++) {
            		String col2 = dataList.get(5).get(i).get(1).toUpperCase();
            		if(dataList.get(5).get(i).contains(courseNumber)) {
            			dataList.get(5).remove(i);
            			//dataList.get(5).add(innerList);
            		}
            	}
            	dataList.get(5).add(innerList);
            }
            else {
            	//Loops through the Prereq table and replaces the default course with the special course
            	for(int i = 0; i < dataList.get(4).size(); i++) {
            		String col2 = dataList.get(4).get(i).get(1).toUpperCase();
            		if(col2.equals(courseNumber)) {
            			dataList.get(4).remove(i);
            			//dataList.get(4).add(innerList);
            		}
            	}
            	dataList.get(4).add(innerList);
    		}
            
    	}
    	
    	//Iterates the specialCourses ArrayList and sets the grades
    	for(int i = 0; i < specialCourses.size(); i++) {
    		if(specialCourses.get(i).get(1).equals("CS 5343")) {
    			grade5343 = specialCourses.get(i).get(4);
    			//System.out.println(grade5343 + "*\n" + grade5333 + "*\n" + grade5348 + "*\n");
    		}
    		else if(specialCourses.get(i).get(1).equals("CS 5333")) {
    			grade5333 = specialCourses.get(i).get(4);
    			//System.out.println(grade5343 + "*\n" + grade5333 + "*\n" + grade5348 + "*\n");
    		}
    		else if(specialCourses.get(i).get(1).equals("CS 5348")) {
    			grade5348 = specialCourses.get(i).get(4);
//    			System.out.println(grade5343 + "*\n" + grade5333 + "*\n" + grade5348 + "*\n");
    		}
    	}
    	
    	//Gets the course with the highest grade from another function
		String highestGrade = getHighestGrade(grade5343, grade5333, grade5348);
//		System.out.println(grade5343 + "*\n" + grade5333 + "*\n" + grade5348 + "*\n");
//		System.out.println(highestGrade);
		//Loops through the ArrayList and adds the course with the highest grade to the table
		for(int i = 0; i < specialCourses.size(); i++) {
			if(specialCourses.get(i).get(1).equals(highestGrade)) {
				if(numTables == 6) {
					specialCourses.get(i).add("Additional Electives");
					dataList.get(3).add(specialCourses.get(i));
					specialCourses.remove(i);
				}
				else {
					specialCourses.get(i).add("Additional Electives");
					dataList.get(2).add(specialCourses.get(i));
					specialCourses.remove(i);
				}
			}
		}
		
    	//Loops through the dataList Arraylist and populates the columns with the special courses
    	for(int i = 0; i < dataList.size(); i++) {
    		for(int j = 0; j < dataList.get(i).size(); j++) {
    			String col2 = dataList.get(i).get(j).get(1).toUpperCase();
    			for(int k = 0; k < specialCourses.size(); k++) {
    				String studentCol2 = specialCourses.get(k).get(1);
    				
    				if(col2.equals(studentCol2)) {
    					dataList.get(i).get(j).set(0, specialCourses.get(k).get(0));
    					dataList.get(i).get(j).set(2, specialCourses.get(k).get(2));
    					dataList.get(i).get(j).set(3, specialCourses.get(k).get(3));
    					dataList.get(i).get(j).set(4, specialCourses.get(k).get(4));
    				}
    			}
    		}
    	}
    }
    
    /**
     * Method takes the grades of the three special courses and returns the course
     * with the highest grade.
     * 
     * @param Cs5343
     * @param Cs5333
     * @param Cs5348
     *
     */
    public String getHighestGrade(String Cs5343, String Cs5333, String Cs5348) {
    	String[] grades = {"A", "A-", "B", "B-", "C", "C-", "F", "I", "P"};
    	int index5343 = -1;
    	int index5333 = -1;
    	int index5348 = -1;
    	
    	for(int i = 0; i < grades.length; i++) {
    		if(grades[i].equals(Cs5343)) {
    			index5343 = i;
    		}
    		else if(grades[i].equals(Cs5333)) {
    			index5333 = i;
    		}
    		else if(grades[i].equals(Cs5348)) {
    			index5348 = i;
    		}
    	}
    	
    	if(index5343 == index5333 && index5343 == index5348 && index5348 == index5333) {
    		System.out.println("1");
    		return "CS 5343";
    	}
    	else if(index5343 == -1 && index5333 == -1) {
    		System.out.println("1");
    		return "CS 5348";
    	}
    	else if(index5333 == -1 && index5348 == -1) {
    		System.out.println("1");
    		return "CS 5343";
    	}
    	else if(index5343 == -1 && index5348 == -1) {
    		System.out.println("1");
    		return "CS 5333";
    	}
    	else if(index5343 == -1) {
    		System.out.println("1");
    		if(index5348 == index5333) {
    			return "CS 5348";
    		}
    		else if(index5348 < index5333) {
    			return "CS 5348";
    		}
    		else {
    			return "CS 5333";
    		}
    	}
    	else if(index5333 == -1) {
    		System.out.println("1");
    		if(index5348 == index5343) {
    			return "CS 5343";
    		}
    		else if(index5343 < index5348) {
    			return "CS 5343";
    		}
    		else {
    			return "CS 5348";
    		}
    	}
    	else if(index5348 == -1) {
    		System.out.println("1");
    		if(index5343 == index5333) {
    			return "CS 5343";
    		}
    		else if(index5343 < index5333) {
    			return "CS 5343";
    		}
    		else {
    			return "CS 5333";
    		}
    	}
    	else if(index5343 < index5333 && index5343 < index5348) {
    		System.out.println("1");
    		return "CS 5343";
    	}
    	else if(index5333 < index5343 && index5333 < index5348) {
    		System.out.println("1");
    		return "CS 5333";
    	}
    	else if(index5348 < index5343 && index5348 < index5333) {
    		System.out.println("1");
    		return "CS 5348";
    	}
    	else {
    		return "";
    	}
    }

    /**
     * Method populates the default courses with the info from the read-in course ArrayList
     * and add the 6000 level Electives to their specified tables
     *
     */
    public void popullateTableColumns() {
    	//Loops through the dataList that contains the default courses and populates their columns
    	for(int i = 0; i < dataList.size(); i++) {
    		for(int j = 0; j < dataList.get(i).size(); j++) {
    			//Stores the first and second column of the dataList course into Strings
    			String col1 = dataList.get(i).get(j).get(0).toUpperCase();
    			String col2 = dataList.get(i).get(j).get(1).toUpperCase();
    			for(int k = 0; k < studentCourseAL.size(); k++) {
    				//Stores the first and second column of the unchangedStudentCourseAL course into Strings
    				String studentCol1 = studentCourseAL.get(k).get(0);
    				String studentCol2 = studentCourseAL.get(k).get(1);
    				
    				//Checks if the modified row's course matches with the read-in course ArrayList
    				if(col1.equals(studentCol1) && col2.equals(studentCol2) || col2.equals(studentCol2)) {
    					//Populates the other columns if the course matches
    					dataList.get(i).get(j).set(2, studentCourseAL.get(k).get(2));
    					dataList.get(i).get(j).set(3, studentCourseAL.get(k).get(3));
    					dataList.get(i).get(j).set(4, studentCourseAL.get(k).get(4));
    					//removes the course
    					studentCourseAL.remove(k);
    				}
    			}
    		}
    	}
    	
    	//First checks of the degree plan
    	if (degreePlan.equals("Intelligent Systems")) {
    		Integer count = 0;
    		
    		//Loops through the ArrayList until its empty or if count reachs 5
    		while(!studentCourseAL.isEmpty() && count < 5) {
    			//Adds the courses to the tables if the course is level 6000
    			for(int j = 0; j < studentCourseAL.size(); j++) {
    				String[] number = studentCourseAL.get(j).get(1).split(" ");
        			Integer courseNum = Integer.parseInt(number[1]);
        			if(courseNum > 6000) {
        				studentCourseAL.get(j).add("6000 Electives");
        				dataList.get(2).add(studentCourseAL.get(j));
        				studentCourseAL.remove(studentCourseAL.get(j));
        				count++;
        			}
        		}
    		}
    		
    		//Loops through the ArrayList and add the remaining courses to next table
    		while(!studentCourseAL.isEmpty()) {
    			for(int j = 0; j < studentCourseAL.size(); j++) {
    				String[] number = studentCourseAL.get(j).get(1).split(" ");
        			Integer courseNum = Integer.parseInt(number[1]);
        			if(courseNum > 5000) {
        				studentCourseAL.get(j).add("Additional Electives");
        				dataList.get(3).add(studentCourseAL.get(j));
        				studentCourseAL.remove(j);
        			}
        		}
    		}
    	}
    	else if (degreePlan.equals("Systems")) {
    		Integer count = 0;
    		//Loops through the ArrayList until its empty or if count reachs 5
    		while(!studentCourseAL.isEmpty() && count < 5) {
    			//Adds the courses to the tables if the course is level 6000
    			for(int j = 0; j < studentCourseAL.size(); j++) {
    				String[] number = studentCourseAL.get(j).get(1).split(" ");
        			Integer courseNum = Integer.parseInt(number[1]);
        			if(courseNum > 6000) {
        				studentCourseAL.get(j).add("6000 Electives");
        				dataList.get(2).add(studentCourseAL.get(j));
        				studentCourseAL.remove(studentCourseAL.get(j));
        				count++;
        			}
        		}
    		}
    		
    		//Loops through the ArrayList and add the remaining courses to next table
    		while(!studentCourseAL.isEmpty()) {
    			for(int j = 0; j < studentCourseAL.size(); j++) {
    				String[] number = studentCourseAL.get(j).get(1).split(" ");
        			Integer courseNum = Integer.parseInt(number[1]);
        			if(courseNum > 5000) {
        				studentCourseAL.get(j).add("Additional Electives");
        				dataList.get(3).add(studentCourseAL.get(j));
        				studentCourseAL.remove(j);
        			}
        		}
    		}
    	}
    	else if (degreePlan.equals("Interactive Computing")) {
    		Integer count = 0;
    		//Loops through the ArrayList until its empty or if count reachs 5
    		while(!studentCourseAL.isEmpty() && count < 5) {
    			//Adds the courses to the tables if the course is level 6000
    			for(int j = 0; j < studentCourseAL.size(); j++) {
        			String[] number = studentCourseAL.get(j).get(1).split(" ");
        			Integer courseNum = Integer.parseInt(number[1]);
        			if(courseNum > 6000 && (number[0].equals("CS") || number[0].equals("SE"))) {
        				studentCourseAL.get(j).add("6000 Electives");
        				dataList.get(2).add(studentCourseAL.get(j));
        				studentCourseAL.remove(studentCourseAL.get(j));
        				count++;
        			}
        		}
    		}
    		
    		//Loops through the ArrayList and add the remaining courses to next table
    		while(!studentCourseAL.isEmpty()) {
    			for(int j = 0; j < studentCourseAL.size(); j++) {
        			String[] number = studentCourseAL.get(j).get(1).split(" ");
        			Integer courseNum = Integer.parseInt(number[1]);
        			if(courseNum > 5000) {
        				studentCourseAL.get(j).add("Additional Electives");
        				dataList.get(2).add(studentCourseAL.get(j));
        				studentCourseAL.remove(j);
        			}
        		}
    		}
    	}
    	else if (degreePlan.equals("Data Science")) {
    		Integer count = 0;
    		//Loops through the ArrayList until its empty or if count reachs 5
    		while(!studentCourseAL.isEmpty() && count < 5) {
    			//Adds the courses to the tables if the course is level 6000
    			for(int j = 0; j < studentCourseAL.size(); j++) {
        			String[] number = studentCourseAL.get(j).get(1).split(" ");
        			Integer courseNum = Integer.parseInt(number[1]);
        			if(courseNum > 6000) {
        				studentCourseAL.get(j).add("6000 Electives");
        				dataList.get(2).add(studentCourseAL.get(j));
        				studentCourseAL.remove(studentCourseAL.get(j));
        				count++;
        			}
        		}
    		}
    		
    		//Loops through the ArrayList and add the remaining courses to next table
    		while(!studentCourseAL.isEmpty()) {
    			for(int j = 0; j < studentCourseAL.size(); j++) {
        			String[] number = studentCourseAL.get(j).get(1).split(" ");
        			Integer courseNum = Integer.parseInt(number[1]);
        			if(courseNum > 5000) {
        				studentCourseAL.get(j).add("Additional Electives");
        				dataList.get(3).add(studentCourseAL.get(j));
        				studentCourseAL.remove(j);
        			}
        		}
    		}
    	}
    	else if (degreePlan.equals("Traditional Computer Science")) {
    		Integer count = 0;
    		//Loops through the ArrayList until its empty or if count reachs 5
    		while(!studentCourseAL.isEmpty() && count < 5) {
    			//Adds the courses to the tables if the course is level 6000
    			for(int j = 0; j < studentCourseAL.size(); j++) {
        			String[] number = studentCourseAL.get(j).get(1).split(" ");
        			Integer courseNum = Integer.parseInt(number[1]);
        			if(courseNum > 6000) {
        				studentCourseAL.get(j).add("6000 Electives");
        				dataList.get(2).add(studentCourseAL.get(j));
        				studentCourseAL.remove(studentCourseAL.get(j));
        				count++;
        			}
        		}
    		}
    		
    		//Loops through the ArrayList and add the remaining courses to next table
    		while(!studentCourseAL.isEmpty()) {
    			for(int j = 0; j < studentCourseAL.size(); j++) {
        			String[] number = studentCourseAL.get(j).get(1).split(" ");
        			Integer courseNum = Integer.parseInt(number[1]);
        			if(courseNum > 5000) {
        				studentCourseAL.get(j).add("Additional Electives");
        				dataList.get(2).add(studentCourseAL.get(j));
        				studentCourseAL.remove(j);
        			}
        		}
    		}
    	}
    	else if (degreePlan.equals("Networks and Telecommunications")) {
    		Integer count = 0;
    		//Loops through the ArrayList until its empty or if count reachs 5
    		while(!studentCourseAL.isEmpty() && count < 5) {
    			//Adds the courses to the tables if the course is level 6000
    			for(int j = 0; j < studentCourseAL.size(); j++) {
        			String[] number = studentCourseAL.get(j).get(1).split(" ");
        			Integer courseNum = Integer.parseInt(number[1]);
        			if(courseNum > 6000) {
        				studentCourseAL.get(j).add("6000 Electives");
        				dataList.get(1).add(studentCourseAL.get(j));
        				studentCourseAL.remove(studentCourseAL.get(j));
        				count++;
        			}
        		}
    		}
    		
    		//Loops through the ArrayList and add the remaining courses to next table
    		while(!studentCourseAL.isEmpty()) {
    			for(int j = 0; j < studentCourseAL.size(); j++) {
        			String[] number = studentCourseAL.get(j).get(1).split(" ");
        			Integer courseNum = Integer.parseInt(number[1]);
        			if(courseNum > 5000) {
        				studentCourseAL.get(j).add("Additional Electives");
        				dataList.get(2).add(studentCourseAL.get(j));
        				studentCourseAL.remove(j);
        			}
        		}
    		}
    	}
    	else if (degreePlan.equals("Software Engineering")) {
    		Integer count = 0;
    		//Loops through the ArrayList until its empty or if count reachs 5
    		while(!studentCourseAL.isEmpty() && count < 5) {
    			//Adds the courses to the tables if the course is level 6000
    			for(int j = 0; j < studentCourseAL.size(); j++) {
        			String[] number = studentCourseAL.get(j).get(1).split(" ");
        			Integer courseNum = Integer.parseInt(number[1]);
        			if(courseNum > 6000) {
        				studentCourseAL.get(j).add("6000 Electives");
        				dataList.get(1).add(studentCourseAL.get(j));
        				studentCourseAL.remove(studentCourseAL.get(j));
        				count++;
        			}
        		}
    		}
    		
    		//Loops through the ArrayList and add the remaining courses to next table
    		while(!studentCourseAL.isEmpty()) {
    			for(int j = 0; j < studentCourseAL.size(); j++) {
        			String[] number = studentCourseAL.get(j).get(1).split(" ");
        			Integer courseNum = Integer.parseInt(number[1]);
        			if(courseNum > 5000) {
        				studentCourseAL.get(j).add("Additional Electives");
        				dataList.get(2).add(studentCourseAL.get(j));
        				studentCourseAL.remove(j);
        			}
        		}
    		}
    	}
    	else if (degreePlan.equals("Cyber Security")) {
    		Integer count = 0;
    		//Loops through the ArrayList until its empty or if count reachs 5
    		while(!studentCourseAL.isEmpty() && count < 5) {
    			//Adds the courses to the tables if the course is level 6000
    			for(int j = 0; j < studentCourseAL.size(); j++) {
        			String[] number = studentCourseAL.get(j).get(1).split(" ");
        			Integer courseNum = Integer.parseInt(number[1]);
        			if(courseNum > 6000) {
        				studentCourseAL.get(j).add("6000 Electives");
        				dataList.get(2).add(studentCourseAL.get(j));
        				studentCourseAL.remove(studentCourseAL.get(j));
        				count++;
        			}
        		}
    		}
    		
    		//Loops through the ArrayList and add the remaining courses to next table
    		while(!studentCourseAL.isEmpty()) {
    			for(int j = 0; j < studentCourseAL.size(); j++) {
        			String[] number = studentCourseAL.get(j).get(1).split(" ");
        			Integer courseNum = Integer.parseInt(number[1]);
        			if(courseNum > 5000) {
        				studentCourseAL.get(j).add("Additional Electives");
        				dataList.get(3).add(studentCourseAL.get(j));
        				studentCourseAL.remove(j);
        			}
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
    	if(numTables == 5) {
    		// Loops through to create tables
            for (int i = 0; i < numTables; i++) {
                // Pollulates data for table 1
                if (i == 0) {
                	if (degreePlan.equals("Networks and Telecommunications")) {
                        tempTable = new ArrayList<ArrayList<String>>();
                        for (int row = 0; row < 5; row++) {
                        	//Flag is added to the course
                        	defaultCourseAL.get(dataRow).add("Core Courses");
                            tempTable.add(defaultCourseAL.get(dataRow));
                            dataRow++;
                        }
                        dataList.add(tempTable);
                    } else if (degreePlan.equals("Software Engineering")) {
                        tempTable = new ArrayList<ArrayList<String>>();
                        for (int row = 0; row < 5; row++) {
                        	//Flag is added to the course
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
                        	//Flag is added to the course
                        	defaultCourseAL.get(dataRow).add("Admission Prerequisites");
                            tempTable.add(defaultCourseAL.get(dataRow));
                            dataRow++;
                        }
                        dataList.add(tempTable);
                    } else if (degreePlan.equals("Software Engineering")) {
                        dataRow = 5;
                        tempTable = new ArrayList<ArrayList<String>>();
                        for (int row = 0; row < 6; row++) {
                        	//Flag is added to the course
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
	                    	//Flag is added to the course
	                    	defaultCourseAL.get(dataRow).add("Core Courses");
	                        tempTable.add(defaultCourseAL.get(dataRow));
	                        dataRow++;
	                    }
	                    dataList.add(tempTable);
	                } else if (degreePlan.equals("Interactive Computing")) {
	                    tempTable = new ArrayList<ArrayList<String>>();
	                    for (int row = 0; row < 2; row++) {
	                    	//Flag is added to the course
	                    	defaultCourseAL.get(dataRow).add("Core Courses");
	                        tempTable.add(defaultCourseAL.get(dataRow));
	                        dataRow++;
	                    }
	                    dataList.add(tempTable);
	                } else if (degreePlan.equals("Traditional Computer Science")) {
	                    tempTable = new ArrayList<ArrayList<String>>();
	                    for (int row = 0; row < 3; row++) {
	                    	//Flag is added to the course
	                    	defaultCourseAL.get(dataRow).add("Core Courses");
	                        tempTable.add(defaultCourseAL.get(dataRow));
	                        dataRow++;
	                    }
	                    dataList.add(tempTable);
	                } else if (degreePlan.equals("Data Science")) {
	                    tempTable = new ArrayList<ArrayList<String>>();
	                    for (int row = 0; row < 4; row++) {
	                    	//Flag is added to the course
	                    	defaultCourseAL.get(dataRow).add("Core Courses");
	                        tempTable.add(defaultCourseAL.get(dataRow));
	                        dataRow++;
	                    }
	                    dataList.add(tempTable);
	                } else if (degreePlan.equals("Systems")) {
	                    tempTable = new ArrayList<ArrayList<String>>();
	                    for (int row = 0; row < 4; row++) {
	                    	//Flag is added to the course
	                    	defaultCourseAL.get(dataRow).add("Core Courses");
	                        tempTable.add(defaultCourseAL.get(dataRow));
	                        dataRow++;
	                    }
	                    dataList.add(tempTable);
	                } else if (degreePlan.equals("Cyber Security")) {
	                    tempTable = new ArrayList<ArrayList<String>>();
	                    for (int row = 0; row < 3; row++) {
	                    	//Flag is added to the course
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
	                    	//Flag is added to the course
	                    	defaultCourseAL.get(dataRow).add("X of the Following Courses");
	                        tempTable.add(defaultCourseAL.get(dataRow));
	                        dataRow++;
	                    }
	                    dataList.add(tempTable);
	                } else if (degreePlan.equals("Interactive Computing")) {
	                    dataRow = 2;
	                    tempTable = new ArrayList<ArrayList<String>>();
	                    for (int row = 0; row < 5; row++) {
	                    	//Flag is added to the course
	                    	defaultCourseAL.get(dataRow).add("X of the Following Courses");
	                        tempTable.add(defaultCourseAL.get(dataRow));
	                        dataRow++;
	                    }
	                    dataList.add(tempTable);
	                } else if (degreePlan.equals("Traditional Computer Science")) {
	                    dataRow = 3;
	                    tempTable = new ArrayList<ArrayList<String>>();
	                    for (int row = 0; row < 3; row++) {
	                    	//Flag is added to the course
	                    	defaultCourseAL.get(dataRow).add("X of the Following Courses");
	                        tempTable.add(defaultCourseAL.get(dataRow));
	                        dataRow++;
	                    }
	                    dataList.add(tempTable);
	                } else if (degreePlan.equals("Data Science")) {
	                    dataRow = 4;
	                    tempTable = new ArrayList<ArrayList<String>>();
	                    for (int row = 0; row < 5; row++) {
	                    	//Flag is added to the course
	                    	defaultCourseAL.get(dataRow).add("X of the Following Courses");
	                        tempTable.add(defaultCourseAL.get(dataRow));
	                        dataRow++;
	                    }
	                    dataList.add(tempTable);
	                } else if (degreePlan.equals("Systems")) {
	                    dataRow = 4;
	                    tempTable = new ArrayList<ArrayList<String>>();
	                    for (int row = 0; row < 5; row++) {
	                    	//Flag is added to the course
	                    	defaultCourseAL.get(dataRow).add("X of the Following Courses");
	                        tempTable.add(defaultCourseAL.get(dataRow));
	                        dataRow++;
	                    }
	                    dataList.add(tempTable);
	                } else if (degreePlan.equals("Cyber Security")) {
	                    dataRow = 3;
	                    tempTable = new ArrayList<ArrayList<String>>();
	                    for (int row = 0; row < 4; row++) {
	                    	//Flag is added to the course
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
	                    	//Flag is added to the course
	                    	defaultCourseAL.get(dataRow).add("Admission Prerequisites");
	                        tempTable.add(defaultCourseAL.get(dataRow));
	                        dataRow++;
	                    }
	                    dataList.add(tempTable);
	                } else if (degreePlan.equals("Interactive Computing")) {
	                    dataRow = 7;
	                    tempTable = new ArrayList<ArrayList<String>>();
	                    for (int row = 0; row < 5; row++) {
	                    	//Flag is added to the course
	                    	defaultCourseAL.get(dataRow).add("Admission Prerequisites");
	                        tempTable.add(defaultCourseAL.get(dataRow));
	                        dataRow++;
	                    }
	                    dataList.add(tempTable);
	                } else if (degreePlan.equals("Traditional Computer Science")) {
	                    dataRow = 6;
	                    tempTable = new ArrayList<ArrayList<String>>();
	                    for (int row = 0; row < 7; row++) {
	                    	//Flag is added to the course
	                    	defaultCourseAL.get(dataRow).add("Admission Prerequisites");
	                        tempTable.add(defaultCourseAL.get(dataRow));
	                        dataRow++;
	                    }
	                    dataList.add(tempTable);
	                } else if (degreePlan.equals("Data Science")) {
	                    dataRow = 9;
	                    tempTable = new ArrayList<ArrayList<String>>();
	                    for (int row = 0; row < 6; row++) {
	                    	//Flag is added to the course
	                    	defaultCourseAL.get(dataRow).add("Admission Prerequisites");
	                        tempTable.add(defaultCourseAL.get(dataRow));
	                        dataRow++;
	                    }
	                    dataList.add(tempTable);
	                } else if (degreePlan.equals("Systems")) {
	                    dataRow = 9;
	                    tempTable = new ArrayList<ArrayList<String>>();
	                    for (int row = 0; row < 6; row++) {
	                    	//Flag is added to the course
	                    	defaultCourseAL.get(dataRow).add("Admission Prerequisites");
	                        tempTable.add(defaultCourseAL.get(dataRow));
	                        dataRow++;
	                    }
	                    dataList.add(tempTable);
	                } else if (degreePlan.equals("Cyber Security")) {
	                    dataRow = 7;
	                    tempTable = new ArrayList<ArrayList<String>>();
	                    for (int row = 0; row < 6; row++) {
	                    	//Flag is added to the course
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
    	fireTableCellUpdated(row, column);
        return true;
    }

    /*
     * Method adds a empty row to the table and has a flag
     * with which table the row in add to
     */
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
    		dataList.get(5).add(innerList);
        }
    }

    /*
     * Method deletes the selected row from the specified table
     */
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

    /*
     * Method used to print data
     */
    public void printData() {
        for (int i = 0; i < dataList.size(); i++) {
            for (int j = 0; j < dataList.get(i).size(); j++) {
                System.out.print(dataList.get(i).get(j) + "\n");
            }
            System.out.println();
        }
        
//        for (Course course : convertedLeveling_Prereq) {
//    		System.out.println(course.getClassName() + " " + course.getDepartment() + " " + course.getCourseNumber() + " " +
//    							course.getSemester() + " " + course.getTransferType() + " " + course.getLetterGrade());
//    	}
        
//        System.out.println("Student Course Array:");
//        
//        for (int i = 0; i < specialCourses.size(); i++) {
//        	System.out.print(specialCourses.get(i) + "\n");
//        }
//        System.out.println();
//        
        
    	
//    	for(int i = 0; i < specialCourses.size(); i++) {
//    		System.out.println(specialCourses.get(i));
//    	}
    }

}