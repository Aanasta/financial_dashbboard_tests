package pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import uiUtils.WaiterUtil;

public abstract class AbstractPage {

    private static final String BASE_URL = "https://ui-automation-app.web.app/";

    protected WebDriver driver;

    public AbstractPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public WaiterUtil getWaiter() {
        return new WaiterUtil(this.driver);
    }

    public void openApp() {
        driver.get(BASE_URL);
    }
}
