import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is used to parse the contents of read-in files. 
 * 
 */
public class ParsingAlgorithms{
    private String name; // Variable holds the name of the graduate student 
    private String ID; // Varaible holds the ID of the graduate student 
    private String program; // Variable holds the academic program the graduate student is in 
    private String appliedIn; // Variable holds the semester addmited into program 
    private String major; // Variable holds the major the graduate student is in (Computer Science or Software Engineering)
    private Course course; // Variable holds a course 
    private ArrayList<String> defaultCSTracks = new ArrayList<>(); // ArrayList will store the CS tracks available 
    private ArrayList<String> defaultSETracks = new ArrayList<>(); // ArrayList will store the SE tracks available 
    private ArrayList<String> defaultLeveling = new ArrayList<>(); // ArrayList will store the leveling courses/pre-requisites that are possible 
    private ArrayList<Course> coursesArray = new ArrayList<>(); // ArrayList of Courses, valid courses read-in from transcriptData are stored here 
    private HashMap<String, ArrayList<Course>> defaultCoursesMap = new HashMap<>(); // Hashmap stores the default courses found in the Default.txt file
                                                                                    // with the key being the type of track the courses fall under  
    private ArrayList<Course> coreCourses = new ArrayList<>(); // ArrayList will store the core courses being used in the degree plan 
    private ArrayList<Course> XFollowingCourses = new ArrayList<>(); // ArrayList will store the "X000 following courses" being used in the degree plan
    private ArrayList<Course> electives = new ArrayList<>(); // ArrayList will store the elective courses being used in the degree plan
    private ArrayList<Course> additionalElectives = new ArrayList<>(); // ArrayList will store the additional elective courses being used in the degree plan
    private ArrayList<Course> otherRequirements = new ArrayList<>(); // ArrayList will store the other requirement courses being used in the degree plan
    private ArrayList<Course> admissionPrerequisites = new ArrayList<>(); // ArrayList will store the admission prerequisite courses being used in the degree plan 
    private ArrayList<ArrayList<ArrayList<String>>> finalDataList; // ArrayList stores data found in each row of each table of the pre-view degree plan
    
