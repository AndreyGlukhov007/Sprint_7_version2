package POJO.AuthorizationCourier;

public class AuthorizationCourierRequestPOJO {

    private String login;
    private String password;

    public AuthorizationCourierRequestPOJO() {}

    public AuthorizationCourierRequestPOJO(String login, String password) {
        this.login = login;
        this.password = password;
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
}
