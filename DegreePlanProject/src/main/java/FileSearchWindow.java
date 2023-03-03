import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is used to launch the GUI that allows the user to search and select a desired file. 
 * 
 */
public class FileSearchWindow extends JFrame{
    JButton searchButton; 
    private String filePath; // Variable will hold the absolute file path of the selected transcript 

    /**
     * @return the filePath
     */
    public String getFilePath(){
        return filePath;
    }
    
    /**
     * @param file the file to set the file path from
     */
    public void setFilePath(File file){
        this.filePath = file.getAbsolutePath();
    }

    /**
     * Constructor.
     *
     */
    public FileSearchWindow(){
        // JFrame setup
        JFrame frame = new JFrame("Degree Plan/Audit Tool");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setLayout(new FlowLayout());
        
        // Search button setup
        searchButton = new JButton("Search for File");
        searchButton.setFocusable(false); // Search button cannot be selected with the keyboard
        frame.add(searchButton); // Add search button to JFrame content pane 
        
        searchButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent evt){
                if (evt.getSource() == searchButton) // Check if the event source is the searchButton having been pressed
                {
                    // Allow the user to select a file to upload 
                    JFileChooser fileUpload = new JFileChooser(); 
                    fileUpload.setCurrentDirectory(new File(".")); // The current directory is set using setCurrentDirectory()
                    int returnVal = fileUpload.showOpenDialog(null); // Method call is to open file chooser dialog box. Method returns 'JFileChooser.APPROVE_OPTION'  
                                                                     // if user selects a file and clicks the "Open" button.
                    if (returnVal == JFileChooser.APPROVE_OPTION) 
                    {
                        setFilePath(fileUpload.getSelectedFile());
                        try{
                            FileReader fileReader = new FileReader(getFilePath());
                            frame.dispose(); // Close frame
                        } catch (IOException ex){
                            Logger.getLogger(FileSearchWindow.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        });
        
        frame.setVisible(true);
    }
}
