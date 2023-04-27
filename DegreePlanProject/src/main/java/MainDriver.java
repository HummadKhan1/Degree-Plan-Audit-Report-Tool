import java.io.IOException;

/**
 * This class is the main driver of the application. 
 * 
 */
public class MainDriver{
    public static void main(String[] args) throws InterruptedException, IOException{
        FileSearchWindow fileSearchWindow = new FileSearchWindow();
        fileSearchWindow.getLatch().await(); // Used to make the program wait until latch has been released 
        
        FileReader fileReader = new FileReader(fileSearchWindow.getFilePath());
        
        ParsingAlgorithms parse = new ParsingAlgorithms();
        parse.parseTranscript(fileReader.getReadInPDF());
        parse.handleCourseRepeats();
        parse.handleTransferType();
        
        parse.parseDefaultTracks(fileReader.getReadInTxt());
        parse.parseDefaultLeveling(fileReader.getReadInTxt());      
        parse.parseDefaultCourses(fileReader.getReadInTxt());
        
        PreViewWindow preViewWindow = new PreViewWindow(parse.getCoursesArray(), parse.getDefaultCSTracks(), parse.getDefaultSETracks(), parse.getDefaultLeveling(),parse.getDefaultCoursesMap()); 
        preViewWindow.getLatch().await(); // Used to make the program wait until latch has been released  
        
        parse.setFinalDataList(preViewWindow.getFinalDataList());
        parse.populateDegreePlanArrayLists();
        
        //MakeTXTFile makeTxtFile = new MakeTXTFile();
        
        System.exit(0); 
        
    }
}