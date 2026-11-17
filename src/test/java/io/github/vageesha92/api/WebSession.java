package io.github.vageesha92.api;

import io.github.vageesha92.core.Config;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.session.SessionFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

public final class WebSession {

    private WebSession() {
        // utility class
    }

    static RequestSpecification web() {
        return new RequestSpecBuilder().setBaseUri(Config.get().apiBaseUri().resolve("/parabank")).build();
    }

    public static void register(String username, String password) {
        SessionFilter form = new SessionFilter();                     // GET and POST must use the same server session
        given(web()).filter(form).get("/register.htm").then().statusCode(200);
        given(web()).filter(form).contentType(ContentType.URLENC)
                .formParam("customer.firstName", "Vageesha").formParam("customer.lastName", "Test")
                .formParam("customer.address.street", "1 Main St").formParam("customer.address.city", "Markham")
                .formParam("customer.address.state", "ON").formParam("customer.address.zipCode", "L3R0A1")
                .formParam("customer.phoneNumber", "4165550100").formParam("customer.ssn", "123-45-6789")
                .formParam("customer.username", username).formParam("customer.password", password)
                .formParam("repeatedPassword", password)
                .post("/register.htm")
                .then().statusCode(200).body(containsString("created successfully"));
    }

    public static String logIn(String username, String password) {
        Response login = given(web()).redirects().follow(false).contentType(ContentType.URLENC)
                .formParam("username", username).formParam("password", password)
                .post("/login.htm");
        // a good login answers 302 to the accounts overview, not 200
        login.then().statusCode(302).header("Location", containsString("overview.htm"));
        return login.cookie("JSESSIONID");
    }
}
