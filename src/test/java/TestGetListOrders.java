import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsNull.notNullValue;

public class TestGetListOrders {

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }
    @Step("Получить список заказов")
    public  Response getListOrder () {
        return given()
                        .header("Content-type", "application/json")
                        .get("/api/v1/orders");
    }

    @Step("Проверка сообщения ответа")
    public  void checkMessage (Response response) {
        response.then().assertThat().body("orders", notNullValue());
    }
    @Step("Проверка статуса ответа")
    public  void checkStatus (Response response) {
        response.then().statusCode(200);
    }


    @Test
    public void testGetOrders() {
        Response response = getListOrder();
        checkMessage(response);
        checkStatus(response);
    }
}
