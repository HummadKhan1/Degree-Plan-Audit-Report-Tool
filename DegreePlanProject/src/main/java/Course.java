/**
 * This class is used to store all the course specifications a graduate student has taken.
 * 
 */
public class Course{
    private String department;
    private String courseNumber;
    private String className; 
    private double attemptedCredits;
    private double earnedCredits;
    private String letterGrade;
    private double points;
    private String semester; 

    /**
     * @return the department
     */
    public String getDepartment(){
        return department;
    }

    /**
     * @return the courseNumber
     */
    public String getCourseNumber(){
        return courseNumber;
    }

    /**
     * @return the className
     */
    public String getClassName(){
        return className;
    }

    /**
     * @return the attemptedCredits
     */
    public double getAttemptedCredits(){
        return attemptedCredits;
    }

    /**
     * @return the earnedCredits
     */
    public double getEarnedCredits(){
        return earnedCredits;
    }

    /**
     * @return the letterGrade
     */
    public String getLetterGrade(){
        return letterGrade;
    }

    /**
     * @return the points
     */
    public double getPoints(){
        return points;
    }

    /**
     * @return the semester
     */
    public String getSemester(){
        return semester;
    }
    
    public void setSemester(String semester){
        this.semester = semester; 
    }
    
    /**
     * Constructor
     * @param department
     * @param courseNumber
     * @param className
     * @param earnedCredits
     * @param letterGrade
     * @param points 
     */
    public Course(String department, String courseNumber, String className, double attemptedCredits, 
            double earnedCredits, String letterGrade, double points, String semester){
    this.department = department;
    this.courseNumber = courseNumber;
    this.className = className;
    this.attemptedCredits = attemptedCredits;
    this.earnedCredits = earnedCredits;
    this.letterGrade = letterGrade;
    this.points = points;
    this.semester = semester;
    }
}
