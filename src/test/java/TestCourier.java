import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;


public class TestCourier extends BaseTest {

    private Integer courierId;

    @Step("Создание курьера   Post /api/v1/courier")
    public Response createCourier (Courier courier) {

        Response response =
                given ()
                        .header("Content-type", "application/json")
                        .body(courier)
                        .when()
                        .post(Url.CREATE_COURIER);
              return response;
    }

    @Step("Авторизация курьера для получения ID")
    public Response loginAndGetId( ) {
        Courier loginCourier = new Courier();
        loginCourier.setLogin("master");
        loginCourier.setPassword("1234");
        Response response = given()
                .header("Content-type", "application/json")
                .body(loginCourier)
                .when()
                .post(Url.LOGIN_COURIER);
        courierId = response.then().extract().path("id");
        return response;

    }


    @Step ("Проверка сообщения при успешном создании курьера")
    public  void checkMessage (Response response) {
        response.then().assertThat().body("ok",equalTo(true));
    }

    @Step ("Проверка статуса при успешном создании курьера")
    public  void checkStatus (Response response) {
        response.then().statusCode(SC_CREATED);
    }

    @Step ("Проверка сообщения при создании курьера без обязательного поля")
    public  void checkMessageWithoutField (Response response) {
        response.then().assertThat().body("message",equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Step ("Проверка статуса при создании курьера без обязательного поля")
    public  void checkStatusWithoutField (Response response) {
        response.then().statusCode(SC_BAD_REQUEST);
    }

    @Step("Удаление курьера DELETE /api/v1/courier/")
    public void deleteCourier () {
        given()
                .header("Content-type", "application/json")
                .delete(Url.DELETE_COURIER + courierId);

    }

    @Test
    public void testCourierCreate (){
        Courier courier = new Courier("master", "1234", "Ivan");

        Response response = createCourier(courier);
        checkStatus(response);
        checkMessage(response);


        }


    @Test
    public void testCourierCreateWithRepetition (){
        Courier courier = new Courier("master", "1234", "Ivan");

        Response response = createCourier(courier);
        checkStatus(response);
        checkMessage(response);

        Response responseDouble = createCourier(courier);
        responseDouble.then().statusCode(SC_CONFLICT);
        responseDouble.then().assertThat().body("message",equalTo("Этот логин уже используется"));

    }

   @Test
   public void testCourierCreateWithoutLogin () {
       Courier courier = new Courier("", "1234", "Ivan");
       Response response = createCourier(courier);
       checkStatusWithoutField(response);
       checkMessageWithoutField(response);

   }

    @Test
    public void testCourierCreateWithoutPassword () {
        Courier courier = new Courier("master", "", "Ivan");
        Response response = createCourier(courier);
        checkStatusWithoutField(response);
        checkMessageWithoutField(response);

    }

    @Test
    public void testCourierCreateWithoutFirstName () {
        Courier courier = new Courier("master", "1234", "");
        Response response = createCourier(courier);
        checkStatus(response);
        checkMessage(response);

    }


    @After
    public void tearDown() {
        loginAndGetId();
        if(courierId != null) {
            deleteCourier();
        }
    }

}
