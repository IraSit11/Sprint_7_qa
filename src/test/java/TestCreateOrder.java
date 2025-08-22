import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsNull.notNullValue;


@RunWith(Parameterized.class)
public class TestCreateOrder {

    private Integer trackId;
    public List<String> color;
    public TestCreateOrder (List<String> color) {
        this.color = color;
    }
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }

    @Step("Создание заказа")
    public Response createOrder (Order order) {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(order)
                        .when()
                        .post("/api/v1/orders");
        return response;
    }
    @Step("Проверка сообщения ответа")
    public  void checkMessage (Response response) {
        response.then().assertThat().body("track", notNullValue());
    }
    @Step("Проверка статуса ответа")
    public  void checkStatus (Response response) {
        response.then().statusCode(201);
    }

    @Parameterized.Parameters (name = "Цвет самоката: {0}")
    public static Object[][] testColor () {
        return new Object[][] {
                {List.of("Black")},
                {List.of("Black","Grey")},
                {List.of("Grey")},
                {List.of()},
        };
    }

    @Test
    public void testCreateOrder () {
        Order order = new Order("Иван",
                "Петров",
                "Москва, ул.Усачева, д.3",
                "Полянка","89279991545",
                "3",
                "05.09.2025",
                "самокат",
                color);
        Response response = createOrder(order);
        checkMessage(response);
        checkStatus(response);
        trackId= response.then().extract().path("track");

    }


    @After
    public void tearDown() {
        if(trackId != null) {
            given()
                    .header("Content-type", "application/json")
                    .put("/api/v1/orders/cancel" + trackId);
        }

    }
}
