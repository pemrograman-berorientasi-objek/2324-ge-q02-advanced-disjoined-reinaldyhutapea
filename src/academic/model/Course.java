package academic.model;

/**
 * 
 * @author 12S22010 - reinaldi hutapea
 * @author 12S22048 - ira silalahi
 */


import java.util.ArrayList;

public class Course {
    private String code;
    private String name;
    private String credits;
    private String passingGrade;
    private ArrayList<CourseOpening> courseOpenings;

    public Course(String code, String name, String credits, String passingGrade) {
        this.code = code;
        this.name = name;
        this.credits = credits;
        this.passingGrade = passingGrade;
        this.courseOpenings = new ArrayList<>();
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getCredits() {
        return credits;
    }

    public String passingGrade() {
        return passingGrade;
    }

    public void addCourseOpening(CourseOpening courseOpening) {
        this.courseOpenings.add(courseOpening);
    }

    // Metode untuk mengambil daftar pembukaan kursus
    public ArrayList<CourseOpening> getCourseOpenings() {
        return courseOpenings;
    }

   
    public void printCourseHistory() {
  
    }
}
