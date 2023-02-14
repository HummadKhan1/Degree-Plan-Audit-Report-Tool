import java.io.*;
import java.util.*;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

/**
 * This class is used to read-in graduate student transcripts from pdf file format   
 */
public class PDFReader{
    private String fileName; // Variable holds the name of the pdf file 
    
    /**
    * Constructor 
    */
    public PDFReader(){
        setFileName(this.fileName); 
        ReadPDF(this.fileName);
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
        Scanner scanner = new Scanner(System.in);  // Create a Scanner object
        System.out.println("Enter filename (without the file extension): ");
        
        String possibleName = scanner.nextLine();  // Read user input
        scanner.close(); // Close Scanner 
        this.fileName = validateFile(possibleName); // Method call and assignment 
        
        //if (this.fileName.equals("END!"))
    }
    
    public String validateFile(String fName){
        Scanner scanner = new Scanner(System.in);  // Create a Scanner object 

        while (fName.contains(" ") || fName.contains(".pdf")) 
        {
            System.out.println("The filename you have entered contains whitespace and/or the file extension. Please re-enter the filename: ");
            fName = scanner.nextLine(); // Read user input
        }
       
        scanner.close(); // Close Scanner 
        fName += ".pdf";
        return fName;  
    }
    
    public void ReadPDF(String fileName){
        try {
            // Load the PDF file
            File file = new File(fileName);
            PDDocument document = PDDocument.load(file);

            // Extract the text from the PDF file
            PDFTextStripper textStripper = new PDFTextStripper();
            String text = textStripper.getText(document);

            // Print the extracted text
            System.out.println(text);

            // Close the PDF document
            document.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    
}

