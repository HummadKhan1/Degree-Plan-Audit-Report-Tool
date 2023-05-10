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
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.Templates;

import org.apache.pdfbox.contentstream.operator.text.SetFontAndSize;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.apache.poi.xwpf.usermodel.XWPFTable.XWPFBorderType;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;

public class AuditReport {
    private static final ParagraphAlignment CENTER = ParagraphAlignment.CENTER;
    private static ArrayList<Section> sections = new ArrayList<Section>();
    private static double coreGPA = 0, overallGPA = 0, electiveGPA = 0;
    private static int coreHours = 0, overallHours = 0, electiveHours = 0;
    private static double coreGradePoints = 0, overallGradePoints = 0, electiveGradePoints = 0;
    XWPFDocument document;
    XWPFTable table;
    static GraduateStudent Grad;
    
    public AuditReport(ArrayList<Section> s, GraduateStudent grad, ParsingAlgorithms p) {
        //sections = s;
        Grad = grad;
        document = new XWPFDocument();
        XWPFParagraph title = document.createParagraph();
        XWPFRun titleRun = title.createRun();
        setFont(titleRun, "Calibre Light", 24);
        title.setAlignment(CENTER);
        titleRun.setText("Audit Report");
        table = document.createTable();
        table.setWidth(9500);
        setBorders(table, null, 0, 0, null);
        addCells(table.getRow(0), 1);
        table.getRow(0).getCell(0).setText("Name: " + grad.getParse().getName());
        table.getRow(0).getCell(0).setWidth("6500");
        table.getRow(0).getCell(1).setText("ID: " + grad.getParse().getID());
        table.createRow().getCell(0).setText("Plan: " + grad.getParse().getProgram());
        table.getRow(1).getCell(1).setText("Major: " + grad.getParse().getMajor());
        table.createRow().getCell(1).setText("Track: " + grad.getPVW().getSelectedTrack());
        grad.getParse().setAllElectives();
        XWPFParagraph gpa = document.createParagraph();
        ArrayList<Course> overall = new ArrayList<>();
        overall.addAll(grad.getParse().getCoursesArray());
        //overall.addAll(s.get(0).getCourses());
        printGPA(gpa, calcGPA(grad.getParse().getCoreCourses(), "Core"), calcGPA(grad.getParse().getAllElectives(), "Elective"),
                calcGPA(overall, "Overall"));
        printCourses(gpa, grad.getParse().getCoreCourses(), grad.getParse().getAllElectives(), grad.getParse().getAdmissionPrerequisites(), grad.getParse().getAdditionalElectives());
        printReqs(gpa, sections);
    }
    
    public static void processSection(ArrayList<Section> s){
        for(Section f : s){
            if(f.getTitle() == "CORE COURSES"){
                f.setTitle("Core");
            }
            if(f.getTitle() == "One of the following Course"){
                f.setTitle("Overall");
            }
            if(f.getTitle() == "5 APPROVED 6000 LEVEL ELECTIVES"){
                f.setTitle("Elective");
            }
            if(f.getTitle() == "Additional Electives"){
                f.setTitle("Elective");
            }
            if(f.getTitle() == "Other Requirements"){
                f.setTitle("Overall"); 
            }
            if(f.getTitle() == "Admission Prerequesites"){
                f.setTitle("Overall");
            }
        }
        sections = s;
    }

    public static void setFont(XWPFRun run, String fontFamily, int size) {
        run.setFontFamily(fontFamily);
        run.setFontSize(size);
    }

