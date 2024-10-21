package utils.ui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import java.time.Duration;

public class ScrollingUtil {

    private static final Logger logger = LogManager.getLogger(ScrollingUtil.class);

    private static final int MAX_SCROLL_COUNTS = 10;
    private static final long SCROLL_WAIT_TIME = 10;
    private static final long POLLING_INTERVAL = 1;

    public static void scrollUpToElement(WebDriver driver, WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true);", element);
        logger.info("Scrolling the app up");
    }

    public static void scrollDownToTheBottom(WebDriver driver) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        long lastHeight = getHeight(js);
        int scrollCounts = 1;
        Wait<WebDriver> wait = createFluentWait(driver);

        while (scrollCounts <= MAX_SCROLL_COUNTS) {
            logger.info("Scrolling the app one screen down. Attempt {}", scrollCounts);
            scrollToBottom(js);
            if (waitForHeightChange(wait, js, lastHeight)) {
                lastHeight = getHeight(js);
            } else {
                logger.info("Reached the bottom of the page or height did not change. Attempt: {}", scrollCounts);
                break;
            }
            scrollCounts++;
        }
    }

    private static long getHeight(JavascriptExecutor js) {
        return (Long) js.executeScript("return document.body.scrollHeight");
    }

    private static void scrollToBottom(JavascriptExecutor js) {
        js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
    }

    private static boolean waitForHeightChange(Wait<WebDriver> wait, JavascriptExecutor js, long lastHeight) {
        try {
            wait.until(d -> {
                long newHeight = getHeight(js);
                return newHeight > lastHeight;
            });
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    private static Wait<WebDriver> createFluentWait(WebDriver driver) {
        return new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(SCROLL_WAIT_TIME))
                .pollingEvery(Duration.ofSeconds(POLLING_INTERVAL))
                .ignoring(NoSuchElementException.class);
    }
}
