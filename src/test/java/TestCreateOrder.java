import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.List;

@RunWith(Parameterized.class)
public class TestCreateOrder extends BaseTest {

    private Integer trackId;
    public List<String> color;
    public TestCreateOrder (List<String> color) {
        this.color = color;
    }

    OrderApi orderApi = new OrderApi();



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
        Response response = orderApi.createOrder(order);
        orderApi.checkStatus(response);
        orderApi.checkMessage(response);

        trackId= response.then().extract().path("track");

    }


    @After
    public void tearDown() {
        if(trackId != null) {
            orderApi.deleteOrder(trackId);
        }

    }
}
