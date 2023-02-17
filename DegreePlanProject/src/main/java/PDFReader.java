import java.io.*;
import java.util.*;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

/**
 * This class is used to read-in graduate student transcripts from PDF file format. 
 * 
 */
public class PDFReader{
    private static final String FILE_EXTENSION = ".pdf";
    Scanner scanner = new Scanner(System.in);
    private String fileName; // Variable holds the name of the PDF file   
    private String readInPDF; // Variable holds the entire content of the fileName specified PDF
    
    /**
     * Constructor 
     */
    public PDFReader(){
        setFileName(this.fileName);  
        readPDF(this.fileName); 
        scanner.close(); // Close Scanner
    } 

    /**
     * @return the readInPDF
     */
    public String getReadInPDF(){
        return readInPDF;
    }

    /**
     * @param readInPDF the readInPDF to set
     */
    public void setReadInPDF(String readInPDF){
        this.readInPDF = readInPDF;
    }
    
    /**
     * @return the fileName
     */
    public String getFileName(){
        return fileName;
    }

    /**
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName){
        while (true) 
        {
            System.out.println("Enter filename (if you wish to exit application type EXIT): ");
            String inputFileName = scanner.nextLine();  // Read user input
            
            try {
                this.fileName = validateFile(inputFileName); // Method call and assignment
                break; // Exit the loop if the input is valid
            } catch (IllegalArgumentException e){
                System.out.println();
                System.out.println(e.getMessage());
            }
        }
    }
    
    /**
     * @param inputFileName
     * @throws IllegalArgumentException if the file name is invalid
     * @return 
     * This method takes the users input for the PDF file name and validates that the name of said file is a valid one. 
     * Application terminates if "EXIT" (case sensitive) is entered. 
     */
    public String validateFile(String inputFileName) throws IllegalArgumentException{ 
        if (inputFileName.contains("EXIT")) 
        {
            System.out.println();
            System.out.println("EXIT has been entered, application has terminated. Goodbye!");
            System.exit(0);
        } 
        else if (inputFileName.endsWith(FILE_EXTENSION)) 
        {
            // Check that input contains only valid characters for PDF file names
            if (inputFileName.matches("^(?!.*[/\\\\:*?\"<>|])[a-zA-Z0-9 _(),.-]+\\.pdf$")) 
            {
                return inputFileName;
            }
        } 
        else 
        {
            // Add ".pdf" extension if it is missing
            inputFileName += FILE_EXTENSION;
            // Check that input contains only valid characters for PDF file names
            if (inputFileName.matches("^(?!.*[/\\\\:*?\"<>|])[a-zA-Z0-9 _(),.-]+\\.pdf$")) 
            {
                return inputFileName;
            }
        }

        throw new IllegalArgumentException("Invalid PDF file name. Please enter a valid PDF file name.");
    }
    
    /**
     * @param fileName 
     * This method is used to read-in the fileName specified PDF.  
     */
    public void readPDF(String fileName){
        try (PDDocument document = PDDocument.load(new File(fileName))){
            // Extract the text from the PDF file
            PDFTextStripper textStripper = new PDFTextStripper();
            setReadInPDF(textStripper.getText(document));
            
            // Print the extracted text
            System.out.println(getReadInPDF());
        } catch (IOException e){
            System.out.println();
            System.out.println("An error has occurred! Most likely you have entered a filename of a file that doesn't exist or cannot be found.");
            System.out.println("Filename in question is: " + getFileName());
            
            // Gives user another chance to enter a filename of a file that does exist and can be found. User is also given chance to end application.
            setFileName(this.fileName);
            readPDF(this.fileName);   
        } catch (NullPointerException e){
            System.out.println();
            System.out.println("An error has occurred! Filename is null.");
            System.out.println("Application has terminated due to error. Goodbye!");
            System.exit(0);
        } finally{
            scanner.close(); // Close Scanner
        }
    }
}

