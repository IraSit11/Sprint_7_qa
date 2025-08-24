import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.core.IsNull.notNullValue;

public class OrderApi {
    @Step("Создание заказа")
    public Response createOrder (Order order) {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(order)
                        .when()
                        .post(Url.CREATE_ORDER);
        return response;
    }
    @Step("Проверка сообщения ответа при успешном создании заказа")
    public  void checkMessage (Response response) {
        response.then().assertThat().body("track", notNullValue());
    }
    @Step("Проверка статуса ответа при успешном создании заказа")
    public  void checkStatus (Response response) {
        response.then().statusCode(SC_CREATED);
    }

    @Step("Получить список заказов")
    public  Response getListOrder () {
        return given()
                .header("Content-type", "application/json")
                .get(Url.GET_LIST_ORDERS);
    }

    @Step("Проверка сообщения ответа при получении списка заказов")
    public  void checkMessageGetListOrder (Response response) {
        response.then().assertThat().body("orders", notNullValue());
    }
    @Step("Проверка статуса ответа при получении списка заказов")
    public  void checkStatusGetListOrder (Response response) {
        response.then().statusCode(SC_OK);
    }


    @Step ("Удаление заказа Delete /api/v1/orders/cancel")
    public void deleteOrder (Integer trackId) {
        given()
                .header("Content-type", "application/json")
                .put(Url.DELETE_ORDER + trackId);
    }
}
