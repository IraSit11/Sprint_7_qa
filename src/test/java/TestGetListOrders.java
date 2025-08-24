import io.qameta.allure.Step;
import static org.apache.http.HttpStatus.*;
import io.restassured.response.Response;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsNull.notNullValue;

public class TestGetListOrders extends BaseTest {

    OrderApi orderApi = new OrderApi();


    @Test
    public void testGetOrders() {
        Response response = orderApi.getListOrder();
        orderApi.checkStatusGetListOrder(response);
        orderApi.checkMessageGetListOrder(response);

    }
}
