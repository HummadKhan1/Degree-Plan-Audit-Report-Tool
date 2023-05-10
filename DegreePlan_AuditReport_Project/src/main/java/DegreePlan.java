/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author zypro
 */
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.pdfbox.contentstream.operator.text.SetFontAndSize;
import org.apache.poi.xwpf.usermodel.Borders;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.TableWidthType;
import org.apache.poi.xwpf.usermodel.TextAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVMerge;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STMerge;
import java.math.BigInteger;

public class DegreePlan {
    ArrayList<Section> sections;
    XWPFDocument document;
    XWPFTable table;
    ArrayList<XWPFTableRow> rows;
    ArrayList<String> headerTexts;
    PreViewWindow PVW;
    GraduateStudent Grad;

    public DegreePlan(ArrayList<Section> s, GraduateStudent grad, PreViewWindow pvw){
        Grad = grad;
        PVW = pvw;
        System.out.println(grad.getFirstName());
        sections = s;
        document = new XWPFDocument();
        table = document.createTable();
        table.setWidth(9500);
        createTableOutline(table);
        setHeaderTexts(grad);
        setHeader(table, table.getRow(0).getCell(0), grad, headerTexts);
        setCategories(table.getRow(1));
        /*
        table.getRow(0).getCell(0).setWidth("100%");
        System.out.println(table.getRow(0).getCell(0).getWidth());
        */
        
        int lastIndex = table.getNumberOfRows() - 1;
        for(int i = 0, j = 2; i < s.size(); i++){
            mergeCellHorizontally(table, j, 0, 4);
            table.getRow(j).getCell(0).setText(s.get(i).getTitle());
            setCellColor(table.getRow(j++));
            for(Course course : s.get(i).getCourses()){
                
                fillRow(course, table.getRow(j++));
            }
            lastIndex = j;
        }

        createFooter(table, table.getRow(lastIndex), lastIndex);
        for(int i = table.getRows().size()-1; i > 0; i--){
            if(table.getRow(i).getCell(0).getText() == ""){
                table.removeRow(i);
            }
        }
        
        
    }

    private void setHeaderTexts(GraduateStudent grad){
        headerTexts = new ArrayList<String>();
        headerTexts.add("Degree Plan");
        headerTexts.add("The University of Texas at Dallas");
        headerTexts.add("Masters in " + PVW.getSelectedTrack() );
        //headerTexts.add("Name of Student: " + grad.getFirstName() + "                 Fast Track: Y / N");
        
        String ft = "";
        String th = "";
        if(PVW.getFastTrackORThesis() == "Fast Track"){
            ft = "Y";
            th = "N";
        }
        else if(PVW.getFastTrackORThesis() == "Thesis"){
            ft = "N";
            th = "Y";
        }
        else{
            ft = th = "N";
        }
        
        
        
        
        
        setSpacing("Name of Student: " + grad.getParse().getName(), "Fast Track: " + ft, headerTexts, 85);
        //headerTexts.add("Fast Track");
        //headerTexts.add("Student ID Number: " + grad.getID() + "                   Thesis: Y / N");
        setSpacing("Student ID Number: " + grad.getParse().getID(), "Thesis: " + th, headerTexts, 70);
        //headerTexts.add("Thesis");
        headerTexts.add("Semester Admitted to program: " + grad.getParse().getAppliedIn());
        headerTexts.add("Anticipated Graduation: " + getGradDate(grad));
    }
    
    public String getGradDate(GraduateStudent grad){
        String apply = grad.getParse().getAppliedIn();
        String year = apply.substring(0,4);
        String sem = apply.substring(4);
        int yr = Integer.parseInt(year);
        yr = yr + 2;
        return "" + yr + sem;
        
        
        
    }

