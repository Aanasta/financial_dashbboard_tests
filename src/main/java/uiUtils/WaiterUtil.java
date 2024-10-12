package uiUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class WaiterUtil {

    private static final int STANDARD_TIMEOUT = 1;

    private WebDriver driver;

    public WaiterUtil(WebDriver driver) {
        this.driver = driver;
    }

    public WebElement waitForElementToBePresent(WebElement element) {
        WebDriverWait wait = new WebDriverWait(this.driver, Duration.ofSeconds(STANDARD_TIMEOUT));
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    public List<WebElement> waitForElementsPresent(List<WebElement> elementsList) {
        WebDriverWait wait = new WebDriverWait(this.driver, Duration.ofSeconds(STANDARD_TIMEOUT));
        return wait.until(ExpectedConditions.visibilityOfAllElements(elementsList));
    }
}
