package POJO.CreateCourier;

// кемментарий для того чтобы я мог сделать Pull Request 1.
public class CreateCourierResponsePOJO {

    private boolean ok;

    public CreateCourierResponsePOJO() {};

    public CreateCourierResponsePOJO(boolean ok) {
        this.ok = ok;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }
}
