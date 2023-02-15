import java.io.*;
import java.util.*;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

/**
 * This class is used to read-in graduate student transcripts from pdf file format   
 */
public class PDFReader{
    private String fileName; // Variable holds the name of the pdf file 
    Scanner scanner = new Scanner(System.in);  // Create a Scanner object 
    private String readInPDF; // This variable holds the entire content of the fileName specified pdf
    
    /**
     * Constructor 
     */
    public PDFReader(){
        setFileName(this.fileName); // Method call 
        ReadPDF(this.fileName); // Method call 
         
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
        System.out.println("Enter filename (if you wish to exit application type EXIT): ");
        
        String possibleName = scanner.nextLine();  // Read user input
        this.fileName = validateFile(possibleName); // Method call and assignment 
    }
    
    /**
     * @param fName
     * @return 
     * This method takes the users input for fileName and validates that the name of said file is a valid one. 
     * Program can also terminate in "EXIT" (case sensitive) is entered. 
     */
    public String validateFile(String fName){
        if (fName.contains("EXIT")) // if-statement executes if fName constains "EXIT" 
        {
            System.out.println("EXIT has been entered. Goodbye");
            System.exit(0); 
        }
        else if (fName.contains(".pdf")) // if-statement executes if fName constains ".pdf"
        {
            return fName; 
        }
        else if (!(fName.contains(".pdf"))) // if-statement executes if fName does not constain ".pdf"
        {
            return fName += ".pdf";
        }

        return "ERROR: validateFile method didnt work";  
    }
    
    /**
     * @param fileName 
     * This method is used to read-in the fileName specified pdf.  
     */
    public void ReadPDF(String fileName){
        try {
            // Load the PDF file
            File file = new File(fileName);
            PDDocument document = PDDocument.load(file);

            // Extract the text from the PDF file
            PDFTextStripper textStripper = new PDFTextStripper();
            setReadInPDF(textStripper.getText(document));

            // Print the extracted text
            System.out.println(getReadInPDF());

            // Close the PDF document
            document.close();
        } catch (IOException e){
            System.out.println("An error has occurred! Most likely you have entered a filename that doesn't exist or cannot be found.");
            System.out.println("Filename in question is: " + getFileName());
            PDFReader pdfReader = new PDFReader();  
        }
    }
}

