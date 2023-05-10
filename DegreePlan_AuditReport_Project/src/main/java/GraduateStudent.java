//import java.util.*;

import java.io.File;
import java.util.ArrayList;

//import java.util.*;


public class GraduateStudent extends ParsingAlgorithms{

    public DegreePlan d;
    public AuditReport a;
    public ParsingAlgorithms parse;
    public String track;
    public String path;
    public PreViewWindow PVW;
    
    public GraduateStudent(){

    }
    
    public GraduateStudent(ParsingAlgorithms p, PreViewWindow pvw, String filePath){
        parse = p;
        PVW = pvw;
        File original = new File(filePath);
        path = original.getParent();
        //super.parseTranscript(transcriptData);
        track = pvw.getSelectedTrack();
        Section core = new Section(p.getCoreCourses(), "CORE COURSES");
        Section xFollowing = new Section(p.getXFollowingCourses(), "One of the following Course");
        Section electives = new Section(p.getElectives(), "5 APPROVED 6000 LEVEL ELECTIVES");
        Section addElectives = new Section(p.getAdditionalElectives(), "Additional Electives");
        Section other = new Section(p.getOtherRequirements(), "Other Requirements");
        Section admission = new Section(p.getAdmissionPrerequisites(), "Admission Prerequisites");
        
        
        System.out.println("add Electives: "+ addElectives.getCourses().size());
        System.out.println("Other Req " + other.getCourses().size());
        System.out.println("Admission PR: " + other.getCourses().size());
        
        Section temp = new Section(new ArrayList<>(), "temp");
        System.out.println("jowe mama " + PVW.getSelectedTrack());
        
        if(PVW.getSelectedTrack().equals("Software Engineering") || PVW.getSelectedTrack().equals("Networks and Telecommunications")){
            System.out.println("im here");
            
            temp.getCourses().addAll(other.getCourses());
            
            other.setCourses(new ArrayList<>());
            other.getCourses().addAll(addElectives.getCourses());
            
            admission.setCourses(new ArrayList<>());
            admission.getCourses().addAll(temp.getCourses());
           
        }
        
        System.out.println("add Electives: "+ addElectives.getCourses().size());
        System.out.println("Other Req " + other.getCourses().size());
        System.out.println("Admission PR: " + other.getCourses().size());
        
        
        
        
        
        ArrayList<Section> s = new ArrayList<>();
        s.add(core);
        s.add(xFollowing);
        s.add(electives);
        s.add(other);
        s.add(addElectives);
        
        s.add(admission);
        
        
        /*
        Section core1 = new Section(p.getCoreCourses(), "Core");
        Section electives1 = new Section(p.getElectives(), "Elective");
        Section overall1 = new Section(p.getCoreCourses(),"Overall");
        
        overall1.addAll(p.getXFollowingCourses());
        overall1.addAll(p.getElectives());
        overall1.addAll(p.getAdditionalElectives());
        electives.addAll(p.getAdditionalElectives());
        overall1.addAll(p.getAdmissionPrerequisites());
        
        
        
        ArrayList<Section> s1 = new ArrayList<>();
        s1.add(core1);
        s1.add(electives1);
        s1.add(overall1);
        */
        
        d = new DegreePlan(s, this, pvw);
        d.outputDoc();
        
        runAudit(p);
        //a = new AuditReport(s, this, p);
        
        //a.outputDoc();
        
 
         /*
        ArrayList<ArrayList<ArrayList<String>>> dataList = p.getFinalDataList();
        
        for(ArrayList<ArrayList<String>> matrix : dataList){
            for(ArrayList<String> array : matrix){
                for(String s : array){
                    System.out.println(s);
                }
            }
        }
    */
        
    }
    public PreViewWindow getPVW(){
        return PVW;
    }
    public void runAudit(ParsingAlgorithms p){
        Section core = new Section(p.getCoreCourses(), "Core");
        Section overall = new Section(p.getCoursesArray(), "Overall");
        Section electives = new Section(p.getElectives(), "Elective");
        
        ArrayList<Section> sections = new ArrayList<>();
        sections.add(overall);
        sections.add(core);
        
        sections.add(electives);
        
        a = new AuditReport(sections, this, p);
        
        a.outputDoc();
    }
    public String getFirstName(){
        //return super.getFirstName();
        return "";
    }
    public ParsingAlgorithms getParse(){
        return parse;
    }
    public String getPath(){
        return path;
    }


    
}
