import java.io.*;
import java.util.*;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

/**
 * This class is used to read-in graduate student transcripts from PDF file format. 
 * This class extends the Parent class FileDropWindow 
 */
public class PDFReader extends FileDropWindow{
    private String readInPDF; // Variable holds the entire content of the fileName specified PDF
    
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
     * @param filePath 
     * This method is used to read-in the filePath specified PDF.  
     */
    public void readPDF(String filePath){
        try (PDDocument document = PDDocument.load(new File(filePath))){
            // Extract the text from the PDF file
            PDFTextStripper textStripper = new PDFTextStripper();
            setReadInPDF(textStripper.getText(document));
            
        } catch (IOException e){
            System.out.println();
            System.out.println("An error has occurred! Most likely you have entered a file path to a file that doesn't exist or cannot be found.");
            System.out.println("File path in question is: " + getFileDirectory());
            
            // Gives user another chance to enter a filename of a file that does exist and can be found. User is also given chance to end application.
//            setFilePath(this.getFilePath());
//            readPDF(this.getFilePath());   
        } catch (NullPointerException e){
            System.out.println();
            System.out.println("An error has occurred! Filename is null.");
            System.out.println("Application has terminated due to error. Goodbye!");
            System.exit(0);
        } 
    }
}

