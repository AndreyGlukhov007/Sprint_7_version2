package Tests;

import POJO.CreateCourier.CreateCourierRequestPOJO;
import POJO.CreateCourier.CreateCourierResponsePOJO;
import POJO.CreateCourier.CreateCourierResponseСonflictPOJO;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class СourierTest {

    //Этот метод нужен чтобы создавать уникальных пользователей. К логоину и имени добавляется актуальная дата и время (часы, минуты, секунды).
    public static String getCurrentDateTime() {
        // Получаем текущие дату и время
        LocalDateTime now = LocalDateTime.now();

        // Форматируем вывод
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Возвращаем отформатированную строку
        return now.format(formatter);
    }

    @BeforeEach
    public void setUp(){
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }

    // курьера можно создать;
    // запрос возвращает правильный код ответа;
    @Test
    public void createСourierCode201() throws InterruptedException {
        Thread.sleep(1000); // если убрать эту строчку, то данный тест падает через раз.
       CreateCourierRequestPOJO createCourierPOJO = new CreateCourierRequestPOJO("LoginTest " + getCurrentDateTime(), "qwerty", "firstNameTest " + getCurrentDateTime());
       Response response = RestAssured
               .given()
                   .header("Content-type", "application/json")
                   .and()
                   .body(createCourierPOJO)
               .when()
                   .post("/api/v1/courier");

        assertEquals(response.getStatusCode(), 201); // проверяем что правильный код ответ 201
    }

    // успешный запрос возвращает ok: true;
    @Test
    public void createСourierBody(){
        CreateCourierRequestPOJO createCourierPOJO = new CreateCourierRequestPOJO("LoginTest " + getCurrentDateTime(), "qwerty", "firstNameTest " + getCurrentDateTime());
        CreateCourierResponsePOJO response = RestAssured
                .given()
                    .header("Content-type", "application/json")
                    .and()
                    .body(createCourierPOJO)
                .when()
                    .post("/api/v1/courier")
                .then()
                    .extract()
                    .as(CreateCourierResponsePOJO.class);

        assertEquals(response.isOk(), true); // проверяем в теле ответа приходит - ok: true
    }

    // нельзя создать двух одинаковых курьеров; Проверяем текст в ответе Body
    @Test
    public void createСourierSameСourierText(){
        CreateCourierRequestPOJO createCourierPOJO = new CreateCourierRequestPOJO("LoginTestFor22062025", "qwerty", "firstNameTestFor22062025");
        Response responseOne = RestAssured
                .given()
                    .header("Content-type", "application/json")
                    .and()
                    .body(createCourierPOJO)
                .when()
                    .post("/api/v1/courier");

        CreateCourierResponseСonflictPOJO responseTwo = RestAssured
                .given()
                    .header("Content-type", "application/json")
                    .and()
                    .body(createCourierPOJO)
                .when()
                    .post("/api/v1/courier")
                .then()
                    .extract()
                    .as(CreateCourierResponseСonflictPOJO.class);

        assertEquals(responseTwo.getMessage(), "Этот логин уже используется. Попробуйте другой."); // проверяем в теле ответа приходит - "message": "Этот логин уже используется. Попробуйте другой."
    }

    // Запрос с повторяющимся логином. Проверяем что код ответ - 409
    @Test
    public void createСourierSameСourierBody() {
        CreateCourierRequestPOJO createCourierPOJO = new CreateCourierRequestPOJO("LoginTestFor22062025", "qwerty", "firstNameTestFor22062025");
        Response responseOne = RestAssured
                .given()
                    .header("Content-type", "application/json")
                    .and()
                    .body(createCourierPOJO)
                .when()
                    .post("/api/v1/courier");

        Response responseTwo = RestAssured
                .given()
                    .header("Content-type", "application/json")
                    .and()
                    .body(createCourierPOJO)
                .when()
                    .post("/api/v1/courier");

        assertEquals(responseTwo.getStatusCode(), 409); // проверяем что код ответа 409
    }

    // Создание курьера без обязательного поля login. Проверяем текст ответа: "message": "Недостаточно данных для создания учетной записи"
    @Test
    public void createСourierNotLoginTestMessageLogin() {
        CreateCourierRequestPOJO createCourierPOJO = new CreateCourierRequestPOJO( "qwerty", "firstNameTestFor22062025");
        CreateCourierResponseСonflictPOJO response = RestAssured
                .given()
                    .header("Content-type", "application/json")
                    .and()
                    .body(createCourierPOJO)
                .when()
                    .post("/api/v1/courier")
                    .then()
                    .extract()
                    .as(CreateCourierResponseСonflictPOJO.class);

        assertEquals(response.getMessage(), "Недостаточно данных для создания учетной записи"); // проверяем что текст ответа "Недостаточно данных для создания учетной записи".
    }

    // Создание курьера без обязательного поля login. Проверяем что в ответ пришёл статус - 400.
    @Test
    public void createСourierNotLoginTestCodeLogin(){
        CreateCourierRequestPOJO createCourierPOJO = new CreateCourierRequestPOJO("qwerty", "firstNameTest " + getCurrentDateTime());
        Response response = RestAssured
                .given()
                    .header("Content-type", "application/json")
                    .and()
                    .body(createCourierPOJO)
                .when()
                    .post("/api/v1/courier");

        assertEquals(response.getStatusCode(), 400); // проверяем что правильный код ответ 201 // проверяем что код ответа - 400
    }

    // Создание курьера без обязательного поля password. Проверяем текст ответа: "message": "Недостаточно данных для создания учетной записи"
    @Test
    public void createСourierNotLoginTestMessagePassword() {
        CreateCourierRequestPOJO createCourierPOJO = new CreateCourierRequestPOJO();
        createCourierPOJO.setLogin("LoginTest " + getCurrentDateTime());
        createCourierPOJO.setFirstName("firstNameTest " + getCurrentDateTime());
        CreateCourierResponseСonflictPOJO response = RestAssured
                .given()
                    .header("Content-type", "application/json")
                    .and()
                    .body(createCourierPOJO)
                .when()
                    .post("/api/v1/courier")
                    .then()
                    .extract()
                    .as(CreateCourierResponseСonflictPOJO.class);

        assertEquals(response.getMessage(), "Недостаточно данных для создания учетной записи"); // проверяем что текст ответа "Недостаточно данных для создания учетной записи".
    }

    // Создание курьера без обязательного поля password. Проверяем что код ответа - 400
    @Test
    public void createСourierNotLoginTestCodePassword() {
        CreateCourierRequestPOJO createCourierPOJO = new CreateCourierRequestPOJO();
        createCourierPOJO.setLogin("LoginTest " + getCurrentDateTime());
        createCourierPOJO.setFirstName("firstNameTest " + getCurrentDateTime());
        Response response = RestAssured
                .given()
                .header("Content-type", "application/json")
                .and()
                .body(createCourierPOJO)
                .when()
                .post("/api/v1/courier");

        assertEquals(response.getStatusCode(), 400); // проверяем что правильный код ответ 400 //
    }
}
