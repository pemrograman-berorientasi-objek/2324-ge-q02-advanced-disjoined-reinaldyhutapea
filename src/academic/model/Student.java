package academic.model;
/**
 * 
 * @author 12S22010 - reinaldi hutapea
 * @author 12S22048 - ira silalahi
 */


public class Student  {
    private String id;
    private String name;
    private String year;
    private String major;

    public Student(String id, String name, String year, String major) {
        this.id = id;
        this.name = name;
        this.year = year;
        this.major = major;

        
    }
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getYear() {
        return year;
    }

    public String getMajor() {
        return major;
    }
}