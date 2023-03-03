import java.io.*;
import java.util.*;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

/**
 * This class is used to read-in files of different file types.
 * 
 */
public class FileReader{
    private String readInPDF; // Variable holds the entire content of the filePath specified PDF
    private String readInTxt; // Variable holds the entire content of the filePath specified Text file
    
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
     * @return the readInTxt
     */
    public String getReadInTxt(){
        return readInTxt;
    }

    /**
     * @param readInTxt the readInTxt to set
     */
    public void setReadInTxt(String readInTxt){
        this.readInTxt = readInTxt;
    }
    
    /**
     * Constructor is used to read-in the file specified by the absolute file path 
     * 
     * @param filePath the absolute file path of the PDF file to be read-in
     * @throws IOException
     */
    FileReader(String filePath) throws IOException{
        try{
            // Create a File object from the specified file path
            File file = new File(filePath);

            // Check if the file exists, is a file (not a directory), and has a ".pdf" extension
            if (file.exists() && file.isFile() && file.getName().endsWith(".pdf")) 
            {
                PDDocument document = PDDocument.load(file); // Load the PDF document 
                
                // Extract the text from the PDF file
                PDFTextStripper textStripper = new PDFTextStripper();
                setReadInPDF(textStripper.getText(document));
                
                document.close(); // Close the PDF document 
                System.out.println(getReadInPDF());
            } 
            else if (file.exists() && file.isFile() && file.getName().endsWith(".txt")) // Check if the file exists, is a file (not a directory), and has a ".txt" extension
            {
                
            }
            else 
            {
                System.out.println("No file found at the specified path: " + filePath);
            }
        } catch (NullPointerException e){ // Catch if filePath is null
            System.out.println();
            System.out.println("An error has occurred! File path is null.");
            System.out.println("Application has terminated due to error. Goodbye!");
            System.exit(0);
        }
    }
}

