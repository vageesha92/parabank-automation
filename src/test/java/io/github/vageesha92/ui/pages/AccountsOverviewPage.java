package io.github.vageesha92.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AccountsOverviewPage extends BasePage {

    private static final By TITLE = By.cssSelector("#showOverview h1.title");
    // data rows have an account link, the Total row does not
    private static final By ACCOUNT_ROWS = By.cssSelector("#accountTable tbody tr:has(td a)");

    private static final By TOTAL_CELL = By.xpath("//table[@id='accountTable']//tr[td/b[text()='Total']]/td[2]");

    public record AccountRow(String id, BigDecimal balance) { }

    public AccountsOverviewPage(WebDriver driver) {
        super(driver);
    }

    public String title() {
        return text(TITLE);
    }

    public List<AccountRow> accounts() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(TOTAL_CELL)); // the Total row comes last
        List<AccountRow> rows = new ArrayList<>();
        List<WebElement> accountRows = driver.findElements(ACCOUNT_ROWS);
        for (WebElement row : accountRows) {
            List<WebElement> cells = row.findElements(By.tagName("td"));

            // cell 0 = account id, cell 1 = balance
            String id = cells.get(0).getText().trim();
            BigDecimal balance = money(cells.get(1).getText().trim());

            rows.add(new AccountRow(id, balance));
        }
        return rows;
    }

    static BigDecimal money(String shown) {
        return new BigDecimal(shown.replace("$", "").replace(",", ""));
    }
}
