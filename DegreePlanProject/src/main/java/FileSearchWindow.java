import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.awt.event.ActionEvent;

public class FileSearchWindow extends JFrame implements ActionListener {
    
    JButton b;//create button  
    JButton existingSutdent;
    JLabel label;
    String file_directory; // change variable name to file_directory

    public FileSearchWindow(){
        JFrame frame = new JFrame("Degree Plan/Audit Tool");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setVisible(true);
        frame.setLayout(new FlowLayout());

        b = new JButton("Create New Degree Plan/Audit");
        existingSutdent = new JButton("Existing Degree Plan/Audit");
        
        //b.setBounds(100, 100, 225, 50);
        b.setFocusable(false);
        b.addActionListener(this);
        
        //existingSutdent.setBounds(100, 200, 225, 50);
        existingSutdent.setFocusable(false);

        label = new JLabel("Empty");
                
        frame.add(b);//adding button on frame
        frame.add(existingSutdent);
        frame.add(label);

    }

    public void actionPerformed(ActionEvent evt){
        if(evt.getSource() == b){
            // Second_Frame frame = new Second_Frame();
            // this.dispose();

            JFileChooser file_upload = new JFileChooser();
            file_upload.setCurrentDirectory(new File("."));

            int res = file_upload.showOpenDialog(null);

            if(res == JFileChooser.APPROVE_OPTION){
                
                this.file_directory = file_upload.getSelectedFile().getParent(); // change this line to set file_directory to the parent directory of the selected file
                PDFReader pdfReader = new PDFReader(); 
                pdfReader.readPDF(file_directory);
                label.setText(pdfReader.getReadInPDF());

                System.out.println(file_upload.getSelectedFile().getAbsolutePath());
            }
        }
    }

    public String getFileDirectory(){
        return file_directory;
    }

}
