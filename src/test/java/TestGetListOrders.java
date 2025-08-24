import io.qameta.allure.Step;
import static org.apache.http.HttpStatus.*;
import io.restassured.response.Response;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsNull.notNullValue;

public class TestGetListOrders extends BaseTest {


    @Step("Получить список заказов")
    public  Response getListOrder () {
        return given()
                        .header("Content-type", "application/json")
                        .get(Url.GET_LIST_ORDERS);
    }

    @Step("Проверка сообщения ответа")
    public  void checkMessage (Response response) {
        response.then().assertThat().body("orders", notNullValue());
    }
    @Step("Проверка статуса ответа")
    public  void checkStatus (Response response) {
        response.then().statusCode(SC_OK);
    }


    @Test
    public void testGetOrders() {
        Response response = getListOrder();
        checkStatus(response);
        checkMessage(response);

    }
}
