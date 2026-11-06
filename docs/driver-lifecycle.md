# How the driver works

UI tests run in parallel, so each test needs its own browser.

## In short

- I create the driver in `@BeforeMethod` (`BaseUiTest`) and quit it in `@AfterMethod`.
- Tests get their driver with `DriverManager.getDriver()`.
- I never keep the driver in a field or pass it to another thread.

## Host vs container: why the browser uses a different URL

- `Config` reads `/config/<env>.properties` (`env` defaults to `local`). Any key can be overridden with `-D`.
- The test JVM runs on the host, so it uses `localhost`:
  - `api.baseUri` = `http://localhost:8080/...`
  - `grid.url` = `http://localhost:4444`
- The browser runs inside a Grid container, where `localhost` is the container itself. So it uses the Compose
  service name:
  - `ui.baseUrl` = `http://parabank:8080/parabank`

## Threads

- `testng-ui.xml` runs the same tests in two blocks, both with `parallel="methods"`:
  - UI Chrome, `thread-count="2"`
  - UI Firefox, `thread-count="1"`
- `BaseUiTest` prints the thread name at each step:

```
[thread] BeforeSuite on main
[thread] BeforeMethod CHROME on TestNG-test-UI Chrome-1
[thread] AfterMethod on TestNG-test-UI Chrome-1
[thread] AfterSuite on main
```

- `@BeforeMethod`, the test and `@AfterMethod` run on the same thread, so a `ThreadLocal` is enough to give each
  test its own driver.
- `@BeforeSuite` and `@AfterSuite` run on `main`, so a driver made there would be no use to any test.
- TestNG makes one object per test class, shared by all its threads. A driver in a field would be shared too.

## Why quit and remove

The pool reuses threads, and a `ThreadLocal` value stays until something removes it. So `DriverManager.quitDriver()`:

```java
try {
    ...
    session.driver().quit();   // 1. close the browser and free the Grid slot
} finally {
    SESSION.remove();          // 2. clear this thread's value
}
```

- `quit()` first: `remove()` alone leaves the browser open, holding a Grid slot until the node timeout (300 s).
- `remove()` in `finally`: it runs even if `quit()` fails, so the next test on this thread starts clean.
- `BaseUiTest.startBrowser` fails straight away if the thread still has a driver, so a broken teardown shows up
  in the right test.
- `DriverFactory.create` quits the session if setup fails after the session was created.

## Threads vs Grid slots

- Chrome node: 2 slots (`SE_NODE_MAX_SESSIONS=2`). Firefox node: 1 slot. That's why the thread counts are 2 and 1.
- More threads than slots doesn't fail at once. The extra requests wait in the hub queue, and time out if they
  wait too long.
- What I measured with four tests on my laptop (4 CPUs, warm containers):

| Chrome slots | Threads | Suite time |
|---|---|---|
| 1 | 3 | 21.44 s |
| 2 | 2 | 18.37 s |
| 3 | 3 | 12.73 s |

- 3 slots was fastest, but 2 is enough to prove the tests are safe in parallel, and the laptop has little memory.
- The first run after a container starts is slower, so I ignore it when measuring.

## Checks

- `hasDriver()` check in `BaseUiTest.startBrowser` - a test never starts with another test's driver
- `ThreadIsolationTest` - a driver only works on its own thread (`ThreadGuard.protect` in `DriverFactory` makes
  another thread fail with a `WebDriverException`)
- Open sessions during a run:

```bash
curl -s localhost:4444/status | jq '[.value.nodes[].slots[] | select(.session != null) | .stereotype.browserName]'
```

An empty list after the run means nothing was left open.
