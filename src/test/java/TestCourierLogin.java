import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class TestCourierLogin extends  BaseTest{

    private Integer courierId;
    CourierApi courierApi = new CourierApi();

    @Before
    public void setUp() {
        Courier courier = new Courier("misters", "1234", "Ivan");
        courierApi.createCourier(courier);
    }



    @Test
    public void testCourierAuth() {
        Courier loginCourier = new Courier();
        loginCourier.setLogin("misters");
        loginCourier.setPassword("1234");

       Response response = courierApi.loginAndGetId(loginCourier);
        courierApi.checkStatusLogin(response);
        courierApi.checkMessageLogin(response);
        courierId = response.then().extract().path("id");

    }

    @Test
    public void testCourierAuthWithoutLogin () {
        Courier loginCourier = new Courier();
        loginCourier.setLogin("");
        loginCourier.setPassword("1234");
        Response response = courierApi.courierAuthWithoutLogin(loginCourier);
        courierApi.checkStatusWithoutLoginOrPassword(response);
        courierApi.checkMessageWithoutLoginOrPassword(response);

    }

    @Test
    public void testCourierAuthWithoutPassword () {
        Courier loginCourier = new Courier();
        loginCourier.setLogin("misters");
        loginCourier.setPassword("");

        Response response = courierApi.courierAuthWithoutPassword(loginCourier);
        courierApi.checkStatusWithoutLoginOrPassword(response);
        courierApi.checkMessageWithoutLoginOrPassword(response);

    }

    @Test
    public void testCourierNotFoundLogin () {
        Courier loginCourier = new Courier();
        loginCourier.setLogin("hello");
        loginCourier.setPassword("1234");
        Response response = courierApi.courierNotFoundLogin(loginCourier);
        courierApi.checkStatusCourierNotFound(response);
        courierApi.checkMessageCourierNotFound(response);


    }

    @Test
    public void testCourierNotFoundPassword () {
        Courier loginCourier = new Courier();
        loginCourier.setLogin("misters");
        loginCourier.setPassword("1454");
        Response response = courierApi.courierNotFoundPassword(loginCourier);
        courierApi.checkStatusCourierNotFound(response);
        courierApi.checkMessageCourierNotFound(response);

    }


    @After
    public void tearDown() {
        if(courierId != null) {
            courierApi.deleteCourier(courierId);
        }
    }
}
