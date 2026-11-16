package io.github.vageesha92.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class RegisterPage extends BasePage {

    private static final By SUBMIT = By.cssSelector("#customerForm input[type='submit']");
    private static final By WELCOME = By.cssSelector("#rightPanel h1.title");

    public RegisterPage(WebDriver driver) {
        super(driver);
    }

    public RegisterPage open(String baseUrl) {
        driver.get(baseUrl + "/register.htm");
        visible(field("customer.firstName"));

        return this;
    }

    public String register(String username, String password) {
        fillAll(username, password);
        click(SUBMIT);
        return text(WELCOME);
    }

    public RegisterPage submitWithEmpty(String path) {
        fillAll(uniqueUsername(), "secret1");
        visible(field(path)).clear();
        click(SUBMIT);
        return this;
    }

    // Spring puts the error in <span id="<path>.errors">
    public String errorFor(String path) {
        return text(By.id(path + ".errors"));
    }

    private void fillAll(String username, String password) {
        type(field("customer.firstName"), "Vageesha");
        type(field("customer.lastName"), "Test");
        type(field("customer.address.street"), "1 Main St");
        type(field("customer.address.city"), "Markham");
        type(field("customer.address.state"), "ON");
        type(field("customer.address.zipCode"), "L3R0A1");
        type(field("customer.phoneNumber"), "4165550100");
        type(field("customer.ssn"), "123-45-6789");
        type(field("customer.username"), username);
        type(field("customer.password"), password);
        type(field("repeatedPassword"), password);
    }

    private static By field(String path) {
        return By.name(path);
    }

    public static String uniqueUsername() {
        return "v" + System.currentTimeMillis();
    }
}
