import java.io.*;
import java.util.*;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

/**
 * This class is used to read-in graduate student transcripts from PDF file format. 
 * 
 */
public class PDFReader{
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
     * This method is used to read-in the first PDF file in a specified
     * directory.
     *
     * @param fileDirectory the directory containing the PDF file to be read
     */
    public void readPDF(String fileDirectory) {
        try {
            // Create a File object for the specified directory
            File dir = new File(fileDirectory);

            // List all files in the directory that have a .pdf file extension
            File[] files = dir.listFiles((d, name) -> name.endsWith(".pdf"));

            // If there is at least one PDF file in the directory, read in the first one
            if (files != null && files.length > 0) {
                PDDocument document = PDDocument.load(files[0]);
                PDFTextStripper textStripper = new PDFTextStripper();
                setReadInPDF(textStripper.getText(document));
                document.close();
            } else {
                // If there are no PDF files in the directory, inform the user
                System.out.println("No PDF files found in the specified directory: " + fileDirectory);
            }
        } catch (IOException e) {
            // Handle the case where the specified file directory is invalid or cannot be found
            System.out.println();
            System.out.println("An error has occurred! Most likely you have entered a file directory that doesn't exist or cannot be found.");
            System.out.println("Directory in question is: " + fileDirectory);
        } catch (NullPointerException e) {
            // Handle the case where the file directory is null
            System.out.println();
            System.out.println("An error has occurred! File directory is null.");
            System.out.println("Application has terminated due to error. Goodbye!");
            System.exit(0);
        }
    }
}

