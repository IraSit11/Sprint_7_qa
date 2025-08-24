import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;

public class TestCourierLogin extends  BaseTest{

    private Integer courierId;

    @Before
    public void setUp() {
        createCourier();
    }

    @Step("Создание курьера")
    public Response createCourier () {
        Courier courier = new Courier("misters", "1234", "Ivan");
        Response response =
                given ()
                        .header("Content-type", "application/json")
                        .body(courier)
                        .when()
                        .post(Url.CREATE_COURIER);
        return response;
    }

    @Step("Успешная аутентификация курьера  post /api/v1/courier/login")
    public Response courierAuth () {
        Courier loginCourier = new Courier();
        loginCourier.setLogin("misters");
        loginCourier.setPassword("1234");
        Response response = given ()
                .header("Content-type", "application/json")
                .body(loginCourier)
                .when()
                .post(Url.LOGIN_COURIER);
        courierId = response.then().extract().path("id");
        return response;

    }

    @Step ("Проверка сообщения при успешном входе")
    public  void checkMessage (Response response) {
        response.then().assertThat().body("id", notNullValue());
    }

    @Step ("Проверка статуса при успешном входе")
    public  void checkStatus (Response response) {        response.then().statusCode(SC_OK);    }

    @Step("Авторизация курьера без логина ")
    public Response courierAuthWithoutLogin () {
        Courier loginCourier = new Courier();
        loginCourier.setLogin("");
        loginCourier.setPassword("1234");
        Response response = given ()
                .header("Content-type", "application/json")
                .body(loginCourier)
                .when()
                .post(Url.LOGIN_COURIER);
        courierId = response.then().extract().path("id");

        return response;
    }

    @Step("Авторизация курьера без пароля ")
    public Response courierAuthWithoutPassword () {
        Courier loginCourier = new Courier();
        loginCourier.setLogin("misters");
        loginCourier.setPassword("");
        Response response = given ()
                .header("Content-type", "application/json")
                .body(loginCourier)
                .when()
                .post(Url.LOGIN_COURIER);
        courierId = response.then().extract().path("id");

        return response;

    }
    @Step ("Проверка сообщения при невыполненном входе")
    public  void checkMessageWithoutLoginOrPassword (Response response) {
        response.then().assertThat().body("message", equalTo("Недостаточно данных для входа"));
    }

    @Step ("Проверка статуса при успешном входе")
    public  void checkStatusWithoutLoginOrPassword (Response response) {
        response.then().statusCode(SC_BAD_REQUEST);
    }

    @Step("Авторизация несуществующего курьера (неверный логин) ")
    public Response courierNotFoundLogin () {
        Courier loginCourier = new Courier();
        loginCourier.setLogin("hello");
        loginCourier.setPassword("1234");
        Response response = given ()
                .header("Content-type", "application/json")
                .body(loginCourier)
                .when()
                .post(Url.LOGIN_COURIER);
        courierId = response.then().extract().path("id");

        return response;
    }
    @Step("Авторизация несуществующего курьера (неверный пароль) ")
    public Response courierNotFoundPassword () {
        Courier loginCourier = new Courier();
        loginCourier.setLogin("misters");
        loginCourier.setPassword("1454");
        Response response = given ()
                .header("Content-type", "application/json")
                .body(loginCourier)
                .when()
                .post(Url.LOGIN_COURIER);
        courierId = response.then().extract().path("id");

        return response;
    }


    @Step ("Проверка сообщения при авторизации несуществующего курьера")
    public  void checkMessageCourierNotFound (Response response) {
        response.then().assertThat().body("message", equalTo("Учетная запись не найдена"));
    }


    @Step ("Проверка статуса при авторизации несуществующего курьера")
    public  void checkStatusCourierNotFound (Response response) {
        response.then().statusCode(SC_NOT_FOUND);
    }

    @Step("Удаление курьера DELETE /api/v1/courier/")
    public void deleteCourier () {
        given()
                .header("Content-type", "application/json")
                .delete(Url.DELETE_COURIER + courierId);

    }


    @Test
    public void testCourierAuth() {

       Response response = courierAuth();
        checkStatus(response);
       checkMessage(response);

    }

    @Test
    public void testCourierAuthWithoutLogin () {

        Response response = courierAuthWithoutLogin();
        checkStatusWithoutLoginOrPassword(response);
        checkMessageWithoutLoginOrPassword(response);

    }

    @Test
    public void testCourierAuthWithoutPassword () {

        Response response = courierAuthWithoutPassword();
        checkStatusWithoutLoginOrPassword(response);
        checkMessageWithoutLoginOrPassword(response);

    }

    @Test
    public void testCourierNotFoundLogin () {
        Response response = courierNotFoundLogin();
        checkStatusCourierNotFound(response);
        checkMessageCourierNotFound(response);


    }

    @Test
    public void testCourierNotFoundPassword () {
        Response response = courierNotFoundPassword();
        checkStatusCourierNotFound(response);
        checkMessageCourierNotFound(response);

    }



    @After
    public void tearDown() {
        if(courierId != null) {
            deleteCourier();
        }
    }
}
