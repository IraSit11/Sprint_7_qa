import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;

public class TestCourierLogin {

    private Integer courierId;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
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
                        .post("/api/v1/courier");
        return response;
    }

    @Step("Успешная аутентификация курьера  post /api/v1/courier/login")
    public Response courierAuth () {
        Response response = given ()
                .header("Content-type", "application/json")
                .body("{\"login\": \"misters\", \"password\": \"1234\"}")
                .when()
                .post("/api/v1/courier/login");
        courierId = response.then().extract().path("id");
        return response;

    }

    @Step ("Проверка сообщения при успешном входе")
    public  void checkMessage (Response response) {
        response.then().assertThat().body("id", notNullValue());
    }

    @Step ("Проверка статуса при успешном входе")
    public  void checkStatus (Response response) {
        response.then().statusCode(200);
    }

    @Step("Авторизация курьера без логина ")
    public Response courierAuthWithoutLogin () {
        Response response = given ()
                .header("Content-type", "application/json")
                .body("{\"login\": \"\", \"password\": \"1234\"}")
                .when()
                .post("/api/v1/courier/login");
        courierId = response.then().extract().path("id");

        return response;
    }

    @Step("Авторизация курьера без пароля ")
    public Response courierAuthWithoutPassword () {
        Response response = given ()
                .header("Content-type", "application/json")
                .body("{\"login\": \"misters\", \"password\": \"\"}")
                .when()
                .post("/api/v1/courier/login");
        courierId = response.then().extract().path("id");

        return response;

    }
    @Step ("Проверка сообщения при невыполненном входе")
    public  void checkMessageWithoutLoginOrPassword (Response response) {
        response.then().assertThat().body("message", equalTo("Недостаточно данных для входа"));
    }

    @Step ("Проверка статуса при успешном входе")
    public  void checkStatusWithoutLoginOrPassword (Response response) {
        response.then().statusCode(400);
    }

    @Step("Авторизация несуществующего курьера ")
    public Response courierNotFound () {
        Response response = given ()
                .header("Content-type", "application/json")
                .body("{\"login\": \"hello\", \"password\": \"1454\"}")
                .when()
                .post("/api/v1/courier/login");
        courierId = response.then().extract().path("id");

        return response;
    }

    @Step ("Проверка сообщения при авторизации несуществующего курьера")
    public  void checkMessageCourierNotFound (Response response) {
        response.then().assertThat().body("message", equalTo("Учетная запись не найдена"));
    }

    @Step ("Проверка статуса при авторизации несуществующего курьера")
    public  void checkStatusCourierNotFound (Response response) {
        response.then().statusCode(404);
    }


    @Test
    public void testCourierAuth() {

       Response response = courierAuth();
       checkMessage(response);
       checkStatus(response);
    }

    @Test
    public void testCourierAuthWithoutLogin () {

        Response response = courierAuthWithoutLogin();
        checkMessageWithoutLoginOrPassword(response);
        checkStatusWithoutLoginOrPassword(response);
    }

    @Test
    public void testCourierAuthWithoutPassword () {

        Response response = courierAuthWithoutPassword();
        checkMessageWithoutLoginOrPassword(response);
        checkStatusWithoutLoginOrPassword(response);
    }

    @Test
    public void testCourierNotFound () {
        Response response = courierNotFound();
        checkMessageCourierNotFound(response);
        checkStatusCourierNotFound(response);

    }



    @After
    public void tearDown() {
        if(courierId != null) {
            given()
                    .header("Content-type", "application/json")
                    .delete("/api/v1/courier/" + courierId);

        }

    }
}
