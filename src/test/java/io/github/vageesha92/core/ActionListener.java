package io.github.vageesha92.core;

import io.qameta.allure.Allure;
import io.qameta.allure.model.Status;
import org.openqa.selenium.support.events.WebDriverListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ActionListener implements WebDriverListener {

    private static final Logger LOG = LoggerFactory.getLogger(ActionListener.class);

    private final List<String> actions = new ArrayList<>();

    private long startTime;

    @Override
    public void beforeAnyCall(Object target, Method method, Object[] args) {
        startTime = System.nanoTime();
    }

    @Override
    public void afterAnyCall(Object target, Method method, Object[] args, Object result) {
        record(method, args, "OK");
    }

    // A call that throws does not get afterAnyCall, so failed calls are recorded here.
    @Override
    public void onError(Object target, Method method, Object[] args, InvocationTargetException e) {
        record(method, args, "ERROR " + e.getCause().getClass().getSimpleName());
    }

    private void record(Method method, Object[] args, String outcome) {
        if (method.getName().equals("getScreenshotAs")) {
            return; // a screenshot is not a test step
        }
        long ms = (System.nanoTime() - startTime) / 1_000_000;
        // sendKeys text can be a password, so it is never logged
        String what = method.getName().equals("sendKeys") ? "***" : Arrays.deepToString(args);
        String line = method.getName() + " " + what + " " + ms + " ms " + outcome;
        LOG.info(line);
        actions.add(line);
        // failed calls show as BROKEN steps in Allure
        Allure.step(line, outcome.equals("OK") ? Status.PASSED : Status.BROKEN);
    }

    public List<String> lastActions() {
        return List.copyOf(actions);
    }
}
