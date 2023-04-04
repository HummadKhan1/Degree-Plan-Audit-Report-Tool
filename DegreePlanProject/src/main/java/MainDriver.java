import java.io.IOException;
//import java.util.*;

/**
 * This class is the main driver of the application. 
 * 
 */
public class MainDriver{
    public static void main(String[] args) throws InterruptedException, IOException{
        FileSearchWindow window = new FileSearchWindow();
        window.getLatch().await(); // Used to make the program wait until latch has been released 
        
        FileReader fileReader = new FileReader(window.getFilePath());
        
        ParsingAlgorithms parseTranscript = new ParsingAlgorithms();
        parseTranscript.parseTranscript(fileReader.getReadInPDF());
        //parseTranscript.handleCourseRepeats();
        //parseTranscript.handleTransferType();
        parseTranscript.printCourses();
    }
}