    public void outputDoc(){
        String s = Grad.getPath() +"\\"+ Grad.getParse().getName() + "'s Degree Plan.docx";
        File output = new File(s);
        System.out.println(output.getAbsolutePath());
        //FileOutputStream out;
        try ( FileOutputStream out = new FileOutputStream(output)) {
            document.write(out);
            out.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        


    }
    public void fillRow(Course c, XWPFTableRow r){
        r.getCell(0).setText(c.getClassName());
        r.getCell(1).setText(c.getCourseNumber());
        r.getCell(2).setText(c.getSemester());
        r.getCell(3).setText(c.getTransferType());
        r.getCell(4).setText(c.getLetterGrade());

    }
    public static void setCellColor(XWPFTableRow row){
        row.getCell(0).setColor("d4d404");
        System.out.println("Changed color to yellow");
    }
    public static void addCells(XWPFTableRow row , int x){
        for (int i = 0; i < x ; i++){
            row.addNewTableCell();
        }
    }
    public static void setFont(XWPFRun run, String fontFamily, int size){
        run.setFontFamily(fontFamily);
        run.setFontSize(size);
    }

    public static void setHeader(XWPFTable table, XWPFTableCell cell, GraduateStudent grad, ArrayList<String> texts){
        mergeCellHorizontally(table, 0, 0, 4);
        XWPFParagraph firstLine= cell.getParagraphArray(0);
        firstLine.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun firstRun = firstLine.createRun();
        firstRun.setFontSize(16);
        firstRun.setText(texts.get(0));
        firstRun.setFontFamily("Calibre Light");

        for(int i = 1; i < texts.size(); i++){
            XWPFParagraph header = cell.addParagraph();
            if(i <= 2)
                header.setAlignment(ParagraphAlignment.CENTER);
            else
                header.setAlignment(ParagraphAlignment.LEFT);

            XWPFRun run = header.createRun();
            run.setFontFamily("Calibre Light");
            run.setFontSize(16);
            run.setText(texts.get(i));
        }
    }

    private static void setSpacing(String text, String addOn, ArrayList<String> array, int spacing){
        int size = text.length();
        int sizeAO = addOn.length();

        while((size + sizeAO) < spacing){
            text = text + " ";
            size = text.length();
        }
        array.add(text + addOn);
    }

    private static void setCategories(XWPFTableRow row){
        row.getCell(0).setText("Course Title");
        row.getCell(0).getParagraphArray(0).setAlignment(ParagraphAlignment.CENTER);
        row.getCell(1).setText("Course Number");
        row.getCell(1).getParagraphArray(0).setAlignment(ParagraphAlignment.CENTER);
        row.getCell(2).setText("UTD Semester");
        row.getCell(2).getParagraphArray(0).setAlignment(ParagraphAlignment.CENTER);
        row.getCell(3).setText("Transfer");
        row.getCell(3).getParagraphArray(0).setAlignment(ParagraphAlignment.CENTER);
        row.getCell(4).setText("Grade");
        row.getCell(4).getParagraphArray(0).setAlignment(ParagraphAlignment.CENTER);

    }

    //Creates all rows and cells but is empty
    private static void createTableOutline(XWPFTable table){
        XWPFTableRow row = table.getRow(0);
        XWPFTableCell cell2 = row.addNewTableCell();
        XWPFTableCell cell3 = row.addNewTableCell();
        XWPFTableCell cell4 = row.addNewTableCell();
        XWPFTableCell cell5 = row.addNewTableCell();
        for(int i = 0; i < 34; i++){
            XWPFTableRow rowT = table.createRow();
        }
    }

    static void mergeCellVertically(XWPFTable table, int col, int fromRow, int toRow) {
        for (int rowIndex = fromRow; rowIndex <= toRow; rowIndex++) {
            XWPFTableCell cell = table.getRow(rowIndex).getCell(col);
            CTVMerge vmerge = CTVMerge.Factory.newInstance();
            if (rowIndex == fromRow) {
                // The first merged cell is set with RESTART merge value
                vmerge.setVal(STMerge.RESTART);
            } else {
                // Cells which join (merge) the first one, are set with CONTINUE
                vmerge.setVal(STMerge.CONTINUE);
                // and the content should be removed
                for (int i = cell.getParagraphs().size(); i > 0; i--) {
                    cell.removeParagraph(0);
                }
                cell.addParagraph();
            }
            // Try getting the TcPr. Not simply setting an new one every time.
            CTTcPr tcPr = cell.getCTTc().getTcPr();
            if (tcPr == null)
                tcPr = cell.getCTTc().addNewTcPr();
            tcPr.setVMerge(vmerge);
        }
    }

    static void mergeCellHorizontally(XWPFTable table, int row, int fromCol, int toCol) {
        XWPFTableCell cell = table.getRow(row).getCell(fromCol);
        // Try getting the TcPr. Not simply setting an new one every time.
        CTTcPr tcPr = cell.getCTTc().getTcPr();
        if (tcPr == null) tcPr = cell.getCTTc().addNewTcPr();
        // The first merged cell has grid span property set
        if (tcPr.isSetGridSpan()) {
         tcPr.getGridSpan().setVal(BigInteger.valueOf(toCol-fromCol+1));
        } else {
         tcPr.addNewGridSpan().setVal(BigInteger.valueOf(toCol-fromCol+1));
        }
        // Cells which join (merge) the first one, must be removed
        for(int colIndex = toCol; colIndex > fromCol; colIndex--) {
         table.getRow(row).getCtRow().removeTc(colIndex);
         table.getRow(row).removeCell(colIndex);
        }
       }

    static void createFooter(XWPFTable table, XWPFTableRow lastRow, int position){
        lastRow.setHeight(2000);
        mergeCellHorizontally(table, position, 0, 4);
        mergeCellHorizontally(table, position + 1, 0, 4);

        XWPFParagraph firstParegraph = lastRow.getCell(0).getParagraphArray(0);
        firstParegraph.setAlignment(ParagraphAlignment.CENTER);
        firstParegraph.setVerticalAlignment(TextAlignment.TOP);
        XWPFRun firstRun = firstParegraph.createRun();
        firstRun.setText("* May include any 6000 or 7000 level CS course without prior permission *");
        firstRun.addBreak();
        firstRun.addBreak();
        firstRun.addBreak();
        firstRun.addBreak();

        XWPFParagraph secondParagraph = lastRow.getCell(0).addParagraph();
        secondParagraph.setAlignment(ParagraphAlignment.LEFT);
        secondParagraph.setVerticalAlignment(TextAlignment.BOTTOM);
        XWPFRun secondRun = secondParagraph.createRun();
        secondRun.setText("Academic Advisor __________________________________ Date Submitted ______________");
        
    }
    
}
