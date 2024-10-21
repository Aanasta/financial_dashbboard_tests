package pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class DashboardHeader extends AbstractPage {

    @FindBy(xpath = "//button[.='Table 1']")
    private WebElement tableOneButton;
    @FindBy(xpath = "//button[.='Table 2']")
    private WebElement tableTwoButton;

    public DashboardHeader(WebDriver driver) {
        super(driver);
    }

    public boolean isTableOneTabDisplayed() {
        return getWaiter().waitForElementToBePresent(tableOneButton).isDisplayed();
    }

    public boolean isTableTwoTabDisplayed() {
        return getWaiter().waitForElementToBePresent(tableTwoButton).isDisplayed();
    }

    public DataTable openTableOne() {
        return openDataTable(tableOneButton);
    }

    public DataTable openTableTwo() {
        return openDataTable(tableTwoButton);
    }

    private DataTable openDataTable(WebElement button) {
        getWaiter().waitForElementToBePresent(button).click();
        return new DataTable(driver);
    }
}
