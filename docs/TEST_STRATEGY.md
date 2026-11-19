# Test strategy

## Test scope: flows covered

- Register a new customer and log in; a bad login shows the right error
- Accounts Overview: the total matches the sum of the accounts
- Open an account and transfer money, checked in the UI, through the API and in the database
- Upload a statement and check the transaction rows
- Extras page: hover menu, reordering payees with the mouse, chat inside an iframe
- A second window and a new tab on ParaBank's pages

Not covered: loans, bill pay and the admin pages.

## Where each check goes

I test each thing at the lowest level where it can be checked.

- UI tests use Selenium on the Grid. They check what the user sees and does on the pages (`ui/`, `ui/pages/`).
- API tests use Rest Assured. They check the status code, the JSON fields & the schema (`api/`).
- Database checks use JDBC. They check the rows saved after a UI action, for example after a transfer.
  `TransferFundsTest` and `StatementUploadTest` use them.

Each UI test runs once in Chrome, then once in Firefox.
That is 45 tests per browser and 90 in total.

## What I don't automate

- Performance. One ParaBank container on a laptop does not tell anything about real bank servers.
- Security. The API has no login at all, so I listed it in the README under Known limits.
- Visual layout. The tests check text & state, not pixels.
- Edge, Safari and mobile browsers.

## Test data

- API tests reset ParaBank to its demo data before the first test, using `POST /initializeDB`.
  They use the demo customer john (12212).
- UI tests that log in or move money register a new user first.
  The username is `v` + the current time in milliseconds, from `RegisterPage.uniqueUsername()`.
- `AccountsErrorTest` logs in as john but does not change any data.
  It fakes the server error only in its own browser.

## Where it runs

- Locally on my laptop, with Docker and JDK 21. I start the stack with `docker compose up -d --wait`.
- On CI in GitHub Actions, on `ubuntu-24.04` with Temurin JDK 21. It starts the stack with the same command.

Both use the same `docker-compose.yml`.
CI runs on every push to `main`, on pull requests, and when I start it by hand.
The URLs for each environment are in `src/test/resources/config/`, and `local.properties` is the default.

## Known risks

- Same username twice. Chrome runs 2 threads, so two registrations in the same millisecond would get the same name.
  It has not happened in a run yet.
- Shared data. The API reset changes ParaBank's data, so the API and UI suites should not run at the same time.
- Image updates. ParaBank is pinned by digest and the Selenium images by a dated tag (`4.49.0-20260909`).
  So nothing changes until I change `docker-compose.yml`.

## Flaky tests

- I don't use retries, because a retry hides the first failure.
- When a test fails, I first look at its Allure result: the screenshot, the last actions & the browser console.
- Then I fix the cause, for example a wait that is too short or data that another test changed.
- If the cause is not clear yet, I take the test out of the suite with a note until it is fixed.
- Known one: `accountMenuOpensOnHover` failed once on CI in Chrome (the menu was closed in the screenshot)
  and once in a local run. It passes on the next run. I have not found the cause yet.