    /**
     * Method is used to parse through the transcript data and extract relevant information. 
     * 
     * @param transcriptData the transcript content to be parsed 
     */
    public void parseTranscript(String transcriptData){
        String semester = ""; // Variable holds the semester the course was taken in 
        String transferType = ""; // Variable holds the transfer type of the course (Transfer or Fast Track)
        boolean flagBGR = false; // Flag is used to signal if the "Beginning of Graduate Record" (BGR) has began to be parsed, variable is true if it has  
        boolean flagProgram = false; // Flag is used to signal if the program "Master" has began to be parsed 
        
        try (Scanner scanner = new Scanner(transcriptData)){
            // Regular expressions to match patterns in transcript data
            Pattern namePattern = Pattern.compile("^Name:\\s+(.+)$");
            Pattern idPattern = Pattern.compile("Student ID:\\s+(\\w+)");
            Pattern semesterPattern = Pattern.compile("(\\d{4})\\s+(Spring|Summer|Fall)"); 
            Pattern programPattern = Pattern.compile("Program:\\s+(Master)");
            Pattern majorPattern = Pattern.compile("\\d{4}-\\d{2}-\\d{2}:\\s+(Computer Science|Software Engineering) Major");
            Pattern courseRepeatPattern = Pattern.compile("Repeated:\\s+(\\w+)\\s+(\\w+)");
            Pattern courseDataPattern = Pattern.compile("([A-Z]*)\\s+(\\d{4})\\s+(.*?)\\s+(\\d+\\.?\\d*)\\s+(\\d+\\.?\\d*)\\s*([A-Z]([+-])?)?\\s*(\\d+\\.?\\d*)");
             
            while (scanner.hasNextLine()) // While-loop iterates through the entirety of the transcript data
            {
                String line = scanner.nextLine().trim(); 
                
                // Regular expression matchers to extract relevant information from transcript data
                Matcher nameMatcher = namePattern.matcher(line);
                Matcher idMatcher = idPattern.matcher(line);
                Matcher semesterMatcher = semesterPattern.matcher(line);
                Matcher programMatcher = programPattern.matcher(line);
                Matcher majorMatcher = majorPattern.matcher(line);
                Matcher courseRepeatMatcher = courseRepeatPattern.matcher(line);
                Matcher courseDataMatcher = courseDataPattern.matcher(line);
                
                if (nameMatcher.matches()) // Checks to see if the line matches the pattern of a name
                {
                    setName(nameMatcher.group(1));;
                } 
                else if (idMatcher.matches()) // Checks to see if the line matches the pattern of a ID
                {
                    setID(idMatcher.group(1));
                }
                else if (programMatcher.matches()) // Checks to see if the line matches the pattern of a program
                {
                    setProgram(programMatcher.group(1));
                    flagProgram = true; 
                }
                else if (majorMatcher.matches() && flagProgram == true) // Checks to see if the line matches the pattern of a major.
                                                                        // Flag is true to signal the program "Master" has began to be parsed
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
                else if (semesterMatcher.matches()) // Checks to see if the line matches the pattern of a semester
                {
                    semester = semesterMatcher.group(1) + " " + semesterMatcher.group(2); 
                    
                    if (flagBGR) // If-statement executes only to assign the semester addmited into program 
                    {
                        setAppliedIn(semester); 
                        flagBGR = false; // Re-set flag 
                    }
                }
                else if (courseRepeatMatcher.matches()) // Checks to see if the line matches the pattern of a reapeated course
                {
                    getCourse().setRepeatCourse(courseRepeatMatcher.group(1) + " " + courseRepeatMatcher.group(2));
                }
                else if (courseDataMatcher.matches()) // Checks to see if the line matches the pattern of course data.
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
                        setCourse(new Course(department, courseNumber, title, attemptedCredits, earnedCredits, letterGrade, points, semester, transferType));
                        coursesArray.add(getCourse()); // Add course to arrayList
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
    public static boolean isNumeric(String str){
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e){
            return false;
        }
    }
    
    /**
     * Method is used to parse through the students default data and extract relevant information. 
     * 
     * @param data the student specific default data to be parsed 
     */
    public void parseTXT(String data){    
        try (Scanner scanner = new Scanner(data)){
            // Regular expressions to match patterns in data
            Pattern namePattern = Pattern.compile("^Name:\\s+(.+)$");
            Pattern idPattern = Pattern.compile("Student ID:\\s+(\\w+)");
            Pattern programPattern = Pattern.compile("Program: (.*)");
            Pattern appliedInPattern = Pattern.compile("Applied In: (.*)");
            Pattern majorPattern = Pattern.compile("Major: (.*)");
            Pattern coursePattern = Pattern.compile("^(.*?)\\s+([A-Za-z]+) (\\d{4})\\s+(\\d{4} (?:Fall|Spring|Summer))\\s+\\[([^\\]]*)\\]\\s*([A-Z][+-]?|)$");
            
             
            while (scanner.hasNextLine()) // While-loop iterates through the entirety of the data
            {
                String line = scanner.nextLine().trim(); 
                
                // Regular expression matchers to extract relevant information from data
                Matcher nameMatcher = namePattern.matcher(line);
                Matcher idMatcher = idPattern.matcher(line);
                Matcher programMatcher = programPattern.matcher(line);
                Matcher appliedInMatcher = appliedInPattern.matcher(line);
                Matcher majorMatcher = majorPattern.matcher(line);
                Matcher courseMatcher = coursePattern.matcher(line);
                
                if (line.equals("Computer Science Tracks:"))
                {
                    break; 
                }
                else if (nameMatcher.matches()) // Checks to see if the line matches the pattern of a name
                {
                    setName(nameMatcher.group(1));;
                } 
                else if (idMatcher.matches()) // Checks to see if the line matches the pattern of a ID
                {
                    setID(idMatcher.group(1));
                }
                else if (programMatcher.matches()) // Checks to see if the line matches the pattern of a program
                {
                    setProgram(programMatcher.group(1));
                }
                else if (appliedInMatcher.matches()) // Checks to see if the line matches the pattern of applied in date
                {
                    setAppliedIn(appliedInMatcher.group(1));
                }
                else if (majorMatcher.matches()) // Checks to see if the line matches the pattern of major
                {
                    setMajor(majorMatcher.group(1));
                }
                else if (courseMatcher.matches())  // Checks to see if the line matches the pattern of course
                {
                    setCourse(new Course(courseMatcher.group(1).trim(), courseMatcher.group(2), courseMatcher.group(3), courseMatcher.group(4), 
                            courseMatcher.group(5), courseMatcher.group(6)));
                    coursesArray.add(getCourse()); // Add course to arrayList 
                }
            }
        }
    }
    
    /**
     * Method is used to parse through the TXT data and extract relevant course information relating to the specific track.
     * Note that the term "default" is dependent on how the degree plan and audit report are being created, in other words
     * did the user select to generate said files from the PDF or the TXT option in the beginning of the program. 
     * 
     * @param defaultData 
     */
    public void parseDefaultCourses(String defaultData){   
        String track = ""; // Variable holds the type of track  
        String degreePlanSection = ""; // Variable holds the corresponding degree plan section 
        ArrayList<Course> tempCoursesArray = new ArrayList<>(); // tempCoursesArray holds the default courses for the respective track 
        boolean flag = false; // Flag is used to signal if a tracks default courses have started to be held in tempCoursesArray before another tracks 
                              // default courses have also started to be held in tempCoursesArray 
        final String CORE_COURSES = "Core Courses";
        final String X_FOLLOWING_COURSES = "X of the Following Courses";
        final String ADMISSION_PRE_REQ = "Admission Prerequisites";
        
        try (Scanner scanner = new Scanner(defaultData)){
            // Regular expressions to match patterns in default data
            Pattern trackPattern = Pattern.compile("^Track:\\s+(.+)$");
            Pattern courseDataPattern = Pattern.compile("^\\s*(.+?)\\s+-*\\s*(CS|SE)\\s+-*\\s*(\\d{4})\\s*$");
  
            while (scanner.hasNextLine()) // While-loop iterates through the entirety of the default data
            {
                String line = scanner.nextLine().trim();  
                
                // Regular expression matchers to extract relevant information from default data
                Matcher trackMatcher = trackPattern.matcher(line);
                Matcher courseDataMatcher = courseDataPattern.matcher(line);
                
                if (trackMatcher.matches()) // Checks to see if the line matches the pattern of a track 
                {   
                    if (flag) // If-statement executes if a tracks default courses have all been held in tempCoursesArray just before the track is re-initialized
                    {
                        getDefaultCoursesMap().put(track, new ArrayList<>(tempCoursesArray)); 
                        flag = false; // Re-set flag
                        tempCoursesArray.clear(); 
                    }
                    
                    track = trackMatcher.group(1);
                }
                else if (line.equals(CORE_COURSES))  
                {
                    degreePlanSection = CORE_COURSES;
                }
                else if (line.equals(X_FOLLOWING_COURSES))
                {
                    degreePlanSection = X_FOLLOWING_COURSES;
                }
                else if (line.equals(ADMISSION_PRE_REQ))
                {
                    degreePlanSection = ADMISSION_PRE_REQ;
                }
                else if (courseDataMatcher.matches()) // Checks to see if the line matches the pattern course data  
                {
                    String className = courseDataMatcher.group(1);
                    String department = courseDataMatcher.group(2);
                    String courseNumber = courseDataMatcher.group(3);
                    
                    setCourse(new Course(department, courseNumber, className, degreePlanSection)); 
                    tempCoursesArray.add(getCourse());
                    flag = true; 
                }  
            }
        }
        
        // This will add the very last track and contents of the tempCoursesArray into the defaultCoursesMap due to the while-loop ending
        getDefaultCoursesMap().put(track, new ArrayList<>(tempCoursesArray)); 
        tempCoursesArray.clear();
    }
    
    /**
     * Method is used to parse through the TXT data and extract all the tracks available.
     * Note that the term "default" is dependent on how the degree plan and audit report are being created, in other words
     * did the user select to generate said files from the PDF or the TXT option in the beginning of the program. 
     * 
     * @param defaultData 
     */
    public void parseDefaultTracks(String defaultData){
        final String LINE1 = "*********************DO NOT REMOVE THIS LINE 1*********************";
        String flag = ""; // Variable is used to distinguish between CS or SE tracks 
        
        try (Scanner scanner = new Scanner(defaultData)){
            while (scanner.hasNextLine()) // While-loop iterates through the entirety of the default data
            {
                String line = scanner.nextLine().trim();
                
                if (line.equals(LINE1)) // Executes when all the CS and SE tracks have been stored 
                {
                    break; 
                }
                else if (line.contains("Computer Science Tracks"))
                {
                    flag = "CS"; 
                }
                else if (line.contains("Software Engineering Tracks"))
                {
                    flag = "SE";
                }
                else if (flag.equals("CS") && !(line.isEmpty()))
                {
                    getDefaultCSTracks().add(line);
                }
                else if (flag.equals("SE") && !(line.isEmpty()))
                {
                    getDefaultSETracks().add(line);
                }
            }
        }
               
    }
    
    /**
     * Method is used to parse through the TXT data and extract all the leveling courses/pre-requisites that are possible.
     * Note that the term "default" is dependent on how the degree plan and audit report are being created, in other words
     * did the user select to generate said files from the PDF or the TXT option in the beginning of the program. 
     * 
     * @param defaultData 
     */
    public void parseDefaultLeveling(String defaultData){
        final String LINE2 = "*********************DO NOT REMOVE THIS LINE 2*********************";
        boolean flag = false; // Variable is used to signal when leveling courses/pre-requisites can start being stored 
        
        try (Scanner scanner = new Scanner(defaultData)){
            while (scanner.hasNextLine()) // While-loop iterates through the entirety of the default data
            {
                String line = scanner.nextLine().trim();
                
                if (line.equals(LINE2)) // Executes when all the leveling courses/pre-requisites have been stored 
                {
                    break; 
                }
                else if(line.contains("Leveling Courses/Pre-requisites Assigned at Admission")) 
                {
                    flag = true; 
                }
                else if (flag && !(line.isEmpty()))
                {
                    getDefaultLeveling().add(line);
                }
            }
        }
    }
    
    /**
     * Method handles courses that have been repeated only twice.
     * Repeat courses marked as "Repeat Excluded" will no longer be in courseArray.
     * 
     */
    public void handleCourseRepeats() 
    {
        ArrayList<Course> newCoursesArray = new ArrayList<>(); // ArrayList used to hold only courses not marked with "Repeat Excluded" 

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
    /**
     * This method is populating several ArrayLists (coreCourses, XFollowingCourses, electives, additionalElectives, otherRequirements, and admissionPrerequisites) 
     * with Course objects. 
     * The Course objects are created by extracting relevant information from a nested ArrayList of data called finalDataList.
     * 
     */
    public void populateDegreePlanArrayLists(){
        String department = "";
        String courseNumber = "";
        
        for (int i = 0; i < getFinalDataList().size(); i++) // if-statement loops through the outer list of finalDataList 
        {
            for (int j = 0; j < getFinalDataList().get(i).size(); j++) // if-statement loops through the inner lists of finalDataList
            {   
                String courseDepAndNumber = getFinalDataList().get(i).get(j).get(1).trim();
               
                if (!(courseDepAndNumber.equals(""))) 
                {
                    String[] parts = courseDepAndNumber.split(" "); // split the string on a space
                    department = parts[0];
                    courseNumber = parts[1];
                }
                
                // Based on the index i of the outer loop, create Course objects and add them to corresponding ArrayLists
                if (i == 0 && !(getFinalDataList().get(i).get(j).get(0).trim().equals(""))) 
                {
                    course = new Course(getFinalDataList().get(i).get(j).get(0).trim(), department, courseNumber, getFinalDataList().get(i).get(j).get(2).trim(),
                            getFinalDataList().get(i).get(j).get(3).trim(), getFinalDataList().get(i).get(j).get(4).trim());
                    coreCourses.add(course);
                } 
                else if (i == 1 && !(getFinalDataList().get(i).get(j).get(0).trim().equals(""))) 
                {
                    course = new Course(getFinalDataList().get(i).get(j).get(0).trim(), department, courseNumber, getFinalDataList().get(i).get(j).get(2).trim(),
                            getFinalDataList().get(i).get(j).get(3).trim(), getFinalDataList().get(i).get(j).get(4).trim());
                    XFollowingCourses.add(course);
                } 
                else if (i == 2 && !(getFinalDataList().get(i).get(j).get(0).trim().equals("")))
                {
                    course = new Course(getFinalDataList().get(i).get(j).get(0).trim(), department, courseNumber, getFinalDataList().get(i).get(j).get(2).trim(),
                            getFinalDataList().get(i).get(j).get(3).trim(), getFinalDataList().get(i).get(j).get(4).trim());
                    electives.add(course);
                } 
                else if (i == 3 && !(getFinalDataList().get(i).get(j).get(0).trim().equals("")))
                {
                    course = new Course(getFinalDataList().get(i).get(j).get(0).trim(), department, courseNumber, getFinalDataList().get(i).get(j).get(2).trim(),
                            getFinalDataList().get(i).get(j).get(3).trim(), getFinalDataList().get(i).get(j).get(4).trim());
                    additionalElectives.add(course);
                } 
                else if (i == 4 && !(getFinalDataList().get(i).get(j).get(0).trim().equals(""))) 
                {
                    course = new Course(getFinalDataList().get(i).get(j).get(0).trim(), department, courseNumber, getFinalDataList().get(i).get(j).get(2).trim(),
                            getFinalDataList().get(i).get(j).get(3).trim(), getFinalDataList().get(i).get(j).get(4).trim());
                    otherRequirements.add(course);
                }
                else if (i == 5 && !(getFinalDataList().get(i).get(j).get(0).trim().equals("")))
                {
                    course = new Course(getFinalDataList().get(i).get(j).get(0).trim(), department, courseNumber, getFinalDataList().get(i).get(j).get(2).trim(),
                            getFinalDataList().get(i).get(j).get(3).trim(), getFinalDataList().get(i).get(j).get(4).trim());
                    admissionPrerequisites.add(course);
                }
            }
        }
    }
    
    public void mergeLists(){
        // Hashmap keeps track of courses by their Course key which is a concatenated String of department + course number
        HashMap<String, Course> coursesArrayMap = new HashMap<>();  // Key: Course key, Value: Course 

        // Populate the HashMap with the courses from coursesArray
        for (Course course : getCoursesArray()) 
        {
            String key = course.getDepartment() + course.getCourseNumber();
            coursesArrayMap.put(key, course);
        }
        
        ArrayList<Course> mergedCourses = new ArrayList<>(); // mergedCourses contains all the courses that are used for the degree plan 
        mergedCourses.addAll(coreCourses);
        mergedCourses.addAll(XFollowingCourses);
        mergedCourses.addAll(electives);
        mergedCourses.addAll(additionalElectives);
        mergedCourses.addAll(otherRequirements);
        mergedCourses.addAll(admissionPrerequisites);


        // Use an iterator to iterate over the mergedCourses ArrayList and modify the corresponding courses in coursesArrayMap
        Iterator<Course> iterator = mergedCourses.iterator();
        while (iterator.hasNext()) 
        {
            Course currentMergedCourse = iterator.next();
            String key = currentMergedCourse.getDepartment() + currentMergedCourse.getCourseNumber();
            Course courseFromList = coursesArrayMap.get(key);

            if (courseFromList != null) 
            {
                if (currentMergedCourse.getClassName().contains(">->") && (!(currentMergedCourse.getSemester() == null) || !(currentMergedCourse.getSemester().equals(""))))
                {
                    courseFromList.setSemester(currentMergedCourse.getSemester());
                    courseFromList.setLetterGrade(currentMergedCourse.getLetterGrade());
                    courseFromList.setTransferType(currentMergedCourse.getTransferType());
                    coursesArrayMap.put(key, courseFromList); 
                    continue;  
                }
                else if (currentMergedCourse.getClassName().contains(">->") && (currentMergedCourse.getSemester() == null || currentMergedCourse.getSemester().equals("")))
                {
                    continue; 
                }
                    
                courseFromList.setSemester(currentMergedCourse.getSemester());
                courseFromList.setLetterGrade(currentMergedCourse.getLetterGrade());
                courseFromList.setTransferType(currentMergedCourse.getTransferType());
                coursesArrayMap.put(key, courseFromList);
            }
            else if (!(currentMergedCourse.getLetterGrade() == null) && !(currentMergedCourse.getLetterGrade().equals("")))
            {
                if (currentMergedCourse.getClassName().contains(">->") && (currentMergedCourse.getSemester() == null || currentMergedCourse.getSemester().equals("")))
                {
                    continue; 
                }
                coursesArrayMap.put(key, currentMergedCourse);
            }
        }
        coursesArray = new ArrayList<>(coursesArrayMap.values()); // Update the coursesArray with the modified courses from coursesArrayMap    
    }
    
    public void printCourses(){
        System.out.println("Name: " + getName());
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
    
    public void printHashMap(){
        for (String key : defaultCoursesMap.keySet()) 
        {
            System.out.println("Track: " + key);
            ArrayList<Course> courses = defaultCoursesMap.get(key);
            for (Course course : courses)
            {
                System.out.println("\t" + course.getClassName() + " - " + course.getDepartment() + " - " + course.getCourseNumber());
            }
        }
    }
    
    /**
     * @return the name
     */
    public String getName(){
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name){
        this.name = name;
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
    public ArrayList<Course> getCoursesArray(){
        return coursesArray;
    }

    /**
     * @return the course
     */
    public Course getCourse(){
        return course;
    }

    /**
     * @param course the course to set
     */
    public void setCourse(Course course){
        this.course = course;
    }

    /**
     * @return the defaultCoursesMap
     */
    public HashMap<String, ArrayList<Course>> getDefaultCoursesMap(){
        return defaultCoursesMap;
    }

    /**
     * @return the defaultCSTracks
     */
    public ArrayList<String> getDefaultCSTracks(){
        return defaultCSTracks;
    }

    /**
     * @param defaultCSTracks the defaultCSTracks to set
     */
    public void setDefaultCSTracks(ArrayList<String> defaultCSTracks){
        this.defaultCSTracks = defaultCSTracks;
    }

    /**
     * @return the defaultSETracks
     */
    public ArrayList<String> getDefaultSETracks(){
        return defaultSETracks;
    }

    /**
     * @param defaultSETracks the defaultSETracks to set
     */
    public void setDefaultSETracks(ArrayList<String> defaultSETracks){
        this.defaultSETracks = defaultSETracks;
    }

    /**
     * @return the defaultLeveling
     */
    public ArrayList<String> getDefaultLeveling(){
        return defaultLeveling;
    }

    /**
     * @param defaultLeveling the defaultLeveling to set
     */
    public void setDefaultLeveling(ArrayList<String> defaultLeveling){
        this.defaultLeveling = defaultLeveling;
    }

    /**
     * @return the finalDataList
     */
    public ArrayList<ArrayList<ArrayList<String>>> getFinalDataList(){
        return finalDataList;
    }

    /**
     * @param finalDataList the finalDataList to set
     */
    public void setFinalDataList(ArrayList<ArrayList<ArrayList<String>>> finalDataList){
        this.finalDataList = finalDataList;
    }
    
}