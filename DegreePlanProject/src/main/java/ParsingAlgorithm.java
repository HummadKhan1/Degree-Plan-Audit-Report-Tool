import java.util.ArrayList;
import java.util.HashMap;
//import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is used to parse the contents of read-in files. 
 * 
 */
public class ParsingAlgorithm{
    private String firstName; // Variable holds the firstName of the graduate student 
    private String lastName; // Variable holds the lastName of the graduate student
    private String ID; // Varaible holds the ID of the graduate student 
    private String program; // Variable holds the academic program the graduate student is in 
    private String appliedIn; // Variable holds the semester addmited into program 
    private String major; // Variable holds the major the graduate student is in (Computer Science or Software Engineering)
    Course course; // Variable holds a course 
    private ArrayList<Course> coursesArray = new ArrayList<>(); // ArrayList of Courses 

    /**
     * @return the firstName
     */
    public String getFirstName(){
        return firstName;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName){
        this.firstName = firstName;
    }
    
    /**
     * @return the lastName
     */
    public String getLastName(){
        return lastName;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName){
        this.lastName = lastName;
    }
    
    /**
     * @return the ID
     */
    public String getID(){
        return ID;
    }

    /**
     * @param ID the ID to set
     */
    public void setID(String ID){
        this.ID = ID;
    }

    /**
     * @return the program
     */
    public String getProgram(){
        return program;
    }

    /**
     * @param program the program to set
     */
    public void setProgram(String program){
        this.program = program;
    }

    /**
     * @return the appliedIn
     */
    public String getAppliedIn(){
        return appliedIn;
    }

    /**
     * @param appliedIn the appliedIn to set
     */
    public void setAppliedIn(String appliedIn){
        this.appliedIn = appliedIn;
    }

    /**
     * @return the major
     */
    public String getMajor(){
        return major;
    }

    /**
     * @param major the major to set
     */
    public void setMajor(String major){
        this.major = major;
    }

    /**
     * @return the coursesArray
     */
    public ArrayList<Course> getCourses(){
        return coursesArray;
    }
    
    /**
     * Method is used to parse through the transcript data and extract relevant information. 
     * 
     * @param transcriptData the transcript content to be parsed 
     */
    public void parseTranscript(String transcriptData){
        String semester = ""; // Variable holds the semester the course was taken in 
        String transferType = ""; // Variable holds the transfer type of the course (Transfer or Fast Track)
        Boolean flagBGR = false; // Flag is used to signal if the "Beginning of Graduate Record" (BGR) has began to be parsed  
        Boolean flagProgram = false; // Flag is used to signal if the program "Master" has began to be parsed 
        
        try (Scanner scanner = new Scanner(transcriptData)){
            // Regular expressions to match patterns in transcript data
            Pattern namePattern = Pattern.compile("Name:\\s+(\\w+)\\s+(\\w+)");
            Pattern idPattern = Pattern.compile("Student ID:\\s+(\\w+)");
            Pattern semesterPattern = Pattern.compile("(\\d{4})\\s+(Spring|Summer|Fall)"); 
            Pattern programPattern = Pattern.compile("Program:\\s+(Master)");
            Pattern majorPattern = Pattern.compile("\\d{4}-\\d{2}-\\d{2}:\\s+(Computer Science|Software Engineering) Major");
            Pattern courseRepeatPattern = Pattern.compile("Repeated:\\s+(\\w+)\\s+(\\w+)");
            Pattern courseDataPattern = Pattern.compile("([A-Z]*)\\s+(\\d{4})\\s+(.*?)\\s+(\\d+\\.?\\d*)\\s+(\\d+\\.?\\d*)\\s*([A-Z]([+-])?)?\\s*(\\d+\\.?\\d*)");
             
            while (scanner.hasNextLine()) // While-loop iterates through the entirety of the transcript data
            {
                String line = scanner.nextLine().trim(); // Store a line within the transcript data 
                
                // Regular expression matchers to extract relevant information from transcript data
                Matcher nameMatcher = namePattern.matcher(line);
                Matcher idMatcher = idPattern.matcher(line);
                Matcher semesterMatcher = semesterPattern.matcher(line);
                Matcher programMatcher = programPattern.matcher(line);
                Matcher majorMatcher = majorPattern.matcher(line);
                Matcher courseRepeatMatcher = courseRepeatPattern.matcher(line);
                Matcher courseDataMatcher = courseDataPattern.matcher(line);
                
                if (nameMatcher.matches()) 
                {
                    setFirstName(nameMatcher.group(1));
                    setLastName(nameMatcher.group(2));
                } 
                else if (idMatcher.matches())
                {
                    setID(idMatcher.group(1));
                }
                else if (programMatcher.matches())
                {
                    setProgram(programMatcher.group(1));
                    flagProgram = true; 
                }
                else if (majorMatcher.matches() && flagProgram == true)
                {
                    setMajor(majorMatcher.group(1));
                    flagProgram = false; // Re-set flag 
                }
                else if (line.equals("Transfer Credit from The University of Texas at Dallas")) 
                {
                    transferType = "Transfer";
                } 
                else if (line.equals("Transfer Credit from UT Dallas Fast Track")) 
                {
                    transferType = "Fast Track";
                }
                else if (line.equals("Beginning of Graduate Record"))
                {
                    flagBGR = true; 
                    transferType = ""; 
                }
                else if (semesterMatcher.matches()) 
                {
                    semester = semesterMatcher.group(1) + " " + semesterMatcher.group(2); 
                    
                    if (flagBGR) // If-statement executes only to assign the semester addmited into program 
                    {
                        setAppliedIn(semester); 
                        flagBGR = false; // Re-set flag 
                    }
                }
                else if (courseRepeatMatcher.matches())
                {
                    course.setRepeatCourse(courseRepeatMatcher.group(1) + " " + courseRepeatMatcher.group(2));
                }
                else if (courseDataMatcher.matches())
                {
                    String[] tokens = line.split("\\s+"); // Split the String line into array of Strings with separator as space or multiple spaces
                    String department = tokens[0]; 
                    String courseNumber = tokens[1];
                    
                    if (Integer.parseInt(courseNumber) >= 5000 && !(courseNumber.equals("5177"))) // Check to see if the course number is greater 
                                                                                                    // than 5000 and course number is not 5177
                    {
                        String title = ""; // Holds the course title 
                        double attemptedCredits = 0.0;
                        double earnedCredits = 0.0;
                        String letterGrade = "";
                        double points = 0.0;

                        for (int ix = 2; ix < tokens.length; ix++) // For-loop is used to iterate through the tokens array, body of loop assumes
                                                                   // fields for attempted credits, earned credits, letter grade, and points are not always present
                        {
                            String token = tokens[ix];
                            if (isNumeric(token)) 
                            {
                                if (attemptedCredits == 0.0) 
                                {
                                    attemptedCredits = Double.parseDouble(token);
                                } 
                                else if (earnedCredits == 0.0) 
                                {
                                    earnedCredits = Double.parseDouble(token);
                                } 
                                else
                                {
                                    points = Double.parseDouble(token);
                                }
                            } 
                            else if (token.matches("[A-Z][+-]?")) 
                            {
                                letterGrade = token;
                            } 
                            else
                            {
                                title += token + " ";
                            }
                        }
                        
                        title = title.trim(); // Remove whitespace from both ends of title String 
                        course = new Course(department, courseNumber, title, attemptedCredits, earnedCredits, letterGrade, points, semester, transferType);
                        coursesArray.add(course); // Add course to arrayList
                    }
                }
            }
        }
    }

