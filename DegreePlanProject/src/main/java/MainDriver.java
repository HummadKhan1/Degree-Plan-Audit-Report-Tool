/**
 * This class is the main driver of the application  
 */
public class MainDriver{
    public static void main(String[] args){
        do {
            PDFReader pdfReader = new PDFReader();
            GraduateStudent gradStudent = new GraduateStudent(pdfReader.getReadInPDF());
        } while (true);
   
    }
}
