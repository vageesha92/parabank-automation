package io.github.vageesha92.api;

import io.github.vageesha92.core.Config;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.LogConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public final class ApiSpec {

    private ApiSpec() {
        // utility class
    }

    public static RequestSpecification bank() {
        return new RequestSpecBuilder()
                .setBaseUri(Config.get().apiBaseUri())
                .setAccept(ContentType.JSON)               // ParaBank returns XML unless we ask for JSON
                .setConfig(RestAssuredConfig.config().logConfig(
                        LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails()))
                .build();
    }
}
