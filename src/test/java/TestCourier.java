import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;


public class TestCourier extends BaseTest {


    CourierApi courierApi = new CourierApi();

    @Test
    public void testCourierCreate (){
        Courier courier = new Courier("master", "1234", "Ivan");

        Response response = courierApi.createCourier(courier);
        courierApi.checkStatus(response);
        courierApi.checkMessage(response);
        }

    @Test
    public void testCourierCreateWithRepetition (){
        Courier courier = new Courier("master", "1234", "Ivan");

        Response response = courierApi.createCourier(courier);
        courierApi.checkStatus(response);
        courierApi.checkMessage(response);

        Response responseDouble = courierApi.createCourier(courier);
        responseDouble.then().statusCode(SC_CONFLICT);
        responseDouble.then().assertThat().body("message",equalTo("Этот логин уже используется"));

    }

   @Test
   public void testCourierCreateWithoutLogin () {
       Courier courier = new Courier("", "1234", "Ivan");
       Response response = courierApi.createCourier(courier);
       courierApi.checkStatusWithoutField(response);
       courierApi.checkMessageWithoutField(response);

   }

    @Test
    public void testCourierCreateWithoutPassword () {
        Courier courier = new Courier("master", "", "Ivan");
        Response response = courierApi.createCourier(courier);
        courierApi.checkStatusWithoutField(response);
        courierApi.checkMessageWithoutField(response);
    }

    @Test
    public void testCourierCreateWithoutFirstName () {
        Courier courier = new Courier("master", "1234", "");
        Response response = courierApi.createCourier(courier);
        courierApi.checkStatus(response);
        courierApi.checkMessage(response);
    }


    @After
    public void tearDown() {
        Courier loginCourier = new Courier();
        loginCourier.setLogin("master");
        loginCourier.setPassword("1234");
        Response response = courierApi.loginAndGetId(loginCourier);
        Integer courierId = response.then().extract().path("id");
        if(courierId != null) {
            courierApi.deleteCourier(courierId);
        }
    }

}
