package pageobjects;

import models.Record;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import utils.ui.ScrollingUtil;

import java.util.ArrayList;
import java.util.List;

public class DataTable extends AbstractPage {

    private static final Logger logger = LogManager.getLogger(DataTable.class);

    @FindBy(xpath = "//tr[@class='row']")
    private List<WebElement> recordsRows;
    @FindBy(className = "header")
    private WebElement header;

    private static final String COMPANY_NAME_BY_INDEX_XPATH = "//tr[@class='row'][%s]/td[1]";
    private static final String TICKER_BY_INDEX_XPATH = "//tr[@class='row'][%s]/td[2]";
    private static final String COB_DATE_BY_INDEX_XPATH = "//tr[@class='row'][%s]/td[3]";
    private static final String STOCK_PRICE_BY_INDEX_XPATH = "//tr[@class='row'][%s]/td[4]";
    private static final String MARKET_CAP_BY_INDEX_XPATH = "//tr[@class='row'][%s]/td[5]";

    public DataTable(WebDriver driver) {
        super(driver);
    }

    public List<Record> collectAllRecords() {
        int count = getAllRecordsCount();
        logger.info("Found {} records in the data table", count);
        List<Record> records = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            String companyName = driver.findElement(By.xpath(String.format(COMPANY_NAME_BY_INDEX_XPATH, i))).getText();
            logger.info("{}. Collecting data for the record: {} ", i, companyName);
            Record currentRecord = Record.builder()
                    .companyName(companyName)
                    .ticker(driver.findElement(By.xpath(String.format(TICKER_BY_INDEX_XPATH, i))).getText())
                    .cobDate(driver.findElement(By.xpath(String.format(COB_DATE_BY_INDEX_XPATH, i))).getText())
                    .stockPrice(driver.findElement(By.xpath(String.format(STOCK_PRICE_BY_INDEX_XPATH, i))).getText())
                    .marketCap(driver.findElement(By.xpath(String.format(MARKET_CAP_BY_INDEX_XPATH, i))).getText())
                    .build();
            records.add(currentRecord);
        }
        logger.info("Collected {} records from the data table", records.size());
        return records;
    }

    private int getAllRecordsCount() {
        return getWaiter().waitForElementsPresent(recordsRows).size();
    }

    public void scrollToTheBottomOfThePage() {
        ScrollingUtil.scrollDownToTheBottom(driver);
    }

    public void scrollUpToHeader() {
        ScrollingUtil.scrollUpToElement(driver, header);
    }
}
