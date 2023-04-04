import java.io.*;

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
     * Constructor is used to read-in the file specified by the absolute file path.
     * If a PDF is read-in first then a TXT file named Default is also read-in. If a 
     * TXT file is read-in first then that is the only file that will be read-in by the program.
     * 
     * @param filePath the absolute file path of the PDF file to be read-in
     * @throws IOException
     */
    FileReader(String filePath) throws IOException{
        try{
            // Create a File object from the specified file path
            File file = new File(filePath);
            
            File txtFile; // Variable will only be used to open the Default.txt file 

            // Check if the file exists, is a file (not a directory), and has a ".pdf" extension
            if (file.exists() && file.isFile() && file.getName().endsWith(".pdf")) 
            {
                PDDocument document = PDDocument.load(file); // Load the PDF document 
                
                // Extract the text from the PDF file
                PDFTextStripper textStripper = new PDFTextStripper();
                setReadInPDF(textStripper.getText(document));
                
                document.close(); // Close the PDF document 
                
                // Open a TXT file named Default.txt
                txtFile = new File("Default.txt"); // File is located amongst the program folders 
                
                if (txtFile.exists() && txtFile.isFile() && txtFile.getName().endsWith(".txt")) // Check if the file exists, is a file (not a directory), and has a ".txt" extension 
                {
                    BufferedReader bufferedReader = new BufferedReader(new java.io.FileReader(txtFile));
                    StringBuilder sringBuilder = new StringBuilder();
                    String line;
                    
                    while ((line = bufferedReader.readLine()) != null)
                    {
                        sringBuilder.append(line);
                        sringBuilder.append(System.lineSeparator()); // Append newline character after each line
                    }
                    
                    bufferedReader.close(); // Close BufferedReader
                    setReadInTxt(sringBuilder.toString()); // Store the contents of the file
                } 
                else
                {
                    System.out.println("Default.txt file not found.");
                    System.out.println("Application has terminated due to error. Goodbye!");
                    System.exit(0);
                }
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
}

