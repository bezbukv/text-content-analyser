package test_call_jy;

/**
 * Created with IntelliJ IDEA.
 * User: кей
 * Date: 14.03.13
 * Time: 20:16
 * To change this template use File | Settings | File Templates.
 */
public class Beach {

    private String name;
    private String city;


    public Beach(String name, String city){
        this.name = name;
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

}