package pageobjects;

import models.Record;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import utils.ui.ScrollingUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static helpers.DataConstants.*;

public class DataTable extends AbstractPage {

    private static final Logger logger = LogManager.getLogger(DataTable.class);

    private static final String COMPANY_NAME_BY_INDEX_XPATH = "//tr[@class='row'][%s]/td[1]";
    private static final String TICKER_BY_INDEX_XPATH = "//tr[@class='row'][%s]/td[2]";
    private static final String COB_DATE_BY_INDEX_XPATH = "//tr[@class='row'][%s]/td[3]";
    private static final String STOCK_PRICE_BY_INDEX_XPATH = "//tr[@class='row'][%s]/td[4]";
    private static final String MARKET_CAP_BY_INDEX_XPATH = "//tr[@class='row'][%s]/td[5]";

    @FindBy(xpath = "//tr[@class='row']")
    private List<WebElement> recordsRows;
    @FindBy(className = "header")
    private WebElement header;

    public DataTable(WebDriver driver) {
        super(driver);
    }

    public List<Record> collectAllRecords() {
        int count = getAllRecordsCount();
        logger.info("Found {} records in the data table", count);
        List<Record> records = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            String companyName = getCompanyNameByIndex(i);
            logger.info("{}. Collecting data for the record: {} ", i, companyName);
            Record currentRecord = buildRecordByIndex(i, companyName);
            records.add(currentRecord);
        }
        logger.info("Collected {} records from the data table", records.size());
        return records;
    }

    private Record buildRecordByIndex(int i, String companyName) {
        return Record.builder()
                .companyName(companyName)
                .ticker(getTextValueByIndex(TICKER_BY_INDEX_XPATH, i, TICKER_FIELD, companyName).orElse(""))
                .cobDate(getTextValueByIndex(COB_DATE_BY_INDEX_XPATH, i, COB_DATE_FIELD, companyName).orElse(""))
                .stockPrice(getStockPriceByIndex(i, companyName).orElse(0.0))
                .marketCap(getMarketCapByIndex(i, companyName).orElse(0))
                .build();
    }

    private String getCompanyNameByIndex(int i) {
        try {
            return driver.findElement(By.xpath(String.format(COMPANY_NAME_BY_INDEX_XPATH, i))).getText();
        } catch (NoSuchElementException e) {
            String composedName = String.format("Company %d", i);
            logger.error("Could not find Company Name for company with index {}. Composing name: {}", i, composedName);
            return composedName;
        }
    }

    private Optional<String> getTextValueByIndex(String locator, int i, String fieldName, String companyName) {
        try {
            String text = driver.findElement(By.xpath(String.format(locator, i))).getText();
            return Optional.ofNullable(text);
        } catch (NoSuchElementException e) {
            logger.error("Could not find {} for Company {}. Returning empty string", fieldName, companyName);
            return Optional.empty();
        }
    }

    private Optional<Double> getStockPriceByIndex(int i, String companyName) {
        return getNumericValueByIndex(STOCK_PRICE_BY_INDEX_XPATH, i, STOCK_PRICE_FIELD,  companyName);
    }

    private Optional<Integer> getMarketCapByIndex(int i, String companyName) {
        return getNumericValueByIndex(MARKET_CAP_BY_INDEX_XPATH, i, MARKET_CAP_FIELD, companyName)
                .map(Double::intValue);
    }

    private Optional<Double> getNumericValueByIndex(String locator, int i, String fieldName, String companyName) {
        try {
            String value = driver.findElement(By.xpath(String.format(locator, i))).getText();
            return Optional.of(Double.parseDouble(value));
        } catch (NoSuchElementException | NumberFormatException e) {
            logger.error("Could not collect {} for Company {}. Returning empty value", fieldName, companyName);
            return Optional.empty();
        }
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
