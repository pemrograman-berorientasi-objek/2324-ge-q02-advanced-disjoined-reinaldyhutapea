package academic.model;
/**
 * 
 * @author 12S22010 - reinaldi hutapea
 * @author 12S22048 - ira silalahi
 */

public class Enrollment {
    private String studentId;
    private String courseId;
    private String academicYear;
    private String semester;
    private String grade;
   

    public Enrollment(String studentId, String courseId, String academicYear, String semester, String grade) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.academicYear = academicYear;
        this.semester = semester;
        this.grade = grade;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getGrade() {
        return grade;
    }

    
    public void updateGrade(String grade) {
        this.grade = grade;
    }
}