import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to make the student specific default TXT file. 
 * 
 */
public class MakeTXTFile{
    private String name; // Variable holds the name of the graduate student 
    private String ID; // Varaible holds the ID of the graduate student 
    private String program; // Variable holds the academic program the graduate student is in 
    private String appliedIn; // Variable holds the semester addmited into program 
    private String major; // Variable holds the major the graduate student is in (Computer Science or Software Engineering)
    private ArrayList<String> defaultCSTracks = new ArrayList<>(); // ArrayList will store the CS tracks available 
    private ArrayList<String> defaultSETracks = new ArrayList<>(); // ArrayList will store the SE tracks available 
    private ArrayList<String> defaultLeveling = new ArrayList<>(); // ArrayList will store the leveling courses/pre-requisites that are possible 
    private ArrayList<Course> coursesArray; // ArrayList of Courses, valid courses read-in from transcriptData are stored here 
    private String filePath; // Variable holds the absolute file path of the selected transcript or student specific default TXT file 
    
    public MakeTXTFile(String name, String ID, String program, String appliedIn, String major,
            ArrayList<String> defaultCSTracks, ArrayList<String> defaultSETracks, ArrayList<String> defaultLeveling, ArrayList<Course> coursesArray,
            String filePath){
        this.name = name;
        this.ID = ID;
        this.program = program;
        this.appliedIn = appliedIn;
        this.major = major;
        this.defaultCSTracks = defaultCSTracks;
        this.defaultSETracks = defaultSETracks;
        this.defaultLeveling = defaultLeveling;
        this.coursesArray = new ArrayList<>(coursesArray); 
        this.filePath = filePath; 
        prepareData();
    }
    
    /**
     * This method prepares the data by copying and modifying a file.
     * 
     */
    public void prepareData(){
        String sourceFileName = "Default.txt";
        String destinationFileName = name + "'s Default.txt";

        File originalFile = new File(filePath);
        String originalFileDirectory = originalFile.getParent();
        
        //System.out.println("parent: " + originalFileDirectory);
        
        try {
            copyAndModifyFile(sourceFileName, destinationFileName, originalFileDirectory);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Method copies and modifies the content of a file, starting after a specific line.
     *
     * @param sourceFileName the name of the source file
     * @param destinationFileName the name of the destination file
     * @throws IOException if an I/O error occurs
     */
    public void copyAndModifyFile(String sourceFileName, String destinationFileName, String originalFileDirectory) throws IOException{
        String specificLine = "*********************DO NOT REMOVE THIS LINE 2*********************";
        boolean startCopying = false;

        // Read the content of the source file
        List<String> originalLines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(sourceFileName))){
            String line;
            while ((line = reader.readLine()) != null) 
            {
                if (line.equals(specificLine))
                {
                    startCopying = true;
                } 
                else if (startCopying)
                {
                    originalLines.add(line);
                }
            }
        }

        // Add courses with proper formatting
        List<String> newLines = new ArrayList<>();
        newLines.add("Name: " + name);
        newLines.add("Student ID: " + ID);
        newLines.add("Program: " + program);
        newLines.add("Applied In: " + appliedIn);
        newLines.add("Major: " + major + "\n");
        
        newLines.add("Courses Taken:");
        
        for (Course course : coursesArray)
        {
            int length = course.getClassName().length(); 
            int amountOfSpaces = 67 - length; 
            String spaces = ""; 
            int ix = 0; 
            while (ix < amountOfSpaces)
            {
                spaces += " "; 
                ix++; 
            }
            
                    
            newLines.add(course.getClassName() + spaces + course.getDepartment() + " " + course.getCourseNumber() + " " + 
                    course.getSemester() + " [" + course.getTransferType() + "] " + course.getLetterGrade());
        }
        
        newLines.add("\nComputer Science Tracks:");
        for (String track : defaultCSTracks) 
        {
            newLines.add(track);
        }
                
        newLines.add("\nSoftware Engineering Tracks:");
        for (String track : defaultSETracks)
        {
            newLines.add(track);
        }
        
        newLines.add("\n*********************DO NOT REMOVE THIS LINE 1*********************\n");
        
        newLines.add("Leveling Courses/Pre-requisites Assigned at Admission:");
        for (String leveling : defaultLeveling) 
        {
            newLines.add(leveling);
        }
        
        newLines.add("\n*********************DO NOT REMOVE THIS LINE 2*********************");

        // Combine new lines with original lines
        List<String> combinedLines = new ArrayList<>();
        combinedLines.addAll(newLines);
        combinedLines.addAll(originalLines);

        // Create the destination file in the same directory as the selected transcript or student specific default TXT file
        File destinationFile = new File(originalFileDirectory, destinationFileName);
        if (!destinationFile.exists()) 
        {
            destinationFile.createNewFile();
        }

        // Write the modified content to the destination file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(destinationFile))){
            for (String line : combinedLines)
            {
                writer.write(line);
                writer.newLine();
            }
        }
    }
}