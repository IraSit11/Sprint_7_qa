import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.hamcrest.core.IsNull.notNullValue;


@RunWith(Parameterized.class)
public class TestCreateOrder extends BaseTest {

    private Integer trackId;
    public List<String> color;
    public TestCreateOrder (List<String> color) {
        this.color = color;
    }


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
    @Step("Проверка сообщения ответа")
    public  void checkMessage (Response response) {
        response.then().assertThat().body("track", notNullValue());
    }
    @Step("Проверка статуса ответа")
    public  void checkStatus (Response response) {
        response.then().statusCode(SC_CREATED);
    }

    @Step ("Удаление заказа Delete /api/v1/orders/cancel")
    public void deleteOrder () {
        given()
                .header("Content-type", "application/json")
                .put(Url.DELETE_ORDER + trackId);
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
        checkStatus(response);
        checkMessage(response);

        trackId= response.then().extract().path("track");

    }


    @After
    public void tearDown() {
        if(trackId != null) {
            deleteOrder();
        }

    }
}
