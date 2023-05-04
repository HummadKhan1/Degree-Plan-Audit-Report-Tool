import java.io.IOException;
import javax.swing.JOptionPane;

/**
 * This class is the main driver of the application. 
 * 
 */
public class MainDriver{
    public static void main(String[] args) throws InterruptedException, IOException{
        int chosenOption = 0;
        
        do {
            FileSearchWindow fileSearchWindow = new FileSearchWindow();
            fileSearchWindow.getLatch().await(); // Used to make the program wait until latch has been released 

            FileReader fileReader = new FileReader(fileSearchWindow.getFilePath());

            if (fileSearchWindow.getFilePath().contains(".pdf")) // Executes if the transcript is the basis 
            {
                ParsingAlgorithms parse = new ParsingAlgorithms();
                parse.parseTranscript(fileReader.getReadInPDF());
                parse.handleCourseRepeats();
                parse.handleTransferType();

                parse.parseDefaultTracks(fileReader.getReadInTxt());
                parse.parseDefaultLeveling(fileReader.getReadInTxt());
                parse.parseDefaultCourses(fileReader.getReadInTxt());

                PreViewWindow preViewWindow = new PreViewWindow(parse.getCoursesArray(), parse.getDefaultCSTracks(), parse.getDefaultSETracks(), parse.getDefaultLeveling(), parse.getDefaultCoursesMap());
                preViewWindow.getLatch().await(); // Used to make the program wait until latch has been released  

                parse.setFinalDataList(preViewWindow.getFinalDataList());
                parse.populateDegreePlanArrayLists();

                parse.mergeLists(); 

                MakeTXTFile makeTXT = new MakeTXTFile(parse.getName(), parse.getID(), parse.getProgram(), parse.getAppliedIn(), parse.getMajor(), 
                        parse.getDefaultCSTracks(),parse.getDefaultSETracks(), parse.getDefaultLeveling(), parse.getCoursesArray(),
                        fileSearchWindow.getFilePath());
            } 
            else // Executes if the student specific default TXT file is the basis 
            {
                ParsingAlgorithms parse = new ParsingAlgorithms();
                parse.parseTXT(fileReader.getReadInTxt());
                parse.handleCourseRepeats();
                parse.handleTransferType();

                parse.parseDefaultTracks(fileReader.getReadInTxt());
                parse.parseDefaultLeveling(fileReader.getReadInTxt());
                parse.parseDefaultCourses(fileReader.getReadInTxt());

                PreViewWindow preViewWindow = new PreViewWindow(parse.getCoursesArray(), parse.getDefaultCSTracks(), parse.getDefaultSETracks(), parse.getDefaultLeveling(), parse.getDefaultCoursesMap());
                preViewWindow.getLatch().await(); // Used to make the program wait until latch has been released  

                parse.setFinalDataList(preViewWindow.getFinalDataList());
                parse.populateDegreePlanArrayLists();

                parse.mergeLists();
                
                MakeTXTFile makeTXT = new MakeTXTFile(parse.getName(), parse.getID(), parse.getProgram(), parse.getAppliedIn(), parse.getMajor(),
                        parse.getDefaultCSTracks(), parse.getDefaultSETracks(), parse.getDefaultLeveling(), parse.getCoursesArray(),
                        fileSearchWindow.getFilePath());
            }
            chosenOption = Continue_or_Not(); 
            
        } while (chosenOption != 2);
    }
    
    /**
     * Method is used to display a dialogue box where the user can select if they wish to continue or not. 
     * 
     * @return an int representing the chosen option: [1] Yes, [2] No
     */
    public static int Continue_or_Not(){
        String[] options = {"[1] YES", "[2] NO"};
        
        // Create dialog box 
        int choice = JOptionPane.showOptionDialog(null, "Would you like to continue?",
                "Options", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options, options[0]);

        // Executes if the user closes the dialog box, result is exit program
        if (choice == JOptionPane.CLOSED_OPTION) 
        {
            System.exit(0);
        } 
        return choice + 1; 
    }
}