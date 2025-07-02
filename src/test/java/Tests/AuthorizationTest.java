package Tests;

import POJO.AuthorizationCourier.AuthorizationCourierMessagePOJO;
import POJO.AuthorizationCourier.AuthorizationCourierRequestPOJO;
import POJO.AuthorizationCourier.AuthorizationCourierResponsePOJO;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AuthorizationTest {

    String bearerToken = "Андрей";

    private int idCourier;

    @BeforeEach
    public void setUp(){
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }

    // Курьер может авторизоваться;
    @Test
    public void authorization(){
        AuthorizationCourierRequestPOJO authorizationCourierRequestPOJO = new AuthorizationCourierRequestPOJO("Test_Login_01072025", "1234");
        AuthorizationCourierResponsePOJO response = RestAssured
                .given()
                    .header("Content-type", "application/json")
                    .and()
                    .body(authorizationCourierRequestPOJO)
                .when()
                    .post("/api/v1/courier/login")
                .then()
                    .extract()
                    .as(AuthorizationCourierResponsePOJO.class);

        idCourier = response.getId(); // тут мы присваиваем id курьера чтобы оптом воспользоваться этим id и удалить курьера через этот id.

        assertEquals(response.getId(), 563348); // проверяем в теле ответа возвращается "id": 563348
    }

    // Попытка авторизоваться без заполнения поля Login
    @Test
    public void authorizationNotLogin(){
        AuthorizationCourierRequestPOJO authorizationCourierRequestPOJO = new AuthorizationCourierRequestPOJO("Test_Login_01072025", "");
        AuthorizationCourierMessagePOJO response = RestAssured
                .given()
                    .header("Content-type", "application/json")
                    .and()
                    .body(authorizationCourierRequestPOJO)
                .when()
                    .post("/api/v1/courier/login")
                .then()
                    .extract()
                    .as(AuthorizationCourierMessagePOJO.class);

        assertEquals(response.getMessage(), "Недостаточно данных для входа"); // проверяем в теле ответа возвращается "message": "Недостаточно данных для входа"
        assertEquals(response.getCode(), 400); // проверяем в теле ответа возвращается код - 400
    }

    // Попытка авторизоваться без заполнения поля Password
    @Test
    public void authorizationNotPassword(){
        AuthorizationCourierRequestPOJO authorizationCourierRequestPOJO = new AuthorizationCourierRequestPOJO("", "1234");
        AuthorizationCourierMessagePOJO response = RestAssured
                .given()
                    .header("Content-type", "application/json")
                    .and()
                    .body(authorizationCourierRequestPOJO)
                .when()
                    .post("/api/v1/courier/login")
                .then()
                    .extract()
                    .as(AuthorizationCourierMessagePOJO.class);

        assertEquals(response.getMessage(), "Недостаточно данных для входа"); // проверяем в теле ответа возвращается "message": "Недостаточно данных для входа"
        assertEquals(response.getCode(), 400); // проверяем в теле ответа возвращается код - 400
    }

    // Попытка авторизоваться по неверному логину.
    @Test
    public void authorizationLoginNotTrue(){
        AuthorizationCourierRequestPOJO authorizationCourierRequestPOJO = new AuthorizationCourierRequestPOJO("Test_Login_01072025_1", "1234");
        AuthorizationCourierMessagePOJO response = RestAssured
                .given()
                    .header("Content-type", "application/json")
                    .and()
                    .body(authorizationCourierRequestPOJO)
                .when()
                    .post("/api/v1/courier/login")
                .then()
                    .extract()
                    .as(AuthorizationCourierMessagePOJO.class);

        assertEquals(response.getMessage(), "Учетная запись не найдена"); // проверяем в теле ответа возвращается "message": "Учетная запись не найдена"
        assertEquals(response.getCode(), 404); // проверяем в теле ответа возвращается код - 404
    }

    // Попытка авторизоваться по неверному паролю.
    @Test
    public void authorizationPasswordNotTrue(){
        AuthorizationCourierRequestPOJO authorizationCourierRequestPOJO = new AuthorizationCourierRequestPOJO("Test_Login_01072025", "12345");
        AuthorizationCourierMessagePOJO response = RestAssured
                .given()
                    .header("Content-type", "application/json")
                    .and()
                    .body(authorizationCourierRequestPOJO)
                .when()
                    .post("/api/v1/courier/login")
                .then()
                    .extract()
                    .as(AuthorizationCourierMessagePOJO.class);

        assertEquals(response.getMessage(), "Учетная запись не найдена"); // проверяем в теле ответа возвращается "message": "Учетная запись не найдена"
        assertEquals(response.getCode(), 404); // проверяем в теле ответа возвращается код - 404
    }

    // Попытка авторизоваться под несуществующим пользователем.
    @Test
    public void authorizationUserFalse(){
        AuthorizationCourierRequestPOJO authorizationCourierRequestPOJO = new AuthorizationCourierRequestPOJO("Test_Login_01072025_1", "12345");
        AuthorizationCourierMessagePOJO response = RestAssured
                .given()
                    .header("Content-type", "application/json")
                    .and()
                    .body(authorizationCourierRequestPOJO)
                .when()
                    .post("/api/v1/courier/login")
                .then()
                    .extract()
                    .as(AuthorizationCourierMessagePOJO.class);

        assertEquals(response.getMessage(), "Учетная запись не найдена"); // проверяем в теле ответа возвращается "message": "Учетная запись не найдена"
        assertEquals(response.getCode(), 404); // проверяем в теле ответа возвращается код - 404
    }

    @AfterEach
    public void deleteCourier(){
         Response response = RestAssured
                .given()
                    .header("Content-type", "application/json")
                    .and()
                    .body(idCourier)
                .when()
                    .delete("/api/v1/courier/"+idCourier);
    }

}
