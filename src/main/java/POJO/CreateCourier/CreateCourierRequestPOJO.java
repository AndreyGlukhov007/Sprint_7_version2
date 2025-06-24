package POJO.CreateCourier;

public class CreateCourierRequestPOJO {

    private String login;
    private String password;
    private String firstName;

    public CreateCourierRequestPOJO() {};

    public CreateCourierRequestPOJO(String password, String firstName){
        this.password = password;
        this.firstName = firstName;
    }

    public CreateCourierRequestPOJO(String login, String password, String firstName){
        this.login = login;
        this.password = password;
        this.firstName = firstName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

}
