/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author zypro
 */
import java.util.ArrayList;

public class Section {
    ArrayList<Course> courses;
    String title;
    String type;

    public Section(){
        courses = null;
        title = "Other Requirements";
    }
    public Section(ArrayList<Course> c , String t){
        courses = c;
        title = t;
    }

    public Section(String t){
        courses = null;
        title = t;
    }

    public String getTitle(){
        return title;
    }
    public ArrayList<Course> getCourses() {
        return courses;
    }
    public void setCourses(ArrayList<Course> courses) {
        this.courses = courses;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void addCourse(Course c){
        courses.add(c);
    }
    public void removeCourse(String name){
        boolean b = false;
        for(Course c : courses){
            if(c.getClassName() == name){
                courses.remove(c);
                b = true;
            }
        }
        if(!b)
            System.out.println("ERROR: " + name + "not found");
    }
    public void addAll(ArrayList<Course> c){
        for(Course g : c){
            courses.add(g);
        }
    }
}
