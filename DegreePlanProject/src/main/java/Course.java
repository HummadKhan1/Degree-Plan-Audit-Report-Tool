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
    private String transferType; 
    private String levelingCourseDisposition = ""; // Variable could be initialized to "Completed", "Waived", "Not required by plan or electives", or "Other"
    private String degreePlanSection = ""; // Variable could be initialized to "Core Courses", "X of the Following Courses", or "Admission Prerequisites"
    private String repeatCourse = ""; // This variable contains the String "Repeat Excluded" if a course has been taken once before. Default is empty String
     
    /**
     * Constructor.
     * Only parameter not being assigned using this constructor is 'repeatCourse' of type String. 
     * 
     * @param department
     * @param courseNumber
     * @param className
     * @param attemptedCredits
     * @param earnedCredits
     * @param letterGrade
     * @param points 
     * @param semester
     * @param transferType
     */
    public Course(String department, String courseNumber, String className, double attemptedCredits, 
            double earnedCredits, String letterGrade, double points, String semester, String transferType){
    this.department = department;
    this.courseNumber = courseNumber;
    this.className = className;
    this.attemptedCredits = attemptedCredits;
    this.earnedCredits = earnedCredits;
    this.letterGrade = letterGrade;
    this.points = points;
    this.semester = semester;
    this.transferType = transferType; 
    }
    
    public Course(String department, String courseNumber, String className, String degreePlanSection){
        this.department = department;
        this.courseNumber = courseNumber;
        this.className = className;
        this.degreePlanSection = degreePlanSection;
    }
    
    /**
     * @return the department
     */
    public String getDepartment(){
        return department;
    }
    
    /**
     * @param department the department to set
     */
    public void setDepartment(String department){
        this.department = department;
    }
    
    /**
     * @return the courseNumber
     */
    public String getCourseNumber(){
        return courseNumber;
    }
    
    /**
     * @param courseNumber the courseNumber to set
     */
    public void setCourseNumber(String courseNumber){
        this.courseNumber = courseNumber;
    }
    
    /**
     * @return the className
     */
    public String getClassName(){
        return className;
    }
    
    /**
     * @param className the className to set
     */
    public void setClassName(String className){
        this.className = className;
    }

    /**
     * @return the attemptedCredits
     */
    public double getAttemptedCredits(){
        return attemptedCredits;
    }
    
    /**
     * @param attemptedCredits the attemptedCredits to set
     */
    public void setAttemptedCredits(double attemptedCredits){
        this.attemptedCredits = attemptedCredits;
    }

    /**
     * @return the earnedCredits
     */
    public double getEarnedCredits(){
        return earnedCredits;
    }
    
    /**
     * @param earnedCredits the earnedCredits to set
     */
    public void setEarnedCredits(double earnedCredits){
        this.earnedCredits = earnedCredits;
    }

    /**
     * @return the letterGrade
     */
    public String getLetterGrade(){
        return letterGrade;
    }
    
    /**
     * @param letterGrade the letterGrade to set
     */
    public void setLetterGrade(String letterGrade){
        this.letterGrade = letterGrade;
    }

    /**
     * @return the points
     */
    public double getPoints(){
        return points;
    }

    /**
     * @param points the points to set
     */
    public void setPoints(double points){
        this.points = points;
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
     * @return the transferType
     */
    public String getTransferType(){
        return transferType;
    }

    /**
     * @param transferType the transferType to set
     */
    public void setTransferType(String transferType){
        this.transferType = transferType;
    }
    
    /**
     * @return the repeat
     */
    public String getRepeatCourse(){
        return repeatCourse;
    }

    /**
     * @param repeat the repeat to set
     */
    public void setRepeatCourse(String repeatCourse){
        this.repeatCourse = repeatCourse;
    }

    /**
     * @return the levelingCourseDisposition
     */
    public String getLevelingCourseDisposition(){
        return levelingCourseDisposition;
    }

    /**
     * @param levelingCourseDisposition the levelingCourseDisposition to set
     */
    public void setLevelingCourseDisposition(String levelingCourseDisposition){
        this.levelingCourseDisposition = levelingCourseDisposition;
    }

    /**
     * @return the degreePlanSection
     */
    public String getDegreePlanSection(){
        return degreePlanSection;
    }

    /**
     * @param degreePlanSection the degreePlanSection to set
     */
    public void setDegreePlanSection(String degreePlanSection){
        this.degreePlanSection = degreePlanSection;
    }
}
