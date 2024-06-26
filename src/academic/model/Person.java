package academic.model;
/**
 * @author 12S22010 Reinaldy Hutapea
 * @author 12S22048 Ira Wianda Sari Silalahi
 */
public class Person <T extends PersonDetail> implements PersonDetail{
    protected String id;
    protected String name;

    public Person(String id, String name) {
        this.id     = id;
        this.name   = name;
    }
    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
}