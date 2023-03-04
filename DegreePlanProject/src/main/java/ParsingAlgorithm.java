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
    private ArrayList<Course> courses = new ArrayList<>(); // ArrayList of Courses 

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
     * @return the courses
     */
    public ArrayList<Course> getCourses(){
        return courses;
    }
    
    /**
     * Method is used to parse through the transcript data and extract relevant information 
     * 
     * @param transcriptData the transcript content to be parsed 
     */
    public void parseTranscript(String transcriptData){
        String semester = ""; // Variable holds the semester the course was taken in 
        
        try (Scanner scanner = new Scanner(transcriptData)){
            // Regular expressions to match patterns in transcript data
            Pattern namePattern = Pattern.compile("Name:\\s+(\\w+)\\s+(\\w+)");
            Pattern idPattern = Pattern.compile("Student ID:\\s+(\\w+)");
            Pattern semesterPattern = Pattern.compile("(\\d{4})\\s+(Spring|Summer|Fall)");
            
            while (scanner.hasNextLine()) // While-loop iterates through the entirety of the transcript data
            {
                String line = scanner.nextLine().trim(); // Store a line within the transcript data 
                
                // Regular expression matchers to extract relevant information from transcript data
                Matcher nameMatcher = namePattern.matcher(line);
                Matcher idMatcher = idPattern.matcher(line);
                Matcher semesterMatcher = semesterPattern.matcher(line);

                if (nameMatcher.matches()) 
                {
                    setFirstName(nameMatcher.group(1));
                    setLastName(nameMatcher.group(2));
                } 
                else if (idMatcher.matches())
                {
                    setID(idMatcher.group(1));
                } 
                else if (semesterMatcher.matches()) 
                {
                    semester = semesterMatcher.group(1) + " " + semesterMatcher.group(2); 
                }
                else if (line.startsWith("CS") || line.startsWith("ECSC") || line.startsWith("SE"))
                {
                    String[] tokens = line.split("\\s+"); // Split the String line into array of Strings with separator as space or multiple spaces
                    String department = tokens[0]; 
                    String courseNumber = tokens[1];
                    
                    if (Integer.parseInt(courseNumber) >= 5000) // Check to see if the course number is greater than 5000
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
                        Course course = new Course(department, courseNumber, title, attemptedCredits, earnedCredits, letterGrade, points, semester);
                        courses.add(course); // Add course to arrayList
                    }
                }
            }
        }
    }

    /**
     * Helper method that checks if str is a number or not
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
    
public void removeDuplicates() {
    HashMap<String, Course> courseMap = new HashMap<>(); // Key: Course key, Value: Course

    for (Course course : courses) {
        String courseKey = course.getDepartment() + course.getCourseNumber();
        Course existingCourse = courseMap.get(courseKey);

        if (existingCourse == null ||
            (!existingCourse.getLetterGrade().equals("") &&
             (course.getLetterGrade().equals("") ||
              course.getSemester().compareTo(existingCourse.getSemester()) > 0))) {
            courseMap.put(courseKey, course);
        }
    }

    courses = new ArrayList<>(courseMap.values()); // Update the ArrayList of courses to remove duplicates
}
    
    public void printCourses(){
        System.out.println("Courses:");
        for (Course course : courses) 
        {
            System.out.println(course.getDepartment() + " " + course.getCourseNumber() + ": " + course.getClassName());
            System.out.println("Attempted Points: " + course.getAttemptedCredits());
            System.out.println("Earned Points: " + course.getEarnedCredits());
            System.out.println("Letter Grade: " + course.getLetterGrade());
            System.out.println("Class Points: " + course.getPoints());
            System.out.println("Semester: " + course.getSemester());
            System.out.println();
        }
    }
}