    public void outputDoc() {
        String s = Grad.getPath() +"\\"+ Grad.getParse().getName() + "'s Audit Report.docx";
        // FileOutputStream out;
        try (FileOutputStream out = new FileOutputStream(new File(s))) {
            document.write(out);
            out.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void addCells(XWPFTableRow row, int x) {
        for (int i = 0; i < x; i++) {
            row.addNewTableCell();
        }
    }

    public static void setBorders(XWPFTable table, XWPFBorderType type, int size, int space, String rgbColor) {
        table.setBottomBorder(type, size, space, rgbColor);
        table.setInsideHBorder(type, size, space, rgbColor);
        table.setInsideVBorder(type, size, space, rgbColor);
        table.setLeftBorder(type, size, space, rgbColor);
        table.setRightBorder(type, size, space, rgbColor);
        table.setTopBorder(type, size, space, rgbColor);
    }

    public static void printGPA(XWPFParagraph par, double cGPA, double eGPA, double cbGPA) {
        XWPFRun run = par.createRun();
        run.setText("Core GPA: " + String.format("%.2f", cGPA));
        run.addCarriageReturn();
        run.setText("Elective GPA: " + String.format("%.2f", eGPA));
        run.addCarriageReturn();
        run.setText("Combined GPA: " + String.format("%.2f", cbGPA));
        run.addCarriageReturn();
        run.addCarriageReturn();
    }

    public static void printCourses(XWPFParagraph par, ArrayList<Course> cores, ArrayList<Course> electives,
            ArrayList<Course> admissionPre, ArrayList<Course> addElectives) {
        XWPFRun run = par.createRun();
        admissionPre.addAll(addElectives);
     
        ArrayList<Course> level = new ArrayList<>();
        
        
        for(Course c : admissionPre){
            if(c.getClassName().contains(">")){
                level.add(c);
            }
        }
        
        
        
        
        //level.addAll(admissionPre);
       // level.addAll(Grad.getParse().getAllElectives());
        run.setText("Core Courses: ");
        if (cores == null || cores.isEmpty())
            run.setText("None");
        else
            for (Course c : cores) {
                run.setText(c.getDepartment() + " " + c.getCourseNumber() + " ");
            }
        
        run.addCarriageReturn();
        run.setText("Elective Courses: ");

        if (electives == null || electives.isEmpty())
            run.setText("None");
        else
            for (Course c : electives) {
                run.setText(c.getDepartment() + " " + c.getCourseNumber() + " ");
            }

        run.addCarriageReturn();
        run.setText("Leveling Courses and Pre-requisites from Admission Letter:");
        run.addCarriageReturn();

        if (level == null || level.isEmpty())
            run.setText("None");
        else
            System.out.println("Level size: " + level.size());
            for (Course c : level) {
                System.out.println(c.getClassName() + " yo");
                if(c.getClassName().indexOf('>')>-1){
                    //System.out.println("here");
                    if(c.getLetterGrade() == null || c.getLetterGrade().equals("")){
                        run.setText(c.getDepartment() + " " + c.getClassName() + " - " + c.getTransferType());
                        run.addCarriageReturn();
                    }
                    else{
                        run.setText(c.getDepartment() + " " + c.getCourseNumber() + " - Completed");
                        run.addCarriageReturn();
                    }
                }
            }

        run.addCarriageReturn();
        run.addCarriageReturn();

    }

    public static void printReqs(XWPFParagraph par, ArrayList<Section> section) {
        XWPFRun run = par.createRun();
        run.setText("Outstanding Requirements");
        run.addCarriageReturn();
        System.out.println("section size " + section.size());
        for (Section s : section) {
            if (s.getCourses() == null) {
                run.setText(s.getTitle() + " Complete");
            } else {
                //To maintain a 3.19 core GPA:
                String minGPA = (s.getTitle().equals("Core")) ? "3.19 " + s.getTitle() + " GPA:": "3.00 " + s.getTitle() + " GPA:";
                run.setText("To maintain a " + minGPA);
                run.addCarriageReturn();
                run.addTab();
                run.setText(neededGPA(s.getTitle()));
            }
            run.addCarriageReturn();
        }

    }

    /**
     * @param courses
     * @param title
     * @return double
     */
    private static Double calcGPA(ArrayList<Course> courses, String title) {
        double points = 0;
        int totalHours = 0;
        ArrayList<Course> tempArrayList = new ArrayList<Course>();
        for (Course course : courses) {
            int semesterHours = (Character.getNumericValue(course.getCourseNumber().charAt(1)));
            switch (course.getLetterGrade()) {
                case "A+":
                case "A":
                    points += 4 * semesterHours;
                    totalHours += semesterHours;
                    break;
                case "A-":
                    points += 3.670 * semesterHours;
                    totalHours += semesterHours;
                case "B+":
                    points += 3.333 * semesterHours;
                    totalHours += semesterHours;
                    break;
                case "B":
                    points += 3.000 * semesterHours;
                    totalHours += semesterHours;
                    break;
                case "B-":
                    points += 2.670 * semesterHours;
                    totalHours += semesterHours;
                    break;
                case "C+":
                    points += 2.330 * semesterHours;
                    totalHours += semesterHours;
                    break;
                case "C":
                    points += 2.000 * semesterHours;
                    totalHours += semesterHours;
                    break;
                case "F":
                    points += 0 * semesterHours;
                    totalHours += semesterHours;
                    break;
                default:
                    tempArrayList.add(course);
                    // courses.remove(course);
                    break;
            }
        }
        if (title.equals("Core")){
            coreHours = totalHours;
            coreGPA = points/totalHours;
            coreGradePoints = points;
        }
        else if(title.equals("Elective")){
            electiveHours = totalHours;
            electiveGPA = points/totalHours;
            electiveGradePoints = points;
        }
        else{
            overallHours = totalHours;
            overallGPA = points/totalHours;
            overallGradePoints = points;
        }

        if (tempArrayList.size() > 0) {
            Section tempSection = new Section(tempArrayList, title);
            sections.add(tempSection);
        } else {
            Section tempSection = new Section(title);
            sections.add(tempSection);
        }
        //courses.removeAll(tempArrayList);
        return points / totalHours;
        // String.valueOf(points/courses.size());
    }

    private static void printSection() {
        for (Section s : sections) {
            if (s.getCourses() != null) {
                for (Course c : s.getCourses()) {
                    System.out.println("Sections: " + s.getTitle() + " " + c.getCourseNumber() + " " + c.getLetterGrade());
                }
            }
        }
    }

    private static String neededGPA(String typeGPA){
        double neededGPA;
        String firstPart, secondPart = "";
        int coursesSize;

        Map<String, Integer> myMap = new HashMap<String, Integer>();
        myMap.put("Core", 0);
        myMap.put("Elective", 1);
        myMap.put("Overall", 2);
        
            System.out.println(typeGPA);
            System.out.println(myMap.get(typeGPA));
            int tempHours = 0;
            coursesSize = sections.get(myMap.get(typeGPA)).getCourses().size();
            
            for (Course c : sections.get(myMap.get(typeGPA)).getCourses()){
                tempHours += (Character.getNumericValue(c.getCourseNumber().charAt(1)));
                secondPart += c.getCourseNumber() + " ";
            }
            //Target Quality Points = (Target GPA x (Total Completed Credit Hours + Total Credit Hours of Planned Courses)) 
            //- Current Quality Points
            double gradePointsNeeded = typeGPA.equals("Core") ? 3.19 * coreHours : 3.0 * coreHours;
            neededGPA = (gradePointsNeeded - coreGradePoints ) / tempHours;
            firstPart = (coursesSize > 1) ? "The student needs >= " : "The student needs a GPA >= ";
            return getGPAReq(neededGPA, firstPart, secondPart);
        
    }
    

    private static String getGPAReq(double grade, String firstPart, String secondPart){
        String tempLetterGrade = "";
        if(grade > 3.670) 
            tempLetterGrade = "A+";
        else if(grade > 3.330)
            tempLetterGrade = "A";
        else if(grade > 3.000)
            tempLetterGrade = "B+";
        else if(grade > 2.670)
            tempLetterGrade = "B";
        else if(grade > 2.330)
            tempLetterGrade = "B-";
        else if(grade > 2.000)
            tempLetterGrade = "C";     

        if(grade >= 2.0){
            return firstPart + tempLetterGrade + " " + secondPart;
        }
        else{
            return "The student must pass " + secondPart;
        }
     }

}