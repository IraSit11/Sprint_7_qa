import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;

public class CourierApi {


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

    @Step("Авторизация курьера Post /api/v1/courier/login")
    public  Response loginAndGetId(Courier loginCourier) {

        Response response = given()
                .header("Content-type", "application/json")
                .body(loginCourier)
                .when()
                .post(Url.LOGIN_COURIER);
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

    @Step ("Проверка сообщения при успешном входе")
    public  void checkMessageLogin (Response response) {
        response.then().assertThat().body("id", notNullValue());
    }

    @Step ("Проверка статуса при успешном входе")
    public  void checkStatusLogin (Response response) {        response.then().statusCode(SC_OK);    }

    @Step("Авторизация курьера без логина ")
    public Response courierAuthWithoutLogin (Courier loginCourier) {

        Response response = given ()
                .header("Content-type", "application/json")
                .body(loginCourier)
                .when()
                .post(Url.LOGIN_COURIER);
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

    @Step("Авторизация курьера без пароля ")
    public Response courierAuthWithoutPassword (Courier loginCourier) {

        Response response = given ()
                .header("Content-type", "application/json")
                .body(loginCourier)
                .when()
                .post(Url.LOGIN_COURIER);
                return response;

    }


    @Step("Авторизация несуществующего курьера (неверный логин) ")
    public Response courierNotFoundLogin (Courier loginCourier) {

        Response response = given ()
                .header("Content-type", "application/json")
                .body(loginCourier)
                .when()
                .post(Url.LOGIN_COURIER);
                return response;
    }

    @Step("Авторизация несуществующего курьера (неверный пароль) ")
    public Response courierNotFoundPassword (Courier loginCourier) {

        Response response = given ()
                .header("Content-type", "application/json")
                .body(loginCourier)
                .when()
                .post(Url.LOGIN_COURIER);
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
    public void deleteCourier (Integer courierId) {
        given()
                .header("Content-type", "application/json")
                .delete(Url.DELETE_COURIER + courierId);

    }
}
