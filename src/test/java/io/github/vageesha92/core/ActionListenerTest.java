package io.github.vageesha92.core;

import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

import static org.testng.Assert.assertFalse;

public class ActionListenerTest {

    @Test
    public void sendKeysTextIsNotLogged() throws Exception {
        ActionListener listener = new ActionListener();
        Method sendKeys = WebElement.class.getMethod("sendKeys", CharSequence[].class);
        Object[] args = {new CharSequence[] {"demo-password"}};

        listener.beforeAnyCall(this, sendKeys, args);
        listener.afterAnyCall(this, sendKeys, args, null);

        assertFalse(listener.lastActions().get(0).contains("demo-password"),
                "the password was logged: " + listener.lastActions());
    }
}
