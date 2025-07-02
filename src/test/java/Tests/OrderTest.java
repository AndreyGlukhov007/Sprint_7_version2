package Tests;

import POJO.CreateOrder.CreateOrderRequestPOJO;
import POJO.CreateOrder.CreateOrderResponsePOJO;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class OrderTest {

    @BeforeEach
    public void setUp(){
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }

    // Источник параметров для теста
    private static Stream<Arguments> orderColorDataProvider() {
        return Stream.of(
                // color, description
                Arguments.of(Collections.singletonList("BLACK"), "Заказ с цветом BLACK"),
                Arguments.of(Collections.singletonList("GREY"), "Заказ с цветом GREY"),
                Arguments.of(Collections.emptyList(), "Заказ без указания цвета")
        );
    }

    /*
    ВАЖНО! Этот тест иногда падает. Связано это с тем что в ответ иногда приходит track из 5-ти цифр вместо 6-ти.
    Можно по разному это трактовать, но на мой взгляд это шибка, т.к. в примере track состоит из 6-ти цифр. Пример -  track: 124124
    И то что в ответ иногда track формируется 5-ть цифр больше похоже на ошибку. Но если в рамках ревью мне нужно будет исправить эту проверку - я это сделаю.
     */
    @ParameterizedTest(name = "{1}") // Используем описание из параметров как имя теста
    @MethodSource("orderColorDataProvider")
    void createOrder_WithDifferentColors_ReturnsValidTrack(List<String> color, String description) throws InterruptedException {
        // 1. Подготовка тестовых данных
        CreateOrderRequestPOJO createOrderRequestPOJO = new CreateOrderRequestPOJO(
                "Naruto",
                "Uchiha",
                "Konoha, 142 apt.",
                4,
                "+7 800 355 35 35",
                5,
                "2020-06-06",
                "Saske, come back to Konoha",
                color
        );

        Thread.sleep(5000); // если убрать этот sleep, то иногда тест падает.

        // 2. Отправка запроса и получение ответа
        CreateOrderResponsePOJO response = RestAssured
                .given()
                    .header("Content-type", "application/json")
                    .body(createOrderRequestPOJO)
                .when()
                    .post("/api/v1/orders")
                .then()
                    .extract()
                    .as(CreateOrderResponsePOJO.class);

        // 3. Проверка трек-номера
        int track = response.getTrack();
        assertTrue(track >= 100000 && track <= 999999,
                "Трек должен быть 6-значным числом. Получено: " + track);
    }

}

