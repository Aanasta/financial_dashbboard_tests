package test;

import models.Record;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pageObjects.DashboardHeader;
import pageObjects.DataTable;

import java.util.List;

import static helpers.DatasetComparisonHelper.areDatasetsDifferent;

public class CompareTablesTest extends TestBase {

    private DashboardHeader header;
    private DataTable table;

    @Test
    public void compareDataTablesTest() {
        SoftAssert softly = new SoftAssert();

        DashboardHeader header = new DashboardHeader(driver);
        header.open("https://ui-automation-app.web.app/");

        Assert.assertTrue(header.isTableOneTabDisplayed(), "Table 1 tab is not displayed in the header");
        Assert.assertTrue(header.isTableTwoTabDisplayed(), "Table 2 tab is not displayed in the header");

        DataTable tableOne = header.openTableOne();
        tableOne.scrollToTheBottomOfThePage();
        List<Record> recordsOne = tableOne.collectAllRecords();
        tableOne.scrollUpToHeader();

        DataTable tableTwo = header.openTableTwo();
        tableTwo.scrollToTheBottomOfThePage();
        List<Record> recordsTwo = tableTwo.collectAllRecords();

        int recordsOneSize = recordsOne.size();
        int recordsTwoSize = recordsTwo.size();
        String discrepancies = areDatasetsDifferent(recordsOne, recordsTwo);
        softly.assertEquals(recordsOne.size(), recordsTwo.size(), String.format("Records count mismatch: " +
                "Table 1: %s records. Table 2: %s records.", recordsOneSize, recordsTwoSize));
        softly.assertTrue(discrepancies.isEmpty(), discrepancies);
        softly.assertAll();
    }
}