    /**
     * Helper method that checks if str is a number or not.
     * 
     * @param str the String to check if it's a number
     * @return true if str is a number. false if str is not a number 
     */
    private static boolean isNumeric(String str){
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e){
            return false;
        }
    }
    
    /**
     * Method handles courses that have been repeated only twice.
     * Repeat courses marked as "Repeat Excluded" will no longer be in courseArray.
     * 
     */
    public void handleCourseRepeats() 
    {
        ArrayList<Course> newCoursesArray = new ArrayList<>(); // ArrayList used to store only courses not marked with "Repeat Excluded" 

        for (Course course : coursesArray) // For-loop is used to iterate through the courseArray
        {
            if (!course.getRepeatCourse().equals("Repeat Excluded")) // Check that only courses not marked with "Repeat Excluded" are added to the newCoursesArray 
            {
                newCoursesArray.add(course);
            }
        }

        coursesArray = newCoursesArray;  // courseArray will no longer contain courses with repeatCourse equal to "Repeat Excluded"
    }
    
    /**
     * Method handles duplicate courses caused by Transfer credits or Fast Track credits.
     * Kept Course object will contain the type of transfer the course is and also contain the original semester the course was taken in.
     * 
     */
    public void handleTransferType(){
        // Hashmap keeps track of courses by their Course key which is a concatenated String of department + course number
        HashMap<String, Course> courseMap = new HashMap<>(); // Key: Course key, Value: Course 
        
        for (Course course : coursesArray) // For-loop is used to iterate through the courseArray
        {
            String courseKey = course.getDepartment() + course.getCourseNumber();
            Course existingCourse = courseMap.get(courseKey); // Get existing course object in the HashMap matching the courseKey
            
            if (existingCourse == null)
            {
                courseMap.put(courseKey, course); 
            }
            else if (!(existingCourse.getTransferType().isEmpty()) && course.getTransferType().isEmpty())
            {
                existingCourse.setSemester(course.getSemester());
            }
            else if (existingCourse.getTransferType().isEmpty() && !(course.getTransferType().isEmpty()))
            {
                existingCourse.setTransferType(course.getTransferType());
            }
        }
        
        coursesArray = new ArrayList<>(courseMap.values());  // courseArray will no longer contain courses with duplicates caused by Transfer credits or Fast Track credits
}
    
//    public void removeDuplicates(){
//        // Hashmap keeps track of courses by their department and course number
//        HashMap<String, Course> courseMap = new HashMap<>(); // Key: Course key, Value: Course
//
//        for (Course course : coursesArray) // For-loop is used to iterate through the courseArray
//        {
//            String courseKey = course.getDepartment() + course.getCourseNumber();
//            Course existingCourse = courseMap.get(courseKey); // Get existing course object in the HashMap with the courseKey
//
//            if (existingCourse == null
//                    || (!existingCourse.getLetterGrade().equals("") && (course.getLetterGrade().equals("")
//                    || course.getSemester().compareTo(existingCourse.getSemester()) > 0)))
//            {
//                courseMap.put(courseKey, course);
//            }
//        }
//
//        coursesArray = new ArrayList<>(courseMap.values()); // Update the ArrayList of courses to remove duplicates
//    }
    
    public void printCourses(){
        System.out.println("Name: " + getFirstName() + " " + getLastName());
        System.out.println("ID: " + getID());
        System.out.println("Program: " + getProgram());
        System.out.println("Applied in: " + getAppliedIn());
        System.out.println("Major: " + getMajor());
        System.out.println("Courses:");
        System.out.println();
        
        for (Course course : coursesArray) 
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
}
