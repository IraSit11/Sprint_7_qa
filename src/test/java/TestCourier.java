import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;


public class TestCourier {
    private Integer courierId;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";

    }

    @Step("Создание курьера   Post /api/v1/courier")
    public Response createCourier (Courier courier) {

        Response response =
                given ()
                        .header("Content-type", "application/json")
                        .body(courier)
                        .when()
                        .post("/api/v1/courier");
              return response;
    }

    @Step("Авторизация курьера для получения ID")
    public Response loginAndGetId() {
        Response response = given()
                .header("Content-type", "application/json")
                .body("{\"login\": \"master\", \"password\": \"1234\"}")
                .when()
                .post("/api/v1/courier/login");
        courierId = response.then().extract().path("id");
        return response;

    }


    @Step ("Проверка сообщения при успешном создании курьера")
    public  void checkMessage (Response response) {
        response.then().assertThat().body("ok",equalTo(true));
    }

    @Step ("Проверка статуса при успешном создании курьера")
    public  void checkStatus (Response response) {
        response.then().statusCode(201);
    }

    @Step ("Проверка сообщения при создании курьера без обязательного поля")
    public  void checkMessageWithoutField (Response response) {
        response.then().assertThat().body("message",equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Step ("Проверка статуса при создании курьера без обязательного поля")
    public  void checkStatusWithoutField (Response response) {
        response.then().statusCode(400);
    }

    @Test
    public void testCourierCreate (){
        Courier courier = new Courier("master", "1234", "Ivan");

        Response response = createCourier(courier);
        checkMessage(response);
        checkStatus(response);
        loginAndGetId();
        }


    @Test
    public void testCourierCreateWithRepetition (){
        Courier courier = new Courier("master", "1234", "Ivan");

        Response response = createCourier(courier);
        checkMessage(response);
        checkStatus(response);
        loginAndGetId();

        Response responseDouble = createCourier(courier);
        responseDouble.then().statusCode(409);
        responseDouble.then().assertThat().body("message",equalTo("Этот логин уже используется"));

    }

   @Test
   public void testCourierCreateWithoutLogin () {
       Courier courier = new Courier("", "1234", "Ivan");
       Response response = createCourier(courier);
       checkMessageWithoutField(response);
       checkStatusWithoutField(response);
       loginAndGetId();
   }

    @Test
    public void testCourierCreateWithoutPassword () {
        Courier courier = new Courier("master", "", "Ivan");
        Response response = createCourier(courier);
        checkMessageWithoutField(response);
        checkStatusWithoutField(response);
        loginAndGetId();
    }

    @Test
    public void testCourierCreateWithoutFirstName () {
        Courier courier = new Courier("master", "1234", "");
        Response response = createCourier(courier);
        checkMessage(response);
        checkStatus(response);
        loginAndGetId();

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
